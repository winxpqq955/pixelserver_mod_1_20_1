package me.ho3.classictweaks.mixin.disable_modern_fade;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraftforge.client.loading.ForgeLoadingOverlay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ForgeLoadingOverlay.class)
public class ForgeLoadingOverlayMixin {
    @Shadow(remap = false)
    private long fadeOutStart;

    @Shadow(remap = false)
    @Final
    private MinecraftClient minecraft;

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (this.fadeOutStart != -1) {
            this.minecraft.setOverlay(null);
        }
    }
}