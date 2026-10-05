package com.blue.bluesclient.modmixins.bettercombatmod;

import com.blue.bluesclient.config.BCConfig;
import com.blue.bluesclient.feat.everythingnunchaku.NunchakuConfigProvider;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "bettercombat.mod.util.BetterSurvivalHandler", remap = false)
public abstract class BetterSurvivalHandler_MixinLegacy {
    @ModifyReturnValue(method = "isNunchaku", at = @At("RETURN"), remap = false)
    private static boolean bluesclient$isNunchakuAnything(boolean isNunchaku, Item item) {
        return isNunchaku || NunchakuConfigProvider.isClientNunchaku(item);
    }
}