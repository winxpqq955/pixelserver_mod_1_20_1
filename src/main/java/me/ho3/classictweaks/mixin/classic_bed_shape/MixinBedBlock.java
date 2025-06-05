package me.ho3.classictweaks.mixin.classic_bed_shape;

import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BedBlock.class, priority = 2000)
public abstract class MixinBedBlock extends HorizontalFacingBlock {

    protected MixinBedBlock(Settings settings) {
        super(settings);
    }

    @Unique
    private static VoxelShape CLASSIC_SHAPE;

    @Inject(at = @At("HEAD"), method = "getOutlineShape", cancellable = true)
    public void getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (CLASSIC_SHAPE == null)
            CLASSIC_SHAPE = VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, 0.5625, 1.0);
        cir.setReturnValue(CLASSIC_SHAPE);
    }
}