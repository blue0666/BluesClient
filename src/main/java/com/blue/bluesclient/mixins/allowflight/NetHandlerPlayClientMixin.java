package com.blue.bluesclient.mixins.allowflight;

import com.blue.bluesclient.config.BCConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class NetHandlerPlayClientMixin {
    @Inject(method = "handlePlayerAbilities", at = @At("RETURN"))
    private void bluesclient$keepLocalFlight(SPacketPlayerAbilities packet, CallbackInfo ci) {
        if (!BCConfig.AllowFlight.getBooleanValue()) return;
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player == null) return;
        player.capabilities.allowFlying = true;
        player.capabilities.isFlying = true;
    }
}
