package me.ho3.classictweaks.ui;

import me.sunstorm.blaze.Animation;
import me.sunstorm.blaze.AnimationType;
import me.sunstorm.blaze.Animator;
import me.sunstorm.blaze.Eases;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class MemeScreen extends Screen {
    private final Animator animator = new Animator();
    private final Animation fade;

    protected MemeScreen() {
        super(Text.of("Meme"));
        fade = Animation.animation(Eases.LINEAR, AnimationType.bouncing(Eases.LINEAR), 0.1);
        animator.start(fade);
    }

    private String wtf = "";
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        animator.update(delta);
        var value = fade.value();
        if (value <= 0.1) value = 0.1;
        if (value == 1) {
            if (wtf.length() <= 3)
                wtf += ".";
            else
                wtf = "";
        }

        final var str = "认证中" + wtf;
        final var x = context.getScaledWindowWidth() / 2 - this.textRenderer.getWidth(str) * 4 / 2;
        final var y = context.getScaledWindowHeight() / 2 - 30;
        context.getMatrices().push();
        context.getMatrices().translate(x, y, 0);
        context.getMatrices().scale(4.0f, 4.0f, 0.0f);
        context.drawTextWithShadow(this.textRenderer, str, 0 , 0, new Color(255, 255, 255, 255).getRGB());
        context.getMatrices().pop();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) return false;
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
