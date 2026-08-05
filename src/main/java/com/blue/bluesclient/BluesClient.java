package com.blue.bluesclient;

import com.blue.bluesclient.event.malilib.InitListener;
import com.blue.bluesclient.feat.nohiddenflag.TooltipListener;
import fi.dy.masa.malilib.event.InitializationHandler;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = BluesClient.MODID,
        name = BluesClient.NAME,
        version = BluesClient.VERSION,
        clientSideOnly = true,
        acceptedMinecraftVersions = "[1.12.2]",
        dependencies = "required-after:malilib;required-after:fermiumbooter;",
        guiFactory = "com.blue.bluesclient.config.gui.BCGuiFactory"
)
public class BluesClient
{
    public static final String MODID = "bluesclient";
    public static final String NAME = "BluesClient";
    public static final String VERSION = "0.0.5";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        MinecraftForge.EVENT_BUS.register(TooltipListener.class);
        InitializationHandler.getInstance()
                .registerInitializationHandler(new InitListener());
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        logger.info("BluesClient Mixin in");
    }
}
