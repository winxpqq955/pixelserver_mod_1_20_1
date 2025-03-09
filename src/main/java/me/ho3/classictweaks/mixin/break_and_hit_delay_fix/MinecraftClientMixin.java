package me.ho3.classictweaks.mixin.break_and_hit_delay_fix;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow protected int attackCooldown;

    @Inject(method = "doAttack", at = @At("HEAD"))
    private void doAttack(CallbackInfoReturnable<Boolean> cir) {
        this.attackCooldown = 0;
    }

    @Inject(method = "handleBlockBreaking", at = @At("HEAD"))
    private void handleBlockBreaking(boolean breaking, CallbackInfo ci) {
        this.attackCooldown = 0;
    }
}
