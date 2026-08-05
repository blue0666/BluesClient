package com.blue.bluesclient.feat.alwayssneak;

import com.blue.bluesclient.config.BCConfig;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class SneakOpenHandler {
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!BCConfig.AlwaysSneak.getBooleanValue()) {
            return;
        }
        if (!event.getWorld().isRemote) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;
        if (player == null) {
            return;
        }

        if (!player.isSneaking()) {
            return;
        }

        net.minecraft.block.Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
        if (!(block instanceof BlockChest ||
                block instanceof BlockEnderChest ||
                block instanceof BlockShulkerBox)) {
            return;
        }
        openChestWhileSneaking(player, event.getPos(), event.getFace(), event.getHand());
        event.setCanceled(true);
    }

    private static void openChestWhileSneaking(EntityPlayerSP player, BlockPos pos, EnumFacing facing, EnumHand hand) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.getConnection() == null) {
            return;
        }

        mc.getConnection().sendPacket(new CPacketEntityAction(player, CPacketEntityAction.Action.STOP_SNEAKING));

        mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(
                pos,
                facing,
                hand,
                0.5f,  // hitX
                0.5f,  // hitY
                0.5f   // hitZ
        ));

        mc.getConnection().sendPacket(new CPacketEntityAction(player, CPacketEntityAction.Action.START_SNEAKING));
    }
}