package com.blue.bluesclient.feat.autounlock;

import net.minecraft.client.gui.inventory.GuiContainer;

public final class LockDriver {
    private static boolean active;

    private LockDriver() {}

    public static void enable() {
        active = true;
    }

    public static void disable() {
        active = false;
    }

    public static boolean isActive() {
        return active;
    }

    public static void onTick(GuiContainer gui, int selectedPin, int[] combination) {
        if (!active || combination == null || combination.length == 0) {
            return;
        }
        int step = LocksAccess.getCurrentIndex(gui);
        if (step >= combination.length) {
            LocksAccess.release(gui);
            active = false;
            return;
        }
        int dest = combination[step];
        if (selectedPin < dest) {
            LocksAccess.moveRight(gui);
            return;
        }
        if (selectedPin == dest) {
            LocksAccess.release(gui);
            LocksAccess.sendPin(gui);
            return;
        }
        LocksAccess.moveLeft(gui);
    }
}