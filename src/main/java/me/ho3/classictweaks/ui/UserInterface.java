package me.ho3.classictweaks.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInterface extends Screen {
    private final List<ModuleButton> moduleButtons = new ArrayList<>();
    private final List<Category> categories = new ArrayList<>();
    private Category selectedCategory = null;

    // UI dimensions
    private static final int HEADER_HEIGHT = 24;
    private static final int SIDEBAR_WIDTH = 110;

    // Colors
    private static final int BACKGROUND_COLOR = new Color(15, 15, 15, 180).getRGB();
    private static final int HEADER_COLOR = new Color(20, 20, 20, 220).getRGB();
    private static final int ACCENT_COLOR = new Color(33, 150, 243, 255).getRGB();
    private static final int ACCENT_HOVER_COLOR = new Color(66, 165, 245, 255).getRGB();
    private static final int MODULE_BG_COLOR = new Color(35, 35, 35, 200).getRGB();
    private static final int MODULE_ENABLED_COLOR = new Color(33, 150, 243, 100).getRGB();

    // Animation
    private final Map<String, Float> hoverAnimations = new HashMap<>();
    private final Map<String, Float> enabledAnimations = new HashMap<>();
    private int logoRotation = 0;

    // Blur shader
    private static final Identifier BLUR_SHADER = new Identifier("shaders/post/blur.json");
    private boolean blurEnabled = true;

    public UserInterface() {
        super(Text.of("ClassicTweaks"));
        initCategories();
    }

    @Override
    protected void init() {
        super.init();

        // Initialize animations for modules
        for (ModuleButton button : moduleButtons) {
            String buttonId = button.name + "_" + button.category.name;
            if (!hoverAnimations.containsKey(buttonId)) {
                hoverAnimations.put(buttonId, 0f);
                enabledAnimations.put(buttonId, button.enabled ? 1f : 0f);
            }
        }

        // Initialize animations for categories
        for (Category category : categories) {
            if (!hoverAnimations.containsKey(category.name)) {
                hoverAnimations.put(category.name, 0f);
            }
        }

        // Select first category by default
        if (!categories.isEmpty() && selectedCategory == null) {
            selectedCategory = categories.get(0);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);

        // Update animations
        updateAnimations(mouseX, mouseY, delta);

        // Draw blur background
        drawBlurredBackground(context);

        // Draw main panel
        roundedRect(context, SIDEBAR_WIDTH, HEADER_HEIGHT, width - 10, height - 10, 8, BACKGROUND_COLOR);

        // Draw header with gradient
        drawGradientRect(context, 0, 0, width, HEADER_HEIGHT,
                new Color(20, 20, 25, 255).getRGB(),
                new Color(25, 25, 30, 255).getRGB());

        // Draw logo and title
        logoRotation = (logoRotation + 1) % 360;
        context.getMatrices().push();
        context.getMatrices().translate(20, HEADER_HEIGHT/2.0f, 0);
        context.getMatrices().scale(0.7f, 0.7f, 1.0f);
        context.drawCenteredTextWithShadow(textRenderer, "CT", 0, -5, ACCENT_COLOR);
        context.getMatrices().pop();

        context.drawTextWithShadow(textRenderer, "ClassicTweaks", 35, HEADER_HEIGHT/2 - 4, 0xFFFFFFFF);

        // Draw sidebar with rounded corners
        roundedRect(context, 10, HEADER_HEIGHT + 10, SIDEBAR_WIDTH - 10, height - 20, 8,
                new Color(25, 25, 30, 220).getRGB());

        // Draw categories
        renderCategories(context, mouseX, mouseY);

        // Draw selected category content
        if (selectedCategory != null) {
            renderModules(context, mouseX, mouseY);
        }

        super.render(context, mouseX, mouseY, delta);
    }

    private void renderCategories(DrawContext context, int mouseX, int mouseY) {
        int buttonY = HEADER_HEIGHT + 25;

        for (Category category : categories) {
            boolean isSelected = category == selectedCategory;
            boolean isHovered = mouseX >= 15 && mouseX <= SIDEBAR_WIDTH - 15 &&
                    mouseY >= buttonY && mouseY <= buttonY + 28;

            // Update hover animation
            float hoverProgress = hoverAnimations.getOrDefault(category.name, 0f);

            // Background color with animation
            int bgColor = isSelected ? ACCENT_COLOR :
                    lerpColor(MODULE_BG_COLOR, new Color(45, 45, 50, 220).getRGB(), hoverProgress);

            // Draw button with rounded corners
            roundedRect(context, 15, buttonY, SIDEBAR_WIDTH - 15, buttonY + 28, 6, bgColor);

            // Draw category name
            int textColor = isSelected ? 0xFFFFFFFF : lerpColor(0xAAFFFFFF, 0xFFFFFFFF, hoverProgress);
            context.drawCenteredTextWithShadow(textRenderer, category.name,
                    SIDEBAR_WIDTH / 2 - 5, buttonY + 10, textColor);

            buttonY += 35;
        }
    }

    private void renderModules(DrawContext context, int mouseX, int mouseY) {
        context.drawTextWithShadow(textRenderer, selectedCategory.name,
                SIDEBAR_WIDTH + 15, HEADER_HEIGHT + 20, 0xFFFFFFFF);

        // Divider
        context.fill(SIDEBAR_WIDTH + 15, HEADER_HEIGHT + 35,
                width - 25, HEADER_HEIGHT + 36, new Color(100, 100, 100, 100).getRGB());

        int moduleY = HEADER_HEIGHT + 50;
        int moduleWidth = width - SIDEBAR_WIDTH - 40;

        for (ModuleButton module : moduleButtons) {
            if (module.category.equals(selectedCategory)) {
                String buttonId = module.name + "_" + module.category.name;
                boolean isHovered = mouseX >= SIDEBAR_WIDTH + 15 && mouseX <= width - 25 &&
                        mouseY >= moduleY && mouseY <= moduleY + 35;

                // Update hover animation
                float hoverProgress = hoverAnimations.getOrDefault(buttonId, 0f);
                float enabledProgress = enabledAnimations.getOrDefault(buttonId, 0f);

                // Calculate colors with animations
                int bgColor = lerpColor(MODULE_BG_COLOR,
                        new Color(45, 45, 50, 220).getRGB(), hoverProgress);

                // Draw button with rounded corners
                roundedRect(context, SIDEBAR_WIDTH + 15, moduleY, width - 25, moduleY + 35, 6, bgColor);

                // Draw enabled indicator
                if (enabledProgress > 0) {
                    int indicatorWidth = (int)(moduleWidth * enabledProgress);
                    roundedRect(context, SIDEBAR_WIDTH + 15, moduleY,
                            SIDEBAR_WIDTH + 15 + indicatorWidth, moduleY + 35, 6, MODULE_ENABLED_COLOR);
                }

                // Draw module name
                int textColor = lerpColor(0xAAFFFFFF, 0xFFFFFFFF, hoverProgress);
                context.drawTextWithShadow(textRenderer, module.name,
                        SIDEBAR_WIDTH + 25, moduleY + 13, textColor);

                moduleY += 45;
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Handle category clicks
        int buttonY = HEADER_HEIGHT + 25;
        for (Category category : categories) {
            if (mouseX >= 15 && mouseX <= SIDEBAR_WIDTH - 15 &&
                    mouseY >= buttonY && mouseY <= buttonY + 28) {
                selectedCategory = category;
                return true;
            }
            buttonY += 35;
        }

        // Handle module clicks
        if (selectedCategory != null) {
            int moduleY = HEADER_HEIGHT + 50;
            for (ModuleButton module : moduleButtons) {
                if (module.category.equals(selectedCategory)) {
                    if (mouseX >= SIDEBAR_WIDTH + 15 && mouseX <= width - 25 &&
                            mouseY >= moduleY && mouseY <= moduleY + 35) {
                        module.enabled = !module.enabled;
                        return true;
                    }
                    moduleY += 45;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void updateAnimations(int mouseX, int mouseY, float delta) {
        float animationSpeed = 0.15f * delta;

        // Update category animations
        int buttonY = HEADER_HEIGHT + 25;
        for (Category category : categories) {
            boolean isHovered = mouseX >= 15 && mouseX <= SIDEBAR_WIDTH - 15 &&
                    mouseY >= buttonY && mouseY <= buttonY + 28;

            float currentValue = hoverAnimations.getOrDefault(category.name, 0f);
            float targetValue = isHovered ? 1f : 0f;
            float newValue = MathHelper.lerp(animationSpeed, currentValue, targetValue);
            hoverAnimations.put(category.name, newValue);

            buttonY += 35;
        }

        // Update module animations
        int moduleY = HEADER_HEIGHT + 50;
        for (ModuleButton module : moduleButtons) {
            if (module.category.equals(selectedCategory)) {
                String buttonId = module.name + "_" + module.category.name;
                boolean isHovered = mouseX >= SIDEBAR_WIDTH + 15 && mouseX <= width - 25 &&
                        mouseY >= moduleY && mouseY <= moduleY + 35;

                // Hover animation
                float currentValue = hoverAnimations.getOrDefault(buttonId, 0f);
                float targetValue = isHovered ? 1f : 0f;
                float newValue = MathHelper.lerp(animationSpeed, currentValue, targetValue);
                hoverAnimations.put(buttonId, newValue);

                // Enabled animation
                float currentEnabled = enabledAnimations.getOrDefault(buttonId, 0f);
                float targetEnabled = module.enabled ? 1f : 0f;
                float newEnabled = MathHelper.lerp(animationSpeed, currentEnabled, targetEnabled);
                enabledAnimations.put(buttonId, newEnabled);

                moduleY += 45;
            }
        }
    }

    private void drawBlurredBackground(DrawContext context) {
        // Semi-transparent overlay for the entire screen
        context.fill(0, 0, width, height, new Color(0, 0, 0, 120).getRGB());
    }

    private void roundedRect(DrawContext context, int left, int top, int right, int bottom, int radius, int color) {
        // Background
        context.fill(left + radius, top, right - radius, bottom, color);
        context.fill(left, top + radius, left + radius, bottom - radius, color);
        context.fill(right - radius, top + radius, right, bottom - radius, color);

        // Corners
        drawCircleQuarter(context, left + radius, top + radius, radius, 180, color);
        drawCircleQuarter(context, right - radius, top + radius, radius, 270, color);
        drawCircleQuarter(context, right - radius, bottom - radius, radius, 0, color);
        drawCircleQuarter(context, left + radius, bottom - radius, radius, 90, color);
    }

    private void drawCircleQuarter(DrawContext context, int x, int y, int radius, int startAngle, int color) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                int distanceSq = dx * dx + dy * dy;
                if (distanceSq <= radius * radius && distanceSq >= (radius-1) * (radius-1)) {
                    // Check if in the correct quarter based on startAngle
                    boolean draw = false;
                    switch (startAngle) {
                        case 0: draw = dx >= 0 && dy >= 0; break;    // bottom-right
                        case 90: draw = dx <= 0 && dy >= 0; break;   // bottom-left
                        case 180: draw = dx <= 0 && dy <= 0; break;  // top-left
                        case 270: draw = dx >= 0 && dy <= 0; break;  // top-right
                    }

                    if (draw) {
                        context.fill(x + dx, y + dy, x + dx + 1, y + dy + 1, color);
                    }
                }
            }
        }
    }

    private void drawGradientRect(DrawContext context, int left, int top, int right, int bottom, int startColor, int endColor) {
        context.fillGradient(left, top, right, bottom, startColor, endColor);
    }

    private int lerpColor(int a, int b, float t) {
        float alpha1 = ((a >> 24) & 0xFF) / 255f;
        float red1 = ((a >> 16) & 0xFF) / 255f;
        float green1 = ((a >> 8) & 0xFF) / 255f;
        float blue1 = (a & 0xFF) / 255f;

        float alpha2 = ((b >> 24) & 0xFF) / 255f;
        float red2 = ((b >> 16) & 0xFF) / 255f;
        float green2 = ((b >> 8) & 0xFF) / 255f;
        float blue2 = (b & 0xFF) / 255f;

        float alpha = alpha1 + (alpha2 - alpha1) * t;
        float red = red1 + (red2 - red1) * t;
        float green = green1 + (green2 - green1) * t;
        float blue = blue1 + (blue2 - blue1) * t;

        int alphaInt = Math.round(alpha * 255f);
        int redInt = Math.round(red * 255f);
        int greenInt = Math.round(green * 255f);
        int blueInt = Math.round(blue * 255f);

        return (alphaInt << 24) | (redInt << 16) | (greenInt << 8) | blueInt;
    }

    private void initCategories() {
        // Define categories
        Category general = new Category("General");
        Category hud = new Category("HUD");
        Category cosmetics = new Category("Cosmetics");
        Category performance = new Category("Performance");

        categories.add(general);
        categories.add(hud);
        categories.add(cosmetics);
        categories.add(performance);

        // Add modules
        moduleButtons.add(new ModuleButton("FPS Display", general, false));
        moduleButtons.add(new ModuleButton("Keystrokes", hud, false));
        moduleButtons.add(new ModuleButton("CPS Counter", hud, false));
        moduleButtons.add(new ModuleButton("Motion Blur", performance, false));
        moduleButtons.add(new ModuleButton("Cape", cosmetics, false));
        moduleButtons.add(new ModuleButton("Old Animations", cosmetics, false));
        moduleButtons.add(new ModuleButton("Memory Usage", hud, false));
        moduleButtons.add(new ModuleButton("Fullbright", general, false));
        moduleButtons.add(new ModuleButton("Item Physics", performance, false));
    }

    private static class Category {
        public final String name;

        public Category(String name) {
            this.name = name;
        }
    }

    private static class ModuleButton {
        public final String name;
        public final Category category;
        public boolean enabled;

        public ModuleButton(String name, Category category, boolean enabled) {
            this.name = name;
            this.category = category;
            this.enabled = enabled;
        }
    }
}