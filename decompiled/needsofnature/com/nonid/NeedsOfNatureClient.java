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
 *  net.minecraft.class_1011
 *  net.minecraft.class_10186
 *  net.minecraft.class_10186$class_10188
 *  net.minecraft.class_10186$class_10189
 *  net.minecraft.class_10186$class_10190
 *  net.minecraft.class_10191
 *  net.minecraft.class_10192
 *  net.minecraft.class_10201
 *  net.minecraft.class_10394
 *  net.minecraft.class_1043
 *  net.minecraft.class_1044
 *  net.minecraft.class_124
 *  net.minecraft.class_1299
 *  net.minecraft.class_1304
 *  net.minecraft.class_1309
 *  net.minecraft.class_1472
 *  net.minecraft.class_1492
 *  net.minecraft.class_1493
 *  net.minecraft.class_1496
 *  net.minecraft.class_1498
 *  net.minecraft.class_1664
 *  net.minecraft.class_1767
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1844
 *  net.minecraft.class_1935
 *  net.minecraft.class_2396
 *  net.minecraft.class_2561
 *  net.minecraft.class_2960
 *  net.minecraft.class_304
 *  net.minecraft.class_304$class_11900
 *  net.minecraft.class_310
 *  net.minecraft.class_3298
 *  net.minecraft.class_3532
 *  net.minecraft.class_3675$class_307
 *  net.minecraft.class_437
 *  net.minecraft.class_5148
 *  net.minecraft.class_5321
 *  net.minecraft.class_5619
 *  net.minecraft.class_742
 *  net.minecraft.class_746
 *  net.minecraft.class_7923
 *  net.minecraft.class_8685
 *  net.minecraft.class_8710
 *  net.minecraft.class_898
 *  net.minecraft.class_9282
 *  net.minecraft.class_9334
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid;

import com.afwid.api.AfwGeckoModelEvents;
import com.afwid.api.AfwSoundCueEvents;
import com.afwid.client.render.gecko.AfwGeckoResourceResolver;
import com.afwid.client.runtime.AfwClientAnimationRuntime;
import com.afwid.mixin.client.EntityRenderManagerAccessor;
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
import net.minecraft.class_1011;
import net.minecraft.class_10186;
import net.minecraft.class_10191;
import net.minecraft.class_10192;
import net.minecraft.class_10201;
import net.minecraft.class_10394;
import net.minecraft.class_1043;
import net.minecraft.class_1044;
import net.minecraft.class_124;
import net.minecraft.class_1299;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1472;
import net.minecraft.class_1492;
import net.minecraft.class_1493;
import net.minecraft.class_1496;
import net.minecraft.class_1498;
import net.minecraft.class_1664;
import net.minecraft.class_1767;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1844;
import net.minecraft.class_1935;
import net.minecraft.class_2396;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_3298;
import net.minecraft.class_3532;
import net.minecraft.class_3675;
import net.minecraft.class_437;
import net.minecraft.class_5148;
import net.minecraft.class_5321;
import net.minecraft.class_5619;
import net.minecraft.class_742;
import net.minecraft.class_746;
import net.minecraft.class_7923;
import net.minecraft.class_8685;
import net.minecraft.class_8710;
import net.minecraft.class_898;
import net.minecraft.class_9282;
import net.minecraft.class_9334;
import org.jetbrains.annotations.Nullable;

public class NeedsOfNatureClient
implements ClientModInitializer {
    private static final Gson HOST_CONFIG_SYNC_GSON = new Gson();
    private static final class_2960 WOLF_COLLAR_TEXTURE = class_2960.method_60656((String)"textures/entity/wolf/wolf_collar.png");
    private static final class_2960 SHEEP_WOOL_TEXTURE = class_2960.method_60656((String)"textures/entity/sheep/sheep_wool.png");
    private static final class_2960 SHEEP_WOOL_UNDERCOAT_TEXTURE = class_2960.method_60656((String)"textures/entity/sheep/sheep_wool_undercoat.png");
    private static final class_2960 FILL_BOTTLE_ANIMATION_ID = class_2960.method_60655((String)"animationframework", (String)"player_fill_bottle");
    private static final String FILL_BOTTLE_PROP_CUE_EFFECT = "fillbottle_prop_fill";
    private static final class_2960 VANILLA_BOTTLE_FILL_SOUND = class_2960.method_60656((String)"item.bottle.fill");
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
    private static final Map<Integer, class_2960> WOLF_COLLAR_TINT_CACHE = new HashMap<Integer, class_2960>();
    private static final Map<Integer, class_2960> SHEEP_WOOL_TINT_CACHE = new HashMap<Integer, class_2960>();
    private static final Map<Integer, class_2960> SHEEP_WOOL_UNDERCOAT_TINT_CACHE = new HashMap<Integer, class_2960>();
    private static final Map<String, class_2960> EQUIPMENT_TINT_CACHE = new HashMap<String, class_2960>();
    private static final Set<String> SHEEP_WOOL_BONES = Set.of("bodywool", "headwool", "leg1wool", "leg2wool", "leg3wool", "leg4wool");
    private static final int PLAYER_OUTER_SKIN_CUBE_INDEX = 1;
    private static final class_304.class_11900 NON_KEY_CATEGORY = class_304.class_11900.method_74698((class_2960)class_2960.method_60655((String)"needsofnature", (String)"main"));
    private static final class_304 START_MANUAL_PEAK_KEY = KeyBindingHelper.registerKeyBinding((class_304)new class_304("key.needsofnature.start_manual_peak", class_3675.class_307.field_1668, 77, NON_KEY_CATEGORY));

    public static void sendClientSetupWarning(String message) {
        if (message == null || message.isBlank()) {
            return;
        }
        if (!NeedsOfNature.getConfig().allowsDebugChat(NonDebugChatCategory.SETUP)) {
            return;
        }
        class_310 client = class_310.method_1551();
        if (client == null || client.field_1724 == null) {
            return;
        }
        client.field_1724.method_7353((class_2561)class_2561.method_43469((String)"debug.needsofnature.prefix", (Object[])new Object[]{class_2561.method_43470((String)message)}).method_27692(class_124.field_1054), false);
    }

    public void onInitializeClient() {
        class_5619.method_32173(NeedsOfNature.HORSE_LIQUID_COLLECTOR_ENTITY_TYPE, HorseLiquidCollectorRenderer::new);
        class_5619.method_32173(NeedsOfNature.PREGNANCY_EGG_ENTITY_TYPE, PregnancyEggRenderer::new);
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
        ClientPlayNetworking.registerGlobalReceiver(AttackStateS2CPayload.ID, (payload, context) -> context.client().execute(() -> NonHudOverlay.setEscapeControlState(payload.instanceId(), payload.attack(), payload.escapable(), payload.escapeProfile())));
        ClientPlayNetworking.registerGlobalReceiver(StageProgressS2CPayload.ID, (payload, context) -> context.client().execute(() -> NonHudOverlay.setStageProgressState(payload)));
        ClientPlayNetworking.registerGlobalReceiver(LiquidStateS2CPayload.ID, (payload, context) -> context.client().execute(() -> {
            NonHudOverlay.setLiquidState(payload.stored(), payload.capacity(), payload.tintRgb());
            class_310 client = context.client();
            class_746 patt0$temp = client.field_1724;
            if (patt0$temp instanceof LiquidHolder) {
                LiquidHolder holder = (LiquidHolder)patt0$temp;
                holder.setLiquidStored(payload.stored());
            }
        }));
        ClientPlayNetworking.registerGlobalReceiver(PregnancyStateS2CPayload.ID, (payload, context) -> context.client().execute(() -> {
            String raw = payload.pregnantEntityTypeId();
            class_2960 id = raw == null || raw.isBlank() ? null : class_2960.method_12829((String)raw);
            NonPregnancyClientState.setPregnantEntityTypeId(id);
        }));
        ClientPlayNetworking.registerGlobalReceiver(GameplayRuntimeSettingsS2CPayload.ID, (payload, context) -> context.client().execute(() -> NonHudOverlay.setRuntimeGameplaySettings(payload.loopSeconds(), payload.peakLoopSeconds(), payload.attackEscapeHits(), payload.attackDecayPerSecond(), payload.attackEscapeDamageDifficultyPercent(), payload.attackCreativePlayers())));
        ClientPlayNetworking.registerGlobalReceiver(GenderSelectionPromptS2CPayload.ID, (payload, context) -> context.client().execute(() -> class_310.method_1551().method_1507((class_437)new NonGenderSelectionScreen(payload.allowedMask(), payload.currentMask(), payload.permanent()))));
        ClientPlayNetworking.registerGlobalReceiver(HostConfigSyncS2CPayload.ID, (payload, context) -> context.client().execute(() -> {
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
                class_310 client = context.client();
                if (client.method_1576() == null) {
                    data.applyTo(config);
                }
                NonHudOverlay.setRuntimeGameplaySettings(data.loopProgressSeconds, data.peakLoopProgressSeconds, data.attackEscapeHits, data.attackDecayPerSecond, data.attackEscapeDamageDifficultyPercent, data.attackCreativePlayers);
            }
            catch (RuntimeException e) {
                NeedsOfNature.LOGGER.warn("[NoN] Failed to parse host config sync payload.", (Throwable)e);
            }
        }));
        ClientPlayNetworking.registerGlobalReceiver(DestroyedSkinSyncS2CPayload.ID, (payload, context) -> context.client().execute(() -> NonDestroyedSkinClient.setSkin(payload.playerUuid(), payload.stage(), payload.pngBytes())));
        ClientPlayNetworking.registerGlobalReceiver(DestroyedSkinMaskSyncS2CPayload.ID, (payload, context) -> context.client().execute(() -> NonDestroyedSkinClient.setMask(payload.playerUuid(), payload.stage(), payload.pngBytes())));
        ClientPlayNetworking.registerGlobalReceiver(DestroyedSkinStateS2CPayload.ID, (payload, context) -> context.client().execute(() -> NonDestroyedSkinClient.setStage(payload.playerUuid(), payload.stage())));
        ClientPlayNetworking.registerGlobalReceiver(DestroyedSkinParticlesS2CPayload.ID, (payload, context) -> context.client().execute(() -> NonDestroyedSkinClient.spawnRippedFabricParticles(payload.playerUuid(), payload.x(), payload.y(), payload.z(), payload.count(), payload.seed())));
        ClientPlayNetworking.registerGlobalReceiver(MessStateS2CPayload.ID, (payload, context) -> context.client().execute(() -> NonDestroyedSkinClient.setMess(payload.playerUuid(), payload.vMess(), payload.aMess(), payload.mMess(), payload.tintRgb(), payload.liquidStored(), payload.liquidCapacity())));
        ClientPlayNetworking.registerGlobalReceiver(AccessoryShedStateS2CPayload.ID, (payload, context) -> context.client().execute(() -> NonAccessoryShedSystem.setClientShedState(payload.playerUuid(), payload.shedV(), payload.shedA(), payload.forcedV(), payload.forcedA())));
        ClientPlayNetworking.registerGlobalReceiver(SkinRippedInfoRequestS2CPayload.ID, (payload, context) -> context.client().execute(NonDestroyedSkinClient::sendDetectedRippedSkinInfo));
        ClientPlayNetworking.registerGlobalReceiver(ManualPeakPropOverrideS2CPayload.ID, (payload, context) -> context.client().execute(() -> NeedsOfNatureClient.applyManualPeakPropOverride(payload)));
        NeedsOfNatureClient.registerSoundCueResolver();
        ParticleFactoryRegistry.getInstance().register((class_2396)NonParticles.LIQUID_PARTICLE, LiquidParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register((class_2396)NonParticles.LIQUID_PARTICLE_FALLING, LiquidFallingParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register((class_2396)NonParticles.LIQUID_PARTICLE_PUDDLE, LiquidPuddleParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register((class_2396)NonParticles.LIQUID_PARTICLE_WATER, LiquidWaterParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register((class_2396)NonParticles.SMALLHEART, SmallHeartParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register((class_2396)NonParticles.RIPPED_FABRIC, RippedFabricParticle.Factory::new);
        AfwGeckoModelEvents.RESOLVE_RENDER.register((entity, entityTypeId, currentModel, currentTexture) -> {
            boolean isFemale;
            if (NonDebugSpinMode.isEnabled()) {
                return new AfwGeckoModelEvents.RenderOverride(NonDebugSpinMode.DEBUG_SPIN_MODEL_RESOURCE, NonDebugSpinMode.DEBUG_SPIN_TEXTURE_RESOURCE);
            }
            AfwGeckoModelEvents.RenderOverride collectorOverride = NeedsOfNatureClient.resolveCollectorRenderOverride(entity);
            if (collectorOverride != null) {
                return collectorOverride;
            }
            class_2960 destroyedBaseTexture = NonDestroyedSkinClient.resolveBaseTexture(entity, NeedsOfNatureClient.resolveDestroyedSkinBaseTexture(entity, currentTexture));
            class_2960 destroyedOverlayTexture = destroyedBaseTexture == null ? NonDestroyedSkinClient.resolveOverlayTexture(entity) : null;
            ArrayList<class_2960> layers = new ArrayList<class_2960>();
            if (destroyedOverlayTexture != null) {
                layers.add(destroyedOverlayTexture);
            }
            layers.addAll(NeedsOfNatureClient.resolveSheepOverlays(entity));
            layers.addAll(NeedsOfNatureClient.resolveHorseOverlays(entity));
            layers.addAll(NeedsOfNatureClient.resolveWolfOverlays(entity));
            List<class_2960> layerTextures = layers.isEmpty() ? List.of() : List.copyOf(layers);
            Map<String, AfwGeckoModelEvents.BoneItemProp> fallbackBoneItems = NeedsOfNatureClient.resolveFillBottleFallbackBoneItems(entity);
            Map<String, class_2960> boneTextures = NeedsOfNatureClient.resolveSheepBoneTextures(entity);
            Map<String, Boolean> boneVisibility = NeedsOfNatureClient.mergeBooleanMaps(NeedsOfNatureClient.resolveDonkeyChestBoneVisibility(entity), NeedsOfNatureClient.resolveSheepBoneVisibility(entity));
            Map<String, Set<Integer>> hiddenBoneCubeIndices = NeedsOfNatureClient.resolvePlayerSkinPartHiddenCubeIndices(entity);
            boolean hasLayerTextures = !layerTextures.isEmpty();
            boolean hasBoneTextures = !boneTextures.isEmpty();
            boolean hasFallbackBoneItems = !fallbackBoneItems.isEmpty();
            boolean hasBoneVisibility = !boneVisibility.isEmpty();
            boolean hasHiddenBoneCubeIndices = !hiddenBoneCubeIndices.isEmpty();
            class_2960 baseModel = currentModel;
            if (baseModel == null && entityTypeId != null) {
                baseModel = class_2960.method_60655((String)"animationframework", (String)("entity/" + entityTypeId.method_12832()));
            }
            class_2960 defaultGenderModel = NeedsOfNatureClient.resolveExistingGenderedModel(baseModel, true);
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
            String path = baseModel.method_12832();
            if (path.endsWith(".m") || path.endsWith(".f")) {
                if (!(destroyedBaseTexture != null || hasLayerTextures || hasBoneTextures || hasFallbackBoneItems || hasBoneVisibility || hasHiddenBoneCubeIndices)) {
                    return null;
                }
                return NeedsOfNatureClient.createAfwRenderOverride(null, destroyedBaseTexture, hasLayerTextures ? layerTextures : null, hasBoneTextures ? boneTextures : null, hasFallbackBoneItems ? fallbackBoneItems : null, hasBoneVisibility ? boneVisibility : null, hasHiddenBoneCubeIndices ? hiddenBoneCubeIndices : null);
            }
            class_2960 model = NeedsOfNatureClient.resolveExistingGenderedModel(baseModel, isMale);
            return NeedsOfNatureClient.createAfwRenderOverride(model, destroyedBaseTexture, hasLayerTextures ? layerTextures : null, hasBoneTextures ? boneTextures : null, hasFallbackBoneItems ? fallbackBoneItems : null, hasBoneVisibility ? boneVisibility : null, hasHiddenBoneCubeIndices ? hiddenBoneCubeIndices : null);
        });
    }

    @Nullable
    private static class_2960 resolveExistingGenderedModel(@Nullable class_2960 baseModel, boolean preferMale) {
        if (baseModel == null) {
            return null;
        }
        String path = baseModel.method_12832();
        if (path.endsWith(".m") || path.endsWith(".f")) {
            return AfwGeckoResourceResolver.hasGeoModel((class_2960)baseModel) ? baseModel : null;
        }
        class_2960 preferred = class_2960.method_60655((String)baseModel.method_12836(), (String)(path + (preferMale ? ".m" : ".f")));
        if (AfwGeckoResourceResolver.hasGeoModel((class_2960)preferred)) {
            return preferred;
        }
        class_2960 fallback = class_2960.method_60655((String)baseModel.method_12836(), (String)(path + (preferMale ? ".f" : ".m")));
        return AfwGeckoResourceResolver.hasGeoModel((class_2960)fallback) ? fallback : null;
    }

    @Nullable
    private static AfwGeckoModelEvents.RenderOverride resolveCollectorRenderOverride(class_1309 entity) {
        if (!(entity instanceof HorseLiquidCollectorEntity)) {
            return null;
        }
        HorseLiquidCollectorEntity collector = (HorseLiquidCollectorEntity)entity;
        class_2960 texture = collector.isCollectorFull() ? HorseLiquidCollectorGeoModel.FULL_TEXTURE_ID : HorseLiquidCollectorGeoModel.EMPTY_TEXTURE_ID;
        return new AfwGeckoModelEvents.RenderOverride(HorseLiquidCollectorGeoModel.MODEL_ID, texture);
    }

    private static AfwGeckoModelEvents.RenderOverride createAfwRenderOverride(@Nullable class_2960 model, @Nullable class_2960 texture, @Nullable List<class_2960> layerTextures, @Nullable Map<String, class_2960> boneTextures, @Nullable Map<String, AfwGeckoModelEvents.BoneItemProp> boneItems, @Nullable Map<String, Boolean> boneVisibility, @Nullable Map<String, Set<Integer>> hiddenBoneCubeIndices) {
        if (hiddenBoneCubeIndices == null || hiddenBoneCubeIndices.isEmpty()) {
            return new AfwGeckoModelEvents.RenderOverride(model, texture, layerTextures, boneTextures, boneItems, boneVisibility);
        }
        try {
            return (AfwGeckoModelEvents.RenderOverride)AfwGeckoModelEvents.RenderOverride.class.getConstructor(class_2960.class, class_2960.class, List.class, Map.class, Map.class, Map.class, Map.class).newInstance(model, texture, layerTextures, boneTextures, boneItems, boneVisibility, hiddenBoneCubeIndices);
        }
        catch (ReflectiveOperationException | RuntimeException ignored) {
            return new AfwGeckoModelEvents.RenderOverride(model, texture, layerTextures, boneTextures, boneItems, boneVisibility);
        }
    }

    @Nullable
    private static class_2960 resolveDestroyedSkinBaseTexture(class_1309 entity, @Nullable class_2960 currentTexture) {
        if (entity instanceof class_742) {
            class_742 player = (class_742)entity;
            try {
                class_8685 skin = player.method_52814();
                if (skin != null && skin.comp_1626() != null && skin.comp_1626().comp_3627() != null) {
                    return skin.comp_1626().comp_3627();
                }
            }
            catch (RuntimeException runtimeException) {
                // empty catch block
            }
        }
        return currentTexture;
    }

    private static Map<String, Boolean> resolveDonkeyChestBoneVisibility(class_1309 entity) {
        if (!(entity instanceof class_1492)) {
            return Map.of();
        }
        class_1492 donkey = (class_1492)entity;
        if (donkey.method_6703()) {
            return Map.of();
        }
        return Map.of("left_chest", false, "right_chest", false);
    }

    private static Map<String, Set<Integer>> resolvePlayerSkinPartHiddenCubeIndices(class_1309 entity) {
        if (!(entity instanceof class_742)) {
            return Map.of();
        }
        class_742 player = (class_742)entity;
        HashMap<String, Set<Integer>> hiddenCubeIndices = new HashMap<String, Set<Integer>>();
        NeedsOfNatureClient.hideOuterCubeIfDisabled(player, hiddenCubeIndices, class_1664.field_7563, "head");
        NeedsOfNatureClient.hideOuterCubeIfDisabled(player, hiddenCubeIndices, class_1664.field_7564, "body");
        NeedsOfNatureClient.hideOuterCubeIfDisabled(player, hiddenCubeIndices, class_1664.field_7570, "rightarm");
        NeedsOfNatureClient.hideOuterCubeIfDisabled(player, hiddenCubeIndices, class_1664.field_7568, "leftarm");
        NeedsOfNatureClient.hideOuterCubeIfDisabled(player, hiddenCubeIndices, class_1664.field_7565, "rightleg");
        NeedsOfNatureClient.hideOuterCubeIfDisabled(player, hiddenCubeIndices, class_1664.field_7566, "leftleg");
        return hiddenCubeIndices.isEmpty() ? Map.of() : Map.copyOf(hiddenCubeIndices);
    }

    private static void hideOuterCubeIfDisabled(class_742 player, Map<String, Set<Integer>> hiddenCubeIndices, class_1664 part, String boneName) {
        if (player == null || hiddenCubeIndices == null || part == null || boneName == null || boneName.isBlank()) {
            return;
        }
        if (!player.method_74091(part)) {
            hiddenCubeIndices.put(boneName, Set.of(Integer.valueOf(1)));
        }
    }

    private static Map<String, class_2960> resolveSheepBoneTextures(class_1309 entity) {
        if (!(entity instanceof class_1472)) {
            return Map.of();
        }
        class_1472 sheep = (class_1472)entity;
        if (sheep.method_6629()) {
            return Map.of();
        }
        int rgb = NeedsOfNatureClient.getSheepColorRgb(sheep);
        class_2960 woolTexture = NeedsOfNatureClient.getTintedTexture(SHEEP_WOOL_TEXTURE, rgb, "sheep_wool", SHEEP_WOOL_TINT_CACHE);
        HashMap<String, class_2960> textures = new HashMap<String, class_2960>();
        for (String bone : SHEEP_WOOL_BONES) {
            textures.put(bone, woolTexture);
        }
        return Map.copyOf(textures);
    }

    private static List<class_2960> resolveSheepOverlays(class_1309 entity) {
        if (!(entity instanceof class_1472)) {
            return List.of();
        }
        class_1472 sheep = (class_1472)entity;
        class_2960 undercoatTexture = NeedsOfNatureClient.getTintedTexture(SHEEP_WOOL_UNDERCOAT_TEXTURE, NeedsOfNatureClient.getSheepColorRgb(sheep), "sheep_wool_undercoat", SHEEP_WOOL_UNDERCOAT_TINT_CACHE);
        return List.of(undercoatTexture);
    }

    private static Map<String, Boolean> resolveSheepBoneVisibility(class_1309 entity) {
        if (!(entity instanceof class_1472)) {
            return Map.of();
        }
        class_1472 sheep = (class_1472)entity;
        if (!sheep.method_6629()) {
            return Map.of();
        }
        HashMap<String, Boolean> visibility = new HashMap<String, Boolean>();
        for (String bone : SHEEP_WOOL_BONES) {
            visibility.put(bone, Boolean.FALSE);
        }
        return Map.copyOf(visibility);
    }

    private static int getSheepColorRgb(class_1472 sheep) {
        if (sheep == null) {
            return class_1767.field_7952.method_7787();
        }
        class_1767 color = sheep.method_6633();
        return color == null ? class_1767.field_7952.method_7787() : color.method_7787();
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

    private static class_1799 createFilledBottlePropStack(class_1309 entity) {
        LiquidHolder holder;
        int tint = NonHudOverlay.getLiquidTintRgb() & 0xFFFFFF;
        if (tint == 0) {
            tint = 15920063;
        }
        class_1844 contents = new class_1844(Optional.of(class_7923.field_41179.method_47983((Object)NonPotions.LIQUID)), Optional.of(tint), List.of(), Optional.empty());
        class_1799 stack = new class_1799((class_1935)class_1802.field_8574);
        stack.method_57379(class_9334.field_49651, (Object)contents);
        if (entity instanceof LiquidHolder && (holder = (LiquidHolder)entity).getLiquidComposition() == LiquidHolder.LiquidComposition.ENTITY) {
            NonPotions.setLiquidBottleEntityTypeId(stack, holder.getLiquidEntityTypeId());
        } else {
            NonPotions.setLiquidBottleEntityTypeId(stack, null);
        }
        return stack;
    }

    private static boolean isFillBottleAnimation(class_2960 animationId) {
        if (animationId == null) {
            return false;
        }
        if (!FILL_BOTTLE_ANIMATION_ID.method_12836().equals(animationId.method_12836())) {
            return false;
        }
        String path = animationId.method_12832();
        if (path == null || path.isBlank()) {
            return false;
        }
        String normalized = path.toLowerCase(Locale.ROOT).replaceFirst("\\.p\\d+$", "");
        return FILL_BOTTLE_ANIMATION_ID.method_12832().equals(normalized);
    }

    private static List<class_2960> resolveHorseOverlays(class_1309 entity) {
        if (!(entity instanceof class_1496)) {
            return List.of();
        }
        class_1496 horse = (class_1496)entity;
        if (horse.method_6109()) {
            return List.of();
        }
        ArrayList<class_2960> overlays = new ArrayList<class_2960>();
        if (horse instanceof class_1498) {
            class_1498 vanillaHorse = (class_1498)horse;
            class_5148 marking = vanillaHorse.method_27078();
            class_2960 markingTexture = NeedsOfNatureClient.mapHorseMarkingTexture(marking);
            if (markingTexture != null) {
                overlays.add(markingTexture);
            }
            NeedsOfNatureClient.addSaddleOverlay(overlays, (class_1496)vanillaHorse);
            class_1799 armor = vanillaHorse.method_6118(class_1304.field_48824);
            if (!armor.method_7960()) {
                overlays.addAll(NeedsOfNatureClient.resolveHorseArmorTextures(armor));
            }
            return overlays.isEmpty() ? List.of() : List.copyOf(overlays);
        }
        NeedsOfNatureClient.addSaddleOverlay(overlays, horse);
        return overlays.isEmpty() ? List.of() : List.copyOf(overlays);
    }

    private static void addSaddleOverlay(List<class_2960> overlays, class_1496 horse) {
        if (overlays == null || horse == null) {
            return;
        }
        class_1799 saddle = horse.method_6118(class_1304.field_55946);
        if (!saddle.method_7960()) {
            overlays.addAll(NeedsOfNatureClient.resolveEquipmentLayerTextures((class_5321<class_10394>)class_10191.field_55981, saddle, NeedsOfNatureClient.resolveHorseSaddleLayerType(horse), "horse_saddle"));
        }
    }

    private static class_10186.class_10190 resolveHorseSaddleLayerType(class_1496 horse) {
        class_1299 type;
        class_1299 class_12992 = type = horse == null ? null : horse.method_5864();
        if (type == class_1299.field_6067) {
            return class_10186.class_10190.field_56127;
        }
        if (type == class_1299.field_6057) {
            return class_10186.class_10190.field_56128;
        }
        if (type == class_1299.field_6075) {
            return class_10186.class_10190.field_56130;
        }
        if (type == class_1299.field_6048) {
            return class_10186.class_10190.field_56129;
        }
        return class_10186.class_10190.field_56126;
    }

    private static class_2960 mapHorseMarkingTexture(class_5148 marking) {
        if (marking == null || marking == class_5148.field_23808) {
            return null;
        }
        if (marking == class_5148.field_23809) {
            return class_2960.method_60656((String)"textures/entity/horse/horse_markings_white.png");
        }
        if (marking == class_5148.field_23810) {
            return class_2960.method_60656((String)"textures/entity/horse/horse_markings_whitefield.png");
        }
        if (marking == class_5148.field_23811) {
            return class_2960.method_60656((String)"textures/entity/horse/horse_markings_whitedots.png");
        }
        if (marking == class_5148.field_23812) {
            return class_2960.method_60656((String)"textures/entity/horse/horse_markings_blackdots.png");
        }
        return null;
    }

    private static List<class_2960> resolveHorseArmorTextures(class_1799 armor) {
        return NeedsOfNatureClient.resolveEquipmentLayerTextures(armor, class_10186.class_10190.field_54129, "horse_armor");
    }

    private static List<class_2960> resolveEquipmentLayerTextures(class_1799 stack, class_10186.class_10190 layerType, String tintCachePrefix) {
        if (stack == null || stack.method_7960() || layerType == null) {
            return List.of();
        }
        class_10192 equippable = (class_10192)stack.method_58694(class_9334.field_54196);
        if (equippable == null) {
            return List.of();
        }
        Optional assetId = equippable.comp_3176();
        if (assetId.isEmpty()) {
            return List.of();
        }
        return NeedsOfNatureClient.resolveEquipmentLayerTextures((class_5321<class_10394>)((class_5321)assetId.get()), stack, layerType, tintCachePrefix);
    }

    private static List<class_2960> resolveEquipmentLayerTextures(class_5321<class_10394> assetId, class_1799 stack, class_10186.class_10190 layerType, String tintCachePrefix) {
        if (assetId == null || layerType == null) {
            return List.of();
        }
        class_10201 loader = NeedsOfNatureClient.resolveEquipmentModelLoader();
        if (loader == null) {
            return List.of();
        }
        class_10186 model = loader.method_64087(assetId);
        if (model == null) {
            return List.of();
        }
        List layers = model.method_63996(layerType);
        if (layers == null || layers.isEmpty()) {
            return List.of();
        }
        class_9282 dyed = (class_9282)stack.method_58694(class_9334.field_49644);
        ArrayList<class_2960> textures = new ArrayList<class_2960>(layers.size());
        for (class_10186.class_10189 layer : layers) {
            class_2960 texture;
            if (layer == null || (texture = layer.method_64007(layerType)) == null) continue;
            if (layer.comp_3172().isPresent()) {
                Integer tint;
                Optional fallbackColor = ((class_10186.class_10188)layer.comp_3172().get()).comp_3170();
                Integer n = tint = dyed != null ? Integer.valueOf(dyed.comp_2384()) : (Integer)fallbackColor.orElse(null);
                if (tint != null) {
                    texture = NeedsOfNatureClient.getTintedEquipmentTexture(texture, tint, tintCachePrefix);
                }
            }
            textures.add(texture);
        }
        return textures.isEmpty() ? List.of() : List.copyOf(textures);
    }

    @Nullable
    private static class_10201 resolveEquipmentModelLoader() {
        class_310 client = class_310.method_1551();
        if (client == null) {
            return null;
        }
        class_898 renderManager = client.method_1561();
        if (!(renderManager instanceof EntityRenderManagerAccessor)) {
            return null;
        }
        EntityRenderManagerAccessor accessor = (EntityRenderManagerAccessor)renderManager;
        return accessor.afw$getEquipmentModelLoader();
    }

    private static List<class_2960> resolveWolfOverlays(class_1309 entity) {
        class_1767 collarColor;
        if (!(entity instanceof class_1493)) {
            return List.of();
        }
        class_1493 wolf = (class_1493)entity;
        ArrayList<class_2960> overlays = new ArrayList<class_2960>();
        class_1799 armor = wolf.method_6118(class_1304.field_48824);
        if (!armor.method_7960()) {
            overlays.addAll(NeedsOfNatureClient.resolveEquipmentLayerTextures(armor, class_10186.class_10190.field_54128, "wolf_armor"));
        }
        if (wolf.method_6181() && (collarColor = wolf.method_6713()) != null) {
            overlays.add(NeedsOfNatureClient.getTintedTexture(WOLF_COLLAR_TEXTURE, collarColor.method_7787(), "wolf_collar", WOLF_COLLAR_TINT_CACHE));
        }
        return overlays.isEmpty() ? List.of() : List.copyOf(overlays);
    }

    private static class_2960 getTintedTexture(class_2960 baseTexture, int rgb, String cachePrefix, Map<Integer, class_2960> cache) {
        class_2960 class_29602;
        block11: {
            class_2960 cached = cache.get(rgb);
            if (cached != null) {
                return cached;
            }
            class_310 client = class_310.method_1551();
            if (client == null) {
                return baseTexture;
            }
            Optional resourceOpt = client.method_1478().method_14486(baseTexture);
            if (resourceOpt.isEmpty()) {
                return baseTexture;
            }
            class_3298 resource = (class_3298)resourceOpt.get();
            InputStream in = resource.method_14482();
            try {
                class_1011 image = class_1011.method_4309((InputStream)in);
                NeedsOfNatureClient.tintImage(image, rgb);
                String idPath = "textures/dynamic/" + cachePrefix + "_" + String.format("%06x", rgb) + ".png";
                class_2960 id = class_2960.method_60655((String)"needsofnature", (String)idPath);
                client.method_1531().method_4616(id, (class_1044)new class_1043(() -> ((class_2960)id).toString(), image));
                cache.put(rgb, id);
                class_29602 = id;
                if (in == null) break block11;
            }
            catch (Throwable throwable) {
                try {
                    if (in != null) {
                        try {
                            in.close();
                        }
                        catch (Throwable throwable2) {
                            throwable.addSuppressed(throwable2);
                        }
                    }
                    throw throwable;
                }
                catch (IOException e) {
                    NeedsOfNature.LOGGER.debug("[NoN] Failed to tint texture {} (rgb={})", new Object[]{baseTexture, rgb, e});
                    return baseTexture;
                }
            }
            in.close();
        }
        return class_29602;
    }

    private static class_2960 getTintedEquipmentTexture(class_2960 baseTexture, int rgb, String cachePrefix) {
        class_2960 class_29602;
        block12: {
            if (baseTexture == null) {
                return null;
            }
            int tint = rgb & 0xFFFFFF;
            String cacheKey = String.valueOf(baseTexture) + "|" + tint;
            class_2960 cached = EQUIPMENT_TINT_CACHE.get(cacheKey);
            if (cached != null) {
                return cached;
            }
            class_310 client = class_310.method_1551();
            if (client == null) {
                return baseTexture;
            }
            Optional resourceOpt = client.method_1478().method_14486(baseTexture);
            if (resourceOpt.isEmpty()) {
                return baseTexture;
            }
            class_3298 resource = (class_3298)resourceOpt.get();
            InputStream in = resource.method_14482();
            try {
                class_1011 image = class_1011.method_4309((InputStream)in);
                NeedsOfNatureClient.tintImage(image, tint);
                String textureKey = (baseTexture.method_12836() + "_" + baseTexture.method_12832()).replace('/', '_').replace('.', '_');
                String idPath = "textures/dynamic/" + cachePrefix + "_" + textureKey + "_" + String.format("%06x", tint) + ".png";
                class_2960 id = class_2960.method_60655((String)"needsofnature", (String)idPath);
                client.method_1531().method_4616(id, (class_1044)new class_1043(() -> ((class_2960)id).toString(), image));
                EQUIPMENT_TINT_CACHE.put(cacheKey, id);
                class_29602 = id;
                if (in == null) break block12;
            }
            catch (Throwable throwable) {
                try {
                    if (in != null) {
                        try {
                            in.close();
                        }
                        catch (Throwable throwable2) {
                            throwable.addSuppressed(throwable2);
                        }
                    }
                    throw throwable;
                }
                catch (IOException | RuntimeException e) {
                    NeedsOfNature.LOGGER.debug("[NoN] Failed to tint equipment texture {} (rgb={})", new Object[]{baseTexture, tint, e});
                    return baseTexture;
                }
            }
            in.close();
        }
        return class_29602;
    }

    private static void tintImage(class_1011 image, int rgb) {
        int tintR = rgb >> 16 & 0xFF;
        int tintG = rgb >> 8 & 0xFF;
        int tintB = rgb & 0xFF;
        int width = image.method_4307();
        int height = image.method_4323();
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int argb = image.method_61940(x, y);
                int alpha = argb >>> 24 & 0xFF;
                if (alpha == 0) continue;
                int r = argb >>> 16 & 0xFF;
                int g = argb >>> 8 & 0xFF;
                int b = argb & 0xFF;
                int outR = r * tintR / 255;
                int outG = g * tintG / 255;
                int outB = b * tintB / 255;
                int outArgb = alpha << 24 | outR << 16 | outG << 8 | outB;
                image.method_61941(x, y, outArgb);
            }
        }
    }

    private static void handleVoluntaryStopInput(class_310 client) {
        if (client == null) {
            return;
        }
        class_746 player = client.field_1724;
        if (player == null) {
            return;
        }
        while (client.field_1690.field_1832.method_1436()) {
            UUID instanceId;
            if (!AfwClientAnimationRuntime.isActorPendingOrActive((UUID)player.method_5667()) || (instanceId = AfwClientAnimationRuntime.findLatestActiveInstanceContaining((UUID)player.method_5667())) == null || !ClientPlayNetworking.canSend(StopVoluntaryAnimationC2SPayload.ID)) continue;
            ClientPlayNetworking.send((class_8710)new StopVoluntaryAnimationC2SPayload(instanceId));
        }
    }

    private static void handleAttackInput(class_310 client) {
        if (client == null || client.field_1687 == null) {
            return;
        }
        class_746 player = client.field_1724;
        if (player == null) {
            return;
        }
        UUID instanceId = AfwClientAnimationRuntime.findLatestActiveInstanceContaining((UUID)player.method_5667());
        if (!NonHudOverlay.isAttackInstance(instanceId)) {
            return;
        }
        if (NonHudOverlay.consumeAttackInputClear()) {
            NeedsOfNatureClient.drainPendingPresses(client.field_1690.field_1913);
            NeedsOfNatureClient.drainPendingPresses(client.field_1690.field_1849);
            return;
        }
        int leftPresses = NeedsOfNatureClient.drainPendingPresses(client.field_1690.field_1913);
        int rightPresses = NeedsOfNatureClient.drainPendingPresses(client.field_1690.field_1849);
        if (leftPresses > 0 && rightPresses > 0) {
            return;
        }
        if (leftPresses > 0) {
            NonHudOverlay.recordAttackInput(instanceId, NonHudOverlay.AttackKey.LEFT);
        } else if (rightPresses > 0) {
            NonHudOverlay.recordAttackInput(instanceId, NonHudOverlay.AttackKey.RIGHT);
        }
        if (NonHudOverlay.isAttackProgressComplete() && ClientPlayNetworking.canSend(StopAttackAnimationC2SPayload.ID)) {
            ClientPlayNetworking.send((class_8710)new StopAttackAnimationC2SPayload(instanceId));
            NonHudOverlay.resetAttackProgress();
        }
    }

    private static void handleManualPeakInput(class_310 client) {
        if (client == null || client.field_1687 == null) {
            return;
        }
        class_746 player = client.field_1724;
        if (player == null || player.method_7325()) {
            return;
        }
        while (START_MANUAL_PEAK_KEY.method_1436()) {
            UUID activeInstance = AfwClientAnimationRuntime.findLatestActiveInstanceContaining((UUID)player.method_5667());
            if (activeInstance != null || !ClientPlayNetworking.canSend(StartManualPeakC2SPayload.ID)) continue;
            ClientPlayNetworking.send((class_8710)new StartManualPeakC2SPayload());
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
            ClientPlayNetworking.send((class_8710)new SetPlayerGenderC2SPayload(mask, false));
        }
        NonWildfireGenderSync.syncFromNonConfig(config);
        NonWildfireGenderSync.syncDestroyedSkinOverrides(config);
    }

    private static void syncLocalGenderMirror(class_310 client) {
        if (client == null || client.field_1724 == null) {
            return;
        }
        class_746 class_7462 = client.field_1724;
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
                if (instanceId != null && context.anchor() != null && !(replacedByAfw = NeedsOfNatureClient.trySetAfwRightHandPropOverride(instanceId, context.anchor().method_5667(), NeedsOfNatureClient.createFilledBottlePropStack(context.anchor())))) {
                    FILLED_BOTTLE_PROP_INSTANCES.add(instanceId);
                }
                return AfwSoundCueEvents.SoundOverride.play((class_2960)VANILLA_BOTTLE_FILL_SOUND, (float)context.volume(), (float)context.pitch());
            }
            class_2960 soundId = NonSoundCues.resolveSoundId(context.effect(), context.anchor(), context.animationId(), context.stageIndex());
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
            return AfwSoundCueEvents.SoundOverride.play((class_2960)soundId, (float)volume, (float)context.pitch());
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
        class_2960 logicalStageId = stage.animationId();
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
        class_310 client = class_310.method_1551();
        if (client == null || client.field_1724 == null) {
            return false;
        }
        UUID playerInstance = AfwClientAnimationRuntime.findLatestActiveInstanceContaining((UUID)client.field_1724.method_5667());
        return instanceId.equals(playerInstance);
    }

    private static void tickIntifaceBaseline(class_310 client) {
        boolean active = false;
        boolean peak = false;
        double specialVibratorScalar = Double.NaN;
        if (client != null && client.field_1724 != null) {
            IntifaceAnimationState state = NeedsOfNatureClient.findLocalIntifaceAnimationState(client);
            boolean bl = active = state != null;
            if (state != null) {
                peak = NonSoundCues.isPeakAnimationStage(state.stageAnimationId(), state.stageIndex());
                specialVibratorScalar = NeedsOfNatureClient.resolveSpecialIntifaceVibratorScalar(state);
            } else {
                INTIFACE_BIRTH_CUTOFF_INSTANCES.clear();
            }
        }
        int energizedLevel = client != null && client.field_1724 != null ? NonHudOverlay.getEnergizedLevel(client.field_1724) : 0;
        double energizedPulse = energizedLevel > 0 ? NonHudOverlay.computeHeartPulse(energizedLevel) : 0.0;
        NonIntifaceBridge.clientTick(NeedsOfNature.getConfig(), active, peak, specialVibratorScalar, energizedLevel, energizedPulse);
    }

    @Nullable
    private static IntifaceAnimationState findLocalIntifaceAnimationState(class_310 client) {
        if (client == null || client.field_1724 == null || client.field_1687 == null) {
            return null;
        }
        UUID playerUuid = client.field_1724.method_5667();
        UUID instanceId = AfwClientAnimationRuntime.findLatestActiveInstanceContaining((UUID)playerUuid);
        if (instanceId == null) {
            return null;
        }
        AnimationStageInfo stage = AfwClientAnimationRuntime.findCurrentStage((UUID)instanceId);
        if (stage == null) {
            return null;
        }
        class_2960 stageAnimationId = stage.effectiveAnimationId();
        class_2960 logicalStageAnimationId = stage.animationId();
        class_2960 animationId = NonPeakStages.baseAnimationId(logicalStageAnimationId);
        Integer stageNumber = NonPeakStages.stageNumberFromId(logicalStageAnimationId);
        int stageIndex = stageNumber == null ? 0 : Math.max(0, stageNumber - 1);
        long stageStartTick = AfwClientAnimationRuntime.findStageStartTick((UUID)instanceId);
        double speed = Math.max(1.0E-4, AfwClientAnimationRuntime.findLatestSpeedForActor((UUID)playerUuid));
        double elapsedTicks = Math.max(0.0, (double)(client.field_1687.method_75260() - stageStartTick) * speed);
        double cycleTicks = NonLoopSecondsOverrides.getCycleTicks(logicalStageAnimationId);
        if (cycleTicks <= 0.0) {
            cycleTicks = stage.lengthTicks();
        }
        double durationTicks = NeedsOfNatureClient.resolveIntifaceStageDurationTicks(animationId, logicalStageAnimationId, stageIndex, cycleTicks);
        return new IntifaceAnimationState(instanceId, animationId, stageAnimationId, stageIndex, elapsedTicks, durationTicks);
    }

    private static double resolveIntifaceStageDurationTicks(class_2960 animationId, class_2960 stageAnimationId, int stageIndex, double cycleLengthTicks) {
        int seconds;
        double multiplier = NonLoopSecondsOverrides.getStageDurationMultiplier(stageAnimationId);
        Integer override = NonLoopSecondsOverrides.getOverrideSeconds(stageAnimationId);
        if (NonLoopSecondsOverrides.isInfinite(override)) {
            override = null;
        }
        if (override != null) {
            return Math.max(1.0, (double)Math.max(1, override) * 20.0 * multiplier);
        }
        class_2960 baseId = NonPeakStages.baseAnimationId(stageAnimationId);
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
        class_2960 stagedId = NeedsOfNatureClient.stagedAnimationId(state.animationId(), state.stageIndex() + 1);
        return NonPregnancyCues.getCueTick(stagedId);
    }

    @Nullable
    private static class_2960 stagedAnimationId(class_2960 animationId, int stageNumber) {
        if (animationId == null || stageNumber <= 0) {
            return null;
        }
        String path = animationId.method_12832();
        if (NonPeakStages.stageNumberFromId(animationId) != null) {
            return animationId;
        }
        return class_2960.method_60655((String)animationId.method_12836(), (String)(path + ".p" + stageNumber));
    }

    private static boolean isManualPeakAnimation(class_2960 animationId) {
        return animationId != null && animationId.method_12832().contains("manual_peak");
    }

    private static boolean isBirthAnimation(@Nullable class_2960 animationId, @Nullable class_2960 stageAnimationId) {
        return NeedsOfNatureClient.hasBirthAnimationPath(animationId) || NeedsOfNatureClient.hasBirthAnimationPath(stageAnimationId);
    }

    private static boolean hasBirthAnimationPath(@Nullable class_2960 animationId) {
        if (animationId == null) {
            return false;
        }
        return animationId.method_12832().contains("birth");
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

    private static Map<String, AfwGeckoModelEvents.BoneItemProp> resolveFillBottleFallbackBoneItems(class_1309 entity) {
        if (entity == null || NeedsOfNatureClient.getAfwRightPropOverrideMethod() != null) {
            return Map.of();
        }
        UUID instanceId = AfwClientAnimationRuntime.findLatestActiveInstanceContaining((UUID)entity.method_5667());
        if (instanceId == null || !FILLED_BOTTLE_PROP_INSTANCES.contains(instanceId)) {
            return Map.of();
        }
        class_2960 animationId = AfwClientAnimationRuntime.findLatestActiveAnimationIdContaining((UUID)entity.method_5667());
        if (!NeedsOfNatureClient.isFillBottleAnimation(animationId)) {
            return Map.of();
        }
        return Map.of(RIGHT_PROP_BONE, new AfwGeckoModelEvents.BoneItemProp(NeedsOfNatureClient.createFilledBottlePropStack(entity)));
    }

    private static boolean trySetAfwRightHandPropOverride(UUID instanceId, UUID actorUuid, class_1799 stack) {
        if (instanceId == null || actorUuid == null || stack == null || stack.method_7960()) {
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
        if (!class_7923.field_41178.method_10250(payload.itemId())) {
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
        if (!class_7923.field_41178.method_10250(pending.itemId())) {
            return true;
        }
        NeedsOfNatureClient.trySetAfwRightHandPropOverride(pending.instanceId(), pending.actorUuid(), new class_1799((class_1935)class_7923.field_41178.method_63535(pending.itemId())));
        return true;
    }

    private static void applyPendingManualPeakPropOverrides(class_310 client) {
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
            AFW_RIGHT_PROP_OVERRIDE_METHOD = method = AfwClientAnimationRuntime.class.getMethod("setRightHandPropOverride", UUID.class, UUID.class, class_1799.class);
            return method;
        }
        catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static void pruneFilledBottlePropInstances(class_310 client) {
        if (FILLED_BOTTLE_PROP_INSTANCES.isEmpty()) {
            return;
        }
        FILLED_BOTTLE_PROP_INSTANCES.removeIf(instanceId -> instanceId == null || AfwClientAnimationRuntime.findCurrentStage((UUID)instanceId) == null);
    }

    private static int drainPendingPresses(class_304 key) {
        int count = 0;
        while (key.method_1436()) {
            ++count;
        }
        return count;
    }

    private static void triggerReactiveImpactFovPulse() {
        class_310 client = class_310.method_1551();
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
        class_310 client = class_310.method_1551();
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
        float t = class_3532.method_15363((float)(recovery / 14.0f), (float)0.0f, (float)1.0f);
        float remaining = 1.0f - t;
        float eased = remaining * remaining;
        return -0.5f * eased;
    }

    private static long getCurrentClientTicks(class_310 client) {
        if (client != null && client.field_1687 != null) {
            return client.field_1687.method_75260();
        }
        return CLIENT_FALLBACK_TICKS;
    }

    private record IntifaceAnimationState(UUID instanceId, class_2960 animationId, class_2960 stageAnimationId, int stageIndex, double stageElapsedTicks, double stageDurationTicks) {
        double stageProgress() {
            if (this.stageDurationTicks <= 0.0) {
                return 0.0;
            }
            return Math.max(0.0, Math.min(1.0, this.stageElapsedTicks / this.stageDurationTicks));
        }
    }

    private record PendingManualPeakPropOverride(UUID instanceId, UUID actorUuid, class_2960 itemId, long expireTick) {
    }
}

