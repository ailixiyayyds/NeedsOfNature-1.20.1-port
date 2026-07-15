/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2960
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid.client;

import com.nonid.NonConfig;
import java.util.Locale;
import java.util.Map;
import net.minecraft.class_2960;
import org.jetbrains.annotations.Nullable;

public final class NonLiquidColors {
    public static final int MIXED_RGB = 15920063;
    public static final int ENTITY_RGB = 16776427;

    private NonLiquidColors() {
    }

    public static int toOpaqueArgb(int rgb) {
        return 0xFF000000 | rgb & 0xFFFFFF;
    }

    public static int resolveEntityRgb(@Nullable class_2960 entityTypeId, @Nullable NonConfig config) {
        if (entityTypeId == null || config == null) {
            return 16776427;
        }
        Integer override = config.getLiquidColorOverrideRgb(entityTypeId.toString());
        if (override != null) {
            return override;
        }
        return 16776427;
    }

    @Nullable
    public static String normalizeHexColor(@Nullable String raw) {
        if (raw == null) {
            return null;
        }
        String text = raw.trim();
        if (text.isEmpty()) {
            return null;
        }
        if (text.startsWith("#")) {
            text = text.substring(1);
        }
        if (text.length() != 6) {
            return null;
        }
        for (int i = 0; i < text.length(); ++i) {
            boolean isHex;
            char c = text.charAt(i);
            boolean bl = isHex = c >= '0' && c <= '9' || c >= 'a' && c <= 'f' || c >= 'A' && c <= 'F';
            if (isHex) continue;
            return null;
        }
        return text.toUpperCase(Locale.ROOT);
    }

    @Nullable
    public static Integer parseHexColor(@Nullable String raw) {
        String normalized = NonLiquidColors.normalizeHexColor(raw);
        if (normalized == null) {
            return null;
        }
        try {
            return Integer.parseInt(normalized, 16) & 0xFFFFFF;
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    @Nullable
    public static Integer resolveEntityOverrideFromMap(@Nullable Map<String, String> map, @Nullable String entityId) {
        if (map == null || entityId == null) {
            return null;
        }
        String hex = map.get(entityId);
        if (hex == null) {
            return null;
        }
        return NonLiquidColors.parseHexColor(hex);
    }
}

