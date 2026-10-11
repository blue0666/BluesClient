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
import org.lwjgl.input.Mouse;

public class TooltipScrollHandler {
    private static int offsetY;
    private static ItemStack last = ItemStack.EMPTY;

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
        offsetY += wheel > 0 ? -20 : 20;
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
            } else {
                last = stack.copy();
            }
        }
        e.setY(e.getY() + offsetY);
    }

    @SubscribeEvent
    public static void onGui(GuiOpenEvent e) {
        if (e.getGui() == null) {
            offsetY = 0;
            last = ItemStack.EMPTY;
        }
    }
}