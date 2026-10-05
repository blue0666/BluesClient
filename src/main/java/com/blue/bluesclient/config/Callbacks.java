package com.blue.bluesclient.config;

import com.blue.bluesclient.config.gui.BCConfigScreen;
import com.blue.bluesclient.feat.everythingnunchaku.NunchakuConfigProvider;
import fi.dy.masa.malilib.util.InfoUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketEntityAction;

import static com.blue.bluesclient.config.BCConfig.EverythingNunchakuToggle;
import static com.blue.bluesclient.config.BCConfig.FlightToggle;

public class Callbacks {
    public static void init(Minecraft client) {
        BCConfig.OpenWindow.getKeybind().setCallback((action, key) -> {
                    client.displayGuiScreen(new BCConfigScreen());
                    return true;
        });

        BCConfig.AlwaysSneak.setValueChangeCallback(cfg -> {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.player == null || mc.getConnection() == null) return;
            if (cfg.getBooleanValue()) {
                mc.getConnection().sendPacket(new CPacketEntityAction(
                        mc.player, CPacketEntityAction.Action.START_SNEAKING));
            } else {
                mc.getConnection().sendPacket(new CPacketEntityAction(
                        mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            }
        });

        BCConfig.AlwaysSneakToggle.getKeybind().setCallback((action, key) -> {
            BCConfig.AlwaysSneak.toggleBooleanValue();
            InfoUtils.printBooleanConfigToggleMessage(
                    BCConfig.AlwaysSneak.getPrettyName(),
                    BCConfig.AlwaysSneak.getBooleanValue()
            );
            return true;
        });

        EverythingNunchakuToggle.getKeybind().setCallback((a, k) -> {
            BCConfig.EverythingNunchaku.toggleBooleanValue();
            InfoUtils.printBooleanConfigToggleMessage(
                    BCConfig.EverythingNunchaku.getPrettyName(),
                    BCConfig.EverythingNunchaku.getBooleanValue());
            return true;
        });

        FlightToggle.getKeybind().setCallback((a,k)->{
            BCConfig.FlightDebug.toggleBooleanValue();
            InfoUtils.printBooleanConfigToggleMessage(
                    BCConfig.FlightDebug.getPrettyName(),
                    BCConfig.FlightDebug.getBooleanValue());
            return true;
        });

        BCConfig.FlightDebug.setValueChangeCallback(cfg -> {
            if (cfg.getBooleanValue()) return;
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.player == null || mc.playerController == null) return;
            mc.playerController.setPlayerCapabilities(mc.player);
        });

        Runnable reload = NunchakuConfigProvider::initClientNunchakus;
        BCConfig.NunchakuItemIDWhitelist.setValueChangeCallback(c -> reload.run());
        BCConfig.NunchakuItemIDBlacklist.setValueChangeCallback(c -> reload.run());
        BCConfig.NunchakuEntityBlacklist.setValueChangeCallback(c -> reload.run());
        BCConfig.NunchakuItemClassWhitelist.setValueChangeCallback(c -> reload.run());
    }
}
