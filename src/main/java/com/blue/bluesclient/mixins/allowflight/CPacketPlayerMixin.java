package com.blue.bluesclient.mixins.allowflight;

import com.blue.bluesclient.config.BCConfig;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CPacketPlayer.class)
public abstract class CPacketPlayerMixin {
    @Shadow
    protected boolean onGround;
    @Inject(method = "writePacketData", at = @At("HEAD"))
    private void bluesclient$forceOnGround(PacketBuffer buf, CallbackInfo ci) {
        if (BCConfig.AllowFlight.getBooleanValue()) {
            this.onGround = true;
        }
    }
}