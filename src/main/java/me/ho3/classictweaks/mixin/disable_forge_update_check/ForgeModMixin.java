package me.ho3.classictweaks.mixin.disable_forge_update_check;

import me.ho3.classictweaks.ClassicTweaks;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ForgeMod.class, remap = false)
public class ForgeModMixin {

    @Redirect(method = "preInit", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/VersionChecker;startVersionCheck()V"), remap = false)
    public void preInit(FMLCommonSetupEvent evt)
    {
        ClassicTweaks.LOGGER.info("Forge Version checker is disabled");
    }
}
