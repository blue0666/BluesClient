package com.blue.bluesclient.config.gui;

import com.blue.bluesclient.BluesClient;
import com.blue.bluesclient.ModReference;
import com.blue.bluesclient.config.BCConfig;
import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.gui.ConfigGuiTabBase;
import fi.dy.masa.malilib.config.options.IConfigBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.interfaces.IConfigGuiTab;

import java.util.ArrayList;
import java.util.List;

public class BCConfigScreen extends GuiConfigsBase {
    private static final ConfigGuiTabBase VALUE =
            new ConfigGuiTabBase("bluesclient.gui.tab.value", 100, false, buildValueTab());
    private static final ConfigGuiTabBase HOTKEY =
            new ConfigGuiTabBase("bluesclient.gui.tab.hotkey", 200, true, buildHotKeyTab());
    private static final ConfigGuiTabBase LIST =
            new ConfigGuiTabBase("bluesclient.gui.tab.list", 200, false, BCConfig.LIST);
    private static final ConfigGuiTabBase MISCELLANEOUS =
            new ConfigGuiTabBase("bluesclient.gui.tab.miscellaneous", 200, false, BCConfig.MISCELLANEOUS);
    private static final ConfigGuiTabBase DEBUG =
            new ConfigGuiTabBase("bluesclient.gui.tab.debug", 200, false, buildDebugTab());
    private static final ImmutableList<IConfigGuiTab> TABS =
            ImmutableList.of(VALUE, LIST, HOTKEY, MISCELLANEOUS, DEBUG);

    private static IConfigGuiTab currentTab = VALUE;

    public BCConfigScreen() {
        super(10, 50, BluesClient.MODID, null, TABS, "bluesclient.gui.title");
    }

    @Override
    public IConfigGuiTab getCurrentTab() {
        return currentTab;
    }

    @Override
    public void setCurrentTab(IConfigGuiTab tab) {
        currentTab = tab;
    }

    private static ImmutableList<IConfigBase> buildValueTab() {
        List<IConfigBase> list = new ArrayList<>(BCConfig.VALUE);
        if (false/*!hasRlCombatServerConfig()*/) {
            list.remove(BCConfig.RLCombatOffhand);
            list.remove(BCConfig.RLCombatEntityBlacklist);
            list.remove(BCConfig.RLCombatOffhandNunchaku);
        }
        return ImmutableList.copyOf(list);
    }

    private static ImmutableList<IConfigBase> buildHotKeyTab() {
        List<IConfigBase> list = new ArrayList<>(BCConfig.KEYBIND);
        if (!ModReference.FlightDebug) {
            list.remove(BCConfig.FlightToggle);
        }
        return ImmutableList.copyOf(list);
    }

    private static ImmutableList<IConfigBase> buildDebugTab() {
        List<IConfigBase> list = new ArrayList<>(BCConfig.DEBUG);
        if (!ModReference.FlightDebug) {
            list.remove(BCConfig.FlightDebug);
        }
        return ImmutableList.copyOf(list);
    }
}
