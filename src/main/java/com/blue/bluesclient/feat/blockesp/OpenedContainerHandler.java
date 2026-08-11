package com.blue.bluesclient.feat.blockesp;

import com.blue.bluesclient.config.BCConfig;
import com.blue.bluesclient.feat.blockesp.BlockEspStore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(modid = "bluesclient", value = Side.CLIENT)
public class OpenedContainerHandler {
    private static BlockPos pendingPos;
    private static final int PENDING_TTL_TICKS = 40;
    private static int pendingAge;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!event.getWorld().isRemote) return;
        if (!BCConfig.TileEntityEspSkipOpened.getBooleanValue()) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || event.getEntityPlayer() != mc.player) return;
        BlockPos pos = event.getPos();
        if (!BlockEspStore.isTrackedContainer(event.getWorld(), pos)) return;
        pendingPos = pos.toImmutable();
        pendingAge = 0;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onGuiOpen(GuiOpenEvent event) {
        if (!BCConfig.TileEntityEspSkipOpened.getBooleanValue()) return;
        GuiScreen gui = event.getGui();
        if (gui == null) return;

        if (!(gui instanceof GuiContainer)) return;
        if (gui instanceof GuiInventory || gui instanceof GuiContainerCreative) return;
        Minecraft mc = Minecraft.getMinecraft();
        World world = mc.world;
        if (world == null || mc.player == null) return;

        if (pendingPos != null && BlockEspStore.isTrackedContainer(world, pendingPos)) {
            BlockEspStore.markOpenedContainer(world, pendingPos);
        }
        pendingPos = null;
        pendingAge = 0;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (pendingPos == null) return;
        if (++pendingAge > PENDING_TTL_TICKS) {
            pendingPos = null;
            pendingAge = 0;
        }
    }

    private static boolean markFromContainer(Container container, World world) {
        boolean any = false;
        for (Slot slot : container.inventorySlots) {
            IInventory inv = slot.inventory;
            if (inv instanceof TileEntity) {
                BlockPos pos = ((TileEntity) inv).getPos();
                if (BlockEspStore.isTrackedContainer(world, pos)) {
                    BlockEspStore.markOpenedContainer(world, pos);
                    any = true;
                }
                break;
            }
        }
        return any;
    }
}