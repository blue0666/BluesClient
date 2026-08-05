package com.blue.bluesclient.feat.blockfakevillager;

import com.blue.bluesclient.config.BCConfig; // 若还没有开关，可先写死 true
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = "bluesclient", value = Side.CLIENT)
public final class FakeVillagerTradeBlocker {

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (shouldBlock(event.getTarget())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onEntityInteractSpecific(PlayerInteractEvent.EntityInteractSpecific event) {
        if (shouldBlock(event.getTarget())) {
            event.setCanceled(true);
        }
    }

    private static boolean shouldBlock(Entity target) {
        if (!(target instanceof EntityVillager)) {
            return false;
        }
        if (BCConfig.BlockFakeLibrarianTrade.getBooleanValue()){
            if(FakeVillagerHandler.isTrapVillager(target)){
                return true;
            }
        }
        return false;
    }
}
