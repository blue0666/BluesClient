package com.blue.bluesclient.modmixins.eaglemixins;

import com.blue.bluesclient.config.BCConfig;
import eaglemixins.handlers.DamageFalloffHandler;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DamageFalloffHandler.class)
public class DamageFalloffHandlerMixin {
    @Inject (
            method = "onLivingDamage",
            at=@At("HEAD"),
            cancellable = true,
            remap = false
    )
    private static void noDamageFalloff(LivingDamageEvent event, CallbackInfo ci){
        if(BCConfig.NoDamageFallOff.getBooleanValue()){
            ci.cancel();
        }
    }
}
