package com.blue.bluesclient.modmixins.locks;

import com.blue.bluesclient.config.BCConfig;
import com.blue.bluesclient.feat.autounlock.*;
import melonslise.locks.client.gui.LockPickingGui;
import melonslise.locks.client.gui.sprite.Sprite;
import melonslise.locks.common.container.LockPickingContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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

    @Unique
    private int unlockMode;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void bluesclient$unlockModeSwitch(
            LockPickingContainer cont, CallbackInfo ci) {
        LockDriver.disable();

        if (BCConfig.AutoUnlockInstant.getBooleanValue()) {
            this.unlockMode = 1;
            InstantUnlock.tryUnlock(this, this.length);
            return;
        }

        if (BCConfig.AutoUnlockPlus.getBooleanValue()) {
            this.unlockMode = 2;
            LocksController.onOpen(this, this.length);
            return;
        }

        if (BCConfig.AutoUnlockEnum.getBooleanValue()) {
            this.unlockMode = 3;
            EnumUnlock.begin(cont);
            return;
        }

        this.unlockMode = 0;
    }

    public LockPickingGuiMixin(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    @Inject(method = "updateScreen", at = @At("HEAD"), remap = true)
    private void bluesclient$onTick(CallbackInfo ci) {
        LockPickingGui gui = (LockPickingGui) (Object) this;
        LockPickingContainer cont = LocksAccess.container(gui);

        switch (this.unlockMode) {
            case 1:
                if (InstantUnlock.autoCloseGuiAfterUnlock(gui)) {
                    this.mc.player.closeScreen();
                }
                break;

            case 2:
                if (BCConfig.AutoUnlockPlus.getBooleanValue()) {
                    LocksController.onTick(this, this.getSelectedPin());
                }
                break;

            case 3:
                if (cont.isOpen()) {
                    EnumUnlock.end(cont);
                    this.mc.player.closeScreen();
                    return;
                }

                EnumUnlock.onTick(cont);
                break;

            default:
                break;
        }
    }

    @Inject(
            method = "updateLockPickSpeed",
            at = @At("RETURN"),
            remap = false
    )
    private void bluesclient$forcePickSpeed(CallbackInfo ci) {
        //本来可以用操作键盘来控制撬锁器，但是整合包内的Replay-Lock Mod对键盘操作有反作弊检验，
        //不允许未按下键盘的移动动作，因此这里转为直接修改Speed本身
        if (this.unlockMode != 2
                || !LocksController.canRun()
                || !LockDriver.isActive()
                || this.frozen) {
            return;
        }

        this.lockPick.speedX = LocksController.desiredPickSpeed();
    }
}