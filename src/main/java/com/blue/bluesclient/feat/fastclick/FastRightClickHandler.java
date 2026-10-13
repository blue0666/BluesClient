package com.blue.bluesclient.feat.fastclick;

import com.blue.bluesclient.config.BCConfig;
import com.blue.bluesclient.config.gui.BCConfigScreen;
import com.blue.bluesclient.mixins.fastclick.FastRightClickAccessor;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class FastRightClickHandler {
    private static final int FAST_RIGHT_CLICK_PERTICK = 2;

    @SubscribeEvent
    public static void onClientTick(TickEvent .ClientTickEvent event){
        if (event.phase != TickEvent.Phase.END) return;
        if(!BCConfig.FastRightClick.getBooleanValue()) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen != null) {
            if (mc.currentScreen instanceof BCConfigScreen) {
                return;
            }
            BCConfig.FastRightClick.setBooleanValue(false);
            return;
        }
        if (mc.player == null || mc.world == null) return;
        if (!mc.gameSettings.keyBindUseItem.isKeyDown()) return;
        FastRightClickAccessor acc = (FastRightClickAccessor) mc;
        for (int i = 0; i < FAST_RIGHT_CLICK_PERTICK; i++) {
            acc.bluesclient$rightClickMouse();
        }
    }
}
