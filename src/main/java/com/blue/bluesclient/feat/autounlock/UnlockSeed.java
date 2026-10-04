package com.blue.bluesclient.feat.autounlock;

import com.blue.bluesclient.config.BCConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraft.server.MinecraftServer;

import java.util.OptionalLong;

public final class UnlockSeed {
    private UnlockSeed() {}

    public static OptionalLong resolve() {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server != null) {
            World overworld = server.getWorld(0);
            if (overworld != null) {
                return OptionalLong.of(overworld.getSeed());
            }
            return OptionalLong.empty();
        }
        String seed = BCConfig.LockWorldSeed.getStringValue();
        if (seed == null) {
            return OptionalLong.empty();
        }
        seed = seed.trim();
        if (seed.isEmpty()) {
            return OptionalLong.empty();
        }
        try {
            return OptionalLong.of(Long.parseLong(seed));
        } catch (NumberFormatException e) {
            return OptionalLong.empty();
        }
    }
}
