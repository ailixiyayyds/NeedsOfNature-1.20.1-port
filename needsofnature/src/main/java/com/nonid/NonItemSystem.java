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
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.Hand;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.item.ItemConvertible;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.Nullable;

final class NonItemSystem {
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
        ItemStack stack = new ItemStack((ItemConvertible)Items.POTION);
        PotionUtil.setPotion(stack, NonPotions.LIQUID);
        stack.getOrCreateNbt().putInt("CustomPotionColor", tint);
        NonPotions.setLiquidBottleEntityTypeId(stack, entityTypeId);
        if (entityTypeId == null) {
            stack.setCustomName(Text.translatable((String)"item.needsofnature.mixed_liquid_bottle").copy().styled(style -> style.withItalic(Boolean.valueOf(false))));
        } else {
            stack.setCustomName(Text.translatable((String)"item.needsofnature.entity_liquid_bottle", (Object[])new Object[]{NonItemSystem.resolveEntityNameText(entityTypeId)}).copy().styled(style -> style.withItalic(Boolean.valueOf(false))));
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
        ArrayList<Potion> nonPotions = new ArrayList<Potion>();
        for (Potion potion : Registries.POTION) {
            Identifier potionId = Registries.POTION.getId(potion);
            if (potionId == null || !"needsofnature".equals(potionId.getNamespace())) continue;
            nonPotions.add(potion);
        }
        entries.add(NonItemSystem.createLiquidBottleStack(null));
        for (Potion potion : nonPotions) {
            if (potion == NonPotions.LIQUID) continue;
            entries.add(NonItemSystem.createPotionVariantStack(Items.POTION, potion));
        }
        for (Potion potion : nonPotions) {
            if (potion == NonPotions.LIQUID) continue;
            entries.add(NonItemSystem.createPotionVariantStack(Items.SPLASH_POTION, potion));
        }
        for (Potion potion : nonPotions) {
            if (potion == NonPotions.LIQUID) continue;
            entries.add(NonItemSystem.createPotionVariantStack(Items.LINGERING_POTION, potion));
        }
        for (Potion potion : nonPotions) {
            if (potion == NonPotions.LIQUID) continue;
            entries.add(NonItemSystem.createPotionVariantStack(Items.TIPPED_ARROW, potion));
        }
    }

    static ItemStack createPotionVariantStack(Item item, Potion potion) {
        ItemStack stack = new ItemStack((ItemConvertible)item);
        return PotionUtil.setPotion(stack, potion);
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
        Identifier potionId = Registries.POTION.getId(PotionUtil.getPotion(stack));
        return potionId != null && "needsofnature".equals(potionId.getNamespace());
    }

    static ItemStack createEnergyStabilizerStack(@Nullable Identifier entityTypeId) {
        return EnergyStabilizerItem.createStackForEntity(entityTypeId);
    }

    private static Text resolveEntityNameText(Identifier entityTypeId) {
        if (entityTypeId == null) {
            return Text.literal((String)"Unknown");
        }
        EntityType<?> entityType = Registries.ENTITY_TYPE.get(entityTypeId);
        return entityType == null ? Text.literal(NonItemSystem.formatFallbackEntityName(entityTypeId)) : entityType.getName();
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

