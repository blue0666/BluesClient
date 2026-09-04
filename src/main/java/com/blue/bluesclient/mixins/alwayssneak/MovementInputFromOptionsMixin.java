package com.blue.bluesclient.mixins.alwayssneak;

import com.blue.bluesclient.config.BCConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MovementInputFromOptions.class)
public abstract class MovementInputFromOptionsMixin extends MovementInput {
    @Inject(method = "updatePlayerMoveState", at = @At("RETURN"))
    private void forceSneak(CallbackInfo ci) {
        if (BCConfig.AlwaysSneak.getBooleanValue()) {
            boolean alreadySlowed = this.sneak;
            this.sneak = true;
            if (alreadySlowed) return;

            EntityPlayerSP player = Minecraft.getMinecraft().player;

            if (player != null && player.capabilities.isFlying) return;
            this.moveStrafe = (float) ((double) this.moveStrafe * 0.3D);
            this.moveForward = (float) ((double) this.moveForward * 0.3D);
        }
    }
}
