package com.blue.bluesclient.feat.autounlock;

import melonslise.locks.client.gui.LockPickingGui;
import melonslise.locks.common.container.LockPickingContainer;
import net.minecraft.client.gui.inventory.GuiContainer;

public final class LocksAccess {
    private LocksAccess() {}

    public static LockPickingContainer container(LockPickingGui gui) {
        return (LockPickingContainer) gui.inventorySlots;
    }

    public static int getCurrentIndex(GuiContainer gui) {
        return container((LockPickingGui) gui).getCurrentIndex();
    }

    public static int getLockId(GuiContainer gui) {
        return container((LockPickingGui) gui).lockable.lock.id;
    }

    public static int getLockLength(GuiContainer gui) {
        return container((LockPickingGui) gui).lockable.lock.getLength();
    }

    public static void moveRight(GuiContainer screen) {
        ((LockPickingGui) screen).keyReleased(screen.mc.gameSettings.keyBindLeft.getKeyCode());
        ((LockPickingGui) screen).keyPressed(screen.mc.gameSettings.keyBindRight.getKeyCode());
    }

    public static void moveLeft(GuiContainer screen) {
        ((LockPickingGui) screen).keyReleased(screen.mc.gameSettings.keyBindRight.getKeyCode());
        ((LockPickingGui) screen).keyPressed(screen.mc.gameSettings.keyBindLeft.getKeyCode());
    }

    public static void release(GuiContainer screen) {
        ((LockPickingGui) screen).keyReleased(screen.mc.gameSettings.keyBindLeft.getKeyCode());
        ((LockPickingGui) screen).keyReleased(screen.mc.gameSettings.keyBindRight.getKeyCode());
    }

    public static void sendPin(GuiContainer screen) {
        ((LockPickingGui) screen).keyPressed(screen.mc.gameSettings.keyBindForward.getKeyCode());
    }
}