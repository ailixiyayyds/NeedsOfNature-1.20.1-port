/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid.data;

import com.nonid.data.NonLiquidRoles;
import java.util.Locale;
import java.util.Set;
import org.jetbrains.annotations.Nullable;

public record NonAccessoryBehavior(Set<String> occupiesSlots, Set<NonLiquidRoles.InjectorRole> blocksInjectorTypes, @Nullable String exclusiveGroup, int maxDurability, int protectionDurabilityCost, boolean ignoreInjectorSlotVisualShedding, boolean ignoreInjectorSlotEffectSuppression, int vInjectionDurabilityCost, int aInjectionDurabilityCost, boolean blocksPregnancy) {
    public static final NonAccessoryBehavior NONE = new NonAccessoryBehavior(Set.of(), Set.of(), null, 0, 1, false, false, 0, 0, false);

    public boolean isEmpty() {
        return this.occupiesSlots.isEmpty() && this.blocksInjectorTypes.isEmpty() && (this.exclusiveGroup == null || this.exclusiveGroup.isBlank()) && !this.ignoreInjectorSlotVisualShedding && !this.ignoreInjectorSlotEffectSuppression && this.vInjectionDurabilityCost <= 0 && this.aInjectionDurabilityCost <= 0 && !this.blocksPregnancy;
    }

    public boolean occupiesSlot(String slotId) {
        if (slotId == null) {
            return false;
        }
        String normalized = NonAccessoryBehavior.normalizeSlot(slotId);
        for (String occupied : this.occupiesSlots) {
            if (!NonAccessoryBehavior.isSlot(normalized, occupied)) continue;
            return true;
        }
        return false;
    }

    public boolean blocks(NonLiquidRoles.InjectorRole role) {
        return role != null && this.blocksInjectorTypes.contains((Object)role);
    }

    public static String normalizeSlot(String slotId) {
        if (slotId == null) {
            return "";
        }
        String slot = slotId.trim().toLowerCase(Locale.ROOT);
        if ("v".equals(slot)) {
            return "legs/v";
        }
        if ("a".equals(slot)) {
            return "legs/a";
        }
        if ("d".equals(slot)) {
            return "legs/d";
        }
        return slot;
    }

    public static boolean isSlot(String actual, String expected) {
        String normalizedExpected;
        if (actual == null || expected == null) {
            return false;
        }
        String normalizedActual = NonAccessoryBehavior.normalizeSlot(actual);
        return normalizedActual.equals(normalizedExpected = NonAccessoryBehavior.normalizeSlot(expected)) || normalizedActual.startsWith(normalizedExpected + "/");
    }
}

