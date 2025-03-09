package me.ho3.classictweaks.mixin.classic_pause_menu;

import me.ho3.classictweaks.ui.Classic_1_7_10_GameMenuScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Redirect(method = "openPauseMenu", at = @At(value = "NEW", target = "(Z)Lnet/minecraft/client/gui/screen/GameMenuScreen;"))
    public GameMenuScreen openPauseMenu(boolean showMenu) {
        return new Classic_1_7_10_GameMenuScreen(showMenu);
    }
}
