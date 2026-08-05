package com.blue.bluesclient;

import net.minecraftforge.fml.common.Loader;

public class ModReference {
    public static String RUSTIC = "rustic";
    public static String dshuds = "dshuds";
    public static boolean hasMod(String modId) {
        return Loader.isModLoaded(modId);
    }
}
