/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.afwid.api.AfwAnimationApi
 *  com.afwid.api.AfwAnimationApi$AnimationCandidateFilter
 *  com.afwid.api.AfwAnimationApi$MatchOptions
 *  com.afwid.api.AfwAnimationApi$MatchedAnimation
 *  com.afwid.api.AfwAnimationApi$StartOptions
 *  com.afwid.api.AfwAnimationEvents
 *  com.afwid.api.AfwDamageBehavior
 *  com.afwid.data.AfwAnimationDefinitions
 *  com.afwid.data.AfwAnimationDefinitions$Definition
 *  com.afwid.data.AfwAnimationDefinitions$MatchResult
 *  com.afwid.data.AfwAnimationDefinitions$WaterRequirement
 *  com.afwid.network.AnimationStageInfo
 *  com.afwid.server.AfwServerAnimationController
 *  com.afwid.util.AfwStageTimeWarp
 *  com.google.gson.Gson
 *  net.fabricmc.api.ModInitializer
 *  net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
 *  net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
 *  net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents
 *  net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
 *  net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
 *  net.fabricmc.fabric.api.event.player.UseBlockCallback
 *  net.fabricmc.fabric.api.event.player.UseEntityCallback
 *  net.fabricmc.fabric.api.event.player.UseItemCallback
 *  net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
 *  net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
 *  net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
 *  net.fabricmc.fabric.api.networking.v1.PlayerLookup
 *  net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
 *  net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
 *  net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry
 *  net.minecraft.entity.LazyEntityReference
 *  net.minecraft.util.Formatting
 *  net.minecraft.util.Hand
 *  net.minecraft.util.ActionResult
 *  net.minecraft.entity.effect.StatusEffectInstance
 *  net.minecraft.entity.passive.PassiveEntity
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityType
 *  net.minecraft.entity.EntityType$Builder
 *  net.minecraft.entity.ExperienceOrbEntity
 *  net.minecraft.entity.mob.MobEntity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.SpawnGroup
 *  net.minecraft.entity.passive.TameableEntity
 *  net.minecraft.entity.passive.AnimalEntity
 *  net.minecraft.entity.passive.MooshroomEntity$Variant
 *  net.minecraft.entity.passive.ParrotEntity$Variant
 *  net.minecraft.entity.passive.SalmonEntity$Variant
 *  net.minecraft.entity.passive.RabbitEntity$Variant
 *  net.minecraft.entity.passive.AbstractHorseEntity
 *  net.minecraft.entity.passive.HorseEntity
 *  net.minecraft.entity.passive.LlamaEntity$Variant
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.item.ItemGroup
 *  net.minecraft.item.ItemGroup$Entries
 *  net.minecraft.util.DyeColor
 *  net.minecraft.item.Item
 *  net.minecraft.item.Item$Settings
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.item.ItemConvertible
 *  net.minecraft.world.World
 *  net.minecraft.block.Blocks
 *  net.minecraft.registry.Registry
 *  net.minecraft.util.math.Box
 *  net.minecraft.particle.ParticleEffect
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.text.Text
 *  net.minecraft.util.Identifier
 *  net.minecraft.server.world.ServerWorld
 *  net.minecraft.server.network.ServerPlayerEntity
 *  net.minecraft.sound.SoundEvents
 *  net.minecraft.sound.SoundCategory
 *  net.minecraft.registry.tag.FluidTags
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.entity.SpawnReason
 *  net.minecraft.entity.passive.FoxEntity$Variant
 *  net.minecraft.entity.attribute.DefaultAttributeContainer$Builder
 *  net.minecraft.entity.passive.HorseMarking
 *  net.minecraft.entity.passive.HorseColor
 *  net.minecraft.registry.RegistryKey
 *  net.minecraft.entity.mob.Angerable
 *  net.minecraft.world.ServerWorldAccess
 *  net.minecraft.entity.passive.AxolotlEntity$Variant
 *  net.minecraft.util.math.random.Random
 *  net.minecraft.registry.entry.RegistryEntry
 *  net.minecraft.registry.RegistryWrapper$WrapperLookup
 *  net.minecraft.registry.Registries
 *  net.minecraft.registry.RegistryKeys
 *  net.minecraft.network.packet.CustomPayload
 *  net.minecraft.component.ComponentType
 *  net.minecraft.component.DataComponentTypes
 *  net.minecraft.registry.entry.LazyRegistryEntryReference
 *  net.minecraft.server.MinecraftServer
 *  org.jetbrains.annotations.Nullable
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.nonid;

import com.afwid.api.AfwAnimationApi;
import com.afwid.api.AfwAnimationEvents;
import com.afwid.api.AfwDamageBehavior;
import com.afwid.data.AfwAnimationDefinitions;
import com.afwid.network.AnimationStageInfo;
import com.afwid.server.AfwServerAnimationController;
import com.afwid.util.AfwStageTimeWarp;
import com.google.gson.Gson;
import com.nonid.DebugStaffItem;
import com.nonid.DestroyedSkinHolder;
import com.nonid.EnergyHolder;
import com.nonid.LiquidHolder;
import com.nonid.MessHolder;
import com.nonid.NonAccessoryEffects;
import com.nonid.NonAccessoryShedSystem;
import com.nonid.NonAdvancementHooks;
import com.nonid.NonApiInternals;
import com.nonid.NonAttackSystem;
import com.nonid.NonAuraSystem;
import com.nonid.NonCommands;
import com.nonid.NonConfig;
import com.nonid.NonDebugChatCategory;
import com.nonid.NonDestroyedSkinSystem;
import com.nonid.NonGatherCancelReason;
import com.nonid.NonGatherSystem;
import com.nonid.NonGenderSystem;
import com.nonid.NonInjectorMatchPolicy;
import com.nonid.NonItemSystem;
import com.nonid.NonLiquidSystem;
import com.nonid.NonLiquidTankType;
import com.nonid.NonMessSystem;
import com.nonid.NonStats;
import com.nonid.NonTrinketsIntegration;
import com.nonid.PregnancyHolder;
import com.nonid.api.NonEvents;
import com.nonid.client.NonLiquidColors;
import com.nonid.data.NonAccessoryDefinitions;
import com.nonid.data.NonAccessoryItemRegistry;
import com.nonid.data.NonAnimationEligibility;
import com.nonid.data.NonEntityProfiles;
import com.nonid.data.NonEscapableStages;
import com.nonid.data.NonLiquidGainMultipliers;
import com.nonid.data.NonLiquidGainOverrides;
import com.nonid.data.NonLiquidRoles;
import com.nonid.data.NonLoopSecondsOverrides;
import com.nonid.data.NonManualPeakDefinitions;
import com.nonid.data.NonPeakStages;
import com.nonid.data.NonPregnancyCues;
import com.nonid.data.NonReactiveImpactCues;
import com.nonid.effect.NonStatusEffects;
import com.nonid.entity.HorseLiquidCollectorEntity;
import com.nonid.entity.PregnancyEggEntity;
import com.nonid.item.EnergyGainAdjustItem;
import com.nonid.item.EnergyStabilizerItem;
import com.nonid.item.HorseLiquidCollectorItem;
import com.nonid.network.AccessoryShedStateS2CPayload;
import com.nonid.network.AttackStateS2CPayload;
import com.nonid.network.DestroyedSkinMaskSyncS2CPayload;
import com.nonid.network.DestroyedSkinMaskUploadC2SPayload;
import com.nonid.network.DestroyedSkinParticlesS2CPayload;
import com.nonid.network.DestroyedSkinStateS2CPayload;
import com.nonid.network.DestroyedSkinSyncS2CPayload;
import com.nonid.network.DestroyedSkinUploadC2SPayload;
import com.nonid.network.GameplayRuntimeSettingsS2CPayload;
import com.nonid.network.GenderSelectionPromptS2CPayload;
import com.nonid.network.HostConfigSyncData;
import com.nonid.network.HostConfigSyncS2CPayload;
import com.nonid.network.LiquidStateS2CPayload;
import com.nonid.network.ManualDamageDestroyedSkinC2SPayload;
import com.nonid.network.ManualPeakPropOverrideS2CPayload;
import com.nonid.network.MessStateS2CPayload;
import com.nonid.network.PregnancyStateS2CPayload;
import com.nonid.network.RepairDestroyedSkinC2SPayload;
import com.nonid.network.SetPlayerGenderC2SPayload;
import com.nonid.network.SkinRippedInfoRequestS2CPayload;
import com.nonid.network.StageProgressS2CPayload;
import com.nonid.network.StartManualPeakC2SPayload;
import com.nonid.network.StopAttackAnimationC2SPayload;
import com.nonid.network.StopVoluntaryAnimationC2SPayload;
import com.nonid.particle.NonParticles;
import com.nonid.potion.NonBrewingRecipes;
import com.nonid.potion.NonPotions;
import com.nonid.recipe.NonRecipeSerializers;
import com.nonid.sound.NonSoundEvents;
import java.lang.invoke.LambdaMetafactory;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.LazyEntityReference;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.SalmonEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ItemConvertible;
import net.minecraft.world.World;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registry;
import net.minecraft.util.math.Box;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundCategory;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.passive.HorseMarking;
import net.minecraft.entity.passive.HorseColor;
import net.minecraft.registry.RegistryKey;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.util.math.random.Random;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.registry.entry.LazyRegistryEntryReference;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeedsOfNature
implements ModInitializer {
    public static final String MOD_ID = "needsofnature";
    public static final int MAX_ENERGY = 200;
    private static final int PLAYER_ANIMATION_START_ENERGY_GAIN = 20;
    public static final float MIN_ENERGY_GAIN_MULT = 0.4f;
    public static final float MAX_ENERGY_GAIN_MULT = 6.0f;
    public static final String ENERGY_NBT_KEY = "NeedsOfNatureEnergy";
    public static final String ENERGY_GAIN_MULT_NBT_KEY = "NeedsOfNatureEnergyGainMult";
    public static final String ENERGY_GAIN_DRIFT_NBT_KEY = "NeedsOfNatureEnergyGainDrift";
    public static final String GENDER_NBT_KEY = "NeedsOfNatureGender";
    public static final String LIQUID_NBT_KEY = "NeedsOfNatureLiquid";
    public static final String LIQUID_TYPE_NBT_KEY = "NeedsOfNatureLiquidType";
    public static final String LIQUID_TYPE_MIXED = "mixed";
    public static final String LIQUID_COLOR_MIXED_KEY = "needsofnature:mixed";
    public static final String PREGNANCY_TYPE_NBT_KEY = "NeedsOfNaturePregnancyType";
    public static final String PREGNANCY_END_TICK_NBT_KEY = "NeedsOfNaturePregnancyEndTick";
    public static final String PREGNANCY_PENDING_NBT_KEY = "NeedsOfNaturePregnancyPending";
    public static final String PREGNANCY_VARIANT_NBT_KEY = "NeedsOfNaturePregnancyVariantData";
    public static final String PREGNANCY_OFFSPRING_COUNT_NBT_KEY = "NeedsOfNaturePregnancyOffspringCount";
    public static final String DESTROYED_SKIN_STAGE_NBT_KEY = "NeedsOfNatureDestroyedSkinStage";
    public static final String DESTROYED_SKIN_DAMAGE_NBT_KEY = "NeedsOfNatureDestroyedSkinDamage";
    public static final String MESS_V_NBT_KEY = "NeedsOfNatureMessV";
    public static final String MESS_A_NBT_KEY = "NeedsOfNatureMessA";
    public static final String MESS_M_NBT_KEY = "NeedsOfNatureMessM";
    public static final float FILLED_STAGE_ONE_THRESHOLD = 0.3f;
    public static final float FILLED_STAGE_TWO_THRESHOLD = 0.5f;
    public static final float FILLED_STAGE_THREE_THRESHOLD = 0.8f;
    public static final float FILLED_STAGE_ONE_SPEED_MULT = 0.8f;
    public static final float FILLED_STAGE_TWO_SPEED_MULT = 0.7f;
    public static final float FILLED_STAGE_THREE_SPEED_MULT = 0.6f;
    public static final float FILLED_STAGE_ONE_JUMP_MULT = 0.9f;
    public static final float FILLED_STAGE_TWO_JUMP_MULT = 0.75f;
    public static final float FILLED_STAGE_THREE_JUMP_MULT = 0.6f;
    public static final float PREGNANCY_MIN_MOVEMENT_MULT = 0.5f;
    public static final float PREGNANCY_SLOWDOWN_CURVE_POWER = 5.0f;
    public static final Logger LOGGER = LoggerFactory.getLogger((String)"needsofnature");
    private static final int MAX_PENDING_SETUP_CHAT_MESSAGES = 100;
    private static final List<PendingSetupChatMessage> PENDING_SETUP_CHAT = new ArrayList<PendingSetupChatMessage>();
    private static final Gson HOST_CONFIG_SYNC_GSON = new Gson();
    public static final RegistryKey<Item> DEBUG_STAFF_KEY = RegistryKey.of((RegistryKey)Registries.ITEM.getKey(), (Identifier)NeedsOfNature.id("debug_staff"));
    public static final Item DEBUG_STAFF = NeedsOfNature.registerItem(DEBUG_STAFF_KEY, new DebugStaffItem(new Item.Settings().registryKey(DEBUG_STAFF_KEY).maxCount(1)));
    public static final RegistryKey<Item> FLOWER_MIX_KEY = RegistryKey.of((RegistryKey)Registries.ITEM.getKey(), (Identifier)NeedsOfNature.id("flower_mix"));
    public static final Item FLOWER_MIX = NeedsOfNature.registerItem(FLOWER_MIX_KEY, new Item(new Item.Settings().registryKey(FLOWER_MIX_KEY)));
    public static final RegistryKey<Item> ENERGY_AUGMENTER_KEY = RegistryKey.of((RegistryKey)Registries.ITEM.getKey(), (Identifier)NeedsOfNature.id("energy_augmenter"));
    public static final Item ENERGY_AUGMENTER = NeedsOfNature.registerItem(ENERGY_AUGMENTER_KEY, new EnergyGainAdjustItem(new Item.Settings().registryKey(ENERGY_AUGMENTER_KEY), 0.2f));
    public static final RegistryKey<Item> ENERGY_DIMINISHER_KEY = RegistryKey.of((RegistryKey)Registries.ITEM.getKey(), (Identifier)NeedsOfNature.id("energy_diminisher"));
    public static final Item ENERGY_DIMINISHER = NeedsOfNature.registerItem(ENERGY_DIMINISHER_KEY, new EnergyGainAdjustItem(new Item.Settings().registryKey(ENERGY_DIMINISHER_KEY), -0.2f));
    public static final RegistryKey<Item> ENERGY_STABILIZER_KEY = RegistryKey.of((RegistryKey)Registries.ITEM.getKey(), (Identifier)NeedsOfNature.id("energy_stabilizer"));
    public static final Item ENERGY_STABILIZER = NeedsOfNature.registerItem(ENERGY_STABILIZER_KEY, new EnergyStabilizerItem(new Item.Settings().registryKey(ENERGY_STABILIZER_KEY)));
    public static final List<Item> DATA_DRIVEN_ACCESSORIES = NeedsOfNature.registerDataDrivenAccessories();
    public static final RegistryKey<EntityType<?>> HORSE_LIQUID_COLLECTOR_ENTITY_TYPE_KEY = RegistryKey.of((RegistryKey)Registries.ENTITY_TYPE.getKey(), (Identifier)NeedsOfNature.id("horse_liquid_collector"));
    public static final EntityType<HorseLiquidCollectorEntity> HORSE_LIQUID_COLLECTOR_ENTITY_TYPE = (EntityType)Registry.register((Registry)Registries.ENTITY_TYPE, (Identifier)NeedsOfNature.id("horse_liquid_collector"), (Object)EntityType.Builder.create(HorseLiquidCollectorEntity::new, (SpawnGroup)SpawnGroup.MISC).dimensions(0.8f, 1.2f).build(HORSE_LIQUID_COLLECTOR_ENTITY_TYPE_KEY));
    public static final RegistryKey<EntityType<?>> PREGNANCY_EGG_ENTITY_TYPE_KEY = RegistryKey.of((RegistryKey)Registries.ENTITY_TYPE.getKey(), (Identifier)NeedsOfNature.id("pregnancy_egg"));
    public static final EntityType<PregnancyEggEntity> PREGNANCY_EGG_ENTITY_TYPE = (EntityType)Registry.register((Registry)Registries.ENTITY_TYPE, (Identifier)NeedsOfNature.id("pregnancy_egg"), (Object)EntityType.Builder.create(PregnancyEggEntity::new, (SpawnGroup)SpawnGroup.MISC).dimensions(0.3f, 0.3f).build(PREGNANCY_EGG_ENTITY_TYPE_KEY));
    public static final RegistryKey<Item> HORSE_LIQUID_COLLECTOR_KEY = RegistryKey.of((RegistryKey)Registries.ITEM.getKey(), (Identifier)NeedsOfNature.id("horse_liquid_collector"));
    public static final Item HORSE_LIQUID_COLLECTOR = NeedsOfNature.registerItem(HORSE_LIQUID_COLLECTOR_KEY, new HorseLiquidCollectorItem(new Item.Settings().registryKey(HORSE_LIQUID_COLLECTOR_KEY).maxCount(1)));
    private static final Map<UUID, PlayerActiveInstance> ACTIVE_PLAYER_INSTANCES = new ConcurrentHashMap<UUID, PlayerActiveInstance>();
    private static final Map<RegistryKey<World>, ScanBudget> SCAN_BUDGETS = new ConcurrentHashMap<RegistryKey<World>, ScanBudget>();
    private static final Map<UUID, LoopAdvance> LOOP_ADVANCE = new ConcurrentHashMap<UUID, LoopAdvance>();
    private static final Map<UUID, PostDeathStateSync> PENDING_POST_DEATH_STATE_SYNCS = new ConcurrentHashMap<UUID, PostDeathStateSync>();
    private static final ItemGroup ITEM_GROUP = (ItemGroup)Registry.register((Registry)Registries.ITEM_GROUP, (Identifier)NeedsOfNature.id("main"), (Object)FabricItemGroup.builder().displayName((Text)Text.translatable((String)"itemGroup.needsofnature.main")).icon(() -> new ItemStack((ItemConvertible)HORSE_LIQUID_COLLECTOR)).entries((context, entries) -> {
        entries.add((ItemConvertible)FLOWER_MIX);
        entries.add((ItemConvertible)ENERGY_AUGMENTER);
        entries.add((ItemConvertible)ENERGY_DIMINISHER);
        DATA_DRIVEN_ACCESSORIES.forEach(arg_0 -> ((ItemGroup.Entries)entries).add(arg_0));
        entries.add((ItemConvertible)HORSE_LIQUID_COLLECTOR);
        NonItemSystem.addEntityLiquidAndStabilizersToItemGroup(entries, NeedsOfNature.getConfig());
        NonItemSystem.addNonPotionVariantsToItemGroup(entries);
    }).build());
    private static NonConfig CONFIG;
    private static final Map<UUID, PeakState> PEAK_STATES;
    private static final int PEAK_XP_CAP = 20;
    private static final Identifier PLAYER_ENTITY_TYPE_ID;
    private static final int PENDING_PREGNANCY_EFFECT_TICKS = 21;
    private static final int MESS_AURA_MAX_TOTAL = 15;
    private static final double MESS_AURA_MAX_MULTIPLIER = 10.0;
    private static final int GATHER_TIMEOUT_COOLDOWN_TICKS = 1200;
    private static final int GATHER_PAIR_TRACK_TICKS = 6000;
    private static final int BREEDING_PENDING_TIMEOUT_TICKS = 600;
    private static final int REGULAR_PRUNE_INTERVAL_TICKS = 20;
    private static final int FERTILITY_INCREASER_I_MIN_CHANCE = 80;
    private static final int FERTILITY_INCREASER_II_MIN_CHANCE = 95;
    private static final double LIQUID_SNEAK_DECAY_MULTIPLIER = 16.0;
    private static final double LIQUID_WATER_DECAY_MULTIPLIER = 2.0;
    private static final double LIQUID_DECAY_MIN_MULT = 0.1;
    private static final double LIQUID_DECAY_MAX_MULT = 1.5;
    private static final double NEAR_ANIMATION_RADIUS = 20.0;
    private static final Map<UUID, InstanceControl> INSTANCE_CONTROLS;
    private static final Map<UUID, List<UUID>> INSTANCE_ACTORS;
    private static final Map<UUID, AttackJoinCarryover> ATTACK_JOIN_CARRYOVER;
    private static final int ATTACK_JOIN_CARRYOVER_TICKS = 400;
    private static final Map<GatherPairKey, GatherPairState> PENDING_GATHER_PAIRS;
    private static final Map<UUID, NoNGatherActorState> NON_PENDING_GATHER_ACTORS;
    private static final String AFW_ANIMATION_TAG_DEFEAT_PREFIX = "defeat.";
    private static final String AFW_ANIMATION_TAG_DEFEAT_ON_BELLY = "defeat.on_belly";
    private static final String AFW_ANIMATION_TAG_DEFEAT_ON_BACK = "defeat.on_back";
    private static final String AFW_ANIMATION_TAG_DEFEAT_SIDE = "defeat.side";
    private static final List<String> AFW_ANIMATION_TAG_DEFEAT_FALLBACKS;
    private static final String AFW_ANIMATION_TAG_MANUAL_PEAK = "manualPeak";
    private static final String AFW_ANIMATION_TAG_FILL_BOTTLE = "fillBottle";
    private static final String AFW_ANIMATION_TAG_BIRTH = "birth";
    private static final String AFW_ANIMATION_TAG_BIRTH_PREFIX = "birth_";
    private static final int BIRTH_LOOP_STAGE = 2;
    private static final int BIRTH_FINAL_STAGE = 3;
    private static final double BIRTH_LOOP_MAX_SPEED_MULTIPLIER = 2.0;
    private static final double BIRTH_LOOP_SPEED_TAPER_EXPONENT = 2.5;
    private static final Map<String, String> DEFEAT_POSE_TAG_BY_CONTENT_TAG;
    private static final String NON_MODE_META_KEY = "needsofnature.mode";
    private static final String NON_MODE_ATTACK = "attack";
    private static final String NON_MODE_VOLUNTARY = "voluntary";
    private static final String NON_MODE_PREGNANCY = "pregnancy";
    private static final String AFW_JOIN_REPLACE_META_KEY = "afw.join_replace";
    private static final String AFW_JOIN_REPLACE_FROM_META_KEY = "afw.join_replace_from";
    private static final String NON_BREEDING_REQUEST_ID_META_KEY = "needsofnature.breeding_request_id";
    private static final String NON_PREGNANCY_ENTITY_META_KEY = "needsofnature.pregnancy_entity";
    private static final String NON_PREGNANCY_BIRTH_ENTITY_META_KEY = "needsofnature.pregnancy_birth_entity";
    private static final String NON_PREGNANCY_BIRTH_MODE_META_KEY = "needsofnature.pregnancy_birth_mode";
    private static final String NON_PREGNANCY_OFFSPRING_COUNT_META_KEY = "needsofnature.pregnancy_offspring_count";
    private static final String NON_PREGNANCY_VARIANT_META_KEY = "needsofnature.pregnancy_variant";
    static final String NON_PREGNANCY_FRIENDLY_TAG = "non.pregnancy_friendly";
    public static final String NON_ENERGY_STABILIZED_TAG = "non.energy_stabilized";
    private static final Set<UUID> DEFEATED_QUEUED_INSTANCES;
    private static final Map<UUID, Long> LAST_DEFEATED_COOLDOWN_UNTIL_BY_PLAYER;
    private static final Map<UUID, Long> POST_ESCAPE_DAMAGE_INVULNERABLE_UNTIL_BY_PLAYER;
    private static final int ATTACK_ESCAPE_DAMAGE_DELAY_TICKS = 1;
    private static final Map<UUID, PendingAttackEscapeResolution> PENDING_ATTACK_ESCAPE_RESOLUTIONS;
    private static final List<VariantComponentDescriptor> PREGNANCY_VARIANT_COMPONENTS;
    private static final int INFINITE_PEAK_LIQUID_PULSE_ML = 5;
    private static final Map<UUID, PeakLiquidProgressState> PEAK_LIQUID_PROGRESS;
    private static final Map<UUID, FillBottleState> FILL_BOTTLE_STATES;
    private static final Map<BreedingPairKey, PendingBreedingState> BREEDING_PENDING_BY_PAIR;
    private static final Map<String, BreedingPairKey> BREEDING_PENDING_BY_REQUEST;
    private static final Map<UUID, ActiveBreedingState> BREEDING_ACTIVE_BY_INSTANCE;
    private static final Map<BreedingPairKey, UUID> BREEDING_ACTIVE_INSTANCE_BY_PAIR;
    private static final Map<UUID, PregnancyHatchState> PREGNANCY_HATCH_BY_INSTANCE;
    private static final Map<UUID, UUID> PREGNANCY_ACTIVE_INSTANCE_BY_PLAYER;
    private static final Set<UUID> PREGNANCY_QUEUED_PLAYERS;
    private static final Map<UUID, Long> PREGNANCY_START_RETRY_TICKS;
    private static final Map<UUID, PregnancyCueRuntime> PREGNANCY_CUE_RUNTIME_BY_INSTANCE;
    private static final Set<String> REFLECTION_FAILURE_LOGS;
    private static final Map<UUID, ReceiverDripState> RECEIVER_DRIP;
    private static final Map<UUID, PendingMInjectorReward> PENDING_M_INJECTOR_REWARDS;
    private static final int RECEIVER_DRIP_INTERVAL_TICKS = 20;
    private static final int RECEIVER_DRIP_DURATION_TICKS = 200;
    private static final float FILL_BOTTLE_LOOK_DOWN_MIN_PITCH = 80.0f;
    private static final Vec3d RECEIVER_DRIP_OFFSET_DEFAULT;
    private static final Map<Identifier, Vec3d> RECEIVER_DRIP_OFFSETS;

    public static float getPregnancyMovementMultiplier(int remainingTicks) {
        if (remainingTicks == -1) {
            return 1.0f;
        }
        if (remainingTicks <= 22) {
            return 0.5f;
        }
        long configuredTotalTicks = Math.max(1L, (long)NeedsOfNature.getConfig().getPregnancyDurationMinutes() * 1200L);
        double remaining01 = MathHelper.clamp((double)((double)remainingTicks / (double)configuredTotalTicks), (double)0.0, (double)1.0);
        double closeness = 1.0 - remaining01;
        double curve = Math.pow(closeness, 5.0);
        double multiplier = 1.0 - 0.5 * curve;
        return (float)MathHelper.clamp((double)multiplier, (double)0.5, (double)1.0);
    }

    public static void applyAnimationToggleConfig() {
        NonConfig config = CONFIG;
        if (config == null) {
            AfwAnimationDefinitions.setDisabledAnimationFilters(Set.of(), Set.of());
            return;
        }
        LinkedHashSet<Identifier> disabledAnimations = new LinkedHashSet<Identifier>();
        for (String raw : config.getDisabledAnimations()) {
            Identifier id = Identifier.tryParse((String)raw);
            if (id == null) continue;
            disabledAnimations.add(id);
        }
        AfwAnimationDefinitions.setDisabledAnimationFilters(disabledAnimations, config.getDisabledAnimationPacks());
    }

    public void onInitialize() {
        CONFIG = NonConfig.load();
        NeedsOfNature.applyAnimationToggleConfig();
        NonStats.registerBaseStats();
        NonStats.registerKnownEntityStats();
        NonSoundEvents.registerAll();
        NonParticles.registerAll();
        NonStatusEffects.registerAll();
        NonPotions.registerAll();
        NonBrewingRecipes.registerAll();
        NonRecipeSerializers.registerAll();
        NonTrinketsIntegration.registerPredicates();
        FabricDefaultAttributeRegistry.register(HORSE_LIQUID_COLLECTOR_ENTITY_TYPE, (DefaultAttributeContainer.Builder)HorseLiquidCollectorEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(PREGNANCY_EGG_ENTITY_TYPE, (DefaultAttributeContainer.Builder)PregnancyEggEntity.createAttributes());
        PayloadTypeRegistry.playC2S().register(StopVoluntaryAnimationC2SPayload.ID, StopVoluntaryAnimationC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(StopAttackAnimationC2SPayload.ID, StopAttackAnimationC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(StartManualPeakC2SPayload.ID, StartManualPeakC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(SetPlayerGenderC2SPayload.ID, SetPlayerGenderC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(RepairDestroyedSkinC2SPayload.ID, RepairDestroyedSkinC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(ManualDamageDestroyedSkinC2SPayload.ID, ManualDamageDestroyedSkinC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(DestroyedSkinUploadC2SPayload.ID, DestroyedSkinUploadC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(DestroyedSkinMaskUploadC2SPayload.ID, DestroyedSkinMaskUploadC2SPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(AttackStateS2CPayload.ID, AttackStateS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(StageProgressS2CPayload.ID, StageProgressS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(LiquidStateS2CPayload.ID, LiquidStateS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(PregnancyStateS2CPayload.ID, PregnancyStateS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(GameplayRuntimeSettingsS2CPayload.ID, GameplayRuntimeSettingsS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(GenderSelectionPromptS2CPayload.ID, GenderSelectionPromptS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(HostConfigSyncS2CPayload.ID, HostConfigSyncS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(DestroyedSkinSyncS2CPayload.ID, DestroyedSkinSyncS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(DestroyedSkinMaskSyncS2CPayload.ID, DestroyedSkinMaskSyncS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(DestroyedSkinStateS2CPayload.ID, DestroyedSkinStateS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(DestroyedSkinParticlesS2CPayload.ID, DestroyedSkinParticlesS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(MessStateS2CPayload.ID, MessStateS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(AccessoryShedStateS2CPayload.ID, AccessoryShedStateS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SkinRippedInfoRequestS2CPayload.ID, SkinRippedInfoRequestS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ManualPeakPropOverrideS2CPayload.ID, ManualPeakPropOverrideS2CPayload.CODEC);
        NonGenderSystem.registerMobGenderTags();
        NonPeakStages.registerReloadListener();
        NonLoopSecondsOverrides.registerReloadListener();
        NonEscapableStages.registerReloadListener();
        NonAnimationEligibility.registerReloadListener();
        NonEntityProfiles.registerReloadListener();
        NonLiquidRoles.registerReloadListener();
        NonLiquidGainOverrides.registerReloadListener();
        NonLiquidGainMultipliers.registerReloadListener();
        NonPregnancyCues.registerReloadListener();
        NonReactiveImpactCues.registerReloadListener();
        NonManualPeakDefinitions.registerReloadListener();
        this.registerStartupResourceReload();
        this.registerDebugStaffInteraction();
        this.registerEnergyControlItemInteraction();
        this.registerPlayerAnimationJoinInteraction();
        NonGenderSystem.registerCowMilkingGenderGate();
        this.registerHorseLiquidCollectorBottleInteraction();
        this.registerHorseLiquidCollectorDrops();
        NonDestroyedSkinSystem.registerDestroyedSkinRepairNetworking();
        this.registerDebugCommands();
        this.registerLiquidPotionGroupCleanup();
        this.registerAfwListeners();
        this.registerLoopProgression();
        NonDestroyedSkinSystem.registerDestroyedSkinNetworking();
        this.registerVoluntaryStopRequests();
        this.registerAttackStopRequests();
        this.registerManualPeakRequests();
        this.registerNormalDamageDestroyedSkinDamage();
        NonGenderSystem.registerPlayerGenderRequests();
        this.registerFillBottleUseTrigger();
        this.registerLastDefeatedDeathRescue();
        this.registerPregnancyDeathCleanup();
        this.registerDeathPersistenceCopy();
        this.registerPostEscapeDamageInvulnerability();
        this.registerDirectAttackBlockCleanup();
        this.registerLiquidSync();
    }

    private void registerStartupResourceReload() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> server.execute(() -> {
            try {
                LOGGER.info("[NoN] Reloading server resources after startup so generated/default NoN packs are active on first world entry.");
                server.reloadResources(server.getDataPackManager().getEnabledIds()).exceptionally(throwable -> {
                    LOGGER.warn("[NoN] Startup server resource reload failed.", throwable);
                    return null;
                });
            }
            catch (RuntimeException e) {
                LOGGER.warn("[NoN] Failed to schedule startup server resource reload.", (Throwable)e);
            }
        }));
    }

    private void registerDebugStaffInteraction() {
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!(entity instanceof LivingEntity)) {
                return ActionResult.PASS;
            }
            LivingEntity living = (LivingEntity)entity;
            ItemStack stack = player.getStackInHand(hand);
            if (stack.isEmpty() || stack.getItem() != DEBUG_STAFF) {
                return ActionResult.PASS;
            }
            if (!(living instanceof EnergyHolder)) {
                return ActionResult.PASS;
            }
            if (world.isClient()) {
                return ActionResult.SUCCESS;
            }
            ActionResult result = stack.getItem().useOnEntity(stack, player, living, hand);
            return result == ActionResult.PASS ? ActionResult.SUCCESS : result;
        });
    }

    private void registerEnergyControlItemInteraction() {
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!(entity instanceof LivingEntity)) {
                return ActionResult.PASS;
            }
            LivingEntity living = (LivingEntity)entity;
            ItemStack stack = player.getStackInHand(hand);
            if (stack.isEmpty()) {
                return ActionResult.PASS;
            }
            if (!(stack.getItem() instanceof EnergyStabilizerItem) && !(stack.getItem() instanceof EnergyGainAdjustItem)) {
                return ActionResult.PASS;
            }
            if (!(living instanceof EnergyHolder)) {
                return ActionResult.PASS;
            }
            ActionResult result = stack.getItem().useOnEntity(stack, player, living, hand);
            return result == ActionResult.PASS ? ActionResult.SUCCESS : result;
        });
    }

    private void registerPlayerAnimationJoinInteraction() {
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            ServerWorld serverWorld;
            block8: {
                block7: {
                    if (!(world instanceof ServerWorld)) break block7;
                    serverWorld = (ServerWorld)world;
                    if (player instanceof ServerPlayerEntity) break block8;
                }
                return ActionResult.PASS;
            }
            ServerPlayerEntity joiningPlayer = (ServerPlayerEntity)player;
            if (hand != Hand.MAIN_HAND) {
                return ActionResult.PASS;
            }
            if (!joiningPlayer.getStackInHand(Hand.MAIN_HAND).isEmpty()) {
                return ActionResult.PASS;
            }
            if (joiningPlayer.isSpectator()) {
                return ActionResult.PASS;
            }
            if (entity == null || entity.isRemoved() || entity.getUuid().equals(joiningPlayer.getUuid())) {
                return ActionResult.PASS;
            }
            return NeedsOfNature.tryPlayerJoinActiveAnimation(serverWorld, joiningPlayer, entity) ? ActionResult.SUCCESS : ActionResult.PASS;
        });
    }

    private void registerHorseLiquidCollectorBottleInteraction() {
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!(entity instanceof HorseLiquidCollectorEntity)) {
                return ActionResult.PASS;
            }
            HorseLiquidCollectorEntity collector = (HorseLiquidCollectorEntity)entity;
            ItemStack stack = player.getStackInHand(hand);
            if (stack.isEmpty() || !stack.isOf(Items.GLASS_BOTTLE)) {
                return ActionResult.PASS;
            }
            if (world.isClient()) {
                return ActionResult.SUCCESS;
            }
            if (!(player instanceof ServerPlayerEntity)) {
                return ActionResult.SUCCESS;
            }
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity)player;
            if (!collector.isCollectorFull()) {
                return ActionResult.SUCCESS;
            }
            if (!NonItemSystem.consumeOneGlassBottle(serverPlayer, hand)) {
                return ActionResult.SUCCESS;
            }
            ItemStack resultBottle = NonItemSystem.createLiquidBottleStack(collector.getBottleLiquidEntityTypeId());
            collector.clearStoredLiquid();
            NonItemSystem.giveItemOrDrop(serverPlayer, resultBottle);
            world.playSound(null, collector.getX(), collector.getY(), collector.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.PLAYERS, 1.0f, 1.0f);
            return ActionResult.SUCCESS;
        });
    }

    private void registerHorseLiquidCollectorDrops() {
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            ServerPlayerEntity player;
            if (!(entity instanceof HorseLiquidCollectorEntity)) {
                return;
            }
            World patt0$temp = entity.getEntityWorld();
            if (!(patt0$temp instanceof ServerWorld)) {
                return;
            }
            ServerWorld world = (ServerWorld)patt0$temp;
            Entity attacker = damageSource.getAttacker();
            if (attacker instanceof ServerPlayerEntity && (player = (ServerPlayerEntity)attacker).isCreative()) {
                return;
            }
            entity.dropStack(world, new ItemStack((ItemConvertible)HORSE_LIQUID_COLLECTOR));
        });
    }

    private void registerDebugCommands() {
        NonCommands.register();
    }

    public static Identifier id(String path) {
        return Identifier.of((String)MOD_ID, (String)path);
    }

    private static void grantNoNAdvancement(ServerPlayerEntity player, String path) {
        NonAdvancementHooks.grant(player, path);
    }

    public static void grantEmergencySnackAdvancement(ServerPlayerEntity player) {
        NonAdvancementHooks.grantEmergencySnack(player);
    }

    public static void grantUdderlyUnfortunateAdvancementIfPregnant(ServerPlayerEntity player) {
        if (NeedsOfNature.getPregnantEntityTypeId(player) == null) {
            return;
        }
        NonAdvancementHooks.grantUdderlyUnfortunate(player);
    }

    public static void resetStableDietStreak(ServerPlayerEntity player) {
        NonAdvancementHooks.resetStableDietStreak(player);
    }

    private static void checkLiquidAdvancements(ServerPlayerEntity player, LiquidHolder holder) {
        NonAdvancementHooks.checkLiquidAdvancements(player, holder);
    }

    private static void trackFieldResearch(ServerWorld world, ServerPlayerEntity player, Map<Identifier, Integer> perEntityAmounts) {
        NonAdvancementHooks.trackFieldResearch(world, player, perEntityAmounts);
    }

    static void checkBadIdeaBeaconAdvancement(ServerPlayerEntity player) {
        NonAdvancementHooks.checkBadIdeaBeacon(player);
    }

    static boolean isMessSystemEnabled() {
        NonConfig config = NeedsOfNature.getConfig();
        return config == null || config.isMessSystemEnabled();
    }

    static boolean isDestroyedSkinSystemEnabled() {
        NonConfig config = NeedsOfNature.getConfig();
        return config == null || config.isDestroyedSkinSystemEnabled();
    }

    static int getDestroyedSkinStageForPlayer(ServerPlayerEntity player) {
        return NonDestroyedSkinSystem.getDestroyedSkinStageForPlayer(player);
    }

    static int getTotalMessForPlayer(ServerPlayerEntity player) {
        if (player == null || !NeedsOfNature.isMessSystemEnabled()) {
            return 0;
        }
        if (!(player instanceof MessHolder)) {
            return 0;
        }
        MessHolder holder = (MessHolder)player;
        return NonMessSystem.clampMess(holder.getVMess()) + NonMessSystem.clampMess(holder.getAMess()) + NonMessSystem.clampMess(holder.getMMess());
    }

    public static UUID getActivePlayerInstance(ServerPlayerEntity player) {
        PlayerActiveInstance tracked = ACTIVE_PLAYER_INSTANCES.get(player.getUuid());
        if (tracked == null) {
            return null;
        }
        if (!tracked.worldKey.equals((Object)player.getEntityWorld().getRegistryKey())) {
            return null;
        }
        return tracked.instanceId();
    }

    static boolean hasActivePlayerAnimation(ServerPlayerEntity player) {
        if (player == null) {
            return false;
        }
        UUID active = NeedsOfNature.getActivePlayerInstance(player);
        return active != null;
    }

    public static boolean instanceContainsActor(UUID instanceId, UUID actorUuid) {
        if (instanceId == null || actorUuid == null) {
            return false;
        }
        List<UUID> actors = INSTANCE_ACTORS.get(instanceId);
        return actors != null && actors.contains(actorUuid);
    }

    public static boolean hasNearbyVisibleAnimation(ServerWorld world, MobEntity viewer) {
        return NeedsOfNature.getNearbyVisibleAnimationGainMultiplier(world, viewer) > 1.0;
    }

    public static double getNearbyVisibleAnimationGainMultiplier(ServerWorld world, MobEntity viewer) {
        if (world == null || viewer == null) {
            return 1.0;
        }
        if (INSTANCE_ACTORS.isEmpty()) {
            return 1.0;
        }
        double maxDistSq = 400.0;
        UUID viewerUuid = viewer.getUuid();
        double best = 1.0;
        for (List<UUID> actorUuids : INSTANCE_ACTORS.values()) {
            if (actorUuids == null || actorUuids.isEmpty() || actorUuids.contains(viewerUuid)) continue;
            for (UUID actorUuid : actorUuids) {
                LivingEntity living;
                Entity actor;
                if (actorUuid == null || !((actor = world.getEntity(actorUuid)) instanceof LivingEntity) || !(living = (LivingEntity)actor).isAlive() || living.isRemoved() || viewer.squaredDistanceTo((Entity)living) > maxDistSq || !viewer.canSee((Entity)living)) continue;
                double multiplier = NeedsOfNature.getNearAnimationGainMult();
                if (living instanceof ServerPlayerEntity) {
                    ServerPlayerEntity player = (ServerPlayerEntity)living;
                    multiplier *= NonTrinketsIntegration.getAccessoryEffects((LivingEntity)player).nearAnimationMobEnergyMultiplier();
                }
                best = Math.max(best, multiplier);
            }
        }
        return best;
    }

    public static float getNearAnimationGainMult() {
        NonConfig config = NeedsOfNature.getConfig();
        if (config == null) {
            return 3.0f;
        }
        return (float)config.getNearAnimationEnergyGainMult();
    }

    public static float getNearAnimationGainMult(@Nullable ServerPlayerEntity player) {
        float base = NeedsOfNature.getNearAnimationGainMult();
        if (player == null) {
            return base;
        }
        return (float)((double)base * NonTrinketsIntegration.getAccessoryEffects((LivingEntity)player).nearAnimationMobEnergyMultiplier());
    }

    public static void tickPlayerEnergyAura(ServerWorld world, ServerPlayerEntity player) {
        NonAuraSystem.tickPlayerEnergyAura(world, player);
    }

    public static PlayerEnergyAuraDebug getPlayerEnergyAuraDebug(ServerWorld world, MobEntity mob, EnergyHolder holder) {
        NonAuraSystem.PlayerEnergyAuraDebug debug = NonAuraSystem.getPlayerEnergyAuraDebug(world, mob, holder);
        if (debug == null) {
            return null;
        }
        return new PlayerEnergyAuraDebug(debug.player(), debug.totalMultiplier(), debug.playerEnergy(), debug.destroyedSkinStage(), debug.messTotal(), debug.radius());
    }

    private static Item registerItem(RegistryKey<Item> key, Item item) {
        return (Item)Registry.register((Registry)Registries.ITEM, key, (Object)item);
    }

    private static List<Item> registerDataDrivenAccessories() {
        NonAccessoryItemRegistry.registerStartupItems();
        NonAccessoryDefinitions.loadStartupDefinitions();
        return NonAccessoryItemRegistry.getRegisteredItems();
    }

    private void registerLiquidPotionGroupCleanup() {
        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((group, entries) -> {
            Identifier groupId = Registries.ITEM_GROUP.getId((Object)group);
            if (groupId != null && groupId.equals((Object)NeedsOfNature.id("main"))) {
                return;
            }
            entries.getDisplayStacks().removeIf(NonItemSystem::isNonPotionVariantStack);
            entries.getSearchTabStacks().removeIf(NonItemSystem::isNonPotionVariantStack);
        });
    }

    private void registerPregnancyDeathCleanup() {
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            if (entity instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity)entity;
                NonConfig config = NeedsOfNature.getConfig();
                if (config != null && config.keepPregnancyAfterDeath()) {
                    return;
                }
                NeedsOfNature.clearPregnancyOnDeath(player);
            }
        });
    }

    private void registerDeathPersistenceCopy() {
        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            if (alive) {
                return;
            }
            NeedsOfNature.copyDeathPersistentPlayerState(oldPlayer, newPlayer);
        });
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            if (alive) {
                return;
            }
            NeedsOfNature.schedulePostDeathStateSync(newPlayer);
        });
    }

    private static void copyDeathPersistentPlayerState(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer) {
        if (oldPlayer == null || newPlayer == null) {
            return;
        }
        NonConfig config = NeedsOfNature.getConfig();
        if (config == null) {
            return;
        }
        if (config.isMessSystemEnabled()) {
            if (config.keepMessAfterDeath()) {
                MessSnapshot.copy(oldPlayer, newPlayer);
            } else {
                NonMessSystem.clearMessStoredState(newPlayer);
            }
        }
        if (config.isDestroyedSkinSystemEnabled()) {
            if (config.keepRippedSkinAfterDeath()) {
                RippedSkinSnapshot.copy(oldPlayer, newPlayer);
            } else {
                NonDestroyedSkinSystem.clearDestroyedSkinStoredState(newPlayer);
            }
        }
        if (config.isLiquidTankEnabled()) {
            if (config.keepLiquidTankAfterDeath()) {
                LiquidSnapshot.copy(oldPlayer, newPlayer);
            } else {
                NeedsOfNature.clearLiquidStoredState(newPlayer);
            }
        }
        if (config.isPregnancyEnabled() && config.keepPregnancyAfterDeath()) {
            PregnancySnapshot.copy(oldPlayer, newPlayer);
        }
    }

    private static void schedulePostDeathStateSync(ServerPlayerEntity player) {
        ServerWorld class_32182;
        if (player == null || !((class_32182 = player.getEntityWorld()) instanceof ServerWorld)) {
            return;
        }
        ServerWorld world = class_32182;
        PENDING_POST_DEATH_STATE_SYNCS.put(player.getUuid(), new PostDeathStateSync((RegistryKey<World>)world.getRegistryKey(), world.getTime() + 2L));
    }

    private static void tickPostDeathStateSync(ServerWorld world, long now) {
        if (world == null || PENDING_POST_DEATH_STATE_SYNCS.isEmpty()) {
            return;
        }
        RegistryKey worldKey = world.getRegistryKey();
        for (Map.Entry<UUID, PostDeathStateSync> entry : PENDING_POST_DEATH_STATE_SYNCS.entrySet()) {
            UUID playerUuid = entry.getKey();
            PostDeathStateSync sync = entry.getValue();
            if (playerUuid == null || sync == null) {
                if (playerUuid == null) continue;
                PENDING_POST_DEATH_STATE_SYNCS.remove(playerUuid);
                continue;
            }
            if (!worldKey.equals(sync.worldKey()) || now < sync.dueTick()) continue;
            ServerPlayerEntity player = world.getServer().getPlayerManager().getPlayer(playerUuid);
            if (player != null) {
                NeedsOfNature.syncPostDeathPlayerState(player);
            }
            PENDING_POST_DEATH_STATE_SYNCS.remove(playerUuid, sync);
        }
    }

    private static void syncPostDeathPlayerState(ServerPlayerEntity player) {
        if (player == null) {
            return;
        }
        NonConfig config = NeedsOfNature.getConfig();
        if (config == null) {
            return;
        }
        if (!config.keepMessAfterDeath()) {
            NonMessSystem.clearMessStoredState(player);
        }
        NonMessSystem.broadcastMessState(player);
        if (!config.keepRippedSkinAfterDeath()) {
            NonDestroyedSkinSystem.clearDestroyedSkinStoredState(player);
        }
        NonDestroyedSkinSystem.broadcastDestroyedSkinStage(player);
        if (!config.keepLiquidTankAfterDeath()) {
            NeedsOfNature.clearLiquidStoredState(player);
        }
        NeedsOfNature.syncLiquidState(player);
        NeedsOfNature.syncPregnancyState(player);
    }

    private static void clearLiquidStoredState(ServerPlayerEntity player) {
        if (!(player instanceof LiquidHolder)) {
            return;
        }
        LiquidHolder holder = (LiquidHolder)player;
        holder.setLiquidStored(0);
        holder.clearLiquidComposition();
    }

    private void registerLastDefeatedDeathRescue() {
        ServerLivingEntityEvents.ALLOW_DEATH.register((entity, damageSource, damageAmount) -> {
            MobEntity mob;
            MobEntity damagingMob;
            MobEntity mob2;
            Entity sourceEntity;
            if (!(entity instanceof ServerPlayerEntity)) {
                return true;
            }
            ServerPlayerEntity player = (ServerPlayerEntity)entity;
            ServerWorld patt0$temp = player.getEntityWorld();
            if (!(patt0$temp instanceof ServerWorld)) {
                return true;
            }
            ServerWorld world = patt0$temp;
            NonConfig config = NeedsOfNature.getConfig();
            if (config == null || !config.isLastDefeatedEnabled()) {
                return true;
            }
            if (NeedsOfNature.hasVanillaTotemReady(player)) {
                return true;
            }
            if (AfwAnimationApi.isActorPendingOrActive((ServerWorld)world, (UUID)player.getUuid())) {
                return true;
            }
            Entity attacker = damageSource == null ? null : damageSource.getAttacker();
            Entity class_12972 = sourceEntity = damageSource == null ? null : damageSource.getSource();
            Object object = attacker instanceof MobEntity ? (mob2 = (MobEntity)attacker) : (damagingMob = sourceEntity instanceof MobEntity ? (mob = (MobEntity)sourceEntity) : null);
            if (damagingMob == null) {
                return true;
            }
            long nowTick = NeedsOfNature.getGlobalTick(world);
            Long cooldownUntil = LAST_DEFEATED_COOLDOWN_UNTIL_BY_PLAYER.get(player.getUuid());
            if (cooldownUntil != null && cooldownUntil > nowTick) {
                return true;
            }
            LastDefeatedCandidate candidate = NeedsOfNature.findLastDefeatedCandidate(world, player, damagingMob);
            if (candidate == null) {
                return true;
            }
            player.setHealth(1.0f);
            UUID instanceId = NeedsOfNature.startAnimationNowWithMetadata(world, candidate.defeatMatch().animationId(), List.of(player), candidate.defeatMatch().actorKeys(), candidate.defeatMatch().stages(), AfwDamageBehavior.BLOCK_DAMAGE, true, (Entity)player, NeedsOfNature.buildModeMetadata(true));
            if (instanceId == null) {
                player.setHealth(0.0f);
                return true;
            }
            int cooldownTicks = config.getLastDefeatedCooldownSeconds() * 20;
            if (cooldownTicks > 0) {
                LAST_DEFEATED_COOLDOWN_UNTIL_BY_PLAYER.put(player.getUuid(), nowTick + (long)cooldownTicks);
            }
            LOGGER.info("[NoN] Last defeated rescued player={} from lethal mob damage. mob={} sourceAnimation={} defeatedAnimation={} instance={}", new Object[]{player.getName().getString(), Registries.ENTITY_TYPE.getId((Object)candidate.mob().getType()), candidate.gatherCandidate().animationId(), candidate.defeatMatch().animationId(), instanceId});
            return false;
        });
    }

    private void registerDirectAttackBlockCleanup() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (!(entity instanceof ServerPlayerEntity)) {
                return true;
            }
            ServerPlayerEntity player = (ServerPlayerEntity)entity;
            ServerWorld patt0$temp = player.getEntityWorld();
            if (!(patt0$temp instanceof ServerWorld)) {
                return true;
            }
            ServerWorld world = patt0$temp;
            Entity attacker = source.getAttacker();
            if (!(attacker instanceof MobEntity)) {
                return true;
            }
            MobEntity mob = (MobEntity)attacker;
            Entity directSource = source.getSource();
            if (directSource != null && directSource != attacker) {
                return true;
            }
            AttackBlockReason reason = NeedsOfNature.getMobAttackBlockReason(world, mob, player);
            if (reason == null) {
                return true;
            }
            NeedsOfNature.onMobAttackBlocked(world, mob, player, reason);
            return false;
        });
    }

    private void registerPostEscapeDamageInvulnerability() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (!(entity instanceof ServerPlayerEntity)) {
                return true;
            }
            ServerPlayerEntity player = (ServerPlayerEntity)entity;
            ServerWorld patt0$temp = player.getEntityWorld();
            if (!(patt0$temp instanceof ServerWorld)) {
                return true;
            }
            ServerWorld world = patt0$temp;
            return !NeedsOfNature.isPostEscapeDamageInvulnerable(world, player);
        });
    }

    private static void setPostEscapeDamageInvulnerable(ServerPlayerEntity player, int ticks) {
        if (player == null) {
            return;
        }
        ServerWorld class_32182 = player.getEntityWorld();
        if (!(class_32182 instanceof ServerWorld)) {
            return;
        }
        ServerWorld world = class_32182;
        if (ticks <= 0) {
            POST_ESCAPE_DAMAGE_INVULNERABLE_UNTIL_BY_PLAYER.remove(player.getUuid());
            return;
        }
        POST_ESCAPE_DAMAGE_INVULNERABLE_UNTIL_BY_PLAYER.put(player.getUuid(), world.getTime() + (long)ticks);
    }

    private static boolean isPostEscapeDamageInvulnerable(ServerWorld world, ServerPlayerEntity player) {
        if (world == null || player == null) {
            return false;
        }
        Long untilTick = POST_ESCAPE_DAMAGE_INVULNERABLE_UNTIL_BY_PLAYER.get(player.getUuid());
        if (untilTick == null) {
            return false;
        }
        if (world.getTime() >= untilTick) {
            POST_ESCAPE_DAMAGE_INVULNERABLE_UNTIL_BY_PLAYER.remove(player.getUuid(), untilTick);
            return false;
        }
        return true;
    }

    private void registerNormalDamageDestroyedSkinDamage() {
        ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseDamageTaken, damageTaken, blocked) -> {
            if (blocked || damageTaken <= 0.0f) {
                return;
            }
            if (!(entity instanceof ServerPlayerEntity)) {
                return;
            }
            ServerPlayerEntity player = (ServerPlayerEntity)entity;
            ServerWorld patt0$temp = player.getEntityWorld();
            if (!(patt0$temp instanceof ServerWorld)) {
                return;
            }
            ServerWorld world = patt0$temp;
            if (!NeedsOfNature.isDestroyedSkinSystemEnabled()) {
                return;
            }
            if (AfwAnimationApi.isActorPendingOrActive((ServerWorld)world, (UUID)player.getUuid())) {
                return;
            }
            int chance = NeedsOfNature.getConfig().getNormalDamageDestroyedSkinChancePercent();
            if (chance <= 0) {
                return;
            }
            if (chance < 100 && world.getRandom().nextInt(100) >= chance) {
                return;
            }
            NonDestroyedSkinSystem.applyDestroyedSkinAttackDamage(world, player);
        });
    }

    private void registerAfwListeners() {
        AfwAnimationEvents.START.register((world, instanceId, animationId, actorUuids, actorKeys, stages, startTick, requester, damageBehavior, ignoreAttackers) -> {
            NeedsOfNature.clearPendingGatherPairsForActors(world, actorUuids);
            boolean hasPlayer = false;
            ArrayList<ServerPlayerEntity> playerActors = new ArrayList<ServerPlayerEntity>();
            for (UUID uuid : actorUuids) {
                Entity e = world.getEntity(uuid);
                if (!(e instanceof ServerPlayerEntity)) continue;
                ServerPlayerEntity player = (ServerPlayerEntity)e;
                hasPlayer = true;
                playerActors.add(player);
            }
            Map<String, String> metadata = NeedsOfNature.getInstanceMetadata(world, instanceId);
            NeedsOfNature.adoptPendingBreedingRequest(world, instanceId, metadata);
            NeedsOfNature.adoptPendingPregnancyHatch(world, instanceId, actorUuids, metadata);
            String mode = metadata == null ? null : metadata.get(NON_MODE_META_KEY);
            boolean isPregnancy = hasPlayer && NON_MODE_PREGNANCY.equalsIgnoreCase(mode);
            boolean isAttack = hasPlayer && NON_MODE_ATTACK.equalsIgnoreCase(mode);
            boolean isDefeatAnimation = NeedsOfNature.hasAnimationTagWithPrefix(animationId, AFW_ANIMATION_TAG_DEFEAT_PREFIX);
            if (!isAttack && hasPlayer && mode == null && NeedsOfNature.consumeAttackJoinCarryover((RegistryKey<World>)world.getRegistryKey(), actorUuids, world.getTime())) {
                isAttack = true;
            }
            NeedsOfNature.applyPlayerAnimationStartEnergy(world, playerActors, animationId, metadata);
            for (UUID uUID : actorUuids) {
                Entity e = world.getEntity(uUID);
                if (e instanceof EnergyHolder) {
                    EnergyHolder holder = (EnergyHolder)e;
                    holder.onNonAnimationStarted();
                }
                if (!(e instanceof ServerPlayerEntity)) continue;
                ServerPlayerEntity player = (ServerPlayerEntity)e;
                ACTIVE_PLAYER_INSTANCES.put(player.getUuid(), new PlayerActiveInstance(instanceId, (RegistryKey<World>)world.getRegistryKey()));
            }
            if (!playerActors.isEmpty()) {
                for (ServerPlayerEntity class_32222 : playerActors) {
                    NonStats.incrementAnimationStarted(class_32222);
                    if (actorUuids.size() > 2) {
                        NonStats.incrementMultiActorAnimation(class_32222);
                    }
                    if (actorUuids.size() >= 4) {
                        NonAdvancementHooks.grantGroupActivity(class_32222);
                    }
                    if (NeedsOfNature.isWaterAnimation(animationId)) {
                        NonStats.incrementWaterAnimation(class_32222);
                    }
                    if (isAttack && !isDefeatAnimation) {
                        NonStats.incrementAttackAnimationStarted(class_32222);
                        continue;
                    }
                    if (isAttack) continue;
                    NonAdvancementHooks.resetTossedAround(class_32222);
                }
            }
            if (NeedsOfNature.isFillBottleAnimation(animationId) && !playerActors.isEmpty()) {
                FILL_BOTTLE_STATES.putIfAbsent(instanceId, new FillBottleState(((ServerPlayerEntity)playerActors.getFirst()).getUuid(), false));
            }
            if (isPregnancy) {
                for (ServerPlayerEntity class_32223 : playerActors) {
                    class_32223.removeStatusEffect(Registries.STATUS_EFFECT.getEntry((Object)NonStatusEffects.PREGNANT));
                }
            }
            NonAccessoryShedSystem.ShedState animationAccessoryShedState = NonAccessoryShedSystem.resolveShedStateForAnimation(animationId);
            if (!playerActors.isEmpty()) {
                for (ServerPlayerEntity player : playerActors) {
                    NonAccessoryShedSystem.ShedState accessoryShedState = animationAccessoryShedState;
                    if (isPregnancy) {
                        accessoryShedState = accessoryShedState.merge(NonAccessoryShedSystem.forSlot(NonTrinketsIntegration.getActiveTankSlotId((LivingEntity)player)));
                    }
                    if (accessoryShedState.isEmpty()) continue;
                    NonAccessoryShedSystem.addServerShedState(world, player, accessoryShedState);
                }
            }
            INSTANCE_CONTROLS.put(instanceId, new InstanceControl(animationId, !isAttack, hasPlayer, damageBehavior, ignoreAttackers, mode));
            if (NeedsOfNature.shouldApplyDestroyedSkinAttackDamage(isAttack, metadata, animationId) && !playerActors.isEmpty()) {
                for (ServerPlayerEntity player : playerActors) {
                    NonDestroyedSkinSystem.applyDestroyedSkinAttackDamage(world, player);
                }
            }
            int n = NeedsOfNature.resolveStartStage(stages);
            AnimationStageInfo startStageInfo = NeedsOfNature.stageInfoForNumber(stages, n);
            long interval = NeedsOfNature.resolveLoopAdvanceIntervalTicks(animationId, n, startStageInfo);
            if (interval >= 0L) {
                LOOP_ADVANCE.put(instanceId, new LoopAdvance((RegistryKey<World>)world.getRegistryKey(), startTick + interval));
            }
            NeedsOfNature.syncStageProgressState(instanceId, startStageInfo, startTick, interval, playerActors);
            Integer peakStage = NonPeakStages.getPeakStage(animationId);
            if (peakStage != null) {
                boolean grantAtStart = n == peakStage;
                int actualAddedMl = 0;
                if (grantAtStart) {
                    actualAddedMl = NeedsOfNature.applyPeakStageEffects(world, instanceId, actorUuids, actorKeys, animationId, startStageInfo, n, startTick);
                }
                PEAK_STATES.put(instanceId, new PeakState(animationId, peakStage, grantAtStart, n, hasPlayer, grantAtStart, actualAddedMl));
                if (grantAtStart && isAttack && !NeedsOfNature.isManualPeakAnimation(animationId)) {
                    NeedsOfNature.queueDefeatedTailForInstance(world, instanceId, actorUuids);
                }
            }
            INSTANCE_ACTORS.put(instanceId, List.copyOf(actorUuids));
            NeedsOfNature.refreshPregnancyCueRuntime(world, instanceId, startTick, animationId);
            if (!playerActors.isEmpty()) {
                NeedsOfNature.sendEscapeControlState(instanceId, isAttack, isDefeatAnimation, startStageInfo, playerActors);
            }
        });
        AfwAnimationEvents.STOP.register((world, instanceId, animationId, actorUuids, actorKeys, stages) -> {
            NeedsOfNature.clearPendingGatherPairsForActors(world, actorUuids);
            NeedsOfNature.clearEscapeControlState(world, instanceId, actorUuids);
            for (UUID uuid : actorUuids) {
                PlayerActiveInstance tracked = ACTIVE_PLAYER_INSTANCES.get(uuid);
                if (tracked == null || !tracked.instanceId().equals(instanceId) || !tracked.worldKey().equals((Object)world.getRegistryKey())) continue;
                ACTIVE_PLAYER_INSTANCES.remove(uuid);
            }
            LOOP_ADVANCE.remove(instanceId);
            PEAK_LIQUID_PROGRESS.remove(instanceId);
            boolean isAttack = NeedsOfNature.isInstanceAttack(instanceId);
            PeakState peakState = PEAK_STATES.remove(instanceId);
            if (peakState != null && peakState.peaked) {
                if (isAttack) {
                    NeedsOfNature.recordAttackOutcomeForMobs(world, actorUuids, "peak");
                }
                if (peakState.hasPlayer) {
                    NeedsOfNature.calmMobsAfterPeakedPlayerAnimation(world, actorUuids);
                }
                if (!NeedsOfNature.isManualPeakAnimation(peakState.animationId) && !NeedsOfNature.isFillBottleAnimation(peakState.animationId)) {
                    if (peakState.hasPlayer) {
                        int xpEligibleMl = NeedsOfNature.resolveXpEligibleMl(world, actorUuids, actorKeys, peakState.animationId, peakState.actualAddedMl);
                        NeedsOfNature.spawnPeakXp(world, actorUuids, xpEligibleMl);
                    }
                    NeedsOfNature.scheduleReceiverDripOnPeak(world, actorUuids, actorKeys, peakState.animationId, world.getTime());
                }
            }
            boolean peaked = peakState != null && peakState.peaked;
            NeedsOfNature.finalizeAnimationBreeding(world, instanceId, peaked);
            FillBottleState fillState = FILL_BOTTLE_STATES.remove(instanceId);
            if (fillState != null) {
                NeedsOfNature.handleFillBottleStop(world, fillState, peaked);
            }
            NeedsOfNature.finalizePregnancyHatch(world, instanceId, false);
            INSTANCE_CONTROLS.remove(instanceId);
            INSTANCE_ACTORS.remove(instanceId);
            DEFEATED_QUEUED_INSTANCES.remove(instanceId);
        });
        AfwAnimationEvents.STAGE_ADVANCE.register((world, instanceId, animationId, actorUuids, actorKeys, advanceTick) -> {
            LoopAdvance existing = LOOP_ADVANCE.get(instanceId);
            AnimationStageInfo advancedStage = AfwAnimationApi.getCurrentStage((ServerWorld)world, (UUID)instanceId);
            Integer advancedStageNumber = NonPeakStages.stageNumberFromId(advancedStage == null ? null : advancedStage.animationId());
            long advancedStageInterval = NeedsOfNature.resolveLoopAdvanceIntervalTicks(animationId, advancedStageNumber, advancedStage);
            if (existing != null) {
                if (advancedStageInterval >= 0L) {
                    LOOP_ADVANCE.put(instanceId, new LoopAdvance(existing.worldKey, advanceTick + advancedStageInterval));
                } else {
                    LOOP_ADVANCE.remove(instanceId);
                }
            }
            NeedsOfNature.syncStageProgressState(instanceId, advancedStage, advanceTick, advancedStageInterval, NeedsOfNature.collectPlayerActors(world, actorUuids));
            NeedsOfNature.refreshPregnancyCueRuntime(world, instanceId, advanceTick, animationId);
            NeedsOfNature.routePregnancyBirthStage(world, instanceId);
            AnimationStageInfo currentStage = AfwAnimationApi.getCurrentStage((ServerWorld)world, (UUID)instanceId);
            NeedsOfNature.syncEscapeControlState(world, instanceId, actorUuids, currentStage);
            PeakState peakState = PEAK_STATES.get(instanceId);
            if (peakState == null) {
                return;
            }
            int nextStage = peakState.lastStage;
            Integer stageNumber = NonPeakStages.stageNumberFromId(currentStage == null ? null : currentStage.animationId());
            if (stageNumber != null) {
                nextStage = stageNumber;
            }
            boolean liquidGranted = peakState.liquidGranted;
            int actualAddedMl = peakState.actualAddedMl;
            if (!liquidGranted && stageNumber != null && stageNumber == peakState.peakStage) {
                liquidGranted = true;
                actualAddedMl += NeedsOfNature.applyPeakStageEffects(world, instanceId, actorUuids, actorKeys, animationId, currentStage, stageNumber, advanceTick);
                InstanceControl control = INSTANCE_CONTROLS.get(instanceId);
                if (control != null && !control.voluntary() && peakState.hasPlayer && !NeedsOfNature.isManualPeakAnimation(animationId) && !NeedsOfNature.isFillBottleAnimation(animationId)) {
                    NeedsOfNature.queueDefeatedTailForInstance(world, instanceId, actorUuids);
                }
            } else if (liquidGranted && stageNumber != null && stageNumber != peakState.peakStage) {
                PEAK_LIQUID_PROGRESS.remove(instanceId);
            }
            boolean peaked = peakState.peaked;
            if (!peaked) {
                if (stageNumber != null && stageNumber == peakState.peakStage) {
                    peaked = true;
                } else if (peakState.lastStage == peakState.peakStage) {
                    peaked = true;
                }
            }
            PEAK_STATES.put(instanceId, new PeakState(peakState.animationId, peakState.peakStage, peaked, nextStage, peakState.hasPlayer, liquidGranted, actualAddedMl));
        });
    }

    private static void syncEscapeControlState(ServerWorld world, UUID instanceId, List<UUID> actorUuids, @Nullable AnimationStageInfo currentStage) {
        List<ServerPlayerEntity> playerActors = NeedsOfNature.collectPlayerActors(world, actorUuids);
        if (playerActors.isEmpty()) {
            return;
        }
        InstanceControl control = INSTANCE_CONTROLS.get(instanceId);
        boolean isAttack = control != null && !control.voluntary();
        Identifier animationId = control == null ? null : control.animationId();
        boolean isDefeatAnimation = NeedsOfNature.hasAnimationTagWithPrefix(animationId, AFW_ANIMATION_TAG_DEFEAT_PREFIX);
        NeedsOfNature.sendEscapeControlState(instanceId, isAttack, isDefeatAnimation, currentStage, playerActors);
    }

    private static void sendEscapeControlState(UUID instanceId, boolean isAttack, boolean isDefeatAnimation, @Nullable AnimationStageInfo currentStage, List<ServerPlayerEntity> playerActors) {
        if (instanceId == null || playerActors == null || playerActors.isEmpty()) {
            return;
        }
        boolean escapable = isAttack && NeedsOfNature.isEscapableStage(currentStage);
        AttackStateS2CPayload.EscapeProfile profile = AttackStateS2CPayload.EscapeProfile.NONE;
        if (isAttack) {
            profile = isDefeatAnimation ? AttackStateS2CPayload.EscapeProfile.DEFEATED : AttackStateS2CPayload.EscapeProfile.NORMAL;
        }
        AttackStateS2CPayload payload = new AttackStateS2CPayload(instanceId, isAttack, escapable, profile);
        for (ServerPlayerEntity player : playerActors) {
            ServerPlayNetworking.send((ServerPlayerEntity)player, (CustomPayload)payload);
        }
    }

    private static void clearEscapeControlState(ServerWorld world, UUID instanceId, List<UUID> actorUuids) {
        if (world == null || instanceId == null) {
            return;
        }
        List<ServerPlayerEntity> playerActors = NeedsOfNature.collectPlayerActors(world, actorUuids);
        if (playerActors.isEmpty()) {
            return;
        }
        AttackStateS2CPayload payload = new AttackStateS2CPayload(instanceId, false, false, AttackStateS2CPayload.EscapeProfile.NONE);
        for (ServerPlayerEntity player : playerActors) {
            ServerPlayNetworking.send((ServerPlayerEntity)player, (CustomPayload)payload);
        }
    }

    private static void syncStageProgressState(UUID instanceId, @Nullable AnimationStageInfo stage, long startTick, long intervalTicks, List<ServerPlayerEntity> playerActors) {
        StageProgressS2CPayload.Mode mode;
        if (instanceId == null || stage == null || stage.animationId() == null || playerActors == null || playerActors.isEmpty()) {
            return;
        }
        long endTick = -1L;
        if (!stage.loop()) {
            mode = StageProgressS2CPayload.Mode.AFW_TIMELINE;
        } else if (intervalTicks < 0L) {
            mode = StageProgressS2CPayload.Mode.HIDDEN;
        } else {
            mode = StageProgressS2CPayload.Mode.SERVER_WINDOW;
            endTick = startTick + Math.max(1L, intervalTicks);
        }
        StageProgressS2CPayload payload = new StageProgressS2CPayload(instanceId, stage.animationId(), startTick, endTick, mode);
        for (ServerPlayerEntity player : playerActors) {
            ServerPlayNetworking.send((ServerPlayerEntity)player, (CustomPayload)payload);
        }
    }

    private static boolean isEscapableStage(@Nullable AnimationStageInfo stage) {
        if (stage == null || stage.animationId() == null) {
            return true;
        }
        return NonEscapableStages.isEscapable(stage.animationId());
    }

    private static int resolveXpEligibleMl(ServerWorld world, List<UUID> actorUuids, List<String> actorKeys, Identifier animationId, int actualAddedMl) {
        if (actualAddedMl <= 0) {
            return 0;
        }
        LiquidGainProfile gain = NeedsOfNature.resolveLiquidGainProfile(world, actorUuids, actorKeys, animationId);
        int total = Math.max(0, gain.amount());
        int eligible = Math.max(0, gain.xpEligibleAmount());
        if (total <= 0 || eligible <= 0) {
            return 0;
        }
        if (eligible >= total) {
            return actualAddedMl;
        }
        int scaled = (int)Math.round((double)eligible * (double)actualAddedMl / (double)total);
        return Math.max(0, Math.min(actualAddedMl, scaled));
    }

    private static void spawnPeakXp(ServerWorld world, List<UUID> actorUuids, int xpEligibleMl) {
        if (world == null || actorUuids == null || actorUuids.isEmpty()) {
            return;
        }
        if (xpEligibleMl <= 0) {
            return;
        }
        double xpPerMl = NeedsOfNature.getConfig().getPeakXpPerMl();
        if (xpPerMl <= 0.0) {
            return;
        }
        int xpAmount = Math.max(1, (int)Math.round((double)xpEligibleMl * xpPerMl));
        xpAmount = Math.min(20, xpAmount);
        for (UUID uuid : actorUuids) {
            Entity entity = world.getEntity(uuid);
            if (entity == null) continue;
            Vec3d pos = new Vec3d(entity.getX(), entity.getBodyY(0.5), entity.getZ());
            ExperienceOrbEntity.spawn((ServerWorld)world, (Vec3d)pos, (int)xpAmount);
        }
    }

    private static void applyPlayerAnimationStartEnergy(ServerWorld world, List<ServerPlayerEntity> playerActors, Identifier animationId, @Nullable Map<String, String> metadata) {
        if (world == null || playerActors == null || playerActors.isEmpty()) {
            return;
        }
        if (!NeedsOfNature.shouldApplyPlayerAnimationStartEnergy(animationId, metadata)) {
            return;
        }
        for (ServerPlayerEntity player : playerActors) {
            if (!(player instanceof EnergyHolder)) continue;
            EnergyHolder holder = (EnergyHolder)player;
            int current = Math.max(0, holder.getEnergy());
            holder.setEnergy(Math.min(holder.getMaxEnergy(), current + 20));
            NeedsOfNature.updateEnergizedEffect(player, holder);
        }
    }

    private static boolean shouldApplyPlayerAnimationStartEnergy(Identifier animationId, @Nullable Map<String, String> metadata) {
        if (animationId == null) {
            return false;
        }
        if (metadata != null && Boolean.parseBoolean(metadata.getOrDefault(AFW_JOIN_REPLACE_META_KEY, "false"))) {
            return false;
        }
        if (NeedsOfNature.isManualPeakAnimation(animationId)) {
            return false;
        }
        if (NeedsOfNature.isBirthAnimation(animationId)) {
            return false;
        }
        return !NeedsOfNature.isFillBottleAnimation(animationId);
    }

    private static void applyEnergyOnPeak(ServerWorld world, List<UUID> actorUuids, boolean manualPeak) {
        if (world == null || actorUuids == null || actorUuids.isEmpty()) {
            return;
        }
        for (UUID uuid : actorUuids) {
            Entity entity = world.getEntity(uuid);
            if (!(entity instanceof EnergyHolder)) continue;
            EnergyHolder holder = (EnergyHolder)entity;
            if (entity instanceof ServerPlayerEntity) {
                int current = Math.max(0, holder.getEnergy());
                int next = manualPeak ? (int)Math.floor((double)current * 0.6666666666666666) : current / 2;
                holder.setEnergy(next);
                continue;
            }
            holder.setEnergy(0);
            float currentMult = holder.getEnergyGainMultiplier();
            if (!Float.isFinite(currentMult) || !(currentMult > 0.0f)) continue;
            holder.setEnergyGainMultiplier(Math.min(6.0f, currentMult + 0.05f));
        }
    }

    private static int applyPeakStageEffects(ServerWorld world, UUID instanceId, List<UUID> actorUuids, List<String> actorKeys, Identifier animationId, @Nullable AnimationStageInfo currentStage, @Nullable Integer stageNumber, long stageStartTick) {
        if (world == null || actorUuids == null || actorUuids.isEmpty()) {
            return 0;
        }
        boolean manualPeak = NeedsOfNature.isManualPeakAnimation(animationId);
        boolean fillBottle = NeedsOfNature.isFillBottleAnimation(animationId);
        int actualAddedMl = 0;
        if (!fillBottle) {
            NeedsOfNature.applyEnergyOnPeak(world, actorUuids, manualPeak);
        }
        if (!manualPeak && !fillBottle) {
            actualAddedMl = NeedsOfNature.startPeakLiquidProgression(world, instanceId, actorUuids, actorKeys, animationId, currentStage, stageNumber, stageStartTick);
            NeedsOfNature.fillHorseLiquidCollectorsOnPeak(world, actorUuids, actorKeys, animationId);
        }
        NeedsOfNature.trackPeakStats(world, actorUuids, manualPeak);
        if (!manualPeak && !fillBottle) {
            NeedsOfNature.maybeStartPregnancyOnPeak(world, actorUuids, actorKeys, animationId);
        }
        NeedsOfNature.applyInjectionAccessoryDurabilityCosts(world, actorUuids, animationId);
        NonMessSystem.incrementMessOnPeak(world, actorUuids, animationId);
        NeedsOfNature.scheduleMInjectorRewardsOnPeak(world, actorUuids, animationId);
        return actualAddedMl;
    }

    private static void applyInjectionAccessoryDurabilityCosts(ServerWorld world, List<UUID> actorUuids, Identifier animationId) {
        if (world == null || actorUuids == null || actorUuids.isEmpty() || animationId == null) {
            return;
        }
        NonLiquidRoles.LiquidRoles roles = NonLiquidRoles.getRoles(animationId);
        if (roles == null || roles.injectorRoles().isEmpty()) {
            return;
        }
        boolean hasV = roles.injectorRoles().containsValue((Object)NonLiquidRoles.InjectorRole.V);
        boolean hasA = roles.injectorRoles().containsValue((Object)NonLiquidRoles.InjectorRole.A);
        if (!hasV && !hasA) {
            return;
        }
        for (ServerPlayerEntity player : NeedsOfNature.collectPlayerActors(world, actorUuids)) {
            if (player == null || !player.isAlive() || player.isRemoved()) continue;
            if (hasV) {
                NonTrinketsIntegration.applyInjectionDurabilityCost(world, player, NonLiquidRoles.InjectorRole.V);
            }
            if (!hasA) continue;
            NonTrinketsIntegration.applyInjectionDurabilityCost(world, player, NonLiquidRoles.InjectorRole.A);
        }
    }

    private static void scheduleMInjectorRewardsOnPeak(ServerWorld world, List<UUID> actorUuids, Identifier animationId) {
        if (world == null || actorUuids == null || actorUuids.isEmpty()) {
            return;
        }
        if (!NeedsOfNature.hasMInjectorRole(animationId)) {
            return;
        }
        for (ServerPlayerEntity player : NeedsOfNature.collectPlayerActors(world, actorUuids)) {
            if (player == null || !player.isAlive() || player.isRemoved()) continue;
            PENDING_M_INJECTOR_REWARDS.put(player.getUuid(), new PendingMInjectorReward((RegistryKey<World>)world.getRegistryKey(), -1L));
            NonStats.addMInjectorPeaksMl(player, 8);
        }
    }

    private static boolean hasMInjectorRole(@Nullable Identifier animationId) {
        NonLiquidRoles.LiquidRoles roles = NonLiquidRoles.getRoles(animationId);
        if (roles == null || roles.injectorRoles().isEmpty()) {
            return false;
        }
        for (NonLiquidRoles.InjectorRole role : roles.injectorRoles().values()) {
            if (role != NonLiquidRoles.InjectorRole.M) continue;
            return true;
        }
        return false;
    }

    @Nullable
    private static AnimationStageInfo stageInfoForNumber(@Nullable List<AnimationStageInfo> stages, int stageNumber) {
        if (stages == null || stages.isEmpty()) {
            return null;
        }
        if (stageNumber <= 0) {
            return null;
        }
        int index = stageNumber - 1;
        if (index < 0 || index >= stages.size()) {
            return null;
        }
        return stages.get(index);
    }

    private static int startPeakLiquidProgression(ServerWorld world, UUID instanceId, List<UUID> actorUuids, List<String> actorKeys, Identifier animationId, @Nullable AnimationStageInfo currentStage, @Nullable Integer stageNumber, long stageStartTick) {
        if (world == null || instanceId == null || animationId == null) {
            return 0;
        }
        LiquidGainProfile gain = NeedsOfNature.resolveLiquidGainProfile(world, actorUuids, actorKeys, animationId);
        if (gain.amount() <= 0) {
            PEAK_LIQUID_PROGRESS.remove(instanceId);
            return 0;
        }
        Identifier stageClipId = NeedsOfNature.resolvePeakStageClipId(animationId, currentStage, stageNumber);
        List<Integer> cueOffsets = NeedsOfNature.resolveReactiveImpactCueOffsets(stageClipId);
        double cycleTicks = NeedsOfNature.resolveReactiveImpactCycleTicks(currentStage, stageClipId);
        cueOffsets = NeedsOfNature.resolveWarpedReactiveImpactCueOffsets(currentStage, cycleTicks, cueOffsets);
        double stageSpeed = NeedsOfNature.resolveStageSpeed(currentStage);
        if (cueOffsets.isEmpty() || cycleTicks <= 0.0) {
            PEAK_LIQUID_PROGRESS.remove(instanceId);
            return NeedsOfNature.grantLiquidPulseToReceivers(world, actorUuids, actorKeys, animationId, gain, gain.amount());
        }
        long stageDurationTicks = NeedsOfNature.resolveLoopAdvanceIntervalTicks(animationId, stageNumber, currentStage);
        if (stageDurationTicks < 0L) {
            PEAK_LIQUID_PROGRESS.put(instanceId, PeakLiquidProgressState.infinite((RegistryKey<World>)world.getRegistryKey(), animationId, List.copyOf(actorUuids), List.copyOf(actorKeys), gain, cycleTicks, List.copyOf(cueOffsets), stageStartTick, stageSpeed));
            return 0;
        }
        FinitePulseSchedule schedule = NeedsOfNature.buildFinitePulseSchedule(stageStartTick, stageDurationTicks, cycleTicks, cueOffsets, gain.amount(), stageSpeed);
        if (schedule == null || schedule.ticks().isEmpty()) {
            PEAK_LIQUID_PROGRESS.remove(instanceId);
            return NeedsOfNature.grantLiquidPulseToReceivers(world, actorUuids, actorKeys, animationId, gain, gain.amount());
        }
        PEAK_LIQUID_PROGRESS.put(instanceId, PeakLiquidProgressState.finite((RegistryKey<World>)world.getRegistryKey(), animationId, List.copyOf(actorUuids), List.copyOf(actorKeys), gain, schedule.ticks(), schedule.amounts()));
        return 0;
    }

    private static Identifier resolvePeakStageClipId(Identifier animationId, @Nullable AnimationStageInfo currentStage, @Nullable Integer stageNumber) {
        if (currentStage != null) {
            Identifier playback = currentStage.effectiveAnimationId();
            if (playback != null) {
                return playback;
            }
            if (currentStage.animationId() != null) {
                return currentStage.animationId();
            }
        }
        return NeedsOfNature.resolveStageId(animationId, stageNumber);
    }

    private static List<Integer> resolveReactiveImpactCueOffsets(@Nullable Identifier stageClipId) {
        NonReactiveImpactCues.CueProfile profile = NonReactiveImpactCues.getProfile(stageClipId);
        if (profile == null || profile.cueTicks() == null || profile.cueTicks().isEmpty()) {
            return List.of();
        }
        ArrayList<Integer> out = new ArrayList<Integer>();
        for (Integer tick : profile.cueTicks()) {
            if (tick == null || tick < 0) continue;
            out.add(tick);
        }
        if (out.isEmpty()) {
            return List.of();
        }
        out.sort(Integer::compareTo);
        return List.copyOf(out);
    }

    private static double resolveReactiveImpactCycleTicks(@Nullable AnimationStageInfo currentStage, @Nullable Identifier stageClipId) {
        double ticks;
        long cueCycleTicks;
        NonReactiveImpactCues.CueProfile profile = NonReactiveImpactCues.getProfile(stageClipId);
        if (profile != null && profile.cycleTicks() != null && (cueCycleTicks = Math.max(0L, (long)profile.cycleTicks().intValue())) > 0L) {
            return cueCycleTicks;
        }
        if (currentStage != null && (ticks = NeedsOfNature.exactCycleTicks(currentStage.animationId(), currentStage)) > 0.0) {
            return ticks;
        }
        return 0.0;
    }

    private static List<Integer> resolveWarpedReactiveImpactCueOffsets(@Nullable AnimationStageInfo currentStage, double cycleTicks, List<Integer> cueOffsets) {
        if (currentStage == null || cueOffsets == null || cueOffsets.isEmpty()) {
            return cueOffsets;
        }
        if (!currentStage.loop() || cycleTicks <= 0.0) {
            return cueOffsets;
        }
        double offsetTicks = AfwStageTimeWarp.offsetSecondsToTicks((double)currentStage.cycleMidpointOffsetSeconds());
        if (!AfwStageTimeWarp.hasWarp((double)cycleTicks, (double)offsetTicks)) {
            return cueOffsets;
        }
        TreeSet<Integer> warped = new TreeSet<Integer>();
        for (Integer cueOffset : cueOffsets) {
            if (cueOffset == null || cueOffset < 0) continue;
            double shifted = AfwStageTimeWarp.authoredToWarpedCycleTicks((double)cueOffset.intValue(), (double)cycleTicks, (double)offsetTicks);
            warped.add(Math.max(0, (int)Math.round(shifted)));
        }
        if (warped.isEmpty()) {
            return List.of();
        }
        return List.copyOf(warped);
    }

    private static double resolveStageSpeed(@Nullable AnimationStageInfo currentStage) {
        if (currentStage == null) {
            return 1.0;
        }
        double speed = currentStage.speed();
        if (!Double.isFinite(speed) || speed <= 0.0) {
            return 1.0;
        }
        return NeedsOfNature.clampAnimationSpeed(speed);
    }

    private static double clampAnimationSpeed(double speed) {
        if (!Double.isFinite(speed) || speed <= 0.0) {
            return 1.0;
        }
        return Math.max(0.1, Math.min(speed, 4.0));
    }

    private static long elapsedWorldTicksFromAnimationTicks(double animationTicks, double speed) {
        if (!Double.isFinite(animationTicks) || animationTicks <= 0.0) {
            return 0L;
        }
        double safeSpeed = !Double.isFinite(speed) || speed <= 0.0 ? 1.0 : speed;
        double elapsed = animationTicks / safeSpeed;
        if (!Double.isFinite(elapsed) || elapsed <= 0.0) {
            return 0L;
        }
        return Math.max(0L, (long)Math.ceil(elapsed - 1.0E-9));
    }

    @Nullable
    private static FinitePulseSchedule buildFinitePulseSchedule(long stageStartTick, long stageDurationTicks, double cycleTicks, List<Integer> cueOffsets, int totalAmountPerReceiver, double stageSpeed) {
        double firstAnimTick;
        long firstPulseTick;
        if (stageDurationTicks <= 0L || cycleTicks <= 0.0 || totalAmountPerReceiver <= 0) {
            return null;
        }
        if (cueOffsets == null || cueOffsets.isEmpty()) {
            return null;
        }
        long stageEndTick = stageStartTick + stageDurationTicks;
        ArrayList<Long> pulseTicks = new ArrayList<Long>();
        ArrayList<Integer> sortedOffsets = new ArrayList<Integer>();
        for (Integer rawOffset : cueOffsets) {
            if (rawOffset == null) continue;
            sortedOffsets.add(Math.max(0, rawOffset));
        }
        if (sortedOffsets.isEmpty()) {
            return null;
        }
        sortedOffsets.sort(Integer::compareTo);
        int minOffset = (Integer)sortedOffsets.getFirst();
        for (long cycleIndex = 0L; cycleIndex < 100000L && (firstPulseTick = stageStartTick + NeedsOfNature.elapsedWorldTicksFromAnimationTicks(firstAnimTick = (double)cycleIndex * cycleTicks + (double)minOffset, stageSpeed)) < stageEndTick; ++cycleIndex) {
            Iterator iterator = sortedOffsets.iterator();
            while (iterator.hasNext()) {
                int offset = (Integer)iterator.next();
                double cueAnimTick = (double)cycleIndex * cycleTicks + (double)offset;
                long pulseTick = stageStartTick + NeedsOfNature.elapsedWorldTicksFromAnimationTicks(cueAnimTick, stageSpeed);
                if (pulseTick >= stageEndTick) continue;
                pulseTicks.add(pulseTick);
            }
        }
        if (pulseTicks.isEmpty()) {
            return null;
        }
        pulseTicks.sort(Long::compareTo);
        int pulseCount = pulseTicks.size();
        int base = totalAmountPerReceiver / pulseCount;
        int remainder = totalAmountPerReceiver % pulseCount;
        ArrayList<Long> scheduledTicks = new ArrayList<Long>(pulseCount);
        ArrayList<Integer> scheduledAmounts = new ArrayList<Integer>(pulseCount);
        for (int i = 0; i < pulseCount; ++i) {
            int amount = base + (i < remainder ? 1 : 0);
            if (amount <= 0) continue;
            scheduledTicks.add((Long)pulseTicks.get(i));
            scheduledAmounts.add(amount);
        }
        if (scheduledTicks.isEmpty()) {
            return null;
        }
        return new FinitePulseSchedule(List.copyOf(scheduledTicks), List.copyOf(scheduledAmounts));
    }

    private static int grantLiquidPulseToReceivers(ServerWorld world, List<UUID> actorUuids, List<String> actorKeys, Identifier animationId, LiquidGainProfile baseGain, int amountPerReceiver) {
        if (world == null || actorUuids == null || actorUuids.isEmpty()) {
            return 0;
        }
        if (actorKeys == null || actorKeys.isEmpty()) {
            return 0;
        }
        if (actorKeys.size() != actorUuids.size()) {
            return 0;
        }
        if (animationId == null || baseGain == null || amountPerReceiver <= 0) {
            return 0;
        }
        NonLiquidRoles.LiquidRoles roles = NonLiquidRoles.getRoles(animationId);
        if (roles == null || roles.receiverKeys().isEmpty()) {
            return 0;
        }
        int totalAdded = 0;
        int count = Math.min(actorUuids.size(), actorKeys.size());
        for (int i = 0; i < count; ++i) {
            LiquidGainProfile pulseGain;
            LiquidGainProfile playerGain;
            int added;
            ServerPlayerEntity player;
            Entity entity;
            if (!roles.receiverKeys().contains(actorKeys.get(i)) || !((entity = world.getEntity(actorUuids.get(i))) instanceof ServerPlayerEntity) || !((player = (ServerPlayerEntity)entity) instanceof LiquidHolder)) continue;
            LiquidHolder holder = (LiquidHolder)player;
            LiquidGainProfile matchingGain = NeedsOfNature.resolveLiquidGainProfile(world, actorUuids, actorKeys, animationId, NonGenderSystem.getLiquidTankType((Entity)player));
            if (matchingGain.amount() <= 0 || (added = NeedsOfNature.addLiquidWithComposition(holder, playerGain = NeedsOfNature.applyApiLiquidGainModifier(player, NeedsOfNature.applyAccessoryLiquidGainMultiplier(player, pulseGain = NeedsOfNature.scaleLiquidGainProfile(matchingGain, baseGain.amount() <= 0 ? 1.0 : (double)amountPerReceiver / (double)baseGain.amount()))))) <= 0) continue;
            totalAdded += added;
            Map<Identifier, Integer> scaledAmounts = NeedsOfNature.scaleEntityAmounts(playerGain.entityAmounts(), added);
            NonStats.addLiquidGained(player, added, scaledAmounts);
            NeedsOfNature.trackFieldResearch(world, player, scaledAmounts);
            NeedsOfNature.syncLiquidState(player);
        }
        return totalAdded;
    }

    private static void addActualAddedMlToPeakState(UUID instanceId, int addedMl) {
        if (instanceId == null || addedMl <= 0) {
            return;
        }
        PeakState state = PEAK_STATES.get(instanceId);
        if (state == null) {
            return;
        }
        PEAK_STATES.put(instanceId, new PeakState(state.animationId(), state.peakStage(), state.peaked(), state.lastStage(), state.hasPlayer(), state.liquidGranted(), state.actualAddedMl() + addedMl));
    }

    private static void trackPeakStats(ServerWorld world, List<UUID> actorUuids, boolean manualPeak) {
        if (world == null || actorUuids == null || actorUuids.isEmpty()) {
            return;
        }
        List<ServerPlayerEntity> players = NeedsOfNature.collectPlayerActors(world, actorUuids);
        if (players.isEmpty()) {
            return;
        }
        Set<Identifier> nonPlayerEntityTypes = NeedsOfNature.collectNonPlayerEntityTypes(world, actorUuids);
        for (ServerPlayerEntity player : players) {
            NonStats.incrementPeakedAnimations(player, nonPlayerEntityTypes);
            NonAdvancementHooks.grantNaturalCuriosity(player);
            if (!manualPeak) continue;
            NonStats.incrementManualPeak(player);
        }
    }

    static List<ServerPlayerEntity> collectPlayerActors(ServerWorld world, List<UUID> actorUuids) {
        if (world == null || actorUuids == null || actorUuids.isEmpty()) {
            return List.of();
        }
        ArrayList<ServerPlayerEntity> players = new ArrayList<ServerPlayerEntity>();
        for (UUID uuid : actorUuids) {
            Entity entity = world.getEntity(uuid);
            if (!(entity instanceof ServerPlayerEntity)) continue;
            ServerPlayerEntity player = (ServerPlayerEntity)entity;
            players.add(player);
        }
        return players;
    }

    private static List<ServerPlayerEntity> collectPlayerActors(List<? extends Entity> actors) {
        if (actors == null || actors.isEmpty()) {
            return List.of();
        }
        ArrayList<ServerPlayerEntity> players = new ArrayList<ServerPlayerEntity>();
        for (Entity class_12972 : actors) {
            if (!(class_12972 instanceof ServerPlayerEntity)) continue;
            ServerPlayerEntity player = (ServerPlayerEntity)class_12972;
            players.add(player);
        }
        return players;
    }

    private static Set<Identifier> collectNonPlayerEntityTypes(ServerWorld world, List<UUID> actorUuids) {
        if (world == null || actorUuids == null || actorUuids.isEmpty()) {
            return Set.of();
        }
        LinkedHashSet<Identifier> out = new LinkedHashSet<Identifier>();
        for (UUID uuid : actorUuids) {
            Entity entity = world.getEntity(uuid);
            if (entity == null || entity instanceof ServerPlayerEntity) continue;
            Identifier entityId = Registries.ENTITY_TYPE.getId((Object)entity.getType());
            out.add(entityId);
        }
        return out;
    }

    private static void handleFillBottleStop(ServerWorld world, FillBottleState state, boolean peaked) {
        if (world == null || state == null) {
            return;
        }
        ServerPlayerEntity player = NeedsOfNature.findPlayerOnWorldServer(world, state.playerUuid());
        if (player == null || player.getEntityWorld() != world) {
            return;
        }
        if (!(player instanceof LiquidHolder)) {
            if (state.reservedBottle()) {
                NonItemSystem.refundGlassBottle(player);
            }
            return;
        }
        LiquidHolder holder = (LiquidHolder)player;
        if (!peaked) {
            if (state.reservedBottle()) {
                NonItemSystem.refundGlassBottle(player);
            }
            return;
        }
        int stored = holder.getLiquidStored();
        NeedsOfNature.updateFilledEffect(player, holder);
        int requiredMl = NeedsOfNature.getFillBottleRequiredMl(holder);
        if (stored < requiredMl || !NeedsOfNature.hasFilledStageOneOrHigher(player)) {
            if (state.reservedBottle()) {
                NonItemSystem.refundGlassBottle(player);
            }
            return;
        }
        ItemStack resultBottle = NonItemSystem.createBottleStackForTank(holder);
        holder.setLiquidStored(stored - requiredMl);
        NeedsOfNature.syncLiquidState(player);
        NonItemSystem.giveItemOrDrop(player, resultBottle);
        NonStats.incrementFillBottle(player);
        NonAdvancementHooks.checkCollectorsHabit(player);
    }

    private static int getFillBottleRequiredMl(LiquidHolder holder) {
        if (holder == null) {
            return Integer.MAX_VALUE;
        }
        return NeedsOfNature.getFilledStageOneRequiredMl(holder.getLiquidCapacity());
    }

    private static int getFilledStageOneRequiredMl(int capacity) {
        if (capacity <= 0) {
            return Integer.MAX_VALUE;
        }
        return Math.max(1, (int)Math.ceil((float)capacity * 0.3f));
    }

    private static boolean hasFilledStageOneOrHigher(ServerPlayerEntity player) {
        if (player == null) {
            return false;
        }
        RegistryEntry entry = Registries.STATUS_EFFECT.getEntry((Object)NonStatusEffects.FILLED);
        StatusEffectInstance effect = player.getStatusEffect(entry);
        return effect != null && effect.getAmplifier() >= 0;
    }

    private static int addLiquidWithComposition(LiquidHolder holder, LiquidGainProfile gain) {
        if (holder == null || gain == null || gain.amount() <= 0) {
            return 0;
        }
        int stored = holder.getLiquidStored();
        int capacity = holder.getLiquidCapacity();
        int room = Math.max(0, capacity - stored);
        int added = Math.min(room, gain.amount());
        if (added == 0) {
            return 0;
        }
        if (stored <= 0) {
            if (gain.composition() == LiquidHolder.LiquidComposition.ENTITY && gain.entityTypeId() != null) {
                holder.setLiquidCompositionEntity(gain.entityTypeId());
            } else {
                holder.setLiquidCompositionMixed();
            }
        } else {
            LiquidHolder.LiquidComposition existing = holder.getLiquidComposition();
            if (existing == LiquidHolder.LiquidComposition.ENTITY) {
                boolean sameEntity;
                Identifier existingType = holder.getLiquidEntityTypeId();
                boolean bl = sameEntity = gain.composition() == LiquidHolder.LiquidComposition.ENTITY && gain.entityTypeId() != null && gain.entityTypeId().equals((Object)existingType);
                if (!sameEntity) {
                    holder.setLiquidCompositionMixed();
                }
            } else if (existing != LiquidHolder.LiquidComposition.MIXED) {
                holder.setLiquidCompositionMixed();
            }
        }
        holder.setLiquidStored(stored + added);
        return added;
    }

    private static Map<Identifier, Integer> scaleEntityAmounts(Map<Identifier, Integer> source, int targetTotal) {
        if (source == null || source.isEmpty() || targetTotal <= 0) {
            return Map.of();
        }
        int sourceTotal = 0;
        for (Integer value : source.values()) {
            if (value == null || value <= 0) continue;
            sourceTotal += value.intValue();
        }
        if (sourceTotal <= 0) {
            return Map.of();
        }
        if (targetTotal >= sourceTotal) {
            return Map.copyOf(source);
        }
        LinkedHashMap<Identifier, Integer> scaled = new LinkedHashMap<Identifier, Integer>();
        ArrayList<Map.Entry<Identifier, Double>> remainders = new ArrayList<Map.Entry<Identifier, Double>>();
        int assigned = 0;
        for (Map.Entry<Identifier, Integer> entry : source.entrySet()) {
            int amount;
            if (entry.getKey() == null || (amount = entry.getValue() == null ? 0 : entry.getValue()) <= 0) continue;
            double exact = (double)amount * (double)targetTotal / (double)sourceTotal;
            int floor = (int)Math.floor(exact);
            if (floor > 0) {
                scaled.put(entry.getKey(), floor);
                assigned += floor;
            }
            remainders.add(Map.entry(entry.getKey(), exact - (double)floor));
        }
        int remaining = targetTotal - assigned;
        if (remaining <= 0) {
            return Map.copyOf(scaled);
        }
        remainders.sort((a, b) -> Double.compare((Double)b.getValue(), (Double)a.getValue()));
        for (int i = 0; i < remainders.size() && remaining > 0; --remaining, ++i) {
            Identifier entityId = (Identifier)((Map.Entry)remainders.get(i)).getKey();
            scaled.merge(entityId, 1, Integer::sum);
        }
        return Map.copyOf(scaled);
    }

    public static NonConfig getConfig() {
        return CONFIG;
    }

    public static boolean canMobAttackPlayer(ServerPlayerEntity player) {
        return NonAttackSystem.canMobAttackPlayer(player);
    }

    public static Map<String, String> buildModeMetadata(boolean attack) {
        return Map.of(NON_MODE_META_KEY, attack ? NON_MODE_ATTACK : NON_MODE_VOLUNTARY);
    }

    public static void setAnimationStartProtection(ServerPlayerEntity player, int ticks) {
        NonAttackSystem.setAnimationStartProtection(player, ticks);
    }

    public static boolean hasAnimationStartProtection(ServerWorld world, ServerPlayerEntity player) {
        return NonAttackSystem.hasAnimationStartProtection(world, player);
    }

    public static boolean isActorPendingOrActive(ServerWorld world, UUID actorUuid) {
        if (world == null || actorUuid == null) {
            return false;
        }
        if (NeedsOfNature.isNoNGatheringActor(world, actorUuid)) {
            return true;
        }
        return AfwAnimationApi.isActorPendingOrActive((ServerWorld)world, (UUID)actorUuid);
    }

    public static boolean isNoNGatheringActor(ServerWorld world, UUID actorUuid) {
        if (world == null || actorUuid == null) {
            return false;
        }
        NoNGatherActorState nonGatherState = NON_PENDING_GATHER_ACTORS.get(actorUuid);
        if (nonGatherState != null) {
            if (world.getRegistryKey().equals(nonGatherState.worldKey()) && nonGatherState.expiresTick() > world.getTime() && NonGatherSystem.isActorGathering(actorUuid)) {
                return true;
            }
            NON_PENDING_GATHER_ACTORS.remove(actorUuid, nonGatherState);
        }
        return false;
    }

    public static boolean isMobAttackFailsafeActive(ServerWorld world, UUID mobUuid) {
        return NonAttackSystem.isMobAttackFailsafeActive(world, mobUuid);
    }

    public static List<String> getActivePlayerCooldownDebugLines(ServerWorld world, ServerPlayerEntity player) {
        PendingMInjectorReward pendingReward;
        long remaining;
        Long pregnancyRetry;
        Long postEscapeDamageInvulnerableUntil;
        ArrayList<String> lines = new ArrayList<String>();
        if (world == null || player == null) {
            return lines;
        }
        long nowTick = NeedsOfNature.getGlobalTick(world);
        Long lastDefeatedUntil = LAST_DEFEATED_COOLDOWN_UNTIL_BY_PLAYER.get(player.getUuid());
        if (lastDefeatedUntil != null) {
            long remaining2 = lastDefeatedUntil - nowTick;
            if (remaining2 > 0L) {
                lines.add("last defeated: " + remaining2 + "t/" + NeedsOfNature.formatCooldownSeconds(remaining2) + "s");
            } else {
                LAST_DEFEATED_COOLDOWN_UNTIL_BY_PLAYER.remove(player.getUuid(), lastDefeatedUntil);
            }
        }
        if ((postEscapeDamageInvulnerableUntil = POST_ESCAPE_DAMAGE_INVULNERABLE_UNTIL_BY_PLAYER.get(player.getUuid())) != null) {
            long remaining3 = postEscapeDamageInvulnerableUntil - nowTick;
            if (remaining3 > 0L) {
                lines.add("post-escape damage invulnerability: " + remaining3 + "t/" + NeedsOfNature.formatCooldownSeconds(remaining3) + "s");
            } else {
                POST_ESCAPE_DAMAGE_INVULNERABLE_UNTIL_BY_PLAYER.remove(player.getUuid(), postEscapeDamageInvulnerableUntil);
            }
        }
        if (AfwAnimationApi.isActorPendingOrActive((ServerWorld)world, (UUID)player.getUuid())) {
            lines.add("animation active/pending");
        }
        if (PREGNANCY_QUEUED_PLAYERS.contains(player.getUuid())) {
            lines.add("pregnancy queued");
        }
        if ((pregnancyRetry = PREGNANCY_START_RETRY_TICKS.get(player.getUuid())) != null && (remaining = pregnancyRetry - nowTick) > 0L) {
            lines.add("pregnancy retry: " + remaining + "t/" + NeedsOfNature.formatCooldownSeconds(remaining) + "s");
        }
        if ((pendingReward = PENDING_M_INJECTOR_REWARDS.get(player.getUuid())) != null) {
            lines.add((String)(pendingReward.freeSinceTick() < 0L ? "M reward pending: waiting for animation end" : "M reward pending: free for " + Math.max(0L, nowTick - pendingReward.freeSinceTick()) + "t"));
        }
        return lines;
    }

    private static String formatCooldownSeconds(long ticks) {
        return String.format(Locale.ROOT, "%.1f", (double)ticks / 20.0);
    }

    static int clearAttackOutcomeFailsafeEntries() {
        return NonAttackSystem.clearAttackOutcomeFailsafeEntries();
    }

    public static String getMobAttackFailsafeDebug(ServerWorld world, UUID mobUuid) {
        return NonAttackSystem.getMobAttackFailsafeDebug(world, mobUuid);
    }

    public static String formatTicksForDebug(long ticks) {
        long safeTicks = Math.max(0L, ticks);
        double seconds = (double)safeTicks / 20.0;
        return safeTicks + "t/" + String.format(Locale.ROOT, "%.1fs", seconds);
    }

    public static boolean handleAnimationBreedingAttempt(ServerWorld world, AnimalEntity first, AnimalEntity second) {
        if (world == null || first == null || second == null) {
            return false;
        }
        NonConfig config = NeedsOfNature.getConfig();
        if (config == null || !config.useAnimationBreeding()) {
            return false;
        }
        if (first == second) {
            return true;
        }
        if (!first.isAlive() || !second.isAlive() || first.isRemoved() || second.isRemoved()) {
            return true;
        }
        BreedingPairKey pairKey = NeedsOfNature.breedingPairKey(first.getUuid(), second.getUuid());
        if (pairKey == null) {
            return true;
        }
        if (BREEDING_PENDING_BY_PAIR.containsKey(pairKey)) {
            return true;
        }
        if (BREEDING_ACTIVE_INSTANCE_BY_PAIR.containsKey(pairKey)) {
            return true;
        }
        if (config.requireMaleFemaleForBreeding() && !NeedsOfNature.isMaleFemalePair((Entity)first, (Entity)second)) {
            return true;
        }
        ArrayList<Object> actors = new ArrayList<Object>(2);
        actors.add(first);
        actors.add(second);
        actors.sort(Comparator.comparingInt(Entity::getId));
        AfwMatchedAnimation match = NeedsOfNature.findAnimationForActors(world, actors, Set.of(), null, false, null, Set.of());
        if (match == null) {
            return false;
        }
        List<String> actorKeys = match.actorKeys();
        if (actorKeys == null || actorKeys.size() != actors.size()) {
            return false;
        }
        List<AnimationStageInfo> stages = match.stages();
        String requestId = UUID.randomUUID().toString();
        long expiresTick = world.getTime() + 600L;
        PendingBreedingState pending = new PendingBreedingState((RegistryKey<World>)world.getRegistryKey(), pairKey.firstUuid(), pairKey.secondUuid(), expiresTick);
        BREEDING_PENDING_BY_PAIR.put(pairKey, pending);
        BREEDING_PENDING_BY_REQUEST.put(requestId, pairKey);
        Map<String, String> metadata = Map.of(NON_BREEDING_REQUEST_ID_META_KEY, requestId);
        UUID started = NeedsOfNature.startAnimationNowWithMetadata(world, match.animationId(), actors, actorKeys, stages, AfwDamageBehavior.STOP_ON_DAMAGE, false, null, metadata);
        if (started != null) {
            NeedsOfNature.adoptPendingBreedingRequest(world, started, metadata);
        }
        return true;
    }

    @Nullable
    private static BreedingPairKey breedingPairKey(UUID firstUuid, UUID secondUuid) {
        if (firstUuid == null || secondUuid == null) {
            return null;
        }
        if (firstUuid.equals(secondUuid)) {
            return null;
        }
        return firstUuid.compareTo(secondUuid) <= 0 ? new BreedingPairKey(firstUuid, secondUuid) : new BreedingPairKey(secondUuid, firstUuid);
    }

    private static boolean isMaleFemalePair(Entity first, Entity second) {
        if (first == null || second == null) {
            return false;
        }
        boolean firstCanBeMale = NonGenderSystem.hasMaleTag(first);
        boolean firstCanBeFemale = NonGenderSystem.hasFemaleTag(first);
        boolean secondCanBeMale = NonGenderSystem.hasMaleTag(second);
        boolean secondCanBeFemale = NonGenderSystem.hasFemaleTag(second);
        return firstCanBeMale && secondCanBeFemale || firstCanBeFemale && secondCanBeMale;
    }

    private static void adoptPendingBreedingRequest(ServerWorld world, UUID instanceId, @Nullable Map<String, String> metadata) {
        if (world == null || instanceId == null || metadata == null || metadata.isEmpty()) {
            return;
        }
        String requestId = metadata.get(NON_BREEDING_REQUEST_ID_META_KEY);
        if (requestId == null || requestId.isBlank()) {
            return;
        }
        BreedingPairKey pairKey = BREEDING_PENDING_BY_REQUEST.remove(requestId);
        if (pairKey == null) {
            return;
        }
        PendingBreedingState pending = BREEDING_PENDING_BY_PAIR.remove(pairKey);
        if (pending == null) {
            return;
        }
        if (!world.getRegistryKey().equals(pending.worldKey())) {
            return;
        }
        ActiveBreedingState active = new ActiveBreedingState(pending.worldKey(), pending.firstUuid(), pending.secondUuid());
        BREEDING_ACTIVE_BY_INSTANCE.put(instanceId, active);
        BREEDING_ACTIVE_INSTANCE_BY_PAIR.put(pairKey, instanceId);
    }

    private static void adoptPendingPregnancyHatch(ServerWorld world, UUID instanceId, List<UUID> actorUuids, @Nullable Map<String, String> metadata) {
        String birthMode;
        if (world == null || instanceId == null || actorUuids == null || metadata == null || metadata.isEmpty()) {
            return;
        }
        String mode = metadata.get(NON_MODE_META_KEY);
        if (!NON_MODE_PREGNANCY.equalsIgnoreCase(mode)) {
            return;
        }
        ServerPlayerEntity player = null;
        for (UUID actorUuid : actorUuids) {
            ServerPlayerEntity p;
            Entity entity = world.getEntity(actorUuid);
            if (!(entity instanceof ServerPlayerEntity)) continue;
            player = p = (ServerPlayerEntity)entity;
            break;
        }
        if (player == null) {
            return;
        }
        Identifier entityTypeId = Identifier.tryParse((String)metadata.getOrDefault(NON_PREGNANCY_ENTITY_META_KEY, ""));
        if (entityTypeId == null) {
            return;
        }
        NonEntityProfiles.ResolvedProfile profile = NonEntityProfiles.resolve(entityTypeId);
        Identifier birthEntityTypeId = Identifier.tryParse((String)metadata.getOrDefault(NON_PREGNANCY_BIRTH_ENTITY_META_KEY, ""));
        if (birthEntityTypeId == null) {
            birthEntityTypeId = profile.birthEntityTypeId();
        }
        if ((birthMode = NonConfig.normalizeBirthMode(metadata.getOrDefault(NON_PREGNANCY_BIRTH_MODE_META_KEY, ""))) == null) {
            birthMode = profile.birthMode();
        }
        int spawnCount = 1;
        String rawSpawnCount = metadata.get(NON_PREGNANCY_OFFSPRING_COUNT_META_KEY);
        if (rawSpawnCount != null && !rawSpawnCount.isBlank()) {
            try {
                spawnCount = Math.max(1, Math.min(16, Integer.parseInt(rawSpawnCount.trim())));
            }
            catch (NumberFormatException ignored) {
                spawnCount = Math.max(1, spawnCount);
            }
        }
        PregnancyVariantData variantData = NeedsOfNature.decodePregnancyVariantData(metadata.getOrDefault(NON_PREGNANCY_VARIANT_META_KEY, ""));
        PREGNANCY_HATCH_BY_INSTANCE.put(instanceId, new PregnancyHatchState(player.getUuid(), entityTypeId, birthEntityTypeId, birthMode, spawnCount, spawnCount, 0, variantData));
        PREGNANCY_ACTIVE_INSTANCE_BY_PLAYER.put(player.getUuid(), instanceId);
        PREGNANCY_QUEUED_PLAYERS.remove(player.getUuid());
        PREGNANCY_START_RETRY_TICKS.remove(player.getUuid());
    }

    private static void finalizeAnimationBreeding(ServerWorld world, UUID instanceId, boolean peaked) {
        AnimalEntity first;
        Entity secondEntity;
        block12: {
            block11: {
                if (world == null || instanceId == null) {
                    return;
                }
                ActiveBreedingState active = BREEDING_ACTIVE_BY_INSTANCE.remove(instanceId);
                if (active == null) {
                    return;
                }
                BreedingPairKey pairKey = NeedsOfNature.breedingPairKey(active.firstUuid(), active.secondUuid());
                if (pairKey != null) {
                    BREEDING_ACTIVE_INSTANCE_BY_PAIR.remove(pairKey, instanceId);
                }
                if (!peaked) {
                    return;
                }
                if (!world.getRegistryKey().equals(active.worldKey())) {
                    return;
                }
                Entity firstEntity = world.getEntity(active.firstUuid());
                secondEntity = world.getEntity(active.secondUuid());
                if (!(firstEntity instanceof AnimalEntity)) break block11;
                first = (AnimalEntity)firstEntity;
                if (secondEntity instanceof AnimalEntity) break block12;
            }
            return;
        }
        AnimalEntity second = (AnimalEntity)secondEntity;
        if (!first.isAlive() || !second.isAlive() || first.isRemoved() || second.isRemoved()) {
            return;
        }
        if (first.getEntityWorld() != world || second.getEntityWorld() != world) {
            return;
        }
        PassiveEntity baby = first.createChild(world, (PassiveEntity)second);
        if (baby == null) {
            return;
        }
        baby.setBaby(true);
        baby.refreshPositionAndAngles(first.getX(), first.getY(), first.getZ(), 0.0f, 0.0f);
        first.breed(world, second, baby);
        world.spawnEntityAndPassengers((Entity)baby);
    }

    private static void clearPendingBreedingForActors(ServerWorld world, List<UUID> actorUuids) {
        if (world == null || actorUuids == null || actorUuids.isEmpty() || BREEDING_PENDING_BY_PAIR.isEmpty()) {
            return;
        }
        RegistryKey worldKey = world.getRegistryKey();
        HashSet<UUID> actorSet = new HashSet<UUID>(actorUuids);
        HashSet removedPairs = new HashSet();
        BREEDING_PENDING_BY_PAIR.entrySet().removeIf(entry -> {
            boolean matchesActors;
            PendingBreedingState state = (PendingBreedingState)entry.getValue();
            BreedingPairKey key = (BreedingPairKey)entry.getKey();
            if (state == null || key == null) {
                return true;
            }
            if (!worldKey.equals(state.worldKey())) {
                return false;
            }
            boolean bl = matchesActors = actorSet.contains(key.firstUuid()) && actorSet.contains(key.secondUuid());
            if (matchesActors) {
                removedPairs.add(key);
            }
            return matchesActors;
        });
        if (!removedPairs.isEmpty()) {
            BREEDING_PENDING_BY_REQUEST.entrySet().removeIf(entry -> removedPairs.contains(entry.getValue()));
        }
    }

    private static void prunePendingAnimationBreeding(ServerWorld world, long nowTick) {
        if (world == null || BREEDING_PENDING_BY_PAIR.isEmpty()) {
            return;
        }
        RegistryKey worldKey = world.getRegistryKey();
        HashSet removedPairs = new HashSet();
        BREEDING_PENDING_BY_PAIR.entrySet().removeIf(entry -> {
            PendingBreedingState state = (PendingBreedingState)entry.getValue();
            BreedingPairKey key = (BreedingPairKey)entry.getKey();
            if (state == null || key == null) {
                return true;
            }
            if (!worldKey.equals(state.worldKey())) {
                return false;
            }
            if (state.expiresTick() > nowTick) {
                return false;
            }
            removedPairs.add(key);
            return true;
        });
        if (!removedPairs.isEmpty()) {
            BREEDING_PENDING_BY_REQUEST.entrySet().removeIf(entry -> removedPairs.contains(entry.getValue()));
        }
    }

    private static void trackPendingGatherPairs(ServerWorld world, List<? extends Entity> actors) {
        if (world == null || actors == null || actors.isEmpty()) {
            return;
        }
        long expiresTick = world.getTime() + 6000L;
        RegistryKey worldKey = world.getRegistryKey();
        ArrayList<UUID> playerUuids = new ArrayList<UUID>();
        ArrayList<UUID> mobUuids = new ArrayList<UUID>();
        for (Entity class_12972 : actors) {
            if (class_12972 == null || class_12972.getEntityWorld() != world) continue;
            if (class_12972 instanceof ServerPlayerEntity) {
                playerUuids.add(class_12972.getUuid());
                continue;
            }
            if (!(class_12972 instanceof MobEntity)) continue;
            mobUuids.add(class_12972.getUuid());
        }
        if (playerUuids.isEmpty() || mobUuids.isEmpty()) {
            return;
        }
        for (UUID uUID : mobUuids) {
            for (UUID playerUuid : playerUuids) {
                PENDING_GATHER_PAIRS.put(new GatherPairKey(uUID, playerUuid), new GatherPairState((RegistryKey<World>)worldKey, expiresTick));
            }
        }
    }

    private static void clearPendingGatherPairsForActors(ServerWorld world, List<UUID> actorUuids) {
        if (world == null || actorUuids == null || actorUuids.isEmpty() || PENDING_GATHER_PAIRS.isEmpty()) {
            return;
        }
        RegistryKey worldKey = world.getRegistryKey();
        HashSet<UUID> actorSet = new HashSet<UUID>(actorUuids);
        PENDING_GATHER_PAIRS.entrySet().removeIf(entry -> {
            GatherPairState state = (GatherPairState)entry.getValue();
            GatherPairKey key = (GatherPairKey)entry.getKey();
            if (state == null || key == null) {
                return true;
            }
            if (!worldKey.equals(state.worldKey())) {
                return false;
            }
            return actorSet.contains(key.mobUuid()) && actorSet.contains(key.playerUuid());
        });
    }

    private static void prunePendingGatherPairs(ServerWorld world, long nowTick) {
        if (world == null || PENDING_GATHER_PAIRS.isEmpty()) {
            return;
        }
        RegistryKey worldKey = world.getRegistryKey();
        PENDING_GATHER_PAIRS.entrySet().removeIf(entry -> {
            GatherPairState state = (GatherPairState)entry.getValue();
            GatherPairKey key = (GatherPairKey)entry.getKey();
            if (state == null || key == null) {
                return true;
            }
            if (!worldKey.equals(state.worldKey())) {
                return false;
            }
            if (state.expiresTick() <= nowTick) {
                return true;
            }
            if (!NeedsOfNature.isActorPendingOrActive(world, key.mobUuid())) {
                return true;
            }
            return !NeedsOfNature.isActorPendingOrActive(world, key.playerUuid());
        });
        NON_PENDING_GATHER_ACTORS.entrySet().removeIf(entry -> {
            NoNGatherActorState state = (NoNGatherActorState)entry.getValue();
            if (state == null) {
                return true;
            }
            return worldKey.equals(state.worldKey()) && state.expiresTick() <= nowTick;
        });
    }

    static boolean isMobGatheringOrActiveWithPlayer(ServerWorld world, MobEntity mob, ServerPlayerEntity player) {
        if (world == null || mob == null || player == null) {
            return false;
        }
        UUID playerInstance = NeedsOfNature.getActivePlayerInstance(player);
        if (NeedsOfNature.instanceContainsActor(playerInstance, mob.getUuid())) {
            return true;
        }
        GatherPairKey key = new GatherPairKey(mob.getUuid(), player.getUuid());
        GatherPairState state = PENDING_GATHER_PAIRS.get(key);
        if (state == null) {
            return false;
        }
        if (!world.getRegistryKey().equals(state.worldKey()) || state.expiresTick() <= world.getTime()) {
            PENDING_GATHER_PAIRS.remove(key);
            return false;
        }
        if (!NeedsOfNature.isActorPendingOrActive(world, mob.getUuid()) || !NeedsOfNature.isActorPendingOrActive(world, player.getUuid())) {
            PENDING_GATHER_PAIRS.remove(key);
            return false;
        }
        return true;
    }

    public static AttackBlockReason getMobAttackBlockReason(ServerWorld world, MobEntity mob, ServerPlayerEntity player) {
        return NonAttackSystem.getMobAttackBlockReason(world, mob, player);
    }

    public static void onMobAttackBlocked(ServerWorld world, MobEntity mob, ServerPlayerEntity player, AttackBlockReason reason) {
        NonAttackSystem.onMobAttackBlocked(world, mob, player, reason);
    }

    private static boolean hasPlayerAndHostileActor(List<? extends Entity> actors) {
        return NonAttackSystem.hasPlayerAndHostileActor(actors);
    }

    private static void recordAttackOutcomeForMobs(ServerWorld world, List<UUID> actorUuids, String reason) {
        NonAttackSystem.recordAttackOutcomeForMobs(world, actorUuids, reason);
    }

    public static boolean enqueueAnimation(ServerWorld world, Identifier animationId, List<? extends Entity> actors, int insertIndex, AfwDamageBehavior damageBehavior, boolean ignoreAttackers, Map<String, String> metadata, ServerPlayerEntity requester) {
        if (world == null || animationId == null || actors == null || actors.isEmpty()) {
            return false;
        }
        boolean effectiveIgnoreAttackers = ignoreAttackers || NeedsOfNature.hasPlayerAndHostileActor(actors);
        Map<Object, Object> safeMetadata = metadata == null ? Map.of() : Map.copyOf(metadata);
        return AfwAnimationApi.enqueueAnimation((ServerWorld)world, (Identifier)animationId, actors, (int)insertIndex, (AfwDamageBehavior)damageBehavior, (boolean)effectiveIgnoreAttackers, safeMetadata, (ServerPlayerEntity)requester);
    }

    public static void clearQueuedAnimations(ServerWorld world, ServerPlayerEntity player) {
        if (world == null || player == null) {
            return;
        }
        AfwAnimationApi.clearQueuedAnimations((ServerWorld)world, (ServerPlayerEntity)player);
    }

    public static Map<String, String> getInstanceMetadata(ServerWorld world, UUID instanceId) {
        if (world == null || instanceId == null) {
            return Map.of();
        }
        return AfwAnimationApi.getInstanceMetadata((ServerWorld)world, (UUID)instanceId);
    }

    public static List<UUID> getInstanceActors(UUID instanceId) {
        if (instanceId == null) {
            return List.of();
        }
        List<UUID> actors = INSTANCE_ACTORS.get(instanceId);
        return actors == null ? List.of() : actors;
    }

    public static boolean isInstanceAttack(UUID instanceId) {
        if (instanceId == null) {
            return false;
        }
        InstanceControl control = INSTANCE_CONTROLS.get(instanceId);
        return control != null && !control.voluntary;
    }

    static boolean isVoluntaryManualPeakInstance(ServerWorld world, UUID instanceId) {
        if (world == null || instanceId == null) {
            return false;
        }
        Map<String, String> metadata = NeedsOfNature.getInstanceMetadata(world, instanceId);
        if (!NON_MODE_VOLUNTARY.equalsIgnoreCase(metadata.get(NON_MODE_META_KEY))) {
            return false;
        }
        InstanceControl control = INSTANCE_CONTROLS.get(instanceId);
        if (control != null && NeedsOfNature.isManualPeakAnimation(control.animationId())) {
            return true;
        }
        AnimationStageInfo stage = AfwAnimationApi.getCurrentStage((ServerWorld)world, (UUID)instanceId);
        if (stage == null || stage.animationId() == null) {
            return false;
        }
        return NeedsOfNature.isManualPeakAnimation(NonPeakStages.baseAnimationId(stage.animationId()));
    }

    public static void noteAttackJoin(ServerWorld world, List<? extends Entity> actors) {
        if (world == null || actors == null || actors.isEmpty()) {
            return;
        }
        HashSet<UUID> actorSet = new HashSet<UUID>();
        for (Entity class_12972 : actors) {
            if (class_12972 == null) continue;
            actorSet.add(class_12972.getUuid());
        }
        if (actorSet.isEmpty()) {
            return;
        }
        RegistryKey worldKey = world.getRegistryKey();
        long l = world.getTime() + 400L;
        ArrayList<UUID> stale = new ArrayList<UUID>();
        for (Map.Entry<UUID, AttackJoinCarryover> entry : ATTACK_JOIN_CARRYOVER.entrySet()) {
            AttackJoinCarryover carry = entry.getValue();
            if (carry == null || carry.expiresTick <= world.getTime()) {
                stale.add(entry.getKey());
                continue;
            }
            if (!carry.worldKey.equals((Object)worldKey) || !carry.actors.equals(actorSet)) continue;
            stale.add(entry.getKey());
        }
        for (UUID id : stale) {
            ATTACK_JOIN_CARRYOVER.remove(id);
        }
        ATTACK_JOIN_CARRYOVER.put(UUID.randomUUID(), new AttackJoinCarryover((RegistryKey<World>)worldKey, Set.copyOf(actorSet), l));
    }

    private static boolean consumeAttackJoinCarryover(RegistryKey<World> worldKey, List<UUID> actorUuids, long nowTick) {
        if (worldKey == null || actorUuids == null || actorUuids.isEmpty()) {
            return false;
        }
        if (ATTACK_JOIN_CARRYOVER.isEmpty()) {
            return false;
        }
        Set<UUID> actorSet = Set.copyOf(actorUuids);
        ArrayList<UUID> stale = new ArrayList<UUID>();
        for (Map.Entry<UUID, AttackJoinCarryover> entry : ATTACK_JOIN_CARRYOVER.entrySet()) {
            AttackJoinCarryover carry = entry.getValue();
            if (carry == null || carry.expiresTick <= nowTick) {
                stale.add(entry.getKey());
                continue;
            }
            if (!carry.worldKey.equals(worldKey) || !carry.actors.equals(actorSet)) continue;
            ATTACK_JOIN_CARRYOVER.remove(entry.getKey());
            return true;
        }
        for (UUID id : stale) {
            ATTACK_JOIN_CARRYOVER.remove(id);
        }
        return false;
    }

    public static void sendDebugChat(ServerPlayerEntity player, String message) {
        NeedsOfNature.sendDebugChat(player, NonDebugChatCategory.INFO, message);
    }

    public static void sendDebugChat(ServerPlayerEntity player, NonDebugChatCategory category, String message) {
        NeedsOfNature.sendDebugChat(player, category, (Text)Text.literal((String)message));
    }

    public static void sendDebugChat(ServerPlayerEntity player, NonDebugChatCategory category, Text message) {
        if (player == null) {
            return;
        }
        if (!CONFIG.allowsDebugChat(category)) {
            return;
        }
        player.sendMessage((Text)Text.translatable((String)"debug.needsofnature.prefix", (Object[])new Object[]{message}).formatted(NeedsOfNature.chatColor(category)), false);
    }

    public static void sendDebugChatToNearby(ServerWorld world, Entity source, double radius, String message) {
        NeedsOfNature.sendDebugChatToNearby(world, source, radius, NonDebugChatCategory.INFO, message);
    }

    public static void sendDebugChatToNearby(ServerWorld world, Entity source, double radius, NonDebugChatCategory category, String message) {
        NeedsOfNature.sendDebugChatToNearby(world, source, radius, category, (Text)Text.literal((String)message));
    }

    public static void sendDebugChatToNearby(ServerWorld world, Entity source, double radius, NonDebugChatCategory category, Text message) {
        if (world == null || source == null) {
            return;
        }
        if (!CONFIG.allowsDebugChat(category)) {
            return;
        }
        double maxDistSq = radius * radius;
        for (ServerPlayerEntity p : world.getPlayers()) {
            if (!(p.squaredDistanceTo(source) <= maxDistSq)) continue;
            p.sendMessage((Text)Text.translatable((String)"debug.needsofnature.prefix", (Object[])new Object[]{message}).formatted(NeedsOfNature.chatColor(category)), false);
        }
    }

    public static void logSetupWarning(String template, Object ... args) {
        LOGGER.warn(template, args);
        NeedsOfNature.queueSetupChat(NeedsOfNature.formatLogTemplate(template, args));
    }

    public static void logSetupError(String template, Object ... args) {
        LOGGER.error(template, args);
        NeedsOfNature.queueSetupChat(NeedsOfNature.formatLogTemplate(template, args));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void queueSetupChat(String message) {
        if (message == null || message.isBlank()) {
            return;
        }
        List<PendingSetupChatMessage> list = PENDING_SETUP_CHAT;
        synchronized (list) {
            if (PENDING_SETUP_CHAT.size() >= 100) {
                PENDING_SETUP_CHAT.remove(0);
            }
            PENDING_SETUP_CHAT.add(new PendingSetupChatMessage((Text)Text.literal((String)message), new HashSet<UUID>()));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void flushPendingSetupChat(ServerWorld world) {
        if (world == null) {
            return;
        }
        List players = world.getPlayers();
        if (players.isEmpty()) {
            return;
        }
        List<PendingSetupChatMessage> list = PENDING_SETUP_CHAT;
        synchronized (list) {
            PENDING_SETUP_CHAT.removeIf(message -> {
                for (ServerPlayerEntity player : players) {
                    if (player == null || message.sentTo().contains(player.getUuid())) continue;
                    if (CONFIG.allowsDebugChat(NonDebugChatCategory.SETUP)) {
                        NeedsOfNature.sendDebugChat(player, NonDebugChatCategory.SETUP, message.message());
                    }
                    message.sentTo().add(player.getUuid());
                }
                return !message.sentTo().isEmpty() && message.sentTo().containsAll(players.stream().map(Entity::getUuid).toList());
            });
        }
    }

    public static String formatLogTemplate(String template, Object ... args) {
        if (template == null) {
            return "";
        }
        if (args == null || args.length == 0) {
            return template;
        }
        StringBuilder out = new StringBuilder(template.length() + args.length * 8);
        int argIndex = 0;
        for (int i = 0; i < template.length(); ++i) {
            char c = template.charAt(i);
            if (c == '{' && i + 1 < template.length() && template.charAt(i + 1) == '}' && argIndex < args.length) {
                Object arg;
                if (!((arg = args[argIndex++]) instanceof Throwable)) {
                    out.append(String.valueOf(arg));
                }
                ++i;
                continue;
            }
            out.append(c);
        }
        return out.toString();
    }

    private static Formatting chatColor(NonDebugChatCategory category) {
        if (category == null) {
            return Formatting.WHITE;
        }
        return switch (category) {
            default -> throw new MatchException(null, null);
            case NonDebugChatCategory.ALWAYS -> Formatting.DARK_GRAY;
            case NonDebugChatCategory.SETUP -> Formatting.YELLOW;
            case NonDebugChatCategory.WARNING -> Formatting.LIGHT_PURPLE;
            case NonDebugChatCategory.ERROR -> Formatting.RED;
            case NonDebugChatCategory.INFO -> Formatting.WHITE;
        };
    }

    public static boolean consumeScanBudget(ServerWorld world) {
        if (world == null) {
            return false;
        }
        RegistryKey key = world.getRegistryKey();
        long now = world.getTime();
        int configured = CONFIG.getScanBudgetPerTick();
        ScanBudget budget = SCAN_BUDGETS.get(key);
        if (budget == null || budget.tick != now) {
            budget = new ScanBudget(now, configured);
        }
        if (budget.remaining <= 0) {
            SCAN_BUDGETS.put((RegistryKey<World>)key, budget);
            return false;
        }
        SCAN_BUDGETS.put((RegistryKey<World>)key, new ScanBudget(now, budget.remaining - 1));
        return true;
    }

    private void registerLoopProgression() {
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            long now = world.getTime();
            NeedsOfNature.tickPregnancyCueRuntime(world, now);
            for (Map.Entry<UUID, LoopAdvance> entry : LOOP_ADVANCE.entrySet()) {
                UUID instanceId = entry.getKey();
                LoopAdvance progress = entry.getValue();
                if (progress.worldKey != world.getRegistryKey() || now < progress.nextTick) continue;
                AnimationStageInfo stage = AfwAnimationApi.getCurrentStage((ServerWorld)world, (UUID)instanceId);
                if (stage == null) {
                    LOOP_ADVANCE.remove(instanceId);
                    continue;
                }
                if (!stage.loop()) {
                    LOOP_ADVANCE.remove(instanceId);
                    continue;
                }
                boolean advanced = AfwAnimationApi.advanceStage((ServerWorld)world, (UUID)instanceId);
                if (advanced) continue;
                LOOP_ADVANCE.remove(instanceId);
            }
            NeedsOfNature.tickLiquidDecay(world);
            NeedsOfNature.tickPeakLiquidProgression(world, now);
            NeedsOfNature.tickReceiverDripParticles(world, now);
            NeedsOfNature.tickPregnancy(world);
            NeedsOfNature.tickMInjectorRewards(world, now);
            NeedsOfNature.tickPendingAttackEscapeResolutions(world, now);
            NeedsOfNature.tickPostDeathStateSync(world, now);
            NonGatherSystem.tickServerWorld(world);
            NonAccessoryShedSystem.tickServerWorld(world);
            NonMessSystem.tickMessCleaning(world, now);
            NeedsOfNature.flushPendingSetupChat(world);
            if (now % 20L == 0L) {
                NeedsOfNature.prunePendingGatherPairs(world, now);
                NeedsOfNature.prunePendingAnimationBreeding(world, now);
                NeedsOfNature.pruneAttackOutcomeFailsafe(world, now);
                NeedsOfNature.pruneAttackBlockDebug(world, now);
            }
        });
    }

    private static void tickMInjectorRewards(ServerWorld world, long now) {
        if (world == null || PENDING_M_INJECTOR_REWARDS.isEmpty()) {
            return;
        }
        RegistryKey worldKey = world.getRegistryKey();
        for (Map.Entry<UUID, PendingMInjectorReward> entry : PENDING_M_INJECTOR_REWARDS.entrySet()) {
            UUID playerUuid = entry.getKey();
            PendingMInjectorReward reward = entry.getValue();
            if (playerUuid == null || reward == null) {
                if (playerUuid == null) continue;
                PENDING_M_INJECTOR_REWARDS.remove(playerUuid);
                continue;
            }
            if (!worldKey.equals(reward.worldKey())) continue;
            ServerPlayerEntity player = world.getServer().getPlayerManager().getPlayer(playerUuid);
            if (player == null || player.getEntityWorld() != world || !player.isAlive() || player.isRemoved()) {
                PENDING_M_INJECTOR_REWARDS.remove(playerUuid, reward);
                continue;
            }
            if (AfwAnimationApi.isActorPendingOrActive((ServerWorld)world, (UUID)playerUuid)) {
                if (reward.freeSinceTick() < 0L) continue;
                PENDING_M_INJECTOR_REWARDS.put(playerUuid, new PendingMInjectorReward(reward.worldKey(), -1L));
                continue;
            }
            long freeSince = reward.freeSinceTick();
            if (freeSince < 0L) {
                PENDING_M_INJECTOR_REWARDS.put(playerUuid, new PendingMInjectorReward(reward.worldKey(), now));
                continue;
            }
            if (now - freeSince < 20L) continue;
            NeedsOfNature.applyMInjectorReward(world, player);
            PENDING_M_INJECTOR_REWARDS.remove(playerUuid, reward);
        }
    }

    private static void applyMInjectorReward(ServerWorld world, ServerPlayerEntity player) {
        if (world == null || player == null) {
            return;
        }
        int foodBefore = player.getHungerManager().getFoodLevel();
        player.getHungerManager().add(1, 0.0f);
        if (foodBefore < 20) {
            NeedsOfNature.grantEmergencySnackAdvancement(player);
        }
        world.playSound(null, player.getX(), player.getY() + (double)player.getStandingEyeHeight() * 0.5, player.getZ(), (RegistryEntry)SoundEvents.ITEM_HONEY_BOTTLE_DRINK, SoundCategory.PLAYERS, 1.0f, 0.95f + world.getRandom().nextFloat() * 0.1f);
    }

    private static void tickPeakLiquidProgression(ServerWorld world, long nowTick) {
        if (world == null || PEAK_LIQUID_PROGRESS.isEmpty()) {
            return;
        }
        RegistryKey worldKey = world.getRegistryKey();
        for (Map.Entry<UUID, PeakLiquidProgressState> entry : PEAK_LIQUID_PROGRESS.entrySet()) {
            int cueOffset;
            double cueAnimTick;
            long dueTick;
            UUID instanceId = entry.getKey();
            PeakLiquidProgressState state = entry.getValue();
            if (instanceId == null || state == null) {
                if (instanceId == null) continue;
                PEAK_LIQUID_PROGRESS.remove(instanceId);
                continue;
            }
            if (!worldKey.equals(state.worldKey)) continue;
            PeakState peakState = PEAK_STATES.get(instanceId);
            if (peakState == null || peakState.lastStage() != peakState.peakStage()) {
                PEAK_LIQUID_PROGRESS.remove(instanceId);
                continue;
            }
            if (!state.infinite) {
                while (state.scheduleIndex < state.scheduledTicks.size() && state.scheduledTicks.get(state.scheduleIndex) <= nowTick) {
                    int added;
                    int amount = state.scheduledAmounts.get(state.scheduleIndex);
                    ++state.scheduleIndex;
                    if (amount <= 0 || (added = NeedsOfNature.grantLiquidPulseToReceivers(world, state.actorUuids, state.actorKeys, state.animationId, state.gainProfile, amount)) <= 0) continue;
                    NeedsOfNature.addActualAddedMlToPeakState(instanceId, added);
                }
                if (state.scheduleIndex < state.scheduledTicks.size()) continue;
                PEAK_LIQUID_PROGRESS.remove(instanceId);
                continue;
            }
            if (state.remainingPerReceiverMl <= 0 || state.cycleTicks <= 0.0 || state.cueOffsets.isEmpty()) {
                PEAK_LIQUID_PROGRESS.remove(instanceId);
                continue;
            }
            for (int guard = 0; state.remainingPerReceiverMl > 0 && guard < 64 && nowTick >= (dueTick = state.stageStartTick + NeedsOfNature.elapsedWorldTicksFromAnimationTicks(cueAnimTick = (double)state.cycleIndex * state.cycleTicks + (double)(cueOffset = Math.max(0, state.cueOffsets.get(state.cueIndex))), state.stageSpeed)); ++guard) {
                int amount = Math.min(5, state.remainingPerReceiverMl);
                state.remainingPerReceiverMl -= amount;
                int added = NeedsOfNature.grantLiquidPulseToReceivers(world, state.actorUuids, state.actorKeys, state.animationId, state.gainProfile, amount);
                if (added > 0) {
                    NeedsOfNature.addActualAddedMlToPeakState(instanceId, added);
                }
                ++state.cueIndex;
                if (state.cueIndex < state.cueOffsets.size()) continue;
                state.cueIndex = 0;
                ++state.cycleIndex;
            }
            if (state.remainingPerReceiverMl > 0) continue;
            PEAK_LIQUID_PROGRESS.remove(instanceId);
        }
    }

    private static void calmMobsAfterPeakedPlayerAnimation(ServerWorld world, List<UUID> actorUuids) {
        Entity entity;
        if (world == null || actorUuids == null || actorUuids.isEmpty()) {
            return;
        }
        HashSet<UUID> playerUuids = new HashSet<UUID>();
        for (UUID actorUuid : actorUuids) {
            entity = world.getEntity(actorUuid);
            if (!(entity instanceof ServerPlayerEntity)) continue;
            ServerPlayerEntity player = (ServerPlayerEntity)entity;
            playerUuids.add(player.getUuid());
        }
        if (playerUuids.isEmpty()) {
            return;
        }
        for (UUID actorUuid : actorUuids) {
            UUID angryAtUuid;
            ServerPlayerEntity player;
            entity = world.getEntity(actorUuid);
            if (!(entity instanceof MobEntity)) continue;
            MobEntity mob = (MobEntity)entity;
            LivingEntity target = mob.getTarget();
            if (target instanceof ServerPlayerEntity && playerUuids.contains((player = (ServerPlayerEntity)target).getUuid())) {
                mob.setTarget(null);
            }
            if (!(mob instanceof Angerable)) continue;
            Angerable angerable = (Angerable)mob;
            LazyEntityReference angryAt = angerable.getAngryAt();
            UUID uUID = angryAtUuid = angryAt == null ? null : angryAt.getUuid();
            if (angryAtUuid != null && !playerUuids.contains(angryAtUuid)) continue;
            angerable.stopAnger();
        }
    }

    private static void pruneAttackOutcomeFailsafe(ServerWorld world, long nowTick) {
        NonAttackSystem.pruneAttackOutcomeFailsafe(world, nowTick);
    }

    private static void pruneAttackBlockDebug(ServerWorld world, long nowTick) {
        NonAttackSystem.pruneAttackBlockDebug(world, nowTick);
    }

    private void registerLiquidSync() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            NonGenderSystem.ensurePlayerGenderInitialized(handler.player);
            NonDestroyedSkinSystem.cacheDestroyedSkinStage(handler.player);
            NeedsOfNature.syncLiquidState(handler.player);
            NeedsOfNature.syncPregnancyState(handler.player);
            NeedsOfNature.syncRuntimeGameplaySettings(handler.player);
            NeedsOfNature.syncHostConfigState(handler.player);
            NeedsOfNature.syncDestroyedSkins(handler.player);
            NonMessSystem.syncMessStates(handler.player);
            NonAccessoryShedSystem.syncAllToViewer(handler.player);
        });
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            UUID playerUuid = handler.player.getUuid();
            FILL_BOTTLE_STATES.entrySet().removeIf(entry -> playerUuid.equals(((FillBottleState)entry.getValue()).playerUuid()));
            PENDING_GATHER_PAIRS.entrySet().removeIf(entry -> playerUuid.equals(((GatherPairKey)entry.getKey()).playerUuid()));
            LAST_DEFEATED_COOLDOWN_UNTIL_BY_PLAYER.remove(playerUuid);
            POST_ESCAPE_DAMAGE_INVULNERABLE_UNTIL_BY_PLAYER.remove(playerUuid);
            PENDING_ATTACK_ESCAPE_RESOLUTIONS.remove(playerUuid);
            PENDING_POST_DEATH_STATE_SYNCS.remove(playerUuid);
            UUID activeHatchInstance = PREGNANCY_ACTIVE_INSTANCE_BY_PLAYER.remove(playerUuid);
            if (activeHatchInstance != null) {
                PREGNANCY_HATCH_BY_INSTANCE.remove(activeHatchInstance);
                PREGNANCY_CUE_RUNTIME_BY_INSTANCE.remove(activeHatchInstance);
            }
            PREGNANCY_QUEUED_PLAYERS.remove(playerUuid);
            PREGNANCY_START_RETRY_TICKS.remove(playerUuid);
            NonTrinketsIntegration.clearAccessoryState(playerUuid);
            NonAccessoryShedSystem.clearServerShedState(handler.player.getEntityWorld(), playerUuid);
            NonAdvancementHooks.clearPlayerState(playerUuid);
        });
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity)entity;
                NonGenderSystem.ensurePlayerGenderInitialized(player);
                NonDestroyedSkinSystem.cacheDestroyedSkinStage(player);
                NeedsOfNature.syncLiquidState(player);
                NeedsOfNature.syncPregnancyState(player);
                NeedsOfNature.syncRuntimeGameplaySettings(player);
                NeedsOfNature.syncHostConfigState(player);
                NeedsOfNature.syncDestroyedSkins(player);
                NonMessSystem.syncMessStates(player);
            }
        });
    }

    public static void syncLiquidState(ServerPlayerEntity player) {
        NonLiquidSystem.syncLiquidState(player);
    }

    private static void syncPregnancyState(ServerPlayerEntity player) {
        if (player == null) {
            return;
        }
        Identifier entityTypeId = NeedsOfNature.getPregnantEntityTypeId(player);
        String raw = entityTypeId == null ? "" : entityTypeId.toString();
        ServerPlayNetworking.send((ServerPlayerEntity)player, (CustomPayload)new PregnancyStateS2CPayload(raw));
    }

    public static void syncRuntimeGameplaySettings(ServerPlayerEntity player) {
        if (player == null) {
            return;
        }
        NonConfig config = NeedsOfNature.getConfig();
        int attackHits = config.getAttackEscapeHits() + NonTrinketsIntegration.getAccessoryEffects((LivingEntity)player).attackEscapeHitsAdd();
        attackHits = Math.max(1, Math.min(50, attackHits));
        ServerPlayNetworking.send((ServerPlayerEntity)player, (CustomPayload)new GameplayRuntimeSettingsS2CPayload(config.getLoopProgressSeconds(), config.getPeakLoopProgressSeconds(), attackHits, config.getAttackDecayPerSecond(), config.getAttackEscapeDamageDifficultyPercent(), config.canAttackCreativePlayers()));
    }

    public static void syncRuntimeGameplaySettingsToAll(@Nullable MinecraftServer server) {
        if (server == null) {
            return;
        }
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            NeedsOfNature.syncRuntimeGameplaySettings(player);
        }
    }

    private static void syncHostConfigState(ServerPlayerEntity player) {
        if (player == null) {
            return;
        }
        NonConfig config = NeedsOfNature.getConfig();
        HostConfigSyncData data = HostConfigSyncData.fromConfig(config);
        data.genderSpawnByEntity = new LinkedHashMap<String, NonConfig.GenderSpawnChances>(NonGenderSystem.resolveEffectiveGenderSpawnMap());
        data.entityProfilesByEntity = new LinkedHashMap<String, NonConfig.EntityProfile>(NonEntityProfiles.resolveEffectiveProfilesMap());
        data.liquidGainByEntity = new LinkedHashMap<String, Integer>(NeedsOfNature.resolveEffectiveLiquidGainMap());
        data.liquidColorByEntity = new LinkedHashMap<String, String>(NeedsOfNature.resolveEffectiveLiquidColorMap());
        String json = HOST_CONFIG_SYNC_GSON.toJson((Object)data);
        ServerPlayNetworking.send((ServerPlayerEntity)player, (CustomPayload)new HostConfigSyncS2CPayload(json));
    }

    private static void syncDestroyedSkins(ServerPlayerEntity player) {
        NonDestroyedSkinSystem.syncDestroyedSkins(player);
    }

    private static boolean shouldApplyDestroyedSkinAttackDamage(boolean isAttack, @Nullable Map<String, String> metadata, @Nullable Identifier animationId) {
        boolean joinedFromDefeatedAnimation;
        if (!isAttack) {
            return false;
        }
        boolean joinReplace = metadata != null && Boolean.parseBoolean(metadata.getOrDefault(AFW_JOIN_REPLACE_META_KEY, "false"));
        boolean defeatedAnimation = NeedsOfNature.hasAnimationTagWithPrefix(animationId, AFW_ANIMATION_TAG_DEFEAT_PREFIX);
        boolean bl = joinedFromDefeatedAnimation = metadata != null && NeedsOfNature.hasAnimationTagWithPrefix(Identifier.tryParse((String)metadata.getOrDefault(AFW_JOIN_REPLACE_FROM_META_KEY, "")), AFW_ANIMATION_TAG_DEFEAT_PREFIX);
        if (joinReplace && (defeatedAnimation || joinedFromDefeatedAnimation)) {
            return true;
        }
        if (joinReplace) {
            return false;
        }
        return !defeatedAnimation;
    }

    public static void syncHostConfigStateToAll(@Nullable MinecraftServer server) {
        if (server == null) {
            return;
        }
        NeedsOfNature.clearDisabledSystemStoredStates(server);
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            NonDestroyedSkinSystem.cacheDestroyedSkinStage(player);
            NeedsOfNature.syncRuntimeGameplaySettings(player);
            NeedsOfNature.syncHostConfigState(player);
            NeedsOfNature.syncLiquidState(player);
            NeedsOfNature.syncDestroyedSkins(player);
            NonMessSystem.syncMessStates(player);
        }
    }

    private static void clearDisabledSystemStoredStates(MinecraftServer server) {
        if (server == null) {
            return;
        }
        boolean messEnabled = NeedsOfNature.isMessSystemEnabled();
        boolean destroyedSkinEnabled = NeedsOfNature.isDestroyedSkinSystemEnabled();
        if (messEnabled && destroyedSkinEnabled) {
            return;
        }
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            if (!messEnabled) {
                NonMessSystem.clearMessStoredState(player);
            }
            if (destroyedSkinEnabled) continue;
            NonDestroyedSkinSystem.clearDestroyedSkinStoredState(player);
        }
    }

    public static void syncLiquidStateToAll(@Nullable MinecraftServer server) {
        NonLiquidSystem.syncLiquidStateToAll(server);
    }

    public static void refreshLiquidBottleTintsToAll(@Nullable MinecraftServer server) {
        NonLiquidSystem.refreshLiquidBottleTintsToAll(server);
    }

    static int resolveLiquidTintRgb(LiquidHolder holder) {
        return NonLiquidSystem.resolveLiquidTintRgb(holder);
    }

    private static int grantLiquidOnPeak(ServerWorld world, List<UUID> actorUuids, List<String> actorKeys, Identifier animationId) {
        if (world == null || actorUuids == null || actorUuids.isEmpty()) {
            return 0;
        }
        if (actorKeys == null || actorKeys.isEmpty()) {
            return 0;
        }
        if (actorKeys.size() != actorUuids.size()) {
            return 0;
        }
        NonLiquidRoles.LiquidRoles roles = NonLiquidRoles.getRoles(animationId);
        if (roles == null) {
            return 0;
        }
        Set<String> receiverKeys = roles.receiverKeys();
        if (receiverKeys.isEmpty()) {
            return 0;
        }
        int count = Math.min(actorUuids.size(), actorKeys.size());
        int totalAdded = 0;
        for (int i = 0; i < count; ++i) {
            LiquidGainProfile playerGain;
            int added;
            ServerPlayerEntity player;
            Entity entity;
            if (!receiverKeys.contains(actorKeys.get(i)) || !((entity = world.getEntity(actorUuids.get(i))) instanceof ServerPlayerEntity) || !((player = (ServerPlayerEntity)entity) instanceof LiquidHolder)) continue;
            LiquidHolder holder = (LiquidHolder)player;
            LiquidGainProfile gain = NeedsOfNature.resolveLiquidGainProfile(world, actorUuids, actorKeys, animationId, NonGenderSystem.getLiquidTankType((Entity)player));
            if (gain.amount() <= 0 || (added = NeedsOfNature.addLiquidWithComposition(holder, playerGain = NeedsOfNature.applyApiLiquidGainModifier(player, NeedsOfNature.applyAccessoryLiquidGainMultiplier(player, gain)))) <= 0) continue;
            totalAdded += added;
            Map<Identifier, Integer> scaledAmounts = NeedsOfNature.scaleEntityAmounts(playerGain.entityAmounts(), added);
            NonStats.addLiquidGained(player, added, scaledAmounts);
            NeedsOfNature.trackFieldResearch(world, player, scaledAmounts);
            NeedsOfNature.syncLiquidState(player);
        }
        return totalAdded;
    }

    private static void fillHorseLiquidCollectorsOnPeak(ServerWorld world, List<UUID> actorUuids, List<String> actorKeys, Identifier animationId) {
        if (world == null || actorUuids == null || actorUuids.isEmpty()) {
            return;
        }
        if (actorKeys == null || actorKeys.isEmpty()) {
            return;
        }
        if (actorKeys.size() != actorUuids.size()) {
            return;
        }
        NonLiquidRoles.LiquidRoles roles = NonLiquidRoles.getRoles(animationId);
        if (roles == null) {
            return;
        }
        Set<String> receiverKeys = roles.receiverKeys();
        Set<String> injectorKeys = roles.injectorKeys();
        if (receiverKeys.isEmpty() || injectorKeys.isEmpty()) {
            return;
        }
        ArrayList<HorseLiquidCollectorEntity> collectors = new ArrayList<HorseLiquidCollectorEntity>();
        LinkedHashSet<Identifier> donorTypes = new LinkedHashSet<Identifier>();
        int count = Math.min(actorUuids.size(), actorKeys.size());
        for (int i = 0; i < count; ++i) {
            EntityType entityType;
            String actorKey = actorKeys.get(i);
            Entity entity = world.getEntity(actorUuids.get(i));
            if (entity == null) continue;
            if (receiverKeys.contains(actorKey) && entity instanceof HorseLiquidCollectorEntity) {
                HorseLiquidCollectorEntity collector = (HorseLiquidCollectorEntity)entity;
                collectors.add(collector);
            }
            if (!injectorKeys.contains(actorKey) || (entityType = entity.getType()) != EntityType.HORSE && entityType != EntityType.DONKEY && entityType != EntityType.MULE) continue;
            Identifier typeId = Registries.ENTITY_TYPE.getId((Object)entityType);
            donorTypes.add(typeId);
        }
        if (collectors.isEmpty() || donorTypes.isEmpty()) {
            return;
        }
        boolean mixedDonor = donorTypes.size() > 1;
        Identifier donorType = mixedDonor ? null : (Identifier)donorTypes.iterator().next();
        for (HorseLiquidCollectorEntity collector : collectors) {
            if (mixedDonor) {
                collector.absorbMixedLiquid();
                continue;
            }
            collector.absorbEntityLiquid(donorType);
        }
    }

    private static LiquidGainProfile resolveLiquidGainProfile(ServerWorld world, List<UUID> actorUuids, List<String> actorKeys, Identifier animationId) {
        return NeedsOfNature.resolveLiquidGainProfile(world, actorUuids, actorKeys, animationId, null);
    }

    private static LiquidGainProfile resolveLiquidGainProfile(ServerWorld world, List<UUID> actorUuids, List<String> actorKeys, Identifier animationId, @Nullable NonLiquidTankType tankType) {
        if (world == null || actorUuids == null || actorUuids.isEmpty()) {
            return new LiquidGainProfile(0, 0, LiquidHolder.LiquidComposition.EMPTY, null, Map.of());
        }
        if (actorKeys == null || actorKeys.isEmpty()) {
            return new LiquidGainProfile(0, 0, LiquidHolder.LiquidComposition.EMPTY, null, Map.of());
        }
        if (actorKeys.size() != actorUuids.size()) {
            return new LiquidGainProfile(0, 0, LiquidHolder.LiquidComposition.EMPTY, null, Map.of());
        }
        NonLiquidRoles.LiquidRoles roles = NonLiquidRoles.getRoles(animationId);
        if (roles == null) {
            return new LiquidGainProfile(0, 0, LiquidHolder.LiquidComposition.EMPTY, null, Map.of());
        }
        Map<String, NonLiquidRoles.InjectorRole> injectorRoles = roles.injectorRoles();
        if (injectorRoles.isEmpty()) {
            return new LiquidGainProfile(0, 0, LiquidHolder.LiquidComposition.EMPTY, null, Map.of());
        }
        double liquidGainMultiplier = NeedsOfNature.resolveLiquidGainMultiplier(animationId);
        int total = 0;
        int xpEligible = 0;
        LinkedHashSet<Identifier> injectorEntityTypes = new LinkedHashSet<Identifier>();
        LinkedHashMap<Identifier, Integer> entityAmounts = new LinkedHashMap<Identifier, Integer>();
        int count = Math.min(actorUuids.size(), actorKeys.size());
        for (int i = 0; i < count; ++i) {
            Identifier typeId;
            int gain;
            Entity entity;
            String actorKey = actorKeys.get(i);
            NonLiquidRoles.InjectorRole role = injectorRoles.get(actorKey);
            if (!NeedsOfNature.isLiquidInjectorAccepted(role, tankType) || (entity = world.getEntity(actorUuids.get(i))) == null || (gain = NeedsOfNature.applyLiquidGainMultiplier(NeedsOfNature.resolveEntityLiquidGainMl(typeId = Registries.ENTITY_TYPE.getId((Object)entity.getType())), liquidGainMultiplier)) <= 0) continue;
            total += gain;
            if (!PLAYER_ENTITY_TYPE_ID.equals((Object)typeId)) {
                xpEligible += gain;
            }
            injectorEntityTypes.add(typeId);
            entityAmounts.merge(typeId, gain, Integer::sum);
        }
        if (total <= 0) {
            return new LiquidGainProfile(0, 0, LiquidHolder.LiquidComposition.EMPTY, null, Map.of());
        }
        Map<Identifier, Integer> frozenEntityAmounts = Map.copyOf(entityAmounts);
        if (injectorEntityTypes.size() == 1) {
            return new LiquidGainProfile(total, xpEligible, LiquidHolder.LiquidComposition.ENTITY, (Identifier)injectorEntityTypes.iterator().next(), frozenEntityAmounts);
        }
        return new LiquidGainProfile(total, xpEligible, LiquidHolder.LiquidComposition.MIXED, null, frozenEntityAmounts);
    }

    public static boolean isProtectedByAccessory(ServerWorld world, AfwAnimationDefinitions.Definition definition, List<? extends Entity> actors) {
        if (world == null || definition == null || actors == null || actors.isEmpty()) {
            return false;
        }
        for (ServerPlayerEntity player : NeedsOfNature.collectPlayerActors(actors)) {
            for (NonLiquidRoles.InjectorRole role : NeedsOfNature.resolveEffectiveInjectorRolesForPlayer(definition.id(), player)) {
                if (!NonTrinketsIntegration.hasProtectorFor(player, role)) continue;
                return true;
            }
        }
        return false;
    }

    public static boolean consumeProtectorForAnimation(ServerWorld world, AfwAnimationDefinitions.Definition definition, List<? extends Entity> actors) {
        if (world == null || definition == null || actors == null || actors.isEmpty()) {
            return false;
        }
        for (ServerPlayerEntity player : NeedsOfNature.collectPlayerActors(actors)) {
            for (NonLiquidRoles.InjectorRole role : NeedsOfNature.resolveEffectiveInjectorRolesForPlayer(definition.id(), player)) {
                if (!NonTrinketsIntegration.consumeProtectorFor(world, player, role)) continue;
                return true;
            }
        }
        return false;
    }

    public static void trackNoNGatherPair(ServerWorld world, List<? extends Entity> actors) {
        NeedsOfNature.trackPendingGatherPairs(world, actors);
        NeedsOfNature.trackNoNGatherActors(world, actors);
    }

    public static void clearNoNGatherPair(ServerWorld world, List<UUID> actorUuids) {
        NeedsOfNature.clearPendingGatherPairsForActors(world, actorUuids);
        NeedsOfNature.clearNoNGatherActors(world, actorUuids);
    }

    private static void trackNoNGatherActors(ServerWorld world, List<? extends Entity> actors) {
        if (world == null || actors == null || actors.isEmpty()) {
            return;
        }
        long expiresTick = world.getTime() + 6000L;
        RegistryKey worldKey = world.getRegistryKey();
        for (Entity class_12972 : actors) {
            if (class_12972 == null || class_12972.getEntityWorld() != world) continue;
            NON_PENDING_GATHER_ACTORS.put(class_12972.getUuid(), new NoNGatherActorState((RegistryKey<World>)worldKey, expiresTick));
        }
    }

    private static void clearNoNGatherActors(ServerWorld world, List<UUID> actorUuids) {
        if (world == null || actorUuids == null || actorUuids.isEmpty()) {
            return;
        }
        RegistryKey worldKey = world.getRegistryKey();
        for (UUID actorUuid : actorUuids) {
            NoNGatherActorState state;
            if (actorUuid == null || (state = NON_PENDING_GATHER_ACTORS.get(actorUuid)) != null && !worldKey.equals(state.worldKey())) continue;
            NON_PENDING_GATHER_ACTORS.remove(actorUuid);
        }
    }

    public static void applyNoNGatherCancellation(ServerWorld world, Identifier animationId, List<UUID> actorUuids, NonGatherCancelReason reason) {
        if (world == null || actorUuids == null || actorUuids.isEmpty()) {
            return;
        }
        NeedsOfNature.clearPendingGatherPairsForActors(world, actorUuids);
        if (reason != NonGatherCancelReason.NO_LOS && reason != NonGatherCancelReason.TOO_FAR && reason != NonGatherCancelReason.TIMEOUT) {
            return;
        }
        long now = world.getTime();
        for (UUID actorId : actorUuids) {
            Entity actor = world.getEntity(actorId);
            if (!(actor instanceof EnergyHolder)) continue;
            EnergyHolder holder = (EnergyHolder)actor;
            for (UUID otherId : actorUuids) {
                if (otherId.equals(actorId)) continue;
                holder.applyPartnerCooldown(otherId, now, 1200);
            }
        }
        Entity chatSource = null;
        for (UUID actorId : actorUuids) {
            Entity actor = world.getEntity(actorId);
            if (actor instanceof MobEntity) {
                chatSource = actor;
                break;
            }
            if (chatSource != null || actor == null) continue;
            chatSource = actor;
        }
        if (chatSource != null) {
            NeedsOfNature.sendDebugChatToNearby(world, chatSource, 20.0, NonDebugChatCategory.WARNING, (Text)Text.translatable((String)"debug.needsofnature.gather_cancelled", (Object[])new Object[]{animationId == null ? "unknown" : animationId.getPath(), reason.name().toLowerCase(Locale.ROOT)}));
        }
        LOGGER.info("[NoN] Gather cancelled: animation={} reason={} actors={}", new Object[]{animationId == null ? "unknown" : animationId, reason, actorUuids});
    }

    public static UUID startAnimationNowWithMetadata(ServerWorld world, Identifier animationId, List<? extends Entity> actors, List<String> actorKeys, List<AnimationStageInfo> stages, AfwDamageBehavior damageBehavior, boolean ignoreAttackers, @Nullable Entity positionAnchor, Map<String, String> metadata) {
        return NeedsOfNature.startAnimationNowWithMetadata(world, animationId, actors, actorKeys, stages, damageBehavior, ignoreAttackers, positionAnchor, metadata, null, false);
    }

    public static UUID startAnimationNowWithMetadata(ServerWorld world, Identifier animationId, List<? extends Entity> actors, List<String> actorKeys, List<AnimationStageInfo> stages, AfwDamageBehavior damageBehavior, boolean ignoreAttackers, @Nullable Entity positionAnchor, Map<String, String> metadata, @Nullable ServerPlayerEntity requester, boolean forceChat) {
        if (world == null || animationId == null || actors == null || actors.isEmpty()) {
            return null;
        }
        boolean effectiveIgnoreAttackers = ignoreAttackers || NeedsOfNature.hasPlayerAndHostileActor(actors);
        Map<Object, Object> safeMetadata = metadata == null ? Map.of() : Map.copyOf(metadata);
        return AfwAnimationApi.startAnimationNow((ServerWorld)world, (Identifier)animationId, actors, actorKeys, stages, (AfwAnimationApi.StartOptions)new AfwAnimationApi.StartOptions(damageBehavior, effectiveIgnoreAttackers, positionAnchor, requester, safeMetadata, forceChat));
    }

    @Nullable
    public static AfwMatchedAnimation findAnimationForActors(ServerWorld world, List<? extends Entity> actors, @Nullable Set<String> requiredAnimationTags, @Nullable Entity positionAnchor, boolean requireStartEligibility, @Nullable Entity requiredActiveActor, @Nullable Set<Identifier> excludedAnimationIds) {
        return NeedsOfNature.findAnimationForActors(world, actors, requiredAnimationTags, positionAnchor, requireStartEligibility, requiredActiveActor, excludedAnimationIds, null);
    }

    @Nullable
    public static AfwMatchedAnimation findAnimationForActors(ServerWorld world, List<? extends Entity> actors, @Nullable Set<String> requiredAnimationTags, @Nullable Entity positionAnchor, boolean requireStartEligibility, @Nullable Entity requiredActiveActor, @Nullable Set<Identifier> excludedAnimationIds, @Nullable AfwAnimationApi.AnimationCandidateFilter candidateFilter) {
        Set<Object> safeExcluded;
        if (world == null || actors == null || actors.isEmpty()) {
            return null;
        }
        Set<Object> safeTags = requiredAnimationTags == null ? Set.of() : Set.copyOf(requiredAnimationTags);
        AfwAnimationApi.MatchedAnimation result = AfwAnimationApi.findAnimationForActors((ServerWorld)world, actors, (AfwAnimationApi.MatchOptions)new AfwAnimationApi.MatchOptions(safeTags, positionAnchor, requireStartEligibility, requiredActiveActor, safeExcluded = excludedAnimationIds == null ? Set.of() : Set.copyOf(excludedAnimationIds), candidateFilter));
        if (result == null) {
            return null;
        }
        return new AfwMatchedAnimation(result.animationId(), result.actorKeys(), result.stages());
    }

    public static boolean joinAnimationNow(ServerWorld world, UUID instanceId, List<? extends Entity> newActors, @Nullable ServerPlayerEntity requester) {
        if (world == null || instanceId == null || newActors == null || newActors.isEmpty()) {
            return false;
        }
        return NeedsOfNature.joinAnimationNow(world, instanceId, newActors, requester, null);
    }

    private static boolean tryPlayerJoinActiveAnimation(ServerWorld world, ServerPlayerEntity joiningPlayer, Entity clickedActor) {
        if (world == null || joiningPlayer == null || clickedActor == null) {
            return false;
        }
        if (joiningPlayer.getEntityWorld() != world || clickedActor.getEntityWorld() != world) {
            return false;
        }
        if (joiningPlayer.isSpectator() || clickedActor.isRemoved()) {
            return false;
        }
        UUID instanceId = NeedsOfNature.findActiveInstanceContainingActor(world, clickedActor.getUuid());
        if (instanceId == null) {
            return false;
        }
        InstanceControl control = INSTANCE_CONTROLS.get(instanceId);
        if (control == null || !control.voluntary() || control.isPregnancyMode()) {
            return false;
        }
        AnimationStageInfo currentStage = AfwAnimationApi.getCurrentStage((ServerWorld)world, (UUID)instanceId);
        if (currentStage == null) {
            return false;
        }
        if (!currentStage.allowJoin()) {
            NeedsOfNature.sendDebugChat(joiningPlayer, NonDebugChatCategory.WARNING, (Text)Text.translatable((String)"debug.needsofnature.join_stage_disallows"));
            return true;
        }
        if (AfwAnimationApi.isActorPendingOrActive((ServerWorld)world, (UUID)joiningPlayer.getUuid()) || NonGatherSystem.isActorGathering(joiningPlayer.getUuid())) {
            NeedsOfNature.sendDebugChat(joiningPlayer, NonDebugChatCategory.WARNING, (Text)Text.translatable((String)"debug.needsofnature.player_join_denied_busy"));
            return true;
        }
        Identifier joinAnimationId = NeedsOfNature.resolvePlayerJoinAnimationId(world, instanceId, joiningPlayer, clickedActor);
        if (joinAnimationId == null) {
            NeedsOfNature.sendDebugChat(joiningPlayer, NonDebugChatCategory.WARNING, (Text)Text.translatable((String)"debug.needsofnature.player_join_no_expanded_match"));
            return true;
        }
        LOGGER.info("Player join attempt: player={} target={} instance={} anim={}", new Object[]{joiningPlayer.getName().getString(), clickedActor.getName().getString(), instanceId, joinAnimationId});
        boolean joined = NeedsOfNature.joinAnimationNow(world, instanceId, List.of(joiningPlayer), joiningPlayer, joinAnimationId, clickedActor);
        if (joined) {
            NeedsOfNature.sendDebugChat(joiningPlayer, NonDebugChatCategory.INFO, (Text)Text.translatable((String)"debug.needsofnature.player_join_accepted", (Object[])new Object[]{NeedsOfNature.shortAnimationId(joinAnimationId)}));
        } else {
            NeedsOfNature.sendDebugChat(joiningPlayer, NonDebugChatCategory.WARNING, (Text)Text.translatable((String)"debug.needsofnature.player_join_failed", (Object[])new Object[]{NeedsOfNature.shortAnimationId(joinAnimationId)}));
        }
        return true;
    }

    @Nullable
    private static UUID findActiveInstanceContainingActor(ServerWorld world, UUID actorUuid) {
        if (world == null || actorUuid == null || INSTANCE_ACTORS.isEmpty()) {
            return null;
        }
        for (Map.Entry<UUID, List<UUID>> entry : INSTANCE_ACTORS.entrySet()) {
            UUID instanceId = entry.getKey();
            List<UUID> actors = entry.getValue();
            if (instanceId == null || actors == null || !actors.contains(actorUuid) || AfwAnimationApi.getCurrentStage((ServerWorld)world, (UUID)instanceId) == null) continue;
            return instanceId;
        }
        return null;
    }

    @Nullable
    private static Identifier resolvePlayerJoinAnimationId(ServerWorld world, UUID instanceId, ServerPlayerEntity joiningPlayer, Entity clickedActor) {
        List<Entity> combinedActors = NeedsOfNature.resolvePlayerJoinActors(world, instanceId, joiningPlayer);
        if (combinedActors.isEmpty()) {
            return null;
        }
        AfwMatchedAnimation matched = NeedsOfNature.findAnimationForActors(world, combinedActors, Set.of(), clickedActor, true, null, Set.of(), NonInjectorMatchPolicy::allowsAutomaticNonMatch);
        if (matched == null) {
            return null;
        }
        AfwAnimationDefinitions.Definition definition = AfwAnimationDefinitions.getDefinition((Identifier)matched.animationId());
        if (definition == null) {
            return null;
        }
        if (!NeedsOfNature.isProtectedByAccessory(world, definition, combinedActors)) {
            return matched.animationId();
        }
        if (!NeedsOfNature.consumeProtectorForAnimation(world, definition, combinedActors)) {
            return matched.animationId();
        }
        NonGatherSystem.GatherCandidate fallback = NonGatherSystem.findUnprotectedFallbackCandidate(world, combinedActors, false, (Entity)joiningPlayer, clickedActor, true, matched.animationId());
        return fallback == null ? null : fallback.animationId();
    }

    private static List<Entity> resolvePlayerJoinActors(ServerWorld world, UUID instanceId, ServerPlayerEntity joiningPlayer) {
        if (world == null || instanceId == null || joiningPlayer == null) {
            return List.of();
        }
        List<UUID> existingActorUuids = NeedsOfNature.getInstanceActors(instanceId);
        if (existingActorUuids.isEmpty()) {
            return List.of();
        }
        List<Entity> combined = NeedsOfNature.collectEntities(world, existingActorUuids);
        if (combined.isEmpty()) {
            return List.of();
        }
        HashSet<UUID> existing = new HashSet<UUID>(existingActorUuids);
        if (!(existing.contains(joiningPlayer.getUuid()) || joiningPlayer.getEntityWorld() != world || joiningPlayer.isRemoved() || joiningPlayer.isSpectator())) {
            combined.add((Entity)joiningPlayer);
        }
        if (combined.size() <= existingActorUuids.size()) {
            return List.of();
        }
        combined.sort(Comparator.comparingInt(Entity::getId));
        return combined;
    }

    private static String shortAnimationId(@Nullable Identifier animationId) {
        return animationId == null ? "unknown" : animationId.getPath();
    }

    public static boolean joinAnimationNow(ServerWorld world, UUID instanceId, List<? extends Entity> newActors, @Nullable ServerPlayerEntity requester, @Nullable Identifier expandedAnimationId) {
        return NeedsOfNature.joinAnimationNow(world, instanceId, newActors, requester, expandedAnimationId, null);
    }

    /*
     * Could not resolve type clashes
     * Unable to fully structure code
     */
    public static boolean joinAnimationNow(ServerWorld world, UUID instanceId, List<? extends Entity> newActors, @Nullable ServerPlayerEntity requester, @Nullable Identifier expandedAnimationId, @Nullable Entity positionAnchor) {
        if (world == null || instanceId == null || newActors == null || newActors.isEmpty()) {
            return false;
        }
        currentStage = AfwAnimationApi.getCurrentStage((ServerWorld)world, (UUID)instanceId);
        if (currentStage == null || !currentStage.allowJoin()) {
            return false;
        }
        existingActorUuids = NeedsOfNature.getInstanceActors(instanceId);
        if (existingActorUuids.isEmpty()) {
            return false;
        }
        combined = NeedsOfNature.collectEntities(world, existingActorUuids);
        if (combined.isEmpty()) {
            return false;
        }
        existing = new HashSet<UUID>(existingActorUuids);
        originalCount = combined.size();
        for (Entity actor : newActors) {
            if (actor == null || actor.getEntityWorld() != world || actor.isRemoved() || existing.contains(actor.getUuid()) || (actor instanceof ServerPlayerEntity != false ? (playerActor = (ServerPlayerEntity)actor).isSpectator() != false : actor instanceof PlayerEntity != false || actor instanceof MobEntity == false)) continue;
            combined.add(actor);
            existing.add(actor.getUuid());
        }
        if (combined.size() <= originalCount) {
            return false;
        }
        combined.sort(Comparator.comparingInt((ToIntFunction<Entity>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)I, getId(), (Lnet/minecraft/Entity;)I)()));
        definition = NeedsOfNature.findJoinDefinition(combined, expandedAnimationId);
        if (definition == null || !AfwAnimationApi.isAnimationStartEligible((ServerWorld)world, (AfwAnimationDefinitions.Definition)definition, combined, (Entity)positionAnchor)) {
            return false;
        }
        actorKeys = AfwAnimationDefinitions.resolveActorKeys((AfwAnimationDefinitions.Definition)definition, combined, (Random)world.getRandom());
        if (actorKeys == null || actorKeys.size() != combined.size()) {
            return false;
        }
        control = NeedsOfNature.INSTANCE_CONTROLS.get(instanceId);
        metadata = new LinkedHashMap<String, String>(NeedsOfNature.getInstanceMetadata(world, instanceId));
        metadata.put("afw.join_replace", "true");
        metadata.put("afw.join_replace_from", control != null && control.animationId() != null ? control.animationId().toString() : "unknown");
        if (!AfwServerAnimationController.stopInstance((ServerWorld)world, (UUID)instanceId, (boolean)false)) {
            return false;
        }
        v0 = definition.id();
        v1 = definition.stages();
        v2 = control != null && control.damageBehavior() != null ? control.damageBehavior() : AfwDamageBehavior.STOP_ON_DAMAGE;
        v3 = control != null && control.ignoreAttackers() != false;
        if (requester == null) ** GOTO lbl-1000
        if (combined.stream().filter((Predicate<Entity>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Z, isInstance(java.lang.Object ), (Lnet/minecraft/Entity;)Z)(ServerPlayerEntity.class)).count() > 1L) {
            v4 = true;
        } else lbl-1000:
        // 2 sources

        {
            v4 = false;
        }
        replacement = NeedsOfNature.startAnimationNowWithMetadata(world, v0, combined, actorKeys, v1, v2, v3, positionAnchor, metadata, requester, v4);
        return replacement != null;
    }

    @Nullable
    private static AfwAnimationDefinitions.Definition findJoinDefinition(List<Entity> combined, @Nullable Identifier expandedAnimationId) {
        if (combined == null || combined.isEmpty()) {
            return null;
        }
        AfwAnimationDefinitions.MatchResult match = AfwAnimationDefinitions.match(combined);
        if (expandedAnimationId == null) {
            return match.chosen();
        }
        List candidates = match.candidatesSorted();
        if (candidates == null || candidates.isEmpty()) {
            return null;
        }
        for (AfwAnimationDefinitions.Definition candidate : candidates) {
            if (candidate == null || !expandedAnimationId.equals((Object)candidate.id())) continue;
            return candidate;
        }
        return null;
    }

    private static List<Entity> collectEntities(ServerWorld world, List<UUID> actorUuids) {
        if (world == null || actorUuids == null || actorUuids.isEmpty()) {
            return List.of();
        }
        ArrayList<Entity> actors = new ArrayList<Entity>();
        for (UUID actorUuid : actorUuids) {
            Entity entity = world.getEntity(actorUuid);
            if (entity == null || entity.isRemoved()) continue;
            actors.add(entity);
        }
        actors.sort(Comparator.comparingInt(Entity::getId));
        return actors;
    }

    private static Set<NonLiquidRoles.InjectorRole> resolveEffectiveInjectorRolesForPlayer(Identifier animationId, ServerPlayerEntity player) {
        NonLiquidRoles.LiquidRoles roles = NonLiquidRoles.getRoles(animationId);
        if (roles == null || roles.injectorRoles().isEmpty()) {
            return Set.of();
        }
        LinkedHashSet<NonLiquidRoles.InjectorRole> out = new LinkedHashSet<NonLiquidRoles.InjectorRole>();
        for (NonLiquidRoles.InjectorRole role : roles.injectorRoles().values()) {
            NonLiquidRoles.InjectorRole effective = role;
            if (effective == NonLiquidRoles.InjectorRole.V && player != null && NonGenderSystem.isOnlyMale((Entity)player) && NeedsOfNature.getConfig().convertMaleOnlyVInjectionsToA()) {
                effective = NonLiquidRoles.InjectorRole.A;
            }
            if (effective == null || effective == NonLiquidRoles.InjectorRole.NONE) continue;
            out.add(effective);
        }
        return Set.copyOf(out);
    }

    private static boolean isLiquidInjectorAccepted(@Nullable NonLiquidRoles.InjectorRole role, @Nullable NonLiquidTankType tankType) {
        if (role != NonLiquidRoles.InjectorRole.V && role != NonLiquidRoles.InjectorRole.A) {
            return false;
        }
        if (tankType == null) {
            return true;
        }
        NonLiquidRoles.InjectorRole effective = role;
        if (tankType == NonLiquidTankType.A && role == NonLiquidRoles.InjectorRole.V && NeedsOfNature.getConfig().convertMaleOnlyVInjectionsToA()) {
            effective = NonLiquidRoles.InjectorRole.A;
        }
        return tankType.accepts(effective);
    }

    private static LiquidGainProfile scaleLiquidGainProfile(LiquidGainProfile gain, double scale) {
        if (gain == null || gain.amount() <= 0) {
            return new LiquidGainProfile(0, 0, LiquidHolder.LiquidComposition.EMPTY, null, Map.of());
        }
        double safe = !Double.isFinite(scale) || scale < 0.0 ? 1.0 : scale;
        int amount = NeedsOfNature.applyLiquidGainMultiplier(gain.amount(), safe);
        int xpEligible = Math.min(amount, NeedsOfNature.applyLiquidGainMultiplier(gain.xpEligibleAmount(), safe));
        LinkedHashMap<Identifier, Integer> entityAmounts = new LinkedHashMap<Identifier, Integer>();
        for (Map.Entry<Identifier, Integer> entry : gain.entityAmounts().entrySet()) {
            int scaled = NeedsOfNature.applyLiquidGainMultiplier(entry.getValue(), safe);
            if (scaled <= 0) continue;
            entityAmounts.put(entry.getKey(), scaled);
        }
        return new LiquidGainProfile(amount, xpEligible, amount > 0 ? gain.composition() : LiquidHolder.LiquidComposition.EMPTY, amount > 0 ? gain.entityTypeId() : null, Map.copyOf(entityAmounts));
    }

    private static double resolveLiquidGainMultiplier(@Nullable Identifier animationId) {
        Double value = NonLiquidGainMultipliers.getMultiplier(animationId);
        if (value == null || !Double.isFinite(value) || value < 0.0) {
            return 1.0;
        }
        return value;
    }

    private static int applyLiquidGainMultiplier(int baseGain, double multiplier) {
        if (baseGain <= 0) {
            return 0;
        }
        double safe = !Double.isFinite(multiplier) || multiplier < 0.0 ? 1.0 : multiplier;
        long scaled = Math.round((double)baseGain * safe);
        if (scaled <= 0L) {
            return 0;
        }
        return scaled > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)scaled;
    }

    private static LiquidGainProfile applyAccessoryLiquidGainMultiplier(ServerPlayerEntity player, LiquidGainProfile gain) {
        if (player == null || gain == null || gain.amount() <= 0) {
            return gain;
        }
        double multiplier = NonTrinketsIntegration.getActiveTankAccessoryEffects((LivingEntity)player).liquidGainMultiplier();
        if (!Double.isFinite(multiplier) || Math.abs(multiplier - 1.0) < 1.0E-6) {
            return gain;
        }
        int amount = NeedsOfNature.applyLiquidGainMultiplier(gain.amount(), multiplier);
        int xpEligible = Math.min(amount, NeedsOfNature.applyLiquidGainMultiplier(gain.xpEligibleAmount(), multiplier));
        LinkedHashMap<Identifier, Integer> entityAmounts = new LinkedHashMap<Identifier, Integer>();
        for (Map.Entry<Identifier, Integer> entry : gain.entityAmounts().entrySet()) {
            int scaled = NeedsOfNature.applyLiquidGainMultiplier(entry.getValue(), multiplier);
            if (scaled <= 0) continue;
            entityAmounts.put(entry.getKey(), scaled);
        }
        return new LiquidGainProfile(amount, xpEligible, amount > 0 ? gain.composition() : LiquidHolder.LiquidComposition.EMPTY, amount > 0 ? gain.entityTypeId() : null, Map.copyOf(entityAmounts));
    }

    private static LiquidGainProfile applyApiLiquidGainModifier(ServerPlayerEntity player, LiquidGainProfile gain) {
        if (player == null || gain == null || gain.amount() <= 0) {
            return gain;
        }
        int modifiedAmount = ((NonEvents.ModifyLiquidGain)NonEvents.MODIFY_LIQUID_GAIN.invoker()).modifyLiquidGain(player, gain.entityTypeId(), gain.amount());
        if (modifiedAmount <= 0) {
            return new LiquidGainProfile(0, 0, LiquidHolder.LiquidComposition.EMPTY, null, Map.of());
        }
        if (modifiedAmount == gain.amount()) {
            return gain;
        }
        return NeedsOfNature.scaleLiquidGainProfile(gain, (double)modifiedAmount / (double)gain.amount());
    }

    private static int resolveEntityLiquidGainMl(@Nullable Identifier entityTypeId) {
        if (entityTypeId == null) {
            return 0;
        }
        String key = entityTypeId.toString();
        Map<String, Integer> configMap = NeedsOfNature.getConfig().getLiquidGainByEntity();
        if (configMap.containsKey(key)) {
            Integer configured = configMap.get(key);
            int value = configured == null ? 0 : configured;
            return Math.max(0, Math.min(1000, value));
        }
        Integer packOverride = NonLiquidGainOverrides.getGain(entityTypeId);
        if (packOverride != null) {
            return Math.max(0, Math.min(1000, packOverride));
        }
        return 0;
    }

    private static int resolveEntityLiquidColorRgb(@Nullable Identifier entityTypeId) {
        if (entityTypeId == null) {
            return 16776427;
        }
        String key = entityTypeId.toString();
        String configuredHex = NeedsOfNature.getConfig().getLiquidColorByEntity().get(key);
        Integer configuredRgb = NonLiquidColors.parseHexColor(configuredHex);
        if (configuredRgb != null) {
            return configuredRgb;
        }
        Integer packOverride = NonLiquidGainOverrides.getColorRgb(entityTypeId);
        if (packOverride != null) {
            return packOverride;
        }
        return 16776427;
    }

    private static int resolveMixedLiquidColorRgb() {
        String configuredHex = NeedsOfNature.getConfig().getLiquidColorByEntity().get(LIQUID_COLOR_MIXED_KEY);
        Integer configuredRgb = NonLiquidColors.parseHexColor(configuredHex);
        if (configuredRgb != null) {
            return configuredRgb;
        }
        Integer packOverride = NonLiquidGainOverrides.getMixedColorRgb();
        if (packOverride != null) {
            return packOverride;
        }
        return 15920063;
    }

    private static Map<String, Integer> resolveEffectiveLiquidGainMap() {
        return NonLiquidSystem.resolveEffectiveLiquidGainMap();
    }

    private static Map<String, String> resolveEffectiveLiquidColorMap() {
        return NonLiquidSystem.resolveEffectiveLiquidColorMap();
    }

    private static int resolveStartStage(List<AnimationStageInfo> stages) {
        Integer parsed;
        int startStage = 1;
        if (stages != null && !stages.isEmpty() && (parsed = NonPeakStages.stageNumberFromId(stages.getFirst().animationId())) != null) {
            startStage = parsed;
        }
        return startStage;
    }

    private static int resolveLoopSeconds(Identifier animationId, Integer stageNumber) {
        Identifier stageId = NeedsOfNature.resolveStageId(animationId, stageNumber);
        Integer override = NonLoopSecondsOverrides.getOverrideSeconds(stageId);
        if (override != null) {
            return override;
        }
        Integer baseOverride = NonLoopSecondsOverrides.getOverrideSeconds(animationId);
        if (baseOverride != null) {
            return baseOverride;
        }
        Integer peakStage = NonPeakStages.getPeakStage(animationId);
        if (stageNumber != null && Objects.equals(stageNumber, peakStage)) {
            return CONFIG.getPeakLoopProgressSeconds();
        }
        return CONFIG.getLoopProgressSeconds();
    }

    private static long resolveLoopAdvanceIntervalTicks(Identifier animationId, Integer stageNumber, @Nullable AnimationStageInfo currentStage) {
        long targetTicks = NeedsOfNature.resolveLoopDurationTargetTicks(animationId, stageNumber);
        if (targetTicks < 0L) {
            return -1L;
        }
        Identifier stageId = NeedsOfNature.resolveStageId(animationId, stageNumber);
        double exactCycleTicks = NeedsOfNature.exactCycleTicks(stageId, currentStage);
        if (currentStage == null || !currentStage.loop() || exactCycleTicks <= 0.0) {
            return targetTicks;
        }
        double speed = NeedsOfNature.resolveStageSpeed(currentStage);
        double cycleWorldTicks = exactCycleTicks / speed;
        if (!Double.isFinite(cycleWorldTicks) || cycleWorldTicks <= 0.0) {
            return targetTicks;
        }
        long cycleCount = Math.max(1L, Math.round((double)targetTicks / cycleWorldTicks));
        return Math.max(1L, Math.round((double)cycleCount * cycleWorldTicks));
    }

    private static long resolveLoopDurationTargetTicks(Identifier animationId, Integer stageNumber) {
        int loopSeconds = NeedsOfNature.resolveLoopSeconds(animationId, stageNumber);
        if (loopSeconds < 0) {
            return -1L;
        }
        Identifier stageId = NeedsOfNature.resolveStageId(animationId, stageNumber);
        double multiplier = NonLoopSecondsOverrides.getStageDurationMultiplier(stageId);
        return Math.max(1L, Math.round((double)loopSeconds * 20.0 * multiplier));
    }

    private static double exactCycleTicks(@Nullable Identifier stageId, @Nullable AnimationStageInfo stage) {
        double exact = NonLoopSecondsOverrides.getCycleTicks(stageId);
        if (exact > 0.0) {
            return exact;
        }
        if (stage == null) {
            return 0.0;
        }
        return Math.max(0L, stage.lengthTicks());
    }

    private static Identifier resolveStageId(Identifier animationId, Integer stageNumber) {
        if (animationId == null || stageNumber == null) {
            return animationId;
        }
        return Identifier.of((String)animationId.getNamespace(), (String)(animationId.getPath() + ".p" + stageNumber));
    }

    private static void queueDefeatedTailForInstance(ServerWorld world, UUID instanceId, List<UUID> actorUuids) {
        if (world == null || instanceId == null || actorUuids == null || actorUuids.isEmpty()) {
            return;
        }
        if (!DEFEATED_QUEUED_INSTANCES.add(instanceId)) {
            return;
        }
        ServerPlayerEntity player = null;
        for (UUID actorUuid : actorUuids) {
            ServerPlayerEntity p;
            Entity entity = world.getEntity(actorUuid);
            if (!(entity instanceof ServerPlayerEntity)) continue;
            player = p = (ServerPlayerEntity)entity;
            break;
        }
        if (player == null) {
            DEFEATED_QUEUED_INSTANCES.remove(instanceId);
            return;
        }
        Identifier defeatAnimationId = NeedsOfNature.resolveDefeatAnimationForSource(player, NeedsOfNature.resolveInstanceBaseAnimationId(world, instanceId));
        if (defeatAnimationId == null) {
            DEFEATED_QUEUED_INSTANCES.remove(instanceId);
            LOGGER.warn("[NoN] No defeat-tag animation matched for player actor. world={} instance={} player={}", new Object[]{world.getRegistryKey().getValue(), instanceId, player.getName().getString()});
            NeedsOfNature.sendDebugChat(player, NonDebugChatCategory.SETUP, (Text)Text.translatable((String)"debug.needsofnature.no_defeat_tag_animation"));
            return;
        }
        InstanceControl control = INSTANCE_CONTROLS.get(instanceId);
        boolean inheritIgnoreAttackers = control != null && control.ignoreAttackers();
        boolean queued = NeedsOfNature.enqueueAnimation(world, defeatAnimationId, List.of(player), -1, AfwDamageBehavior.STOP_ON_DAMAGE, inheritIgnoreAttackers, Map.of(NON_MODE_META_KEY, NON_MODE_ATTACK), player);
        if (!queued) {
            DEFEATED_QUEUED_INSTANCES.remove(instanceId);
            return;
        }
        NonAdvancementHooks.recordTossedAround(player);
    }

    private static Identifier resolveDefeatAnimationForSource(ServerPlayerEntity player, @Nullable Identifier sourceAnimationId) {
        Identifier specific;
        if (player == null) {
            return null;
        }
        ServerWorld class_32182 = player.getEntityWorld();
        if (class_32182 instanceof ServerWorld) {
            Object world = class_32182;
            AfwMatchedAnimation match = NeedsOfNature.resolveDefeatAnimationMatchForSource(world, player, sourceAnimationId);
            return match == null ? null : match.animationId();
        }
        String poseTag = NeedsOfNature.resolveDefeatPoseTag(sourceAnimationId);
        if (poseTag != null && (specific = NeedsOfNature.findMatchedAnimationIdByTags(List.of(player), Set.of(poseTag))) != null) {
            return specific;
        }
        for (String fallbackTag : AFW_ANIMATION_TAG_DEFEAT_FALLBACKS) {
            Identifier fallback;
            if (fallbackTag.equals(poseTag) || (fallback = NeedsOfNature.findMatchedAnimationIdByTags(List.of(player), Set.of(fallbackTag))) == null) continue;
            return fallback;
        }
        return null;
    }

    @Nullable
    private static AfwMatchedAnimation resolveDefeatAnimationMatchForSource(ServerWorld world, ServerPlayerEntity player, @Nullable Identifier sourceAnimationId) {
        AfwMatchedAnimation specific;
        if (world == null || player == null) {
            return null;
        }
        String poseTag = NeedsOfNature.resolveDefeatPoseTag(sourceAnimationId);
        if (poseTag != null && (specific = NeedsOfNature.findMatchedAnimationByTags(world, List.of(player), Set.of(poseTag))) != null) {
            return specific;
        }
        for (String fallbackTag : AFW_ANIMATION_TAG_DEFEAT_FALLBACKS) {
            AfwMatchedAnimation fallback;
            if (fallbackTag.equals(poseTag) || (fallback = NeedsOfNature.findMatchedAnimationByTags(world, List.of(player), Set.of(fallbackTag))) == null) continue;
            return fallback;
        }
        return null;
    }

    @Nullable
    private static LastDefeatedCandidate findLastDefeatedCandidate(ServerWorld world, ServerPlayerEntity player, MobEntity damagingMob) {
        if (world == null || player == null || damagingMob == null) {
            return null;
        }
        LastDefeatedCandidate direct = NeedsOfNature.resolveLastDefeatedCandidate(world, player, damagingMob);
        if (direct != null) {
            return direct;
        }
        int radius = NeedsOfNature.getConfig().getLastDefeatedSearchRadius();
        Box box = player.getBoundingBox().expand((double)radius);
        List nearby = world.getEntitiesByClass(MobEntity.class, box, mob -> mob != null && mob != damagingMob && mob.isAlive() && !mob.isRemoved());
        nearby.sort(Comparator.comparingDouble(arg_0 -> ((ServerPlayerEntity)player).squaredDistanceTo(arg_0)));
        for (MobEntity mob2 : nearby) {
            LastDefeatedCandidate candidate = NeedsOfNature.resolveLastDefeatedCandidate(world, player, mob2);
            if (candidate == null) continue;
            return candidate;
        }
        return null;
    }

    @Nullable
    private static LastDefeatedCandidate resolveLastDefeatedCandidate(ServerWorld world, ServerPlayerEntity player, MobEntity mob) {
        EnergyHolder holder;
        if (world == null || player == null || mob == null) {
            return null;
        }
        if (mob.getEntityWorld() != world || player.getEntityWorld() != world) {
            return null;
        }
        if (!mob.isAlive() || mob.isRemoved()) {
            return null;
        }
        if (!(mob instanceof EnergyHolder) || (holder = (EnergyHolder)mob).getEnergy() < NeedsOfNature.getConfig().getLastDefeatedEnergyThreshold()) {
            return null;
        }
        if (!NeedsOfNature.canMobAttackPlayer(player) || NeedsOfNature.isMobAttackFailsafeActive(world, mob.getUuid())) {
            return null;
        }
        if (AfwAnimationApi.isActorPendingOrActive((ServerWorld)world, (UUID)mob.getUuid())) {
            return null;
        }
        ArrayList<Entity> actors = new ArrayList<Entity>(2);
        actors.add((Entity)mob);
        actors.add((Entity)player);
        actors.sort(Comparator.comparingInt(Entity::getId));
        NonGatherSystem.GatherCandidate gatherCandidate = NonGatherSystem.findUnprotectedGatherCandidate(world, actors, true, (Entity)mob, (Entity)player);
        if (gatherCandidate == null) {
            return null;
        }
        AfwMatchedAnimation defeatMatch = NeedsOfNature.resolveDefeatAnimationMatchForSource(world, player, gatherCandidate.animationId());
        if (defeatMatch == null) {
            return null;
        }
        return new LastDefeatedCandidate(mob, gatherCandidate, defeatMatch);
    }

    @Nullable
    private static AfwMatchedAnimation findMatchedAnimationByTags(ServerWorld world, List<? extends Entity> actors, Set<String> requiredTags) {
        if (world == null || actors == null || actors.isEmpty() || requiredTags == null || requiredTags.isEmpty()) {
            return null;
        }
        LinkedHashSet<String> safeTags = new LinkedHashSet<String>();
        for (String tag : requiredTags) {
            String normalized;
            if (tag == null || (normalized = tag.trim()).isEmpty()) continue;
            safeTags.add(normalized);
        }
        if (safeTags.isEmpty()) {
            return null;
        }
        ArrayList<Object> sorted = new ArrayList<Object>(actors.size());
        sorted.addAll(actors);
        sorted.sort(Comparator.comparingInt(Entity::getId));
        return NeedsOfNature.findAnimationForActors(world, sorted, safeTags, null, false, null, Set.of());
    }

    private static boolean hasVanillaTotemReady(ServerPlayerEntity player) {
        if (player == null) {
            return false;
        }
        return player.getMainHandStack().isOf(Items.TOTEM_OF_UNDYING) || player.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING);
    }

    @Nullable
    private static Identifier resolveInstanceBaseAnimationId(ServerWorld world, UUID instanceId) {
        if (world == null || instanceId == null) {
            return null;
        }
        PeakState peakState = PEAK_STATES.get(instanceId);
        if (peakState != null && peakState.animationId() != null) {
            return NonPeakStages.baseAnimationId(peakState.animationId());
        }
        AnimationStageInfo stage = AfwAnimationApi.getCurrentStage((ServerWorld)world, (UUID)instanceId);
        if (stage == null || stage.animationId() == null) {
            return null;
        }
        return NonPeakStages.baseAnimationId(stage.animationId());
    }

    @Nullable
    private static String resolveDefeatPoseTag(@Nullable Identifier sourceAnimationId) {
        if (sourceAnimationId == null) {
            return null;
        }
        for (String contentTag : NeedsOfNature.getAnimationContentTags(sourceAnimationId)) {
            String poseTag;
            if (contentTag == null || (poseTag = DEFEAT_POSE_TAG_BY_CONTENT_TAG.get(contentTag.trim().toLowerCase(Locale.ROOT))) == null || poseTag.isBlank()) continue;
            return poseTag;
        }
        return null;
    }

    private static void tickLiquidDecay(ServerWorld world) {
        NonLiquidSystem.tickLiquidDecay(world);
    }

    private static void updateFilledEffect(ServerPlayerEntity player, LiquidHolder holder) {
        NonLiquidSystem.updateFilledEffect(player, holder);
    }

    public static void updateEnergizedEffect(ServerPlayerEntity player, EnergyHolder holder) {
        if (player == null || holder == null) {
            return;
        }
        int amplifier = NeedsOfNature.getEnergizedAmplifier(holder.getEnergy());
        RegistryEntry entry = Registries.STATUS_EFFECT.getEntry((Object)NonStatusEffects.ENERGIZED);
        StatusEffectInstance current = player.getStatusEffect(entry);
        if (amplifier < 0) {
            if (current != null) {
                player.removeStatusEffect(entry);
            }
            return;
        }
        if (current != null) {
            if (current.getAmplifier() != amplifier) {
                player.removeStatusEffect(entry);
            } else {
                return;
            }
        }
        player.addStatusEffect(new StatusEffectInstance(entry, -1, amplifier, false, false, true));
    }

    private static int getEnergizedAmplifier(int energy) {
        if (energy >= 200) {
            return 2;
        }
        if (energy >= 150) {
            return 1;
        }
        if (energy >= 100) {
            return 0;
        }
        return -1;
    }

    private static void scheduleReceiverDripOnPeak(ServerWorld world, List<UUID> actorUuids, List<String> actorKeys, Identifier animationId, long nowTick) {
        if (world == null || actorUuids == null || actorUuids.isEmpty()) {
            return;
        }
        if (actorKeys == null || actorKeys.size() != actorUuids.size()) {
            return;
        }
        NonLiquidRoles.LiquidRoles roles = NonLiquidRoles.getRoles(animationId);
        if (roles == null) {
            return;
        }
        Set<String> receiverKeys = roles.receiverKeys();
        if (receiverKeys.isEmpty()) {
            return;
        }
        int count = Math.min(actorUuids.size(), actorKeys.size());
        long nextTick = nowTick + 20L;
        long endTick = nowTick + 200L;
        for (int i = 0; i < count; ++i) {
            MobEntity mob;
            Entity entity;
            if (!receiverKeys.contains(actorKeys.get(i)) || !((entity = world.getEntity(actorUuids.get(i))) instanceof MobEntity) || (mob = (MobEntity)entity) instanceof HorseLiquidCollectorEntity || !mob.isAlive() || mob.isRemoved()) continue;
            RECEIVER_DRIP.put(mob.getUuid(), new ReceiverDripState((RegistryKey<World>)world.getRegistryKey(), nextTick, endTick));
        }
    }

    private static void tickReceiverDripParticles(ServerWorld world, long nowTick) {
        if (world == null || RECEIVER_DRIP.isEmpty()) {
            return;
        }
        HashMap<UUID, ReceiverDripState> updates = new HashMap<UUID, ReceiverDripState>();
        HashSet<UUID> removals = new HashSet<UUID>();
        for (Map.Entry<UUID, ReceiverDripState> entry : RECEIVER_DRIP.entrySet()) {
            MobEntity mob;
            UUID uuid = entry.getKey();
            ReceiverDripState state = entry.getValue();
            if (!state.worldKey.equals((Object)world.getRegistryKey())) continue;
            if (nowTick > state.endTick) {
                removals.add(uuid);
                continue;
            }
            Entity entity = world.getEntity(uuid);
            if (!(entity instanceof MobEntity) || !(mob = (MobEntity)entity).isAlive() || mob.isRemoved()) {
                removals.add(uuid);
                continue;
            }
            if (nowTick < state.nextTick) continue;
            Vec3d spawnPos = NeedsOfNature.resolveReceiverDripSpawnPos(mob);
            world.spawnParticles((ParticleEffect)NonParticles.LIQUID_PARTICLE_FALLING, spawnPos.x, spawnPos.y, spawnPos.z, 1, 0.0, 0.0, 0.0, 0.0);
            long nextTick = state.nextTick + 20L;
            if (nextTick > state.endTick) {
                removals.add(uuid);
                continue;
            }
            updates.put(uuid, new ReceiverDripState(state.worldKey, nextTick, state.endTick));
        }
        for (UUID uUID : removals) {
            RECEIVER_DRIP.remove(uUID);
        }
        for (Map.Entry<UUID, ReceiverDripState> entry : updates.entrySet()) {
            if (removals.contains(entry.getKey())) continue;
            RECEIVER_DRIP.put(entry.getKey(), entry.getValue());
        }
    }

    private static Vec3d resolveReceiverDripSpawnPos(MobEntity mob) {
        Vec3d localOffset = NeedsOfNature.resolveReceiverDripOffset(mob);
        double yawRad = Math.toRadians(mob.getYaw());
        double sin = Math.sin(yawRad);
        double cos = Math.cos(yawRad);
        Vec3d forward = new Vec3d(-sin, 0.0, cos);
        Vec3d right = new Vec3d(forward.z, 0.0, -forward.x);
        return new Vec3d(mob.getX(), mob.getY(), mob.getZ()).add(right.multiply(localOffset.x)).add(0.0, localOffset.y, 0.0).add(forward.multiply(localOffset.z));
    }

    private static Vec3d resolveReceiverDripOffset(MobEntity mob) {
        Identifier entityId = Registries.ENTITY_TYPE.getId((Object)mob.getType());
        return RECEIVER_DRIP_OFFSETS.getOrDefault(entityId, RECEIVER_DRIP_OFFSET_DEFAULT);
    }

    private void registerVoluntaryStopRequests() {
        ServerPlayNetworking.registerGlobalReceiver(StopVoluntaryAnimationC2SPayload.ID, (payload, context) -> context.server().execute(() -> {
            ServerPlayerEntity player = context.player();
            ServerWorld patt0$temp = player.getEntityWorld();
            if (!(patt0$temp instanceof ServerWorld)) {
                return;
            }
            ServerWorld world = patt0$temp;
            UUID instanceId = payload.instanceId();
            InstanceControl control = INSTANCE_CONTROLS.get(instanceId);
            if (control == null || !control.voluntary() || !control.hasPlayer()) {
                return;
            }
            if (control.isPregnancyMode()) {
                return;
            }
            PlayerActiveInstance active = ACTIVE_PLAYER_INSTANCES.get(player.getUuid());
            if (active == null) {
                return;
            }
            if (!active.instanceId().equals(instanceId)) {
                return;
            }
            if (!active.worldKey().equals((Object)world.getRegistryKey())) {
                return;
            }
            NeedsOfNature.clearQueuedAnimations(world, player);
            AfwAnimationApi.stopAnimation((ServerWorld)world, (UUID)instanceId);
        }));
    }

    private void registerManualPeakRequests() {
        ServerPlayNetworking.registerGlobalReceiver(StartManualPeakC2SPayload.ID, (payload, context) -> context.server().execute(() -> {
            ServerPlayerEntity player = context.player();
            ServerWorld patt0$temp = player.getEntityWorld();
            if (!(patt0$temp instanceof ServerWorld)) {
                return;
            }
            ServerWorld world = patt0$temp;
            if (player.isSpectator()) {
                return;
            }
            if (NeedsOfNature.hasActivePlayerAnimation(player)) {
                return;
            }
            if (!player.isOnGround()) {
                return;
            }
            NonManualPeakDefinitions.Selection selection = NonManualPeakDefinitions.select(world, player);
            if (selection == null) {
                return;
            }
            List<ServerPlayerEntity> actors = List.of(player);
            AfwAnimationDefinitions.Definition definition = selection.definition();
            UUID instanceId = NeedsOfNature.startAnimationNowWithMetadata(world, definition.id(), actors, selection.actorKeys(), definition.stages(), AfwDamageBehavior.STOP_ON_DAMAGE, false, null, NeedsOfNature.buildModeMetadata(false));
            if (instanceId != null && selection.rightHandPropItemId() != null) {
                NeedsOfNature.sendManualPeakPropOverride(world, player, instanceId, selection.rightHandPropItemId());
            }
            if (instanceId != null && selection.matchedHeldItemId() != null) {
                NonAdvancementHooks.grantAcquireHardware(player);
            }
        }));
    }

    private static void sendManualPeakPropOverride(ServerWorld world, ServerPlayerEntity player, UUID instanceId, Identifier itemId) {
        if (world == null || player == null || instanceId == null || itemId == null) {
            return;
        }
        ManualPeakPropOverrideS2CPayload payload = new ManualPeakPropOverrideS2CPayload(instanceId, player.getUuid(), itemId);
        LinkedHashSet<ServerPlayerEntity> recipients = new LinkedHashSet<ServerPlayerEntity>(PlayerLookup.tracking((Entity)player));
        recipients.add(player);
        for (ServerPlayerEntity recipient : recipients) {
            ServerPlayNetworking.send((ServerPlayerEntity)recipient, (CustomPayload)payload);
        }
    }

    private void registerFillBottleUseTrigger() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ServerWorld serverWorld;
            block3: {
                block2: {
                    if (!(world instanceof ServerWorld)) break block2;
                    serverWorld = (ServerWorld)world;
                    if (player instanceof ServerPlayerEntity) break block3;
                }
                return ActionResult.PASS;
            }
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity)player;
            return NeedsOfNature.tryStartFillBottleAnimationOnUse(serverWorld, serverPlayer, hand) ? ActionResult.SUCCESS : ActionResult.PASS;
        });
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            ServerWorld serverWorld;
            block6: {
                block5: {
                    if (!(world instanceof ServerWorld)) break block5;
                    serverWorld = (ServerWorld)world;
                    if (player instanceof ServerPlayerEntity) break block6;
                }
                return ActionResult.PASS;
            }
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity)player;
            if (world.getFluidState(hitResult.getBlockPos()).isIn(FluidTags.WATER)) {
                return ActionResult.PASS;
            }
            if (world.getBlockState(hitResult.getBlockPos()).isOf(Blocks.WATER_CAULDRON)) {
                return ActionResult.PASS;
            }
            return NeedsOfNature.tryStartFillBottleAnimationOnUse(serverWorld, serverPlayer, hand) ? ActionResult.SUCCESS : ActionResult.PASS;
        });
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            ServerWorld serverWorld;
            block3: {
                block2: {
                    if (!(world instanceof ServerWorld)) break block2;
                    serverWorld = (ServerWorld)world;
                    if (player instanceof ServerPlayerEntity) break block3;
                }
                return ActionResult.PASS;
            }
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity)player;
            return NeedsOfNature.tryStartFillBottleAnimationOnUse(serverWorld, serverPlayer, hand) ? ActionResult.SUCCESS : ActionResult.PASS;
        });
    }

    private static boolean tryStartFillBottleAnimationOnUse(ServerWorld world, ServerPlayerEntity player, Hand hand) {
        if (world == null || player == null || hand == null) {
            return false;
        }
        if (player.isSpectator()) {
            return false;
        }
        if (player.isTouchingWater()) {
            return false;
        }
        if (NeedsOfNature.hasActivePlayerAnimation(player)) {
            return false;
        }
        if (!player.isOnGround()) {
            return false;
        }
        if (player.getPitch() < 80.0f) {
            return false;
        }
        if (!player.getStackInHand(hand).isOf(Items.GLASS_BOTTLE)) {
            return false;
        }
        if (!(player instanceof LiquidHolder)) {
            return false;
        }
        LiquidHolder liquidHolder = (LiquidHolder)player;
        NeedsOfNature.updateFilledEffect(player, liquidHolder);
        int requiredMl = NeedsOfNature.getFillBottleRequiredMl(liquidHolder);
        if (liquidHolder.getLiquidStored() < requiredMl) {
            return false;
        }
        if (!NeedsOfNature.hasFilledStageOneOrHigher(player)) {
            return false;
        }
        Identifier fillBottleId = NeedsOfNature.findMatchedAnimationIdByTags(List.of(player), Set.of(AFW_ANIMATION_TAG_FILL_BOTTLE));
        if (fillBottleId == null) {
            return false;
        }
        AfwAnimationDefinitions.Definition fillDefinition = AfwAnimationDefinitions.getDefinition((Identifier)fillBottleId);
        if (fillDefinition == null) {
            return false;
        }
        if (!NonItemSystem.consumeOneGlassBottle(player, hand)) {
            return false;
        }
        List<ServerPlayerEntity> actors = List.of(player);
        List actorKeys = AfwAnimationDefinitions.resolveActorKeys((AfwAnimationDefinitions.Definition)fillDefinition, actors, (Random)world.getRandom());
        UUID instanceId = NeedsOfNature.startAnimationNowWithMetadata(world, fillDefinition.id(), actors, actorKeys, fillDefinition.stages(), AfwDamageBehavior.STOP_ON_DAMAGE, false, null, NeedsOfNature.buildModeMetadata(false));
        if (instanceId != null) {
            FILL_BOTTLE_STATES.put(instanceId, new FillBottleState(player.getUuid(), true));
            return true;
        }
        NonItemSystem.refundGlassBottle(player);
        return false;
    }

    private static void applyAttackEscapeDamage(ServerWorld world, ServerPlayerEntity player, @Nullable List<UUID> actorUuids) {
        NonAttackSystem.applyAttackEscapeDamage(world, player, actorUuids);
    }

    private static void scheduleAttackEscapeResolution(ServerWorld world, ServerPlayerEntity player, @Nullable List<UUID> actorUuids, boolean allowReattack, int invulnerabilityTicks) {
        if (world == null || player == null) {
            return;
        }
        List<UUID> actors = actorUuids == null || actorUuids.isEmpty() ? List.of() : List.copyOf(actorUuids);
        PENDING_ATTACK_ESCAPE_RESOLUTIONS.put(player.getUuid(), new PendingAttackEscapeResolution((RegistryKey<World>)world.getRegistryKey(), actors, world.getTime() + 1L, allowReattack, invulnerabilityTicks));
    }

    private static void tickPendingAttackEscapeResolutions(ServerWorld world, long nowTick) {
        if (world == null || PENDING_ATTACK_ESCAPE_RESOLUTIONS.isEmpty()) {
            return;
        }
        RegistryKey worldKey = world.getRegistryKey();
        for (Map.Entry<UUID, PendingAttackEscapeResolution> entry : PENDING_ATTACK_ESCAPE_RESOLUTIONS.entrySet()) {
            ServerPlayerEntity player;
            UUID playerUuid = entry.getKey();
            PendingAttackEscapeResolution resolution = entry.getValue();
            if (playerUuid == null || resolution == null) {
                if (playerUuid == null) continue;
                PENDING_ATTACK_ESCAPE_RESOLUTIONS.remove(playerUuid);
                continue;
            }
            if (!worldKey.equals(resolution.worldKey()) || nowTick < resolution.dueTick() || !PENDING_ATTACK_ESCAPE_RESOLUTIONS.remove(playerUuid, resolution) || (player = world.getServer().getPlayerManager().getPlayer(playerUuid)) == null || player.getEntityWorld() != world || !player.isAlive() || player.isRemoved()) continue;
            POST_ESCAPE_DAMAGE_INVULNERABLE_UNTIL_BY_PLAYER.remove(playerUuid);
            NeedsOfNature.applyAttackEscapeDamage(world, player, resolution.actorUuids());
            if (!player.isAlive() || player.isRemoved()) continue;
            if (resolution.invulnerabilityTicks() > 0) {
                NeedsOfNature.setPostEscapeDamageInvulnerable(player, resolution.invulnerabilityTicks());
            }
            if (!resolution.allowReattack()) continue;
            NeedsOfNature.startPostEscapeReattack(world, player, resolution.actorUuids());
        }
    }

    private static void startPostEscapeReattack(ServerWorld world, ServerPlayerEntity player, @Nullable List<UUID> actorUuids) {
        if (world == null || player == null || !player.isAlive() || player.isRemoved()) {
            return;
        }
        NonAttackSystem.startNearbyPostEscapeGathers(world, player, actorUuids);
        NeedsOfNature.requestOriginalAttackers(world, player, actorUuids);
    }

    private static void requestOriginalAttackers(ServerWorld world, ServerPlayerEntity player, @Nullable List<UUID> actorUuids) {
        if (world == null || player == null || !player.isAlive() || player.isRemoved()) {
            return;
        }
        if (actorUuids == null || actorUuids.isEmpty()) {
            return;
        }
        for (UUID actor : actorUuids) {
            MobEntity mob;
            Entity entity;
            if (actor == null || actor.equals(player.getUuid()) || !((entity = world.getEntity(actor)) instanceof MobEntity) || !(mob = (MobEntity)entity).isAlive() || !(mob instanceof EnergyHolder)) continue;
            EnergyHolder holder = (EnergyHolder)mob;
            holder.requestAttackOnPlayer(world, player);
        }
    }

    private static boolean isDefeatedInstance(ServerWorld world, UUID instanceId) {
        if (world == null || instanceId == null) {
            return false;
        }
        PeakState peakState = PEAK_STATES.get(instanceId);
        if (peakState != null && NeedsOfNature.hasAnimationTagWithPrefix(peakState.animationId, AFW_ANIMATION_TAG_DEFEAT_PREFIX)) {
            return true;
        }
        AnimationStageInfo stage = AfwAnimationApi.getCurrentStage((ServerWorld)world, (UUID)instanceId);
        if (stage == null || stage.animationId() == null) {
            return false;
        }
        Identifier baseAnimationId = NonPeakStages.baseAnimationId(stage.animationId());
        return NeedsOfNature.hasAnimationTagWithPrefix(baseAnimationId, AFW_ANIMATION_TAG_DEFEAT_PREFIX);
    }

    private static boolean isManualPeakAnimation(Identifier animationId) {
        return NeedsOfNature.hasAnimationTag(animationId, AFW_ANIMATION_TAG_MANUAL_PEAK);
    }

    private static boolean isFillBottleAnimation(Identifier animationId) {
        return NeedsOfNature.hasAnimationTag(animationId, AFW_ANIMATION_TAG_FILL_BOTTLE);
    }

    private static boolean isBirthAnimation(Identifier animationId) {
        return NeedsOfNature.hasAnimationTag(animationId, AFW_ANIMATION_TAG_BIRTH) || NeedsOfNature.hasAnimationTagWithPrefix(animationId, AFW_ANIMATION_TAG_BIRTH_PREFIX);
    }

    private static boolean isWaterAnimation(Identifier animationId) {
        if (animationId == null) {
            return false;
        }
        AfwAnimationDefinitions.Definition definition = AfwAnimationDefinitions.getDefinition((Identifier)NonPeakStages.baseAnimationId(animationId));
        return definition != null && definition.waterRequirement() != AfwAnimationDefinitions.WaterRequirement.NONE;
    }

    private static boolean hasAnimationTag(Identifier animationId, String tag) {
        if (animationId == null || tag == null || tag.isBlank()) {
            return false;
        }
        AfwAnimationDefinitions.Definition definition = AfwAnimationDefinitions.getDefinition((Identifier)NonPeakStages.baseAnimationId(animationId));
        if (definition == null) {
            return false;
        }
        String expected = tag.trim().toLowerCase(Locale.ROOT);
        if (expected.isEmpty()) {
            return false;
        }
        Set tags = definition.animationTags();
        if (tags == null || tags.isEmpty()) {
            return false;
        }
        for (String value : tags) {
            if (value == null || !expected.equals(value.trim().toLowerCase(Locale.ROOT))) continue;
            return true;
        }
        return false;
    }

    private static boolean hasAnimationTagWithPrefix(Identifier animationId, String prefix) {
        if (animationId == null || prefix == null || prefix.isBlank()) {
            return false;
        }
        AfwAnimationDefinitions.Definition definition = AfwAnimationDefinitions.getDefinition((Identifier)NonPeakStages.baseAnimationId(animationId));
        if (definition == null) {
            return false;
        }
        String expectedPrefix = prefix.trim().toLowerCase(Locale.ROOT);
        if (expectedPrefix.isEmpty()) {
            return false;
        }
        Set tags = definition.animationTags();
        if (tags == null || tags.isEmpty()) {
            return false;
        }
        for (String value : tags) {
            if (value == null || !value.trim().toLowerCase(Locale.ROOT).startsWith(expectedPrefix)) continue;
            return true;
        }
        return false;
    }

    private static List<String> getAnimationContentTags(@Nullable Identifier animationId) {
        if (animationId == null) {
            return List.of();
        }
        AfwAnimationDefinitions.Definition definition = AfwAnimationDefinitions.getDefinition((Identifier)NonPeakStages.baseAnimationId(animationId));
        if (definition == null) {
            return List.of();
        }
        List tags = definition.contentTags();
        if (tags == null || tags.isEmpty()) {
            return List.of();
        }
        ArrayList<String> out = new ArrayList<String>();
        for (String entry : tags) {
            String tag;
            if (entry == null || (tag = entry.trim().toLowerCase(Locale.ROOT)).isEmpty()) continue;
            out.add(tag);
        }
        return out.isEmpty() ? List.of() : List.copyOf(out);
    }

    private static Identifier findMatchedAnimationIdByTags(List<? extends Entity> actors, Set<String> requiredTags) {
        if (actors == null || actors.isEmpty() || requiredTags == null || requiredTags.isEmpty()) {
            return null;
        }
        ArrayList<Object> sorted = new ArrayList<Object>(actors.size());
        sorted.addAll(actors);
        sorted.sort(Comparator.comparingInt(Entity::getId));
        LinkedHashSet<String> safeTags = new LinkedHashSet<String>();
        for (String tag : requiredTags) {
            String normalized;
            if (tag == null || (normalized = tag.trim()).isEmpty()) continue;
            safeTags.add(normalized);
        }
        if (safeTags.isEmpty()) {
            return null;
        }
        return AfwAnimationApi.findMatchedAnimationId(sorted, safeTags);
    }

    private static void maybeStartPregnancyOnPeak(ServerWorld world, List<UUID> actorUuids, List<String> actorKeys, Identifier animationId) {
        if (world == null || actorUuids == null || actorKeys == null) {
            return;
        }
        if (actorUuids.isEmpty() || actorKeys.isEmpty()) {
            return;
        }
        if (actorUuids.size() != actorKeys.size()) {
            return;
        }
        NonLiquidRoles.LiquidRoles roles = NonLiquidRoles.getRoles(animationId);
        if (roles == null) {
            return;
        }
        Map<String, NonLiquidRoles.InjectorRole> injectorRoles = roles.injectorRoles();
        if (injectorRoles.isEmpty()) {
            return;
        }
        ArrayList<PregnancyDonor> donors = new ArrayList<PregnancyDonor>();
        for (int i = 0; i < actorUuids.size(); ++i) {
            Entity actor;
            if (!NeedsOfNature.isLiquidInjectorAccepted(injectorRoles.get(actorKeys.get(i)), NonLiquidTankType.V) || (actor = world.getEntity(actorUuids.get(i))) == null || actor instanceof ServerPlayerEntity) continue;
            donors.add(new PregnancyDonor(Registries.ENTITY_TYPE.getId((Object)actor.getType()), NeedsOfNature.capturePregnancyVariantData(world, actor)));
        }
        if (donors.isEmpty()) {
            return;
        }
        long nowTick = NeedsOfNature.getGlobalTick(world);
        List<ServerPlayerEntity> players = NeedsOfNature.collectPlayerActors(world, actorUuids);
        for (ServerPlayerEntity player : players) {
            if (player == null || !player.isAlive() || player.isRemoved() || NonGenderSystem.isOnlyMale((Entity)player) || NeedsOfNature.getPregnantEntityTypeId(player) != null || NonTrinketsIntegration.blocksPregnancy(player)) continue;
            NonAccessoryEffects accessoryEffects = NonTrinketsIntegration.getActiveTankAccessoryEffects((LivingEntity)player);
            PregnancyDonor chosen = (PregnancyDonor)donors.get(world.getRandom().nextInt(donors.size()));
            Identifier chosenType = chosen.entityTypeId();
            int chancePercent = NonEntityProfiles.resolve(chosenType).pregnancyChancePercent();
            if (chancePercent <= 0) continue;
            chancePercent = NeedsOfNature.applyFertilityIncreaserChance(player, chancePercent);
            chancePercent = (int)Math.round((double)chancePercent * accessoryEffects.pregnancyChanceMultiplier());
            if ((chancePercent = Math.max(0, Math.min(100, chancePercent))) <= 0 || chancePercent < 100 && world.getRandom().nextInt(100) >= chancePercent) continue;
            long durationTicks = Math.max(1L, Math.round((double)NeedsOfNature.getConfig().getPregnancyDurationTicks() * accessoryEffects.pregnancyDurationMultiplier()));
            NeedsOfNature.beginPregnancy(player, chosenType, nowTick + durationTicks, chosen.variantData());
            NonStats.incrementPregnancyStarted(player);
            NonAdvancementHooks.grantFirstPregnancy(player);
        }
    }

    @Nullable
    static PregnancyVariantData capturePregnancyVariantData(ServerWorld world, Entity entity) {
        if (world == null || entity == null) {
            return null;
        }
        LinkedHashMap<String, String> components = new LinkedHashMap<String, String>();
        for (VariantComponentDescriptor descriptor : PREGNANCY_VARIANT_COMPONENTS) {
            descriptor.capture(world, entity, components);
        }
        Integer horseMarking = null;
        if (entity instanceof HorseEntity) {
            HorseEntity horse = (HorseEntity)entity;
            horseMarking = horse.getMarking().getIndex();
        }
        return NeedsOfNature.normalizePregnancyVariantData(new PregnancyVariantData(components, horseMarking));
    }

    @Nullable
    public static PregnancyVariantData normalizePregnancyVariantData(@Nullable PregnancyVariantData data) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        return data;
    }

    @Nullable
    public static String encodePregnancyVariantData(@Nullable PregnancyVariantData data) {
        PregnancyVariantData normalized = NeedsOfNature.normalizePregnancyVariantData(data);
        if (normalized == null) {
            return null;
        }
        return HOST_CONFIG_SYNC_GSON.toJson((Object)normalized);
    }

    @Nullable
    public static PregnancyVariantData decodePregnancyVariantData(@Nullable String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            return NeedsOfNature.normalizePregnancyVariantData((PregnancyVariantData)HOST_CONFIG_SYNC_GSON.fromJson(raw, PregnancyVariantData.class));
        }
        catch (RuntimeException e) {
            LOGGER.warn("[NoN] Ignoring malformed pregnancy variant data.", (Throwable)e);
            return null;
        }
    }

    private static void applyPregnancyVariantData(ServerWorld world, Entity entity, @Nullable PregnancyVariantData data) {
        PregnancyVariantData normalized = NeedsOfNature.normalizePregnancyVariantData(data);
        if (world == null || entity == null || normalized == null) {
            return;
        }
        Map<String, String> components = normalized.components();
        for (VariantComponentDescriptor descriptor : PREGNANCY_VARIANT_COMPONENTS) {
            descriptor.apply(world, entity, components.get(descriptor.key()));
        }
        if (normalized.horseMarkingIndex() != null && entity instanceof HorseEntity) {
            HorseEntity horse = (HorseEntity)entity;
            HorseMarking marking = HorseMarking.byIndex((int)normalized.horseMarkingIndex());
            HorseColor color = horse.getHorseColor();
            Method method = NeedsOfNature.findMethod(horse.getClass(), "setHorseVariant", HorseColor.class, HorseMarking.class);
            if (method != null) {
                NeedsOfNature.tryInvokeMethod(horse, method, color, marking);
            }
        }
    }

    public static void applyPregnancyVariantDataForEgg(ServerWorld world, Entity entity, @Nullable PregnancyVariantData data) {
        NeedsOfNature.applyPregnancyVariantData(world, entity, data);
    }

    private static <T> VariantComponentDescriptor registryVariant(final String key, final ComponentType<RegistryEntry<T>> componentType, final RegistryKey<Registry<T>> registryKey) {
        return new VariantComponentDescriptor(){

            @Override
            public String key() {
                return key;
            }

            @Override
            public void capture(ServerWorld world, Entity entity, Map<String, String> out) {
                RegistryEntry entry = (RegistryEntry)entity.get(componentType);
                if (entry == null) {
                    return;
                }
                entry.getKey().map(RegistryKey::getValue).ifPresent(id -> out.put(key, id.toString()));
            }

            @Override
            public void apply(ServerWorld world, Entity entity, String value) {
                Identifier id = Identifier.tryParse((String)(value == null ? "" : value));
                if (id == null) {
                    return;
                }
                world.getRegistryManager().getOptional(registryKey).flatMap(registry -> registry.getEntry(id)).ifPresent(entry -> entity.setComponent(componentType, entry));
            }
        };
    }

    private static <T> VariantComponentDescriptor lazyRegistryVariant(final String key, final ComponentType<LazyRegistryEntryReference<T>> componentType, final RegistryKey<Registry<T>> registryKey) {
        return new VariantComponentDescriptor(){

            @Override
            public String key() {
                return key;
            }

            @Override
            public void capture(ServerWorld world, Entity entity, Map<String, String> out) {
                LazyRegistryEntryReference value = (LazyRegistryEntryReference)entity.get(componentType);
                if (value == null) {
                    return;
                }
                Optional<Identifier> keyValue = value.getKey();
                if (keyValue.isEmpty()) {
                    keyValue = value.resolveEntry((RegistryWrapper.WrapperLookup)world.getRegistryManager()).flatMap(RegistryEntry::getKey);
                }
                keyValue.map(RegistryKey::getValue).ifPresent(id -> out.put(key, id.toString()));
            }

            @Override
            public void apply(ServerWorld world, Entity entity, String value) {
                Identifier id = Identifier.tryParse((String)(value == null ? "" : value));
                if (id == null) {
                    return;
                }
                RegistryKey valueKey = RegistryKey.of((RegistryKey)registryKey, (Identifier)id);
                entity.setComponent(componentType, (Object)new LazyRegistryEntryReference(valueKey));
            }
        };
    }

    private static <E extends Enum<E>> VariantComponentDescriptor enumVariant(final String key, final ComponentType<E> componentType, final Class<E> enumClass) {
        return new VariantComponentDescriptor(){

            @Override
            public String key() {
                return key;
            }

            @Override
            public void capture(ServerWorld world, Entity entity, Map<String, String> out) {
                Enum value = (Enum)entity.get(componentType);
                if (value != null) {
                    out.put(key, value.name());
                }
            }

            @Override
            public void apply(ServerWorld world, Entity entity, String value) {
                if (value == null || value.isBlank()) {
                    return;
                }
                try {
                    entity.setComponent(componentType, Enum.valueOf(enumClass, value));
                }
                catch (IllegalArgumentException ignored) {
                    LOGGER.warn("[NoN] Ignoring unknown pregnancy variant value {}={}.", (Object)key, (Object)value);
                }
            }
        };
    }

    static int applyFertilityIncreaserChance(ServerPlayerEntity player, int baseChancePercent) {
        int base = Math.max(0, Math.min(100, baseChancePercent));
        if (player == null) {
            return base;
        }
        int boostedFloor = 0;
        for (StatusEffectInstance effect : player.getStatusEffects()) {
            if (effect == null || effect.getEffectType().comp_349() != NonStatusEffects.FERTILITY_INCREASER) continue;
            int floor = effect.getAmplifier() >= 1 ? 95 : 80;
            boostedFloor = Math.max(boostedFloor, floor);
        }
        return Math.max(base, boostedFloor);
    }

    private static void refreshPregnancyCueRuntime(ServerWorld world, UUID instanceId, long stageStartTick, Identifier fallbackAnimationId) {
        boolean cueExpected;
        Identifier clipId;
        if (world == null || instanceId == null) {
            return;
        }
        PregnancyHatchState hatchState = PREGNANCY_HATCH_BY_INSTANCE.get(instanceId);
        if (hatchState == null) {
            PREGNANCY_CUE_RUNTIME_BY_INSTANCE.remove(instanceId);
            return;
        }
        AnimationStageInfo currentStage = AfwAnimationApi.getCurrentStage((ServerWorld)world, (UUID)instanceId);
        Identifier class_29602 = clipId = currentStage != null && currentStage.animationId() != null ? currentStage.animationId() : fallbackAnimationId;
        if (clipId == null) {
            PREGNANCY_CUE_RUNTIME_BY_INSTANCE.remove(instanceId);
            return;
        }
        Integer cueTick = NonPregnancyCues.getCueTick(clipId);
        long startTick = Math.max(0L, stageStartTick);
        double birthLoopMultiplier = NeedsOfNature.pregnancyBirthLoopSpeedMultiplier(clipId, hatchState);
        double runtimeStageSpeed = NeedsOfNature.clampAnimationSpeed(NeedsOfNature.resolveStageSpeed(currentStage) * birthLoopMultiplier);
        PregnancyCueRuntime existing = PREGNANCY_CUE_RUNTIME_BY_INSTANCE.get(instanceId);
        if (existing != null && existing.worldKey().equals((Object)world.getRegistryKey()) && existing.stageStartTick() == startTick && existing.clipId().equals((Object)clipId) && Objects.equals(existing.cueTick(), cueTick) && Math.abs(existing.stageSpeed() - runtimeStageSpeed) < 1.0E-4) {
            return;
        }
        PREGNANCY_CUE_RUNTIME_BY_INSTANCE.put(instanceId, new PregnancyCueRuntime((RegistryKey<World>)world.getRegistryKey(), startTick, clipId, cueTick, false, runtimeStageSpeed));
        NeedsOfNature.applyPregnancyBirthLoopSpeed(world, instanceId, birthLoopMultiplier);
        NeedsOfNature.syncPregnancyLoopProgressState(world, instanceId, currentStage, startTick, runtimeStageSpeed, hatchState);
        Integer stageNumber = NonPeakStages.stageNumberFromId(clipId);
        boolean bl = cueExpected = stageNumber != null && stageNumber >= 2;
        if (cueTick == null && cueExpected && (existing == null || !clipId.equals((Object)existing.clipId()) || existing.cueTick() != null)) {
            LOGGER.warn("[NoN] Missing birth cue in birth clip {}.", (Object)clipId);
            ServerPlayerEntity player = NeedsOfNature.findPlayerOnWorldServer(world, hatchState.playerUuid());
            if (player != null && player.getEntityWorld() == world) {
                NeedsOfNature.sendDebugChat(player, NonDebugChatCategory.SETUP, (Text)Text.translatable((String)"debug.needsofnature.missing_birth_cue", (Object[])new Object[]{clipId.toString()}));
            }
        }
    }

    private static void applyPregnancyBirthLoopSpeed(ServerWorld world, UUID instanceId, double multiplier) {
        if (world == null || instanceId == null) {
            return;
        }
        if (Math.abs(multiplier - 1.0) < 1.0E-4) {
            return;
        }
        AfwServerAnimationController.adjustSpeed((ServerWorld)world, (UUID)instanceId, (double)multiplier, null);
    }

    private static void syncPregnancyLoopProgressState(ServerWorld world, UUID instanceId, @Nullable AnimationStageInfo stage, long startTick, double stageSpeed, PregnancyHatchState hatchState) {
        if (world == null || instanceId == null || stage == null || hatchState == null) {
            return;
        }
        Integer stageNumber = NonPeakStages.stageNumberFromId(stage.animationId());
        if (stageNumber == null || stageNumber != 2) {
            return;
        }
        ServerPlayerEntity player = NeedsOfNature.findPlayerOnWorldServer(world, hatchState.playerUuid());
        if (player == null || player.getEntityWorld() != world) {
            return;
        }
        long cycleWorldTicks = Math.max(1L, NeedsOfNature.elapsedWorldTicksFromAnimationTicks(Math.max(1L, stage.lengthTicks()), stageSpeed));
        NeedsOfNature.syncStageProgressState(instanceId, stage, startTick, cycleWorldTicks, List.of(player));
    }

    private static double pregnancyBirthLoopSpeedMultiplier(Identifier clipId, PregnancyHatchState hatchState) {
        Integer stageNumber = NonPeakStages.stageNumberFromId(clipId);
        if (stageNumber == null || stageNumber != 2) {
            return 1.0;
        }
        if (hatchState == null) {
            return 1.0;
        }
        int pendingRepeatsBeforeFinalLoop = Math.max(0, hatchState.remainingSpawns() - 2);
        if (pendingRepeatsBeforeFinalLoop <= 0) {
            return 1.0;
        }
        int variableRepeatCount = Math.max(1, hatchState.totalSpawnCount() - 2);
        double normalizedPending = Math.max(0.0, Math.min(1.0, (double)pendingRepeatsBeforeFinalLoop / (double)variableRepeatCount));
        double eased = 1.0 - Math.pow(1.0 - normalizedPending, 2.5);
        double speedRange = 1.0;
        return Math.max(1.0, Math.min(2.0, 1.0 + speedRange * eased));
    }

    private static void tickPregnancyCueRuntime(ServerWorld world, long nowTick) {
        if (world == null || PREGNANCY_CUE_RUNTIME_BY_INSTANCE.isEmpty()) {
            return;
        }
        RegistryKey worldKey = world.getRegistryKey();
        for (Map.Entry<UUID, PregnancyCueRuntime> entry : PREGNANCY_CUE_RUNTIME_BY_INSTANCE.entrySet()) {
            PregnancyCueRuntime triggered;
            UUID instanceId = entry.getKey();
            PregnancyCueRuntime runtime = entry.getValue();
            if (!runtime.worldKey().equals((Object)worldKey)) continue;
            Integer cueTick = runtime.cueTick();
            long cueDueTick = runtime.stageStartTick() + NeedsOfNature.elapsedWorldTicksFromAnimationTicks(cueTick == null ? -1.0 : (double)cueTick.intValue(), runtime.stageSpeed());
            if (!runtime.cueTriggered() && cueTick != null && cueTick >= 0 && nowTick >= cueDueTick && PREGNANCY_CUE_RUNTIME_BY_INSTANCE.replace(instanceId, runtime, triggered = new PregnancyCueRuntime(runtime.worldKey(), runtime.stageStartTick(), runtime.clipId(), runtime.cueTick(), true, runtime.stageSpeed()))) {
                NeedsOfNature.handlePregnancyBirthCue(world, instanceId);
                runtime = triggered;
            }
            if (!runtime.cueTriggered()) continue;
            NeedsOfNature.routePregnancyBirthStageAfterCycle(world, instanceId, runtime, nowTick);
        }
    }

    private static void tickPregnancy(ServerWorld world) {
        if (world == null || world.getPlayers().isEmpty()) {
            return;
        }
        long globalTick = NeedsOfNature.getGlobalTick(world);
        for (ServerPlayerEntity player : world.getPlayers()) {
            NeedsOfNature.tickPregnancyForPlayer(world, player, globalTick);
        }
    }

    private static void tickPregnancyForPlayer(ServerWorld world, ServerPlayerEntity player, long nowTick) {
        if (world == null || player == null) {
            return;
        }
        PregnancyHolder holder = NeedsOfNature.getPregnancyHolder(player);
        if (holder == null) {
            return;
        }
        Identifier entityTypeId = holder.getPregnantEntityTypeId();
        if (entityTypeId == null) {
            PREGNANCY_START_RETRY_TICKS.remove(player.getUuid());
            return;
        }
        if (NonGenderSystem.isOnlyMale((Entity)player)) {
            NeedsOfNature.clearPregnancyState(player, true);
            return;
        }
        if (!player.isAlive() || player.isRemoved()) {
            return;
        }
        if (!holder.isPregnancyPendingHatch()) {
            long endTick = holder.getPregnancyEndTick();
            if (endTick > nowTick) {
                if (!player.hasStatusEffect(Registries.STATUS_EFFECT.getEntry((Object)NonStatusEffects.PREGNANT))) {
                    NeedsOfNature.clearPregnancyState(player, false);
                }
                return;
            }
            NeedsOfNature.markPregnancyPending(player, nowTick);
        } else {
            NeedsOfNature.refreshPendingPregnancyEffect(player);
        }
        NeedsOfNature.tryStartPendingPregnancyHatch(world, player, nowTick);
    }

    private static void tryStartPendingPregnancyHatch(ServerWorld world, ServerPlayerEntity player, long nowTick) {
        if (world == null || player == null) {
            return;
        }
        PregnancyHolder holder = NeedsOfNature.getPregnancyHolder(player);
        if (holder == null || !holder.isPregnancyPendingHatch()) {
            return;
        }
        if (NonGenderSystem.isOnlyMale((Entity)player)) {
            NeedsOfNature.clearPregnancyState(player, true);
            return;
        }
        if (!player.isAlive() || player.isRemoved() || player.isSpectator()) {
            return;
        }
        if (player.getEntityWorld() != world) {
            return;
        }
        UUID activeInstance = PREGNANCY_ACTIVE_INSTANCE_BY_PLAYER.get(player.getUuid());
        if (activeInstance != null) {
            if (NeedsOfNature.hasActivePlayerAnimation(player)) {
                return;
            }
            PREGNANCY_ACTIVE_INSTANCE_BY_PLAYER.remove(player.getUuid(), activeInstance);
            PREGNANCY_HATCH_BY_INSTANCE.remove(activeInstance);
            PREGNANCY_CUE_RUNTIME_BY_INSTANCE.remove(activeInstance);
        }
        if (NeedsOfNature.hasActivePlayerAnimation(player)) {
            NeedsOfNature.queuePendingPregnancyHatch(world, player, holder, nowTick);
            return;
        }
        if (PREGNANCY_QUEUED_PLAYERS.contains(player.getUuid())) {
            if (AfwAnimationApi.isActorPendingOrActive((ServerWorld)world, (UUID)player.getUuid())) {
                return;
            }
            PREGNANCY_QUEUED_PLAYERS.remove(player.getUuid());
        }
        if (player.isTouchingWater() || player.isSubmergedInWater()) {
            return;
        }
        if (!player.isOnGround()) {
            return;
        }
        long retryAt = PREGNANCY_START_RETRY_TICKS.getOrDefault(player.getUuid(), 0L);
        if (retryAt > nowTick) {
            return;
        }
        Identifier entityTypeId = holder.getPregnantEntityTypeId();
        if (entityTypeId == null) {
            return;
        }
        Identifier animationId = NeedsOfNature.resolveBirthAnimationId(player, entityTypeId);
        if (animationId == null) {
            LOGGER.warn("[NoN] Missing birth-tag animation for player {}; clearing pregnancy for {}.", (Object)player.getName().getString(), (Object)entityTypeId);
            NeedsOfNature.sendDebugChat(player, NonDebugChatCategory.SETUP, (Text)Text.translatable((String)"debug.needsofnature.missing_birth_animation", (Object[])new Object[]{entityTypeId.toString()}));
            NeedsOfNature.clearPregnancyState(player, true);
            return;
        }
        AfwAnimationDefinitions.Definition definition = AfwAnimationDefinitions.getDefinition((Identifier)animationId);
        if (definition == null) {
            LOGGER.warn("[NoN] Missing birth animation definition {}.", (Object)animationId);
            NeedsOfNature.sendDebugChat(player, NonDebugChatCategory.SETUP, (Text)Text.translatable((String)"debug.needsofnature.missing_birth_animation", (Object[])new Object[]{entityTypeId.toString()}));
            NeedsOfNature.clearPregnancyState(player, true);
            return;
        }
        List<ServerPlayerEntity> actors = List.of(player);
        List actorKeys = AfwAnimationDefinitions.resolveActorKeys((AfwAnimationDefinitions.Definition)definition, actors, (Random)world.getRandom());
        int spawnCount = NeedsOfNature.resolvePregnancyOffspringCount(holder);
        NonEntityProfiles.ResolvedProfile profile = NonEntityProfiles.resolve(entityTypeId);
        Map<String, String> metadata = NeedsOfNature.buildPregnancyHatchMetadata(entityTypeId, profile, spawnCount, holder.getPregnancyVariantData());
        UUID started = NeedsOfNature.startAnimationNowWithMetadata(world, definition.id(), actors, actorKeys, definition.stages(), AfwDamageBehavior.BLOCK_DAMAGE, true, null, metadata);
        if (started == null) {
            PREGNANCY_START_RETRY_TICKS.put(player.getUuid(), nowTick + 20L);
            return;
        }
        PREGNANCY_HATCH_BY_INSTANCE.putIfAbsent(started, new PregnancyHatchState(player.getUuid(), entityTypeId, profile.birthEntityTypeId(), profile.birthMode(), spawnCount, spawnCount, 0, holder.getPregnancyVariantData()));
        PREGNANCY_ACTIVE_INSTANCE_BY_PLAYER.put(player.getUuid(), started);
        PREGNANCY_START_RETRY_TICKS.remove(player.getUuid());
        NeedsOfNature.refreshPregnancyCueRuntime(world, started, nowTick, definition.id());
    }

    private static void queuePendingPregnancyHatch(ServerWorld world, ServerPlayerEntity player, PregnancyHolder holder, long nowTick) {
        if (world == null || player == null || holder == null) {
            return;
        }
        if (NonGenderSystem.isOnlyMale((Entity)player)) {
            NeedsOfNature.clearPregnancyState(player, true);
            return;
        }
        UUID playerUuid = player.getUuid();
        if (PREGNANCY_QUEUED_PLAYERS.contains(playerUuid)) {
            return;
        }
        long retryAt = PREGNANCY_START_RETRY_TICKS.getOrDefault(playerUuid, 0L);
        if (retryAt > nowTick) {
            return;
        }
        Identifier entityTypeId = holder.getPregnantEntityTypeId();
        if (entityTypeId == null) {
            return;
        }
        Identifier animationId = NeedsOfNature.resolveBirthAnimationId(player, entityTypeId);
        if (animationId == null) {
            LOGGER.warn("[NoN] Missing birth-tag animation for player {}; clearing pregnancy for {}.", (Object)player.getName().getString(), (Object)entityTypeId);
            NeedsOfNature.sendDebugChat(player, NonDebugChatCategory.SETUP, (Text)Text.translatable((String)"debug.needsofnature.missing_birth_animation", (Object[])new Object[]{entityTypeId.toString()}));
            NeedsOfNature.clearPregnancyState(player, true);
            return;
        }
        AfwAnimationDefinitions.Definition definition = AfwAnimationDefinitions.getDefinition((Identifier)animationId);
        if (definition == null) {
            LOGGER.warn("[NoN] Missing birth animation definition {}.", (Object)animationId);
            NeedsOfNature.sendDebugChat(player, NonDebugChatCategory.SETUP, (Text)Text.translatable((String)"debug.needsofnature.missing_birth_animation", (Object[])new Object[]{entityTypeId.toString()}));
            NeedsOfNature.clearPregnancyState(player, true);
            return;
        }
        int spawnCount = NeedsOfNature.resolvePregnancyOffspringCount(holder);
        NonEntityProfiles.ResolvedProfile profile = NonEntityProfiles.resolve(entityTypeId);
        boolean queued = NeedsOfNature.enqueueAnimation(world, definition.id(), List.of(player), 1, AfwDamageBehavior.BLOCK_DAMAGE, true, NeedsOfNature.buildPregnancyHatchMetadata(entityTypeId, profile, spawnCount, holder.getPregnancyVariantData()), player);
        if (!queued) {
            PREGNANCY_START_RETRY_TICKS.put(playerUuid, nowTick + 20L);
            return;
        }
        PREGNANCY_QUEUED_PLAYERS.add(playerUuid);
        PREGNANCY_START_RETRY_TICKS.remove(playerUuid);
    }

    private static Map<String, String> buildPregnancyHatchMetadata(Identifier entityTypeId, NonEntityProfiles.ResolvedProfile profile, int spawnCount, @Nullable PregnancyVariantData variantData) {
        LinkedHashMap<String, String> metadata = new LinkedHashMap<String, String>();
        metadata.put(NON_MODE_META_KEY, NON_MODE_PREGNANCY);
        metadata.put(NON_PREGNANCY_ENTITY_META_KEY, entityTypeId.toString());
        if (profile != null) {
            metadata.put(NON_PREGNANCY_BIRTH_ENTITY_META_KEY, profile.birthEntityTypeId().toString());
            metadata.put(NON_PREGNANCY_BIRTH_MODE_META_KEY, profile.birthMode());
        }
        metadata.put(NON_PREGNANCY_OFFSPRING_COUNT_META_KEY, String.valueOf(Math.max(1, Math.min(16, spawnCount))));
        String variantJson = NeedsOfNature.encodePregnancyVariantData(variantData);
        if (variantJson != null && !variantJson.isBlank()) {
            metadata.put(NON_PREGNANCY_VARIANT_META_KEY, variantJson);
        }
        return metadata;
    }

    private static int resolvePregnancyOffspringCount(@Nullable PregnancyHolder holder) {
        int offspringCount;
        int n = offspringCount = holder == null ? 0 : holder.getPregnancyOffspringCount();
        if (offspringCount <= 0) {
            return 1;
        }
        return Math.max(1, Math.min(16, offspringCount));
    }

    private static int rollPregnancyOffspringCount(ServerPlayerEntity player, Identifier entityTypeId, int exactOverride) {
        if (exactOverride > 0) {
            return Math.max(1, Math.min(16, exactOverride));
        }
        NonConfig.OffspringCountRange range = NonEntityProfiles.resolve(entityTypeId).offspringRange();
        int min = range.min();
        int max = range.max();
        if (max <= min) {
            return min;
        }
        return min + player.getRandom().nextInt(max - min + 1);
    }

    private static Identifier resolveBirthAnimationId(ServerPlayerEntity player, Identifier entityTypeId) {
        if (player == null || entityTypeId == null) {
            return null;
        }
        List<ServerPlayerEntity> actors = List.of(player);
        Identifier specific = NeedsOfNature.findMatchedAnimationIdByTags(actors, Set.of(AFW_ANIMATION_TAG_BIRTH_PREFIX + NeedsOfNature.sanitizeBirthTagPart(entityTypeId)));
        if (specific != null) {
            return specific;
        }
        return NeedsOfNature.findMatchedAnimationIdByTags(actors, Set.of(AFW_ANIMATION_TAG_BIRTH));
    }

    private static String sanitizeBirthTagPart(Identifier entityTypeId) {
        String raw = entityTypeId.toString().toLowerCase(Locale.ROOT);
        StringBuilder out = new StringBuilder(raw.length());
        for (int i = 0; i < raw.length(); ++i) {
            char c = raw.charAt(i);
            if (c >= 'a' && c <= 'z' || c >= '0' && c <= '9') {
                out.append(c);
                continue;
            }
            out.append('_');
        }
        return out.toString();
    }

    private static void routePregnancyBirthStage(ServerWorld world, UUID instanceId) {
        if (world == null || instanceId == null) {
            return;
        }
        PregnancyHatchState hatchState = PREGNANCY_HATCH_BY_INSTANCE.get(instanceId);
        if (hatchState == null || hatchState.remainingSpawns() > 1) {
            return;
        }
        AnimationStageInfo currentStage = AfwAnimationApi.getCurrentStage((ServerWorld)world, (UUID)instanceId);
        Integer stageNumber = NonPeakStages.stageNumberFromId(currentStage == null ? null : currentStage.animationId());
        if (stageNumber != null && stageNumber == 2) {
            AfwAnimationApi.advanceToStage((ServerWorld)world, (UUID)instanceId, (int)3);
        }
    }

    private static void routePregnancyBirthStageAfterCycle(ServerWorld world, UUID instanceId, PregnancyCueRuntime runtime, long nowTick) {
        if (world == null || instanceId == null || runtime == null) {
            return;
        }
        PregnancyHatchState hatchState = PREGNANCY_HATCH_BY_INSTANCE.get(instanceId);
        if (hatchState == null || hatchState.remainingSpawns() <= 0) {
            return;
        }
        AnimationStageInfo currentStage = AfwAnimationApi.getCurrentStage((ServerWorld)world, (UUID)instanceId);
        Integer stageNumber = NonPeakStages.stageNumberFromId(currentStage == null ? null : currentStage.animationId());
        if (stageNumber == null || stageNumber != 2) {
            return;
        }
        long cycleTicks = Math.max(1L, currentStage.lengthTicks());
        long elapsedTicks = NeedsOfNature.elapsedWorldTicksFromAnimationTicks(cycleTicks, runtime.stageSpeed());
        long routeTick = runtime.stageStartTick() + Math.max(1L, elapsedTicks);
        if (nowTick < routeTick) {
            return;
        }
        int targetStage = hatchState.remainingSpawns() > 1 ? 2 : 3;
        AfwAnimationApi.advanceToStage((ServerWorld)world, (UUID)instanceId, (int)targetStage);
    }

    private static void handlePregnancyBirthCue(ServerWorld world, UUID instanceId) {
        PregnancyHatchState updated;
        if (world == null || instanceId == null) {
            return;
        }
        PregnancyHatchState hatchState = PREGNANCY_HATCH_BY_INSTANCE.get(instanceId);
        if (hatchState == null || hatchState.remainingSpawns() <= 0) {
            return;
        }
        ServerPlayerEntity player = NeedsOfNature.findPlayerOnWorldServer(world, hatchState.playerUuid());
        if (player == null || player.getEntityWorld() != world) {
            return;
        }
        int spawnNow = 1;
        PregnancyVariantData variantData = hatchState.sourceEntityTypeId().equals((Object)hatchState.birthEntityTypeId()) ? hatchState.variantData() : null;
        int spawned = NeedsOfNature.spawnPregnancyOutcome(world, player, hatchState.sourceEntityTypeId(), hatchState.birthEntityTypeId(), hatchState.birthMode(), spawnNow, variantData);
        if (spawned > 0) {
            NonStats.addOffspringSpawned(player, hatchState.birthEntityTypeId(), spawned);
        }
        if ((updated = hatchState.withProgress(hatchState.remainingSpawns() - spawnNow, hatchState.spawnedCount() + spawned)).remainingSpawns() <= 0) {
            PREGNANCY_HATCH_BY_INSTANCE.remove(instanceId);
            NeedsOfNature.finishPregnancyHatch(world, instanceId, player, updated);
            return;
        }
        PREGNANCY_HATCH_BY_INSTANCE.put(instanceId, updated);
    }

    private static void finalizePregnancyHatch(ServerWorld world, UUID instanceId, boolean cueTriggered) {
        int spawned;
        if (world == null || instanceId == null) {
            return;
        }
        PregnancyHatchState hatchState = PREGNANCY_HATCH_BY_INSTANCE.remove(instanceId);
        PregnancyCueRuntime cueRuntime = PREGNANCY_CUE_RUNTIME_BY_INSTANCE.remove(instanceId);
        if (hatchState == null) {
            return;
        }
        ServerPlayerEntity player = NeedsOfNature.findPlayerOnWorldServer(world, hatchState.playerUuid());
        if (player == null || player.getEntityWorld() != world) {
            return;
        }
        if (!cueTriggered) {
            String clip = cueRuntime == null || cueRuntime.clipId() == null ? "unknown" : cueRuntime.clipId().toString();
            LOGGER.warn("[NoN] Birth fallback spawn on STOP for instance {} (clip={}, cue={}).", new Object[]{instanceId, clip, cueRuntime == null ? "none" : String.valueOf(cueRuntime.cueTick())});
            NeedsOfNature.sendDebugChat(player, NonDebugChatCategory.SETUP, (Text)Text.translatable((String)"debug.needsofnature.birth_fallback_spawn", (Object[])new Object[]{clip}));
        }
        int remaining = Math.max(0, hatchState.remainingSpawns());
        PregnancyVariantData variantData = hatchState.sourceEntityTypeId().equals((Object)hatchState.birthEntityTypeId()) ? hatchState.variantData() : null;
        int n = spawned = remaining > 0 ? NeedsOfNature.spawnPregnancyOutcome(world, player, hatchState.sourceEntityTypeId(), hatchState.birthEntityTypeId(), hatchState.birthMode(), remaining, variantData) : 0;
        if (spawned > 0) {
            NonStats.addOffspringSpawned(player, hatchState.birthEntityTypeId(), spawned);
        }
        NeedsOfNature.finishPregnancyHatch(world, instanceId, player, hatchState.withProgress(0, hatchState.spawnedCount() + spawned));
    }

    private static void finishPregnancyHatch(ServerWorld world, UUID instanceId, ServerPlayerEntity player, PregnancyHatchState hatchState) {
        if (world == null || instanceId == null || player == null || hatchState == null) {
            return;
        }
        PREGNANCY_HATCH_BY_INSTANCE.remove(instanceId);
        PREGNANCY_CUE_RUNTIME_BY_INSTANCE.remove(instanceId);
        PREGNANCY_ACTIVE_INSTANCE_BY_PLAYER.remove(hatchState.playerUuid(), instanceId);
        PREGNANCY_QUEUED_PLAYERS.remove(hatchState.playerUuid());
        NonStats.incrementPregnancyCompleted(player);
        NonAdvancementHooks.checkMotherNature(player);
        if (hatchState.spawnedCount() > 1) {
            NonAdvancementHooks.grantProductivePregnancy(player);
        }
        NeedsOfNature.clearPregnancyState(player, true);
    }

    private static int spawnPregnancyOutcome(ServerWorld world, ServerPlayerEntity player, Identifier sourceEntityTypeId, Identifier entityTypeId, String birthMode, int spawnCount, @Nullable PregnancyVariantData variantData) {
        if (world == null || player == null || entityTypeId == null || spawnCount <= 0) {
            return 0;
        }
        if ("egg".equals(NonConfig.normalizeBirthMode(birthMode))) {
            return NeedsOfNature.spawnPregnancyEggs(world, player, sourceEntityTypeId, entityTypeId, spawnCount, variantData);
        }
        return NeedsOfNature.spawnPregnancyOffspring(world, player, entityTypeId, spawnCount, variantData);
    }

    private static int spawnPregnancyEggs(ServerWorld world, ServerPlayerEntity player, Identifier sourceEntityTypeId, Identifier entityTypeId, int spawnCount, @Nullable PregnancyVariantData variantData) {
        if (world == null || player == null || entityTypeId == null || spawnCount <= 0) {
            return 0;
        }
        Identifier eggProfileEntityId = sourceEntityTypeId == null ? entityTypeId : sourceEntityTypeId;
        NonConfig.EggProfile eggProfile = NonEntityProfiles.resolve(eggProfileEntityId).eggProfile();
        int hatchSeconds = NeedsOfNature.getConfig().getPregnancyEggHatchSecondsFor(eggProfileEntityId.toString());
        long hatchAtTick = NeedsOfNature.getGlobalTick(world) + (long)Math.max(1, hatchSeconds) * 20L;
        int spawned = 0;
        for (int i = 0; i < spawnCount; ++i) {
            PregnancyEggEntity egg = (PregnancyEggEntity)PREGNANCY_EGG_ENTITY_TYPE.create((World)world, SpawnReason.BREEDING);
            if (egg == null) continue;
            float yaw = world.getRandom().nextFloat() * 360.0f;
            Vec3d spawnOffset = NeedsOfNature.randomPregnancyEggSpawnOffset(world, spawnCount);
            egg.refreshPositionAndAngles(player.getX() + spawnOffset.x, player.getY(), player.getZ() + spawnOffset.z, yaw, 0.0f);
            egg.configure(player.getUuid(), entityTypeId, hatchAtTick, variantData, eggProfile);
            if (!world.spawnEntity((Entity)egg)) continue;
            ++spawned;
        }
        return spawned;
    }

    private static Vec3d randomPregnancyEggSpawnOffset(ServerWorld world, int spawnCount) {
        double angle = world.getRandom().nextDouble() * Math.PI * 2.0;
        double radius = 0.08 + world.getRandom().nextDouble() * 0.1;
        return new Vec3d(Math.cos(angle) * radius, 0.0, Math.sin(angle) * radius);
    }

    private static int spawnPregnancyOffspring(ServerWorld world, ServerPlayerEntity player, Identifier entityTypeId, int spawnCount, @Nullable PregnancyVariantData variantData) {
        if (world == null || player == null || entityTypeId == null || spawnCount <= 0) {
            return 0;
        }
        if (!Registries.ENTITY_TYPE.containsId(entityTypeId)) {
            LOGGER.warn("[NoN] Cannot spawn pregnancy offspring: unknown entity type {}.", (Object)entityTypeId);
            NeedsOfNature.sendDebugChat(player, NonDebugChatCategory.SETUP, (Text)Text.translatable((String)"debug.needsofnature.missing_pregnancy_entity_type", (Object[])new Object[]{entityTypeId.toString()}));
            return 0;
        }
        EntityType entityType = (EntityType)Registries.ENTITY_TYPE.get(entityTypeId);
        int spawned = 0;
        for (int i = 0; i < spawnCount; ++i) {
            Entity entity = entityType.create((World)world, SpawnReason.BREEDING);
            if (entity == null) continue;
            entity.refreshPositionAndAngles(player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch());
            if (entity instanceof MobEntity) {
                MobEntity mob = (MobEntity)entity;
                mob.initialize((ServerWorldAccess)world, world.getLocalDifficulty(mob.getBlockPos()), SpawnReason.BREEDING, null);
                NeedsOfNature.applyPregnancyVariantData(world, (Entity)mob, variantData);
                NeedsOfNature.applyPregnancyMobFlags(mob, player);
            }
            if (!world.spawnEntity(entity)) continue;
            ++spawned;
        }
        return spawned;
    }

    private static void applyPregnancyMobFlags(MobEntity mob, ServerPlayerEntity player) {
        NeedsOfNature.applyPregnancyMobFlagsForEgg(mob, player);
    }

    public static void applyPregnancyMobFlagsForEgg(MobEntity mob, @Nullable ServerPlayerEntity player) {
        if (mob == null) {
            return;
        }
        mob.setPersistent();
        if (mob instanceof PassiveEntity) {
            PassiveEntity passive = (PassiveEntity)mob;
            passive.setBreedingAge(-24000);
        } else {
            NeedsOfNature.tryInvokeBooleanSetter(mob, "setBaby");
        }
        if (player != null && NeedsOfNature.getConfig().isPregnancyAutoTameMobs()) {
            if (mob instanceof TameableEntity) {
                TameableEntity tameable = (TameableEntity)mob;
                tameable.setTamedBy((PlayerEntity)player);
                tameable.setTarget(null);
            } else if (mob instanceof AbstractHorseEntity) {
                AbstractHorseEntity horse = (AbstractHorseEntity)mob;
                horse.bondWithPlayer((PlayerEntity)player);
                horse.setOwner((LivingEntity)player);
            } else {
                NeedsOfNature.tryInvokeBooleanSetter(mob, "setTamed");
                NeedsOfNature.tryInvokeBooleanSetter(mob, "setTame");
                NeedsOfNature.tryInvokeSingleArg(mob, "setOwner", PlayerEntity.class, player);
                NeedsOfNature.tryInvokeSingleArg(mob, "setOwner", ServerPlayerEntity.class, player);
                NeedsOfNature.tryInvokeSingleArg(mob, "setOwnerUuid", UUID.class, player.getUuid());
                NeedsOfNature.tryInvokeSingleArg(mob, "bondWithPlayer", PlayerEntity.class, player);
                NeedsOfNature.tryInvokeSingleArg(mob, "bondWithPlayer", ServerPlayerEntity.class, player);
            }
        }
        if (NeedsOfNature.getConfig().pregnancyFriendlyMobsIgnorePlayers()) {
            if (mob instanceof Angerable) {
                Angerable angerable = (Angerable)mob;
                angerable.stopAnger();
            }
            if (mob.getTarget() instanceof ServerPlayerEntity) {
                mob.setTarget(null);
            }
            mob.addCommandTag(NON_PREGNANCY_FRIENDLY_TAG);
        }
    }

    private static void tryInvokeBooleanSetter(Object target, String methodName) {
        if (target == null || methodName == null || methodName.isBlank()) {
            return;
        }
        Method method = NeedsOfNature.findMethod(target.getClass(), methodName, Boolean.TYPE);
        if (method == null) {
            return;
        }
        NeedsOfNature.tryInvokeMethod(target, method, true);
    }

    private static void tryInvokeSingleArg(Object target, String methodName, Class<?> argType, Object argValue) {
        if (target == null || methodName == null || methodName.isBlank() || argType == null) {
            return;
        }
        Method method = NeedsOfNature.findMethod(target.getClass(), methodName, argType);
        if (method == null) {
            return;
        }
        NeedsOfNature.tryInvokeMethod(target, method, argValue);
    }

    private static Method findMethod(Class<?> type, String methodName, Class<?> ... parameterTypes) {
        for (Class<?> cursor = type; cursor != null; cursor = cursor.getSuperclass()) {
            Method declared = NeedsOfNature.findDeclaredMethod(cursor, methodName, parameterTypes);
            if (declared == null) continue;
            return declared;
        }
        return NeedsOfNature.findPublicMethod(Objects.requireNonNull(type), methodName, parameterTypes);
    }

    private static Method findDeclaredMethod(Class<?> owner, String methodName, Class<?> ... parameterTypes) {
        Method[] methods;
        for (Method method : methods = owner.getDeclaredMethods()) {
            if (!method.getName().equals(methodName) || !NeedsOfNature.parameterTypesEqual(method.getParameterTypes(), parameterTypes)) continue;
            try {
                method.setAccessible(true);
                return method;
            }
            catch (RuntimeException e) {
                NeedsOfNature.logReflectionFailureOnce("findDeclared:" + owner.getName() + "#" + methodName, e);
                return null;
            }
        }
        return null;
    }

    private static Method findPublicMethod(Class<?> owner, String methodName, Class<?> ... parameterTypes) {
        Method[] methods;
        for (Method method : methods = owner.getMethods()) {
            if (!method.getName().equals(methodName) || !NeedsOfNature.parameterTypesEqual(method.getParameterTypes(), parameterTypes)) continue;
            try {
                method.setAccessible(true);
                return method;
            }
            catch (RuntimeException e) {
                NeedsOfNature.logReflectionFailureOnce("findPublic:" + owner.getName() + "#" + methodName, e);
                return null;
            }
        }
        return null;
    }

    private static boolean parameterTypesEqual(Class<?>[] left, Class<?>[] right) {
        if (left.length != right.length) {
            return false;
        }
        for (int i = 0; i < left.length; ++i) {
            if (left[i].equals(right[i])) continue;
            return false;
        }
        return true;
    }

    private static void tryInvokeMethod(Object target, Method method, Object ... args) {
        if (target == null || method == null) {
            return;
        }
        try {
            method.invoke(target, args);
        }
        catch (ReflectiveOperationException | RuntimeException e) {
            NeedsOfNature.logReflectionFailureOnce("invoke:" + method.toGenericString(), e);
        }
    }

    private static void logReflectionFailureOnce(String key, Throwable error) {
        if (key == null || !REFLECTION_FAILURE_LOGS.add(key)) {
            return;
        }
        LOGGER.debug("[NoN] Reflection helper failure at {}", (Object)key, (Object)error);
    }

    public static boolean isPregnancyFriendlyMob(MobEntity mob) {
        return mob != null && NeedsOfNature.getConfig().pregnancyFriendlyMobsIgnorePlayers() && mob.getCommandTags().contains(NON_PREGNANCY_FRIENDLY_TAG);
    }

    static long getGlobalTick(ServerWorld world) {
        if (world == null || world.getServer() == null) {
            return 0L;
        }
        ServerWorld overworld = world.getServer().getOverworld();
        return overworld == null ? world.getTime() : overworld.getTime();
    }

    private static PregnancyHolder getPregnancyHolder(ServerPlayerEntity player) {
        if (player instanceof PregnancyHolder) {
            PregnancyHolder holder = (PregnancyHolder)player;
            return holder;
        }
        return null;
    }

    static Identifier getPregnantEntityTypeId(ServerPlayerEntity player) {
        PregnancyHolder holder = NeedsOfNature.getPregnancyHolder(player);
        return holder == null ? null : holder.getPregnantEntityTypeId();
    }

    static void beginPregnancy(ServerPlayerEntity player, Identifier entityTypeId, long endTick) {
        NeedsOfNature.beginPregnancy(player, entityTypeId, endTick, null);
    }

    static void beginPregnancy(ServerPlayerEntity player, Identifier entityTypeId, long endTick, @Nullable PregnancyVariantData variantData) {
        NeedsOfNature.beginPregnancy(player, entityTypeId, endTick, variantData, 0);
    }

    static void beginPregnancy(ServerPlayerEntity player, Identifier entityTypeId, long endTick, @Nullable PregnancyVariantData variantData, int offspringCountOverride) {
        long l;
        ServerWorld class_32182;
        if (player == null || entityTypeId == null) {
            return;
        }
        if (NonGenderSystem.isOnlyMale((Entity)player)) {
            NeedsOfNature.clearPregnancyState(player, true);
            return;
        }
        if (!((NonEvents.AllowPregnancy)NonEvents.ALLOW_PREGNANCY.invoker()).allowPregnancy(player, entityTypeId)) {
            return;
        }
        PregnancyHolder holder = NeedsOfNature.getPregnancyHolder(player);
        if (holder == null) {
            return;
        }
        int offspringCount = NeedsOfNature.rollPregnancyOffspringCount(player, entityTypeId, offspringCountOverride);
        holder.setPregnancyState(entityTypeId, Math.max(0L, endTick), false, variantData);
        holder.setPregnancyOffspringCount(offspringCount);
        NeedsOfNature.syncPregnancyState(player);
        NonApiInternals.firePregnancyChanged(player);
        PREGNANCY_START_RETRY_TICKS.remove(player.getUuid());
        PREGNANCY_QUEUED_PLAYERS.remove(player.getUuid());
        UUID active = PREGNANCY_ACTIVE_INSTANCE_BY_PLAYER.remove(player.getUuid());
        if (active != null) {
            PREGNANCY_HATCH_BY_INSTANCE.remove(active);
            PREGNANCY_CUE_RUNTIME_BY_INSTANCE.remove(active);
        }
        if ((class_32182 = player.getEntityWorld()) instanceof ServerWorld) {
            ServerWorld world = class_32182;
            l = NeedsOfNature.getGlobalTick(world);
        } else {
            l = 0L;
        }
        long nowTick = l;
        long remaining = Math.max(1L, endTick - nowTick);
        int duration = (int)Math.min(Integer.MAX_VALUE, remaining);
        player.addStatusEffect(new StatusEffectInstance(Registries.STATUS_EFFECT.getEntry((Object)NonStatusEffects.PREGNANT), duration, 0, false, false, true));
    }

    static void markPregnancyPending(ServerPlayerEntity player, long nowTick) {
        if (player == null) {
            return;
        }
        if (NonGenderSystem.isOnlyMale((Entity)player)) {
            NeedsOfNature.clearPregnancyState(player, true);
            return;
        }
        PregnancyHolder holder = NeedsOfNature.getPregnancyHolder(player);
        if (holder == null) {
            return;
        }
        Identifier entityTypeId = holder.getPregnantEntityTypeId();
        if (entityTypeId == null) {
            return;
        }
        holder.setPregnancyState(entityTypeId, Math.max(0L, nowTick), true, holder.getPregnancyVariantData());
        NeedsOfNature.refreshPendingPregnancyEffect(player);
        NonApiInternals.firePregnancyChanged(player);
    }

    private static void refreshPendingPregnancyEffect(ServerPlayerEntity player) {
        if (player == null) {
            return;
        }
        RegistryEntry effect = Registries.STATUS_EFFECT.getEntry((Object)NonStatusEffects.PREGNANT);
        StatusEffectInstance current = player.getStatusEffect(effect);
        if (current != null && current.getDuration() >= 21) {
            return;
        }
        player.addStatusEffect(new StatusEffectInstance(effect, 21, 0, false, false, true));
    }

    @Nullable
    private static ServerPlayerEntity findPlayerOnWorldServer(ServerWorld world, UUID playerUuid) {
        if (world == null || playerUuid == null) {
            return null;
        }
        MinecraftServer server = world.getServer();
        if (server == null) {
            return null;
        }
        return server.getPlayerManager().getPlayer(playerUuid);
    }

    static void clearPregnancyState(ServerPlayerEntity player, boolean removeEffect) {
        if (player == null) {
            return;
        }
        PregnancyHolder holder = NeedsOfNature.getPregnancyHolder(player);
        if (holder != null) {
            holder.clearPregnancyState();
        }
        NeedsOfNature.clearPregnancyRuntime(player.getUuid());
        if (removeEffect) {
            player.removeStatusEffect(Registries.STATUS_EFFECT.getEntry((Object)NonStatusEffects.PREGNANT));
        }
        NeedsOfNature.syncPregnancyState(player);
        NonApiInternals.firePregnancyChanged(player);
    }

    private static void clearPregnancyRuntime(UUID playerUuid) {
        if (playerUuid == null) {
            return;
        }
        PREGNANCY_START_RETRY_TICKS.remove(playerUuid);
        PREGNANCY_QUEUED_PLAYERS.remove(playerUuid);
        UUID active = PREGNANCY_ACTIVE_INSTANCE_BY_PLAYER.remove(playerUuid);
        if (active != null) {
            PREGNANCY_HATCH_BY_INSTANCE.remove(active);
            PREGNANCY_CUE_RUNTIME_BY_INSTANCE.remove(active);
        }
    }

    private static void refreshPregnancyAfterDeathCopy(ServerPlayerEntity player) {
        long nowTick;
        PregnancyHolder holder = NeedsOfNature.getPregnancyHolder(player);
        if (holder == null || holder.getPregnantEntityTypeId() == null) {
            return;
        }
        ServerWorld class_32182 = player.getEntityWorld();
        if (class_32182 instanceof ServerWorld) {
            ServerWorld world = class_32182;
            v0 = NeedsOfNature.getGlobalTick(world);
        } else {
            v0 = nowTick = 0L;
        }
        if (holder.isPregnancyPendingHatch() || holder.getPregnancyEndTick() <= nowTick) {
            NeedsOfNature.markPregnancyPending(player, nowTick);
            return;
        }
        long remaining = Math.max(1L, holder.getPregnancyEndTick() - nowTick);
        int duration = (int)Math.min(Integer.MAX_VALUE, remaining);
        player.addStatusEffect(new StatusEffectInstance(Registries.STATUS_EFFECT.getEntry((Object)NonStatusEffects.PREGNANT), duration, 0, false, false, true));
    }

    public static void clearPregnancyOnDeath(ServerPlayerEntity player) {
        NeedsOfNature.clearPregnancyState(player, true);
    }

    private void registerAttackStopRequests() {
        ServerPlayNetworking.registerGlobalReceiver(StopAttackAnimationC2SPayload.ID, (payload, context) -> context.server().execute(() -> {
            boolean actorListed;
            ServerPlayerEntity player = context.player();
            ServerWorld patt0$temp = player.getEntityWorld();
            if (!(patt0$temp instanceof ServerWorld)) {
                return;
            }
            ServerWorld world = patt0$temp;
            UUID instanceId = payload.instanceId();
            InstanceControl control = INSTANCE_CONTROLS.get(instanceId);
            if (control == null || control.voluntary() || !control.hasPlayer()) {
                return;
            }
            List<UUID> actors = INSTANCE_ACTORS.get(instanceId);
            PlayerActiveInstance active = ACTIVE_PLAYER_INSTANCES.get(player.getUuid());
            boolean trackedActive = active != null && active.instanceId().equals(instanceId) && active.worldKey().equals((Object)world.getRegistryKey());
            boolean bl = actorListed = actors != null && actors.contains(player.getUuid());
            if (!trackedActive && !actorListed) {
                return;
            }
            boolean defeatedInstance = NeedsOfNature.isDefeatedInstance(world, instanceId);
            int escapeInvulnerabilityTicks = NeedsOfNature.getConfig().getAttackEscapeInvulnerabilityTicks();
            int escapeAnimationProtectionTicks = NeedsOfNature.getConfig().getAttackEscapeAnimationProtectionTicks();
            if (defeatedInstance && escapeInvulnerabilityTicks > 0) {
                NeedsOfNature.setPostEscapeDamageInvulnerable(player, escapeInvulnerabilityTicks);
            }
            NeedsOfNature.setAnimationStartProtection(player, escapeAnimationProtectionTicks);
            PeakState peakState = PEAK_STATES.get(instanceId);
            boolean allowReattack = peakState == null || !peakState.peaked;
            NeedsOfNature.clearQueuedAnimations(world, player);
            if (!defeatedInstance) {
                NeedsOfNature.scheduleAttackEscapeResolution(world, player, actors, allowReattack, escapeInvulnerabilityTicks);
                NonStats.incrementAttackEscapeSuccess(player);
                NonAdvancementHooks.resetTossedAround(player);
            }
            AfwAnimationApi.stopAnimation((ServerWorld)world, (UUID)instanceId);
            if (defeatedInstance && allowReattack) {
                NeedsOfNature.requestOriginalAttackers(world, player, actors);
            }
        }));
    }

    static {
        PEAK_STATES = new ConcurrentHashMap<UUID, PeakState>();
        PLAYER_ENTITY_TYPE_ID = Identifier.of((String)"minecraft", (String)"player");
        INSTANCE_CONTROLS = new ConcurrentHashMap<UUID, InstanceControl>();
        INSTANCE_ACTORS = new ConcurrentHashMap<UUID, List<UUID>>();
        ATTACK_JOIN_CARRYOVER = new ConcurrentHashMap<UUID, AttackJoinCarryover>();
        PENDING_GATHER_PAIRS = new ConcurrentHashMap<GatherPairKey, GatherPairState>();
        NON_PENDING_GATHER_ACTORS = new ConcurrentHashMap<UUID, NoNGatherActorState>();
        AFW_ANIMATION_TAG_DEFEAT_FALLBACKS = List.of(AFW_ANIMATION_TAG_DEFEAT_ON_BELLY, AFW_ANIMATION_TAG_DEFEAT_ON_BACK, AFW_ANIMATION_TAG_DEFEAT_SIDE);
        DEFEAT_POSE_TAG_BY_CONTENT_TAG = Map.ofEntries(Map.entry("missionary", AFW_ANIMATION_TAG_DEFEAT_ON_BACK), Map.entry("on_back", AFW_ANIMATION_TAG_DEFEAT_ON_BACK), Map.entry("spooning", AFW_ANIMATION_TAG_DEFEAT_SIDE), Map.entry("side", AFW_ANIMATION_TAG_DEFEAT_SIDE), Map.entry("from_behind", AFW_ANIMATION_TAG_DEFEAT_ON_BELLY), Map.entry("doggy", AFW_ANIMATION_TAG_DEFEAT_ON_BELLY), Map.entry("on_belly", AFW_ANIMATION_TAG_DEFEAT_ON_BELLY));
        DEFEATED_QUEUED_INSTANCES = ConcurrentHashMap.newKeySet();
        LAST_DEFEATED_COOLDOWN_UNTIL_BY_PLAYER = new ConcurrentHashMap<UUID, Long>();
        POST_ESCAPE_DAMAGE_INVULNERABLE_UNTIL_BY_PLAYER = new ConcurrentHashMap<UUID, Long>();
        PENDING_ATTACK_ESCAPE_RESOLUTIONS = new ConcurrentHashMap<UUID, PendingAttackEscapeResolution>();
        PREGNANCY_VARIANT_COMPONENTS = List.of(NeedsOfNature.registryVariant("wolf_variant", DataComponentTypes.WOLF_VARIANT, RegistryKeys.WOLF_VARIANT), NeedsOfNature.registryVariant("wolf_sound_variant", DataComponentTypes.WOLF_SOUND_VARIANT, RegistryKeys.WOLF_SOUND_VARIANT), NeedsOfNature.registryVariant("pig_variant", DataComponentTypes.PIG_VARIANT, RegistryKeys.PIG_VARIANT), NeedsOfNature.registryVariant("cow_variant", DataComponentTypes.COW_VARIANT, RegistryKeys.COW_VARIANT), NeedsOfNature.lazyRegistryVariant("chicken_variant", DataComponentTypes.CHICKEN_VARIANT, RegistryKeys.CHICKEN_VARIANT), NeedsOfNature.registryVariant("frog_variant", DataComponentTypes.FROG_VARIANT, RegistryKeys.FROG_VARIANT), NeedsOfNature.registryVariant("cat_variant", DataComponentTypes.CAT_VARIANT, RegistryKeys.CAT_VARIANT), NeedsOfNature.lazyRegistryVariant("zombie_nautilus_variant", DataComponentTypes.ZOMBIE_NAUTILUS_VARIANT, RegistryKeys.ZOMBIE_NAUTILUS_VARIANT), NeedsOfNature.enumVariant("fox_variant", DataComponentTypes.FOX_VARIANT, FoxEntity.Variant.class), NeedsOfNature.enumVariant("salmon_size", DataComponentTypes.SALMON_SIZE, SalmonEntity.Variant.class), NeedsOfNature.enumVariant("parrot_variant", DataComponentTypes.PARROT_VARIANT, ParrotEntity.Variant.class), NeedsOfNature.enumVariant("mooshroom_variant", DataComponentTypes.MOOSHROOM_VARIANT, MooshroomEntity.Variant.class), NeedsOfNature.enumVariant("rabbit_variant", DataComponentTypes.RABBIT_VARIANT, RabbitEntity.Variant.class), NeedsOfNature.enumVariant("horse_variant", DataComponentTypes.HORSE_VARIANT, HorseColor.class), NeedsOfNature.enumVariant("llama_variant", DataComponentTypes.LLAMA_VARIANT, LlamaEntity.Variant.class), NeedsOfNature.enumVariant("axolotl_variant", DataComponentTypes.AXOLOTL_VARIANT, AxolotlEntity.Variant.class), NeedsOfNature.enumVariant("sheep_color", DataComponentTypes.SHEEP_COLOR, DyeColor.class), NeedsOfNature.enumVariant("tropical_fish_base_color", DataComponentTypes.TROPICAL_FISH_BASE_COLOR, DyeColor.class), NeedsOfNature.enumVariant("tropical_fish_pattern_color", DataComponentTypes.TROPICAL_FISH_PATTERN_COLOR, DyeColor.class));
        PEAK_LIQUID_PROGRESS = new ConcurrentHashMap<UUID, PeakLiquidProgressState>();
        FILL_BOTTLE_STATES = new ConcurrentHashMap<UUID, FillBottleState>();
        BREEDING_PENDING_BY_PAIR = new ConcurrentHashMap<BreedingPairKey, PendingBreedingState>();
        BREEDING_PENDING_BY_REQUEST = new ConcurrentHashMap<String, BreedingPairKey>();
        BREEDING_ACTIVE_BY_INSTANCE = new ConcurrentHashMap<UUID, ActiveBreedingState>();
        BREEDING_ACTIVE_INSTANCE_BY_PAIR = new ConcurrentHashMap<BreedingPairKey, UUID>();
        PREGNANCY_HATCH_BY_INSTANCE = new ConcurrentHashMap<UUID, PregnancyHatchState>();
        PREGNANCY_ACTIVE_INSTANCE_BY_PLAYER = new ConcurrentHashMap<UUID, UUID>();
        PREGNANCY_QUEUED_PLAYERS = ConcurrentHashMap.newKeySet();
        PREGNANCY_START_RETRY_TICKS = new ConcurrentHashMap<UUID, Long>();
        PREGNANCY_CUE_RUNTIME_BY_INSTANCE = new ConcurrentHashMap<UUID, PregnancyCueRuntime>();
        REFLECTION_FAILURE_LOGS = ConcurrentHashMap.newKeySet();
        RECEIVER_DRIP = new ConcurrentHashMap<UUID, ReceiverDripState>();
        PENDING_M_INJECTOR_REWARDS = new ConcurrentHashMap<UUID, PendingMInjectorReward>();
        RECEIVER_DRIP_OFFSET_DEFAULT = new Vec3d(0.0, 0.0, -0.5);
        RECEIVER_DRIP_OFFSETS = Map.of(Identifier.of((String)"minecraft", (String)"wolf"), new Vec3d(0.0, 0.0, -0.5));
    }

    private record PlayerActiveInstance(UUID instanceId, RegistryKey<World> worldKey) {
    }

    public record PlayerEnergyAuraDebug(ServerPlayerEntity player, double totalMultiplier, int playerEnergy, int destroyedSkinStage, int messTotal, double radius) {
    }

    private record MessSnapshot(int v, int a, int m) {
        static void copy(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer) {
            MessHolder oldHolder;
            block3: {
                block2: {
                    if (!(oldPlayer instanceof MessHolder)) break block2;
                    oldHolder = (MessHolder)oldPlayer;
                    if (newPlayer instanceof MessHolder) break block3;
                }
                return;
            }
            MessHolder newHolder = (MessHolder)newPlayer;
            MessSnapshot snapshot = new MessSnapshot(oldHolder.getVMess(), oldHolder.getAMess(), oldHolder.getMMess());
            newHolder.setVMess(snapshot.v());
            newHolder.setAMess(snapshot.a());
            newHolder.setMMess(snapshot.m());
        }
    }

    private record RippedSkinSnapshot(int stage, int damage) {
        static void copy(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer) {
            DestroyedSkinHolder oldHolder;
            block3: {
                block2: {
                    if (!(oldPlayer instanceof DestroyedSkinHolder)) break block2;
                    oldHolder = (DestroyedSkinHolder)oldPlayer;
                    if (newPlayer instanceof DestroyedSkinHolder) break block3;
                }
                return;
            }
            DestroyedSkinHolder newHolder = (DestroyedSkinHolder)newPlayer;
            RippedSkinSnapshot snapshot = new RippedSkinSnapshot(oldHolder.getDestroyedSkinStage(), oldHolder.getDestroyedSkinDamage());
            newHolder.setDestroyedSkinDamage(snapshot.damage());
            NonDestroyedSkinSystem.setDestroyedSkinStage(newPlayer, snapshot.stage());
        }
    }

    private record LiquidSnapshot(int stored, LiquidHolder.LiquidComposition composition, @Nullable Identifier entityTypeId) {
        static void copy(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer) {
            LiquidHolder oldHolder;
            block9: {
                block8: {
                    if (!(oldPlayer instanceof LiquidHolder)) break block8;
                    oldHolder = (LiquidHolder)oldPlayer;
                    if (newPlayer instanceof LiquidHolder) break block9;
                }
                return;
            }
            LiquidHolder newHolder = (LiquidHolder)newPlayer;
            LiquidSnapshot snapshot = new LiquidSnapshot(oldHolder.getLiquidStored(), oldHolder.getLiquidComposition(), oldHolder.getLiquidEntityTypeId());
            newHolder.setLiquidStored(snapshot.stored());
            if (newHolder.getLiquidStored() <= 0) {
                newHolder.clearLiquidComposition();
                return;
            }
            if (snapshot.composition() == LiquidHolder.LiquidComposition.ENTITY && snapshot.entityTypeId() != null) {
                newHolder.setLiquidCompositionEntity(snapshot.entityTypeId());
            } else if (snapshot.composition() == LiquidHolder.LiquidComposition.MIXED) {
                newHolder.setLiquidCompositionMixed();
            } else {
                newHolder.clearLiquidComposition();
            }
        }
    }

    private record PregnancySnapshot(Identifier entityTypeId, long endTick, boolean pendingHatch, @Nullable PregnancyVariantData variantData, int offspringCount) {
        static void copy(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer) {
            PregnancyHolder oldHolder = NeedsOfNature.getPregnancyHolder(oldPlayer);
            PregnancyHolder newHolder = NeedsOfNature.getPregnancyHolder(newPlayer);
            if (oldHolder == null || newHolder == null || oldHolder.getPregnantEntityTypeId() == null) {
                return;
            }
            if (NonGenderSystem.isOnlyMale((Entity)newPlayer)) {
                NeedsOfNature.clearPregnancyState(newPlayer, true);
                return;
            }
            PregnancySnapshot snapshot = new PregnancySnapshot(oldHolder.getPregnantEntityTypeId(), oldHolder.getPregnancyEndTick(), oldHolder.isPregnancyPendingHatch(), oldHolder.getPregnancyVariantData(), oldHolder.getPregnancyOffspringCount());
            NeedsOfNature.clearPregnancyRuntime(newPlayer.getUuid());
            newHolder.setPregnancyState(snapshot.entityTypeId(), snapshot.endTick(), snapshot.pendingHatch(), snapshot.variantData());
            newHolder.setPregnancyOffspringCount(snapshot.offspringCount());
            NeedsOfNature.refreshPregnancyAfterDeathCopy(newPlayer);
            NeedsOfNature.syncPregnancyState(newPlayer);
            NonApiInternals.firePregnancyChanged(newPlayer);
        }
    }

    private record PostDeathStateSync(RegistryKey<World> worldKey, long dueTick) {
    }

    private record InstanceControl(Identifier animationId, boolean voluntary, boolean hasPlayer, AfwDamageBehavior damageBehavior, boolean ignoreAttackers, @Nullable String mode) {
        boolean isPregnancyMode() {
            return NeedsOfNature.NON_MODE_PREGNANCY.equalsIgnoreCase(this.mode);
        }
    }

    private record LiquidGainProfile(int amount, int xpEligibleAmount, LiquidHolder.LiquidComposition composition, Identifier entityTypeId, Map<Identifier, Integer> entityAmounts) {
    }

    private record PendingMInjectorReward(RegistryKey<World> worldKey, long freeSinceTick) {
    }

    private static final class PeakLiquidProgressState {
        final RegistryKey<World> worldKey;
        final Identifier animationId;
        final List<UUID> actorUuids;
        final List<String> actorKeys;
        final LiquidGainProfile gainProfile;
        final List<Long> scheduledTicks;
        final List<Integer> scheduledAmounts;
        int scheduleIndex;
        final boolean infinite;
        final double cycleTicks;
        final List<Integer> cueOffsets;
        final long stageStartTick;
        final double stageSpeed;
        long cycleIndex;
        int cueIndex;
        int remainingPerReceiverMl;

        private PeakLiquidProgressState(RegistryKey<World> worldKey, Identifier animationId, List<UUID> actorUuids, List<String> actorKeys, LiquidGainProfile gainProfile, List<Long> scheduledTicks, List<Integer> scheduledAmounts, boolean infinite, double cycleTicks, List<Integer> cueOffsets, long stageStartTick, double stageSpeed, int remainingPerReceiverMl) {
            this.worldKey = worldKey;
            this.animationId = animationId;
            this.actorUuids = actorUuids;
            this.actorKeys = actorKeys;
            this.gainProfile = gainProfile;
            this.scheduledTicks = scheduledTicks;
            this.scheduledAmounts = scheduledAmounts;
            this.scheduleIndex = 0;
            this.infinite = infinite;
            this.cycleTicks = cycleTicks;
            this.cueOffsets = cueOffsets;
            this.stageStartTick = stageStartTick;
            this.stageSpeed = stageSpeed;
            this.cycleIndex = 0L;
            this.cueIndex = 0;
            this.remainingPerReceiverMl = remainingPerReceiverMl;
        }

        static PeakLiquidProgressState finite(RegistryKey<World> worldKey, Identifier animationId, List<UUID> actorUuids, List<String> actorKeys, LiquidGainProfile gainProfile, List<Long> scheduledTicks, List<Integer> scheduledAmounts) {
            return new PeakLiquidProgressState(worldKey, animationId, actorUuids, actorKeys, gainProfile, scheduledTicks, scheduledAmounts, false, 0.0, List.of(), 0L, 1.0, 0);
        }

        static PeakLiquidProgressState infinite(RegistryKey<World> worldKey, Identifier animationId, List<UUID> actorUuids, List<String> actorKeys, LiquidGainProfile gainProfile, double cycleTicks, List<Integer> cueOffsets, long stageStartTick, double stageSpeed) {
            return new PeakLiquidProgressState(worldKey, animationId, actorUuids, actorKeys, gainProfile, List.of(), List.of(), true, cycleTicks, cueOffsets, stageStartTick, stageSpeed, gainProfile.amount());
        }
    }

    private record FinitePulseSchedule(List<Long> ticks, List<Integer> amounts) {
    }

    private record PeakState(Identifier animationId, int peakStage, boolean peaked, int lastStage, boolean hasPlayer, boolean liquidGranted, int actualAddedMl) {
    }

    private record FillBottleState(UUID playerUuid, boolean reservedBottle) {
    }

    private record NoNGatherActorState(RegistryKey<World> worldKey, long expiresTick) {
    }

    private record BreedingPairKey(UUID firstUuid, UUID secondUuid) {
    }

    public record AfwMatchedAnimation(Identifier animationId, List<String> actorKeys, List<AnimationStageInfo> stages) {
    }

    private record PendingBreedingState(RegistryKey<World> worldKey, UUID firstUuid, UUID secondUuid, long expiresTick) {
    }

    private record ActiveBreedingState(RegistryKey<World> worldKey, UUID firstUuid, UUID secondUuid) {
    }

    public record PregnancyVariantData(Map<String, String> components, @Nullable Integer horseMarkingIndex) {
        public PregnancyVariantData(Map<String, String> components, @Nullable Integer horseMarkingIndex) {
            components = components == null || components.isEmpty() ? Map.of() : Map.copyOf(components);
        }

        public boolean isEmpty() {
            return this.components.isEmpty() && this.horseMarkingIndex == null;
        }
    }

    private record PregnancyHatchState(UUID playerUuid, Identifier sourceEntityTypeId, Identifier birthEntityTypeId, String birthMode, int totalSpawnCount, int remainingSpawns, int spawnedCount, @Nullable PregnancyVariantData variantData) {
        PregnancyHatchState withProgress(int newRemainingSpawns, int newSpawnedCount) {
            return new PregnancyHatchState(this.playerUuid, this.sourceEntityTypeId, this.birthEntityTypeId, this.birthMode, this.totalSpawnCount, Math.max(0, newRemainingSpawns), Math.max(0, newSpawnedCount), this.variantData);
        }
    }

    private record GatherPairKey(UUID mobUuid, UUID playerUuid) {
    }

    private record GatherPairState(RegistryKey<World> worldKey, long expiresTick) {
    }

    public static enum AttackBlockReason {
        PREGNANCY_FRIENDLY,
        GATHERING_PAIR,
        ENERGY_ABOVE_70_PLAYER_BUSY;

    }

    private record AttackJoinCarryover(RegistryKey<World> worldKey, Set<UUID> actors, long expiresTick) {
    }

    private record PendingSetupChatMessage(Text message, Set<UUID> sentTo) {
    }

    private record ScanBudget(long tick, int remaining) {
    }

    private record LastDefeatedCandidate(MobEntity mob, NonGatherSystem.GatherCandidate gatherCandidate, AfwMatchedAnimation defeatMatch) {
    }

    private record ReceiverDripState(RegistryKey<World> worldKey, long nextTick, long endTick) {
    }

    private record PendingAttackEscapeResolution(RegistryKey<World> worldKey, List<UUID> actorUuids, long dueTick, boolean allowReattack, int invulnerabilityTicks) {
    }

    record PregnancyDonor(Identifier entityTypeId, @Nullable PregnancyVariantData variantData) {
    }

    private static interface VariantComponentDescriptor {
        public String key();

        public void capture(ServerWorld var1, Entity var2, Map<String, String> var3);

        public void apply(ServerWorld var1, Entity var2, String var3);
    }

    private record PregnancyCueRuntime(RegistryKey<World> worldKey, long stageStartTick, Identifier clipId, Integer cueTick, boolean cueTriggered, double stageSpeed) {
    }

    private record LoopAdvance(RegistryKey<World> worldKey, long nextTick) {
    }
}

