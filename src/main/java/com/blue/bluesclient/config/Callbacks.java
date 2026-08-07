package com.blue.bluesclient.config;

import com.blue.bluesclient.config.gui.BCConfigScreen;
import fi.dy.masa.malilib.util.InfoUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketEntityAction;

import static com.blue.bluesclient.config.BCConfig.EverythingNunchakuToggle;

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
    }
}
