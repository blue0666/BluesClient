package com.blue.bluesclient.feat.autounlock;

import com.blue.bluesclient.config.BCConfig;
import melonslise.locks.client.gui.LockPickingGui;
import melonslise.locks.common.init.LocksNetworks;
import melonslise.locks.common.network.toserver.CheckPinPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;

import java.util.OptionalLong;

public final class InstantUnlock {
    private static boolean pendingClose = false;
    private InstantUnlock() {}

    public static boolean canRun() {
        return BCConfig.AutoUnlockInstant.getBooleanValue()
                && UnlockSeed.resolve().isPresent();
    }

    public static void tryUnlock(GuiContainer gui, int guiLength) {
        if (!canRun()) return;

        OptionalLong seed = UnlockSeed.resolve();
        if (!seed.isPresent()) return;

        int id = LocksAccess.getLockId(gui);
        int length = LocksAccess.getLockLength(gui);
        if (guiLength > 0) {
            length = guiLength;
        }

        int[] combination = LockCombination.compute(id, seed.getAsLong(), length);

        for (int pin : combination) {
            LocksNetworks.MAIN.sendToServer(new CheckPinPacket((byte) pin));
        }
        pendingClose = true;
    }

    public static boolean autoCloseGuiAfterUnlock(LockPickingGui gui) {
        if(!BCConfig.AutoUnlockInstant.getBooleanValue()) return false;
        if (!pendingClose) return false;
        if (!LocksAccess.container(gui).isOpen()) return false;
        pendingClose = false;
        return true;
    }
}