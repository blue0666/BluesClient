package com.blue.bluesclient.config.gui;

import com.blue.bluesclient.BluesClient;
import com.blue.bluesclient.config.BCConfig;
import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.gui.ConfigGuiTabBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.interfaces.IConfigGuiTab;

public class BCConfigScreen extends GuiConfigsBase{
    private static final ConfigGuiTabBase VALUE =
            new ConfigGuiTabBase("值", 100, false, BCConfig.VALUE);
    private static final ConfigGuiTabBase HOTKEY =
            new ConfigGuiTabBase("热键", 200, true, BCConfig.KEYBIND);
    private static final ConfigGuiTabBase LIST =
            new ConfigGuiTabBase("列表", 200, false, BCConfig.LIST);
    private static final ConfigGuiTabBase MISCELLANEOUS =
            new ConfigGuiTabBase("杂项", 200, false, BCConfig.MISCELLANEOUS);
    private static final ImmutableList<IConfigGuiTab> TABS =
            ImmutableList.of(VALUE, LIST, HOTKEY, MISCELLANEOUS);

    private static IConfigGuiTab currentTab = VALUE;
    public BCConfigScreen() {
        super(10, 50, BluesClient.MODID, null, TABS, BluesClient.NAME + " Configs");
    }
    @Override
    public IConfigGuiTab getCurrentTab() {
        return currentTab;
    }
    @Override
    public void setCurrentTab(IConfigGuiTab tab) {
        currentTab = tab;
    }
}
