/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.emi.trinkets.api.SlotReference
 *  dev.emi.trinkets.api.Trinket
 *  dev.emi.trinkets.api.TrinketComponent
 *  dev.emi.trinkets.api.TrinketsApi
 *  net.minecraft.class_1309
 *  net.minecraft.class_1799
 *  net.minecraft.class_3545
 *  net.minecraft.class_7923
 */
package com.nonid;

import com.nonid.NonTrinketsCompat;
import com.nonid.NonTrinketsIntegration;
import com.nonid.data.NonAccessoryBehavior;
import com.nonid.data.NonAccessoryDefinitions;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import java.util.Objects;
import net.minecraft.class_1309;
import net.minecraft.class_1799;
import net.minecraft.class_3545;
import net.minecraft.class_7923;

public final class NonDataAccessoryTrinket
implements Trinket {
    public static final NonDataAccessoryTrinket INSTANCE = new NonDataAccessoryTrinket();

    private NonDataAccessoryTrinket() {
    }

    public boolean canEquip(class_1799 stack, SlotReference slot, class_1309 entity) {
        if (stack == null || stack.method_7960() || slot == null || entity == null) {
            return false;
        }
        NonAccessoryBehavior behavior = NonAccessoryDefinitions.getBehavior(class_7923.field_41178.method_10221((Object)stack.method_7909()));
        String targetSlot = slot.getId();
        if (!NonTrinketsIntegration.isSlotAvailableFor(entity, targetSlot)) {
            return false;
        }
        return TrinketsApi.getTrinketComponent((class_1309)entity).map(component -> NonDataAccessoryTrinket.canEquipWithCurrent(component, stack, slot, behavior, targetSlot)).orElse(true);
    }

    public void onEquip(class_1799 stack, SlotReference slot, class_1309 entity) {
        NonTrinketsCompat.onAccessoryEquipped(entity, slot);
    }

    public void onUnequip(class_1799 stack, SlotReference slot, class_1309 entity) {
        NonTrinketsCompat.onAccessoryUnequipped(entity, slot);
    }

    private static boolean canEquipWithCurrent(TrinketComponent component, class_1799 stack, SlotReference target, NonAccessoryBehavior behavior, String targetSlot) {
        String exclusiveGroup = behavior.exclusiveGroup();
        for (class_3545 pair : component.getAllEquipped()) {
            SlotReference otherSlot = (SlotReference)pair.method_15442();
            class_1799 otherStack = (class_1799)pair.method_15441();
            if (otherSlot == null || otherStack == null || otherStack.method_7960() || NonDataAccessoryTrinket.isSameSlot(otherSlot, target)) continue;
            NonAccessoryBehavior otherBehavior = NonAccessoryDefinitions.getBehavior(class_7923.field_41178.method_10221((Object)otherStack.method_7909()));
            if (otherBehavior.occupiesSlot(targetSlot)) {
                return false;
            }
            for (String occupied : behavior.occupiesSlots()) {
                if (!NonAccessoryBehavior.isSlot(otherSlot.getId(), occupied)) continue;
                return false;
            }
            if (exclusiveGroup == null || exclusiveGroup.isBlank() || !exclusiveGroup.equals(otherBehavior.exclusiveGroup())) continue;
            return false;
        }
        return true;
    }

    private static boolean isSameSlot(SlotReference first, SlotReference second) {
        return first != null && second != null && first.index() == second.index() && Objects.equals(first.getId(), second.getId());
    }
}

