package me.ho3.classictweaks.mixin.classic_hurt_cam;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.player.PlayerEntity;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "getDamageTiltYaw", at = @At("HEAD"), cancellable = true)
    public void getDamageTiltYaw(CallbackInfoReturnable<Float> ci) {
        ci.setReturnValue(0.0f);
    }
}