package me.ho3.classictweaks.mixin.classic_title_and_icon;

import com.mojang.blaze3d.systems.RenderSystem;
import me.ho3.classictweaks.ClassicTweaks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.Icons;
import net.minecraft.client.util.Window;
import net.minecraft.resource.InputSupplier;
import net.minecraft.resource.ResourcePack;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {


    @Shadow public abstract Window getWindow();

    @Inject(at = @At("HEAD"), method = "getWindowTitle", cancellable = true)
    private void getWindowTitle(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue("我的世界 1.20.1");
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;setIcon(Lnet/minecraft/resource/ResourcePack;Lnet/minecraft/client/util/Icons;)V"))
    public void setIcon(Window instance, ResourcePack _useless, Icons _useless2) throws IOException {
        RenderSystem.assertInInitPhase();
        final var _16x16 = new InputSupplier<InputStream>() {
            @Override
            public InputStream get() {
                return ClassicTweaks.class.getResourceAsStream("/_16x16.png");
            }
        };
        final var _32x32 = new InputSupplier<InputStream>() {
            @Override
            public InputStream get() {
                return ClassicTweaks.class.getResourceAsStream("/_32x32.png");
            }
        };
        List<InputSupplier<InputStream>> list = new ArrayList<>();
        list.add(_16x16);
        list.add(_32x32);
        List<ByteBuffer> list1 = new ArrayList<>(list.size());

        try {
            final var memorystack = MemoryStack.stackPush();
            try {
                GLFWImage.Buffer buffer = GLFWImage.malloc(list.size(), memorystack);
                for(int i = 0; i < list.size(); ++i) {
                    NativeImage nativeimage = NativeImage.read(list.get(i).get());
                    try {
                        ByteBuffer bytebuffer = MemoryUtil.memAlloc(nativeimage.getWidth() * nativeimage.getHeight() * 4);
                        list1.add(bytebuffer);
                        bytebuffer.asIntBuffer().put(nativeimage.copyPixelsRgba());
                        buffer.position(i);
                        buffer.width(nativeimage.getWidth());
                        buffer.height(nativeimage.getHeight());
                        buffer.pixels(bytebuffer);
                    } catch (Throwable var19) {
                        try {
                            nativeimage.close();
                        } catch (Throwable var18) {
                            var19.addSuppressed(var18);
                        }

                        throw var19;
                    }
                    nativeimage.close();
                }
                GLFW.glfwSetWindowIcon(instance.getHandle(), buffer.position(0));
            } catch (Throwable var20) {
                try {
                    memorystack.close();
                } catch (Throwable var17) {
                    var20.addSuppressed(var17);
                }
                throw var20;
            }
            memorystack.close();
        } finally {
            list1.forEach(MemoryUtil::memFree);
        }
    }
}
