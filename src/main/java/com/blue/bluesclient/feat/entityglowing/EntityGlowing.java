package com.blue.bluesclient.feat.entityglowing;

import atomicstryker.infernalmobs.common.InfernalMobsCore;
import atomicstryker.infernalmobs.common.MobModifier;
import atomicstryker.infernalmobs.common.mods.MM_Rust;
import com.blue.bluesclient.config.BCConfig;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.Loader;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class EntityGlowing {
    private static final List<Predicate<Entity>> ENTRIES = new ArrayList<>();

    public static boolean shouldGlow(Entity entity){
        return ENTRIES.stream().anyMatch(x -> x.test(entity));
    }

    public static boolean isRust(Entity entity){
        if (!(entity instanceof EntityLivingBase)){return false;}
        if (!Loader.isModLoaded("infernalmobs")) return false;
        MobModifier mod = InfernalMobsCore.getMobModifiers((EntityLivingBase) entity);
        if (mod == null) return false;
        return mod.containsModifierClass(MM_Rust.class);
    }

    private static void register(ConfigBoolean config, Predicate<Entity> predicate) {
        ENTRIES.add(entity -> config.getBooleanValue() && predicate.test(entity));
    }

    static{
        register(BCConfig.HighlightRustMob,EntityGlowing::isRust);
    }
}
