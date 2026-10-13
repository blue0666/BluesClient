package com.blue.bluesclient.feat.tooltipscroller;

import com.blue.bluesclient.config.BCConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

public class TooltipScrollHandler {
    private static final float STEP = 20f;
    private static final float LERP = 0.35f;
    private static float offsetY;
    private static float targetY;
    private static ItemStack last = ItemStack.EMPTY;
    public static int getOffsetY() {
        return Math.round(offsetY);
    }

    private static boolean active() {
        if (!BCConfig.ToolTipScroller.getBooleanValue()) return false;
        Minecraft mc = Minecraft.getMinecraft();
        return mc.currentScreen != null && GameSettings.isKeyDown(mc.gameSettings.keyBindSneak);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onGuiMouse(GuiScreenEvent.MouseInputEvent.Pre e) {
        if (!active()) return;
        int wheel = Mouse.getEventDWheel();
        if (wheel == 0) return;
        targetY += wheel > 0 ? -STEP : STEP;
        e.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onTooltip(RenderTooltipEvent.Pre e) {
        if (!BCConfig.ToolTipScroller.getBooleanValue()) return;
        ItemStack stack = e.getStack();
        if (!stack.isEmpty()) {
            if (last.isEmpty()
                    || last.getItem() != stack.getItem()
                    || last.getMetadata() != stack.getMetadata()) {
                last = stack.copy();
                offsetY = 0;
                targetY = 0;
            } else {
                last = stack.copy();
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRenderTick(TickEvent.RenderTickEvent e) {
        if (e.phase != TickEvent.Phase.START) return;
        if (active()) {
            Mouse.getDWheel();
        }
        if (!BCConfig.ToolTipScroller.getBooleanValue()) return;
        if (Minecraft.getMinecraft().currentScreen == null) return;
        float d = targetY - offsetY;
        if (Math.abs(d) < 0.5f) {
            offsetY = targetY;
        } else {
            offsetY += d * LERP;
        }
    }

    @SubscribeEvent
    public static void onGui(GuiOpenEvent e) {
        if (e.getGui() == null) {
            offsetY = 0;
            targetY = 0;
            last = ItemStack.EMPTY;
        }
    }
}