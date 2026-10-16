package com.blue.bluesclient.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.options.*;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import fi.dy.masa.malilib.util.InfoUtils;
import net.minecraft.client.resources.I18n;

import java.util.function.BooleanSupplier;

public class ConfigFactory {
    private static final String DEFAULT_COMMENT = "No comment";
    private static final String PRETTY_NAME = "No further comment";

    private static String translateOrDefault(String key, String defaultString) {
        return I18n.hasKey(key) ? I18n.format(key) : defaultString;
    }

    public static ConfigBoolean ofLocalizedBoolean(String id, String defaultName) {
        return ofLocalizedBoolean(id, defaultName, false, DEFAULT_COMMENT);
    }


    public static ConfigBoolean ofLocalizedBoolean(String id, String defaultName, Boolean defaultValue) {
        return ofLocalizedBoolean(id, defaultName, defaultValue, DEFAULT_COMMENT);
    }

    public static ConfigBoolean ofLocalizedBoolean(
            String id, String name, boolean defaultValue, String comment) {

        String key = "bluesclient.config." + id;

        return new ConfigBoolean(name, defaultValue, comment) {
            @Override
            public String getPrettyName() {
                return translateOrDefault(key + ".name", name);
            }

            @Override
            public String getConfigGuiDisplayName() {
                return getPrettyName();
            }

            @Override
            public String getComment() {
                return translateOrDefault(key + ".comment", comment);
            }
        };
    }

    public static ConfigString ofLocalizedString(
            String id, String defaultName, String defaultValue) {
        return ofLocalizedString(id, defaultName, defaultValue, DEFAULT_COMMENT);
    }

    public static ConfigString ofLocalizedString(
            String id, String defaultName, String defaultValue, String defaultComment) {

        String key = "bluesclient.config." + id;

        return new ConfigString(defaultName, defaultValue, defaultComment) {
            @Override
            public String getPrettyName() {
                return translateOrDefault(key + ".name", defaultName);
            }

            @Override
            public String getConfigGuiDisplayName() {
                return getPrettyName();
            }

            @Override
            public String getComment() {
                return translateOrDefault(key + ".comment", defaultComment);
            }
        };
    }

    public static ConfigStringList ofLocalizedStringList(
            String id, String defaultName, ImmutableList<String> defaultValue) {
        return ofLocalizedStringList(id, defaultName, defaultValue, DEFAULT_COMMENT);
    }

    public static ConfigStringList ofLocalizedStringList(
            String id, String defaultName, ImmutableList<String> defaultValue,
            String defaultComment) {

        String key = "bluesclient.config." + id;

        return new ConfigStringList(defaultName, defaultValue, defaultComment) {
            @Override
            public String getPrettyName() {
                return translateOrDefault(key + ".name", defaultName);
            }

            @Override
            public String getConfigGuiDisplayName() {
                return getPrettyName();
            }

            @Override
            public String getComment() {
                return translateOrDefault(key + ".comment", defaultComment);
            }
        };
    }

    public static ConfigInteger ofLocalizedInteger(
            String id, String defaultName, int defaultValue,
            int minValue, int maxValue) {
        return ofLocalizedInteger(
                id, defaultName, defaultValue, minValue, maxValue,
                false, DEFAULT_COMMENT);
    }

    public static ConfigInteger ofLocalizedInteger(
            String id, String defaultName, int defaultValue,
            int minValue, int maxValue, boolean useSlider) {
        return ofLocalizedInteger(
                id, defaultName, defaultValue, minValue, maxValue,
                useSlider, DEFAULT_COMMENT);
    }

    public static ConfigInteger ofLocalizedInteger(
            String id, String defaultName, int defaultValue,
            int minValue, int maxValue, String defaultComment) {
        return ofLocalizedInteger(
                id, defaultName, defaultValue, minValue, maxValue,
                false, defaultComment);
    }

    public static ConfigInteger ofLocalizedInteger(
            String id, String defaultName, int defaultValue,
            int minValue, int maxValue, boolean useSlider, String defaultComment) {

        String key = "bluesclient.config." + id;

        return new ConfigInteger(
                defaultName, defaultValue, minValue, maxValue,
                useSlider, defaultComment) {
            @Override
            public String getPrettyName() {
                return translateOrDefault(key + ".name", defaultName);
            }

            @Override
            public String getConfigGuiDisplayName() {
                return getPrettyName();
            }

            @Override
            public String getComment() {
                return translateOrDefault(key + ".comment", defaultComment);
            }
        };
    }

    public static ConfigHotkey ofLocalizedHotkey(
            String id, String defaultName, String defaultStorageString) {
        return ofLocalizedHotkey(
                id, defaultName, defaultStorageString,
                KeybindSettings.DEFAULT, DEFAULT_COMMENT);
    }

    public static ConfigHotkey ofLocalizedHotkey(
            String id, String defaultName, String defaultStorageString,
            String defaultComment) {
        return ofLocalizedHotkey(
                id, defaultName, defaultStorageString,
                KeybindSettings.DEFAULT, defaultComment);
    }

    public static ConfigHotkey ofLocalizedHotkey(
            String id, String defaultName, String defaultStorageString,
            KeybindSettings settings) {
        return ofLocalizedHotkey(
                id, defaultName, defaultStorageString,
                settings, DEFAULT_COMMENT);
    }

    public static ConfigHotkey ofLocalizedHotkey(
            String id, String defaultName, String defaultStorageString,
            KeybindSettings settings, String defaultComment) {

        String key = "bluesclient.config." + id;

        return new ConfigHotkey(
                defaultName, defaultStorageString, settings, defaultComment) {
            @Override
            public String getPrettyName() {
                return translateOrDefault(key + ".name", defaultName);
            }

            @Override
            public String getConfigGuiDisplayName() {
                return getPrettyName();
            }

            @Override
            public String getComment() {
                return translateOrDefault(key + ".comment", defaultComment);
            }
        };
    }

    public static void setToggleCallback(ConfigHotkey config, IHotkeyCallback rawCallback, BooleanSupplier newValue) {
        config.getKeybind().setCallback((action, key) -> {
            boolean success = rawCallback.onKeyAction(action, key);
            InfoUtils.printBooleanConfigToggleMessage(config.getPrettyName(), newValue.getAsBoolean());
            return success;
        });
    }
}
