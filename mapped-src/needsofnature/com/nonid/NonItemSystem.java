/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.component.type.TooltipDisplayComponent
 *  net.minecraft.util.Hand
 *  net.minecraft.entity.EntityType
 *  net.minecraft.item.ItemGroup$Entries
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.potion.Potion
 *  net.minecraft.component.type.PotionContentsComponent
 *  net.minecraft.item.ItemConvertible
 *  net.minecraft.text.Text
 *  net.minecraft.util.Identifier
 *  net.minecraft.server.network.ServerPlayerEntity
 *  net.minecraft.component.type.FoodComponent
 *  net.minecraft.component.type.FoodComponent$Builder
 *  net.minecraft.registry.entry.RegistryEntry
 *  net.minecraft.registry.Registries
 *  net.minecraft.component.DataComponentTypes
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
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.util.Hand;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemConvertible;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.Registries;
import net.minecraft.component.DataComponentTypes;
import org.jetbrains.annotations.Nullable;

final class NonItemSystem {
    private static final FoodComponent LIQUID_BOTTLE_FOOD = new FoodComponent.Builder().nutrition(1).saturationModifier(0.0f).alwaysEdible().build();
    private static final FoodComponent FERTILE_NECTAR_FOOD = new FoodComponent.Builder().nutrition(7).saturationModifier(0.0f).alwaysEdible().build();
    private static final TooltipDisplayComponent LIQUID_BOTTLE_TOOLTIP = TooltipDisplayComponent.DEFAULT.with(DataComponentTypes.POTION_CONTENTS, true);

    private NonItemSystem() {
    }

    static void giveItemOrDrop(ServerPlayerEntity player, ItemStack stack) {
        if (player == null || stack == null || stack.isEmpty()) {
            return;
        }
        if (!player.getInventory().insertStack(stack)) {
            player.dropItem(stack, false);
        }
    }

    static boolean consumeOneGlassBottle(ServerPlayerEntity player, Hand hand) {
        if (player == null || hand == null) {
            return false;
        }
        ItemStack stack = player.getStackInHand(hand);
        if (stack.isEmpty() || !stack.isOf(Items.GLASS_BOTTLE)) {
            return false;
        }
        stack.decrement(1);
        return true;
    }

    static void refundGlassBottle(ServerPlayerEntity player) {
        NonItemSystem.giveItemOrDrop(player, new ItemStack((ItemConvertible)Items.GLASS_BOTTLE));
    }

    static ItemStack createBottleStackForTank(LiquidHolder holder) {
        Identifier entityTypeId = null;
        if (holder != null && holder.getLiquidComposition() == LiquidHolder.LiquidComposition.ENTITY) {
            entityTypeId = holder.getLiquidEntityTypeId();
        }
        return NonItemSystem.createLiquidBottleStack(entityTypeId);
    }

    static ItemStack createLiquidBottleStack(@Nullable Identifier entityTypeId) {
        int tint = NonLiquidSystem.resolveBottleTintRgb(entityTypeId);
        PotionContentsComponent contents = new PotionContentsComponent(Optional.of(Registries.POTION.getEntry((Object)NonPotions.LIQUID)), Optional.of(tint), List.of(), Optional.empty());
        ItemStack stack = new ItemStack((ItemConvertible)Items.POTION);
        stack.set(DataComponentTypes.POTION_CONTENTS, (Object)contents);
        stack.set(DataComponentTypes.MAX_STACK_SIZE, (Object)16);
        stack.set(DataComponentTypes.FOOD, (Object)LIQUID_BOTTLE_FOOD);
        NonPotions.applyHoneyBottleDrinkSound(stack);
        stack.set(DataComponentTypes.TOOLTIP_DISPLAY, (Object)LIQUID_BOTTLE_TOOLTIP);
        NonPotions.setLiquidBottleEntityTypeId(stack, entityTypeId);
        if (entityTypeId == null) {
            stack.set(DataComponentTypes.CUSTOM_NAME, (Object)Text.translatable((String)"item.needsofnature.mixed_liquid_bottle").copy().styled(style -> style.withItalic(Boolean.valueOf(false))));
        } else {
            stack.set(DataComponentTypes.CUSTOM_NAME, (Object)Text.translatable((String)"item.needsofnature.entity_liquid_bottle", (Object[])new Object[]{NonItemSystem.resolveEntityNameText(entityTypeId)}).copy().styled(style -> style.withItalic(Boolean.valueOf(false))));
        }
        return stack;
    }

    static void addEntityLiquidAndStabilizersToItemGroup(ItemGroup.Entries entries, @Nullable NonConfig config) {
        for (String rawEntityId : NonLiquidSystem.resolveEffectiveLiquidGainMap().keySet()) {
            Identifier entityId = Identifier.tryParse((String)rawEntityId);
            if (entityId == null) continue;
            entries.add(NonItemSystem.createLiquidBottleStack(entityId));
            entries.add(NonItemSystem.createEnergyStabilizerStack(entityId));
        }
    }

    static void addNonPotionVariantsToItemGroup(ItemGroup.Entries entries) {
        ArrayList<RegistryEntry> nonPotions = new ArrayList<RegistryEntry>();
        for (Potion potion : Registries.POTION) {
            Identifier potionId = Registries.POTION.getId((Object)potion);
            if (potionId == null || !"needsofnature".equals(potionId.getNamespace())) continue;
            nonPotions.add(Registries.POTION.getEntry((Object)potion));
        }
        entries.add(NonItemSystem.createLiquidBottleStack(null));
        for (RegistryEntry potionEntry : nonPotions) {
            if (potionEntry.comp_349() == NonPotions.LIQUID) continue;
            entries.add(NonItemSystem.createPotionVariantStack(Items.POTION, (RegistryEntry<Potion>)potionEntry));
        }
        for (RegistryEntry potionEntry : nonPotions) {
            if (potionEntry.comp_349() == NonPotions.LIQUID) continue;
            entries.add(NonItemSystem.createPotionVariantStack(Items.SPLASH_POTION, (RegistryEntry<Potion>)potionEntry));
        }
        for (RegistryEntry potionEntry : nonPotions) {
            if (potionEntry.comp_349() == NonPotions.LIQUID) continue;
            entries.add(NonItemSystem.createPotionVariantStack(Items.LINGERING_POTION, (RegistryEntry<Potion>)potionEntry));
        }
        for (RegistryEntry potionEntry : nonPotions) {
            if (potionEntry.comp_349() == NonPotions.LIQUID) continue;
            entries.add(NonItemSystem.createPotionVariantStack(Items.TIPPED_ARROW, (RegistryEntry<Potion>)potionEntry));
        }
    }

    static ItemStack createPotionVariantStack(Item item, RegistryEntry<Potion> potionEntry) {
        ItemStack stack = new ItemStack((ItemConvertible)item);
        stack.set(DataComponentTypes.POTION_CONTENTS, (Object)new PotionContentsComponent(potionEntry));
        if (item == Items.POTION && potionEntry.comp_349() == NonPotions.FERTILE_NECTAR) {
            stack.set(DataComponentTypes.FOOD, (Object)FERTILE_NECTAR_FOOD);
            stack.set(DataComponentTypes.MAX_STACK_SIZE, (Object)16);
            NonPotions.applyHoneyBottleDrinkSound(stack);
        }
        return stack;
    }

    static boolean isNonPotionVariantStack(ItemStack stack) {
        boolean potionLike;
        if (stack == null || stack.isEmpty()) {
            return false;
        }
        Item item = stack.getItem();
        boolean bl = potionLike = item == Items.POTION || item == Items.SPLASH_POTION || item == Items.LINGERING_POTION || item == Items.TIPPED_ARROW;
        if (!potionLike) {
            return false;
        }
        PotionContentsComponent contents = (PotionContentsComponent)stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, (Object)PotionContentsComponent.DEFAULT);
        Optional potionOpt = contents.comp_2378();
        if (potionOpt.isEmpty()) {
            return false;
        }
        Identifier potionId = Registries.POTION.getId((Object)((Potion)((RegistryEntry)potionOpt.get()).comp_349()));
        return potionId != null && "needsofnature".equals(potionId.getNamespace());
    }

    static ItemStack createEnergyStabilizerStack(@Nullable Identifier entityTypeId) {
        return EnergyStabilizerItem.createStackForEntity(entityTypeId);
    }

    private static Text resolveEntityNameText(Identifier entityTypeId) {
        if (entityTypeId == null) {
            return Text.literal((String)"Unknown");
        }
        return Registries.ENTITY_TYPE.getOptionalValue(entityTypeId).map(EntityType::getName).orElseGet(() -> Text.literal((String)NonItemSystem.formatFallbackEntityName(entityTypeId)));
    }

    private static String formatFallbackEntityName(Identifier entityTypeId) {
        String[] parts = entityTypeId.getPath().replace('/', '_').split("_");
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

