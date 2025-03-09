package me.ho3.classictweaks.mixin.blocking;

import me.ho3.classictweaks.blocking.SwordBlockingConfig;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.UseAction;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ShieldItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public abstract class MixinBipedEntityModel<T extends LivingEntity> {
    @Shadow protected abstract void positionRightArm(T entity);

    @Shadow protected abstract void positionLeftArm(T entity);

    @Shadow @Final public ModelPart rightArm;

    @Inject(method = "positionRightArm", at = @At("TAIL"))
    private void inj_positionRightArm(T entity, CallbackInfo ci) {
        if (entity.getActiveItem().getUseAction() == UseAction.BLOCK) {
            this.rightArm.pitch = this.rightArm.pitch * 0.5F - 0.5424779F;
        }
    }
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;animateArms(Lnet/minecraft/entity/LivingEntity;F)V", shift = At.Shift.BEFORE), method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V")
    private void swordblocking$setBlockingAngles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        if (livingEntity.getActiveItem().getUseAction() == UseAction.BLOCK) {
            this.positionRightArm(livingEntity);
        }

    }
}