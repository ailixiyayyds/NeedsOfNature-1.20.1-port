/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Hand
 *  net.minecraft.util.ActionResult
 *  net.minecraft.entity.EntityType
 *  net.minecraft.entity.mob.MobEntity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.item.Item
 *  net.minecraft.item.Item$Settings
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemConvertible
 *  net.minecraft.world.World
 *  net.minecraft.particle.ParticleEffect
 *  net.minecraft.particle.ParticleTypes
 *  net.minecraft.nbt.NbtCompound
 *  net.minecraft.text.Text
 *  net.minecraft.util.Identifier
 *  net.minecraft.server.world.ServerWorld
 *  net.minecraft.sound.SoundEvents
 *  net.minecraft.sound.SoundCategory
 *  net.minecraft.entity.mob.Angerable
 *  net.minecraft.registry.Registries
 *  net.minecraft.component.type.NbtComponent
 *  net.minecraft.component.ComponentType
 *  net.minecraft.component.DataComponentTypes
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid.item;

import com.nonid.EnergyHolder;
import com.nonid.NeedsOfNature;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemConvertible;
import net.minecraft.world.World;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundCategory;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.registry.Registries;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import org.jetbrains.annotations.Nullable;

public class EnergyStabilizerItem
extends Item {
    private static final String STABILIZER_ENTITY_DATA_KEY = "stabilizer_entity";

    public EnergyStabilizerItem(Item.Settings settings) {
        super(settings);
    }

    public Text getName(ItemStack stack) {
        Identifier target = EnergyStabilizerItem.getTargetEntityTypeId(stack);
        if (target == null) {
            return super.getName(stack);
        }
        Text entityName = Registries.ENTITY_TYPE.getOptionalValue(target).map(EntityType::getName).orElseGet(() -> Text.literal((String)target.toString()));
        return Text.translatable((String)"item.needsofnature.entity_energy_stabilizer", (Object[])new Object[]{entityName});
    }

    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        World class_19372;
        MobEntity mob;
        if (!(entity instanceof MobEntity) || !((mob = (MobEntity)entity) instanceof EnergyHolder)) {
            return ActionResult.PASS;
        }
        Identifier target = EnergyStabilizerItem.getTargetEntityTypeId(stack);
        if (target == null) {
            return ActionResult.FAIL;
        }
        Identifier mobType = Registries.ENTITY_TYPE.getId((Object)mob.getType());
        if (!target.equals((Object)mobType)) {
            return ActionResult.FAIL;
        }
        if (mob.getCommandTags().contains("non.energy_stabilized")) {
            return ActionResult.FAIL;
        }
        if (user.getEntityWorld().isClient()) {
            return ActionResult.SUCCESS;
        }
        EnergyHolder holder = (EnergyHolder)mob;
        mob.addCommandTag("non.energy_stabilized");
        holder.setEnergy(Math.min(holder.getEnergy(), 199));
        mob.setTarget(null);
        if (mob instanceof Angerable) {
            Angerable angerable = (Angerable)mob;
            angerable.stopAnger();
        }
        if ((class_19372 = user.getEntityWorld()) instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld)class_19372;
            EnergyStabilizerItem.playFeedback(serverWorld, mob);
        }
        if (!user.isInCreativeMode()) {
            stack.decrement(1);
        }
        return ActionResult.SUCCESS;
    }

    public static ItemStack createStackForEntity(@Nullable Identifier entityTypeId) {
        ItemStack stack = new ItemStack((ItemConvertible)NeedsOfNature.ENERGY_STABILIZER);
        if (entityTypeId != null) {
            EnergyStabilizerItem.setTargetEntityTypeId(stack, entityTypeId);
        }
        return stack;
    }

    public static void setTargetEntityTypeId(ItemStack stack, @Nullable Identifier entityTypeId) {
        if (stack == null || stack.isEmpty()) {
            return;
        }
        NbtComponent.set((ComponentType)DataComponentTypes.CUSTOM_DATA, (ItemStack)stack, nbt -> {
            String value = entityTypeId == null ? "" : entityTypeId.toString();
            nbt.putString(STABILIZER_ENTITY_DATA_KEY, value);
        });
    }

    @Nullable
    public static Identifier getTargetEntityTypeId(ItemStack stack) {
        if (stack == null || stack.isEmpty() || !stack.isOf(NeedsOfNature.ENERGY_STABILIZER)) {
            return null;
        }
        NbtComponent custom = (NbtComponent)stack.get(DataComponentTypes.CUSTOM_DATA);
        if (custom == null || custom.isEmpty()) {
            return null;
        }
        NbtCompound nbt = custom.copyNbt();
        if (!nbt.contains(STABILIZER_ENTITY_DATA_KEY)) {
            return null;
        }
        String raw = nbt.getString(STABILIZER_ENTITY_DATA_KEY, "");
        if (raw == null || raw.isBlank()) {
            return null;
        }
        return Identifier.tryParse((String)raw);
    }

    private static void playFeedback(ServerWorld world, MobEntity mob) {
        world.spawnParticles((ParticleEffect)ParticleTypes.END_ROD, mob.getX(), mob.getBodyY(0.65), mob.getZ(), 12, 0.4, 0.4, 0.4, 0.02);
        world.playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.BLOCK_BEACON_POWER_SELECT, SoundCategory.NEUTRAL, 0.9f, 1.0f);
    }
}

