package com.blue.bluesclient.mixins.alwayssneak;

import com.blue.bluesclient.config.BCConfig;
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
            this.sneak = true;
        }
    }
}
