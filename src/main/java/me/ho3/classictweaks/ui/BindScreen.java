package me.ho3.classictweaks.ui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class BindScreen extends Screen {
    private static final int PANEL_WIDTH = 300;
    private static final int PANEL_HEIGHT = 320;

    private TextFieldWidget usernameField;
    private ButtonWidget registerButton;

    public BindScreen() {
        super(Text.of("注册账号"));
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int centerY = this.height / 2;
        int panelLeft = centerX - PANEL_WIDTH / 2;
        int panelTop = centerY - PANEL_HEIGHT / 2;

        // 创建文本输入框
        usernameField = new TextFieldWidget(this.textRenderer, panelLeft + 30, panelTop + 150, PANEL_WIDTH - 60, 20, Text.of(""));
        usernameField.setPlaceholder(Text.of("(请输入1~8长度的中英文昵称名)"));


        // 创建按钮
        registerButton = ButtonWidget.builder(Text.of("进入服务器"), button -> {
            attemptRegister();
        }).dimensions(panelLeft + 30, panelTop + 200, PANEL_WIDTH - 60, 20).build();

        this.addDrawableChild(usernameField);
        this.addDrawableChild(registerButton);

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        final var str = "昵称注册";
        final var x = context.getScaledWindowWidth() / 2 - this.textRenderer.getWidth(str) * 3 / 2;
        final var y = context.getScaledWindowHeight() / 2 - 80;
        context.getMatrices().push();
        context.getMatrices().translate(x, y, 0);
        context.getMatrices().scale(3.0f, 3.0f, 0.0f);
        context.drawTextWithShadow(this.textRenderer, str, 0 , 0, 0xFFFFFF);
        context.getMatrices().scale(1.0f, 1.0f, 0.0f);
        context.getMatrices().pop();
    }

    private void attemptRegister() {
        String username = usernameField.getText();

        // 这里添加验证逻辑
        if (username.isEmpty()) {
            // 处理输入为空的情况
            return;
        }
        this.getMinecraft().setScreen(new MemeScreen(username));
        // 如果验证通过，执行注册逻辑
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) return false;
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
