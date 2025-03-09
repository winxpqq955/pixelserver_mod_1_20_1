package me.ho3.classictweaks.mixin.classic_ender_pearl;

import net.minecraft.item.EnderPearlItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(EnderPearlItem.class)
public class EnderPearlItemMixin {
    @ModifyConstant(method = { "use" }, constant = { @Constant(intValue=20) })
    private int remove_cooldown(int original) {
        return 0;
    }
}
