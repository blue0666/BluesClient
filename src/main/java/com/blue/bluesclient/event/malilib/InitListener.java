package com.blue.bluesclient.event.malilib;

import com.blue.bluesclient.BluesClient;
import com.blue.bluesclient.config.BCConfig;
import com.blue.bluesclient.config.Callbacks;
import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import net.minecraft.client.Minecraft;

import java.util.logging.Logger;

public class InitListener implements IInitializationHandler {
    @Override
    public void registerModHandlers() {
        ConfigManager.getInstance()
                .registerConfigHandler(BluesClient.MODID, BCConfig.getInstance());
        InputEventHandler.getKeybindManager()
                .registerKeybindProvider(InputListener.INSTANCE);
        Callbacks.init(Minecraft.getMinecraft());
    }
}
