package com.blue.bluesclient;

import com.blue.bluesclient.event.forge.DamageDisplayHandler;
import com.blue.bluesclient.event.forge.OutgoingDamageDisplayHandler;
import com.blue.bluesclient.event.forge.RLCombatHandler;
import com.blue.bluesclient.event.malilib.InitListener;
import com.blue.bluesclient.feat.allowflight.FlightHandler;
import com.blue.bluesclient.feat.everythingnunchaku.NunchakuConfigProvider;
import com.blue.bluesclient.feat.nohiddenflag.TooltipListener;
import fi.dy.masa.malilib.event.InitializationHandler;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = BluesClient.MODID,
        name = BluesClient.NAME,
        version = BluesClient.VERSION,
        clientSideOnly = true,
        acceptedMinecraftVersions = "[1.12.2]",
        dependencies = "required-after:malilib;",
        guiFactory = "com.blue.bluesclient.config.gui.BCGuiFactory"
)
public class BluesClient
{
    public static final String MODID = "bluesclient";
    public static final String NAME = "BluesClient";
    public static final String VERSION = "0.0.9";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        MinecraftForge.EVENT_BUS.register(TooltipListener.class);
        if(Loader.isModLoaded(ModReference.BETTERCOMBATMOD)){
            MinecraftForge.EVENT_BUS.register(RLCombatHandler.class);
        }
        if (Loader.isModLoaded(ModReference.FIRSTAID)) {
            MinecraftForge.EVENT_BUS.register(DamageDisplayHandler.class);
            MinecraftForge.EVENT_BUS.register(OutgoingDamageDisplayHandler.class);
        }
        MinecraftForge.EVENT_BUS.register(FlightHandler.class);
        InitializationHandler.getInstance()
                .registerInitializationHandler(new InitListener());
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        logger.info("BluesClient Mixin in");
    }
    @EventHandler
    public void postInit(FMLPostInitializationEvent event){
        NunchakuConfigProvider.init();
    }
}
