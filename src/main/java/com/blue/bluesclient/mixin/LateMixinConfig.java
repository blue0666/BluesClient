package com.blue.bluesclient.mixin;

import net.minecraftforge.fml.common.Loader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class LateMixinConfig implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) { }

    @Override
    public String getRefMapperConfig() { return null; }

    private static final boolean DEBUG = false;

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        String packagePath = mixinClassName.substring(0, mixinClassName.lastIndexOf('.'));
        String modId = packagePath.substring(packagePath.lastIndexOf('.') + 1);
        boolean loader = false;
        try { loader = Loader.isModLoaded(modId); } catch (Throwable ignored) {}
        boolean fermium = fermiumbooter.FermiumRegistryAPI.isModPresent(modId);
        if (DEBUG) {
            System.out.println("[BC-LATE] " + modId + " loader=" + loader + " fermium=" + fermium + " target=" + targetClassName);
        }
        return fermium || loader;
        //return modLoadedForMixin(mixinClassName);
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) { }

    @Override
    public List<String> getMixins() { return null; }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) { }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) { }

    private boolean modLoadedForMixin(String mixinClassName) {
        String packagePath = mixinClassName.substring(0, mixinClassName.lastIndexOf('.'));
        String packageName = packagePath.substring(packagePath.lastIndexOf('.') + 1);
        return Loader.isModLoaded(packageName);
    }
}
