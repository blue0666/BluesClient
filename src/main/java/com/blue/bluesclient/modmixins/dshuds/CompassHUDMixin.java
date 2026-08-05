package com.blue.bluesclient.modmixins.dshuds;

import com.blue.bluesclient.config.BCConfig;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.orecruncher.dshuds.hud.CompassHUD;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CompassHUD.class)
public class CompassHUDMixin {
    @Inject(
            method = "doRender",
            at=@At("HEAD"),
            cancellable = true,
            remap = false
    )
    public void doRenderBlocker(RenderGameOverlayEvent.Pre event, CallbackInfo ci){
        if (BCConfig.DisableCompassHUD.getBooleanValue()){
            ci.cancel();
        }
    }

}
