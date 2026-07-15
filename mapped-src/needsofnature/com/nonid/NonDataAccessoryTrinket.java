/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.emi.trinkets.api.SlotReference
 *  dev.emi.trinkets.api.Trinket
 *  dev.emi.trinkets.api.TrinketComponent
 *  dev.emi.trinkets.api.TrinketsApi
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.Pair
 *  net.minecraft.registry.Registries
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
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.registry.Registries;

public final class NonDataAccessoryTrinket
implements Trinket {
    public static final NonDataAccessoryTrinket INSTANCE = new NonDataAccessoryTrinket();

    private NonDataAccessoryTrinket() {
    }

    public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (stack == null || stack.isEmpty() || slot == null || entity == null) {
            return false;
        }
        NonAccessoryBehavior behavior = NonAccessoryDefinitions.getBehavior(Registries.ITEM.getId((Object)stack.getItem()));
        String targetSlot = slot.getId();
        if (!NonTrinketsIntegration.isSlotAvailableFor(entity, targetSlot)) {
            return false;
        }
        return TrinketsApi.getTrinketComponent((LivingEntity)entity).map(component -> NonDataAccessoryTrinket.canEquipWithCurrent(component, stack, slot, behavior, targetSlot)).orElse(true);
    }

    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        NonTrinketsCompat.onAccessoryEquipped(entity, slot);
    }

    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        NonTrinketsCompat.onAccessoryUnequipped(entity, slot);
    }

    private static boolean canEquipWithCurrent(TrinketComponent component, ItemStack stack, SlotReference target, NonAccessoryBehavior behavior, String targetSlot) {
        String exclusiveGroup = behavior.exclusiveGroup();
        for (Pair pair : component.getAllEquipped()) {
            SlotReference otherSlot = (SlotReference)pair.getLeft();
            ItemStack otherStack = (ItemStack)pair.getRight();
            if (otherSlot == null || otherStack == null || otherStack.isEmpty() || NonDataAccessoryTrinket.isSameSlot(otherSlot, target)) continue;
            NonAccessoryBehavior otherBehavior = NonAccessoryDefinitions.getBehavior(Registries.ITEM.getId((Object)otherStack.getItem()));
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

