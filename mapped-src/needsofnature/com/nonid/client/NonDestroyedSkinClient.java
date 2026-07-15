/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
 *  net.fabricmc.loader.api.FabricLoader
 *  net.minecraft.client.texture.NativeImage
 *  net.minecraft.client.texture.NativeImage$Format
 *  net.minecraft.client.texture.NativeImageBackedTexture
 *  net.minecraft.client.texture.AbstractTexture
 *  net.minecraft.util.Formatting
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.text.Text
 *  net.minecraft.util.Identifier
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.resource.Resource
 *  net.minecraft.util.math.random.Random
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.client.network.AbstractClientPlayerEntity
 *  net.minecraft.entity.player.PlayerSkinType
 *  net.minecraft.network.packet.CustomPayload
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
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.util.Formatting;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.util.math.random.Random;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerSkinType;
import net.minecraft.network.packet.CustomPayload;
import org.jetbrains.annotations.Nullable;

public final class NonDestroyedSkinClient {
    private static final int MIN_STAGE = 0;
    private static final int MAX_STAGE = 4;
    private static final int FALLBACK_FABRIC_RGB = 15920872;
    private static final int FALLBACK_SKIN_TINT_RGB = 13803641;
    private static final Identifier RIPPED_FABRIC_COLOR_MASK = Identifier.of((String)"needsofnature", (String)"textures/particle/ripped_fabric_color_mask.png");
    private static final Identifier DEFAULT_DESTROYED_TEXTURE_F = Identifier.of((String)"needsofnature", (String)"textures/player_destroyed/destroyed_texture_f.png");
    private static final Identifier DEFAULT_DESTROYED_TEXTURE_M = Identifier.of((String)"needsofnature", (String)"textures/player_destroyed/destroyed_texture_m.png");
    private static final Identifier DEFAULT_DESTROYED_TEXTURE_F_SLIM = Identifier.of((String)"needsofnature", (String)"textures/player_destroyed/destroyed_texture_f_slim.png");
    private static final Identifier DEFAULT_DESTROYED_TEXTURE_F_WIDE = Identifier.of((String)"needsofnature", (String)"textures/player_destroyed/destroyed_texture_f_wide.png");
    private static final Identifier DEFAULT_DESTROYED_TEXTURE_M_SLIM = Identifier.of((String)"needsofnature", (String)"textures/player_destroyed/destroyed_texture_m_slim.png");
    private static final Identifier DEFAULT_DESTROYED_TEXTURE_M_WIDE = Identifier.of((String)"needsofnature", (String)"textures/player_destroyed/destroyed_texture_m_wide.png");
    private static final String DEFAULT_PLAYER_SKIN_PREFIX = "textures/entity/player/";
    private static final String DEFAULT_DESTROYED_SKIN_PREFIX = "textures/player_destroyed/default/";
    private static final String[] VANILLA_DEFAULT_SKIN_NAMES = new String[]{"steve", "alex", "ari", "efe", "kai", "makena", "noor", "sunny", "zuri"};
    private static final double DEFAULT_SKIN_PIXEL_MATCH_THRESHOLD = 0.9;
    private static final Identifier[] MASK_TEXTURES = new Identifier[]{null, Identifier.of((String)"needsofnature", (String)"textures/player_destroyed/mask_1.png"), Identifier.of((String)"needsofnature", (String)"textures/player_destroyed/mask_2.png"), Identifier.of((String)"needsofnature", (String)"textures/player_destroyed/mask_3.png")};
    private static final Identifier[][] MESS_MASK_TEXTURES = new Identifier[][]{{Identifier.of((String)"needsofnature", (String)"textures/entity/player/mess/v_mess1.png"), Identifier.of((String)"needsofnature", (String)"textures/entity/player/mess/v_mess2.png"), Identifier.of((String)"needsofnature", (String)"textures/entity/player/mess/v_mess3.png")}, {Identifier.of((String)"needsofnature", (String)"textures/entity/player/mess/a_mess1.png"), Identifier.of((String)"needsofnature", (String)"textures/entity/player/mess/a_mess2.png"), Identifier.of((String)"needsofnature", (String)"textures/entity/player/mess/a_mess3.png")}, {Identifier.of((String)"needsofnature", (String)"textures/entity/player/mess/m_mess1.png"), Identifier.of((String)"needsofnature", (String)"textures/entity/player/mess/m_mess2.png"), Identifier.of((String)"needsofnature", (String)"textures/entity/player/mess/m_mess3.png")}};
    private static final Identifier V_MESS_TANK_TEXTURE = Identifier.of((String)"needsofnature", (String)"textures/entity/player/mess/v_mess_tank.png");
    private static final Identifier A_MESS_TANK_TEXTURE = Identifier.of((String)"needsofnature", (String)"textures/entity/player/mess/a_mess_tank.png");
    private static final Map<UUID, byte[]> DESTROYED_SKINS = new HashMap<UUID, byte[]>();
    private static final Map<UUID, byte[][]> DESTROYED_SKIN_MASKS = new HashMap<UUID, byte[][]>();
    private static final Map<UUID, Integer> STAGES = new HashMap<UUID, Integer>();
    private static final Map<UUID, MessState> MESS_STATES = new HashMap<UUID, MessState>();
    private static final Map<TextureKey, Identifier> TEXTURE_CACHE = new HashMap<TextureKey, Identifier>();
    private static final Map<Identifier, int[]> ORIGINAL_SKIN_COLOR_CACHE = new HashMap<Identifier, int[]>();
    private static int uploadRetryTicks = 0;
    private static boolean uploadedThisConnection = false;
    private static boolean automaticSkinUnavailableThisConnection = false;
    @Nullable
    private static Identifier lastUploadedAutoBaseSkin = null;

    private NonDestroyedSkinClient() {
    }

    public static void scheduleLocalSkinUploadRetry() {
        uploadRetryTicks = 300;
        uploadedThisConnection = false;
        automaticSkinUnavailableThisConnection = false;
        lastUploadedAutoBaseSkin = null;
    }

    public static void clientTick(MinecraftClient client) {
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
                ClientPlayNetworking.send((CustomPayload)new DestroyedSkinUploadC2SPayload(bytes));
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
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) {
            return false;
        }
        Identifier baseSkin = client.player.getSkin().comp_1626().comp_3627();
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
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) {
            return false;
        }
        if (!ClientPlayNetworking.canSend(DestroyedSkinUploadC2SPayload.ID)) {
            return false;
        }
        if (automaticSkinUnavailableThisConnection) {
            return false;
        }
        Identifier baseSkin = client.player.getSkin().comp_1626().comp_3627();
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
            ClientPlayNetworking.send((CustomPayload)new DestroyedSkinUploadC2SPayload(defaultDestroyedSkin));
            lastUploadedAutoBaseSkin = baseSkin;
            return true;
        }
        try {
            byte[] bytes = NonDestroyedSkinClient.createGeneratedDestroyedSkin(client, baseSkin);
            if (bytes == null || !NonDestroyedSkinClient.isValidSkinPng(bytes)) {
                return false;
            }
            ClientPlayNetworking.send((CustomPayload)new DestroyedSkinUploadC2SPayload(bytes));
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
                ClientPlayNetworking.send((CustomPayload)new DestroyedSkinMaskUploadC2SPayload(stage, bytes));
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
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) {
            return fallbackRgb & 0xFFFFFF;
        }
        Identifier baseSkin = client.player.getSkin().comp_1626().comp_3627();
        if (baseSkin == null) {
            return fallbackRgb & 0xFFFFFF;
        }
        try {
            Resource resource = client.getResourceManager().getResource(baseSkin).orElse(null);
            if (resource == null) {
                return fallbackRgb & 0xFFFFFF;
            }
            try (InputStream in = resource.getInputStream();){
                int n;
                block17: {
                    NativeImage image = NativeImage.read((InputStream)in);
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
        MinecraftClient client = MinecraftClient.getInstance();
        ArrayList<Identifier> texturesToDestroy = new ArrayList<Identifier>(TEXTURE_CACHE.values());
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
        MinecraftClient client = MinecraftClient.getInstance();
        if (clamped > 0 && client != null && client.player != null && playerUuid.equals(client.player.getUuid()) && !DESTROYED_SKINS.containsKey(playerUuid)) {
            NonDestroyedSkinClient.uploadLocalSkinIfPresent();
        }
        if (client != null && client.player != null && playerUuid.equals(client.player.getUuid())) {
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
    public static Identifier resolveBaseTexture(LivingEntity entity, Identifier currentBaseTexture) {
        int stage = NonDestroyedSkinClient.getDestroyedStage(entity);
        MessState messState = NonDestroyedSkinClient.getMessState(entity);
        List<Identifier> accessoryOverlays = NonAccessorySkinOverlays.resolveEquippedOverlays(entity);
        if (stage <= 0 && !messState.hasAnyVisual() && accessoryOverlays.isEmpty()) {
            return null;
        }
        return NonDestroyedSkinClient.getOrCreateTexture(entity, stage, currentBaseTexture, false);
    }

    @Nullable
    public static Identifier resolveOverlayTexture(LivingEntity entity) {
        int stage = NonDestroyedSkinClient.getDestroyedStage(entity);
        if (stage <= 0 || stage >= 4) {
            return null;
        }
        return NonDestroyedSkinClient.getOrCreateTexture(entity, stage, null, true);
    }

    public static void spawnRippedFabricParticles(UUID playerUuid, double x, double y, double z, int count, long seed) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.world == null || playerUuid == null || count <= 0) {
            return;
        }
        AbstractClientPlayerEntity player = null;
        for (AbstractClientPlayerEntity candidate : client.world.getPlayers()) {
            if (!playerUuid.equals(candidate.getUuid())) continue;
            player = candidate;
            break;
        }
        if (player == null) {
            return;
        }
        Identifier baseSkin = player.getSkin().comp_1626().comp_3627();
        int[] colors = NonDestroyedSkinClient.getOriginalSkinColors(client, baseSkin);
        Random random = Random.create((long)seed);
        int particleCount = Math.max(1, Math.min(12, count));
        for (int i = 0; i < particleCount; ++i) {
            double vz;
            double vy;
            double vx;
            double pz;
            double py;
            int rgb = colors[random.nextInt(colors.length)];
            rgb = NonDestroyedSkinClient.varyColor(rgb, random);
            double px = x + (random.nextDouble() - 0.5) * 0.16;
            Particle particle = RippedFabricParticle.create(client.world, px, py = y + (random.nextDouble() - 0.5) * 0.2, pz = z + (random.nextDouble() - 0.5) * 0.16, vx = (random.nextDouble() - 0.5) * 0.001, vy = -0.006 - random.nextDouble() * 0.004, vz = (random.nextDouble() - 0.5) * 0.001, rgb, random);
            if (particle == null) continue;
            client.particleManager.addParticle(particle);
        }
    }

    private static int getDestroyedStage(LivingEntity entity) {
        DestroyedSkinHolder holder;
        int tracked;
        if (!(entity instanceof PlayerEntity)) {
            return 0;
        }
        if (entity == null) {
            return 0;
        }
        if (entity instanceof DestroyedSkinHolder && (tracked = NonDestroyedSkinClient.clampStage((holder = (DestroyedSkinHolder)entity).getDestroyedSkinStage())) > 0) {
            return tracked;
        }
        return STAGES.getOrDefault(entity.getUuid(), 0);
    }

    private static MessState getMessState(@Nullable LivingEntity entity) {
        if (entity == null) {
            return MessState.EMPTY;
        }
        return MESS_STATES.getOrDefault(entity.getUuid(), MessState.EMPTY);
    }

    @Nullable
    private static Identifier getOrCreateTexture(LivingEntity entity, int stage, @Nullable Identifier currentBaseTexture, boolean overlayFallback) {
        TankMaskType tankMaskType;
        List<Object> accessoryOverlays;
        if (entity == null) {
            return null;
        }
        UUID playerUuid = entity.getUuid();
        byte[] skin = DESTROYED_SKINS.get(playerUuid);
        MessState messState = MESS_STATES.getOrDefault(playerUuid, MessState.EMPTY);
        List<Object> list = accessoryOverlays = overlayFallback ? List.of() : NonAccessorySkinOverlays.resolveEquippedOverlays(entity);
        if ((skin == null || skin.length == 0) && !messState.hasAnyVisual() && accessoryOverlays.isEmpty()) {
            return null;
        }
        TextureKey key = new TextureKey(playerUuid, stage, overlayFallback ? null : currentBaseTexture, messState, tankMaskType = NonDestroyedSkinClient.resolveTankMaskType(entity), accessoryOverlays);
        Identifier cached = TEXTURE_CACHE.get(key);
        if (cached != null) {
            return cached;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) {
            return null;
        }
        try {
            NativeImage image;
            if (stage >= 4 && skin != null && skin.length > 0) {
                image = NativeImage.read((byte[])skin);
                NonDestroyedSkinClient.applyMessOverlays(client, image, messState);
                NonDestroyedSkinClient.applyTankOverlay(client, image, messState, tankMaskType);
                NonDestroyedSkinClient.applyAccessoryOverlays(client, image, accessoryOverlays);
            } else {
                image = overlayFallback ? NonDestroyedSkinClient.createMaskedOverlay(client, playerUuid, skin, stage) : NonDestroyedSkinClient.createCompositedSkin(client, playerUuid, skin, stage, currentBaseTexture, messState, tankMaskType, accessoryOverlays);
            }
            if (image == null) {
                return null;
            }
            Identifier id = Identifier.of((String)"needsofnature", (String)("textures/dynamic/destroyed_skin/" + playerUuid.toString().replace("-", "") + "_" + stage + "_" + messState.cacheKey() + "_" + tankMaskType.id + "_" + Integer.toHexString(accessoryOverlays.hashCode()) + (overlayFallback ? "_overlay" : "_base") + ".png"));
            client.getTextureManager().registerTexture(id, (AbstractTexture)new NativeImageBackedTexture(() -> "non_destroyed_skin", image));
            TEXTURE_CACHE.put(key, id);
            return id;
        }
        catch (IOException | RuntimeException e) {
            NonDestroyedSkinClient.setupWarn("[NoN] Failed to create destroyed skin texture for {} stage {}.", playerUuid, stage, e);
            return null;
        }
    }

    @Nullable
    private static NativeImage createCompositedSkin(MinecraftClient client, UUID playerUuid, byte[] skin, int stage, @Nullable Identifier currentBaseTexture, MessState messState, TankMaskType tankMaskType, List<Identifier> accessoryOverlays) throws IOException {
        if (currentBaseTexture == null) {
            return null;
        }
        NativeImage base = NonDestroyedSkinClient.readTextureImage(client, currentBaseTexture);
        if (base == null) {
            return null;
        }
        NativeImage out = new NativeImage(NativeImage.Format.RGBA, base.getWidth(), base.getHeight(), false);
        out.copyFrom(base);
        if (stage > 0 && skin != null && skin.length > 0) {
            NativeImage destroyed = NativeImage.read((byte[])skin);
            NativeImage mask = NonDestroyedSkinClient.readMask(client, playerUuid, stage);
            if (mask != null) {
                int width = Math.min(base.getWidth(), destroyed.getWidth());
                int height = Math.min(base.getHeight(), destroyed.getHeight());
                int maskWidth = mask.getWidth();
                int maskHeight = mask.getHeight();
                for (int y = 0; y < height; ++y) {
                    for (int x = 0; x < width; ++x) {
                        int factor = NonDestroyedSkinClient.maskFactor(mask.getColorArgb(x % maskWidth, y % maskHeight));
                        if (factor <= 0) continue;
                        int baseArgb = base.getColorArgb(x, y);
                        int destroyedArgb = destroyed.getColorArgb(x, y);
                        out.setColorArgb(x, y, NonDestroyedSkinClient.blendArgb(baseArgb, destroyedArgb, factor));
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
    public static NativeImage readTextureImage(MinecraftClient client, Identifier textureId) throws IOException {
        if (client == null || textureId == null) {
            return null;
        }
        Resource resource = client.getResourceManager().getResource(textureId).orElse(null);
        if (resource != null) {
            try (InputStream in = resource.getInputStream();){
                NativeImage LootTableData = NativeImage.read((InputStream)in);
                return LootTableData;
            }
        }
        AbstractTexture texture = client.getTextureManager().getTexture(textureId);
        if (texture instanceof NativeImageBackedTexture) {
            NativeImageBackedTexture backedTexture = (NativeImageBackedTexture)texture;
            NativeImage image = backedTexture.getImage();
            if (image == null) {
                return null;
            }
            NativeImage copy = new NativeImage(NativeImage.Format.RGBA, image.getWidth(), image.getHeight(), false);
            copy.copyFrom(image);
            return copy;
        }
        return null;
    }

    private static boolean hasProfileTextureProperties(MinecraftClient client) {
        Object properties2;
        if (client == null || client.player == null || client.player.getGameProfile() == null) {
            return false;
        }
        try {
            properties2 = client.player.getGameProfile().getClass().getMethod("getProperties", new Class[0]).invoke((Object)client.player.getGameProfile(), new Object[0]);
            if (properties2 instanceof Map) {
                Map map = (Map)properties2;
                return map.containsKey("textures");
            }
        }
        catch (ReflectiveOperationException | RuntimeException properties2) {
            // empty catch block
        }
        try {
            properties2 = client.player.getGameProfile().getClass().getMethod("properties", new Class[0]).invoke((Object)client.player.getGameProfile(), new Object[0]);
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

    private static boolean isLikelyDefaultPlayerSkin(Identifier textureId) {
        if (textureId == null || !"minecraft".equals(textureId.getNamespace())) {
            return false;
        }
        String path = textureId.getPath();
        return path != null && path.startsWith(DEFAULT_PLAYER_SKIN_PREFIX);
    }

    public static int sendDetectedRippedSkinInfo() {
        DefaultSkinKey pixelKey;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) {
            return 0;
        }
        Identifier baseSkin = client.player.getSkin().comp_1626().comp_3627();
        boolean defaultSkinPath = NonDestroyedSkinClient.isLikelyDefaultPlayerSkin(baseSkin);
        boolean profileHasCustomTexture = NonDestroyedSkinClient.hasProfileTextureProperties(client);
        DefaultSkinKey key = NonDestroyedSkinClient.parseDefaultSkinKey(baseSkin);
        DefaultSkinMatch pixelMatch = NonDestroyedSkinClient.detectDefaultSkinMatchByPixels(client, baseSkin);
        DefaultSkinKey defaultSkinKey = pixelKey = pixelMatch == null ? null : pixelMatch.key();
        DefaultSkinKey resolvedKey = key != null ? key : (pixelMatch != null && pixelMatch.accepted() ? pixelMatch.key() : null);
        PlayerSkinType model = client.player.getSkin() == null ? null : client.player.getSkin().comp_1629();
        String profileName = client.player.getName().getString();
        UUID uuid = client.player.getUuid();
        DefaultSkinCandidate defaultDestroyed = resolvedKey == null ? null : NonDestroyedSkinClient.describeDefaultDestroyedSkinCandidate(client, resolvedKey);
        String message = "[NoN RippedSkinInfo] profile=" + profileName + " uuid=" + String.valueOf(uuid) + " base=" + String.valueOf(baseSkin) + " model=" + String.valueOf(model) + " defaultPath=" + defaultSkinPath + " profileTextures=" + profileHasCustomTexture + " parsed=" + (String)(key == null ? "<none>" : key.model() + "/" + key.name()) + " pixel=" + (String)(pixelKey == null ? "<none>" : pixelKey.model() + "/" + pixelKey.name()) + " confidence=" + (pixelMatch == null ? "<none>" : String.format(Locale.ROOT, "%.1f%%", pixelMatch.confidence() * 100.0)) + " accepted=" + (pixelMatch != null && pixelMatch.accepted()) + " resolved=" + (String)(resolvedKey == null ? "<none>" : resolvedKey.model() + "/" + resolvedKey.name()) + " candidate=" + (defaultDestroyed == null ? "<none>" : defaultDestroyed.logString());
        NeedsOfNature.LOGGER.info(message);
        NonDestroyedSkinClient.sendRippedSkinInfoLine(client, "NoN Ripped Skin Info", Formatting.GOLD);
        NonDestroyedSkinClient.sendRippedSkinInfoLine(client, "Player: " + profileName + " (" + String.valueOf(uuid) + ")", Formatting.GRAY);
        NonDestroyedSkinClient.sendRippedSkinInfoLine(client, "Base texture: " + String.valueOf(baseSkin), Formatting.WHITE);
        NonDestroyedSkinClient.sendRippedSkinInfoLine(client, "Model: " + String.valueOf(model), Formatting.WHITE);
        NonDestroyedSkinClient.sendRippedSkinInfoLine(client, "Default path: " + defaultSkinPath + " | Profile textures: " + profileHasCustomTexture, Formatting.WHITE);
        NonDestroyedSkinClient.sendRippedSkinInfoLine(client, "Parsed key: " + NonDestroyedSkinClient.formatDefaultSkinKey(key), Formatting.WHITE);
        NonDestroyedSkinClient.sendRippedSkinInfoLine(client, "Pixel match: " + NonDestroyedSkinClient.formatDefaultSkinKey(pixelKey) + " | Confidence: " + (pixelMatch == null ? "<none>" : String.format(Locale.ROOT, "%.1f%%", pixelMatch.confidence() * 100.0)) + " | Accepted: " + (pixelMatch != null && pixelMatch.accepted()), pixelMatch != null && pixelMatch.accepted() ? Formatting.GREEN : Formatting.YELLOW);
        NonDestroyedSkinClient.sendRippedSkinInfoLine(client, "Resolved key: " + NonDestroyedSkinClient.formatDefaultSkinKey(resolvedKey), resolvedKey == null ? Formatting.RED : Formatting.GREEN);
        if (defaultDestroyed == null) {
            NonDestroyedSkinClient.sendRippedSkinInfoLine(client, "Ripped candidates: <none>", Formatting.RED);
        } else {
            NonDestroyedSkinClient.sendRippedSkinInfoLine(client, "Ripped candidate: " + String.valueOf(defaultDestroyed.genderSpecific()) + " = " + defaultDestroyed.genderFound(), defaultDestroyed.genderFound() ? Formatting.GREEN : Formatting.YELLOW);
            NonDestroyedSkinClient.sendRippedSkinInfoLine(client, "Fallback candidate: " + String.valueOf(defaultDestroyed.generic()) + " = " + defaultDestroyed.genericFound(), defaultDestroyed.genericFound() ? Formatting.GREEN : Formatting.GRAY);
        }
        return 1;
    }

    private static void sendRippedSkinInfoLine(MinecraftClient client, String message, Formatting color) {
        client.player.sendMessage((Text)Text.literal((String)message).formatted(color), false);
    }

    private static String formatDefaultSkinKey(@Nullable DefaultSkinKey key) {
        return key == null ? "<none>" : key.model() + "/" + key.name();
    }

    private static DefaultSkinCandidate describeDefaultDestroyedSkinCandidate(MinecraftClient client, DefaultSkinKey key) {
        String genderSuffix = NeedsOfNature.getConfig().getPlayerGenderSelection() == NonConfig.PlayerGenderSelection.MALE ? "_m" : "_f";
        Identifier genderSpecific = NonDestroyedSkinClient.defaultDestroyedSkinId(key, genderSuffix);
        Identifier generic = NonDestroyedSkinClient.defaultDestroyedSkinId(key, "");
        boolean genderFound = client.getResourceManager().getResource(genderSpecific).isPresent();
        boolean genericFound = client.getResourceManager().getResource(generic).isPresent();
        return new DefaultSkinCandidate(genderSpecific, genderFound, generic, genericFound);
    }

    @Nullable
    private static byte[] readDefaultDestroyedSkin(MinecraftClient client, DefaultSkinKey key) {
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
    private static byte[] readDefaultDestroyedSkin(MinecraftClient client, DefaultSkinKey key, String suffix) {
        byte[] byArray;
        block9: {
            Identifier destroyedId = NonDestroyedSkinClient.defaultDestroyedSkinId(key, suffix);
            Resource resource = client.getResourceManager().getResource(destroyedId).orElse(null);
            if (resource == null) {
                return null;
            }
            InputStream in = resource.getInputStream();
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

    private static Identifier defaultDestroyedSkinId(DefaultSkinKey key, String suffix) {
        return Identifier.of((String)"needsofnature", (String)(DEFAULT_DESTROYED_SKIN_PREFIX + key.model() + "/" + key.name() + suffix + ".png"));
    }

    @Nullable
    private static DefaultSkinKey resolveDefaultSkinKey(MinecraftClient client, Identifier baseSkin) {
        DefaultSkinKey parsed = NonDestroyedSkinClient.parseDefaultSkinKey(baseSkin);
        if (parsed != null) {
            return parsed;
        }
        DefaultSkinMatch match = NonDestroyedSkinClient.detectDefaultSkinMatchByPixels(client, baseSkin);
        return match != null && match.accepted() ? match.key() : null;
    }

    @Nullable
    private static DefaultSkinKey parseDefaultSkinKey(Identifier baseSkin) {
        String[] parts;
        if (baseSkin == null || !"minecraft".equals(baseSkin.getNamespace())) {
            return null;
        }
        String path = baseSkin.getPath();
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
    private static DefaultSkinMatch detectDefaultSkinMatchByPixels(MinecraftClient client, Identifier baseSkin) {
        if (client == null) return null;
        if (baseSkin == null) {
            return null;
        }
        PlayerSkinType currentModel = client.player != null && client.player.getSkin() != null ? client.player.getSkin().comp_1629() : PlayerSkinType.SLIM;
        String preferredModel = currentModel == PlayerSkinType.WIDE ? "wide" : "slim";
        String fallbackModel = "wide".equals(preferredModel) ? "slim" : "wide";
        try (NativeImage base = NonDestroyedSkinClient.readTextureImage(client, baseSkin);){
            if (base == null || base.getWidth() != 64 || base.getHeight() != 64) {
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
    private static DefaultSkinMatch detectDefaultSkinMatchByPixels(MinecraftClient client, NativeImage base, String model) {
        DefaultSkinMatch best = null;
        for (String name : VANILLA_DEFAULT_SKIN_NAMES) {
            Identifier vanillaId = Identifier.ofVanilla((String)(DEFAULT_PLAYER_SKIN_PREFIX + model + "/" + name + ".png"));
            try (NativeImage vanilla = NonDestroyedSkinClient.readTextureImage(client, vanillaId);){
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

    private static double imageMatchConfidence(NativeImage first, NativeImage second) {
        if (first == null || second == null) {
            return 0.0;
        }
        if (first.getWidth() != second.getWidth() || first.getHeight() != second.getHeight()) {
            return 0.0;
        }
        int checked = 0;
        int matched = 0;
        for (int y = 0; y < first.getHeight(); ++y) {
            for (int x = 0; x < first.getWidth(); ++x) {
                int firstArgb = first.getColorArgb(x, y);
                int secondArgb = second.getColorArgb(x, y);
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
    private static NativeImage createMaskedOverlay(MinecraftClient client, UUID playerUuid, byte[] skin, int stage) throws IOException {
        NativeImage destroyed = NativeImage.read((byte[])skin);
        NativeImage mask = NonDestroyedSkinClient.readMask(client, playerUuid, stage);
        if (mask == null) {
            destroyed.close();
            return null;
        }
        NativeImage out = new NativeImage(NativeImage.Format.RGBA, destroyed.getWidth(), destroyed.getHeight(), false);
        int maskWidth = mask.getWidth();
        int maskHeight = mask.getHeight();
        for (int y = 0; y < destroyed.getHeight(); ++y) {
            for (int x = 0; x < destroyed.getWidth(); ++x) {
                int skinArgb = destroyed.getColorArgb(x, y);
                int maskArgb = mask.getColorArgb(x % maskWidth, y % maskHeight);
                int factor = NonDestroyedSkinClient.maskFactor(maskArgb);
                int alpha = (skinArgb >>> 24 & 0xFF) * factor / 255;
                out.setColorArgb(x, y, skinArgb & 0xFFFFFF | alpha << 24);
            }
        }
        destroyed.close();
        mask.close();
        return out;
    }

    /*
     * Exception decompiling
     */
    private static byte[] createGeneratedDestroyedSkin(MinecraftClient client, Identifier baseSkin) throws IOException {
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

    private static NativeImage readDestroyedBaseTexture(MinecraftClient client, PlayerSkinType model) throws IOException {
        boolean wide;
        boolean useMaleTexture = NeedsOfNature.getConfig().getPlayerGenderSelection() == NonConfig.PlayerGenderSelection.MALE;
        Identifier bundledModel = NonDestroyedSkinClient.defaultDestroyedTexture(useMaleTexture, wide = model == PlayerSkinType.WIDE);
        NativeImage bundledModelImage = NonDestroyedSkinClient.readBundledDestroyedTexture(client, bundledModel);
        if (bundledModelImage != null) {
            return bundledModelImage;
        }
        Identifier bundledGender = useMaleTexture ? DEFAULT_DESTROYED_TEXTURE_M : DEFAULT_DESTROYED_TEXTURE_F;
        NativeImage bundledGenderImage = NonDestroyedSkinClient.readBundledDestroyedTexture(client, bundledGender);
        if (bundledGenderImage != null) {
            return bundledGenderImage;
        }
        throw new MissingDestroyedSkinFallbackException("Missing ripped skin fallback texture " + String.valueOf(bundledModel) + " and fallback " + String.valueOf(bundledGender));
    }

    @Nullable
    private static NativeImage readBundledDestroyedTexture(MinecraftClient client, Identifier id) throws IOException {
        Resource resource = client.getResourceManager().getResource(id).orElse(null);
        if (resource == null) {
            return null;
        }
        try (InputStream in = resource.getInputStream();){
            NativeImage LootTableData = NativeImage.read((InputStream)in);
            return LootTableData;
        }
    }

    private static Identifier defaultDestroyedTexture(boolean useMaleTexture, boolean wide) {
        if (useMaleTexture) {
            return wide ? DEFAULT_DESTROYED_TEXTURE_M_WIDE : DEFAULT_DESTROYED_TEXTURE_M_SLIM;
        }
        return wide ? DEFAULT_DESTROYED_TEXTURE_F_WIDE : DEFAULT_DESTROYED_TEXTURE_F_SLIM;
    }

    private static void copyRegion(NativeImage source, NativeImage target, int minX, int minY, int maxX, int maxY) {
        int endX = Math.min(Math.min(source.getWidth(), target.getWidth()), maxX);
        int endY = Math.min(Math.min(source.getHeight(), target.getHeight()), maxY);
        for (int y = Math.max(0, minY); y < endY; ++y) {
            for (int x = Math.max(0, minX); x < endX; ++x) {
                target.setColorArgb(x, y, source.getColorArgb(x, y));
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static byte[] writeImageToBytes(NativeImage image) throws IOException {
        Path temp = Files.createTempFile("non_destroyed_skin", ".png", new FileAttribute[0]);
        try {
            image.writeTo(temp);
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

    private static int detectSkinTint(NativeImage skin) {
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

    private static void addTintSamples(NativeImage skin, Map<Integer, ColorBucket> out, int minX, int minY, int maxX, int maxY, int weight) {
        int clampedMaxX = Math.min(maxX, skin.getWidth());
        int clampedMaxY = Math.min(maxY, skin.getHeight());
        for (int y = Math.max(0, minY); y < clampedMaxY; ++y) {
            for (int x = Math.max(0, minX); x < clampedMaxX; ++x) {
                int argb = skin.getColorArgb(x, y);
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
    private static NativeImage readMask(MinecraftClient client, UUID playerUuid, int stage) {
        NativeImage LootTableData;
        block14: {
            Identifier maskId;
            byte[] synced;
            if (stage <= 0 || stage >= 4) {
                return null;
            }
            byte[][] syncedMasks = DESTROYED_SKIN_MASKS.get(playerUuid);
            if (syncedMasks != null && stage < syncedMasks.length && (synced = syncedMasks[stage]) != null && synced.length > 0) {
                try {
                    return NativeImage.read((byte[])synced);
                }
                catch (IOException | RuntimeException e) {
                    NonDestroyedSkinClient.setupWarn("[NoN] Failed to read synced destroyed skin mask for {} stage {}.", playerUuid, stage, e);
                }
            }
            if ((maskId = MASK_TEXTURES[stage]) == null) {
                return null;
            }
            Resource resource = client.getResourceManager().getResource(maskId).orElse(null);
            if (resource == null) {
                return null;
            }
            InputStream in = resource.getInputStream();
            try {
                LootTableData = NativeImage.read((InputStream)in);
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
        return LootTableData;
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

    private static void applyMessOverlays(MinecraftClient client, NativeImage out, MessState state) {
        if (client == null || out == null || state == null || !state.hasAnyMess()) {
            return;
        }
        NonDestroyedSkinClient.applyMessRole(client, out, state.vMess(), state.tintRgb(), MESS_MASK_TEXTURES[0]);
        NonDestroyedSkinClient.applyMessRole(client, out, state.aMess(), state.tintRgb(), MESS_MASK_TEXTURES[1]);
        NonDestroyedSkinClient.applyMessRole(client, out, state.mMess(), state.tintRgb(), MESS_MASK_TEXTURES[2]);
    }

    private static void applyTankOverlay(MinecraftClient client, NativeImage out, MessState state, TankMaskType tankMaskType) {
        if (client == null || out == null || state == null || !state.hasTankOverlay()) {
            return;
        }
        Identifier maskId = tankMaskType == TankMaskType.A ? A_MESS_TANK_TEXTURE : V_MESS_TANK_TEXTURE;
        NativeImage mask = NonDestroyedSkinClient.readMessMask(client, maskId);
        if (mask == null) {
            return;
        }
        NonDestroyedSkinClient.applyTintMask(out, mask, state.tintRgb(), state.tankAlpha());
        mask.close();
    }

    private static void applyAccessoryOverlays(MinecraftClient client, NativeImage out, List<Identifier> overlays) {
        if (client == null || out == null || overlays == null || overlays.isEmpty()) {
            return;
        }
        for (Identifier overlayId : overlays) {
            NativeImage overlay = NonDestroyedSkinClient.readAccessoryOverlay(client, overlayId);
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
    private static NativeImage readAccessoryOverlay(MinecraftClient client, Identifier overlayId) {
        if (client == null) return null;
        if (overlayId == null) {
            return null;
        }
        try {
            Resource resource = client.getResourceManager().getResource(overlayId).orElse(null);
            if (resource == null) {
                return null;
            }
            try (InputStream in = resource.getInputStream();){
                NativeImage image = NativeImage.read((InputStream)in);
                if (image.getWidth() != 64 || image.getHeight() != 64) {
                    image.close();
                    NonDestroyedSkinClient.setupWarn("[NoN] Ignoring accessory skin overlay {}: expected 64x64 PNG.", overlayId);
                    NativeImage BlockFamilyRecipeFactory = null;
                    return BlockFamilyRecipeFactory;
                }
                NativeImage LootTableData = image;
                return LootTableData;
            }
        }
        catch (IOException | RuntimeException e) {
            NonDestroyedSkinClient.setupWarn("[NoN] Failed to read accessory skin overlay {}.", overlayId, e);
            return null;
        }
    }

    private static void applyRawOverlay(NativeImage out, NativeImage overlay) {
        int width = Math.min(out.getWidth(), overlay.getWidth());
        int height = Math.min(out.getHeight(), overlay.getHeight());
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int overlayArgb = overlay.getColorArgb(x, y);
                int alpha = overlayArgb >>> 24 & 0xFF;
                if (alpha <= 0) continue;
                out.setColorArgb(x, y, NonDestroyedSkinClient.blendArgb(out.getColorArgb(x, y), overlayArgb, alpha));
            }
        }
    }

    private static void applyMessRole(MinecraftClient client, NativeImage out, int mess, int tintRgb, Identifier[] masks) {
        if (mess <= 0 || masks == null || masks.length < 3) {
            return;
        }
        for (MessLayer layer : NonDestroyedSkinClient.messLayers(mess)) {
            NativeImage mask;
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

    private static void applyTintMask(NativeImage out, NativeImage mask, int tintRgb, int alpha) {
        int maskWidth = mask.getWidth();
        int maskHeight = mask.getHeight();
        int overlayBase = 0xFF000000 | tintRgb & 0xFFFFFF;
        for (int y = 0; y < out.getHeight(); ++y) {
            for (int x = 0; x < out.getWidth(); ++x) {
                int factor = NonDestroyedSkinClient.maskFactor(mask.getColorArgb(x % maskWidth, y % maskHeight)) * alpha / 255;
                if (factor <= 0) continue;
                out.setColorArgb(x, y, NonDestroyedSkinClient.blendArgb(out.getColorArgb(x, y), overlayBase, factor));
            }
        }
    }

    @Nullable
    private static NativeImage readMessMask(MinecraftClient client, Identifier maskId) {
        NativeImage LootTableData;
        block10: {
            if (client == null || maskId == null) {
                return null;
            }
            Resource resource = client.getResourceManager().getResource(maskId).orElse(null);
            if (resource == null) {
                return null;
            }
            InputStream in = resource.getInputStream();
            try {
                LootTableData = NativeImage.read((InputStream)in);
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
        return LootTableData;
    }

    private static boolean isValidSkinPng(byte[] bytes) {
        boolean bl;
        block9: {
            if (bytes == null || bytes.length == 0 || bytes.length > 65536) {
                return false;
            }
            NativeImage image = NativeImage.read((byte[])bytes);
            try {
                boolean bl2 = bl = image.getWidth() == 64 && image.getHeight() == 64;
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

    private static int[] getOriginalSkinColors(MinecraftClient client, @Nullable Identifier baseSkin) {
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
    private static int[] readOriginalSkinColors(MinecraftClient client, Identifier baseSkin) {
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

    private static int varyColor(int rgb, Random random) {
        float factor = 0.85f + random.nextFloat() * 0.3f;
        int r = Math.min(255, Math.max(0, Math.round((float)(rgb >> 16 & 0xFF) * factor)));
        int g = Math.min(255, Math.max(0, Math.round((float)(rgb >> 8 & 0xFF) * factor)));
        int b = Math.min(255, Math.max(0, Math.round((float)(rgb & 0xFF) * factor)));
        return r << 16 | g << 8 | b;
    }

    private static void clearCachedTextures(UUID playerUuid) {
        MinecraftClient client = MinecraftClient.getInstance();
        ArrayList<Identifier> texturesToDestroy = new ArrayList<Identifier>();
        TEXTURE_CACHE.entrySet().removeIf(entry -> {
            if (!((TextureKey)entry.getKey()).playerUuid().equals(playerUuid)) {
                return false;
            }
            texturesToDestroy.add((Identifier)entry.getValue());
            return true;
        });
        NonDestroyedSkinClient.destroyTextures(client, texturesToDestroy);
    }

    private static void destroyTextures(@Nullable MinecraftClient client, List<Identifier> textureIds) {
        if (client == null || textureIds == null || textureIds.isEmpty()) {
            return;
        }
        List<Identifier> ids = List.copyOf(textureIds);
        client.execute(() -> {
            for (Identifier id : ids) {
                client.getTextureManager().destroyTexture(id);
            }
        });
    }

    private static int clampStage(int stage) {
        return Math.max(0, Math.min(4, stage));
    }

    private static int clampMess(int value) {
        return Math.max(0, Math.min(10, value));
    }

    private static TankMaskType resolveTankMaskType(@Nullable LivingEntity entity) {
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

    private record TextureKey(UUID playerUuid, int stage, @Nullable Identifier baseTexture, MessState messState, TankMaskType tankMaskType, List<Identifier> accessoryOverlays) {
    }

    private record DefaultSkinMatch(DefaultSkinKey key, double confidence) {
        boolean accepted() {
            return this.confidence >= 0.9;
        }
    }

    private record DefaultSkinCandidate(Identifier genderSpecific, boolean genderFound, Identifier generic, boolean genericFound) {
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

