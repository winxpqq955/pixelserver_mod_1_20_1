package me.ho3.classictweaks.mixin.fake_swing;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(net.minecraft.client.MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    @Nullable
    public ClientPlayerEntity player;
    @Shadow
    @Nullable
    public ClientWorld world;

    @Shadow
    @Nullable
    public HitResult crosshairTarget;

    @Shadow
    @Final
    public ParticleManager particleManager;
    @Shadow
    @Final
    public GameOptions options;

    @Inject(method = "tick", at = @At("TAIL"))
    private void animatium$applySwingWhilstMining(CallbackInfo ci) {
        if (this.player != null && !(this.player.getStackInHand(this.player.getActiveHand()).isEmpty() || !this.player.isUsingItem() || !this.options.attackKey.isPressed())) {
            applySwingWhilstMining(this.world, this.player, this.crosshairTarget, this.particleManager);
        }
    }

    private static void applySwingWhilstMining(World level, ClientPlayerEntity player, HitResult hitResult, ParticleManager particleEngine) {
        Hand activeHand = player.getActiveHand();
        Hand hand = Hand.MAIN_HAND;
        if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK && activeHand.equals(hand)) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;

            BlockPos blockPos = blockHitResult.getBlockPos();
            if (level != null && !level.getBlockState(blockPos).isAir()) {
                Direction direction = blockHitResult.getSide();
                particleEngine.addBlockBreakingParticles(blockPos, direction);
            }

            fakeHandSwing(player, hand);
        }
    }

    private static boolean isNotSwinging(PlayerEntity player) {
        return !player.handSwinging || player.handSwingTicks >= ((LivingEntityAccessor) player).getHandSwingDuration1() / 2 || player.handSwingTicks < 0;
    }

    private static void fakeHandSwing(ClientPlayerEntity player, Hand hand) {
        // Clientside NOTE fake swinging, doesn't send a packet
        if (isNotSwinging(player)) {
            player.handSwingTicks = -1;
            player.handSwinging = true;
            player.preferredHand = hand;
        }
    }
}
