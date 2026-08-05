package com.blue.bluesclient.feat.blockesp;

import com.blue.bluesclient.config.BCConfig;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//方块透视扫描结果缓存与配置记录
public final class BlockEspStore {
    private static final List<BlockPos> NORMAL = new ArrayList<>();
    private static final List<BlockPos> WITHER = new ArrayList<>();
    private static final List<BlockPos> GORGON = new ArrayList<>();
    private static final List<BlockPos> ELDERGUARDIAN = new ArrayList<>();

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
    }
}