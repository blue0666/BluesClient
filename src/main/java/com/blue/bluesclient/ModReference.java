package com.blue.bluesclient;

import net.minecraftforge.fml.common.Loader;

public class ModReference {
    public static String RUSTIC = "rustic";
    public static String DSHUDS = "dshuds";
    public static String FIRSTAID = "firstaid";
    public static String MUJMAJNKRAFTSBETTERSURVIVAL = "mujmajnkraftsbettersurvival";
    public static String BETTERCOMBATMOD = "bettercombatmod";
    public static String REACHFIX = "reachfix";
    public static String SRParasites = "srparasites";
    public static String INFERNALMOBS = "infernalmobs";

    public static boolean hasMod(String modId) {
        return Loader.isModLoaded(modId);
    }

    private static Boolean rlCombatHasServerConfig = null;
    public static boolean hasRlCombatServerConfig() {
        if (rlCombatHasServerConfig != null) return rlCombatHasServerConfig;
        if (!Loader.isModLoaded("bettercombatmod")) {
            return rlCombatHasServerConfig = false;
        }
        try {
            //原版RLC的RLcombat版本不一样，不能一起兼容，需要禁用删除万物双截棍
            Object ignored = bettercombat.mod.util.ConfigurationHandler.server;
            return rlCombatHasServerConfig = true;
        } catch (Throwable t) {
            return rlCombatHasServerConfig = false;
        }
    }
}
