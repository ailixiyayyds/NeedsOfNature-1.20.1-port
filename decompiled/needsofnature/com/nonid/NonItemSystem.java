/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_10712
 *  net.minecraft.class_1268
 *  net.minecraft.class_1299
 *  net.minecraft.class_1761$class_7704
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1842
 *  net.minecraft.class_1844
 *  net.minecraft.class_1935
 *  net.minecraft.class_2561
 *  net.minecraft.class_2960
 *  net.minecraft.class_3222
 *  net.minecraft.class_4174
 *  net.minecraft.class_4174$class_4175
 *  net.minecraft.class_6880
 *  net.minecraft.class_7923
 *  net.minecraft.class_9334
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid;

import com.nonid.LiquidHolder;
import com.nonid.NonConfig;
import com.nonid.NonLiquidSystem;
import com.nonid.item.EnergyStabilizerItem;
import com.nonid.potion.NonPotions;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.class_10712;
import net.minecraft.class_1268;
import net.minecraft.class_1299;
import net.minecraft.class_1761;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1842;
import net.minecraft.class_1844;
import net.minecraft.class_1935;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_3222;
import net.minecraft.class_4174;
import net.minecraft.class_6880;
import net.minecraft.class_7923;
import net.minecraft.class_9334;
import org.jetbrains.annotations.Nullable;

final class NonItemSystem {
    private static final class_4174 LIQUID_BOTTLE_FOOD = new class_4174.class_4175().method_19238(1).method_19237(0.0f).method_19240().method_19242();
    private static final class_4174 FERTILE_NECTAR_FOOD = new class_4174.class_4175().method_19238(7).method_19237(0.0f).method_19240().method_19242();
    private static final class_10712 LIQUID_BOTTLE_TOOLTIP = class_10712.field_56318.method_67215(class_9334.field_49651, true);

    private NonItemSystem() {
    }

    static void giveItemOrDrop(class_3222 player, class_1799 stack) {
        if (player == null || stack == null || stack.method_7960()) {
            return;
        }
        if (!player.method_31548().method_7394(stack)) {
            player.method_7328(stack, false);
        }
    }

    static boolean consumeOneGlassBottle(class_3222 player, class_1268 hand) {
        if (player == null || hand == null) {
            return false;
        }
        class_1799 stack = player.method_5998(hand);
        if (stack.method_7960() || !stack.method_31574(class_1802.field_8469)) {
            return false;
        }
        stack.method_7934(1);
        return true;
    }

    static void refundGlassBottle(class_3222 player) {
        NonItemSystem.giveItemOrDrop(player, new class_1799((class_1935)class_1802.field_8469));
    }

    static class_1799 createBottleStackForTank(LiquidHolder holder) {
        class_2960 entityTypeId = null;
        if (holder != null && holder.getLiquidComposition() == LiquidHolder.LiquidComposition.ENTITY) {
            entityTypeId = holder.getLiquidEntityTypeId();
        }
        return NonItemSystem.createLiquidBottleStack(entityTypeId);
    }

    static class_1799 createLiquidBottleStack(@Nullable class_2960 entityTypeId) {
        int tint = NonLiquidSystem.resolveBottleTintRgb(entityTypeId);
        class_1844 contents = new class_1844(Optional.of(class_7923.field_41179.method_47983((Object)NonPotions.LIQUID)), Optional.of(tint), List.of(), Optional.empty());
        class_1799 stack = new class_1799((class_1935)class_1802.field_8574);
        stack.method_57379(class_9334.field_49651, (Object)contents);
        stack.method_57379(class_9334.field_50071, (Object)16);
        stack.method_57379(class_9334.field_50075, (Object)LIQUID_BOTTLE_FOOD);
        NonPotions.applyHoneyBottleDrinkSound(stack);
        stack.method_57379(class_9334.field_56400, (Object)LIQUID_BOTTLE_TOOLTIP);
        NonPotions.setLiquidBottleEntityTypeId(stack, entityTypeId);
        if (entityTypeId == null) {
            stack.method_57379(class_9334.field_49631, (Object)class_2561.method_43471((String)"item.needsofnature.mixed_liquid_bottle").method_27661().method_27694(style -> style.method_10978(Boolean.valueOf(false))));
        } else {
            stack.method_57379(class_9334.field_49631, (Object)class_2561.method_43469((String)"item.needsofnature.entity_liquid_bottle", (Object[])new Object[]{NonItemSystem.resolveEntityNameText(entityTypeId)}).method_27661().method_27694(style -> style.method_10978(Boolean.valueOf(false))));
        }
        return stack;
    }

    static void addEntityLiquidAndStabilizersToItemGroup(class_1761.class_7704 entries, @Nullable NonConfig config) {
        for (String rawEntityId : NonLiquidSystem.resolveEffectiveLiquidGainMap().keySet()) {
            class_2960 entityId = class_2960.method_12829((String)rawEntityId);
            if (entityId == null) continue;
            entries.method_45420(NonItemSystem.createLiquidBottleStack(entityId));
            entries.method_45420(NonItemSystem.createEnergyStabilizerStack(entityId));
        }
    }

    static void addNonPotionVariantsToItemGroup(class_1761.class_7704 entries) {
        ArrayList<class_6880> nonPotions = new ArrayList<class_6880>();
        for (class_1842 potion : class_7923.field_41179) {
            class_2960 potionId = class_7923.field_41179.method_10221((Object)potion);
            if (potionId == null || !"needsofnature".equals(potionId.method_12836())) continue;
            nonPotions.add(class_7923.field_41179.method_47983((Object)potion));
        }
        entries.method_45420(NonItemSystem.createLiquidBottleStack(null));
        for (class_6880 potionEntry : nonPotions) {
            if (potionEntry.comp_349() == NonPotions.LIQUID) continue;
            entries.method_45420(NonItemSystem.createPotionVariantStack(class_1802.field_8574, (class_6880<class_1842>)potionEntry));
        }
        for (class_6880 potionEntry : nonPotions) {
            if (potionEntry.comp_349() == NonPotions.LIQUID) continue;
            entries.method_45420(NonItemSystem.createPotionVariantStack(class_1802.field_8436, (class_6880<class_1842>)potionEntry));
        }
        for (class_6880 potionEntry : nonPotions) {
            if (potionEntry.comp_349() == NonPotions.LIQUID) continue;
            entries.method_45420(NonItemSystem.createPotionVariantStack(class_1802.field_8150, (class_6880<class_1842>)potionEntry));
        }
        for (class_6880 potionEntry : nonPotions) {
            if (potionEntry.comp_349() == NonPotions.LIQUID) continue;
            entries.method_45420(NonItemSystem.createPotionVariantStack(class_1802.field_8087, (class_6880<class_1842>)potionEntry));
        }
    }

    static class_1799 createPotionVariantStack(class_1792 item, class_6880<class_1842> potionEntry) {
        class_1799 stack = new class_1799((class_1935)item);
        stack.method_57379(class_9334.field_49651, (Object)new class_1844(potionEntry));
        if (item == class_1802.field_8574 && potionEntry.comp_349() == NonPotions.FERTILE_NECTAR) {
            stack.method_57379(class_9334.field_50075, (Object)FERTILE_NECTAR_FOOD);
            stack.method_57379(class_9334.field_50071, (Object)16);
            NonPotions.applyHoneyBottleDrinkSound(stack);
        }
        return stack;
    }

    static boolean isNonPotionVariantStack(class_1799 stack) {
        boolean potionLike;
        if (stack == null || stack.method_7960()) {
            return false;
        }
        class_1792 item = stack.method_7909();
        boolean bl = potionLike = item == class_1802.field_8574 || item == class_1802.field_8436 || item == class_1802.field_8150 || item == class_1802.field_8087;
        if (!potionLike) {
            return false;
        }
        class_1844 contents = (class_1844)stack.method_58695(class_9334.field_49651, (Object)class_1844.field_49274);
        Optional potionOpt = contents.comp_2378();
        if (potionOpt.isEmpty()) {
            return false;
        }
        class_2960 potionId = class_7923.field_41179.method_10221((Object)((class_1842)((class_6880)potionOpt.get()).comp_349()));
        return potionId != null && "needsofnature".equals(potionId.method_12836());
    }

    static class_1799 createEnergyStabilizerStack(@Nullable class_2960 entityTypeId) {
        return EnergyStabilizerItem.createStackForEntity(entityTypeId);
    }

    private static class_2561 resolveEntityNameText(class_2960 entityTypeId) {
        if (entityTypeId == null) {
            return class_2561.method_43470((String)"Unknown");
        }
        return class_7923.field_41177.method_17966(entityTypeId).map(class_1299::method_5897).orElseGet(() -> class_2561.method_43470((String)NonItemSystem.formatFallbackEntityName(entityTypeId)));
    }

    private static String formatFallbackEntityName(class_2960 entityTypeId) {
        String[] parts = entityTypeId.method_12832().replace('/', '_').split("_");
        StringBuilder out = new StringBuilder();
        for (String part : parts) {
            if (part == null || part.isEmpty()) continue;
            if (!out.isEmpty()) {
                out.append(' ');
            }
            out.append(Character.toUpperCase(part.charAt(0)));
            if (part.length() <= 1) continue;
            out.append(part.substring(1));
        }
        return out.isEmpty() ? entityTypeId.toString() : out.toString();
    }
}

