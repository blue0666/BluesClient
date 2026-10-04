package com.blue.bluesclient.mixin;

import fermiumbooter.FermiumRegistryAPI;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nullable;
import java.util.Map;

@IFMLLoadingPlugin.Name("bluesclient")
@IFMLLoadingPlugin.MCVersion(ForgeVersion.mcVersion)
public class FermiumMixinInit implements IFMLLoadingPlugin {

    public FermiumMixinInit() {
        FermiumRegistryAPI.enqueueMixin(false, "mixins.bluesclient.json");
        FermiumRegistryAPI.enqueueMixin(true, "mixins.bluesclient_late.json");

        if (FMLLaunchHandler.isDeobfuscatedEnvironment()) {
            FermiumRegistryAPI.enqueueMixin(false, "mixins.bluesclient.rlcombat_dev.json");
        }
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }


}