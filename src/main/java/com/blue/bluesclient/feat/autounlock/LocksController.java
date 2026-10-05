package com.blue.bluesclient.feat.autounlock;

import com.blue.bluesclient.config.BCConfig;
import net.minecraft.client.gui.inventory.GuiContainer;

import java.util.Arrays;
import java.util.OptionalLong;

public final class LocksController {
    private static int[] combination;

    private LocksController() {}

    public static boolean canRun() {
        return BCConfig.AutoUnlockPlus.getBooleanValue() && UnlockSeed.resolve().isPresent();
    }

    public static void onOpen(GuiContainer gui, int guiLength) {
        LockDriver.disable();
        combination = null;
        if (!canRun()) {
            return;
        }
        OptionalLong seed = UnlockSeed.resolve();
        if (!seed.isPresent()) {
            return;
        }
        int id = LocksAccess.getLockId(gui);
        int length = LocksAccess.getLockLength(gui);
        if (guiLength > 0) {
            length = guiLength;
        }
        combination = LockCombination.compute(id, seed.getAsLong(), length);
        LockDriver.enable();
    }

    public static void onTick(GuiContainer gui, int selectedPin) {
        if (!canRun()) {
            return;
        }
        LockDriver.onTick(gui, selectedPin, combination);
    }

    public static float desiredPickSpeed() {
        return LockDriver.desiredPickSpeed();
    }
}