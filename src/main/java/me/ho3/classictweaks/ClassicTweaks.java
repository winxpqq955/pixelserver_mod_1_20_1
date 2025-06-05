package me.ho3.classictweaks;

import me.ho3.classictweaks.util.HttpUtil;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Mod("classictweaks")
public class ClassicTweaks {
    public static final Logger LOGGER = LogManager.getLogger("ClassicTweaks");
    public ClassicTweaks() {
        IEventBus MOD_BUS = FMLJavaModLoadingContext.get().getModEventBus();
        MOD_BUS.addListener(this::clientSetup);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        //Force enable alwaysSetupTerrainOffThread
        ForgeConfig.CLIENT.alwaysSetupTerrainOffThread.set(true);
        MinecraftForge.EVENT_BUS.register(this);
    }
}