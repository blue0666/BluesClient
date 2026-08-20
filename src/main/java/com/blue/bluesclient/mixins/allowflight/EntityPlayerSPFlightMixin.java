package com.blue.bluesclient.mixins.allowflight;

import com.blue.bluesclient.config.BCConfig;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityPlayerSP.class)
public class EntityPlayerSPFlightMixin {
    @Redirect(
            method = "onUpdateWalkingPlayer",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/entity/EntityPlayerSP;onGround:Z",
                    opcode = Opcodes.GETFIELD
            )
    )
    private boolean bluesclient$packetOnGround(EntityPlayerSP self) {
        if (BCConfig.AllowFlight.getBooleanValue()) {
            return true;
        }
        return self.onGround;
    }
}
