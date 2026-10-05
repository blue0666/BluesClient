package com.blue.bluesclient.mixin;

import com.blue.bluesclient.ModReference;
import fermiumbooter.FermiumRegistryAPI;
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

    private static final boolean DEBUG = true;

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        boolean apply = modLoadedForMixin(mixinClassName);
        if (DEBUG) {
            System.out.println("[BC-LATE] " + mixinClassName + " apply=" + apply + " target=" + targetClassName);
        }
        return apply;
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
        String modId = packagePath.substring(packagePath.lastIndexOf('.') + 1);
        boolean present = false;
        try {
            present = Loader.isModLoaded(modId) || FermiumRegistryAPI.isModPresent(modId);
        } catch (Throwable ignored) { }
        if (!present) return false;
        if (mixinClassName.endsWith("bettercombatmod.BetterSurvivalHandler_Mixin")) {
            return ModReference.hasRlCombatServerConfig();
        }
        if (mixinClassName.endsWith("bettercombatmod.BetterSurvivalHandler_MixinLegacy")) {
            return !ModReference.hasRlCombatServerConfig();
        }
        if ("srparasites".equals(modId)) {
            return ModReference.isSrpBelow110();
        }
        return true;
    }
}
