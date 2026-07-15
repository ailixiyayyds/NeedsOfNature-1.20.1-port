/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
 *  net.fabricmc.loader.api.FabricLoader
 *  net.minecraft.class_1011
 *  net.minecraft.class_1011$class_1012
 *  net.minecraft.class_1043
 *  net.minecraft.class_1044
 *  net.minecraft.class_124
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_2561
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_3298
 *  net.minecraft.class_5819
 *  net.minecraft.class_703
 *  net.minecraft.class_742
 *  net.minecraft.class_7920
 *  net.minecraft.class_8710
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid.client;

import com.nonid.DestroyedSkinHolder;
import com.nonid.GenderHolder;
import com.nonid.NeedsOfNature;
import com.nonid.NeedsOfNatureClient;
import com.nonid.NonConfig;
import com.nonid.client.NonAccessorySkinOverlays;
import com.nonid.client.NonWildfireGenderSync;
import com.nonid.client.particle.RippedFabricParticle;
import com.nonid.network.DestroyedSkinMaskUploadC2SPayload;
import com.nonid.network.DestroyedSkinUploadC2SPayload;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_1044;
import net.minecraft.class_124;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3298;
import net.minecraft.class_5819;
import net.minecraft.class_703;
import net.minecraft.class_742;
import net.minecraft.class_7920;
import net.minecraft.class_8710;
import org.jetbrains.annotations.Nullable;

public final class NonDestroyedSkinClient {
    private static final int MIN_STAGE = 0;
    private static final int MAX_STAGE = 4;
    private static final int FALLBACK_FABRIC_RGB = 15920872;
    private static final int FALLBACK_SKIN_TINT_RGB = 13803641;
    private static final class_2960 RIPPED_FABRIC_COLOR_MASK = class_2960.method_60655((String)"needsofnature", (String)"textures/particle/ripped_fabric_color_mask.png");
    private static final class_2960 DEFAULT_DESTROYED_TEXTURE_F = class_2960.method_60655((String)"needsofnature", (String)"textures/player_destroyed/destroyed_texture_f.png");
    private static final class_2960 DEFAULT_DESTROYED_TEXTURE_M = class_2960.method_60655((String)"needsofnature", (String)"textures/player_destroyed/destroyed_texture_m.png");
    private static final class_2960 DEFAULT_DESTROYED_TEXTURE_F_SLIM = class_2960.method_60655((String)"needsofnature", (String)"textures/player_destroyed/destroyed_texture_f_slim.png");
    private static final class_2960 DEFAULT_DESTROYED_TEXTURE_F_WIDE = class_2960.method_60655((String)"needsofnature", (String)"textures/player_destroyed/destroyed_texture_f_wide.png");
    private static final class_2960 DEFAULT_DESTROYED_TEXTURE_M_SLIM = class_2960.method_60655((String)"needsofnature", (String)"textures/player_destroyed/destroyed_texture_m_slim.png");
    private static final class_2960 DEFAULT_DESTROYED_TEXTURE_M_WIDE = class_2960.method_60655((String)"needsofnature", (String)"textures/player_destroyed/destroyed_texture_m_wide.png");
    private static final String DEFAULT_PLAYER_SKIN_PREFIX = "textures/entity/player/";
    private static final String DEFAULT_DESTROYED_SKIN_PREFIX = "textures/player_destroyed/default/";
    private static final String[] VANILLA_DEFAULT_SKIN_NAMES = new String[]{"steve", "alex", "ari", "efe", "kai", "makena", "noor", "sunny", "zuri"};
    private static final double DEFAULT_SKIN_PIXEL_MATCH_THRESHOLD = 0.9;
    private static final class_2960[] MASK_TEXTURES = new class_2960[]{null, class_2960.method_60655((String)"needsofnature", (String)"textures/player_destroyed/mask_1.png"), class_2960.method_60655((String)"needsofnature", (String)"textures/player_destroyed/mask_2.png"), class_2960.method_60655((String)"needsofnature", (String)"textures/player_destroyed/mask_3.png")};
    private static final class_2960[][] MESS_MASK_TEXTURES = new class_2960[][]{{class_2960.method_60655((String)"needsofnature", (String)"textures/entity/player/mess/v_mess1.png"), class_2960.method_60655((String)"needsofnature", (String)"textures/entity/player/mess/v_mess2.png"), class_2960.method_60655((String)"needsofnature", (String)"textures/entity/player/mess/v_mess3.png")}, {class_2960.method_60655((String)"needsofnature", (String)"textures/entity/player/mess/a_mess1.png"), class_2960.method_60655((String)"needsofnature", (String)"textures/entity/player/mess/a_mess2.png"), class_2960.method_60655((String)"needsofnature", (String)"textures/entity/player/mess/a_mess3.png")}, {class_2960.method_60655((String)"needsofnature", (String)"textures/entity/player/mess/m_mess1.png"), class_2960.method_60655((String)"needsofnature", (String)"textures/entity/player/mess/m_mess2.png"), class_2960.method_60655((String)"needsofnature", (String)"textures/entity/player/mess/m_mess3.png")}};
    private static final class_2960 V_MESS_TANK_TEXTURE = class_2960.method_60655((String)"needsofnature", (String)"textures/entity/player/mess/v_mess_tank.png");
    private static final class_2960 A_MESS_TANK_TEXTURE = class_2960.method_60655((String)"needsofnature", (String)"textures/entity/player/mess/a_mess_tank.png");
    private static final Map<UUID, byte[]> DESTROYED_SKINS = new HashMap<UUID, byte[]>();
    private static final Map<UUID, byte[][]> DESTROYED_SKIN_MASKS = new HashMap<UUID, byte[][]>();
    private static final Map<UUID, Integer> STAGES = new HashMap<UUID, Integer>();
    private static final Map<UUID, MessState> MESS_STATES = new HashMap<UUID, MessState>();
    private static final Map<TextureKey, class_2960> TEXTURE_CACHE = new HashMap<TextureKey, class_2960>();
    private static final Map<class_2960, int[]> ORIGINAL_SKIN_COLOR_CACHE = new HashMap<class_2960, int[]>();
    private static int uploadRetryTicks = 0;
    private static boolean uploadedThisConnection = false;
    private static boolean automaticSkinUnavailableThisConnection = false;
    @Nullable
    private static class_2960 lastUploadedAutoBaseSkin = null;

    private NonDestroyedSkinClient() {
    }

    public static void scheduleLocalSkinUploadRetry() {
        uploadRetryTicks = 300;
        uploadedThisConnection = false;
        automaticSkinUnavailableThisConnection = false;
        lastUploadedAutoBaseSkin = null;
    }

    public static void clientTick(class_310 client) {
        if (client == null) {
            return;
        }
        if (uploadRetryTicks <= 0) {
            return;
        }
        --uploadRetryTicks;
        if (!ClientPlayNetworking.canSend(DestroyedSkinUploadC2SPayload.ID)) {
            if (uploadRetryTicks == 0) {
                NeedsOfNature.LOGGER.info("[NoN] Destroyed skin upload retry expired: upload channel never became sendable.");
            }
            return;
        }
        boolean localCustomSkin = Files.isRegularFile(NonDestroyedSkinClient.localDestroyedSkinPath(), new LinkOption[0]);
        if (localCustomSkin && uploadedThisConnection) {
            return;
        }
        if (!localCustomSkin && automaticSkinUnavailableThisConnection) {
            uploadRetryTicks = 0;
            NonDestroyedSkinClient.uploadLocalMasksIfPresent();
            return;
        }
        boolean uploadedSkin = NonDestroyedSkinClient.uploadLocalSkinIfPresent();
        NonDestroyedSkinClient.uploadLocalMasksIfPresent();
        if (localCustomSkin && (uploadedThisConnection |= uploadedSkin)) {
            uploadRetryTicks = 0;
        } else if (uploadRetryTicks == 0 && !uploadedThisConnection && !automaticSkinUnavailableThisConnection) {
            NeedsOfNature.LOGGER.info("[NoN] Destroyed skin upload retry expired without upload.");
        }
    }

    public static boolean uploadLocalSkinIfPresent() {
        Path path = NonDestroyedSkinClient.localDestroyedSkinPath();
        if (!Files.isRegularFile(path, new LinkOption[0])) {
            return NonDestroyedSkinClient.uploadGeneratedSkinIfPossible();
        }
        try {
            byte[] bytes = Files.readAllBytes(path);
            if (!NonDestroyedSkinClient.isValidSkinPng(bytes)) {
                NonDestroyedSkinClient.setupWarn("[NoN] Ignoring destroyed skin {}: expected a 64x64 PNG.", path);
                return false;
            }
            if (ClientPlayNetworking.canSend(DestroyedSkinUploadC2SPayload.ID)) {
                ClientPlayNetworking.send((class_8710)new DestroyedSkinUploadC2SPayload(bytes));
                return true;
            }
        }
        catch (IOException | RuntimeException e) {
            NonDestroyedSkinClient.setupWarn("[NoN] Failed to upload local destroyed skin from {}.", path, e);
        }
        return false;
    }

    public static boolean hasLocalCustomDestroyedSkin() {
        return Files.isRegularFile(NonDestroyedSkinClient.localDestroyedSkinPath(), new LinkOption[0]);
    }

    public static boolean hasCurrentDefaultDestroyedSkin() {
        class_310 client = class_310.method_1551();
        if (client == null || client.field_1724 == null) {
            return false;
        }
        class_2960 baseSkin = client.field_1724.method_52814().comp_1626().comp_3627();
        if (baseSkin == null) {
            return false;
        }
        boolean defaultSkinPath = NonDestroyedSkinClient.isLikelyDefaultPlayerSkin(baseSkin);
        boolean profileHasCustomTexture = NonDestroyedSkinClient.hasProfileTextureProperties(client);
        if (defaultSkinPath && profileHasCustomTexture) {
            return false;
        }
        DefaultSkinKey key = NonDestroyedSkinClient.resolveDefaultSkinKey(client, baseSkin);
        if (key == null) {
            return false;
        }
        return NonDestroyedSkinClient.readDefaultDestroyedSkin(client, key) != null;
    }

    private static Path localDestroyedSkinPath() {
        return FabricLoader.getInstance().getConfigDir().resolve("needsofnature").resolve("destroyed_skin.png");
    }

    private static boolean uploadGeneratedSkinIfPossible() {
        byte[] defaultDestroyedSkin;
        class_310 client = class_310.method_1551();
        if (client == null || client.field_1724 == null) {
            return false;
        }
        if (!ClientPlayNetworking.canSend(DestroyedSkinUploadC2SPayload.ID)) {
            return false;
        }
        if (automaticSkinUnavailableThisConnection) {
            return false;
        }
        class_2960 baseSkin = client.field_1724.method_52814().comp_1626().comp_3627();
        if (baseSkin == null) {
            return false;
        }
        if (baseSkin.equals((Object)lastUploadedAutoBaseSkin)) {
            return true;
        }
        boolean defaultSkinPath = NonDestroyedSkinClient.isLikelyDefaultPlayerSkin(baseSkin);
        boolean profileHasCustomTexture = NonDestroyedSkinClient.hasProfileTextureProperties(client);
        if (defaultSkinPath && profileHasCustomTexture) {
            return false;
        }
        DefaultSkinKey defaultSkinKey = NonDestroyedSkinClient.resolveDefaultSkinKey(client, baseSkin);
        if (defaultSkinKey != null && (defaultDestroyedSkin = NonDestroyedSkinClient.readDefaultDestroyedSkin(client, defaultSkinKey)) != null && NonDestroyedSkinClient.isValidSkinPng(defaultDestroyedSkin)) {
            ClientPlayNetworking.send((class_8710)new DestroyedSkinUploadC2SPayload(defaultDestroyedSkin));
            lastUploadedAutoBaseSkin = baseSkin;
            return true;
        }
        try {
            byte[] bytes = NonDestroyedSkinClient.createGeneratedDestroyedSkin(client, baseSkin);
            if (bytes == null || !NonDestroyedSkinClient.isValidSkinPng(bytes)) {
                return false;
            }
            ClientPlayNetworking.send((class_8710)new DestroyedSkinUploadC2SPayload(bytes));
            lastUploadedAutoBaseSkin = baseSkin;
            return true;
        }
        catch (MissingDestroyedSkinFallbackException e) {
            NonDestroyedSkinClient.markAutomaticSkinUnavailable("[NoN] No ripped skin fallback texture was found. Install the default NoN pack or provide {} to enable ripped skin visuals.", NonDestroyedSkinClient.localDestroyedSkinPath());
            return false;
        }
        catch (IOException | RuntimeException e) {
            NonDestroyedSkinClient.setupWarn("[NoN] Failed to generate ripped skin fallback. Automatic ripped skin upload is disabled for this connection.", e);
            automaticSkinUnavailableThisConnection = true;
            uploadRetryTicks = 0;
            return false;
        }
    }

    public static boolean uploadLocalMasksIfPresent() {
        boolean uploadedAny = false;
        for (int stage = 1; stage < 4; ++stage) {
            Path path = FabricLoader.getInstance().getConfigDir().resolve("needsofnature").resolve("destroyed_skin_mask_" + stage + ".png");
            if (!Files.isRegularFile(path, new LinkOption[0])) continue;
            try {
                byte[] bytes = Files.readAllBytes(path);
                if (!NonDestroyedSkinClient.isValidSkinPng(bytes)) {
                    NonDestroyedSkinClient.setupWarn("[NoN] Ignoring destroyed skin mask {}: expected a 64x64 PNG.", path);
                    continue;
                }
                if (!ClientPlayNetworking.canSend(DestroyedSkinMaskUploadC2SPayload.ID)) continue;
                ClientPlayNetworking.send((class_8710)new DestroyedSkinMaskUploadC2SPayload(stage, bytes));
                uploadedAny = true;
                continue;
            }
            catch (IOException | RuntimeException e) {
                NonDestroyedSkinClient.setupWarn("[NoN] Failed to upload local destroyed skin mask from {}.", path, e);
            }
        }
        return uploadedAny;
    }

    /*
     * Enabled aggressive exception aggregation
     */
    public static int detectSkinTintFromCurrentPlayer(int fallbackRgb) {
        class_310 client = class_310.method_1551();
        if (client == null || client.field_1724 == null) {
            return fallbackRgb & 0xFFFFFF;
        }
        class_2960 baseSkin = client.field_1724.method_52814().comp_1626().comp_3627();
        if (baseSkin == null) {
            return fallbackRgb & 0xFFFFFF;
        }
        try {
            class_3298 resource = client.method_1478().method_14486(baseSkin).orElse(null);
            if (resource == null) {
                return fallbackRgb & 0xFFFFFF;
            }
            try (InputStream in = resource.method_14482();){
                int n;
                block17: {
                    class_1011 image = class_1011.method_4309((InputStream)in);
                    try {
                        n = NonDestroyedSkinClient.detectSkinTint(image);
                        if (image == null) break block17;
                    }
                    catch (Throwable throwable) {
                        if (image != null) {
                            try {
                                image.close();
                            }
                            catch (Throwable throwable2) {
                                throwable.addSuppressed(throwable2);
                            }
                        }
                        throw throwable;
                    }
                    image.close();
                }
                return n;
            }
        }
        catch (IOException | RuntimeException e) {
            NonDestroyedSkinClient.setupWarn("[NoN] Failed to auto-detect destroyed skin tint.", e);
            return fallbackRgb & 0xFFFFFF;
        }
    }

    public static void clear() {
        class_310 client = class_310.method_1551();
        ArrayList<class_2960> texturesToDestroy = new ArrayList<class_2960>(TEXTURE_CACHE.values());
        DESTROYED_SKINS.clear();
        DESTROYED_SKIN_MASKS.clear();
        STAGES.clear();
        MESS_STATES.clear();
        TEXTURE_CACHE.clear();
        ORIGINAL_SKIN_COLOR_CACHE.clear();
        uploadRetryTicks = 0;
        uploadedThisConnection = false;
        automaticSkinUnavailableThisConnection = false;
        lastUploadedAutoBaseSkin = null;
        NonDestroyedSkinClient.destroyTextures(client, texturesToDestroy);
    }

    public static void setSkin(UUID playerUuid, int stage, byte[] pngBytes) {
        if (playerUuid == null || pngBytes == null || pngBytes.length == 0) {
            return;
        }
        if (!NonDestroyedSkinClient.isValidSkinPng(pngBytes)) {
            NonDestroyedSkinClient.setupWarn("[NoN] Ignoring synced destroyed skin for {}: expected a 64x64 PNG.", playerUuid);
            return;
        }
        NonDestroyedSkinClient.clearCachedTextures(playerUuid);
        DESTROYED_SKINS.put(playerUuid, (byte[])pngBytes.clone());
        NonDestroyedSkinClient.setStage(playerUuid, stage);
    }

    public static void setMask(UUID playerUuid, int stage, byte[] pngBytes) {
        if (playerUuid == null || pngBytes == null || pngBytes.length == 0) {
            return;
        }
        if (stage <= 0 || stage >= 4) {
            return;
        }
        if (!NonDestroyedSkinClient.isValidSkinPng(pngBytes)) {
            NonDestroyedSkinClient.setupWarn("[NoN] Ignoring synced destroyed skin mask for {} stage {}: expected a 64x64 PNG.", playerUuid, stage);
            return;
        }
        byte[][] masks = DESTROYED_SKIN_MASKS.computeIfAbsent(playerUuid, ignored -> new byte[4][]);
        masks[stage] = (byte[])pngBytes.clone();
        NonDestroyedSkinClient.clearCachedTextures(playerUuid);
    }

    public static void setStage(UUID playerUuid, int stage) {
        if (playerUuid == null) {
            return;
        }
        int clamped = NonDestroyedSkinClient.clampStage(stage);
        if (clamped <= 0) {
            STAGES.remove(playerUuid);
        } else {
            STAGES.put(playerUuid, clamped);
        }
        class_310 client = class_310.method_1551();
        if (clamped > 0 && client != null && client.field_1724 != null && playerUuid.equals(client.field_1724.method_5667()) && !DESTROYED_SKINS.containsKey(playerUuid)) {
            NonDestroyedSkinClient.uploadLocalSkinIfPresent();
        }
        if (client != null && client.field_1724 != null && playerUuid.equals(client.field_1724.method_5667())) {
            NonWildfireGenderSync.syncDestroyedSkinOverrides(NeedsOfNature.getConfig(), clamped);
        }
        NonDestroyedSkinClient.clearCachedTextures(playerUuid);
    }

    public static int getStage(UUID playerUuid) {
        if (playerUuid == null) {
            return 0;
        }
        return NonDestroyedSkinClient.clampStage(STAGES.getOrDefault(playerUuid, 0));
    }

    public static void setMess(UUID playerUuid, int vMess, int aMess, int mMess, int tintRgb, int liquidStored, int liquidCapacity) {
        if (playerUuid == null) {
            return;
        }
        MessState state = new MessState(NonDestroyedSkinClient.clampMess(vMess), NonDestroyedSkinClient.clampMess(aMess), NonDestroyedSkinClient.clampMess(mMess), tintRgb & 0xFFFFFF, Math.max(0, liquidStored), Math.max(0, liquidCapacity));
        if (!state.hasAnyVisual()) {
            MESS_STATES.remove(playerUuid);
        } else {
            MESS_STATES.put(playerUuid, state);
        }
        NonDestroyedSkinClient.clearCachedTextures(playerUuid);
    }

    @Nullable
    public static class_2960 resolveBaseTexture(class_1309 entity, class_2960 currentBaseTexture) {
        int stage = NonDestroyedSkinClient.getDestroyedStage(entity);
        MessState messState = NonDestroyedSkinClient.getMessState(entity);
        List<class_2960> accessoryOverlays = NonAccessorySkinOverlays.resolveEquippedOverlays(entity);
        if (stage <= 0 && !messState.hasAnyVisual() && accessoryOverlays.isEmpty()) {
            return null;
        }
        return NonDestroyedSkinClient.getOrCreateTexture(entity, stage, currentBaseTexture, false);
    }

    @Nullable
    public static class_2960 resolveOverlayTexture(class_1309 entity) {
        int stage = NonDestroyedSkinClient.getDestroyedStage(entity);
        if (stage <= 0 || stage >= 4) {
            return null;
        }
        return NonDestroyedSkinClient.getOrCreateTexture(entity, stage, null, true);
    }

    public static void spawnRippedFabricParticles(UUID playerUuid, double x, double y, double z, int count, long seed) {
        class_310 client = class_310.method_1551();
        if (client == null || client.field_1687 == null || playerUuid == null || count <= 0) {
            return;
        }
        class_742 player = null;
        for (class_742 candidate : client.field_1687.method_18456()) {
            if (!playerUuid.equals(candidate.method_5667())) continue;
            player = candidate;
            break;
        }
        if (player == null) {
            return;
        }
        class_2960 baseSkin = player.method_52814().comp_1626().comp_3627();
        int[] colors = NonDestroyedSkinClient.getOriginalSkinColors(client, baseSkin);
        class_5819 random = class_5819.method_43049((long)seed);
        int particleCount = Math.max(1, Math.min(12, count));
        for (int i = 0; i < particleCount; ++i) {
            double vz;
            double vy;
            double vx;
            double pz;
            double py;
            int rgb = colors[random.method_43048(colors.length)];
            rgb = NonDestroyedSkinClient.varyColor(rgb, random);
            double px = x + (random.method_43058() - 0.5) * 0.16;
            class_703 particle = RippedFabricParticle.create(client.field_1687, px, py = y + (random.method_43058() - 0.5) * 0.2, pz = z + (random.method_43058() - 0.5) * 0.16, vx = (random.method_43058() - 0.5) * 0.001, vy = -0.006 - random.method_43058() * 0.004, vz = (random.method_43058() - 0.5) * 0.001, rgb, random);
            if (particle == null) continue;
            client.field_1713.method_3058(particle);
        }
    }

    private static int getDestroyedStage(class_1309 entity) {
        DestroyedSkinHolder holder;
        int tracked;
        if (!(entity instanceof class_1657)) {
            return 0;
        }
        if (entity == null) {
            return 0;
        }
        if (entity instanceof DestroyedSkinHolder && (tracked = NonDestroyedSkinClient.clampStage((holder = (DestroyedSkinHolder)entity).getDestroyedSkinStage())) > 0) {
            return tracked;
        }
        return STAGES.getOrDefault(entity.method_5667(), 0);
    }

    private static MessState getMessState(@Nullable class_1309 entity) {
        if (entity == null) {
            return MessState.EMPTY;
        }
        return MESS_STATES.getOrDefault(entity.method_5667(), MessState.EMPTY);
    }

    @Nullable
    private static class_2960 getOrCreateTexture(class_1309 entity, int stage, @Nullable class_2960 currentBaseTexture, boolean overlayFallback) {
        TankMaskType tankMaskType;
        List<Object> accessoryOverlays;
        if (entity == null) {
            return null;
        }
        UUID playerUuid = entity.method_5667();
        byte[] skin = DESTROYED_SKINS.get(playerUuid);
        MessState messState = MESS_STATES.getOrDefault(playerUuid, MessState.EMPTY);
        List<Object> list = accessoryOverlays = overlayFallback ? List.of() : NonAccessorySkinOverlays.resolveEquippedOverlays(entity);
        if ((skin == null || skin.length == 0) && !messState.hasAnyVisual() && accessoryOverlays.isEmpty()) {
            return null;
        }
        TextureKey key = new TextureKey(playerUuid, stage, overlayFallback ? null : currentBaseTexture, messState, tankMaskType = NonDestroyedSkinClient.resolveTankMaskType(entity), accessoryOverlays);
        class_2960 cached = TEXTURE_CACHE.get(key);
        if (cached != null) {
            return cached;
        }
        class_310 client = class_310.method_1551();
        if (client == null) {
            return null;
        }
        try {
            class_1011 image;
            if (stage >= 4 && skin != null && skin.length > 0) {
                image = class_1011.method_49277((byte[])skin);
                NonDestroyedSkinClient.applyMessOverlays(client, image, messState);
                NonDestroyedSkinClient.applyTankOverlay(client, image, messState, tankMaskType);
                NonDestroyedSkinClient.applyAccessoryOverlays(client, image, accessoryOverlays);
            } else {
                image = overlayFallback ? NonDestroyedSkinClient.createMaskedOverlay(client, playerUuid, skin, stage) : NonDestroyedSkinClient.createCompositedSkin(client, playerUuid, skin, stage, currentBaseTexture, messState, tankMaskType, accessoryOverlays);
            }
            if (image == null) {
                return null;
            }
            class_2960 id = class_2960.method_60655((String)"needsofnature", (String)("textures/dynamic/destroyed_skin/" + playerUuid.toString().replace("-", "") + "_" + stage + "_" + messState.cacheKey() + "_" + tankMaskType.id + "_" + Integer.toHexString(accessoryOverlays.hashCode()) + (overlayFallback ? "_overlay" : "_base") + ".png"));
            client.method_1531().method_4616(id, (class_1044)new class_1043(() -> "non_destroyed_skin", image));
            TEXTURE_CACHE.put(key, id);
            return id;
        }
        catch (IOException | RuntimeException e) {
            NonDestroyedSkinClient.setupWarn("[NoN] Failed to create destroyed skin texture for {} stage {}.", playerUuid, stage, e);
            return null;
        }
    }

    @Nullable
    private static class_1011 createCompositedSkin(class_310 client, UUID playerUuid, byte[] skin, int stage, @Nullable class_2960 currentBaseTexture, MessState messState, TankMaskType tankMaskType, List<class_2960> accessoryOverlays) throws IOException {
        if (currentBaseTexture == null) {
            return null;
        }
        class_1011 base = NonDestroyedSkinClient.readTextureImage(client, currentBaseTexture);
        if (base == null) {
            return null;
        }
        class_1011 out = new class_1011(class_1011.class_1012.field_4997, base.method_4307(), base.method_4323(), false);
        out.method_4317(base);
        if (stage > 0 && skin != null && skin.length > 0) {
            class_1011 destroyed = class_1011.method_49277((byte[])skin);
            class_1011 mask = NonDestroyedSkinClient.readMask(client, playerUuid, stage);
            if (mask != null) {
                int width = Math.min(base.method_4307(), destroyed.method_4307());
                int height = Math.min(base.method_4323(), destroyed.method_4323());
                int maskWidth = mask.method_4307();
                int maskHeight = mask.method_4323();
                for (int y = 0; y < height; ++y) {
                    for (int x = 0; x < width; ++x) {
                        int factor = NonDestroyedSkinClient.maskFactor(mask.method_61940(x % maskWidth, y % maskHeight));
                        if (factor <= 0) continue;
                        int baseArgb = base.method_61940(x, y);
                        int destroyedArgb = destroyed.method_61940(x, y);
                        out.method_61941(x, y, NonDestroyedSkinClient.blendArgb(baseArgb, destroyedArgb, factor));
                    }
                }
                mask.close();
            }
            destroyed.close();
        }
        NonDestroyedSkinClient.applyMessOverlays(client, out, messState);
        NonDestroyedSkinClient.applyTankOverlay(client, out, messState, tankMaskType);
        NonDestroyedSkinClient.applyAccessoryOverlays(client, out, accessoryOverlays);
        base.close();
        return out;
    }

    @Nullable
    public static class_1011 readTextureImage(class_310 client, class_2960 textureId) throws IOException {
        if (client == null || textureId == null) {
            return null;
        }
        class_3298 resource = client.method_1478().method_14486(textureId).orElse(null);
        if (resource != null) {
            try (InputStream in = resource.method_14482();){
                class_1011 class_10112 = class_1011.method_4309((InputStream)in);
                return class_10112;
            }
        }
        class_1044 texture = client.method_1531().method_4619(textureId);
        if (texture instanceof class_1043) {
            class_1043 backedTexture = (class_1043)texture;
            class_1011 image = backedTexture.method_4525();
            if (image == null) {
                return null;
            }
            class_1011 copy = new class_1011(class_1011.class_1012.field_4997, image.method_4307(), image.method_4323(), false);
            copy.method_4317(image);
            return copy;
        }
        return null;
    }

    private static boolean hasProfileTextureProperties(class_310 client) {
        Object properties2;
        if (client == null || client.field_1724 == null || client.field_1724.method_7334() == null) {
            return false;
        }
        try {
            properties2 = client.field_1724.method_7334().getClass().getMethod("getProperties", new Class[0]).invoke((Object)client.field_1724.method_7334(), new Object[0]);
            if (properties2 instanceof Map) {
                Map map = (Map)properties2;
                return map.containsKey("textures");
            }
        }
        catch (ReflectiveOperationException | RuntimeException properties2) {
            // empty catch block
        }
        try {
            properties2 = client.field_1724.method_7334().getClass().getMethod("properties", new Class[0]).invoke((Object)client.field_1724.method_7334(), new Object[0]);
            if (properties2 instanceof Map) {
                Map map = (Map)properties2;
                return map.containsKey("textures");
            }
        }
        catch (ReflectiveOperationException | RuntimeException exception) {
            // empty catch block
        }
        return false;
    }

    private static boolean isLikelyDefaultPlayerSkin(class_2960 textureId) {
        if (textureId == null || !"minecraft".equals(textureId.method_12836())) {
            return false;
        }
        String path = textureId.method_12832();
        return path != null && path.startsWith(DEFAULT_PLAYER_SKIN_PREFIX);
    }

    public static int sendDetectedRippedSkinInfo() {
        DefaultSkinKey pixelKey;
        class_310 client = class_310.method_1551();
        if (client == null || client.field_1724 == null) {
            return 0;
        }
        class_2960 baseSkin = client.field_1724.method_52814().comp_1626().comp_3627();
        boolean defaultSkinPath = NonDestroyedSkinClient.isLikelyDefaultPlayerSkin(baseSkin);
        boolean profileHasCustomTexture = NonDestroyedSkinClient.hasProfileTextureProperties(client);
        DefaultSkinKey key = NonDestroyedSkinClient.parseDefaultSkinKey(baseSkin);
        DefaultSkinMatch pixelMatch = NonDestroyedSkinClient.detectDefaultSkinMatchByPixels(client, baseSkin);
        DefaultSkinKey defaultSkinKey = pixelKey = pixelMatch == null ? null : pixelMatch.key();
        DefaultSkinKey resolvedKey = key != null ? key : (pixelMatch != null && pixelMatch.accepted() ? pixelMatch.key() : null);
        class_7920 model = client.field_1724.method_52814() == null ? null : client.field_1724.method_52814().comp_1629();
        String profileName = client.field_1724.method_5477().getString();
        UUID uuid = client.field_1724.method_5667();
        DefaultSkinCandidate defaultDestroyed = resolvedKey == null ? null : NonDestroyedSkinClient.describeDefaultDestroyedSkinCandidate(client, resolvedKey);
        String message = "[NoN RippedSkinInfo] profile=" + profileName + " uuid=" + String.valueOf(uuid) + " base=" + String.valueOf(baseSkin) + " model=" + String.valueOf(model) + " defaultPath=" + defaultSkinPath + " profileTextures=" + profileHasCustomTexture + " parsed=" + (String)(key == null ? "<none>" : key.model() + "/" + key.name()) + " pixel=" + (String)(pixelKey == null ? "<none>" : pixelKey.model() + "/" + pixelKey.name()) + " confidence=" + (pixelMatch == null ? "<none>" : String.format(Locale.ROOT, "%.1f%%", pixelMatch.confidence() * 100.0)) + " accepted=" + (pixelMatch != null && pixelMatch.accepted()) + " resolved=" + (String)(resolvedKey == null ? "<none>" : resolvedKey.model() + "/" + resolvedKey.name()) + " candidate=" + (defaultDestroyed == null ? "<none>" : defaultDestroyed.logString());
        NeedsOfNature.LOGGER.info(message);
        NonDestroyedSkinClient.sendRippedSkinInfoLine(client, "NoN Ripped Skin Info", class_124.field_1065);
        NonDestroyedSkinClient.sendRippedSkinInfoLine(client, "Player: " + profileName + " (" + String.valueOf(uuid) + ")", class_124.field_1080);
        NonDestroyedSkinClient.sendRippedSkinInfoLine(client, "Base texture: " + String.valueOf(baseSkin), class_124.field_1068);
        NonDestroyedSkinClient.sendRippedSkinInfoLine(client, "Model: " + String.valueOf(model), class_124.field_1068);
        NonDestroyedSkinClient.sendRippedSkinInfoLine(client, "Default path: " + defaultSkinPath + " | Profile textures: " + profileHasCustomTexture, class_124.field_1068);
        NonDestroyedSkinClient.sendRippedSkinInfoLine(client, "Parsed key: " + NonDestroyedSkinClient.formatDefaultSkinKey(key), class_124.field_1068);
        NonDestroyedSkinClient.sendRippedSkinInfoLine(client, "Pixel match: " + NonDestroyedSkinClient.formatDefaultSkinKey(pixelKey) + " | Confidence: " + (pixelMatch == null ? "<none>" : String.format(Locale.ROOT, "%.1f%%", pixelMatch.confidence() * 100.0)) + " | Accepted: " + (pixelMatch != null && pixelMatch.accepted()), pixelMatch != null && pixelMatch.accepted() ? class_124.field_1060 : class_124.field_1054);
        NonDestroyedSkinClient.sendRippedSkinInfoLine(client, "Resolved key: " + NonDestroyedSkinClient.formatDefaultSkinKey(resolvedKey), resolvedKey == null ? class_124.field_1061 : class_124.field_1060);
        if (defaultDestroyed == null) {
            NonDestroyedSkinClient.sendRippedSkinInfoLine(client, "Ripped candidates: <none>", class_124.field_1061);
        } else {
            NonDestroyedSkinClient.sendRippedSkinInfoLine(client, "Ripped candidate: " + String.valueOf(defaultDestroyed.genderSpecific()) + " = " + defaultDestroyed.genderFound(), defaultDestroyed.genderFound() ? class_124.field_1060 : class_124.field_1054);
            NonDestroyedSkinClient.sendRippedSkinInfoLine(client, "Fallback candidate: " + String.valueOf(defaultDestroyed.generic()) + " = " + defaultDestroyed.genericFound(), defaultDestroyed.genericFound() ? class_124.field_1060 : class_124.field_1080);
        }
        return 1;
    }

    private static void sendRippedSkinInfoLine(class_310 client, String message, class_124 color) {
        client.field_1724.method_7353((class_2561)class_2561.method_43470((String)message).method_27692(color), false);
    }

    private static String formatDefaultSkinKey(@Nullable DefaultSkinKey key) {
        return key == null ? "<none>" : key.model() + "/" + key.name();
    }

    private static DefaultSkinCandidate describeDefaultDestroyedSkinCandidate(class_310 client, DefaultSkinKey key) {
        String genderSuffix = NeedsOfNature.getConfig().getPlayerGenderSelection() == NonConfig.PlayerGenderSelection.MALE ? "_m" : "_f";
        class_2960 genderSpecific = NonDestroyedSkinClient.defaultDestroyedSkinId(key, genderSuffix);
        class_2960 generic = NonDestroyedSkinClient.defaultDestroyedSkinId(key, "");
        boolean genderFound = client.method_1478().method_14486(genderSpecific).isPresent();
        boolean genericFound = client.method_1478().method_14486(generic).isPresent();
        return new DefaultSkinCandidate(genderSpecific, genderFound, generic, genericFound);
    }

    @Nullable
    private static byte[] readDefaultDestroyedSkin(class_310 client, DefaultSkinKey key) {
        if (client == null || key == null) {
            return null;
        }
        String genderSuffix = NeedsOfNature.getConfig().getPlayerGenderSelection() == NonConfig.PlayerGenderSelection.MALE ? "_m" : "_f";
        byte[] genderSpecific = NonDestroyedSkinClient.readDefaultDestroyedSkin(client, key, genderSuffix);
        if (genderSpecific != null) {
            return genderSpecific;
        }
        return NonDestroyedSkinClient.readDefaultDestroyedSkin(client, key, "");
    }

    @Nullable
    private static byte[] readDefaultDestroyedSkin(class_310 client, DefaultSkinKey key, String suffix) {
        byte[] byArray;
        block9: {
            class_2960 destroyedId = NonDestroyedSkinClient.defaultDestroyedSkinId(key, suffix);
            class_3298 resource = client.method_1478().method_14486(destroyedId).orElse(null);
            if (resource == null) {
                return null;
            }
            InputStream in = resource.method_14482();
            try {
                byArray = in.readAllBytes();
                if (in == null) break block9;
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
                    NonDestroyedSkinClient.setupWarn("[NoN] Failed to read default destroyed skin {}.", destroyedId, e);
                    return null;
                }
            }
            in.close();
        }
        return byArray;
    }

    private static class_2960 defaultDestroyedSkinId(DefaultSkinKey key, String suffix) {
        return class_2960.method_60655((String)"needsofnature", (String)(DEFAULT_DESTROYED_SKIN_PREFIX + key.model() + "/" + key.name() + suffix + ".png"));
    }

    @Nullable
    private static DefaultSkinKey resolveDefaultSkinKey(class_310 client, class_2960 baseSkin) {
        DefaultSkinKey parsed = NonDestroyedSkinClient.parseDefaultSkinKey(baseSkin);
        if (parsed != null) {
            return parsed;
        }
        DefaultSkinMatch match = NonDestroyedSkinClient.detectDefaultSkinMatchByPixels(client, baseSkin);
        return match != null && match.accepted() ? match.key() : null;
    }

    @Nullable
    private static DefaultSkinKey parseDefaultSkinKey(class_2960 baseSkin) {
        String[] parts;
        if (baseSkin == null || !"minecraft".equals(baseSkin.method_12836())) {
            return null;
        }
        String path = baseSkin.method_12832();
        if (path == null || !path.startsWith(DEFAULT_PLAYER_SKIN_PREFIX)) {
            return null;
        }
        String suffix = path.substring(DEFAULT_PLAYER_SKIN_PREFIX.length());
        if (suffix.endsWith(".png")) {
            suffix = suffix.substring(0, suffix.length() - ".png".length());
        }
        if ((parts = suffix.split("/")).length != 2) {
            return null;
        }
        String model = parts[0];
        String name = parts[1];
        if (!"slim".equals(model) && !"wide".equals(model)) {
            return null;
        }
        if (name.isBlank()) {
            return null;
        }
        return new DefaultSkinKey(model, name);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Nullable
    private static DefaultSkinMatch detectDefaultSkinMatchByPixels(class_310 client, class_2960 baseSkin) {
        if (client == null) return null;
        if (baseSkin == null) {
            return null;
        }
        class_7920 currentModel = client.field_1724 != null && client.field_1724.method_52814() != null ? client.field_1724.method_52814().comp_1629() : class_7920.field_41122;
        String preferredModel = currentModel == class_7920.field_41123 ? "wide" : "slim";
        String fallbackModel = "wide".equals(preferredModel) ? "slim" : "wide";
        try (class_1011 base = NonDestroyedSkinClient.readTextureImage(client, baseSkin);){
            if (base == null || base.method_4307() != 64 || base.method_4323() != 64) {
                DefaultSkinMatch defaultSkinMatch = null;
                return defaultSkinMatch;
            }
            DefaultSkinMatch preferred = NonDestroyedSkinClient.detectDefaultSkinMatchByPixels(client, base, preferredModel);
            DefaultSkinMatch fallback = NonDestroyedSkinClient.detectDefaultSkinMatchByPixels(client, base, fallbackModel);
            if (preferred == null) {
                DefaultSkinMatch defaultSkinMatch = fallback;
                return defaultSkinMatch;
            }
            if (fallback == null) {
                DefaultSkinMatch defaultSkinMatch = preferred;
                return defaultSkinMatch;
            }
            if (preferred.accepted() && !fallback.accepted()) {
                DefaultSkinMatch defaultSkinMatch = preferred;
                return defaultSkinMatch;
            }
            if (fallback.accepted() && !preferred.accepted()) {
                DefaultSkinMatch defaultSkinMatch = fallback;
                return defaultSkinMatch;
            }
            DefaultSkinMatch defaultSkinMatch = preferred.confidence() >= fallback.confidence() ? preferred : fallback;
            return defaultSkinMatch;
        }
        catch (IOException | RuntimeException e) {
            return null;
        }
    }

    @Nullable
    private static DefaultSkinMatch detectDefaultSkinMatchByPixels(class_310 client, class_1011 base, String model) {
        DefaultSkinMatch best = null;
        for (String name : VANILLA_DEFAULT_SKIN_NAMES) {
            class_2960 vanillaId = class_2960.method_60656((String)(DEFAULT_PLAYER_SKIN_PREFIX + model + "/" + name + ".png"));
            try (class_1011 vanilla = NonDestroyedSkinClient.readTextureImage(client, vanillaId);){
                if (vanilla == null) continue;
                double confidence = NonDestroyedSkinClient.imageMatchConfidence(base, vanilla);
                if (best != null && !(confidence > best.confidence())) continue;
                best = new DefaultSkinMatch(new DefaultSkinKey(model, name), confidence);
            }
            catch (IOException | RuntimeException exception) {
                // empty catch block
            }
        }
        return best;
    }

    private static double imageMatchConfidence(class_1011 first, class_1011 second) {
        if (first == null || second == null) {
            return 0.0;
        }
        if (first.method_4307() != second.method_4307() || first.method_4323() != second.method_4323()) {
            return 0.0;
        }
        int checked = 0;
        int matched = 0;
        for (int y = 0; y < first.method_4323(); ++y) {
            for (int x = 0; x < first.method_4307(); ++x) {
                int firstArgb = first.method_61940(x, y);
                int secondArgb = second.method_61940(x, y);
                int firstAlpha = firstArgb >>> 24 & 0xFF;
                int secondAlpha = secondArgb >>> 24 & 0xFF;
                if (firstAlpha == 0 && secondAlpha == 0) continue;
                ++checked;
                if (firstArgb != secondArgb) continue;
                ++matched;
            }
        }
        return checked <= 0 ? 0.0 : (double)matched / (double)checked;
    }

    @Nullable
    private static class_1011 createMaskedOverlay(class_310 client, UUID playerUuid, byte[] skin, int stage) throws IOException {
        class_1011 destroyed = class_1011.method_49277((byte[])skin);
        class_1011 mask = NonDestroyedSkinClient.readMask(client, playerUuid, stage);
        if (mask == null) {
            destroyed.close();
            return null;
        }
        class_1011 out = new class_1011(class_1011.class_1012.field_4997, destroyed.method_4307(), destroyed.method_4323(), false);
        int maskWidth = mask.method_4307();
        int maskHeight = mask.method_4323();
        for (int y = 0; y < destroyed.method_4323(); ++y) {
            for (int x = 0; x < destroyed.method_4307(); ++x) {
                int skinArgb = destroyed.method_61940(x, y);
                int maskArgb = mask.method_61940(x % maskWidth, y % maskHeight);
                int factor = NonDestroyedSkinClient.maskFactor(maskArgb);
                int alpha = (skinArgb >>> 24 & 0xFF) * factor / 255;
                out.method_61941(x, y, skinArgb & 0xFFFFFF | alpha << 24);
            }
        }
        destroyed.close();
        mask.close();
        return out;
    }

    /*
     * Exception decompiling
     */
    private static byte[] createGeneratedDestroyedSkin(class_310 client, class_2960 baseSkin) throws IOException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private static class_1011 readDestroyedBaseTexture(class_310 client, class_7920 model) throws IOException {
        boolean wide;
        boolean useMaleTexture = NeedsOfNature.getConfig().getPlayerGenderSelection() == NonConfig.PlayerGenderSelection.MALE;
        class_2960 bundledModel = NonDestroyedSkinClient.defaultDestroyedTexture(useMaleTexture, wide = model == class_7920.field_41123);
        class_1011 bundledModelImage = NonDestroyedSkinClient.readBundledDestroyedTexture(client, bundledModel);
        if (bundledModelImage != null) {
            return bundledModelImage;
        }
        class_2960 bundledGender = useMaleTexture ? DEFAULT_DESTROYED_TEXTURE_M : DEFAULT_DESTROYED_TEXTURE_F;
        class_1011 bundledGenderImage = NonDestroyedSkinClient.readBundledDestroyedTexture(client, bundledGender);
        if (bundledGenderImage != null) {
            return bundledGenderImage;
        }
        throw new MissingDestroyedSkinFallbackException("Missing ripped skin fallback texture " + String.valueOf(bundledModel) + " and fallback " + String.valueOf(bundledGender));
    }

    @Nullable
    private static class_1011 readBundledDestroyedTexture(class_310 client, class_2960 id) throws IOException {
        class_3298 resource = client.method_1478().method_14486(id).orElse(null);
        if (resource == null) {
            return null;
        }
        try (InputStream in = resource.method_14482();){
            class_1011 class_10112 = class_1011.method_4309((InputStream)in);
            return class_10112;
        }
    }

    private static class_2960 defaultDestroyedTexture(boolean useMaleTexture, boolean wide) {
        if (useMaleTexture) {
            return wide ? DEFAULT_DESTROYED_TEXTURE_M_WIDE : DEFAULT_DESTROYED_TEXTURE_M_SLIM;
        }
        return wide ? DEFAULT_DESTROYED_TEXTURE_F_WIDE : DEFAULT_DESTROYED_TEXTURE_F_SLIM;
    }

    private static void copyRegion(class_1011 source, class_1011 target, int minX, int minY, int maxX, int maxY) {
        int endX = Math.min(Math.min(source.method_4307(), target.method_4307()), maxX);
        int endY = Math.min(Math.min(source.method_4323(), target.method_4323()), maxY);
        for (int y = Math.max(0, minY); y < endY; ++y) {
            for (int x = Math.max(0, minX); x < endX; ++x) {
                target.method_61941(x, y, source.method_61940(x, y));
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static byte[] writeImageToBytes(class_1011 image) throws IOException {
        Path temp = Files.createTempFile("non_destroyed_skin", ".png", new FileAttribute[0]);
        try {
            image.method_4314(temp);
            byte[] byArray = Files.readAllBytes(temp);
            return byArray;
        }
        finally {
            try {
                Files.deleteIfExists(temp);
            }
            catch (IOException iOException) {}
        }
    }

    private static int detectSkinTint(class_1011 skin) {
        HashMap<Integer, ColorBucket> buckets = new HashMap<Integer, ColorBucket>();
        NonDestroyedSkinClient.addTintSamples(skin, buckets, 9, 12, 15, 16, 5);
        NonDestroyedSkinClient.addTintSamples(skin, buckets, 8, 12, 16, 16, 2);
        NonDestroyedSkinClient.addTintSamples(skin, buckets, 20, 17, 28, 24, 3);
        NonDestroyedSkinClient.addTintSamples(skin, buckets, 40, 20, 54, 32, 1);
        NonDestroyedSkinClient.addTintSamples(skin, buckets, 32, 56, 46, 64, 1);
        if (buckets.isEmpty()) {
            return 13803641;
        }
        ArrayList topBuckets = new ArrayList(buckets.values());
        topBuckets.sort((a, b) -> Integer.compare(b.count, a.count));
        int limit = Math.min(3, topBuckets.size());
        long r = 0L;
        long g = 0L;
        long b2 = 0L;
        int totalWeight = 0;
        for (int i = 0; i < limit; ++i) {
            ColorBucket bucket = (ColorBucket)topBuckets.get(i);
            int weight = Math.max(1, bucket.count);
            r += (long)bucket.averageR() * (long)weight;
            g += (long)bucket.averageG() * (long)weight;
            b2 += (long)bucket.averageB() * (long)weight;
            totalWeight += weight;
        }
        if (totalWeight <= 0) {
            return 13803641;
        }
        return (int)(r / (long)totalWeight) << 16 | (int)(g / (long)totalWeight) << 8 | (int)(b2 / (long)totalWeight);
    }

    private static void addTintSamples(class_1011 skin, Map<Integer, ColorBucket> out, int minX, int minY, int maxX, int maxY, int weight) {
        int clampedMaxX = Math.min(maxX, skin.method_4307());
        int clampedMaxY = Math.min(maxY, skin.method_4323());
        for (int y = Math.max(0, minY); y < clampedMaxY; ++y) {
            for (int x = Math.max(0, minX); x < clampedMaxX; ++x) {
                int argb = skin.method_61940(x, y);
                if (!NonDestroyedSkinClient.isSkinTintCandidate(argb)) continue;
                int rgb = argb & 0xFFFFFF;
                int key = NonDestroyedSkinClient.colorBucketKey(rgb);
                out.computeIfAbsent(key, ignored -> new ColorBucket()).add(rgb, Math.max(1, weight));
            }
        }
    }

    private static int colorBucketKey(int rgb) {
        int r = (rgb >>> 16 & 0xFF) / 8;
        int g = (rgb >>> 8 & 0xFF) / 8;
        int b = (rgb & 0xFF) / 8;
        return r << 10 | g << 5 | b;
    }

    private static boolean isSkinTintCandidate(int argb) {
        float saturation;
        if ((argb >>> 24 & 0xFF) < 32) {
            return false;
        }
        int r = argb >>> 16 & 0xFF;
        int g = argb >>> 8 & 0xFF;
        int b = argb & 0xFF;
        int max = Math.max(r, Math.max(g, b));
        int min = Math.min(r, Math.min(g, b));
        int brightness = (r + g + b) / 3;
        if (brightness < 35 || brightness > 245) {
            return false;
        }
        float f = saturation = max == 0 ? 0.0f : (float)(max - min) / (float)max;
        if (saturation > 0.7f) {
            return false;
        }
        return (float)r >= (float)b * 0.72f && (float)g >= (float)b * 0.5f;
    }

    private static int brightness(int rgb) {
        return ((rgb >>> 16 & 0xFF) + (rgb >>> 8 & 0xFF) + (rgb & 0xFF)) / 3;
    }

    private static int tintByBrightness(int tintRgb, int brightness, int alpha) {
        float shade = Math.max(0.0f, Math.min(1.0f, (float)brightness / 255.0f));
        int r = Math.round((float)(tintRgb >>> 16 & 0xFF) * shade);
        int g = Math.round((float)(tintRgb >>> 8 & 0xFF) * shade);
        int b = Math.round((float)(tintRgb & 0xFF) * shade);
        return Math.max(0, Math.min(255, alpha)) << 24 | r << 16 | g << 8 | b;
    }

    private static String formatRgb(int rgb) {
        return String.format(Locale.ROOT, "%06X", rgb & 0xFFFFFF);
    }

    @Nullable
    private static class_1011 readMask(class_310 client, UUID playerUuid, int stage) {
        class_1011 class_10112;
        block14: {
            class_2960 maskId;
            byte[] synced;
            if (stage <= 0 || stage >= 4) {
                return null;
            }
            byte[][] syncedMasks = DESTROYED_SKIN_MASKS.get(playerUuid);
            if (syncedMasks != null && stage < syncedMasks.length && (synced = syncedMasks[stage]) != null && synced.length > 0) {
                try {
                    return class_1011.method_49277((byte[])synced);
                }
                catch (IOException | RuntimeException e) {
                    NonDestroyedSkinClient.setupWarn("[NoN] Failed to read synced destroyed skin mask for {} stage {}.", playerUuid, stage, e);
                }
            }
            if ((maskId = MASK_TEXTURES[stage]) == null) {
                return null;
            }
            class_3298 resource = client.method_1478().method_14486(maskId).orElse(null);
            if (resource == null) {
                return null;
            }
            InputStream in = resource.method_14482();
            try {
                class_10112 = class_1011.method_4309((InputStream)in);
                if (in == null) break block14;
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
                    NonDestroyedSkinClient.setupWarn("[NoN] Failed to read destroyed skin mask {}.", maskId, e);
                    return null;
                }
            }
            in.close();
        }
        return class_10112;
    }

    private static int maskFactor(int maskArgb) {
        int maskAlpha = maskArgb >>> 24 & 0xFF;
        int maskBrightness = ((maskArgb >>> 16 & 0xFF) + (maskArgb >>> 8 & 0xFF) + (maskArgb & 0xFF)) / 3;
        return maskAlpha * maskBrightness / 255;
    }

    private static int blendArgb(int baseArgb, int overlayArgb, int factor) {
        int inv = 255 - factor;
        int a = ((baseArgb >>> 24 & 0xFF) * inv + (overlayArgb >>> 24 & 0xFF) * factor) / 255;
        int r = ((baseArgb >>> 16 & 0xFF) * inv + (overlayArgb >>> 16 & 0xFF) * factor) / 255;
        int g = ((baseArgb >>> 8 & 0xFF) * inv + (overlayArgb >>> 8 & 0xFF) * factor) / 255;
        int b = ((baseArgb & 0xFF) * inv + (overlayArgb & 0xFF) * factor) / 255;
        return a << 24 | r << 16 | g << 8 | b;
    }

    private static void applyMessOverlays(class_310 client, class_1011 out, MessState state) {
        if (client == null || out == null || state == null || !state.hasAnyMess()) {
            return;
        }
        NonDestroyedSkinClient.applyMessRole(client, out, state.vMess(), state.tintRgb(), MESS_MASK_TEXTURES[0]);
        NonDestroyedSkinClient.applyMessRole(client, out, state.aMess(), state.tintRgb(), MESS_MASK_TEXTURES[1]);
        NonDestroyedSkinClient.applyMessRole(client, out, state.mMess(), state.tintRgb(), MESS_MASK_TEXTURES[2]);
    }

    private static void applyTankOverlay(class_310 client, class_1011 out, MessState state, TankMaskType tankMaskType) {
        if (client == null || out == null || state == null || !state.hasTankOverlay()) {
            return;
        }
        class_2960 maskId = tankMaskType == TankMaskType.A ? A_MESS_TANK_TEXTURE : V_MESS_TANK_TEXTURE;
        class_1011 mask = NonDestroyedSkinClient.readMessMask(client, maskId);
        if (mask == null) {
            return;
        }
        NonDestroyedSkinClient.applyTintMask(out, mask, state.tintRgb(), state.tankAlpha());
        mask.close();
    }

    private static void applyAccessoryOverlays(class_310 client, class_1011 out, List<class_2960> overlays) {
        if (client == null || out == null || overlays == null || overlays.isEmpty()) {
            return;
        }
        for (class_2960 overlayId : overlays) {
            class_1011 overlay = NonDestroyedSkinClient.readAccessoryOverlay(client, overlayId);
            if (overlay == null) continue;
            NonDestroyedSkinClient.applyRawOverlay(out, overlay);
            overlay.close();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Nullable
    private static class_1011 readAccessoryOverlay(class_310 client, class_2960 overlayId) {
        if (client == null) return null;
        if (overlayId == null) {
            return null;
        }
        try {
            class_3298 resource = client.method_1478().method_14486(overlayId).orElse(null);
            if (resource == null) {
                return null;
            }
            try (InputStream in = resource.method_14482();){
                class_1011 image = class_1011.method_4309((InputStream)in);
                if (image.method_4307() != 64 || image.method_4323() != 64) {
                    image.close();
                    NonDestroyedSkinClient.setupWarn("[NoN] Ignoring accessory skin overlay {}: expected 64x64 PNG.", overlayId);
                    class_1011 class_10113 = null;
                    return class_10113;
                }
                class_1011 class_10112 = image;
                return class_10112;
            }
        }
        catch (IOException | RuntimeException e) {
            NonDestroyedSkinClient.setupWarn("[NoN] Failed to read accessory skin overlay {}.", overlayId, e);
            return null;
        }
    }

    private static void applyRawOverlay(class_1011 out, class_1011 overlay) {
        int width = Math.min(out.method_4307(), overlay.method_4307());
        int height = Math.min(out.method_4323(), overlay.method_4323());
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int overlayArgb = overlay.method_61940(x, y);
                int alpha = overlayArgb >>> 24 & 0xFF;
                if (alpha <= 0) continue;
                out.method_61941(x, y, NonDestroyedSkinClient.blendArgb(out.method_61940(x, y), overlayArgb, alpha));
            }
        }
    }

    private static void applyMessRole(class_310 client, class_1011 out, int mess, int tintRgb, class_2960[] masks) {
        if (mess <= 0 || masks == null || masks.length < 3) {
            return;
        }
        for (MessLayer layer : NonDestroyedSkinClient.messLayers(mess)) {
            class_1011 mask;
            if (layer.index() < 0 || layer.index() >= masks.length || (mask = NonDestroyedSkinClient.readMessMask(client, masks[layer.index()])) == null) continue;
            NonDestroyedSkinClient.applyTintMask(out, mask, tintRgb, layer.alpha());
            mask.close();
        }
    }

    private static List<MessLayer> messLayers(int mess) {
        return switch (NonDestroyedSkinClient.clampMess(mess)) {
            case 1 -> List.of(new MessLayer(0, 85));
            case 2 -> List.of(new MessLayer(0, 170));
            case 3 -> List.of(new MessLayer(0, 255));
            case 4 -> List.of(new MessLayer(0, 170), new MessLayer(1, 85));
            case 5 -> List.of(new MessLayer(0, 85), new MessLayer(1, 170));
            case 6 -> List.of(new MessLayer(1, 255));
            case 7 -> List.of(new MessLayer(1, 170), new MessLayer(2, 85));
            case 8 -> List.of(new MessLayer(1, 85), new MessLayer(2, 170));
            case 9, 10 -> List.of(new MessLayer(2, 255));
            default -> List.of();
        };
    }

    private static void applyTintMask(class_1011 out, class_1011 mask, int tintRgb, int alpha) {
        int maskWidth = mask.method_4307();
        int maskHeight = mask.method_4323();
        int overlayBase = 0xFF000000 | tintRgb & 0xFFFFFF;
        for (int y = 0; y < out.method_4323(); ++y) {
            for (int x = 0; x < out.method_4307(); ++x) {
                int factor = NonDestroyedSkinClient.maskFactor(mask.method_61940(x % maskWidth, y % maskHeight)) * alpha / 255;
                if (factor <= 0) continue;
                out.method_61941(x, y, NonDestroyedSkinClient.blendArgb(out.method_61940(x, y), overlayBase, factor));
            }
        }
    }

    @Nullable
    private static class_1011 readMessMask(class_310 client, class_2960 maskId) {
        class_1011 class_10112;
        block10: {
            if (client == null || maskId == null) {
                return null;
            }
            class_3298 resource = client.method_1478().method_14486(maskId).orElse(null);
            if (resource == null) {
                return null;
            }
            InputStream in = resource.method_14482();
            try {
                class_10112 = class_1011.method_4309((InputStream)in);
                if (in == null) break block10;
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
                    NonDestroyedSkinClient.setupWarn("[NoN] Failed to read mess mask {}.", maskId, e);
                    return null;
                }
            }
            in.close();
        }
        return class_10112;
    }

    private static boolean isValidSkinPng(byte[] bytes) {
        boolean bl;
        block9: {
            if (bytes == null || bytes.length == 0 || bytes.length > 65536) {
                return false;
            }
            class_1011 image = class_1011.method_49277((byte[])bytes);
            try {
                boolean bl2 = bl = image.method_4307() == 64 && image.method_4323() == 64;
                if (image == null) break block9;
            }
            catch (Throwable throwable) {
                try {
                    if (image != null) {
                        try {
                            image.close();
                        }
                        catch (Throwable throwable2) {
                            throwable.addSuppressed(throwable2);
                        }
                    }
                    throw throwable;
                }
                catch (IOException | RuntimeException e) {
                    return false;
                }
            }
            image.close();
        }
        return bl;
    }

    private static int[] getOriginalSkinColors(class_310 client, @Nullable class_2960 baseSkin) {
        if (baseSkin == null) {
            return new int[]{15920872};
        }
        int[] cached = ORIGINAL_SKIN_COLOR_CACHE.get(baseSkin);
        if (cached != null && cached.length > 0) {
            return cached;
        }
        int[] colors = NonDestroyedSkinClient.readOriginalSkinColors(client, baseSkin);
        ORIGINAL_SKIN_COLOR_CACHE.put(baseSkin, colors);
        return colors;
    }

    /*
     * Exception decompiling
     */
    private static int[] readOriginalSkinColors(class_310 client, class_2960 baseSkin) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [7[TRYBLOCK]], but top level block is 31[DOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private static int varyColor(int rgb, class_5819 random) {
        float factor = 0.85f + random.method_43057() * 0.3f;
        int r = Math.min(255, Math.max(0, Math.round((float)(rgb >> 16 & 0xFF) * factor)));
        int g = Math.min(255, Math.max(0, Math.round((float)(rgb >> 8 & 0xFF) * factor)));
        int b = Math.min(255, Math.max(0, Math.round((float)(rgb & 0xFF) * factor)));
        return r << 16 | g << 8 | b;
    }

    private static void clearCachedTextures(UUID playerUuid) {
        class_310 client = class_310.method_1551();
        ArrayList<class_2960> texturesToDestroy = new ArrayList<class_2960>();
        TEXTURE_CACHE.entrySet().removeIf(entry -> {
            if (!((TextureKey)entry.getKey()).playerUuid().equals(playerUuid)) {
                return false;
            }
            texturesToDestroy.add((class_2960)entry.getValue());
            return true;
        });
        NonDestroyedSkinClient.destroyTextures(client, texturesToDestroy);
    }

    private static void destroyTextures(@Nullable class_310 client, List<class_2960> textureIds) {
        if (client == null || textureIds == null || textureIds.isEmpty()) {
            return;
        }
        List<class_2960> ids = List.copyOf(textureIds);
        client.execute(() -> {
            for (class_2960 id : ids) {
                client.method_1531().method_4615(id);
            }
        });
    }

    private static int clampStage(int stage) {
        return Math.max(0, Math.min(4, stage));
    }

    private static int clampMess(int value) {
        return Math.max(0, Math.min(10, value));
    }

    private static TankMaskType resolveTankMaskType(@Nullable class_1309 entity) {
        GenderHolder holder;
        int mask;
        if (entity instanceof GenderHolder && (mask = (holder = (GenderHolder)entity).getGenderMask() & 3) == 1) {
            return TankMaskType.A;
        }
        return TankMaskType.V;
    }

    private static void setupWarn(String template, Object ... args) {
        NeedsOfNature.LOGGER.warn(template, args);
        NeedsOfNatureClient.sendClientSetupWarning(NeedsOfNature.formatLogTemplate(template, args));
    }

    private static void markAutomaticSkinUnavailable(String template, Object ... args) {
        if (!automaticSkinUnavailableThisConnection) {
            NonDestroyedSkinClient.setupWarn(template, args);
        }
        automaticSkinUnavailableThisConnection = true;
        uploadRetryTicks = 0;
    }

    private record DefaultSkinKey(String model, String name) {
    }

    private static final class MissingDestroyedSkinFallbackException
    extends IOException {
        private MissingDestroyedSkinFallbackException(String message) {
            super(message);
        }
    }

    private record MessState(int vMess, int aMess, int mMess, int tintRgb, int liquidStored, int liquidCapacity) {
        static final MessState EMPTY = new MessState(0, 0, 0, 0xFFFFFF, 0, 0);

        boolean hasAnyMess() {
            return this.vMess > 0 || this.aMess > 0 || this.mMess > 0;
        }

        boolean hasTankOverlay() {
            return this.liquidStored > 0 && this.liquidCapacity > 0;
        }

        boolean hasAnyVisual() {
            return this.hasAnyMess() || this.hasTankOverlay();
        }

        int tankAlpha() {
            if (!this.hasTankOverlay()) {
                return 0;
            }
            float ratio = Math.max(0.0f, Math.min(1.0f, (float)this.liquidStored / (float)this.liquidCapacity));
            return Math.max(3, Math.min(255, Math.round(3.0f + ratio * 252.0f)));
        }

        String cacheKey() {
            return NonDestroyedSkinClient.clampMess(this.vMess) + "_" + NonDestroyedSkinClient.clampMess(this.aMess) + "_" + NonDestroyedSkinClient.clampMess(this.mMess) + "_" + Math.max(0, this.liquidStored) + "_" + Math.max(0, this.liquidCapacity) + "_" + Integer.toHexString(this.tintRgb & 0xFFFFFF);
        }
    }

    private static enum TankMaskType {
        V("v"),
        A("a");

        private final String id;

        private TankMaskType(String id) {
            this.id = id;
        }
    }

    private record TextureKey(UUID playerUuid, int stage, @Nullable class_2960 baseTexture, MessState messState, TankMaskType tankMaskType, List<class_2960> accessoryOverlays) {
    }

    private record DefaultSkinMatch(DefaultSkinKey key, double confidence) {
        boolean accepted() {
            return this.confidence >= 0.9;
        }
    }

    private record DefaultSkinCandidate(class_2960 genderSpecific, boolean genderFound, class_2960 generic, boolean genericFound) {
        String logString() {
            return String.valueOf(this.genderSpecific) + "=" + this.genderFound + ", " + String.valueOf(this.generic) + "=" + this.genericFound;
        }
    }

    private static final class ColorBucket {
        long r;
        long g;
        long b;
        int count;

        private ColorBucket() {
        }

        void add(int rgb, int weight) {
            this.r += (long)(rgb >>> 16 & 0xFF) * (long)weight;
            this.g += (long)(rgb >>> 8 & 0xFF) * (long)weight;
            this.b += (long)(rgb & 0xFF) * (long)weight;
            this.count += weight;
        }

        int averageR() {
            return this.count <= 0 ? 0 : (int)(this.r / (long)this.count);
        }

        int averageG() {
            return this.count <= 0 ? 0 : (int)(this.g / (long)this.count);
        }

        int averageB() {
            return this.count <= 0 ? 0 : (int)(this.b / (long)this.count);
        }
    }

    private record MessLayer(int index, int alpha) {
    }
}

