/*
 * Decompiled with CFR 0.152.
 */
package com.afwid.api;

import java.util.Locale;

public enum AfwDamageBehavior {
    STOP_ON_DAMAGE,
    IGNORE_DAMAGE,
    BLOCK_DAMAGE;


    public String id() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public static AfwDamageBehavior fromId(String id, AfwDamageBehavior fallback) {
        String norm;
        if (id == null || id.isBlank()) {
            return fallback;
        }
        return switch (norm = id.trim().toLowerCase(Locale.ROOT)) {
            case "stop_on_damage", "stop", "stopondamage" -> STOP_ON_DAMAGE;
            case "ignore_damage", "ignore", "ignoredamage" -> IGNORE_DAMAGE;
            case "block_damage", "block", "blockdamage" -> BLOCK_DAMAGE;
            default -> fallback;
        };
    }
}

