package com.blue.bluesclient.feat.blockesp;

import com.blue.bluesclient.config.BCConfig;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

//方块透视扫描结果缓存与配置记录
public final class BlockEspStore {
    private static final List<BlockPos> NORMAL = new ArrayList<>();
    private static final List<BlockPos> WITHER = new ArrayList<>();
    private static final List<BlockPos> GORGON = new ArrayList<>();
    private static final List<BlockPos> ELDERGUARDIAN = new ArrayList<>();
    private static final int OPENED_MAX = 256;
    private static final LinkedHashSet<BlockPos> OPENED = new LinkedHashSet<>();


    private BlockEspStore() {
    }

    public static synchronized void setNormal(List<BlockPos> next) {
        NORMAL.clear();
        NORMAL.addAll(next);
    }

    public static synchronized List<BlockPos> getNormal() {
        return Collections.unmodifiableList(new ArrayList<>(NORMAL));
    }

    public static synchronized void setWither(List<BlockPos> next) {
        WITHER.clear();
        WITHER.addAll(next);
    }

    public static synchronized List<BlockPos> getWither() {
        return Collections.unmodifiableList(new ArrayList<>(WITHER));
    }

    public static synchronized void setGorgon(List<BlockPos> next) {
        GORGON.clear();
        GORGON.addAll(next);
    }

    public static synchronized List<BlockPos> getGorgon() {
        return Collections.unmodifiableList(new ArrayList<>(GORGON));
    }

    public static synchronized void setElderguardian(List<BlockPos> next) {
        ELDERGUARDIAN.clear();
        ELDERGUARDIAN.addAll(next);
    }

    public static synchronized List<BlockPos> getElderguardian() {
        return Collections.unmodifiableList(new ArrayList<>(ELDERGUARDIAN));
    }

    public static boolean isAllStoredListEmpty(){
        return !BlockEspStore.getNormal().isEmpty() || !BlockEspStore.getWither().isEmpty() || !BlockEspStore.getGorgon().isEmpty() || !BlockEspStore.getElderguardian().isEmpty();
    }

    public static boolean isAllBlockEspConfigClosed(){
        return !BCConfig.TileEntityEsp.getBooleanValue()&&
                !BCConfig.WitherSpawnerEsp.getBooleanValue()&&
                !BCConfig.GorgonSpawnerEsp.getBooleanValue()&&
                !BCConfig.ElderGuardianSpawnerEsp.getBooleanValue();
    }

    public static synchronized void clear() {
        NORMAL.clear();
        WITHER.clear();
        GORGON.clear();
        ELDERGUARDIAN.clear();
        OPENED.clear();
    }

    private static void addOpened(BlockPos pos) {
        BlockPos p = pos.toImmutable();
        OPENED.remove(p);
        OPENED.add(p);
        while (OPENED.size() > OPENED_MAX) {
            Iterator<BlockPos> it = OPENED.iterator();
            it.next();
            it.remove();
        }
    }

    public static synchronized boolean isTrackedContainer(World world, BlockPos pos){
        List<String> allow = BCConfig.TileEntityEspList.getStrings();
        Block block = world.getBlockState(pos).getBlock();
        ResourceLocation id = Block.REGISTRY.getNameForObject(block);
        if (allow.contains(id.toString())) {
           return true;
        }
        return false;
    }
    public static synchronized boolean isOpenedContainer(BlockPos pos) {
        return OPENED.contains(pos);
    }

    public static synchronized void markOpenedContainer(World world, BlockPos pos) {
        addOpened(pos);
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof TileEntityChest)) return;
        TileEntityChest chest = (TileEntityChest) te;
        chest.checkForAdjacentChests();
        if (chest.adjacentChestXNeg != null) addOpened(chest.adjacentChestXNeg.getPos());
        if (chest.adjacentChestXPos != null) addOpened(chest.adjacentChestXPos.getPos());
        if (chest.adjacentChestZNeg != null) addOpened(chest.adjacentChestZNeg.getPos());
        if (chest.adjacentChestZPos != null) addOpened(chest.adjacentChestZPos.getPos());
    }
}