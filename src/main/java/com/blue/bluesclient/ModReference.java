package com.blue.bluesclient;

import net.minecraftforge.fml.common.Loader;

public class ModReference {
    public static String RUSTIC = "rustic";
    public static String DSHUDS = "dshuds";
    public static String FIRSTAID = "firstaid";
    public static String MUJMAJNKRAFTSBETTERSURVIVAL = "mujmajnkraftsbettersurvival";
    public static String BETTERCOMBATMOD = "bettercombatmod";
    public static String REACHFIX = "reachfix";
    public static boolean hasMod(String modId) {
        return Loader.isModLoaded(modId);
    }
}
