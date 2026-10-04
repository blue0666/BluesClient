package com.blue.bluesclient.modmixins.locks;

import com.blue.bluesclient.feat.autounlock.LocksController;
import melonslise.locks.client.gui.LockPickingGui;
import melonslise.locks.common.container.LockPickingContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LockPickingGui.class, remap = false)
public abstract class LockPickingGuiMixin extends GuiContainer {
    @Shadow
    @org.spongepowered.asm.mixin.Final
    public int length;

    @Shadow
    protected abstract int getSelectedPin();

    public LockPickingGuiMixin(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void bluesclient$onOpen(LockPickingContainer cont, CallbackInfo ci) {
        LocksController.onOpen(this, this.length);
    }

    @Inject(method = "handlePin", at = @At("HEAD"))
    private void bluesclient$onPin(boolean correct, boolean reset, CallbackInfo ci) {
        LocksController.onPin(correct, reset);
    }

    @Inject(method = "updateScreen", at = @At("RETURN"), remap = true)
    private void bluesclient$onTick(CallbackInfo ci) {
        LocksController.onTick(this, this.getSelectedPin());
    }
}
