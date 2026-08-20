package com.blue.bluesclient;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;

public class ModReference {
    public static final boolean VanillaPlus = true;
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
    private static Boolean srpBelow110 = null;

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

    public static boolean isSrpBelow110() {
        if (srpBelow110 != null) return srpBelow110;
        try {
            if (!Loader.isModLoaded("srparasites")) {
                return srpBelow110 = false;
            }
            String ver = Loader.instance().getIndexedModList()
                    .get("srparasites").getVersion();
            ArtifactVersion current = new DefaultArtifactVersion(ver);
            ArtifactVersion gate = new DefaultArtifactVersion("1.10.0");
            return srpBelow110 = current.compareTo(gate) < 0;
        } catch (Throwable t) {
            return srpBelow110 = false;
        }
    }
}
