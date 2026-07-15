/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
 *  net.minecraft.class_124
 *  net.minecraft.class_1309
 *  net.minecraft.class_1799
 *  net.minecraft.class_2561
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_5250
 *  net.minecraft.class_746
 *  net.minecraft.class_7923
 */
package com.nonid.client;

import com.nonid.NonAccessoryEffects;
import com.nonid.NonTrinketsIntegration;
import com.nonid.data.NonAccessoryBehavior;
import com.nonid.data.NonAccessoryDefinitions;
import com.nonid.data.NonLiquidRoles;
import java.util.List;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.class_124;
import net.minecraft.class_1309;
import net.minecraft.class_1799;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_5250;
import net.minecraft.class_746;
import net.minecraft.class_7923;

public final class NonAccessoryTooltips {
    private static final class_2561 TANK_SCOPED_ICON = class_2561.method_43470((String)"\ud83e\uddea ");

    private NonAccessoryTooltips() {
    }

    public static void register() {
        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> NonAccessoryTooltips.appendTooltip(stack, lines));
    }

    private static void appendTooltip(class_1799 stack, List<class_2561> lines) {
        if (stack == null || stack.method_7960()) {
            return;
        }
        class_2960 itemId = class_7923.field_41178.method_10221((Object)stack.method_7909());
        if (!NonAccessoryDefinitions.hasDefinition(itemId)) {
            return;
        }
        NonAccessoryEffects effects = NonAccessoryDefinitions.getEffects(itemId);
        NonTrinketsIntegration.TankScopeStatus tankScope = NonAccessoryTooltips.resolveTankScopeStatus(stack);
        int effectStartIndex = lines.size();
        boolean warnedInactiveTankSlot = false;
        warnedInactiveTankSlot |= NonAccessoryTooltips.addLiquidDecayTooltip(lines, itemId, effects.liquidDecayMultiplier(), tankScope);
        if (effects.equalizeLiquidDecayContext()) {
            lines.add(NonAccessoryTooltips.tankScopedText((class_2561)class_2561.method_43471((String)"item.needsofnature.accessory.tooltip.equalize_liquid_decay_context"), itemId, "equalize_liquid_decay_context", true, tankScope));
            warnedInactiveTankSlot |= tankScope == NonTrinketsIntegration.TankScopeStatus.INACTIVE_EQUIPPED;
        }
        NonAccessoryTooltips.addMultiplierTooltip(lines, itemId, "player_energy_gain", "player_energy_gain_multiplier", effects.playerEnergyGainMultiplier(), false);
        warnedInactiveTankSlot |= NonAccessoryTooltips.addAdditiveTooltip(lines, itemId, "liquid_capacity", "liquid_capacity_add", effects.liquidCapacityAdd(), true, tankScope);
        warnedInactiveTankSlot |= NonAccessoryTooltips.addMultiplierTooltip(lines, itemId, "liquid_gain", "liquid_gain_multiplier", effects.liquidGainMultiplier(), true, tankScope);
        warnedInactiveTankSlot |= NonAccessoryTooltips.addMultiplierTooltip(lines, itemId, "filled_effect", "filled_effect_multiplier", effects.filledEffectMultiplier(), false, tankScope);
        warnedInactiveTankSlot |= NonAccessoryTooltips.addMultiplierTooltip(lines, itemId, "pregnancy_chance", "pregnancy_chance_multiplier", effects.pregnancyChanceMultiplier(), false, tankScope);
        warnedInactiveTankSlot |= NonAccessoryTooltips.addMultiplierTooltip(lines, itemId, "pregnancy_duration", "pregnancy_duration_multiplier", effects.pregnancyDurationMultiplier(), false, tankScope);
        warnedInactiveTankSlot |= NonAccessoryTooltips.addMultiplierTooltip(lines, itemId, "mess_gain", "mess_gain_multiplier", effects.messGainMultiplier(), false, tankScope);
        NonAccessoryTooltips.addMultiplierTooltip(lines, itemId, "destroyed_skin_damage", "destroyed_skin_damage_multiplier", effects.destroyedSkinDamageMultiplier(), false);
        NonAccessoryTooltips.addAdditiveTooltip(lines, itemId, "attack_escape_hits", "attack_escape_hits_add", effects.attackEscapeHitsAdd(), false);
        NonAccessoryTooltips.addMultiplierTooltip(lines, itemId, "attack_escape_damage", "attack_escape_damage_multiplier", effects.attackEscapeDamageMultiplier(), false);
        NonAccessoryTooltips.addMultiplierTooltip(lines, itemId, "player_energy_aura", "player_energy_aura_multiplier", effects.playerEnergyAuraMultiplier(), false);
        NonAccessoryTooltips.addMultiplierTooltip(lines, itemId, "near_animation_mob_energy", "near_animation_mob_energy_multiplier", effects.nearAnimationMobEnergyMultiplier(), false);
        NonAccessoryTooltips.addBehaviorTooltips(lines, itemId);
        if (warnedInactiveTankSlot) {
            lines.add(effectStartIndex, (class_2561)class_2561.method_43471((String)"item.needsofnature.accessory.tooltip.inactive_tank_slot").method_27692(class_124.field_1080));
        }
    }

    private static void addBehaviorTooltips(List<class_2561> lines, class_2960 itemId) {
        NonAccessoryBehavior behavior = NonAccessoryDefinitions.getBehavior(itemId);
        if (!behavior.blocksInjectorTypes().isEmpty()) {
            boolean blocksV = behavior.blocksInjectorTypes().contains((Object)NonLiquidRoles.InjectorRole.V);
            boolean blocksA = behavior.blocksInjectorTypes().contains((Object)NonLiquidRoles.InjectorRole.A);
            if (blocksV && blocksA) {
                lines.add((class_2561)class_2561.method_43471((String)"item.needsofnature.accessory.tooltip.blocks_injector_av").method_27692(class_124.field_1060));
            } else if (blocksV) {
                lines.add((class_2561)class_2561.method_43471((String)"item.needsofnature.accessory.tooltip.blocks_injector_v").method_27692(class_124.field_1060));
            } else if (blocksA) {
                lines.add((class_2561)class_2561.method_43471((String)"item.needsofnature.accessory.tooltip.blocks_injector_a").method_27692(class_124.field_1060));
            }
        }
        if (behavior.occupiesSlots().size() > 1) {
            lines.add((class_2561)class_2561.method_43471((String)"item.needsofnature.accessory.tooltip.occupies_av").method_27692(class_124.field_1080));
        }
        if (behavior.ignoreInjectorSlotEffectSuppression()) {
            lines.add((class_2561)class_2561.method_43471((String)"item.needsofnature.accessory.tooltip.ignore_injector_slot_effect_suppression").method_27692(class_124.field_1080));
        }
        if (behavior.blocksPregnancy()) {
            lines.add((class_2561)class_2561.method_43471((String)"item.needsofnature.accessory.tooltip.blocks_pregnancy").method_27692(class_124.field_1060));
        }
    }

    private static boolean addLiquidDecayTooltip(List<class_2561> lines, class_2960 itemId, double value, NonTrinketsIntegration.TankScopeStatus tankScope) {
        if (NonAccessoryTooltips.isNeutral(value)) {
            return false;
        }
        boolean beneficial = value < 1.0;
        String key = beneficial ? "item.needsofnature.accessory.tooltip.liquid_decay_reduced" : "item.needsofnature.accessory.tooltip.liquid_decay_increased";
        lines.add(NonAccessoryTooltips.tankScopedText((class_2561)class_2561.method_43469((String)key, (Object[])new Object[]{NonAccessoryTooltips.signedPercentDelta(value)}), itemId, "liquid_decay_multiplier", beneficial, tankScope));
        return tankScope == NonTrinketsIntegration.TankScopeStatus.INACTIVE_EQUIPPED;
    }

    private static void addMultiplierTooltip(List<class_2561> lines, class_2960 itemId, String keyPart, String effectKey, double value, boolean higherIsBeneficial) {
        if (NonAccessoryTooltips.isNeutral(value)) {
            return;
        }
        boolean increased = value > 1.0;
        boolean beneficial = increased == higherIsBeneficial;
        String direction = increased ? "increased" : "reduced";
        lines.add(NonAccessoryTooltips.styledText((class_2561)class_2561.method_43469((String)("item.needsofnature.accessory.tooltip." + keyPart + "." + direction), (Object[])new Object[]{NonAccessoryTooltips.signedPercentDelta(value)}), itemId, effectKey, beneficial));
    }

    private static boolean addMultiplierTooltip(List<class_2561> lines, class_2960 itemId, String keyPart, String effectKey, double value, boolean higherIsBeneficial, NonTrinketsIntegration.TankScopeStatus tankScope) {
        if (NonAccessoryTooltips.isNeutral(value)) {
            return false;
        }
        boolean increased = value > 1.0;
        boolean beneficial = increased == higherIsBeneficial;
        String direction = increased ? "increased" : "reduced";
        lines.add(NonAccessoryTooltips.tankScopedText((class_2561)class_2561.method_43469((String)("item.needsofnature.accessory.tooltip." + keyPart + "." + direction), (Object[])new Object[]{NonAccessoryTooltips.signedPercentDelta(value)}), itemId, effectKey, beneficial, tankScope));
        return tankScope == NonTrinketsIntegration.TankScopeStatus.INACTIVE_EQUIPPED;
    }

    private static void addAdditiveTooltip(List<class_2561> lines, class_2960 itemId, String keyPart, String effectKey, int value, boolean higherIsBeneficial) {
        if (value == 0) {
            return;
        }
        boolean increased = value > 0;
        boolean beneficial = increased == higherIsBeneficial;
        String direction = increased ? "increased" : "reduced";
        lines.add(NonAccessoryTooltips.styledText((class_2561)class_2561.method_43469((String)("item.needsofnature.accessory.tooltip." + keyPart + "." + direction), (Object[])new Object[]{NonAccessoryTooltips.signedInt(value)}), itemId, effectKey, beneficial));
    }

    private static boolean addAdditiveTooltip(List<class_2561> lines, class_2960 itemId, String keyPart, String effectKey, int value, boolean higherIsBeneficial, NonTrinketsIntegration.TankScopeStatus tankScope) {
        if (value == 0) {
            return false;
        }
        boolean increased = value > 0;
        boolean beneficial = increased == higherIsBeneficial;
        String direction = increased ? "increased" : "reduced";
        lines.add(NonAccessoryTooltips.tankScopedText((class_2561)class_2561.method_43469((String)("item.needsofnature.accessory.tooltip." + keyPart + "." + direction), (Object[])new Object[]{NonAccessoryTooltips.signedInt(value)}), itemId, effectKey, beneficial, tankScope));
        return tankScope == NonTrinketsIntegration.TankScopeStatus.INACTIVE_EQUIPPED;
    }

    private static class_2561 tankScopedText(class_2561 body, class_2960 itemId, String effectKey, boolean beneficial, NonTrinketsIntegration.TankScopeStatus tankScope) {
        class_5250 text = class_2561.method_43473().method_10852(TANK_SCOPED_ICON).method_10852(body);
        if (tankScope == NonTrinketsIntegration.TankScopeStatus.INACTIVE_EQUIPPED) {
            return text.method_27692(class_124.field_1080);
        }
        return NonAccessoryTooltips.applyColor(text, NonAccessoryDefinitions.getTooltipColor(itemId, effectKey), beneficial);
    }

    private static class_2561 styledText(class_2561 body, class_2960 itemId, String effectKey, boolean beneficial) {
        return NonAccessoryTooltips.applyColor(body.method_27661(), NonAccessoryDefinitions.getTooltipColor(itemId, effectKey), beneficial);
    }

    private static class_2561 applyColor(class_5250 text, NonAccessoryDefinitions.TooltipColor color, boolean beneficial) {
        if (color == null) {
            return text.method_27692(beneficial ? class_124.field_1060 : class_124.field_1061);
        }
        if (color.formatting() != null) {
            return text.method_27692(color.formatting());
        }
        if (color.rgb() != null) {
            return text.method_10862(text.method_10866().method_36139(color.rgb().intValue()));
        }
        return text.method_27692(beneficial ? class_124.field_1060 : class_124.field_1061);
    }

    private static boolean isNeutral(double value) {
        return Math.abs(value - 1.0) < 1.0E-6;
    }

    private static String signedPercentDelta(double value) {
        int percent = (int)Math.round(Math.abs(value - 1.0) * 100.0);
        String sign = value >= 1.0 ? "+" : "-";
        return sign + percent + "%";
    }

    private static String signedInt(int value) {
        return (value > 0 ? "+" : "") + value;
    }

    private static NonTrinketsIntegration.TankScopeStatus resolveTankScopeStatus(class_1799 stack) {
        class_310 client = class_310.method_1551();
        class_746 player = client == null ? null : client.field_1724;
        return NonTrinketsIntegration.resolveTankScopeStatus((class_1309)player, stack);
    }
}

