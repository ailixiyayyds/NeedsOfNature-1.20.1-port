/*
 * Decompiled with CFR 0.152.
 */
package com.afwid;

import com.afwid.AfwDebugChatCategory;

public enum AfwDebugChatMode {
    ALL("all"),
    SETUP_WARNINGS_ERRORS("setup_warnings_errors"),
    SETUP_ERRORS("setup_errors"),
    ERRORS_ONLY("errors_only");

    private final String id;

    private AfwDebugChatMode(String id) {
        this.id = id;
    }

    public String id() {
        return this.id;
    }

    public boolean allows(AfwDebugChatCategory category) {
        if (category == AfwDebugChatCategory.ALWAYS) {
            return true;
        }
        return switch (this) {
            case ALL -> true;
            case SETUP_WARNINGS_ERRORS -> {
                if (category == AfwDebugChatCategory.SETUP || category == AfwDebugChatCategory.WARNING || category == AfwDebugChatCategory.ERROR) {
                    yield true;
                }
                yield false;
            }
            case SETUP_ERRORS -> {
                if (category == AfwDebugChatCategory.SETUP || category == AfwDebugChatCategory.ERROR) {
                    yield true;
                }
                yield false;
            }
            case ERRORS_ONLY -> category == AfwDebugChatCategory.ERROR;
        };
    }

    public static AfwDebugChatMode fromId(String id, AfwDebugChatMode fallback) {
        if (id != null) {
            for (AfwDebugChatMode mode : AfwDebugChatMode.values()) {
                if (!mode.id.equalsIgnoreCase(id)) continue;
                return mode;
            }
        }
        return fallback;
    }
}

