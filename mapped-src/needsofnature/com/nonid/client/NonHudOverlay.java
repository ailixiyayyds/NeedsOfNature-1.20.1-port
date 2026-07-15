/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.afwid.client.runtime.AfwClientAnimationRuntime
 *  com.afwid.network.AnimationStageInfo
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.client.texture.Sprite
 *  net.minecraft.client.texture.SpriteAtlasTexture
 *  net.minecraft.util.Atlases
 *  net.minecraft.client.gl.RenderPipelines
 *  net.minecraft.entity.effect.StatusEffectInstance
 *  net.minecraft.util.Identifier
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.gui.DrawContext
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.client.network.ClientPlayerEntity
 *  org.joml.Matrix3x2fStack
 */
package com.nonid.client;

import com.afwid.client.runtime.AfwClientAnimationRuntime;
import com.afwid.network.AnimationStageInfo;
import com.nonid.EnergyHolder;
import com.nonid.NeedsOfNature;
import com.nonid.NonConfig;
import com.nonid.client.NonLiquidColors;
import com.nonid.data.NonLoopSecondsOverrides;
import com.nonid.data.NonPeakStages;
import com.nonid.effect.NonStatusEffects;
import com.nonid.network.AttackStateS2CPayload;
import com.nonid.network.StageProgressS2CPayload;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Atlases;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import org.joml.Matrix3x2fStack;

@Environment(value=EnvType.CLIENT)
public final class NonHudOverlay {
    private static final Identifier ENERGY_BAR_BG = Identifier.of((String)"needsofnature", (String)"hud/progress_background");
    private static final Identifier ENERGY_BAR_FILL = Identifier.of((String)"needsofnature", (String)"hud/progress_full");
    private static final Identifier ATTACK_BAR_BG = Identifier.of((String)"needsofnature", (String)"hud/attack_background");
    private static final Identifier ATTACK_BAR_FILL = Identifier.of((String)"needsofnature", (String)"hud/attack_full");
    private static final Identifier ATTACK_PROMPT = Identifier.of((String)"needsofnature", (String)"hud/ad_mash");
    private static final Identifier LIQUID_TANK_BG = Identifier.of((String)"needsofnature", (String)"hud/liquid_background");
    private static final Identifier LIQUID_TANK_FILL = Identifier.of((String)"needsofnature", (String)"hud/liquid_full");
    private static final Identifier ENERGY_HEART = Identifier.of((String)"needsofnature", (String)"textures/particle/smallheart.png");
    private static final int ATTACK_PROMPT_TARGET_SIZE = 32;
    private static final int ATTACK_PROMPT_DEFAULT_TEX_W = 32;
    private static final int ATTACK_PROMPT_DEFAULT_TEX_H = 64;
    private static final int ENERGY_HEART_TEX_SIZE = 8;
    private static final int ENERGY_HEART_BASE_SIZE = 8;
    private static final int LIQUID_DEFAULT_W = 16;
    private static final float LOOP_PROGRESS_HIDDEN = -1.0f;
    private static final int LIQUID_DEFAULT_H = 32;
    private static final float LIQUID_SCALE = 1.0f;
    private static final float ATTACK_ESCAPE_MIN_HEALTH_FACTOR = 0.3f;
    private static final long DEFEATED_EASE_DURATION_TICKS = 300L;
    private static final float DEFEATED_MAX_STEP_MULTIPLIER = 4.0f;
    private static boolean hideVanillaHud = false;
    private static final Map<UUID, StageProgressS2CPayload> STAGE_PROGRESS_STATES = new HashMap<UUID, StageProgressS2CPayload>();
    private static float loopProgress = 0.0f;
    private static long lastUpdateTick = -1L;
    private static UUID attackInstance = null;
    private static float attackProgress = 0.0f;
    private static AttackKey lastAttackKey = AttackKey.NONE;
    private static long lastAttackDecayTick = -1L;
    private static boolean attackInputClearPending = false;
    private static boolean attackEscapable = true;
    private static AttackStateS2CPayload.EscapeProfile attackEscapeProfile = AttackStateS2CPayload.EscapeProfile.NONE;
    private static int liquidStored = 0;
    private static int liquidCapacity = 200;
    private static int liquidTintRgb = 15920063;
    private static int runtimeLoopSeconds = -1;
    private static int runtimePeakLoopSeconds = -1;
    private static int runtimeAttackEscapeHits = -1;
    private static double runtimeAttackDecayPerSecond = -1.0;
    private static int runtimeAttackEscapeDamageDifficultyPercent = -1;
    private static Boolean runtimeAttackCreativePlayers = null;
    private static boolean uiPreviewEnabled = false;
    private static boolean uiPreviewOverrideActive = false;
    private static int previewEnergyX = 0;
    private static int previewEnergyY = 0;
    private static int previewEnergyHeartX = 0;
    private static int previewEnergyHeartY = 0;
    private static int previewAttackX = 0;
    private static int previewAttackY = 0;
    private static int previewPromptX = 0;
    private static int previewPromptY = 0;
    private static int previewLiquidX = 0;
    private static int previewLiquidY = 0;
    private static boolean previewEnergyVisible = true;
    private static boolean previewEnergyHeartVisible = true;
    private static boolean previewAttackVisible = true;
    private static boolean previewPromptVisible = true;
    private static boolean previewLiquidVisible = true;

    private NonHudOverlay() {
    }

    public static void clientTick(MinecraftClient client) {
        long now;
        if (client == null) {
            return;
        }
        ClientPlayerEntity player = client.player;
        if (player == null || client.world == null) {
            NonHudOverlay.clearAnimationHudState();
            liquidStored = 0;
            liquidTintRgb = 15920063;
            return;
        }
        if (player.isSpectator()) {
            NonHudOverlay.clearAnimationHudState();
            return;
        }
        boolean animActive = AfwClientAnimationRuntime.isActorPendingOrActive((UUID)player.getUuid());
        UUID activeInstance = AfwClientAnimationRuntime.findLatestActiveInstanceContaining((UUID)player.getUuid());
        if (!animActive) {
            NonHudOverlay.clearAnimationHudState();
            return;
        }
        hideVanillaHud = true;
        if (attackInstance != null && activeInstance != null && !activeInstance.equals(attackInstance) && !attackInputClearPending) {
            NonHudOverlay.resetAttackState();
        }
        if (lastUpdateTick != (now = client.world.getTime())) {
            lastUpdateTick = now;
            loopProgress = NonHudOverlay.computeLoopProgress(player.getUuid(), now);
            NonHudOverlay.tickAttackDecay(now);
        }
    }

    public static void onDisconnect(MinecraftClient client) {
        NonHudOverlay.clearAnimationHudState();
        liquidStored = 0;
        liquidTintRgb = 15920063;
        NonHudOverlay.clearRuntimeGameplaySettings();
    }

    private static void clearAnimationHudState() {
        hideVanillaHud = false;
        STAGE_PROGRESS_STATES.clear();
        loopProgress = 0.0f;
        NonHudOverlay.resetAttackState();
    }

    public static boolean shouldHideVanillaHud() {
        return hideVanillaHud;
    }

    public static boolean shouldRenderGameplayHud() {
        MinecraftClient client = MinecraftClient.getInstance();
        return client != null && client.player != null && client.options != null && !client.options.hudHidden && !client.player.isSpectator();
    }

    public static void renderOverlay(DrawContext ctx) {
        UUID inst;
        float progress;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || ctx == null) {
            return;
        }
        if (!NonHudOverlay.shouldRenderGameplayHud()) {
            return;
        }
        int width = ctx.getScaledWindowWidth();
        int height = ctx.getScaledWindowHeight();
        int texW = 182;
        int texH = 5;
        int baseEnergyX = (width - texW) / 2;
        int baseEnergyY = height - 32;
        UiOffsets offsets = NonHudOverlay.resolveUiOffsets(false);
        UiVisibility visibility = NonHudOverlay.resolveUiVisibility(false);
        int energyX = baseEnergyX + offsets.energyX();
        int energyY = baseEnergyY + offsets.energyY();
        SpriteSize attackSize = NonHudOverlay.resolveSpriteSize(ATTACK_BAR_BG, texW, texH);
        int attackBarW = attackSize.width();
        int attackBarH = attackSize.height();
        int baseAttackX = baseEnergyX + (texW - attackBarW) / 2;
        int baseAttackY = baseEnergyY - 8 - attackBarH;
        float displayedLoopProgress = loopProgress;
        if (client.player != null && client.world != null) {
            float tickProgress = client.getRenderTickCounter().getTickProgress(false);
            displayedLoopProgress = NonHudOverlay.computeLoopProgress(client.player.getUuid(), (float)client.world.getTime() + Math.max(0.0f, tickProgress));
        }
        float f = progress = displayedLoopProgress < 0.0f ? -1.0f : Math.max(0.0f, Math.min(1.0f, displayedLoopProgress));
        if (visibility.energy() && progress >= 0.0f) {
            int filled = Math.max(0, Math.min(texW, Math.round(progress * (float)texW)));
            ctx.drawGuiTexture(RenderPipelines.GUI_TEXTURED, ENERGY_BAR_BG, energyX, energyY, texW, texH);
            if (filled > 0) {
                ctx.enableScissor(energyX, energyY, energyX + filled, energyY + texH);
                ctx.drawGuiTexture(RenderPipelines.GUI_TEXTURED, ENERGY_BAR_FILL, energyX, energyY, texW, texH);
                ctx.disableScissor();
            }
        }
        if (client.player != null && attackInstance != null && AfwClientAnimationRuntime.isActorPendingOrActive((UUID)client.player.getUuid()) && ((inst = AfwClientAnimationRuntime.findLatestActiveInstanceContaining((UUID)client.player.getUuid())) == null || inst.equals(attackInstance)) && attackEscapable) {
            int attackBarX = baseAttackX + offsets.attackX();
            int attackBarY = baseAttackY + offsets.attackY();
            if (visibility.attack()) {
                float attackFill = Math.max(0.0f, Math.min(1.0f, attackProgress));
                int attackFilled = Math.max(0, Math.min(attackBarW, Math.round(attackFill * (float)attackBarW)));
                ctx.drawGuiTexture(RenderPipelines.GUI_TEXTURED, ATTACK_BAR_BG, attackBarX, attackBarY, attackBarW, attackBarH);
                if (attackFilled > 0) {
                    ctx.enableScissor(attackBarX, attackBarY, attackBarX + attackFilled, attackBarY + attackBarH);
                    ctx.drawGuiTexture(RenderPipelines.GUI_TEXTURED, ATTACK_BAR_FILL, attackBarX, attackBarY, attackBarW, attackBarH);
                    ctx.disableScissor();
                }
            }
            if (visibility.prompt()) {
                AttackPromptInfo promptInfo = NonHudOverlay.resolveAttackPromptInfo();
                int frameW = Math.max(1, promptInfo.frameWidth());
                int frameH = Math.max(1, promptInfo.frameHeight());
                float scale = 32.0f / (float)frameW;
                int promptW = Math.round((float)frameW * scale);
                int promptH = Math.round((float)frameH * scale);
                int basePromptX = (width - promptW) / 2;
                int basePromptY = baseAttackY - promptH - 6;
                int promptX = basePromptX + offsets.promptX();
                int promptY = basePromptY + offsets.promptY();
                if (promptY < 4) {
                    promptY = 4;
                }
                int v = lastAttackKey == AttackKey.LEFT ? frameH : 0;
                float invScale = scale == 0.0f ? 1.0f : 1.0f / scale;
                int drawX = Math.round((float)promptX * invScale);
                int drawY = Math.round((float)promptY * invScale);
                Matrix3x2fStack matrices = ctx.getMatrices();
                matrices.pushMatrix();
                matrices.scale(scale, scale);
                ctx.drawGuiTexture(RenderPipelines.GUI_TEXTURED, ATTACK_PROMPT, promptInfo.textureWidth(), promptInfo.textureHeight(), 0, v, drawX, drawY, frameW, frameH);
                matrices.popMatrix();
            }
        }
        NonHudOverlay.renderLiquidOverlay(ctx);
    }

    public static void renderLiquidOverlay(DrawContext ctx) {
        if (!NonHudOverlay.shouldRenderGameplayHud()) {
            return;
        }
        NonHudOverlay.renderLiquidOverlayInternal(ctx, false);
        NonHudOverlay.renderEnergyHeartOverlay(ctx, false);
    }

    public static void renderUiPreview(DrawContext ctx) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || ctx == null) {
            return;
        }
        int width = ctx.getScaledWindowWidth();
        int height = ctx.getScaledWindowHeight();
        int texW = 182;
        int texH = 5;
        int baseEnergyX = (width - texW) / 2;
        int baseEnergyY = height - 32;
        UiOffsets offsets = NonHudOverlay.resolveUiOffsets(true);
        UiVisibility visibility = NonHudOverlay.resolveUiVisibility(true);
        int energyX = baseEnergyX + offsets.energyX();
        int energyY = baseEnergyY + offsets.energyY();
        SpriteSize attackSize = NonHudOverlay.resolveSpriteSize(ATTACK_BAR_BG, texW, texH);
        int attackBarW = attackSize.width();
        int attackBarH = attackSize.height();
        int baseAttackX = baseEnergyX + (texW - attackBarW) / 2;
        int baseAttackY = baseEnergyY - 8 - attackBarH;
        if (visibility.energy()) {
            NonHudOverlay.drawBar(ctx, ENERGY_BAR_BG, ENERGY_BAR_FILL, energyX, energyY, texW, texH, 0.6f);
        }
        int attackBarX = baseAttackX + offsets.attackX();
        int attackBarY = baseAttackY + offsets.attackY();
        if (visibility.attack()) {
            NonHudOverlay.drawBar(ctx, ATTACK_BAR_BG, ATTACK_BAR_FILL, attackBarX, attackBarY, attackBarW, attackBarH, 0.4f);
        }
        if (visibility.prompt()) {
            AttackPromptInfo promptInfo = NonHudOverlay.resolveAttackPromptInfo();
            int frameW = Math.max(1, promptInfo.frameWidth());
            int frameH = Math.max(1, promptInfo.frameHeight());
            float scale = 32.0f / (float)frameW;
            int promptW = Math.round((float)frameW * scale);
            int promptH = Math.round((float)frameH * scale);
            int basePromptX = (width - promptW) / 2;
            int basePromptY = baseAttackY - promptH - 6;
            int promptX = basePromptX + offsets.promptX();
            int promptY = basePromptY + offsets.promptY();
            if (promptY < 4) {
                promptY = 4;
            }
            int v = 0;
            float invScale = scale == 0.0f ? 1.0f : 1.0f / scale;
            int drawX = Math.round((float)promptX * invScale);
            int drawY = Math.round((float)promptY * invScale);
            Matrix3x2fStack matrices = ctx.getMatrices();
            matrices.pushMatrix();
            matrices.scale(scale, scale);
            ctx.drawGuiTexture(RenderPipelines.GUI_TEXTURED, ATTACK_PROMPT, promptInfo.textureWidth(), promptInfo.textureHeight(), 0, v, drawX, drawY, frameW, frameH);
            matrices.popMatrix();
        }
        NonHudOverlay.renderLiquidOverlayInternal(ctx, true);
        NonHudOverlay.renderEnergyHeartOverlay(ctx, true);
    }

    public static UiPreviewBounds getUiPreviewBounds(DrawContext ctx) {
        if (ctx == null) {
            return UiPreviewBounds.empty();
        }
        int width = ctx.getScaledWindowWidth();
        int height = ctx.getScaledWindowHeight();
        int texW = 182;
        int texH = 5;
        int baseEnergyX = (width - texW) / 2;
        int baseEnergyY = height - 32;
        UiOffsets offsets = NonHudOverlay.resolveUiOffsets(true);
        Rect energy = new Rect(baseEnergyX + offsets.energyX(), baseEnergyY + offsets.energyY(), texW, texH);
        SpriteSize attackSize = NonHudOverlay.resolveSpriteSize(ATTACK_BAR_BG, texW, texH);
        int attackBarW = attackSize.width();
        int attackBarH = attackSize.height();
        int baseAttackX = baseEnergyX + (texW - attackBarW) / 2;
        int baseAttackY = baseEnergyY - 8 - attackBarH;
        Rect attack = new Rect(baseAttackX + offsets.attackX(), baseAttackY + offsets.attackY(), attackBarW, attackBarH);
        AttackPromptInfo promptInfo = NonHudOverlay.resolveAttackPromptInfo();
        int frameW = Math.max(1, promptInfo.frameWidth());
        int frameH = Math.max(1, promptInfo.frameHeight());
        float promptScale = 32.0f / (float)frameW;
        int promptW = Math.round((float)frameW * promptScale);
        int promptH = Math.round((float)frameH * promptScale);
        int basePromptX = (width - promptW) / 2;
        int basePromptY = baseAttackY - promptH - 6;
        int promptY = basePromptY + offsets.promptY();
        if (promptY < 4) {
            promptY = 4;
        }
        Rect prompt = new Rect(basePromptX + offsets.promptX(), promptY, promptW, promptH);
        SpriteSize liquidSize = NonHudOverlay.resolveSpriteSize(LIQUID_TANK_BG, 16, 32);
        int tankW = liquidSize.width();
        int tankH = liquidSize.height();
        int scaledW = Math.max(1, Math.round((float)tankW * 1.0f));
        int scaledH = Math.max(1, Math.round((float)tankH * 1.0f));
        int heartsX = width / 2 - 91;
        int heartsY = height - 39;
        int x = heartsX - scaledW + 192 + offsets.liquidX();
        int y = heartsY + 10 - scaledH + 29 + offsets.liquidY();
        Rect liquid = new Rect(x, y, scaledW, scaledH);
        Rect heart = NonHudOverlay.energyHeartRect(ctx, true);
        return new UiPreviewBounds(energy, heart, attack, prompt, liquid);
    }

    private static void renderLiquidOverlayInternal(DrawContext ctx, boolean preview) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || ctx == null) {
            return;
        }
        if (!NonHudOverlay.resolveUiVisibility(preview).liquid()) {
            return;
        }
        int capacity = liquidCapacity;
        int stored = liquidStored;
        if (preview) {
            capacity = Math.max(1, capacity);
            stored = Math.max(1, capacity / 2);
        }
        int width = ctx.getScaledWindowWidth();
        int height = ctx.getScaledWindowHeight();
        UiOffsets offsets = NonHudOverlay.resolveUiOffsets(preview);
        SpriteSize bgSize = NonHudOverlay.resolveSpriteSize(LIQUID_TANK_BG, 16, 32);
        int tankW = bgSize.width();
        int tankH = bgSize.height();
        float scale = 1.0f;
        int scaledW = Math.max(1, Math.round((float)tankW * scale));
        int scaledH = Math.max(1, Math.round((float)tankH * scale));
        int heartsX = width / 2 - 91;
        int heartsY = height - 39;
        int heartsH = 10;
        int x = heartsX - scaledW + 192 + offsets.liquidX();
        int y = heartsY + heartsH - scaledH + 29 + offsets.liquidY();
        if (stored <= 0 || capacity <= 0) {
            return;
        }
        float progress = Math.max(0.0f, Math.min(1.0f, (float)stored / (float)capacity));
        int filled = Math.max(0, Math.min(scaledH, Math.round(progress * (float)scaledH)));
        ctx.drawGuiTexture(RenderPipelines.GUI_TEXTURED, LIQUID_TANK_BG, x, y, scaledW, scaledH);
        if (filled > 0) {
            int fillY = y + scaledH - filled;
            ctx.enableScissor(x, fillY, x + scaledW, y + scaledH);
            int fillColor = preview ? -1 : NonLiquidColors.toOpaqueArgb(liquidTintRgb);
            ctx.drawGuiTexture(RenderPipelines.GUI_TEXTURED, LIQUID_TANK_FILL, x, y, scaledW, scaledH, fillColor);
            ctx.disableScissor();
        }
    }

    private static void renderEnergyHeartOverlay(DrawContext ctx, boolean preview) {
        if (!NonHudOverlay.resolveUiVisibility(preview).energyHeart()) {
            return;
        }
        Rect rect = NonHudOverlay.energyHeartRect(ctx, preview);
        NonHudOverlay.renderEnergyHeart(ctx, rect.x(), rect.y(), preview);
    }

    private static Rect energyHeartRect(DrawContext ctx, boolean preview) {
        int width = ctx.getScaledWindowWidth();
        int height = ctx.getScaledWindowHeight();
        SpriteSize bgSize = NonHudOverlay.resolveSpriteSize(LIQUID_TANK_BG, 16, 32);
        int tankW = bgSize.width();
        int tankH = bgSize.height();
        int scaledW = Math.max(1, Math.round((float)tankW * 1.0f));
        int scaledH = Math.max(1, Math.round((float)tankH * 1.0f));
        int heartsX = width / 2 - 91;
        int heartsY = height - 39;
        int tankX = heartsX - scaledW + 192;
        int tankY = heartsY + 10 - scaledH + 29;
        UiOffsets offsets = NonHudOverlay.resolveUiOffsets(preview);
        int centerX = tankX + scaledW / 2 + offsets.energyHeartX();
        int centerY = tankY - 1 - 4 + offsets.energyHeartY();
        int x = centerX - 4;
        int y = centerY - 4;
        return new Rect(x, y, 8, 8);
    }

    private static void renderEnergyHeart(DrawContext ctx, int x, int y, boolean preview) {
        int energizedLevel;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || ctx == null) {
            return;
        }
        int n = energizedLevel = preview ? 2 : NonHudOverlay.getEnergizedLevel(client.player);
        if (energizedLevel <= 0) {
            return;
        }
        double pulse = NonHudOverlay.computeHeartPulse(energizedLevel);
        float scale = switch (energizedLevel) {
            case 1 -> 1.0f + (float)pulse * 0.35f;
            case 2 -> 1.0f + (float)pulse * 0.45f;
            default -> 1.0f + (float)pulse * 0.85f;
        };
        int centerX = x + 4;
        int centerY = y + 4;
        int alpha = switch (energizedLevel) {
            case 1 -> 150 + (int)Math.round(pulse * 70.0);
            case 2 -> 185 + (int)Math.round(pulse * 65.0);
            default -> 200 + (int)Math.round(pulse * 55.0);
        };
        int color = Math.max(0, Math.min(255, alpha)) << 24 | 0xFFFFFF;
        Matrix3x2fStack matrices = ctx.getMatrices();
        matrices.pushMatrix();
        matrices.translate((float)centerX, (float)centerY);
        matrices.scale(scale, scale);
        matrices.translate((float)(-centerX), (float)(-centerY));
        ctx.drawTexture(RenderPipelines.GUI_TEXTURED, ENERGY_HEART, x, y, 0.0f, 0.0f, 8, 8, 8, 8, color);
        matrices.popMatrix();
    }

    public static int getEnergizedLevel(ClientPlayerEntity player) {
        if (player == null) {
            return 0;
        }
        for (StatusEffectInstance effect : player.getStatusEffects()) {
            if (effect.getEffectType().comp_349() != NonStatusEffects.ENERGIZED) continue;
            return Math.max(1, Math.min(3, effect.getAmplifier() + 1));
        }
        return 0;
    }

    public static double computeHeartPulse(int energizedLevel) {
        double periodMs = switch (energizedLevel) {
            case 1 -> 900.0;
            case 2 -> 450.0;
            default -> 225.0;
        };
        double attackMs = switch (energizedLevel) {
            case 1 -> 120.0;
            case 2 -> 55.0;
            default -> 30.0;
        };
        double decayMs = switch (energizedLevel) {
            case 1 -> 200.0;
            case 2 -> 150.0;
            default -> 100.0;
        };
        double elapsedMs = System.currentTimeMillis() % (long)periodMs;
        if (elapsedMs >= attackMs + decayMs) {
            return 0.0;
        }
        if (elapsedMs < attackMs) {
            double attack = elapsedMs / attackMs;
            return attack * attack;
        }
        double decay = (elapsedMs - attackMs) / decayMs;
        double remaining = Math.max(0.0, 1.0 - decay);
        return remaining * remaining;
    }

    private static void drawBar(DrawContext ctx, Identifier bg, Identifier fill, int x, int y, int w, int h, float progress) {
        float clamped = Math.max(0.0f, Math.min(1.0f, progress));
        int filled = Math.max(0, Math.min(w, Math.round(clamped * (float)w)));
        ctx.drawGuiTexture(RenderPipelines.GUI_TEXTURED, bg, x, y, w, h);
        if (filled > 0) {
            ctx.enableScissor(x, y, x + filled, y + h);
            ctx.drawGuiTexture(RenderPipelines.GUI_TEXTURED, fill, x, y, w, h);
            ctx.disableScissor();
        }
    }

    public static void setStageProgressState(StageProgressS2CPayload payload) {
        if (payload == null || payload.instanceId() == null || payload.stageAnimationId() == null) {
            return;
        }
        STAGE_PROGRESS_STATES.compute(payload.instanceId(), (instanceId, existing) -> {
            if (existing == null || payload.startTick() >= existing.startTick()) {
                return payload;
            }
            return existing;
        });
    }

    private static float computeLoopProgress(UUID playerUuid, double nowTick) {
        boolean syncedStage;
        UUID inst = AfwClientAnimationRuntime.findLatestActiveInstanceContaining((UUID)playerUuid);
        if (inst == null) {
            return 0.0f;
        }
        long stageStartTick = AfwClientAnimationRuntime.findStageStartTick((UUID)inst);
        AnimationStageInfo currentStage = AfwClientAnimationRuntime.findCurrentStage((UUID)inst);
        if (stageStartTick < 0L || currentStage == null) {
            return 0.0f;
        }
        StageProgressS2CPayload synced = STAGE_PROGRESS_STATES.get(inst);
        boolean bl = syncedStage = synced != null && currentStage.animationId().equals((Object)synced.stageAnimationId());
        if (syncedStage && synced.mode() == StageProgressS2CPayload.Mode.HIDDEN) {
            return -1.0f;
        }
        if (currentStage.loop() && syncedStage && synced.mode() == StageProgressS2CPayload.Mode.SERVER_WINDOW && synced.endTick() > synced.startTick()) {
            return NonHudOverlay.clampProgress((nowTick - (double)synced.startTick()) / (double)(synced.endTick() - synced.startTick()));
        }
        double speed = AfwClientAnimationRuntime.findLatestSpeedForActor((UUID)playerUuid);
        if (!Double.isFinite(speed) || speed <= 0.0) {
            speed = 1.0;
        }
        if (!currentStage.loop()) {
            double lengthTicks;
            double d = lengthTicks = currentStage.cycleTicks() > 0.0 ? currentStage.cycleTicks() : (double)currentStage.lengthTicks();
            if (lengthTicks <= 0.0) {
                return 0.0f;
            }
            double authoredElapsedTicks = Math.max(0.0, nowTick - (double)stageStartTick) * speed;
            return NonHudOverlay.clampProgress(authoredElapsedTicks / lengthTicks);
        }
        double fallbackDurationTicks = NonHudOverlay.resolveFallbackLoopDurationTicks(currentStage, speed);
        if (fallbackDurationTicks < 0.0) {
            return -1.0f;
        }
        if (fallbackDurationTicks <= 0.0) {
            return 0.0f;
        }
        return NonHudOverlay.clampProgress(Math.max(0.0, nowTick - (double)stageStartTick) / fallbackDurationTicks);
    }

    private static double resolveFallbackLoopDurationTicks(AnimationStageInfo currentStage, double speed) {
        int loopSeconds = NonHudOverlay.resolveRuntimeLoopSeconds();
        double stageDurationMultiplier = 1.0;
        Identifier stageAnimationId = currentStage.animationId();
        if (stageAnimationId != null) {
            stageDurationMultiplier = NonLoopSecondsOverrides.getStageDurationMultiplier(stageAnimationId);
            Integer override = NonLoopSecondsOverrides.getOverrideSeconds(stageAnimationId);
            if (NonLoopSecondsOverrides.isInfinite(override)) {
                return -1.0;
            }
            if (override != null) {
                loopSeconds = override;
            } else {
                Identifier baseId = NonPeakStages.baseAnimationId(stageAnimationId);
                Integer baseOverride = NonLoopSecondsOverrides.getOverrideSeconds(baseId);
                if (NonLoopSecondsOverrides.isInfinite(baseOverride)) {
                    return -1.0;
                }
                if (baseOverride != null) {
                    loopSeconds = baseOverride;
                } else {
                    Integer peakStage = NonPeakStages.getPeakStage(stageAnimationId);
                    Integer stageNumber = NonPeakStages.stageNumberFromId(stageAnimationId);
                    if (Objects.equals(stageNumber, peakStage)) {
                        loopSeconds = NonHudOverlay.resolveRuntimePeakLoopSeconds();
                    }
                }
            }
        }
        double targetTicks = Math.max(1.0, (double)loopSeconds * 20.0 * stageDurationMultiplier);
        double cycleTicks = NonLoopSecondsOverrides.getCycleTicks(stageAnimationId);
        if (cycleTicks <= 0.0) {
            cycleTicks = currentStage.cycleTicks();
        }
        if (cycleTicks <= 0.0) {
            return targetTicks;
        }
        double cycleWorldTicks = cycleTicks / speed;
        if (!Double.isFinite(cycleWorldTicks) || cycleWorldTicks <= 0.0) {
            return targetTicks;
        }
        long cycleCount = Math.max(1L, Math.round(targetTicks / cycleWorldTicks));
        return Math.max(1.0, (double)Math.round((double)cycleCount * cycleWorldTicks));
    }

    private static float clampProgress(double progress) {
        return (float)Math.max(0.0, Math.min(1.0, progress));
    }

    private static AttackPromptInfo resolveAttackPromptInfo() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) {
            return new AttackPromptInfo(32, 64);
        }
        SpriteAtlasTexture atlas = client.getAtlasManager().getAtlasTexture(Atlases.GUI);
        Sprite sprite = atlas.getSprite(ATTACK_PROMPT);
        if (sprite == null) {
            return new AttackPromptInfo(32, 64);
        }
        int texW = sprite.getContents().getWidth();
        int texH = sprite.getContents().getHeight();
        if (texW <= 0 || texH <= 0) {
            return new AttackPromptInfo(32, 64);
        }
        return new AttackPromptInfo(texW, texH);
    }

    private static SpriteSize resolveSpriteSize(Identifier id, int fallbackW, int fallbackH) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) {
            return new SpriteSize(fallbackW, fallbackH);
        }
        SpriteAtlasTexture atlas = client.getAtlasManager().getAtlasTexture(Atlases.GUI);
        Sprite sprite = atlas.getSprite(id);
        if (sprite == null) {
            return new SpriteSize(fallbackW, fallbackH);
        }
        int texW = sprite.getContents().getWidth();
        int texH = sprite.getContents().getHeight();
        if (texW <= 0 || texH <= 0) {
            return new SpriteSize(fallbackW, fallbackH);
        }
        return new SpriteSize(texW, texH);
    }

    public static void setRuntimeGameplaySettings(int loopSeconds, int peakLoopSeconds, int attackEscapeHits, double attackDecayPerSecond, int attackEscapeDamageDifficultyPercent, boolean attackCreativePlayers) {
        runtimeLoopSeconds = Math.max(5, Math.min(300, loopSeconds));
        runtimePeakLoopSeconds = Math.max(1, Math.min(300, peakLoopSeconds));
        runtimeAttackEscapeHits = Math.max(1, Math.min(50, attackEscapeHits));
        runtimeAttackDecayPerSecond = Math.max(0.0, Math.min(5.0, attackDecayPerSecond));
        runtimeAttackEscapeDamageDifficultyPercent = Math.max(0, Math.min(200, attackEscapeDamageDifficultyPercent));
        runtimeAttackCreativePlayers = attackCreativePlayers;
    }

    public static int getRuntimeLoopSeconds() {
        return NonHudOverlay.resolveRuntimeLoopSeconds();
    }

    public static int getRuntimePeakLoopSeconds() {
        return NonHudOverlay.resolveRuntimePeakLoopSeconds();
    }

    public static int getRuntimeAttackEscapeHits() {
        return NonHudOverlay.resolveRuntimeAttackEscapeHits();
    }

    public static double getRuntimeAttackDecayPerSecond() {
        return NonHudOverlay.resolveRuntimeAttackDecayPerSecond();
    }

    public static int getRuntimeAttackEscapeDamageDifficultyPercent() {
        return NonHudOverlay.resolveRuntimeAttackEscapeDamageDifficultyPercent();
    }

    public static boolean getRuntimeAttackCreativePlayers() {
        if (runtimeAttackCreativePlayers != null) {
            return runtimeAttackCreativePlayers;
        }
        NonConfig config = NeedsOfNature.getConfig();
        return config != null && config.canAttackCreativePlayers();
    }

    private static int resolveRuntimeLoopSeconds() {
        if (runtimeLoopSeconds >= 0) {
            return Math.max(5, Math.min(300, runtimeLoopSeconds));
        }
        NonConfig config = NeedsOfNature.getConfig();
        if (config == null) {
            return 15;
        }
        return config.getLoopProgressSeconds();
    }

    private static int resolveRuntimePeakLoopSeconds() {
        if (runtimePeakLoopSeconds >= 0) {
            return Math.max(1, Math.min(300, runtimePeakLoopSeconds));
        }
        NonConfig config = NeedsOfNature.getConfig();
        if (config == null) {
            return 7;
        }
        return config.getPeakLoopProgressSeconds();
    }

    private static int resolveRuntimeAttackEscapeHits() {
        if (runtimeAttackEscapeHits >= 0) {
            return Math.max(1, Math.min(50, runtimeAttackEscapeHits));
        }
        NonConfig config = NeedsOfNature.getConfig();
        if (config == null) {
            return 12;
        }
        return config.getAttackEscapeHits();
    }

    private static double resolveRuntimeAttackDecayPerSecond() {
        if (runtimeAttackDecayPerSecond >= 0.0) {
            return Math.max(0.0, Math.min(5.0, runtimeAttackDecayPerSecond));
        }
        NonConfig config = NeedsOfNature.getConfig();
        if (config == null) {
            return 0.2;
        }
        return config.getAttackDecayPerSecond();
    }

    private static int resolveRuntimeAttackEscapeDamageDifficultyPercent() {
        if (runtimeAttackEscapeDamageDifficultyPercent >= 0) {
            return Math.max(0, Math.min(200, runtimeAttackEscapeDamageDifficultyPercent));
        }
        NonConfig config = NeedsOfNature.getConfig();
        if (config == null) {
            return 100;
        }
        return config.getAttackEscapeDamageDifficultyPercent();
    }

    private static void clearRuntimeGameplaySettings() {
        runtimeLoopSeconds = -1;
        runtimePeakLoopSeconds = -1;
        runtimeAttackEscapeHits = -1;
        runtimeAttackDecayPerSecond = -1.0;
        runtimeAttackEscapeDamageDifficultyPercent = -1;
        runtimeAttackCreativePlayers = null;
    }

    public static void setLiquidState(int stored, int capacity, int tintRgb) {
        liquidStored = Math.max(0, stored);
        liquidCapacity = Math.max(1, capacity);
        liquidTintRgb = tintRgb & 0xFFFFFF;
        if (liquidStored > liquidCapacity) {
            liquidStored = liquidCapacity;
        }
    }

    public static float getLiquidFillRatio() {
        if (liquidCapacity <= 0) {
            return 0.0f;
        }
        float ratio = (float)liquidStored / (float)liquidCapacity;
        return Math.max(0.0f, Math.min(1.0f, ratio));
    }

    public static int getLiquidTintRgb() {
        return liquidTintRgb;
    }

    public static void setEscapeControlState(UUID instanceId, boolean attack, boolean escapable, AttackStateS2CPayload.EscapeProfile escapeProfile) {
        if (instanceId == null) {
            return;
        }
        if (attack) {
            if (!instanceId.equals(attackInstance)) {
                NonHudOverlay.resetAttackState();
                attackInstance = instanceId;
                attackInputClearPending = true;
            }
            attackEscapable = escapable;
            attackEscapeProfile = NonHudOverlay.sanitizeEscapeProfile(escapeProfile, attack);
        } else if (instanceId.equals(attackInstance)) {
            NonHudOverlay.resetAttackState();
        }
    }

    private static AttackStateS2CPayload.EscapeProfile sanitizeEscapeProfile(AttackStateS2CPayload.EscapeProfile profile, boolean attack) {
        if (!attack) {
            return AttackStateS2CPayload.EscapeProfile.NONE;
        }
        if (profile == null || profile == AttackStateS2CPayload.EscapeProfile.NONE) {
            return AttackStateS2CPayload.EscapeProfile.NORMAL;
        }
        return profile;
    }

    public static boolean isAttackInstance(UUID instanceId) {
        return instanceId != null && instanceId.equals(attackInstance);
    }

    public static void recordAttackInput(UUID instanceId, AttackKey key) {
        if (!NonHudOverlay.isAttackInstance(instanceId)) {
            return;
        }
        if (key == AttackKey.NONE) {
            return;
        }
        if (!attackEscapable) {
            return;
        }
        if (lastAttackKey != key) {
            float step = NonHudOverlay.resolveAttackProgressStep();
            attackProgress = Math.min(1.0f, attackProgress + step);
            lastAttackKey = key;
        }
    }

    public static boolean isAttackProgressComplete() {
        return attackProgress >= 1.0f;
    }

    public static void resetAttackProgress() {
        attackProgress = 0.0f;
        lastAttackKey = AttackKey.NONE;
        lastAttackDecayTick = -1L;
    }

    private static void resetAttackState() {
        attackInstance = null;
        NonHudOverlay.resetAttackProgress();
        attackInputClearPending = false;
        attackEscapable = true;
        attackEscapeProfile = AttackStateS2CPayload.EscapeProfile.NONE;
    }

    public static void setUiPreviewEnabled(boolean enabled) {
        uiPreviewEnabled = enabled;
        if (!enabled) {
            uiPreviewOverrideActive = false;
        }
    }

    public static boolean isUiPreviewEnabled() {
        return uiPreviewEnabled;
    }

    public static void setUiPreviewOffsets(int energyX, int energyY, int attackX, int attackY, int promptX, int promptY, int liquidX, int liquidY, int energyHeartX, int energyHeartY, boolean energyVisible, boolean energyHeartVisible, boolean attackVisible, boolean promptVisible, boolean liquidVisible) {
        uiPreviewOverrideActive = true;
        previewEnergyX = energyX;
        previewEnergyY = energyY;
        previewAttackX = attackX;
        previewAttackY = attackY;
        previewPromptX = promptX;
        previewPromptY = promptY;
        previewLiquidX = liquidX;
        previewLiquidY = liquidY;
        previewEnergyHeartX = energyHeartX;
        previewEnergyHeartY = energyHeartY;
        previewEnergyVisible = energyVisible;
        previewEnergyHeartVisible = energyHeartVisible;
        previewAttackVisible = attackVisible;
        previewPromptVisible = promptVisible;
        previewLiquidVisible = liquidVisible;
    }

    private static UiOffsets resolveUiOffsets(boolean preview) {
        int liquidY;
        NonConfig config = NeedsOfNature.getConfig();
        int energyX = config == null ? 0 : config.getUiEnergyOffsetX();
        int energyY = config == null ? 0 : config.getUiEnergyOffsetY();
        int energyHeartX = config == null ? 0 : config.getUiEnergyHeartOffsetX();
        int energyHeartY = config == null ? 0 : config.getUiEnergyHeartOffsetY();
        int attackX = config == null ? 0 : config.getUiAttackOffsetX();
        int attackY = config == null ? 0 : config.getUiAttackOffsetY();
        int promptX = config == null ? 0 : config.getUiPromptOffsetX();
        int promptY = config == null ? 0 : config.getUiPromptOffsetY();
        int liquidX = config == null ? 0 : config.getUiLiquidOffsetX();
        int n = liquidY = config == null ? 0 : config.getUiLiquidOffsetY();
        if (preview && uiPreviewOverrideActive) {
            return new UiOffsets(previewEnergyX, previewEnergyY, previewEnergyHeartX, previewEnergyHeartY, previewAttackX, previewAttackY, previewPromptX, previewPromptY, previewLiquidX, previewLiquidY);
        }
        return new UiOffsets(energyX, energyY, energyHeartX, energyHeartY, attackX, attackY, promptX, promptY, liquidX, liquidY);
    }

    private static UiVisibility resolveUiVisibility(boolean preview) {
        boolean liquid;
        NonConfig config = NeedsOfNature.getConfig();
        boolean energy = config == null || config.isUiEnergyVisible();
        boolean energyHeart = config == null || config.isUiEnergyHeartVisible();
        boolean attack = config == null || config.isUiAttackVisible();
        boolean prompt = config == null || config.isUiPromptVisible();
        boolean bl = liquid = config == null || config.isUiLiquidVisible();
        if (preview && uiPreviewOverrideActive) {
            return new UiVisibility(previewEnergyVisible, previewEnergyHeartVisible, previewAttackVisible, previewPromptVisible, previewLiquidVisible);
        }
        return new UiVisibility(energy, energyHeart, attack, prompt, liquid);
    }

    public static boolean consumeAttackInputClear() {
        if (!attackInputClearPending) {
            return false;
        }
        attackInputClearPending = false;
        return true;
    }

    private static float resolveAttackProgressStep() {
        int hits = NonHudOverlay.resolveRuntimeAttackEscapeHits();
        if (hits <= 0) {
            return 1.0f;
        }
        float base = 1.0f / (float)hits;
        float healthFactor = NonHudOverlay.resolveAttackHealthFactor();
        if (healthFactor <= 0.0f) {
            return 0.0f;
        }
        float energyFactor = NonHudOverlay.resolveAttackEnergyFactor();
        if (energyFactor <= 0.0f) {
            return 0.0f;
        }
        float rngScale = healthFactor >= 0.999f ? 1.0f : NonHudOverlay.randomAttackScale();
        float defeatedEase = NonHudOverlay.resolveDefeatedEaseProgress();
        if (defeatedEase <= 0.0f) {
            return base * healthFactor * rngScale * energyFactor;
        }
        float adjustedHealth = MathHelper.lerp((float)defeatedEase, (float)healthFactor, (float)1.0f);
        float adjustedEnergy = MathHelper.lerp((float)defeatedEase, (float)energyFactor, (float)1.0f);
        float adjustedRng = MathHelper.lerp((float)defeatedEase, (float)rngScale, (float)1.0f);
        float multiplier = MathHelper.lerp((float)defeatedEase, (float)1.0f, (float)4.0f);
        return base * adjustedHealth * adjustedRng * adjustedEnergy * multiplier;
    }

    private static float resolveDefeatedEaseProgress() {
        if (attackInstance == null) {
            return 0.0f;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null || client.world == null) {
            return 0.0f;
        }
        if (attackEscapeProfile != AttackStateS2CPayload.EscapeProfile.DEFEATED) {
            return 0.0f;
        }
        Long startTick = AfwClientAnimationRuntime.findStartTickForActor((UUID)client.player.getUuid());
        if (startTick == null) {
            return 0.0f;
        }
        long elapsed = Math.max(0L, client.world.getTime() - startTick);
        return MathHelper.clamp((float)((float)elapsed / 300.0f), (float)0.0f, (float)1.0f);
    }

    private static float resolveAttackHealthFactor() {
        float minHealth;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) {
            return 1.0f;
        }
        float health = client.player.getHealth();
        float maxHealth = client.player.getMaxHealth();
        if (maxHealth <= (minHealth = 0.5f)) {
            return 0.3f;
        }
        float rawFactor = (health - minHealth) / (maxHealth - minHealth);
        rawFactor = MathHelper.clamp((float)rawFactor, (float)0.0f, (float)1.0f);
        float impact = (float)NonHudOverlay.resolveRuntimeAttackEscapeDamageDifficultyPercent() / 100.0f;
        float factor = 1.0f - (1.0f - rawFactor) * impact;
        return MathHelper.clamp((float)factor, (float)0.3f, (float)1.0f);
    }

    private static float resolveAttackEnergyFactor() {
        ClientPlayerEntity class_7462;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || !((class_7462 = client.player) instanceof EnergyHolder)) {
            return 1.0f;
        }
        EnergyHolder holder = (EnergyHolder)class_7462;
        int max = Math.max(1, holder.getMaxEnergy());
        float energy = Math.max(0.0f, (float)Math.min(max, holder.getEnergy()));
        if (energy <= 100.0f) {
            float t = energy / 100.0f;
            return 1.0f - 0.1f * t;
        }
        float t = Math.min(1.0f, (energy - 100.0f) / 100.0f);
        float factor = 0.9f - 0.82f * t * t;
        return Math.max(0.08f, factor);
    }

    private static float randomAttackScale() {
        return 0.5f + ThreadLocalRandom.current().nextFloat();
    }

    private static void tickAttackDecay(long nowTick) {
        if (attackInstance == null) {
            lastAttackDecayTick = nowTick;
            return;
        }
        if (lastAttackDecayTick < 0L) {
            lastAttackDecayTick = nowTick;
            return;
        }
        long elapsedTicks = nowTick - lastAttackDecayTick;
        if (elapsedTicks <= 0L) {
            return;
        }
        lastAttackDecayTick = nowTick;
        double decayPerSecond = NonHudOverlay.resolveRuntimeAttackDecayPerSecond();
        if (decayPerSecond <= 0.0) {
            return;
        }
        float decayPerTick = (float)(decayPerSecond / 20.0);
        attackProgress = Math.max(0.0f, attackProgress - decayPerTick * (float)elapsedTicks);
    }

    private record UiOffsets(int energyX, int energyY, int energyHeartX, int energyHeartY, int attackX, int attackY, int promptX, int promptY, int liquidX, int liquidY) {
    }

    private record UiVisibility(boolean energy, boolean energyHeart, boolean attack, boolean prompt, boolean liquid) {
    }

    private record SpriteSize(int width, int height) {
    }

    private record AttackPromptInfo(int textureWidth, int textureHeight) {
        int frameWidth() {
            return this.textureWidth;
        }

        int frameHeight() {
            return this.textureHeight / 2;
        }
    }

    public static enum AttackKey {
        NONE,
        LEFT,
        RIGHT;

    }

    public record UiPreviewBounds(Rect energy, Rect energyHeart, Rect attack, Rect prompt, Rect liquid) {
        public static UiPreviewBounds empty() {
            Rect empty = new Rect(0, 0, 0, 0);
            return new UiPreviewBounds(empty, empty, empty, empty, empty);
        }
    }

    public record Rect(int x, int y, int width, int height) {
        public boolean contains(double mouseX, double mouseY) {
            return mouseX >= (double)this.x && mouseX < (double)(this.x + this.width) && mouseY >= (double)this.y && mouseY < (double)(this.y + this.height);
        }

        public Rect padded(int padding) {
            int safe = Math.max(0, padding);
            return new Rect(this.x - safe, this.y - safe, this.width + safe * 2, this.height + safe * 2);
        }
    }
}

