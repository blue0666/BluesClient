package com.blue.bluesclient.feat.allowflight;

import com.blue.bluesclient.config.BCConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class FlightHandler {
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player == null) return;
        if (BCConfig.AllowFlight.getBooleanValue()) {
            player.capabilities.allowFlying = true;
            player.capabilities.isFlying = true;
            return;
        }

//        if (!player.capabilities.isCreativeMode
//                && !Minecraft.getMinecraft().playerController.isSpectatorMode()) {
//            player.capabilities.isFlying = false;
//            player.capabilities.allowFlying = false;
//            player.capabilities.setFlySpeed(0.05F);
//        }
    }


}
