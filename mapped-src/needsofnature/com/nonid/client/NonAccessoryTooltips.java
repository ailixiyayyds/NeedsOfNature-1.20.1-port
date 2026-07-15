/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
 *  net.minecraft.util.Formatting
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.item.ItemStack
 *  net.minecraft.text.Text
 *  net.minecraft.util.Identifier
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.text.MutableText
 *  net.minecraft.client.network.ClientPlayerEntity
 *  net.minecraft.registry.Registries
 */
package com.nonid.client;

import com.nonid.NonAccessoryEffects;
import com.nonid.NonTrinketsIntegration;
import com.nonid.data.NonAccessoryBehavior;
import com.nonid.data.NonAccessoryDefinitions;
import com.nonid.data.NonLiquidRoles;
import java.util.List;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.util.Formatting;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.registry.Registries;

public final class NonAccessoryTooltips {
    private static final Text TANK_SCOPED_ICON = Text.literal((String)"\ud83e\uddea ");

    private NonAccessoryTooltips() {
    }

    public static void register() {
        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> NonAccessoryTooltips.appendTooltip(stack, lines));
    }

    private static void appendTooltip(ItemStack stack, List<Text> lines) {
        if (stack == null || stack.isEmpty()) {
            return;
        }
        Identifier itemId = Registries.ITEM.getId((Object)stack.getItem());
        if (!NonAccessoryDefinitions.hasDefinition(itemId)) {
            return;
        }
        NonAccessoryEffects effects = NonAccessoryDefinitions.getEffects(itemId);
        NonTrinketsIntegration.TankScopeStatus tankScope = NonAccessoryTooltips.resolveTankScopeStatus(stack);
        int effectStartIndex = lines.size();
        boolean warnedInactiveTankSlot = false;
        warnedInactiveTankSlot |= NonAccessoryTooltips.addLiquidDecayTooltip(lines, itemId, effects.liquidDecayMultiplier(), tankScope);
        if (effects.equalizeLiquidDecayContext()) {
            lines.add(NonAccessoryTooltips.tankScopedText((Text)Text.translatable((String)"item.needsofnature.accessory.tooltip.equalize_liquid_decay_context"), itemId, "equalize_liquid_decay_context", true, tankScope));
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
            lines.add(effectStartIndex, (Text)Text.translatable((String)"item.needsofnature.accessory.tooltip.inactive_tank_slot").formatted(Formatting.GRAY));
        }
    }

    private static void addBehaviorTooltips(List<Text> lines, Identifier itemId) {
        NonAccessoryBehavior behavior = NonAccessoryDefinitions.getBehavior(itemId);
        if (!behavior.blocksInjectorTypes().isEmpty()) {
            boolean blocksV = behavior.blocksInjectorTypes().contains((Object)NonLiquidRoles.InjectorRole.V);
            boolean blocksA = behavior.blocksInjectorTypes().contains((Object)NonLiquidRoles.InjectorRole.A);
            if (blocksV && blocksA) {
                lines.add((Text)Text.translatable((String)"item.needsofnature.accessory.tooltip.blocks_injector_av").formatted(Formatting.GREEN));
            } else if (blocksV) {
                lines.add((Text)Text.translatable((String)"item.needsofnature.accessory.tooltip.blocks_injector_v").formatted(Formatting.GREEN));
            } else if (blocksA) {
                lines.add((Text)Text.translatable((String)"item.needsofnature.accessory.tooltip.blocks_injector_a").formatted(Formatting.GREEN));
            }
        }
        if (behavior.occupiesSlots().size() > 1) {
            lines.add((Text)Text.translatable((String)"item.needsofnature.accessory.tooltip.occupies_av").formatted(Formatting.GRAY));
        }
        if (behavior.ignoreInjectorSlotEffectSuppression()) {
            lines.add((Text)Text.translatable((String)"item.needsofnature.accessory.tooltip.ignore_injector_slot_effect_suppression").formatted(Formatting.GRAY));
        }
        if (behavior.blocksPregnancy()) {
            lines.add((Text)Text.translatable((String)"item.needsofnature.accessory.tooltip.blocks_pregnancy").formatted(Formatting.GREEN));
        }
    }

    private static boolean addLiquidDecayTooltip(List<Text> lines, Identifier itemId, double value, NonTrinketsIntegration.TankScopeStatus tankScope) {
        if (NonAccessoryTooltips.isNeutral(value)) {
            return false;
        }
        boolean beneficial = value < 1.0;
        String key = beneficial ? "item.needsofnature.accessory.tooltip.liquid_decay_reduced" : "item.needsofnature.accessory.tooltip.liquid_decay_increased";
        lines.add(NonAccessoryTooltips.tankScopedText((Text)Text.translatable((String)key, (Object[])new Object[]{NonAccessoryTooltips.signedPercentDelta(value)}), itemId, "liquid_decay_multiplier", beneficial, tankScope));
        return tankScope == NonTrinketsIntegration.TankScopeStatus.INACTIVE_EQUIPPED;
    }

    private static void addMultiplierTooltip(List<Text> lines, Identifier itemId, String keyPart, String effectKey, double value, boolean higherIsBeneficial) {
        if (NonAccessoryTooltips.isNeutral(value)) {
            return;
        }
        boolean increased = value > 1.0;
        boolean beneficial = increased == higherIsBeneficial;
        String direction = increased ? "increased" : "reduced";
        lines.add(NonAccessoryTooltips.styledText((Text)Text.translatable((String)("item.needsofnature.accessory.tooltip." + keyPart + "." + direction), (Object[])new Object[]{NonAccessoryTooltips.signedPercentDelta(value)}), itemId, effectKey, beneficial));
    }

    private static boolean addMultiplierTooltip(List<Text> lines, Identifier itemId, String keyPart, String effectKey, double value, boolean higherIsBeneficial, NonTrinketsIntegration.TankScopeStatus tankScope) {
        if (NonAccessoryTooltips.isNeutral(value)) {
            return false;
        }
        boolean increased = value > 1.0;
        boolean beneficial = increased == higherIsBeneficial;
        String direction = increased ? "increased" : "reduced";
        lines.add(NonAccessoryTooltips.tankScopedText((Text)Text.translatable((String)("item.needsofnature.accessory.tooltip." + keyPart + "." + direction), (Object[])new Object[]{NonAccessoryTooltips.signedPercentDelta(value)}), itemId, effectKey, beneficial, tankScope));
        return tankScope == NonTrinketsIntegration.TankScopeStatus.INACTIVE_EQUIPPED;
    }

    private static void addAdditiveTooltip(List<Text> lines, Identifier itemId, String keyPart, String effectKey, int value, boolean higherIsBeneficial) {
        if (value == 0) {
            return;
        }
        boolean increased = value > 0;
        boolean beneficial = increased == higherIsBeneficial;
        String direction = increased ? "increased" : "reduced";
        lines.add(NonAccessoryTooltips.styledText((Text)Text.translatable((String)("item.needsofnature.accessory.tooltip." + keyPart + "." + direction), (Object[])new Object[]{NonAccessoryTooltips.signedInt(value)}), itemId, effectKey, beneficial));
    }

    private static boolean addAdditiveTooltip(List<Text> lines, Identifier itemId, String keyPart, String effectKey, int value, boolean higherIsBeneficial, NonTrinketsIntegration.TankScopeStatus tankScope) {
        if (value == 0) {
            return false;
        }
        boolean increased = value > 0;
        boolean beneficial = increased == higherIsBeneficial;
        String direction = increased ? "increased" : "reduced";
        lines.add(NonAccessoryTooltips.tankScopedText((Text)Text.translatable((String)("item.needsofnature.accessory.tooltip." + keyPart + "." + direction), (Object[])new Object[]{NonAccessoryTooltips.signedInt(value)}), itemId, effectKey, beneficial, tankScope));
        return tankScope == NonTrinketsIntegration.TankScopeStatus.INACTIVE_EQUIPPED;
    }

    private static Text tankScopedText(Text body, Identifier itemId, String effectKey, boolean beneficial, NonTrinketsIntegration.TankScopeStatus tankScope) {
        MutableText text = Text.empty().append(TANK_SCOPED_ICON).append(body);
        if (tankScope == NonTrinketsIntegration.TankScopeStatus.INACTIVE_EQUIPPED) {
            return text.formatted(Formatting.GRAY);
        }
        return NonAccessoryTooltips.applyColor(text, NonAccessoryDefinitions.getTooltipColor(itemId, effectKey), beneficial);
    }

    private static Text styledText(Text body, Identifier itemId, String effectKey, boolean beneficial) {
        return NonAccessoryTooltips.applyColor(body.copy(), NonAccessoryDefinitions.getTooltipColor(itemId, effectKey), beneficial);
    }

    private static Text applyColor(MutableText text, NonAccessoryDefinitions.TooltipColor color, boolean beneficial) {
        if (color == null) {
            return text.formatted(beneficial ? Formatting.GREEN : Formatting.RED);
        }
        if (color.formatting() != null) {
            return text.formatted(color.formatting());
        }
        if (color.rgb() != null) {
            return text.setStyle(text.getStyle().withColor(color.rgb().intValue()));
        }
        return text.formatted(beneficial ? Formatting.GREEN : Formatting.RED);
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

    private static NonTrinketsIntegration.TankScopeStatus resolveTankScopeStatus(ItemStack stack) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client == null ? null : client.player;
        return NonTrinketsIntegration.resolveTankScopeStatus((LivingEntity)player, stack);
    }
}

