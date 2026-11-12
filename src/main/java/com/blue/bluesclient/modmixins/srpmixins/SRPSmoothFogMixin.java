package com.blue.bluesclient.modmixins.srpmixins;

import com.blue.bluesclient.config.BCConfig;
import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(value = SRPEventHandlerBus.class, remap = false, priority = 500)
public abstract class SRPSmoothFogMixin {

    @SuppressWarnings("target")
    @Dynamic("Method added to SRPEventHandlerBus by SRPMixins")
    @Inject(
            method = "srpmixins$canApplyFog(Lnet/minecraftforge/client/event/EntityViewRenderEvent;)Z",
            at = @At("HEAD"),
            cancellable = true,
            remap = false,
            require = 0
    )
    private static void bluesclient$disableSmoothFog(
            EntityViewRenderEvent event,
            CallbackInfoReturnable<Boolean> cir) {
        if (BCConfig.NoSRPBiomeFog.getBooleanValue()) {
            cir.setReturnValue(false);
        }
    }
}