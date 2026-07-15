/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityType
 *  net.minecraft.util.Identifier
 *  net.minecraft.registry.Registries
 */
package com.nonid.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;

public final class NonStatLabelResolver {
    private static final String STAT_PREFIX = "stat.needsofnature.";
    private static final String PEAK_ENTITY_PREFIX = "stat.needsofnature.peaked_animations_entity_";
    private static final String LIQUID_ENTITY_PREFIX = "stat.needsofnature.liquid_gained_ml_entity_";
    private static final String OFFSPRING_ENTITY_PREFIX = "stat.needsofnature.offspring_spawned_entity_";
    private static final Map<String, String> ENTITY_NAME_CACHE = new ConcurrentHashMap<String, String>();

    private NonStatLabelResolver() {
    }

    public static String resolve(String key) {
        if (key == null || key.isBlank()) {
            return null;
        }
        if (key.startsWith(PEAK_ENTITY_PREFIX)) {
            String suffix = key.substring(PEAK_ENTITY_PREFIX.length());
            String entityName = NonStatLabelResolver.resolveEntityNameFromSuffix(suffix);
            return "NoN: Peaked Animations (" + entityName + ")";
        }
        if (key.startsWith(LIQUID_ENTITY_PREFIX)) {
            String suffix = key.substring(LIQUID_ENTITY_PREFIX.length());
            String entityName = NonStatLabelResolver.resolveEntityNameFromSuffix(suffix);
            return "NoN: Liquid Gained (ml) (" + entityName + ")";
        }
        if (key.startsWith(OFFSPRING_ENTITY_PREFIX)) {
            String suffix = key.substring(OFFSPRING_ENTITY_PREFIX.length());
            String entityName = NonStatLabelResolver.resolveEntityNameFromSuffix(suffix);
            return "NoN: Offspring Spawned (" + entityName + ")";
        }
        return null;
    }

    private static String resolveEntityNameFromSuffix(String suffix) {
        if (suffix == null || suffix.isBlank()) {
            return "Unknown";
        }
        return ENTITY_NAME_CACHE.computeIfAbsent(suffix, NonStatLabelResolver::lookupEntityName);
    }

    private static String lookupEntityName(String suffix) {
        for (Identifier entityId : Registries.ENTITY_TYPE.getIds()) {
            if (!suffix.equals(NonStatLabelResolver.sanitizeEntityId(entityId))) continue;
            EntityType type = (EntityType)Registries.ENTITY_TYPE.get(entityId);
            return type.getName().getString();
        }
        return NonStatLabelResolver.prettifySuffix(suffix);
    }

    private static String sanitizeEntityId(Identifier entityId) {
        return NonStatLabelResolver.sanitizePathPart(entityId.getNamespace()) + "_" + NonStatLabelResolver.sanitizePathPart(entityId.getPath());
    }

    private static String sanitizePathPart(String value) {
        if (value == null || value.isBlank()) {
            return "unknown";
        }
        StringBuilder out = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); ++i) {
            char c = value.charAt(i);
            if (c >= 'a' && c <= 'z') {
                out.append(c);
                continue;
            }
            if (c >= '0' && c <= '9') {
                out.append(c);
                continue;
            }
            out.append('_');
        }
        if (out.isEmpty()) {
            return "unknown";
        }
        return out.toString();
    }

    private static String prettifySuffix(String suffix) {
        String normalized = suffix.replace('_', ' ').trim();
        if (normalized.isEmpty()) {
            return "Unknown";
        }
        StringBuilder out = new StringBuilder(normalized.length());
        boolean cap = true;
        for (int i = 0; i < normalized.length(); ++i) {
            char c = normalized.charAt(i);
            if (cap && c >= 'a' && c <= 'z') {
                out.append((char)(c - 32));
            } else {
                out.append(c);
            }
            cap = c == ' ';
        }
        return out.toString();
    }
}

