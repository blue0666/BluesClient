package com.blue.bluesclient.modmixins.locks;

import com.blue.bluesclient.config.BCConfig;
import com.blue.bluesclient.feat.autounlock.LocksController;
import melonslise.locks.client.gui.LockPickingGui;
import melonslise.locks.client.gui.sprite.Sprite;
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
    protected boolean frozen;

    @Shadow
    protected Sprite lockPick;

    @Shadow
    protected abstract int getSelectedPin();

    public LockPickingGuiMixin(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void bluesclient$onOpen(LockPickingContainer cont, CallbackInfo ci) {
        LocksController.onOpen(this, this.length);
    }

    @Inject(method = "updateScreen", at = @At("HEAD"), remap = true)
    private void bluesclient$onTick(CallbackInfo ci) {
        LocksController.onTick(this, this.getSelectedPin());
    }

    @Inject(method = "updateLockPickSpeed", at = @At("RETURN"), remap = false, cancellable = true)
    private void bluesclient$forcePickSpeed(CallbackInfo ci) {
        //本来可以用操作键盘来控制撬锁器，但是整合包内键盘的事件太多了(与Debris同时开启的时候被覆写)，因此转为直接修改操作撬锁器本身
        if(!BCConfig.AutoUnlockPlus.getBooleanValue()) return;
        if (this.frozen) {
            return;
        }
        this.lockPick.speedX = LocksController.desiredPickSpeed();
    }
}