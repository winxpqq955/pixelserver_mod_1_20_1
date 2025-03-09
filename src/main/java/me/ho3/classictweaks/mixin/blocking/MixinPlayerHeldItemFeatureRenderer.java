package me.ho3.classictweaks.mixin.blocking;

import me.ho3.classictweaks.blocking.AdvancedBlockingRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.feature.PlayerHeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.UseAction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerHeldItemFeatureRenderer.class)
public class MixinPlayerHeldItemFeatureRenderer<T extends PlayerEntity, M extends EntityModel<T> & ModelWithArms & ModelWithHead> extends HeldItemFeatureRenderer<T, M> {
    @Shadow @Final private HeldItemRenderer playerHeldItemRenderer;

    public MixinPlayerHeldItemFeatureRenderer(FeatureRendererContext<T, M> context, HeldItemRenderer heldItemRenderer) {
        super(context, heldItemRenderer);
    }

    @Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
    protected void renderItem(LivingEntity entity, ItemStack stack, ModelTransformationMode transformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
//        if (!stack.isEmpty() && entity.getActiveItem() == stack) {
        if (entity.getActiveItem().getUseAction() == UseAction.BLOCK) {
            AdvancedBlockingRenderer.renderBlockingWithSword(this.playerHeldItemRenderer, this.getContextModel(), entity, stack, transformationMode, arm, matrices, vertexConsumers, light);
            ci.cancel();
        }

    }
}
