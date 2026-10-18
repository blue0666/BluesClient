package com.blue.bluesclient.feat.autounlock;

import com.blue.bluesclient.config.BCConfig;
import melonslise.locks.common.container.LockPickingContainer;
import melonslise.locks.common.init.LocksNetworks;
import melonslise.locks.common.network.toserver.CheckPinPacket;

import java.util.*;

public class EnumUnlock {
    private static LockPickingContainer currentContainer;
    private static Memory memory;
    private static final Deque<Integer> pending = new ArrayDeque<>();
    // 当前已经确认的服务端步骤
    private static int confirmedStep;
    // 当前批次的扫描方向
    private static boolean ascending = true;
    // 当前批次是否发生过断裂
    private static boolean batchReset;
    private static boolean stopped;

    private static final Map<Integer, Memory> memories = new HashMap<>();

    public static boolean canRun(){
        return BCConfig.AutoUnlockEnum.getBooleanValue();
    }

    private static class Memory {
        final int[] known;
        final boolean[][] rejected;

        Memory(int length) {
            this.known = new int[length];
            Arrays.fill(this.known, -1);

            this.rejected = new boolean[length][length];
        }
    }

    public static void begin(LockPickingContainer container) {
        int id = container.lockable.lock.id;
        int length = container.lockable.lock.getLength();

        Memory savedMemory = memories.get(id);

        if (savedMemory == null || savedMemory.known.length != length) {
            savedMemory = new Memory(length);
            memories.put(id, savedMemory);
        }

        begin(container, savedMemory);
    }

    private static void begin(
            LockPickingContainer container,
            Memory savedMemory) {

        currentContainer = container;
        memory = savedMemory;

        pending.clear();
        confirmedStep = container.getCurrentIndex();

        ascending = true;
        batchReset = false;
        stopped = false;
    }

    private static List<Integer> buildBatch() {
        List<Integer> batch = new ArrayList<>();

        int length = memory.known.length;
        int step = confirmedStep;

        while (step < length && memory.known[step] >= 0) {
            batch.add(memory.known[step]);
            step++;
        }

        if (step >= length) {
            return batch;
        }

        for (int i = 0; i < length; i++) {
            int pin = ascending ? i : length - 1 - i;

            if (memory.rejected[step][pin]) {
                continue;
            }

            boolean used = false;

            for (int previous = 0; previous < step; previous++) {
                if (memory.known[previous] == pin) {
                    used = true;
                    break;
                }
            }

            if (!used) {
                batch.add(pin);
            }
        }

        return batch;
    }

    public static void onTick(LockPickingContainer container) {
        if (!canRun()
                || stopped
                || container != currentContainer
                || memory == null
                || !pending.isEmpty()) {
            return;
        }

        if (container.isOpen()) {
            return;
        }

        if (!container.isValidPick(
                container.player.getHeldItem(container.hand))) {
            return;
        }

        if (container.getCurrentIndex() != confirmedStep) {
            stopped = true;
            return;
        }

        List<Integer> batch = buildBatch();

        if (batch.isEmpty()) {
            stopped = true;
            return;
        }

        batchReset = false;

        pending.addAll(batch);

        for (int pin : batch) {
            LocksNetworks.MAIN.sendToServer(
                    new CheckPinPacket((byte) pin)
            );
        }
    }

    public static void onResult(
            LockPickingContainer container,
            boolean correct,
            boolean reset) {

        if (container != currentContainer
                || memory == null
                || pending.isEmpty()) {
            return;
        }

        int pin = pending.removeFirst();
        int step = confirmedStep;

        if (step < 0 || step >= memory.known.length) {
            stopped = true;
            return;
        }

        if (correct) {
            // 服务端说正确但与旧记忆冲突就停止，避免污染记忆
            if (memory.rejected[step][pin]
                    || (memory.known[step] >= 0
                    && memory.known[step] != pin)) {
                stopped = true;
            } else {
                memory.known[step] = pin;
            }

            confirmedStep++;
        } else {
            if (memory.known[step] == pin) {
                stopped = true;
            } else {
                memory.rejected[step][pin] = true;
            }
        }

        if (reset) {
            confirmedStep = 0;
            batchReset = true;
        }

        if (container.getCurrentIndex() != confirmedStep) {
            stopped = true;
        }

        if (container.isOpen()) {
            pending.clear();
            stopped = true;
            return;
        }

        if (pending.isEmpty() && !batchReset) {
            ascending = !ascending;
        }
    }

    public static void end(LockPickingContainer container) {
        if (container != currentContainer) {
            return;
        }

        currentContainer = null;
        memory = null;
        pending.clear();
        stopped = true;
    }
}
