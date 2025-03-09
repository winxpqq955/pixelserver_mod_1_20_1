package me.ho3.classictweaks.mixin.minimize_language;

import net.minecraft.client.resource.language.LanguageDefinition;
import net.minecraft.client.resource.metadata.LanguageResourceMetadata;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Mixin(LanguageResourceMetadata.class)
public class LanguageResourceMetadataMixin {
    @Unique
    private HashMap<String, LanguageDefinition> CACHED_MINIMIZED;

    @Inject(method = "definitions", at = @At("TAIL"), cancellable = true)
    public void definitions(CallbackInfoReturnable<Map<String, LanguageDefinition>> cir) {
        final var value = cir.getReturnValue();
        if (CACHED_MINIMIZED == null) {
            CACHED_MINIMIZED = new LinkedHashMap<>();
            classicTweaks$auto(value, CACHED_MINIMIZED, "en_us");
            classicTweaks$auto(value, CACHED_MINIMIZED, "zh_cn");
            classicTweaks$auto(value, CACHED_MINIMIZED, "zh_hk");
            classicTweaks$auto(value, CACHED_MINIMIZED, "zh_tw");
            classicTweaks$auto(value, CACHED_MINIMIZED, "lzh");
        }
        cir.setReturnValue(CACHED_MINIMIZED);
    }

    @Unique
    public void classicTweaks$auto(final Map<String, LanguageDefinition> value, final HashMap<String, LanguageDefinition> minimized, final String langCode) {
        minimized.put(langCode, value.get(langCode));
    }
}
