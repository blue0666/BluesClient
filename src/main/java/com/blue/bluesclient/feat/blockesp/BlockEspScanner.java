package com.blue.bluesclient.feat.blockesp;

import com.blue.bluesclient.config.BCConfig;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public final class BlockEspScanner {
    public static final int SCAN_RANGE = 64;
    public static final int SCAN_INTERVAL_TICKS = 20;

    private BlockEspScanner() {
    }

    public static void scanAroundPlayer() {
        Minecraft mc = Minecraft.getMinecraft();
        WorldClient world = mc.world;
        EntityPlayerSP player = mc.player;
        if (world == null || player == null) {
            BlockEspStore.clear();
            return;
        }

        List<String> allow = BCConfig.TileEntityEspList.getStrings();

        boolean isConfigClosed=BlockEspStore.isAllBlockEspConfigClosed();
        if (isConfigClosed) {
            BlockEspStore.clear();
            return;
        }

        BlockPos origin = player.getPosition();
        int range = BCConfig.TileEntityEspDistance.getIntegerValue();
        ;
        if (range < 1 || range > 300) {
            BlockEspStore.clear();
            return;
        }
        int rangeSq = range * range;

        List<BlockPos> foundNormalBlockList = new ArrayList<>();
        List<BlockPos> foundWitherBlockList = new ArrayList<>();
        List<BlockPos> foundGorgonBlockList = new ArrayList<>();
        List<BlockPos> foundGuardianBlockList = new ArrayList<>();

        for (TileEntity te : world.loadedTileEntityList) {
            BlockPos pos = te.getPos();
            if (!world.isBlockLoaded(pos)) {
                continue;
            }
            int dx = pos.getX() - origin.getX();
            int dy = pos.getY() - origin.getY();
            int dz = pos.getZ() - origin.getZ();
            if (dx * dx + dy * dy + dz * dz > rangeSq) {
                continue;
            }
            Block block = world.getBlockState(pos).getBlock();
            ResourceLocation id = Block.REGISTRY.getNameForObject(block);
            if (id == null) {
                continue;
            }
            if (BCConfig.WitherSpawnerEsp.getBooleanValue() && SpecialSpawnerHandler.isTargetSpawner(te, "minecraft:wither")){
                foundWitherBlockList.add(pos.toImmutable());
                continue;
            }
            if (BCConfig.GorgonSpawnerEsp.getBooleanValue() && SpecialSpawnerHandler.isTargetSpawner(te, "iceandfire:gorgon")) {
                foundGorgonBlockList.add(pos.toImmutable());
                continue;
            }
            if (BCConfig.ElderGuardianSpawnerEsp.getBooleanValue() && SpecialSpawnerHandler.isTargetSpawner(te, "minecraft:elder_guardian")) {
                foundGuardianBlockList.add(pos.toImmutable());
                continue;
            }
            if (!allow.contains(id.toString())) {
                continue;
            }
            if (BCConfig.TileEntityEsp.getBooleanValue()){
                foundNormalBlockList.add(pos.toImmutable());
            }
        }
        BlockEspStore.setNormal(foundNormalBlockList);
        BlockEspStore.setWither(foundWitherBlockList);
        BlockEspStore.setGorgon(foundGorgonBlockList);
        BlockEspStore.setElderguardian(foundGuardianBlockList);
    }
}