/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.afwid.api.AfwGeckoModelEvents
 *  com.afwid.api.AfwGeckoModelEvents$BoneItemProp
 *  com.afwid.api.AfwGeckoModelEvents$RenderOverride
 *  com.afwid.api.AfwSoundCueEvents
 *  com.afwid.api.AfwSoundCueEvents$SoundContext
 *  com.afwid.api.AfwSoundCueEvents$SoundOverride
 *  com.afwid.client.render.gecko.AfwGeckoResourceResolver
 *  com.afwid.client.runtime.AfwClientAnimationRuntime
 *  com.afwid.mixin.client.EntityRenderManagerAccessor
 *  com.afwid.network.AnimationStageInfo
 *  com.google.gson.Gson
 *  net.fabricmc.api.ClientModInitializer
 *  net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
 *  net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
 *  net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
 *  net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
 *  net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
 *  net.minecraft.client.texture.NativeImage
 *  net.minecraft.client.render.entity.equipment.EquipmentModel
 *  net.minecraft.client.render.entity.equipment.EquipmentModel$Dyeable
 *  net.minecraft.client.render.entity.equipment.EquipmentModel$Layer
 *  net.minecraft.client.render.entity.equipment.EquipmentModel$LayerType
 *  net.minecraft.item.equipment.EquipmentAssetKeys
 *  net.minecraft.component.type.EquippableComponent
 *  net.minecraft.client.render.entity.equipment.EquipmentModelLoader
 *  net.minecraft.item.equipment.EquipmentAsset
 *  net.minecraft.client.texture.NativeImageBackedTexture
 *  net.minecraft.client.texture.AbstractTexture
 *  net.minecraft.util.Formatting
 *  net.minecraft.entity.EntityType
 *  net.minecraft.entity.EquipmentSlot
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.passive.SheepEntity
 *  net.minecraft.entity.passive.AbstractDonkeyEntity
 *  net.minecraft.entity.passive.WolfEntity
 *  net.minecraft.entity.passive.AbstractHorseEntity
 *  net.minecraft.entity.passive.HorseEntity
 *  net.minecraft.entity.player.PlayerModelPart
 *  net.minecraft.util.DyeColor
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.component.type.PotionContentsComponent
 *  net.minecraft.item.ItemConvertible
 *  net.minecraft.particle.ParticleType
 *  net.minecraft.text.Text
 *  net.minecraft.util.Identifier
 *  net.minecraft.client.option.KeyBinding
 *  net.minecraft.client.option.KeyBinding$Category
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.resource.Resource
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.client.util.InputUtil$Type
 *  net.minecraft.client.gui.screen.Screen
 *  net.minecraft.entity.passive.HorseMarking
 *  net.minecraft.registry.RegistryKey
 *  net.minecraft.client.render.entity.EntityRendererFactories
 *  net.minecraft.client.network.AbstractClientPlayerEntity
 *  net.minecraft.client.network.ClientPlayerEntity
 *  net.minecraft.registry.Registries
 *  net.minecraft.entity.player.SkinTextures
 *  net.minecraft.network.packet.CustomPayload
 *  net.minecraft.client.render.entity.EntityRenderManager
 *  net.minecraft.component.type.DyedColorComponent
 *  net.minecraft.component.DataComponentTypes
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid;

import com.afwid.api.AfwGeckoModelEvents;
import com.afwid.api.AfwSoundCueEvents;
import com.afwid.client.render.gecko.AfwGeckoResourceResolver;
import com.afwid.client.runtime.AfwClientAnimationRuntime;
import com.afwid.network.AnimationStageInfo;
import com.google.gson.Gson;
import com.nonid.GenderHolder;
import com.nonid.LiquidHolder;
import com.nonid.NeedsOfNature;
import com.nonid.NonAccessoryShedSystem;
import com.nonid.NonConfig;
import com.nonid.NonDebugChatCategory;
import com.nonid.client.HorseLiquidCollectorGeoModel;
import com.nonid.client.HorseLiquidCollectorRenderer;
import com.nonid.client.NonAccessoryTooltips;
import com.nonid.client.NonArmorShedClient;
import com.nonid.client.NonDebugSpinMode;
import com.nonid.client.NonDebugSpinMusicClient;
import com.nonid.client.NonDestroyedSkinClient;
import com.nonid.client.NonClientNetworking;
import com.nonid.client.NonGenderSelectionScreen;
import com.nonid.client.NonHudOverlay;
import com.nonid.client.NonPackStartupWarnings;
import com.nonid.client.NonPregnancyClientState;
import com.nonid.client.NonSoundCues;
import com.nonid.client.NonSpinModelLock;
import com.nonid.client.NonWildfireGenderScreenHooks;
import com.nonid.client.NonWildfireGenderSync;
import com.nonid.client.PregnancyEggRenderer;
import com.nonid.client.intiface.NonIntifaceBridge;
import com.nonid.client.particle.LiquidFallingParticle;
import com.nonid.client.particle.LiquidParticle;
import com.nonid.client.particle.LiquidPuddleParticle;
import com.nonid.client.particle.LiquidWaterParticle;
import com.nonid.client.particle.RippedFabricParticle;
import com.nonid.client.particle.SmallHeartParticle;
import com.nonid.data.NonLoopSecondsOverrides;
import com.nonid.data.NonPeakStages;
import com.nonid.data.NonPregnancyCues;
import com.nonid.entity.HorseLiquidCollectorEntity;
import com.nonid.network.AccessoryShedStateS2CPayload;
import com.nonid.network.AttackStateS2CPayload;
import com.nonid.network.DestroyedSkinMaskSyncS2CPayload;
import com.nonid.network.DestroyedSkinParticlesS2CPayload;
import com.nonid.network.DestroyedSkinStateS2CPayload;
import com.nonid.network.DestroyedSkinSyncS2CPayload;
import com.nonid.network.GameplayRuntimeSettingsS2CPayload;
import com.nonid.network.GenderSelectionPromptS2CPayload;
import com.nonid.network.HostConfigSyncData;
import com.nonid.network.HostConfigSyncS2CPayload;
import com.nonid.network.LiquidStateS2CPayload;
import com.nonid.network.ManualPeakPropOverrideS2CPayload;
import com.nonid.network.MessStateS2CPayload;
import com.nonid.network.PregnancyStateS2CPayload;
import com.nonid.network.SetPlayerGenderC2SPayload;
import com.nonid.network.SkinRippedInfoRequestS2CPayload;
import com.nonid.network.StageProgressS2CPayload;
import com.nonid.network.StartManualPeakC2SPayload;
import com.nonid.network.StopAttackAnimationC2SPayload;
import com.nonid.network.StopVoluntaryAnimationC2SPayload;
import com.nonid.particle.NonParticles;
import com.nonid.potion.NonPotions;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.util.Formatting;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.util.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.HorseArmorItem;
import net.minecraft.potion.PotionUtil;
import net.minecraft.item.ItemConvertible;
import net.minecraft.particle.ParticleType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.passive.HorseMarking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.Nullable;

public class NeedsOfNatureClient
implements ClientModInitializer {
    private static final Gson HOST_CONFIG_SYNC_GSON = new Gson();
    private static final Identifier WOLF_COLLAR_TEXTURE = new Identifier("minecraft", "textures/entity/wolf/wolf_collar.png");
    private static final Identifier SHEEP_WOOL_TEXTURE = new Identifier("minecraft", "textures/entity/sheep/sheep_fur.png");
    private static final Identifier FILL_BOTTLE_ANIMATION_ID = Identifier.of((String)"animationframework", (String)"player_fill_bottle");
    private static final String FILL_BOTTLE_PROP_CUE_EFFECT = "fillbottle_prop_fill";
    private static final Identifier VANILLA_BOTTLE_FILL_SOUND = new Identifier("minecraft", "item.bottle.fill");
    private static final String RIGHT_PROP_BONE = "propright";
    private static final float REACTIVE_IMPACT_FOV_DELTA = 0.5f;
    private static final float REACTIVE_IMPACT_HOLD_TICKS = 3.0f;
    private static final float REACTIVE_IMPACT_RECOVER_TICKS = 14.0f;
    private static final double NANOS_PER_CLIENT_TICK = 5.0E7;
    private static final Set<UUID> FILLED_BOTTLE_PROP_INSTANCES = new HashSet<UUID>();
    private static final Set<UUID> INTIFACE_BIRTH_CUTOFF_INSTANCES = new HashSet<UUID>();
    private static final Map<UUID, PendingManualPeakPropOverride> PENDING_MANUAL_PEAK_PROP_OVERRIDES = new HashMap<UUID, PendingManualPeakPropOverride>();
    private static volatile Method AFW_RIGHT_PROP_OVERRIDE_METHOD = null;
    private static volatile boolean AFW_RIGHT_PROP_OVERRIDE_LOOKED_UP = false;
    private static long CLIENT_FALLBACK_TICKS = 0L;
    private static volatile long REACTIVE_IMPACT_PULSE_START_NANOS = Long.MIN_VALUE;
    private static int LAST_LOCAL_GENDER_SYNC_MASK = 0;
    private static final Map<Integer, Identifier> WOLF_COLLAR_TINT_CACHE = new HashMap<Integer, Identifier>();
    private static final Map<Integer, Identifier> SHEEP_WOOL_TINT_CACHE = new HashMap<Integer, Identifier>();
    private static final Map<Integer, Identifier> HORSE_ARMOR_TINT_CACHE = new HashMap<Integer, Identifier>();
    private static final Set<String> SHEEP_WOOL_BONES = Set.of("bodywool", "headwool", "leg1wool", "leg2wool", "leg3wool", "leg4wool");
    private static final int PLAYER_OUTER_SKIN_CUBE_INDEX = 1;
    private static final String NON_KEY_CATEGORY = "key.categories.needsofnature.main";
    private static final KeyBinding START_MANUAL_PEAK_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.needsofnature.start_manual_peak", InputUtil.Type.KEYSYM, 77, NON_KEY_CATEGORY));

    public static void sendClientSetupWarning(String message) {
        if (message == null || message.isBlank()) {
            return;
        }
        if (!NeedsOfNature.getConfig().allowsDebugChat(NonDebugChatCategory.SETUP)) {
            return;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) {
            return;
        }
        client.player.sendMessage((Text)Text.translatable((String)"debug.needsofnature.prefix", (Object[])new Object[]{Text.literal((String)message)}).formatted(Formatting.YELLOW), false);
    }

    public void onInitializeClient() {
        EntityRendererRegistry.register(NeedsOfNature.HORSE_LIQUID_COLLECTOR_ENTITY_TYPE, HorseLiquidCollectorRenderer::new);
        EntityRendererRegistry.register(NeedsOfNature.PREGNANCY_EGG_ENTITY_TYPE, PregnancyEggRenderer::new);
        ClientTickEvents.END_CLIENT_TICK.register(NonHudOverlay::clientTick);
        ClientTickEvents.END_CLIENT_TICK.register(NonSpinModelLock::clientTick);
        ClientTickEvents.END_CLIENT_TICK.register(NonDebugSpinMusicClient::clientTick);
        ClientTickEvents.END_CLIENT_TICK.register(NonPackStartupWarnings::clientTick);
        ClientTickEvents.END_CLIENT_TICK.register(NonDestroyedSkinClient::clientTick);
        ClientTickEvents.END_CLIENT_TICK.register(NonArmorShedClient::clientTick);
        NonAccessoryTooltips.register();
        NonWildfireGenderScreenHooks.register();
        NonArmorShedClient.register();
        ClientTickEvents.END_CLIENT_TICK.register(client -> ++CLIENT_FALLBACK_TICKS);
        ClientTickEvents.END_CLIENT_TICK.register(NeedsOfNatureClient::pruneFilledBottlePropInstances);
        ClientTickEvents.END_CLIENT_TICK.register(NeedsOfNatureClient::applyPendingManualPeakPropOverrides);
        ClientTickEvents.END_CLIENT_TICK.register(NeedsOfNatureClient::syncLocalGenderMirror);
        ClientTickEvents.END_CLIENT_TICK.register(NeedsOfNatureClient::tickIntifaceBaseline);
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            NonIntifaceBridge.onWorldDisconnect(NeedsOfNature.getConfig());
            NonHudOverlay.onDisconnect(client);
            NonPregnancyClientState.clear();
            NonDestroyedSkinClient.clear();
            NonArmorShedClient.clear();
            NonAccessoryShedSystem.clearAll();
            NonPeakStages.clearSyncedPeakStages();
            FILLED_BOTTLE_PROP_INSTANCES.clear();
            NonDebugSpinMusicClient.stopAll(client);
            PENDING_MANUAL_PEAK_PROP_OVERRIDES.clear();
            REACTIVE_IMPACT_PULSE_START_NANOS = Long.MIN_VALUE;
            LAST_LOCAL_GENDER_SYNC_MASK = 0;
        });
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> client.execute(() -> {
            NeedsOfNatureClient.pushConfiguredPlayerGenderToServer();
            NonDestroyedSkinClient.scheduleLocalSkinUploadRetry();
            NonIntifaceBridge.onWorldJoin(NeedsOfNature.getConfig());
        }));
        ClientTickEvents.END_CLIENT_TICK.register(NeedsOfNatureClient::handleVoluntaryStopInput);
        ClientTickEvents.END_CLIENT_TICK.register(NeedsOfNatureClient::handleAttackInput);
        ClientTickEvents.END_CLIENT_TICK.register(NeedsOfNatureClient::handleManualPeakInput);
        NonClientNetworking.register(AttackStateS2CPayload.ID, AttackStateS2CPayload::read, (client, payload) -> NonHudOverlay.setEscapeControlState(payload.instanceId(), payload.attack(), payload.escapable(), payload.escapeProfile()));
        NonClientNetworking.register(StageProgressS2CPayload.ID, StageProgressS2CPayload::read, (client, payload) -> NonHudOverlay.setStageProgressState(payload));
        NonClientNetworking.register(LiquidStateS2CPayload.ID, LiquidStateS2CPayload::read, (client, payload) -> {
            NonHudOverlay.setLiquidState(payload.stored(), payload.capacity(), payload.tintRgb());
            ClientPlayerEntity patt0$temp = client.player;
            if (patt0$temp instanceof LiquidHolder) {
                LiquidHolder holder = (LiquidHolder)patt0$temp;
                holder.setLiquidStored(payload.stored());
            }
        });
        NonClientNetworking.register(PregnancyStateS2CPayload.ID, PregnancyStateS2CPayload::read, (client, payload) -> {
            String raw = payload.pregnantEntityTypeId();
            Identifier id = raw == null || raw.isBlank() ? null : Identifier.tryParse((String)raw);
            NonPregnancyClientState.setPregnantEntityTypeId(id);
        });
        NonClientNetworking.register(GameplayRuntimeSettingsS2CPayload.ID, GameplayRuntimeSettingsS2CPayload::read, (client, payload) -> NonHudOverlay.setRuntimeGameplaySettings(payload.loopSeconds(), payload.peakLoopSeconds(), payload.attackEscapeHits(), payload.attackDecayPerSecond(), payload.attackEscapeDamageDifficultyPercent(), payload.attackCreativePlayers()));
        NonClientNetworking.register(GenderSelectionPromptS2CPayload.ID, GenderSelectionPromptS2CPayload::read, (client, payload) -> client.setScreen((Screen)new NonGenderSelectionScreen(payload.allowedMask(), payload.currentMask(), payload.permanent())));
        NonClientNetworking.register(HostConfigSyncS2CPayload.ID, HostConfigSyncS2CPayload::read, (client, payload) -> {
            String raw = payload.json();
            if (raw == null || raw.isBlank()) {
                return;
            }
            try {
                HostConfigSyncData data = (HostConfigSyncData)HOST_CONFIG_SYNC_GSON.fromJson(raw, HostConfigSyncData.class);
                if (data == null) {
                    return;
                }
                NonConfig config = NeedsOfNature.getConfig();
                if (client.getServer() == null) {
                    data.applyTo(config);
                }
                NonHudOverlay.setRuntimeGameplaySettings(data.loopProgressSeconds, data.peakLoopProgressSeconds, data.attackEscapeHits, data.attackDecayPerSecond, data.attackEscapeDamageDifficultyPercent, data.attackCreativePlayers);
            }
            catch (RuntimeException e) {
                NeedsOfNature.LOGGER.warn("[NoN] Failed to parse host config sync payload.", (Throwable)e);
            }
        });
        NonClientNetworking.register(DestroyedSkinSyncS2CPayload.ID, DestroyedSkinSyncS2CPayload::read, (client, payload) -> NonDestroyedSkinClient.setSkin(payload.playerUuid(), payload.stage(), payload.pngBytes()));
        NonClientNetworking.register(DestroyedSkinMaskSyncS2CPayload.ID, DestroyedSkinMaskSyncS2CPayload::read, (client, payload) -> NonDestroyedSkinClient.setMask(payload.playerUuid(), payload.stage(), payload.pngBytes()));
        NonClientNetworking.register(DestroyedSkinStateS2CPayload.ID, DestroyedSkinStateS2CPayload::read, (client, payload) -> NonDestroyedSkinClient.setStage(payload.playerUuid(), payload.stage()));
        NonClientNetworking.register(DestroyedSkinParticlesS2CPayload.ID, DestroyedSkinParticlesS2CPayload::read, (client, payload) -> NonDestroyedSkinClient.spawnRippedFabricParticles(payload.playerUuid(), payload.x(), payload.y(), payload.z(), payload.count(), payload.seed()));
        NonClientNetworking.register(MessStateS2CPayload.ID, MessStateS2CPayload::read, (client, payload) -> NonDestroyedSkinClient.setMess(payload.playerUuid(), payload.vMess(), payload.aMess(), payload.mMess(), payload.tintRgb(), payload.liquidStored(), payload.liquidCapacity()));
        NonClientNetworking.register(AccessoryShedStateS2CPayload.ID, AccessoryShedStateS2CPayload::read, (client, payload) -> NonAccessoryShedSystem.setClientShedState(payload.playerUuid(), payload.shedV(), payload.shedA(), payload.forcedV(), payload.forcedA()));
        NonClientNetworking.register(SkinRippedInfoRequestS2CPayload.ID, SkinRippedInfoRequestS2CPayload::read, (client, payload) -> NonDestroyedSkinClient.sendDetectedRippedSkinInfo());
        NonClientNetworking.register(ManualPeakPropOverrideS2CPayload.ID, ManualPeakPropOverrideS2CPayload::read, (client, payload) -> NeedsOfNatureClient.applyManualPeakPropOverride(payload));
        NeedsOfNatureClient.registerSoundCueResolver();
        ParticleFactoryRegistry.getInstance().register((ParticleType)NonParticles.LIQUID_PARTICLE, LiquidParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register((ParticleType)NonParticles.LIQUID_PARTICLE_FALLING, LiquidFallingParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register((ParticleType)NonParticles.LIQUID_PARTICLE_PUDDLE, LiquidPuddleParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register((ParticleType)NonParticles.LIQUID_PARTICLE_WATER, LiquidWaterParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register((ParticleType)NonParticles.SMALLHEART, SmallHeartParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register((ParticleType)NonParticles.RIPPED_FABRIC, RippedFabricParticle.Factory::new);
        AfwGeckoModelEvents.RESOLVE_RENDER.register((entity, entityTypeId, currentModel, currentTexture) -> {
            boolean isFemale;
            if (NonDebugSpinMode.isEnabled()) {
                return new AfwGeckoModelEvents.RenderOverride(NonDebugSpinMode.DEBUG_SPIN_MODEL_RESOURCE, NonDebugSpinMode.DEBUG_SPIN_TEXTURE_RESOURCE);
            }
            AfwGeckoModelEvents.RenderOverride collectorOverride = NeedsOfNatureClient.resolveCollectorRenderOverride(entity);
            if (collectorOverride != null) {
                return collectorOverride;
            }
            Identifier destroyedBaseTexture = NonDestroyedSkinClient.resolveBaseTexture(entity, NeedsOfNatureClient.resolveDestroyedSkinBaseTexture(entity, currentTexture));
            Identifier destroyedOverlayTexture = destroyedBaseTexture == null ? NonDestroyedSkinClient.resolveOverlayTexture(entity) : null;
            ArrayList<Identifier> layers = new ArrayList<Identifier>();
            if (destroyedOverlayTexture != null) {
                layers.add(destroyedOverlayTexture);
            }
            layers.addAll(NeedsOfNatureClient.resolveSheepOverlays(entity));
            layers.addAll(NeedsOfNatureClient.resolveHorseOverlays(entity));
            layers.addAll(NeedsOfNatureClient.resolveWolfOverlays(entity));
            List<Identifier> layerTextures = layers.isEmpty() ? List.of() : List.copyOf(layers);
            Map<String, AfwGeckoModelEvents.BoneItemProp> fallbackBoneItems = NeedsOfNatureClient.resolveFallbackBoneItems(entity);
            Map<String, Identifier> boneTextures = NeedsOfNatureClient.resolveSheepBoneTextures(entity);
            Map<String, Boolean> boneVisibility = NeedsOfNatureClient.mergeBooleanMaps(NeedsOfNatureClient.resolveDonkeyChestBoneVisibility(entity), NeedsOfNatureClient.resolveSheepBoneVisibility(entity));
            Map<String, Set<Integer>> hiddenBoneCubeIndices = NeedsOfNatureClient.resolvePlayerSkinPartHiddenCubeIndices(entity);
            boolean hasLayerTextures = !layerTextures.isEmpty();
            boolean hasBoneTextures = !boneTextures.isEmpty();
            boolean hasFallbackBoneItems = !fallbackBoneItems.isEmpty();
            boolean hasBoneVisibility = !boneVisibility.isEmpty();
            boolean hasHiddenBoneCubeIndices = !hiddenBoneCubeIndices.isEmpty();
            Identifier baseModel = currentModel;
            if (baseModel == null && entityTypeId != null) {
                baseModel = Identifier.of((String)"animationframework", (String)("entity/" + entityTypeId.getPath()));
            }
            Identifier defaultGenderModel = NeedsOfNatureClient.resolveExistingGenderedModel(baseModel, true);
            if (!(entity instanceof GenderHolder)) {
                if (!(defaultGenderModel != null || destroyedBaseTexture != null || hasLayerTextures || hasBoneTextures || hasFallbackBoneItems || hasBoneVisibility || hasHiddenBoneCubeIndices)) {
                    return null;
                }
                return NeedsOfNatureClient.createAfwRenderOverride(defaultGenderModel, destroyedBaseTexture, hasLayerTextures ? layerTextures : null, hasBoneTextures ? boneTextures : null, hasFallbackBoneItems ? fallbackBoneItems : null, hasBoneVisibility ? boneVisibility : null, hasHiddenBoneCubeIndices ? hiddenBoneCubeIndices : null);
            }
            GenderHolder holder = (GenderHolder)entity;
            int mask = holder.getGenderMask();
            boolean isMale = (mask & 1) != 0;
            boolean bl = isFemale = !isMale && (mask & 2) != 0;
            if (!isMale && !isFemale) {
                if (!(defaultGenderModel != null || destroyedBaseTexture != null || hasLayerTextures || hasBoneTextures || hasFallbackBoneItems || hasBoneVisibility || hasHiddenBoneCubeIndices)) {
                    return null;
                }
                return NeedsOfNatureClient.createAfwRenderOverride(defaultGenderModel, destroyedBaseTexture, hasLayerTextures ? layerTextures : null, hasBoneTextures ? boneTextures : null, hasFallbackBoneItems ? fallbackBoneItems : null, hasBoneVisibility ? boneVisibility : null, hasHiddenBoneCubeIndices ? hiddenBoneCubeIndices : null);
            }
            if (baseModel == null) {
                if (!(destroyedBaseTexture != null || hasLayerTextures || hasBoneTextures || hasFallbackBoneItems || hasBoneVisibility || hasHiddenBoneCubeIndices)) {
                    return null;
                }
                return NeedsOfNatureClient.createAfwRenderOverride(null, destroyedBaseTexture, hasLayerTextures ? layerTextures : null, hasBoneTextures ? boneTextures : null, hasFallbackBoneItems ? fallbackBoneItems : null, hasBoneVisibility ? boneVisibility : null, hasHiddenBoneCubeIndices ? hiddenBoneCubeIndices : null);
            }
            String path = baseModel.getPath();
            if (path.endsWith(".m") || path.endsWith(".f")) {
                if (!(destroyedBaseTexture != null || hasLayerTextures || hasBoneTextures || hasFallbackBoneItems || hasBoneVisibility || hasHiddenBoneCubeIndices)) {
                    return null;
                }
                return NeedsOfNatureClient.createAfwRenderOverride(null, destroyedBaseTexture, hasLayerTextures ? layerTextures : null, hasBoneTextures ? boneTextures : null, hasFallbackBoneItems ? fallbackBoneItems : null, hasBoneVisibility ? boneVisibility : null, hasHiddenBoneCubeIndices ? hiddenBoneCubeIndices : null);
            }
            Identifier model = NeedsOfNatureClient.resolveExistingGenderedModel(baseModel, isMale);
            return NeedsOfNatureClient.createAfwRenderOverride(model, destroyedBaseTexture, hasLayerTextures ? layerTextures : null, hasBoneTextures ? boneTextures : null, hasFallbackBoneItems ? fallbackBoneItems : null, hasBoneVisibility ? boneVisibility : null, hasHiddenBoneCubeIndices ? hiddenBoneCubeIndices : null);
        });
    }

    @Nullable
    private static Identifier resolveExistingGenderedModel(@Nullable Identifier baseModel, boolean preferMale) {
        if (baseModel == null) {
            return null;
        }
        String path = baseModel.getPath();
        if (path.endsWith(".m") || path.endsWith(".f")) {
            return AfwGeckoResourceResolver.hasGeoModel((Identifier)baseModel) ? baseModel : null;
        }
        Identifier preferred = Identifier.of((String)baseModel.getNamespace(), (String)(path + (preferMale ? ".m" : ".f")));
        if (AfwGeckoResourceResolver.hasGeoModel((Identifier)preferred)) {
            return preferred;
        }
        Identifier fallback = Identifier.of((String)baseModel.getNamespace(), (String)(path + (preferMale ? ".f" : ".m")));
        return AfwGeckoResourceResolver.hasGeoModel((Identifier)fallback) ? fallback : null;
    }

    @Nullable
    private static AfwGeckoModelEvents.RenderOverride resolveCollectorRenderOverride(LivingEntity entity) {
        if (!(entity instanceof HorseLiquidCollectorEntity)) {
            return null;
        }
        HorseLiquidCollectorEntity collector = (HorseLiquidCollectorEntity)entity;
        Identifier texture = collector.isCollectorFull() ? HorseLiquidCollectorGeoModel.FULL_TEXTURE_ID : HorseLiquidCollectorGeoModel.EMPTY_TEXTURE_ID;
        return new AfwGeckoModelEvents.RenderOverride(HorseLiquidCollectorGeoModel.MODEL_ID, texture);
    }

    private static AfwGeckoModelEvents.RenderOverride createAfwRenderOverride(@Nullable Identifier model, @Nullable Identifier texture, @Nullable List<Identifier> layerTextures, @Nullable Map<String, Identifier> boneTextures, @Nullable Map<String, AfwGeckoModelEvents.BoneItemProp> boneItems, @Nullable Map<String, Boolean> boneVisibility, @Nullable Map<String, Set<Integer>> hiddenBoneCubeIndices) {
        if (hiddenBoneCubeIndices == null || hiddenBoneCubeIndices.isEmpty()) {
            return new AfwGeckoModelEvents.RenderOverride(model, texture, layerTextures, boneTextures, boneItems, boneVisibility);
        }
        try {
            return (AfwGeckoModelEvents.RenderOverride)AfwGeckoModelEvents.RenderOverride.class.getConstructor(Identifier.class, Identifier.class, List.class, Map.class, Map.class, Map.class, Map.class).newInstance(model, texture, layerTextures, boneTextures, boneItems, boneVisibility, hiddenBoneCubeIndices);
        }
        catch (ReflectiveOperationException | RuntimeException ignored) {
            return new AfwGeckoModelEvents.RenderOverride(model, texture, layerTextures, boneTextures, boneItems, boneVisibility);
        }
    }

    @Nullable
    private static Identifier resolveDestroyedSkinBaseTexture(LivingEntity entity, @Nullable Identifier currentTexture) {
        if (entity instanceof AbstractClientPlayerEntity) {
            AbstractClientPlayerEntity player = (AbstractClientPlayerEntity)entity;
            try {
                return player.getSkinTexture();
            }
            catch (RuntimeException runtimeException) {
                // empty catch block
            }
        }
        return currentTexture;
    }

    private static Map<String, Boolean> resolveDonkeyChestBoneVisibility(LivingEntity entity) {
        if (!(entity instanceof AbstractDonkeyEntity)) {
            return Map.of();
        }
        AbstractDonkeyEntity donkey = (AbstractDonkeyEntity)entity;
        if (donkey.hasChest()) {
            return Map.of();
        }
        return Map.of("left_chest", false, "right_chest", false);
    }

    private static Map<String, Set<Integer>> resolvePlayerSkinPartHiddenCubeIndices(LivingEntity entity) {
        if (!(entity instanceof AbstractClientPlayerEntity)) {
            return Map.of();
        }
        AbstractClientPlayerEntity player = (AbstractClientPlayerEntity)entity;
        HashMap<String, Set<Integer>> hiddenCubeIndices = new HashMap<String, Set<Integer>>();
        NeedsOfNatureClient.hideOuterCubeIfDisabled(player, hiddenCubeIndices, PlayerModelPart.HAT, "head");
        NeedsOfNatureClient.hideOuterCubeIfDisabled(player, hiddenCubeIndices, PlayerModelPart.JACKET, "body");
        NeedsOfNatureClient.hideOuterCubeIfDisabled(player, hiddenCubeIndices, PlayerModelPart.RIGHT_SLEEVE, "rightarm");
        NeedsOfNatureClient.hideOuterCubeIfDisabled(player, hiddenCubeIndices, PlayerModelPart.LEFT_SLEEVE, "leftarm");
        NeedsOfNatureClient.hideOuterCubeIfDisabled(player, hiddenCubeIndices, PlayerModelPart.RIGHT_PANTS_LEG, "rightleg");
        NeedsOfNatureClient.hideOuterCubeIfDisabled(player, hiddenCubeIndices, PlayerModelPart.LEFT_PANTS_LEG, "leftleg");
        return hiddenCubeIndices.isEmpty() ? Map.of() : Map.copyOf(hiddenCubeIndices);
    }

    private static void hideOuterCubeIfDisabled(AbstractClientPlayerEntity player, Map<String, Set<Integer>> hiddenCubeIndices, PlayerModelPart part, String boneName) {
        if (player == null || hiddenCubeIndices == null || part == null || boneName == null || boneName.isBlank()) {
            return;
        }
        if (!player.isPartVisible(part)) {
            hiddenCubeIndices.put(boneName, Set.of(Integer.valueOf(1)));
        }
    }

    private static Map<String, Identifier> resolveSheepBoneTextures(LivingEntity entity) {
        if (!(entity instanceof SheepEntity)) {
            return Map.of();
        }
        SheepEntity sheep = (SheepEntity)entity;
        if (sheep.isSheared()) {
            return Map.of();
        }
        int rgb = NeedsOfNatureClient.getSheepColorRgb(sheep);
        Identifier woolTexture = NeedsOfNatureClient.getTintedTexture(SHEEP_WOOL_TEXTURE, rgb, "sheep_wool", SHEEP_WOOL_TINT_CACHE);
        HashMap<String, Identifier> textures = new HashMap<String, Identifier>();
        for (String bone : SHEEP_WOOL_BONES) {
            textures.put(bone, woolTexture);
        }
        return Map.copyOf(textures);
    }

    private static List<Identifier> resolveSheepOverlays(LivingEntity entity) {
        if (!(entity instanceof SheepEntity)) {
            return List.of();
        }
        SheepEntity sheep = (SheepEntity)entity;
        return List.of();
    }

    private static Map<String, Boolean> resolveSheepBoneVisibility(LivingEntity entity) {
        if (!(entity instanceof SheepEntity)) {
            return Map.of();
        }
        SheepEntity sheep = (SheepEntity)entity;
        if (!sheep.isSheared()) {
            return Map.of();
        }
        HashMap<String, Boolean> visibility = new HashMap<String, Boolean>();
        for (String bone : SHEEP_WOOL_BONES) {
            visibility.put(bone, Boolean.FALSE);
        }
        return Map.copyOf(visibility);
    }

    private static int getSheepColorRgb(SheepEntity sheep) {
        if (sheep == null) {
            return dyeColorRgb(DyeColor.WHITE);
        }
        DyeColor color = sheep.getColor();
        return dyeColorRgb(color == null ? DyeColor.WHITE : color);
    }

    private static int dyeColorRgb(DyeColor color) {
        float[] components = color.getColorComponents();
        int red = Math.round(components[0] * 255.0f);
        int green = Math.round(components[1] * 255.0f);
        int blue = Math.round(components[2] * 255.0f);
        return red << 16 | green << 8 | blue;
    }

    @SafeVarargs
    private static Map<String, Boolean> mergeBooleanMaps(Map<String, Boolean> ... maps) {
        if (maps == null || maps.length == 0) {
            return Map.of();
        }
        HashMap<String, Boolean> merged = new HashMap<String, Boolean>();
        for (Map<String, Boolean> map : maps) {
            if (map == null || map.isEmpty()) continue;
            merged.putAll(map);
        }
        return merged.isEmpty() ? Map.of() : Map.copyOf(merged);
    }

    private static ItemStack createFilledBottlePropStack(LivingEntity entity) {
        LiquidHolder holder;
        int tint = NonHudOverlay.getLiquidTintRgb() & 0xFFFFFF;
        if (tint == 0) {
            tint = 15920063;
        }
        ItemStack stack = new ItemStack((ItemConvertible)Items.POTION);
        PotionUtil.setPotion(stack, NonPotions.LIQUID);
        stack.getOrCreateNbt().putInt("CustomPotionColor", tint);
        if (entity instanceof LiquidHolder && (holder = (LiquidHolder)entity).getLiquidComposition() == LiquidHolder.LiquidComposition.ENTITY) {
            NonPotions.setLiquidBottleEntityTypeId(stack, holder.getLiquidEntityTypeId());
        } else {
            NonPotions.setLiquidBottleEntityTypeId(stack, null);
        }
        return stack;
    }

    private static boolean isFillBottleAnimation(Identifier animationId) {
        if (animationId == null) {
            return false;
        }
        if (!FILL_BOTTLE_ANIMATION_ID.getNamespace().equals(animationId.getNamespace())) {
            return false;
        }
        String path = animationId.getPath();
        if (path == null || path.isBlank()) {
            return false;
        }
        String normalized = path.toLowerCase(Locale.ROOT).replaceFirst("\\.p\\d+$", "");
        return FILL_BOTTLE_ANIMATION_ID.getPath().equals(normalized);
    }

    private static List<Identifier> resolveHorseOverlays(LivingEntity entity) {
        if (!(entity instanceof AbstractHorseEntity)) {
            return List.of();
        }
        AbstractHorseEntity horse = (AbstractHorseEntity)entity;
        if (horse.isBaby()) {
            return List.of();
        }
        ArrayList<Identifier> overlays = new ArrayList<Identifier>();
        if (horse instanceof HorseEntity) {
            HorseEntity vanillaHorse = (HorseEntity)horse;
            HorseMarking marking = vanillaHorse.getMarking();
            Identifier markingTexture = NeedsOfNatureClient.mapHorseMarkingTexture(marking);
            if (markingTexture != null) {
                overlays.add(markingTexture);
            }
            ItemStack armor = vanillaHorse.getArmorType();
            if (!armor.isEmpty()) {
                overlays.addAll(NeedsOfNatureClient.resolveHorseArmorTextures(armor));
            }
            return overlays.isEmpty() ? List.of() : List.copyOf(overlays);
        }
        return overlays.isEmpty() ? List.of() : List.copyOf(overlays);
    }

    private static Identifier mapHorseMarkingTexture(HorseMarking marking) {
        if (marking == null || marking == HorseMarking.NONE) {
            return null;
        }
        if (marking == HorseMarking.WHITE) {
            return new Identifier("minecraft", "textures/entity/horse/horse_markings_white.png");
        }
        if (marking == HorseMarking.WHITE_FIELD) {
            return new Identifier("minecraft", "textures/entity/horse/horse_markings_whitefield.png");
        }
        if (marking == HorseMarking.WHITE_DOTS) {
            return new Identifier("minecraft", "textures/entity/horse/horse_markings_whitedots.png");
        }
        if (marking == HorseMarking.BLACK_DOTS) {
            return new Identifier("minecraft", "textures/entity/horse/horse_markings_blackdots.png");
        }
        return null;
    }

    private static List<Identifier> resolveHorseArmorTextures(ItemStack armor) {
        if (armor == null || armor.isEmpty() || !(armor.getItem() instanceof HorseArmorItem horseArmor)) {
            return List.of();
        }
        Identifier texture = horseArmor.getEntityTexture();
        if (horseArmor instanceof DyeableItem dyeable) {
            texture = getTintedTexture(texture, dyeable.getColor(armor), "horse_armor", HORSE_ARMOR_TINT_CACHE);
        }
        return List.of(texture);
    }

    private static List<Identifier> resolveWolfOverlays(LivingEntity entity) {
        DyeColor collarColor;
        if (!(entity instanceof WolfEntity)) {
            return List.of();
        }
        WolfEntity wolf = (WolfEntity)entity;
        ArrayList<Identifier> overlays = new ArrayList<Identifier>();
        if (wolf.isTamed() && (collarColor = wolf.getCollarColor()) != null) {
            overlays.add(NeedsOfNatureClient.getTintedTexture(WOLF_COLLAR_TEXTURE, dyeColorRgb(collarColor), "wolf_collar", WOLF_COLLAR_TINT_CACHE));
        }
        return overlays.isEmpty() ? List.of() : List.copyOf(overlays);
    }

    private static Identifier getTintedTexture(Identifier baseTexture, int rgb, String cachePrefix, Map<Integer, Identifier> cache) {
        Identifier cached = cache.get(rgb);
        if (cached != null) {
            return cached;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) {
            return baseTexture;
        }
        Optional<Resource> resourceOpt = client.getResourceManager().getResource(baseTexture);
        if (resourceOpt.isEmpty()) {
            return baseTexture;
        }
        try (InputStream in = resourceOpt.get().getInputStream()) {
            NativeImage image = NativeImage.read(in);
            NeedsOfNatureClient.tintImage(image, rgb);
            String idPath = "textures/dynamic/" + cachePrefix + "_" + String.format("%06x", rgb) + ".png";
            Identifier id = Identifier.of("needsofnature", idPath);
            client.getTextureManager().registerTexture(id, new NativeImageBackedTexture(image));
            cache.put(rgb, id);
            return id;
        }
        catch (IOException | RuntimeException e) {
            NeedsOfNature.LOGGER.debug("[NoN] Failed to tint texture {} (rgb={})", new Object[]{baseTexture, rgb, e});
            return baseTexture;
        }
    }

    private static void tintImage(NativeImage image, int rgb) {
        int tintR = rgb >> 16 & 0xFF;
        int tintG = rgb >> 8 & 0xFF;
        int tintB = rgb & 0xFF;
        int width = image.getWidth();
        int height = image.getHeight();
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int abgr = image.getColor(x, y);
                int alpha = abgr >>> 24 & 0xFF;
                if (alpha == 0) continue;
                int r = abgr & 0xFF;
                int g = abgr >>> 8 & 0xFF;
                int b = abgr >>> 16 & 0xFF;
                int outR = r * tintR / 255;
                int outG = g * tintG / 255;
                int outB = b * tintB / 255;
                int outAbgr = alpha << 24 | outB << 16 | outG << 8 | outR;
                image.setColor(x, y, outAbgr);
            }
        }
    }

    private static void handleVoluntaryStopInput(MinecraftClient client) {
        if (client == null) {
            return;
        }
        ClientPlayerEntity player = client.player;
        if (player == null) {
            return;
        }
        while (client.options.sneakKey.wasPressed()) {
            UUID instanceId;
            if (!AfwClientAnimationRuntime.isActorPendingOrActive((UUID)player.getUuid()) || (instanceId = AfwClientAnimationRuntime.findLatestActiveInstanceContaining((UUID)player.getUuid())) == null || !ClientPlayNetworking.canSend(StopVoluntaryAnimationC2SPayload.ID)) continue;
            NonClientNetworking.send(new StopVoluntaryAnimationC2SPayload(instanceId));
        }
    }

    private static void handleAttackInput(MinecraftClient client) {
        if (client == null || client.world == null) {
            return;
        }
        ClientPlayerEntity player = client.player;
        if (player == null) {
            return;
        }
        UUID instanceId = AfwClientAnimationRuntime.findLatestActiveInstanceContaining((UUID)player.getUuid());
        if (!NonHudOverlay.isAttackInstance(instanceId)) {
            return;
        }
        if (NonHudOverlay.consumeAttackInputClear()) {
            NeedsOfNatureClient.drainPendingPresses(client.options.leftKey);
            NeedsOfNatureClient.drainPendingPresses(client.options.rightKey);
            return;
        }
        int leftPresses = NeedsOfNatureClient.drainPendingPresses(client.options.leftKey);
        int rightPresses = NeedsOfNatureClient.drainPendingPresses(client.options.rightKey);
        if (leftPresses > 0 && rightPresses > 0) {
            return;
        }
        if (leftPresses > 0) {
            NonHudOverlay.recordAttackInput(instanceId, NonHudOverlay.AttackKey.LEFT);
        } else if (rightPresses > 0) {
            NonHudOverlay.recordAttackInput(instanceId, NonHudOverlay.AttackKey.RIGHT);
        }
        if (NonHudOverlay.isAttackProgressComplete() && ClientPlayNetworking.canSend(StopAttackAnimationC2SPayload.ID)) {
            NonClientNetworking.send(new StopAttackAnimationC2SPayload(instanceId));
            NonHudOverlay.resetAttackProgress();
        }
    }

    private static void handleManualPeakInput(MinecraftClient client) {
        if (client == null || client.world == null) {
            return;
        }
        ClientPlayerEntity player = client.player;
        if (player == null || player.isSpectator()) {
            return;
        }
        while (START_MANUAL_PEAK_KEY.wasPressed()) {
            UUID activeInstance = AfwClientAnimationRuntime.findLatestActiveInstanceContaining((UUID)player.getUuid());
            if (activeInstance != null || !ClientPlayNetworking.canSend(StartManualPeakC2SPayload.ID)) continue;
            NonClientNetworking.send(new StartManualPeakC2SPayload());
        }
    }

    public static void pushConfiguredPlayerGenderToServer() {
        NeedsOfNatureClient.pushConfiguredPlayerGenderToServer(NeedsOfNature.getConfig());
    }

    public static void pushConfiguredPlayerGenderToServer(@Nullable NonConfig config) {
        if (config == null) {
            return;
        }
        int mask = config.getPlayerGenderMask() & 3;
        if (mask == 0) {
            mask = NonConfig.PlayerGenderSelection.FEMALE.mask();
        }
        if (ClientPlayNetworking.canSend(SetPlayerGenderC2SPayload.ID)) {
            NonClientNetworking.send(new SetPlayerGenderC2SPayload(mask, false));
        }
        NonWildfireGenderSync.syncFromNonConfig(config);
        NonWildfireGenderSync.syncDestroyedSkinOverrides(config);
    }

    private static void syncLocalGenderMirror(MinecraftClient client) {
        if (client == null || client.player == null) {
            return;
        }
        ClientPlayerEntity class_7462 = client.player;
        if (!(class_7462 instanceof GenderHolder)) {
            return;
        }
        GenderHolder holder = (GenderHolder)class_7462;
        int mask = holder.getGenderMask() & 3;
        if (mask == 0 || mask == LAST_LOCAL_GENDER_SYNC_MASK) {
            return;
        }
        LAST_LOCAL_GENDER_SYNC_MASK = mask;
        NonConfig config = NeedsOfNature.getConfig();
        NonWildfireGenderSync.syncFromMask(config, mask);
        NonWildfireGenderSync.syncDestroyedSkinOverrides(config);
    }

    private static void registerSoundCueResolver() {
        AfwSoundCueEvents.RESOLVE.register(context -> {
            if (NonDebugSpinMode.isEnabled()) {
                return AfwSoundCueEvents.SoundOverride.skip();
            }
            if (NeedsOfNatureClient.isFillBottlePropCue(context)) {
                boolean replacedByAfw;
                UUID instanceId = context.instanceId();
                if (instanceId != null && context.anchor() != null && !(replacedByAfw = NeedsOfNatureClient.trySetAfwRightHandPropOverride(instanceId, context.anchor().getUuid(), NeedsOfNatureClient.createFilledBottlePropStack(context.anchor())))) {
                    FILLED_BOTTLE_PROP_INSTANCES.add(instanceId);
                }
                return AfwSoundCueEvents.SoundOverride.play((Identifier)VANILLA_BOTTLE_FILL_SOUND, (float)context.volume(), (float)context.pitch());
            }
            Identifier soundId = NonSoundCues.resolveSoundId(context.effect(), context.anchor(), context.animationId(), context.stageIndex());
            if (soundId == null) {
                return null;
            }
            boolean localPlayerCue = NeedsOfNatureClient.isLocalPlayerActorInInstance(context.instanceId());
            boolean peakReactiveImpact = NeedsOfNatureClient.isPeakReactiveImpactCue(context);
            if ("reactiveimpact".equalsIgnoreCase(context.effect()) && localPlayerCue) {
                if (peakReactiveImpact) {
                    NeedsOfNatureClient.triggerReactiveImpactFovPulse();
                    NonIntifaceBridge.pulsePeakReactiveImpact(NeedsOfNature.getConfig(), NeedsOfNatureClient.nextSameEffectCueDelayMs(context));
                } else {
                    NonIntifaceBridge.pulseReactiveImpact(NeedsOfNature.getConfig(), NeedsOfNatureClient.nextSameEffectCueDelayMs(context));
                }
            }
            if ("birth".equalsIgnoreCase(context.effect()) && localPlayerCue && context.instanceId() != null) {
                INTIFACE_BIRTH_CUTOFF_INSTANCES.add(context.instanceId());
                NonIntifaceBridge.clientTick(NeedsOfNature.getConfig(), true, false, 0.0);
            }
            float volume = context.volume();
            if (NonSoundCues.isActionSoundId(soundId)) {
                volume *= NeedsOfNature.getConfig().getActionSoundVolumeMultiplier();
            }
            return AfwSoundCueEvents.SoundOverride.play((Identifier)soundId, (float)volume, (float)context.pitch());
        });
    }

    private static boolean isPeakReactiveImpactCue(AfwSoundCueEvents.SoundContext context) {
        if (context == null) {
            return false;
        }
        String effect = context.effect();
        if (effect == null || !"reactiveimpact".equalsIgnoreCase(effect)) {
            return false;
        }
        if (NonSoundCues.isPeakReactiveImpactCue(effect, context.animationId(), context.stageIndex())) {
            return true;
        }
        UUID instanceId = context.instanceId();
        if (instanceId == null) {
            return false;
        }
        AnimationStageInfo stage = AfwClientAnimationRuntime.findCurrentStage((UUID)instanceId);
        if (stage == null || stage.animationId() == null) {
            return false;
        }
        Identifier logicalStageId = stage.animationId();
        Integer stageNumber = NonPeakStages.stageNumberFromId(logicalStageId);
        Integer peakStage = NonPeakStages.getPeakStage(NonPeakStages.baseAnimationId(logicalStageId));
        return stageNumber != null && peakStage != null && stageNumber.equals(peakStage);
    }

    private static double nextSameEffectCueDelayMs(AfwSoundCueEvents.SoundContext context) {
        if (context == null) {
            return 0.0;
        }
        try {
            double d;
            Object value = context.getClass().getMethod("nextSameEffectCueDelayMs", new Class[0]).invoke((Object)context, new Object[0]);
            if (value instanceof Number) {
                Number number = (Number)value;
                d = Math.max(0.0, number.doubleValue());
            } else {
                d = 0.0;
            }
            return d;
        }
        catch (ReflectiveOperationException | RuntimeException e) {
            return 0.0;
        }
    }

    private static boolean isLocalPlayerActorInInstance(@Nullable UUID instanceId) {
        if (instanceId == null) {
            return false;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) {
            return false;
        }
        UUID playerInstance = AfwClientAnimationRuntime.findLatestActiveInstanceContaining((UUID)client.player.getUuid());
        return instanceId.equals(playerInstance);
    }

    private static void tickIntifaceBaseline(MinecraftClient client) {
        boolean active = false;
        boolean peak = false;
        double specialVibratorScalar = Double.NaN;
        if (client != null && client.player != null) {
            IntifaceAnimationState state = NeedsOfNatureClient.findLocalIntifaceAnimationState(client);
            boolean bl = active = state != null;
            if (state != null) {
                peak = NonSoundCues.isPeakAnimationStage(state.stageAnimationId(), state.stageIndex());
                specialVibratorScalar = NeedsOfNatureClient.resolveSpecialIntifaceVibratorScalar(state);
            } else {
                INTIFACE_BIRTH_CUTOFF_INSTANCES.clear();
            }
        }
        int energizedLevel = client != null && client.player != null ? NonHudOverlay.getEnergizedLevel(client.player) : 0;
        double energizedPulse = energizedLevel > 0 ? NonHudOverlay.computeHeartPulse(energizedLevel) : 0.0;
        NonIntifaceBridge.clientTick(NeedsOfNature.getConfig(), active, peak, specialVibratorScalar, energizedLevel, energizedPulse);
    }

    @Nullable
    private static IntifaceAnimationState findLocalIntifaceAnimationState(MinecraftClient client) {
        if (client == null || client.player == null || client.world == null) {
            return null;
        }
        UUID playerUuid = client.player.getUuid();
        UUID instanceId = AfwClientAnimationRuntime.findLatestActiveInstanceContaining((UUID)playerUuid);
        if (instanceId == null) {
            return null;
        }
        AnimationStageInfo stage = AfwClientAnimationRuntime.findCurrentStage((UUID)instanceId);
        if (stage == null) {
            return null;
        }
        Identifier stageAnimationId = stage.effectiveAnimationId();
        Identifier logicalStageAnimationId = stage.animationId();
        Identifier animationId = NonPeakStages.baseAnimationId(logicalStageAnimationId);
        Integer stageNumber = NonPeakStages.stageNumberFromId(logicalStageAnimationId);
        int stageIndex = stageNumber == null ? 0 : Math.max(0, stageNumber - 1);
        long stageStartTick = AfwClientAnimationRuntime.findStageStartTick((UUID)instanceId);
        double speed = Math.max(1.0E-4, AfwClientAnimationRuntime.findLatestSpeedForActor((UUID)playerUuid));
        double elapsedTicks = Math.max(0.0, (double)(client.world.getTime() - stageStartTick) * speed);
        double cycleTicks = NonLoopSecondsOverrides.getCycleTicks(logicalStageAnimationId);
        if (cycleTicks <= 0.0) {
            cycleTicks = stage.lengthTicks();
        }
        double durationTicks = NeedsOfNatureClient.resolveIntifaceStageDurationTicks(animationId, logicalStageAnimationId, stageIndex, cycleTicks);
        return new IntifaceAnimationState(instanceId, animationId, stageAnimationId, stageIndex, elapsedTicks, durationTicks);
    }

    private static double resolveIntifaceStageDurationTicks(Identifier animationId, Identifier stageAnimationId, int stageIndex, double cycleLengthTicks) {
        int seconds;
        double multiplier = NonLoopSecondsOverrides.getStageDurationMultiplier(stageAnimationId);
        Integer override = NonLoopSecondsOverrides.getOverrideSeconds(stageAnimationId);
        if (NonLoopSecondsOverrides.isInfinite(override)) {
            override = null;
        }
        if (override != null) {
            return Math.max(1.0, (double)Math.max(1, override) * 20.0 * multiplier);
        }
        Identifier baseId = NonPeakStages.baseAnimationId(stageAnimationId);
        Integer baseOverride = NonLoopSecondsOverrides.getOverrideSeconds(baseId);
        if (NonLoopSecondsOverrides.isInfinite(baseOverride)) {
            baseOverride = null;
        }
        if (baseOverride != null) {
            return Math.max(1.0, (double)Math.max(1, baseOverride) * 20.0 * multiplier);
        }
        Integer peakStage = NonPeakStages.getPeakStage(animationId);
        int logicalStage = stageIndex + 1;
        int n = seconds = Objects.equals(logicalStage, peakStage) ? NeedsOfNature.getConfig().getPeakLoopProgressSeconds() : NeedsOfNature.getConfig().getLoopProgressSeconds();
        if (seconds > 0) {
            return Math.max(1.0, (double)seconds * 20.0 * multiplier);
        }
        return Math.max(1.0, cycleLengthTicks);
    }

    private static double resolveSpecialIntifaceVibratorScalar(IntifaceAnimationState state) {
        if (state == null || state.animationId() == null) {
            return Double.NaN;
        }
        if (NeedsOfNatureClient.isManualPeakAnimation(state.animationId())) {
            return NeedsOfNatureClient.resolveManualPeakVibratorScalar(state);
        }
        if (NeedsOfNatureClient.isBirthAnimation(state.animationId(), state.stageAnimationId())) {
            return NeedsOfNatureClient.resolveBirthVibratorScalar(state);
        }
        INTIFACE_BIRTH_CUTOFF_INSTANCES.remove(state.instanceId());
        return Double.NaN;
    }

    private static double resolveManualPeakVibratorScalar(IntifaceAnimationState state) {
        Integer peakStage = NonPeakStages.getPeakStage(state.animationId());
        if (peakStage == null) {
            return Double.NaN;
        }
        int logicalStage = state.stageIndex() + 1;
        double progress = state.stageProgress();
        if (logicalStage == peakStage - 1) {
            return Math.max(0.0, Math.min(1.0, progress / 0.9));
        }
        if (logicalStage == peakStage) {
            return Math.max(0.0, Math.min(1.0, 1.0 - progress));
        }
        return Double.NaN;
    }

    private static double resolveBirthVibratorScalar(IntifaceAnimationState state) {
        if (INTIFACE_BIRTH_CUTOFF_INSTANCES.contains(state.instanceId())) {
            return 0.0;
        }
        Integer cueTick = NeedsOfNatureClient.birthCueTickForState(state);
        double elapsed = Math.max(0.0, state.stageElapsedTicks());
        double targetTick = cueTick == null ? Math.max(1.0, state.stageDurationTicks()) : Math.max(1.0, (double)cueTick.intValue());
        return Math.max(0.0, Math.min(1.0, elapsed / targetTick));
    }

    @Nullable
    private static Integer birthCueTickForState(IntifaceAnimationState state) {
        if (state == null) {
            return null;
        }
        Integer direct = NonPregnancyCues.getCueTick(state.stageAnimationId());
        if (direct != null) {
            return direct;
        }
        Identifier stagedId = NeedsOfNatureClient.stagedAnimationId(state.animationId(), state.stageIndex() + 1);
        return NonPregnancyCues.getCueTick(stagedId);
    }

    @Nullable
    private static Identifier stagedAnimationId(Identifier animationId, int stageNumber) {
        if (animationId == null || stageNumber <= 0) {
            return null;
        }
        String path = animationId.getPath();
        if (NonPeakStages.stageNumberFromId(animationId) != null) {
            return animationId;
        }
        return Identifier.of((String)animationId.getNamespace(), (String)(path + ".p" + stageNumber));
    }

    private static boolean isManualPeakAnimation(Identifier animationId) {
        return animationId != null && animationId.getPath().contains("manual_peak");
    }

    private static boolean isBirthAnimation(@Nullable Identifier animationId, @Nullable Identifier stageAnimationId) {
        return NeedsOfNatureClient.hasBirthAnimationPath(animationId) || NeedsOfNatureClient.hasBirthAnimationPath(stageAnimationId);
    }

    private static boolean hasBirthAnimationPath(@Nullable Identifier animationId) {
        if (animationId == null) {
            return false;
        }
        return animationId.getPath().contains("birth");
    }

    private static boolean isFillBottlePropCue(AfwSoundCueEvents.SoundContext context) {
        if (context == null) {
            return false;
        }
        if (!NeedsOfNatureClient.isFillBottleAnimation(context.animationId())) {
            return false;
        }
        String effect = context.effect();
        return FILL_BOTTLE_PROP_CUE_EFFECT.equalsIgnoreCase(effect);
    }

    private static Map<String, AfwGeckoModelEvents.BoneItemProp> resolveFillBottleFallbackBoneItems(LivingEntity entity) {
        if (entity == null || NeedsOfNatureClient.getAfwRightPropOverrideMethod() != null) {
            return Map.of();
        }
        UUID instanceId = AfwClientAnimationRuntime.findLatestActiveInstanceContaining((UUID)entity.getUuid());
        if (instanceId == null || !FILLED_BOTTLE_PROP_INSTANCES.contains(instanceId)) {
            return Map.of();
        }
        Identifier animationId = AfwClientAnimationRuntime.findLatestActiveAnimationIdContaining((UUID)entity.getUuid());
        if (!NeedsOfNatureClient.isFillBottleAnimation(animationId)) {
            return Map.of();
        }
        return Map.of(RIGHT_PROP_BONE, new AfwGeckoModelEvents.BoneItemProp(NeedsOfNatureClient.createFilledBottlePropStack(entity)));
    }

    private static Map<String, AfwGeckoModelEvents.BoneItemProp> resolveFallbackBoneItems(LivingEntity entity) {
        Map<String, AfwGeckoModelEvents.BoneItemProp> fillBottle =
                NeedsOfNatureClient.resolveFillBottleFallbackBoneItems(entity);
        if (!fillBottle.isEmpty() || entity == null) {
            return fillBottle;
        }
        Identifier animationId = AfwClientAnimationRuntime.findLatestActiveAnimationIdContaining(entity.getUuid());
        if (animationId == null || !animationId.getPath().contains("manual_peak-carrot")) {
            return Map.of();
        }
        ItemStack held = entity.getMainHandStack();
        if (!held.isOf(Items.CARROT) && !held.isOf(Items.GOLDEN_CARROT)) {
            held = entity.getOffHandStack();
        }
        if (!held.isOf(Items.CARROT) && !held.isOf(Items.GOLDEN_CARROT)) {
            return Map.of();
        }
        return Map.of(RIGHT_PROP_BONE,
                new AfwGeckoModelEvents.BoneItemProp(held.copyWithCount(1)));
    }

    private static boolean trySetAfwRightHandPropOverride(UUID instanceId, UUID actorUuid, ItemStack stack) {
        if (instanceId == null || actorUuid == null || stack == null || stack.isEmpty()) {
            return false;
        }
        Method method = NeedsOfNatureClient.getAfwRightPropOverrideMethod();
        if (method == null) {
            return false;
        }
        try {
            method.invoke(null, instanceId, actorUuid, stack);
            return true;
        }
        catch (ReflectiveOperationException e) {
            NeedsOfNature.LOGGER.debug("[NoN] Failed to invoke AFW right-hand prop override API.", (Throwable)e);
            return false;
        }
    }

    private static void applyManualPeakPropOverride(ManualPeakPropOverrideS2CPayload payload) {
        if (payload == null || payload.itemId() == null) {
            return;
        }
        if (!Registries.ITEM.containsId(payload.itemId())) {
            return;
        }
        PendingManualPeakPropOverride pending = new PendingManualPeakPropOverride(payload.instanceId(), payload.actorUuid(), payload.itemId(), CLIENT_FALLBACK_TICKS + 40L);
        if (NeedsOfNatureClient.tryApplyManualPeakPropOverride(pending)) {
            return;
        }
        PENDING_MANUAL_PEAK_PROP_OVERRIDES.put(payload.instanceId(), pending);
    }

    private static boolean tryApplyManualPeakPropOverride(PendingManualPeakPropOverride pending) {
        if (pending == null || pending.instanceId() == null || pending.actorUuid() == null || pending.itemId() == null) {
            return true;
        }
        if (AfwClientAnimationRuntime.findCurrentStage((UUID)pending.instanceId()) == null) {
            return false;
        }
        if (!Registries.ITEM.containsId(pending.itemId())) {
            return true;
        }
        NeedsOfNatureClient.trySetAfwRightHandPropOverride(pending.instanceId(), pending.actorUuid(), new ItemStack((ItemConvertible)Registries.ITEM.get(pending.itemId())));
        return true;
    }

    private static void applyPendingManualPeakPropOverrides(MinecraftClient client) {
        if (PENDING_MANUAL_PEAK_PROP_OVERRIDES.isEmpty()) {
            return;
        }
        PENDING_MANUAL_PEAK_PROP_OVERRIDES.entrySet().removeIf(entry -> {
            PendingManualPeakPropOverride pending = (PendingManualPeakPropOverride)entry.getValue();
            if (pending == null) {
                return true;
            }
            if (NeedsOfNatureClient.tryApplyManualPeakPropOverride(pending)) {
                return true;
            }
            return CLIENT_FALLBACK_TICKS > pending.expireTick();
        });
    }

    @Nullable
    private static Method getAfwRightPropOverrideMethod() {
        Method cached = AFW_RIGHT_PROP_OVERRIDE_METHOD;
        if (cached != null) {
            return cached;
        }
        if (AFW_RIGHT_PROP_OVERRIDE_LOOKED_UP) {
            return null;
        }
        AFW_RIGHT_PROP_OVERRIDE_LOOKED_UP = true;
        try {
            Method method;
            AFW_RIGHT_PROP_OVERRIDE_METHOD = method = AfwClientAnimationRuntime.class.getMethod("setRightHandPropOverride", UUID.class, UUID.class, ItemStack.class);
            return method;
        }
        catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static void pruneFilledBottlePropInstances(MinecraftClient client) {
        if (FILLED_BOTTLE_PROP_INSTANCES.isEmpty()) {
            return;
        }
        FILLED_BOTTLE_PROP_INSTANCES.removeIf(instanceId -> instanceId == null || AfwClientAnimationRuntime.findCurrentStage((UUID)instanceId) == null);
    }

    private static int drainPendingPresses(KeyBinding key) {
        int count = 0;
        while (key.wasPressed()) {
            ++count;
        }
        return count;
    }

    private static void triggerReactiveImpactFovPulse() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) {
            return;
        }
        REACTIVE_IMPACT_PULSE_START_NANOS = System.nanoTime();
    }

    public static float getReactiveImpactFovOffset(float tickDelta) {
        long pulseStartNanos = REACTIVE_IMPACT_PULSE_START_NANOS;
        if (pulseStartNanos == Long.MIN_VALUE) {
            return 0.0f;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) {
            return 0.0f;
        }
        long nowNanos = System.nanoTime();
        if (nowNanos < pulseStartNanos) {
            REACTIVE_IMPACT_PULSE_START_NANOS = nowNanos;
            return -0.5f;
        }
        float elapsed = (float)((double)(nowNanos - pulseStartNanos) / 5.0E7);
        if (elapsed <= 3.0f) {
            return -0.5f;
        }
        float recovery = elapsed - 3.0f;
        if (recovery >= 14.0f) {
            REACTIVE_IMPACT_PULSE_START_NANOS = Long.MIN_VALUE;
            return 0.0f;
        }
        float t = MathHelper.clamp((float)(recovery / 14.0f), (float)0.0f, (float)1.0f);
        float remaining = 1.0f - t;
        float eased = remaining * remaining;
        return -0.5f * eased;
    }

    private static long getCurrentClientTicks(MinecraftClient client) {
        if (client != null && client.world != null) {
            return client.world.getTime();
        }
        return CLIENT_FALLBACK_TICKS;
    }

    private record IntifaceAnimationState(UUID instanceId, Identifier animationId, Identifier stageAnimationId, int stageIndex, double stageElapsedTicks, double stageDurationTicks) {
        double stageProgress() {
            if (this.stageDurationTicks <= 0.0) {
                return 0.0;
            }
            return Math.max(0.0, Math.min(1.0, this.stageElapsedTicks / this.stageDurationTicks));
        }
    }

    private record PendingManualPeakPropOverride(UUID instanceId, UUID actorUuid, Identifier itemId, long expireTick) {
    }
}

