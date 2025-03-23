package me.ho3.classictweaks.mixin.backport_screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.CubeMapRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMxixn {
    @Unique
    private static final CubeMapRenderer PANORAMA_CUBE_MAP = new CubeMapRenderer(new Identifier("textures/gui/title/background/panorama"));
    @Unique
    private static final RotatingCubeMapRenderer backgroundRenderer;
    @Unique
    private long lastPanoramaTickTime = Util.getMeasuringTimeMs();

    static {
        backgroundRenderer = new RotatingCubeMapRenderer(PANORAMA_CUBE_MAP);
    }

    @Unique
    protected float getPanoramaTickDelta() {
        long l = Util.getMeasuringTimeMs();
        long m = 50L;
        float f = (float)(l - this.lastPanoramaTickTime) / 50.0F;
        this.lastPanoramaTickTime = l;
        return f > 7.0F ? 0.5F : f;
    }

    @Redirect(method = "renderBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;renderBackgroundTexture(Lnet/minecraft/client/gui/DrawContext;)V"))
    public void renderBackground(Screen instance, DrawContext i) {
        backgroundRenderer.render(getPanoramaTickDelta(), 1F);
        RenderSystem.enableBlend();
    };
}
