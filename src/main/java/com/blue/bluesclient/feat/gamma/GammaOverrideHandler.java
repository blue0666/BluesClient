package com.blue.bluesclient.feat.gamma;

import com.blue.bluesclient.config.BCConfig;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class GammaOverrideHandler {
    private static final float OVERRIDE_GAMMA = 16.0F;

    private static float originalGamma = 1.0F;
    private static boolean hasOriginal;

    public static void applyFromConfig() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.gameSettings == null) return;

        if (BCConfig.GammaOverride.getBooleanValue()) {
            if (!hasOriginal) {
                originalGamma = mc.gameSettings.gammaSetting;
                hasOriginal = true;
            }
            mc.gameSettings.gammaSetting = OVERRIDE_GAMMA;
        } else if (hasOriginal) {
            mc.gameSettings.gammaSetting = originalGamma;
            hasOriginal = false;
        }
    }
}