package com.blue.bluesclient.modmixins.locks;

import com.blue.bluesclient.feat.autounlock.EnumUnlock;
import melonslise.locks.common.container.LockPickingContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LockPickingContainer.class, remap = false)
public class LockPickingContainerMixin {

    @Inject(
            method = "handlePin",
            at = @At("TAIL"),
            require = 1
    )
    private void bluesclient$onResult(boolean correct, boolean reset, CallbackInfo ci) {
        EnumUnlock.onResult((LockPickingContainer) (Object) this, correct, reset);
    }
}
