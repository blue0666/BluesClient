package com.blue.bluesclient.feat.autounlock;

import java.util.Random;

public final class LockCombination {
    private LockCombination() {}

    public static int[] compute(int lockId, long worldSeed, int length) {
        if (length <= 0) {
            return new int[0];
        }
        long mixed = lockId ^ (Math.abs(worldSeed) * 17317L + worldSeed);
        Random rng = new Random(mixed);
        byte[] a = new byte[length];
        for (byte i = 0; i < length; i++) {
            a[i] = i;
        }
        for (int i = length - 1; i > 0; i--) {
            int j = rng.nextInt(i + 1);
            byte t = a[j];
            a[j] = a[i];
            a[i] = t;
        }
        int[] out = new int[length];
        for (int i = 0; i < length; i++) {
            out[i] = a[i];
        }
        return out;
    }
}