package com.blue.bluesclient.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.config.options.ConfigInteger;
import fi.dy.masa.malilib.config.options.ConfigStringList;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;

public class ConfigFactory {
    private static final String DEFAULT_COMMENT="No comment";
    private static final String PRETTY_NAME="No further comment";
    public static ConfigBoolean ofBoolean(String name){
        return new ConfigBoolean(name,false,DEFAULT_COMMENT);
    }

    public static ConfigBoolean ofBoolean(String name,Boolean defaultValue){
        return new ConfigBoolean(name,defaultValue,DEFAULT_COMMENT);
    }

    public static ConfigBoolean ofBoolean(String name,Boolean defaultValue,String comment){
        return new ConfigBoolean(name,defaultValue,comment);
    }

    public static ConfigBoolean ofBoolean(String name,Boolean defaultValue,String comment,String prettyName){
        return new ConfigBoolean(name,defaultValue,comment,prettyName);
    }

    public static ConfigStringList ofStringList(String name, ImmutableList<String> defaultValue) {
        return ofStringList(name, defaultValue, DEFAULT_COMMENT);
    }

    public static ConfigStringList ofStringList(String name, ImmutableList<String> defaultValue, String comment) {
        return new ConfigStringList(name, defaultValue, comment);
    }

    public static ConfigHotkey ofHotkey(String name, String defaultStorageString) {
        return ofHotkey(name, defaultStorageString, DEFAULT_COMMENT);
    }

    public static ConfigHotkey ofHotkey(String name, String defaultStorageString, String comment) {
        return ofHotkey(name, defaultStorageString, KeybindSettings.DEFAULT, comment);
    }

    public static ConfigHotkey ofHotkey(String name, String defaultStorageString, KeybindSettings settings) {
        return ofHotkey(name, defaultStorageString, settings, DEFAULT_COMMENT);
    }

    public static ConfigHotkey ofHotkey(String name, String defaultStorageString, KeybindSettings settings, String comment) {
        return new ConfigHotkey(name, defaultStorageString, settings, comment);
    }

    public static ConfigInteger ofInteger(String name, int defaultValue, int minValue, int maxValue) {
        return ofInteger(name, defaultValue, minValue, maxValue, false);
    }

    public static ConfigInteger ofInteger(String name, int defaultValue, int minValue, int maxValue, boolean useSlider) {
        return ofInteger(name, defaultValue, minValue, maxValue, useSlider, DEFAULT_COMMENT);
    }

    public static ConfigInteger ofInteger(String name, int defaultValue, int minValue, int maxValue, boolean useSlider, String comment) {
        return new ConfigInteger(name, defaultValue, minValue, maxValue, useSlider, comment);
    }
}
