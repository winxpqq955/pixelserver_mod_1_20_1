package me.ho3.classictweaks.mixin.fuckoff_fishhook;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(FishingBobberEntity.class)
public class FishingBobberEntityRenderer {
    @Shadow @Nullable private Entity hookedEntity;

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        if (hookedEntity instanceof ClientPlayerEntity) {
            hookedEntity.remove(Entity.RemovalReason.DISCARDED);
        }
    }
}
