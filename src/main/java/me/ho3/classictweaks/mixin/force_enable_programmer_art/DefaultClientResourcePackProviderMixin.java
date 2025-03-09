package me.ho3.classictweaks.mixin.force_enable_programmer_art;

import net.minecraft.client.resource.DefaultClientResourcePackProvider;
import net.minecraft.resource.*;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DefaultClientResourcePackProvider.class)
public class DefaultClientResourcePackProviderMixin {
    @Redirect(method = "create", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourcePackProfile;create(Ljava/lang/String;Lnet/minecraft/text/Text;ZLnet/minecraft/resource/ResourcePackProfile$PackFactory;Lnet/minecraft/resource/ResourceType;Lnet/minecraft/resource/ResourcePackProfile$InsertionPosition;Lnet/minecraft/resource/ResourcePackSource;)Lnet/minecraft/resource/ResourcePackProfile;"))
    protected ResourcePackProfile create(String pack$info, Text name, boolean displayName, ResourcePackProfile.PackFactory alwaysEnabled, ResourceType packFactory, ResourcePackProfile.InsertionPosition type, ResourcePackSource position) {
        if ("programmer_art".equals(pack$info)) displayName = true;
        return ResourcePackProfile.create(pack$info, name, displayName, alwaysEnabled, ResourceType.CLIENT_RESOURCES, ResourcePackProfile.InsertionPosition.TOP, ResourcePackSource.BUILTIN);
    }
}
