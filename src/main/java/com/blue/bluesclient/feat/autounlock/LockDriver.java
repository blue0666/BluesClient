package com.blue.bluesclient.feat.autounlock;

import net.minecraft.client.gui.inventory.GuiContainer;

public final class LockDriver {
    private static boolean active;
    private static float pickSpeed;

    private LockDriver() {}

    public static void enable() {
        active = true;
        pickSpeed = 0f;
    }

    public static void disable() {
        active = false;
        pickSpeed = 0f;
    }

    public static boolean isActive() {
        return active;
    }

    public static float desiredPickSpeed() {
        return pickSpeed;
    }

    public static void onTick(GuiContainer gui, int selectedPin, int[] combination) {
        pickSpeed = 0f;
        if (!active || combination == null || combination.length == 0) {
            return;
        }
        int step = LocksAccess.getCurrentIndex(gui);
        if (step >= combination.length) {
            active = false;
            return;
        }
        int dest = combination[step];
        if (selectedPin < dest) {
            pickSpeed = 4f;
            return;
        }
        if (selectedPin > dest) {
            pickSpeed = -4f;
            return;
        }
        LocksAccess.sendPin(gui);
    }
}