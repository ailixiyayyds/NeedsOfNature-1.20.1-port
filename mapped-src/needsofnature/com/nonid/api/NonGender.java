/*
 * Decompiled with CFR 0.152.
 */
package com.nonid.api;

public enum NonGender {
    NONE(0),
    MALE(1),
    FEMALE(2),
    BOTH(3);

    private final int mask;

    private NonGender(int mask) {
        this.mask = mask;
    }

    public int mask() {
        return this.mask;
    }

    public static NonGender fromMask(int mask) {
        return switch (mask & 3) {
            case 1 -> MALE;
            case 2 -> FEMALE;
            case 3 -> BOTH;
            default -> NONE;
        };
    }
}

