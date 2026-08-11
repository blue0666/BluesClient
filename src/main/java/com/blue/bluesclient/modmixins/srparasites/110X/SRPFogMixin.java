//package com.blue.bluesclient.modmixins.srparasites;
//
//import com.blue.bluesclient.config.BCConfig;
//import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
//import net.minecraftforge.client.event.EntityViewRenderEvent;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//@Mixin(value = SRPEventHandlerBus.class,remap=false)
//public class SRPFogMixin {
//    @Inject(method="onEvent(Lnet/minecraftforge/client/event/EntityViewRenderEvent$FogDensity;)V",at=@At("HEAD"),cancellable = true, remap = false)
//    public void removeSRPFog(EntityViewRenderEvent.FogDensity event, CallbackInfo ci){
//        if (BCConfig.NoSRPBiomeFog.getBooleanValue()){
//            ci.cancel();
//        }
//    }
//}
