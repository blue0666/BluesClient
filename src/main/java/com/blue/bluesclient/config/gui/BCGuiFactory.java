package com.blue.bluesclient.config.gui;

import com.blue.bluesclient.BluesClient;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.DefaultGuiFactory;

public class BCGuiFactory extends DefaultGuiFactory {
    public BCGuiFactory() {
        super(BluesClient.MODID, BluesClient.NAME + " configs");
    }
    @Override
    public GuiScreen createConfigGui(GuiScreen parent) {
        BCConfigScreen gui = new BCConfigScreen();
        gui.setParent(parent);
        return gui;
    }
}
