package com.blue.bluesclient.feat.blockfakevillager;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;

public class FakeVillagerHandler {
    public static boolean isTrapVillager(Entity entity) {
        if (!(entity instanceof EntityVillager)) return false;
        if (!entity.hasCustomName()) return false;
        String name = entity.getCustomNameTag();
        return "Sussyberian".equals(name) || "Mentalberian".equals(name);
    }
}
