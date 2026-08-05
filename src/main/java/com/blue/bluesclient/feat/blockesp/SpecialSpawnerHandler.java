package com.blue.bluesclient.feat.blockesp;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;

public class SpecialSpawnerHandler {
    public static boolean isTargetSpawner(TileEntity te, String entityId) {
        if (!(te instanceof TileEntityMobSpawner)) return false;
        Entity e = ((TileEntityMobSpawner) te).getSpawnerBaseLogic().getCachedEntity();
        if (e == null) return false;
        ResourceLocation key = EntityList.getKey(e);
        return key != null && entityId.equals(key.toString());
    }
}
