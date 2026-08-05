package com.blue.bluesclient.feat.blockesp;

import com.blue.bluesclient.config.BCConfig;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = "bluesclient", value = Side.CLIENT)
public final class BlockEspEvents {
    private static int tickCounter;

    private BlockEspEvents() {
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (BlockEspStore.isAllBlockEspConfigClosed()) {
            if (BlockEspStore.isAllStoredListEmpty()) {
                BlockEspStore.clear();
            }
            tickCounter = 0;
            return;
        }
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        tickCounter++;
        if (tickCounter < BlockEspScanner.SCAN_INTERVAL_TICKS) {
            return;
        }
        tickCounter = 0;
        BlockEspScanner.scanAroundPlayer();
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event) {
        if (BlockEspStore.isAllBlockEspConfigClosed()) {
            if (BlockEspStore.isAllStoredListEmpty()) {
                BlockEspStore.clear();
            }
            tickCounter = 0;
            return;
        }
        BlockEspRenderer.prerender(event);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Unload event) {
        if (event.getWorld().isRemote) {
            BlockEspStore.clear();
            tickCounter = 0;
        }
    }
}