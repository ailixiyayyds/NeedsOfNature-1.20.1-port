/*
 * Decompiled with CFR 0.152.
 */
package com.nonid;

import com.nonid.NonDebugChatCategory;

public enum NonDebugChatMode {
    ALL("all"),
    SETUP_WARNINGS_ERRORS("setup_warnings_errors"),
    SETUP_ERRORS("setup_errors"),
    ERRORS_ONLY("errors_only");

    private final String id;

    private NonDebugChatMode(String id) {
        this.id = id;
    }

    public String id() {
        return this.id;
    }

    public boolean allows(NonDebugChatCategory category) {
        if (category == NonDebugChatCategory.ALWAYS) {
            return true;
        }
        return switch (this.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> true;
            case 1 -> {
                if (category == NonDebugChatCategory.SETUP || category == NonDebugChatCategory.WARNING || category == NonDebugChatCategory.ERROR) {
                    yield true;
                }
                yield false;
            }
            case 2 -> {
                if (category == NonDebugChatCategory.SETUP || category == NonDebugChatCategory.ERROR) {
                    yield true;
                }
                yield false;
            }
            case 3 -> category == NonDebugChatCategory.ERROR;
        };
    }

    public static NonDebugChatMode fromId(String id, NonDebugChatMode fallback) {
        if (id != null) {
            for (NonDebugChatMode mode : NonDebugChatMode.values()) {
                if (!mode.id.equalsIgnoreCase(id)) continue;
                return mode;
            }
        }
        return fallback;
    }
}

