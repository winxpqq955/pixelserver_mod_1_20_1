package me.ho3.classictweaks.mixin.smooth_scrolling;

import me.wolfii.Config;
import me.wolfii.ScrollMath;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.EntryListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntryListWidget.class)
public abstract class EntryListWidgetMixin {
    @Shadow
    private double scrollAmount;
    @Shadow
    protected int bottom;

    @Shadow
    public abstract int getMaxScroll();

    @Unique
    private double classicTweaks$animationTimer = 0;
    @Unique
    private double classicTweaks$scrollStartVelocity = 0;
    @Unique
    private boolean classicTweaks$renderSmooth = false;


    @Inject(method = "render", at = @At("HEAD"))
    private void manipulateScrollAmount(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        classicTweaks$renderSmooth = true;
        classicTweaks$checkOutOfBounds(delta);

        if (Math.abs(ScrollMath.scrollbarVelocity(classicTweaks$animationTimer, classicTweaks$scrollStartVelocity)) < 1.0) return;
        classicTweaks$applyMotion(delta);
    }

    @Unique
    private void classicTweaks$applyMotion(float delta) {
        scrollAmount += ScrollMath.scrollbarVelocity(classicTweaks$animationTimer, classicTweaks$scrollStartVelocity) * delta;
        classicTweaks$animationTimer += delta * 10 / Config.animationDuration;
    }

    @Unique
    private void classicTweaks$checkOutOfBounds(float delta) {
        if (scrollAmount < 0) {
            scrollAmount += ScrollMath.pushBackStrength(Math.abs(scrollAmount), delta);
            if (scrollAmount > -0.2) scrollAmount = 0;
        }
        if (scrollAmount > getMaxScroll()) {
            scrollAmount -= ScrollMath.pushBackStrength(scrollAmount - getMaxScroll(), delta);
            if (scrollAmount < getMaxScroll() + 0.2) scrollAmount = getMaxScroll();
        }
    }

    @Redirect(method = "mouseScrolled", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/EntryListWidget;setScrollAmount(D)V"))
    private void setVelocity(EntryListWidget<?> instance, double amount) {
        if(!classicTweaks$renderSmooth) {
            instance.setScrollAmount(amount);
            return;
        }
        double diff = amount - scrollAmount;
        diff = Math.signum(diff) * Math.min(Math.abs(diff), 10);
        diff *= Config.scrollSpeed;
        if (Math.signum(diff) != Math.signum(classicTweaks$scrollStartVelocity)) diff *= 2.5d;
        classicTweaks$animationTimer *= 0.5;
        classicTweaks$scrollStartVelocity = ScrollMath.scrollbarVelocity(classicTweaks$animationTimer, classicTweaks$scrollStartVelocity) + diff;
        classicTweaks$animationTimer = 0;
    }

    @Redirect(method = "render", at=@At(value="INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V", ordinal = 1))
    private void modifyScrollbar(DrawContext instance, int x1, int y1, int x2, int y2, int color) {
        int height = y2 - y1;
        if(scrollAmount < 0) {
            y2 -= ScrollMath.dampenSquish(Math.abs(scrollAmount), height);
        }
        if(y1 + height > bottom) {
            y2 = bottom;
            y1 = bottom - height;
        }
        if (scrollAmount > getMaxScroll()) {
            int squish = ScrollMath.dampenSquish(scrollAmount - getMaxScroll(), height);
            y1 += squish;
        }
        instance.fill(x1, y1, x2, y2, color);
        instance.fill(x1, y1, x2 - 1, y2 - 1, -4144960);
    }

    @Redirect(method = "render", at=@At(value="INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V", ordinal = 2))
    private void cancelScrollbarForeground(DrawContext instance, int x1, int y1, int x2, int y2, int color) {}
}