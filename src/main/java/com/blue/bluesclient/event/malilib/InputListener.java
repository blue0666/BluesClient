package com.blue.bluesclient.event.malilib;

import com.blue.bluesclient.BluesClient;
import com.blue.bluesclient.config.BCConfig;
import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.IKeybindProvider;
import fi.dy.masa.malilib.hotkeys.KeybindCategory;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class InputListener implements IKeybindProvider {
    public static final InputListener INSTANCE = new InputListener();
    private InputListener() {}
    @Override
    public List<? extends IHotkey> getAllHotkeys() {
        return BCConfig.KEYBIND;
    }
    @Override
    public List<KeybindCategory> getHotkeyCategoriesForCombinedView() {
        return ImmutableList.of(
                new KeybindCategory(BluesClient.NAME, "热键", BCConfig.KEYBIND)
        );
    }
}