package me.ho3.classictweaks.blocking;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.SwordItem;

public class SwordBlockingConfig {
    public static boolean isWeaponBlocking(LivingEntity entity) {
        return (entity.isUsingItem() && canWeaponBlock(entity));
    }

    public static boolean canWeaponBlock(LivingEntity entity) {
        return entity.getMainHandStack().getItem() instanceof SwordItem;
    }
}
