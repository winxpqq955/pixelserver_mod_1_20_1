package me.ho3.classictweaks.mixin.pixel;

import me.ho3.classictweaks.ui.BindScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.realms.RealmsClient;
import net.minecraft.resource.ResourceReload;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftMixin {


    @Inject(method = "onInitFinished", at = @At(value = "HEAD"), cancellable = true)
    public void onInitFinished(RealmsClient realms, ResourceReload reload, RunArgs.QuickPlay quickPlay, CallbackInfo ci) {
       setOverlay(null);
       setScreen(new BindScreen());
       ci.cancel();
    }

    @Shadow public abstract void setOverlay(@Nullable Overlay overlay);

    @Shadow public abstract void setScreen(@Nullable Screen screen);
}
