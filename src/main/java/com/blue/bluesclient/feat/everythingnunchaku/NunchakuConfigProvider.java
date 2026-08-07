package com.blue.bluesclient.feat.everythingnunchaku;

import bettercombat.mod.util.Helpers;
import com.blue.bluesclient.config.BCConfig;
import com.blue.bluesclient.modmixins.bettercombatmod.EventHandlersClient_Invoker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class NunchakuConfigProvider {
    private static final Logger LOGGER = LogManager.getLogger("BluesClient-Nunchaku");

    private static final Set<Class<?>> validNunchakuClasses = new HashSet<>();
    private static final Set<ResourceLocation> validNunchakuItems = new HashSet<>();
    private static final Set<ResourceLocation> invalidNunchakuItems = new HashSet<>();
    private static final Set<ResourceLocation> invalidTargetEntities = new HashSet<>();

    private NunchakuConfigProvider() {}

    public static void init() {
        initClientNunchakus();
    }

    public static boolean isClientNunchaku(Item item) {
        if (!BCConfig.EverythingNunchaku.getBooleanValue()) return false;

        if (BCConfig.EverythingNunchakuAllowAll.getBooleanValue()) return true;

        if (invalidNunchakuItems.contains(item.getRegistryName())) return false;
        if (validNunchakuItems.contains(item.getRegistryName())) return true;
        for (Class<?> clazz : validNunchakuClasses) {
            if (clazz.isInstance(item)) return true;
        }
        return false;
    }

    public static boolean shouldAttack(Entity entHit, EntityPlayer player) {
        if (BCConfig.RLCombatEntityBlacklist.getBooleanValue()
                && Loader.isModLoaded("bettercombatmod")) {
            if (!EventHandlersClient_Invoker.invokeShouldAttack(entHit, player)) return false;
        } else {
            if (entHit == null) return false;
            if (entHit instanceof EntityPlayerMP) {
                return Helpers.execNullable(entHit.getServer(), MinecraftServer::isPVPEnabled, false);
            }
            if (entHit instanceof IEntityOwnable
                    && ((IEntityOwnable) entHit).getOwner() == player) {
                return false;
            }
        }
        return isEntityNunchakable(entHit);
    }

    public static boolean isEntityNunchakable(Entity entity) {
        if (entity == null) return false;
        ResourceLocation key = EntityList.getKey(entity);
        if (key != null && invalidTargetEntities.contains(key)) {
            return false;
        }
        return true;
    }

    public static void initClientNunchakus() {
        validNunchakuClasses.clear();
        validNunchakuItems.clear();
        invalidNunchakuItems.clear();
        invalidTargetEntities.clear();

        for (String line : BCConfig.NunchakuItemClassWhitelist.getStrings()) {
            try {
                validNunchakuClasses.add(Class.forName(line.trim()));
            } catch (ClassNotFoundException e) {
                LOGGER.warn("Item Class not found: {}, ignoring", line);
            }
        }
        for (String id : BCConfig.NunchakuItemIDWhitelist.getStrings()) {
            ResourceLocation rl = new ResourceLocation(id);
            if (ForgeRegistries.ITEMS.getValue(rl) == null) {
                LOGGER.warn("Whitelist Item ID not found: {}, ignoring", rl);
            } else {
                validNunchakuItems.add(rl);
            }
        }
        for (String id : BCConfig.NunchakuItemIDBlacklist.getStrings()) {
            ResourceLocation rl = new ResourceLocation(id);
            if (ForgeRegistries.ITEMS.getValue(rl) == null) {
                LOGGER.warn("Blacklist Item ID not found: {}, ignoring", rl);
            } else {
                invalidNunchakuItems.add(rl);
            }
        }
        for (String id : BCConfig.NunchakuEntityBlacklist.getStrings()) {
            ResourceLocation rl = new ResourceLocation(id.trim());
            if (ForgeRegistries.ENTITIES.getValue(rl) == null) {
                LOGGER.warn("Blacklist Entity ID not found: {}, ignoring", rl);
            } else {
                invalidTargetEntities.add(rl);
            }
        }
    }
}
