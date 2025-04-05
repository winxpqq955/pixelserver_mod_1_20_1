package me.ho3.classictweaks.mixin.test;

import me.ho3.classictweaks.ui.UserInterface;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class testmixin {

    @Inject(method = "onKey", at = @At("TAIL"))
    public void test(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {

        if (key == GLFW.GLFW_KEY_RIGHT_SHIFT && action == GLFW.GLFW_PRESS) {
            //MinecraftClient.getInstance().setScreen(new UserInterface());
        }
    }
}
