package com.blue.bluesclient.feat.blockfakevillager;

import com.blue.bluesclient.config.BCConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = "bluesclient", value = Side.CLIENT)
public class BlockFakeVillagerEvents {
    private static boolean wasEnabled = false;
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (!BCConfig.BlockFakeLibrarianTrade.getBooleanValue()){
            if (wasEnabled){
                FakeVillagerRenderer.clearGlowing();
            }
            wasEnabled=false;
            return;
        }
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        wasEnabled=true;
        FakeVillagerRenderer.applyGlowing();
    }
}
