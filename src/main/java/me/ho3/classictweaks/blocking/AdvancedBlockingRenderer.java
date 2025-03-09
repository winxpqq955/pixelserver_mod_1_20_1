package me.ho3.classictweaks.blocking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.render.model.json.Transformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;

public class AdvancedBlockingRenderer {

    public static void renderBlockingWithSword(HeldItemRenderer itemInHandRenderer, ModelWithArms model, LivingEntity entity, ItemStack stack, ModelTransformationMode transform, Arm arm, MatrixStack poseStack, VertexConsumerProvider multiBufferSource, int combinedLight) {
        // those transformations are directly ported from Minecraft 1.7, resulting in a pixel-perfect recreation of third-person sword blocking
        // a lot has changed since then (the whole model system has been rewritten twice in 1.8 and 1.9, and had major changes in 1.14 and 1.15),
        // so we reset everything vanilla does now, and apply every single step that was done in 1.7
        // (there were multiple classes and layers involved in 1.7, it is noted down below which class every transformation came from)
        // all this is done in code and not using some custom json model predicate so that every item is supported by default
        poseStack.push();
        model.setArmAngle(arm, poseStack);
        boolean leftHand = arm == Arm.LEFT;
        applyItemBlockingTransform(poseStack, leftHand);
        applyItemTransformInverse(entity, stack, transform, poseStack, leftHand);
        itemInHandRenderer.renderItem(entity, stack, transform, leftHand, poseStack, multiBufferSource, combinedLight);
        poseStack.pop();
    }

    private static void applyItemBlockingTransform(MatrixStack poseStack, boolean leftHand) {
        poseStack.translate((leftHand ? 1.0F : -1.0F) / 16.0F, 0.4375F, 0.0625F);
        // blocking
        poseStack.translate(leftHand ? -0.035F : 0.05F, leftHand ? 0.045F : 0.0F, leftHand ? -0.135F : -0.1F);
        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((leftHand ? -1.0F : 1.0F) * -50.0F));
        poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-10.0F));
        poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((leftHand ? -1.0F : 1.0F) * -60.0F));
        // old item layer
        poseStack.translate(0.0F, 0.1875F, 0.0F);
        // this differs from 1.7 as there was a negative y scale being used, which is not supported on Minecraft 1.16+
        // therefore rotations on X and Y all had to be flipped down the line (and one rotation on X by 180 degrees has been added)
        poseStack.scale(0.625F, 0.625F, 0.625F);
        poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));
        poseStack.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(-100.0F));
        poseStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(leftHand ? 35.0F : 45.0F));
        // old item renderer
        poseStack.translate(0.0F, -0.3F, 0.0F);
        poseStack.scale(1.5F, 1.5F, 1.5F);
        poseStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(50.0F));
        poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(335.0F));
        poseStack.translate(-0.9375F, -0.0625F, 0.0F);
        poseStack.translate(0.5F, 0.5F, 0.25F);
        poseStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(180.0F));
        poseStack.translate(0.0F, 0.0F, 0.28125F);
    }

    private static void applyItemTransformInverse(LivingEntity entity, ItemStack stack, ModelTransformationMode transform, MatrixStack poseStack, boolean leftHand) {
        // revert 1.8+ model changes, so we can work on a blank slate
        MinecraftClient minecraft = MinecraftClient.getInstance();
        BakedModel model = minecraft.getItemRenderer().getModel(stack, entity.getWorld(), entity, 0);
        applyTransformInverse(model.getTransformation().getTransformation(transform), leftHand, poseStack);
    }

    private static void applyTransformInverse(Transformation vec, boolean leftHand, MatrixStack matrixStackIn) {
        // this does the exact inverse of ItemTransform::apply which should be applied right after, so that in the end nothing has changed
        if (vec != Transformation.IDENTITY) {
            float angleX = vec.rotation.x();
            float angleY = leftHand ? -vec.rotation.y() : vec.rotation.y();
            float angleZ = leftHand ? -vec.rotation.z() : vec.rotation.z();
            Quaternionf quaternion = new Quaternionf().rotationXYZ(angleX * 0.017453292F, angleY * 0.017453292F, angleZ * 0.017453292F);
            quaternion.conjugate();
            matrixStackIn.scale(1.0F / vec.scale.x(), 1.0F / vec.scale.y(), 1.0F / vec.scale.z());
            matrixStackIn.multiply(quaternion);
            matrixStackIn.translate((leftHand ? -1.0F : 1.0F) * -vec.translation.x(), -vec.translation.y(), -vec.translation.z());
        }
    }
}
