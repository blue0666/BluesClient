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
        System.out.println("锁ID"+id+"Seed"+seed);
        combination = LockCombination.compute(id, seed.getAsLong(), length);
        System.out.println(Arrays.toString(combination));
        LockDriver.enable();
    }

    public static void onPin(boolean correct, boolean reset) {
    }
    public static void onTick(GuiContainer gui, int selectedPin) {
        if (!canRun()) {
            return;
        }
        LockDriver.onTick(gui, selectedPin, combination);
    }
}