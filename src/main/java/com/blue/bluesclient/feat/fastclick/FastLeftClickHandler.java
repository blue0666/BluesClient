package com.blue.bluesclient.feat.fastclick;

import bettercombat.mod.client.handler.EventHandlersClient;
import bettercombat.mod.util.ConfigurationHandler;
import com.blue.bluesclient.config.BCConfig;
import com.blue.bluesclient.config.gui.BCConfigScreen;
import com.blue.bluesclient.mixins.fastclick.FastLeftClickAccessor;
import com.blue.bluesclient.mixins.fastclick.FastRightClickAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

public class FastLeftClickHandler {
    private static final int FAST_LEFT_CLICK_PERTICK = 2;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!BCConfig.FastLeftClick.getBooleanValue()) return;
        Minecraft mc = Minecraft.getMinecraft();

        if(mc.currentScreen != null){
            if (mc.currentScreen instanceof BCConfigScreen) {
                return;
            }
            BCConfig.FastLeftClick.setBooleanValue(false);
            return;
        }

        if (mc.player == null || mc.world == null) return;

        if (!Mouse.isButtonDown(0)) return;
        if (Loader.isModLoaded("bettercombatmod")) {
            for (int i = 0; i < FAST_LEFT_CLICK_PERTICK; i++) {
                EventHandlersClient.onMouseLeftClick(new MouseEvent());
            }
            return;
        }

        FastLeftClickAccessor acc = (FastLeftClickAccessor) mc;
        for (int i = 0; i < FAST_LEFT_CLICK_PERTICK; i++) {
            acc.bluesclient$setLeftClickCounter(0);
            acc.bluesclient$leftClickMouse();
        }
    }
}
