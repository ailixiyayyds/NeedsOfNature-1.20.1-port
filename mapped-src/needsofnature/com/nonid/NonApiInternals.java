/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.util.Identifier
 *  net.minecraft.server.network.ServerPlayerEntity
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid;

import com.nonid.DestroyedSkinHolder;
import com.nonid.GenderHolder;
import com.nonid.LiquidHolder;
import com.nonid.MessHolder;
import com.nonid.NeedsOfNature;
import com.nonid.NonAccessoryEffects;
import com.nonid.NonDestroyedSkinSystem;
import com.nonid.NonGenderSystem;
import com.nonid.NonLiquidSystem;
import com.nonid.NonMessSystem;
import com.nonid.NonTrinketsIntegration;
import com.nonid.PregnancyHolder;
import com.nonid.api.NonAccessoryEffectsSnapshot;
import com.nonid.api.NonChangeSource;
import com.nonid.api.NonDestroyedSkinSnapshot;
import com.nonid.api.NonEvents;
import com.nonid.api.NonGender;
import com.nonid.api.NonLiquidComposition;
import com.nonid.api.NonLiquidTankSnapshot;
import com.nonid.api.NonMessSnapshot;
import com.nonid.api.NonMessType;
import com.nonid.api.NonPlayerStateSnapshot;
import com.nonid.api.NonPregnancySnapshot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

public final class NonApiInternals {
    private static final int DESTROYED_SKIN_MAX_DAMAGE = 10;

    private NonApiInternals() {
    }

    public static NonPlayerStateSnapshot snapshotPlayerState(PlayerEntity player) {
        return new NonPlayerStateSnapshot(NonApiInternals.getGender(player), NonApiInternals.snapshotLiquid(player), NonApiInternals.snapshotMess(player), NonApiInternals.snapshotDestroyedSkin(player), NonApiInternals.snapshotPregnancy(player), NonApiInternals.snapshotAccessoryEffects(player));
    }

    public static NonGender getGender(PlayerEntity player) {
        if (player instanceof GenderHolder) {
            GenderHolder holder = (GenderHolder)player;
            return NonGender.fromMask(holder.getGenderMask());
        }
        return NonGender.NONE;
    }

    public static boolean setGender(ServerPlayerEntity player, NonGender gender, NonChangeSource source) {
        int n;
        if (player == null || gender == null || gender == NonGender.NONE) {
            return false;
        }
        if (!NonApiInternals.canChangeGender(player, source)) {
            return false;
        }
        if (player instanceof GenderHolder) {
            GenderHolder holder = (GenderHolder)player;
            n = holder.getGenderMask() & 3;
        } else {
            n = 0;
        }
        int before = n;
        int applied = NonGenderSystem.setPlayerGenderFromCommand(player, gender.mask(), source);
        return (before & 3) != (applied & 3);
    }

    public static boolean canChangeGender(ServerPlayerEntity player, NonChangeSource source) {
        if (player == null || source == null) {
            return false;
        }
        return switch (source) {
            default -> throw new MatchException(null, null);
            case NonChangeSource.API, NonChangeSource.COMMAND, NonChangeSource.ITEM, NonChangeSource.JOIN_SELECTION, NonChangeSource.SYSTEM -> true;
            case NonChangeSource.CLIENT_PREFERENCE -> NonGenderSystem.canClientChangeGenderFreely();
        };
    }

    public static NonLiquidTankSnapshot snapshotLiquid(PlayerEntity player) {
        if (player instanceof LiquidHolder) {
            LiquidHolder holder = (LiquidHolder)player;
            return new NonLiquidTankSnapshot(Math.max(0, holder.getLiquidStored()), Math.max(0, holder.getLiquidCapacity()), NonApiInternals.toApiLiquidComposition(holder.getLiquidComposition()), holder.getLiquidEntityTypeId(), NonLiquidSystem.resolveLiquidTintRgb(holder));
        }
        return new NonLiquidTankSnapshot(0, 0, NonLiquidComposition.EMPTY, null, 0xFFFFFF);
    }

    public static int addLiquid(ServerPlayerEntity player, @Nullable Identifier sourceEntityType, int amountMl) {
        LiquidHolder holder;
        block7: {
            block6: {
                if (!NeedsOfNature.getConfig().isLiquidTankEnabled()) {
                    return 0;
                }
                if (player == null || !(player instanceof LiquidHolder)) break block6;
                holder = (LiquidHolder)player;
                if (amountMl > 0) break block7;
            }
            return 0;
        }
        int modifiedAmount = ((NonEvents.ModifyLiquidGain)NonEvents.MODIFY_LIQUID_GAIN.invoker()).modifyLiquidGain(player, sourceEntityType, amountMl);
        if (modifiedAmount <= 0) {
            return 0;
        }
        int added = NonApiInternals.addLiquidWithComposition(holder, sourceEntityType, modifiedAmount);
        if (added > 0) {
            NeedsOfNature.syncLiquidState(player);
        }
        return added;
    }

    public static int drainLiquid(ServerPlayerEntity player, int amountMl) {
        LiquidHolder holder;
        block6: {
            block5: {
                if (player == null || !(player instanceof LiquidHolder)) break block5;
                holder = (LiquidHolder)player;
                if (amountMl > 0) break block6;
            }
            return 0;
        }
        int before = Math.max(0, holder.getLiquidStored());
        int removed = Math.min(before, amountMl);
        if (removed <= 0) {
            return 0;
        }
        int next = before - removed;
        holder.setLiquidStored(next);
        if (next <= 0) {
            holder.clearLiquidComposition();
        }
        NeedsOfNature.syncLiquidState(player);
        return removed;
    }

    public static NonMessSnapshot snapshotMess(PlayerEntity player) {
        if (player instanceof MessHolder) {
            MessHolder holder = (MessHolder)player;
            return new NonMessSnapshot(NonMessSystem.clampMess(holder.getVMess()), NonMessSystem.clampMess(holder.getAMess()), NonMessSystem.clampMess(holder.getMMess()));
        }
        return new NonMessSnapshot(0, 0, 0);
    }

    public static boolean addMess(ServerPlayerEntity player, NonMessType type, int amount) {
        return NonApiInternals.changeMess(player, type, Math.max(0, amount));
    }

    public static boolean cleanMess(ServerPlayerEntity player, NonMessType type, int amount) {
        return NonApiInternals.changeMess(player, type, -Math.max(0, amount));
    }

    public static boolean clearMess(ServerPlayerEntity player) {
        if (player == null || !(player instanceof MessHolder)) {
            return false;
        }
        MessHolder holder = (MessHolder)player;
        boolean changed = holder.getVMess() != 0 || holder.getAMess() != 0 || holder.getMMess() != 0;
        NonMessSystem.clearMess(player);
        return changed;
    }

    public static NonDestroyedSkinSnapshot snapshotDestroyedSkin(PlayerEntity player) {
        if (player instanceof DestroyedSkinHolder) {
            DestroyedSkinHolder holder = (DestroyedSkinHolder)player;
            return new NonDestroyedSkinSnapshot(Math.max(0, Math.min(4, holder.getDestroyedSkinStage())), Math.max(0, Math.min(10, holder.getDestroyedSkinDamage())), 10);
        }
        return new NonDestroyedSkinSnapshot(0, 0, 10);
    }

    public static boolean damageDestroyedSkin(ServerPlayerEntity player, int amount) {
        return NonApiInternals.changeDestroyedSkinDamage(player, Math.max(0, amount));
    }

    public static boolean repairDestroyedSkin(ServerPlayerEntity player, int amount) {
        return NonApiInternals.changeDestroyedSkinDamage(player, -Math.max(0, amount));
    }

    public static NonPregnancySnapshot snapshotPregnancy(PlayerEntity player) {
        if (player instanceof PregnancyHolder) {
            PregnancyHolder holder = (PregnancyHolder)player;
            return new NonPregnancySnapshot(holder.getPregnantEntityTypeId(), holder.getPregnancyEndTick(), holder.isPregnancyPendingHatch());
        }
        return new NonPregnancySnapshot(null, 0L, false);
    }

    public static boolean beginPregnancy(ServerPlayerEntity player, Identifier entityTypeId, long endTick) {
        if (!NeedsOfNature.getConfig().isPregnancyEnabled()) {
            return false;
        }
        if (player == null || entityTypeId == null) {
            return false;
        }
        NeedsOfNature.beginPregnancy(player, entityTypeId, Math.max(1L, endTick));
        return true;
    }

    public static boolean clearPregnancy(ServerPlayerEntity player) {
        PregnancyHolder holder;
        if (player == null) {
            return false;
        }
        boolean hadPregnancy = player instanceof PregnancyHolder && (holder = (PregnancyHolder)player).getPregnantEntityTypeId() != null;
        NeedsOfNature.clearPregnancyState(player, true);
        return hadPregnancy;
    }

    public static NonAccessoryEffectsSnapshot snapshotAccessoryEffects(PlayerEntity player) {
        NonAccessoryEffects nonAccessoryEffects;
        if (player instanceof LivingEntity) {
            PlayerEntity living = player;
            nonAccessoryEffects = NonTrinketsIntegration.getAccessoryEffects((LivingEntity)living);
        } else {
            nonAccessoryEffects = NonAccessoryEffects.NEUTRAL;
        }
        NonAccessoryEffects effects = nonAccessoryEffects;
        return new NonAccessoryEffectsSnapshot(effects.liquidDecayMultiplier(), effects.equalizeLiquidDecayContext(), effects.playerEnergyGainMultiplier(), effects.liquidCapacityAdd(), effects.liquidGainMultiplier(), effects.filledEffectMultiplier(), effects.pregnancyChanceMultiplier(), effects.pregnancyDurationMultiplier(), effects.messGainMultiplier(), effects.destroyedSkinDamageMultiplier(), effects.attackEscapeHitsAdd(), effects.attackEscapeDamageMultiplier(), effects.playerEnergyAuraMultiplier(), effects.nearAnimationMobEnergyMultiplier());
    }

    public static boolean isMessSystemEnabled() {
        return NeedsOfNature.getConfig().isMessSystemEnabled();
    }

    public static boolean isDestroyedSkinSystemEnabled() {
        return NeedsOfNature.getConfig().isDestroyedSkinSystemEnabled();
    }

    public static boolean isLiquidTankEnabled() {
        return NeedsOfNature.getConfig().isLiquidTankEnabled();
    }

    public static boolean isPregnancyEnabled() {
        return NeedsOfNature.getConfig().isPregnancyEnabled();
    }

    public static void fireLiquidChanged(ServerPlayerEntity player) {
        if (player != null) {
            ((NonEvents.LiquidTankChanged)NonEvents.LIQUID_TANK_CHANGED.invoker()).onLiquidTankChanged(player, NonApiInternals.snapshotLiquid((PlayerEntity)player));
        }
    }

    public static void fireMessChanged(ServerPlayerEntity player) {
        if (player != null) {
            ((NonEvents.MessChanged)NonEvents.MESS_CHANGED.invoker()).onMessChanged(player, NonApiInternals.snapshotMess((PlayerEntity)player));
        }
    }

    public static void fireDestroyedSkinChanged(ServerPlayerEntity player) {
        if (player != null) {
            ((NonEvents.DestroyedSkinChanged)NonEvents.DESTROYED_SKIN_CHANGED.invoker()).onDestroyedSkinChanged(player, NonApiInternals.snapshotDestroyedSkin((PlayerEntity)player));
        }
    }

    public static void firePregnancyChanged(ServerPlayerEntity player) {
        if (player != null) {
            ((NonEvents.PregnancyChanged)NonEvents.PREGNANCY_CHANGED.invoker()).onPregnancyChanged(player, NonApiInternals.snapshotPregnancy((PlayerEntity)player));
        }
    }

    public static void fireGenderChanged(ServerPlayerEntity player, int mask, NonChangeSource source) {
        if (player != null) {
            ((NonEvents.PlayerGenderChanged)NonEvents.PLAYER_GENDER_CHANGED.invoker()).onPlayerGenderChanged(player, NonGender.fromMask(mask), source);
        }
    }

    private static int addLiquidWithComposition(LiquidHolder holder, @Nullable Identifier sourceEntityType, int amount) {
        int stored = Math.max(0, holder.getLiquidStored());
        int capacity = Math.max(0, holder.getLiquidCapacity());
        int room = Math.max(0, capacity - stored);
        int added = Math.min(room, amount);
        if (added <= 0) {
            return 0;
        }
        if (stored <= 0) {
            if (sourceEntityType != null) {
                holder.setLiquidCompositionEntity(sourceEntityType);
            } else {
                holder.setLiquidCompositionMixed();
            }
        } else if (holder.getLiquidComposition() == LiquidHolder.LiquidComposition.ENTITY) {
            Identifier existingType = holder.getLiquidEntityTypeId();
            if (sourceEntityType == null || !sourceEntityType.equals((Object)existingType)) {
                holder.setLiquidCompositionMixed();
            }
        } else if (holder.getLiquidComposition() != LiquidHolder.LiquidComposition.MIXED) {
            holder.setLiquidCompositionMixed();
        }
        holder.setLiquidStored(stored + added);
        return added;
    }

    private static boolean changeMess(ServerPlayerEntity player, NonMessType type, int delta) {
        if (!NeedsOfNature.getConfig().isMessSystemEnabled()) {
            return false;
        }
        if (player == null || type == null || delta == 0 || !(player instanceof MessHolder)) {
            return false;
        }
        MessHolder holder = (MessHolder)player;
        int before = switch (type) {
            default -> throw new MatchException(null, null);
            case NonMessType.V -> holder.getVMess();
            case NonMessType.A -> holder.getAMess();
            case NonMessType.M -> holder.getMMess();
        };
        int after = NonMessSystem.clampMess(before + delta);
        if (after == before) {
            return false;
        }
        switch (type) {
            case V: {
                holder.setVMess(after);
                break;
            }
            case A: {
                holder.setAMess(after);
                break;
            }
            case M: {
                holder.setMMess(after);
            }
        }
        NonMessSystem.broadcastMessState(player);
        return true;
    }

    private static boolean changeDestroyedSkinDamage(ServerPlayerEntity player, int delta) {
        if (!NeedsOfNature.getConfig().isDestroyedSkinSystemEnabled()) {
            return false;
        }
        if (player == null || delta == 0 || !(player instanceof DestroyedSkinHolder)) {
            return false;
        }
        DestroyedSkinHolder holder = (DestroyedSkinHolder)player;
        int before = Math.max(0, Math.min(10, holder.getDestroyedSkinDamage()));
        int after = Math.max(0, Math.min(10, before + delta));
        if (after == before) {
            return false;
        }
        holder.setDestroyedSkinDamage(after);
        NonDestroyedSkinSystem.setDestroyedSkinStage(player, NonApiInternals.destroyedSkinStageForDamage(after));
        return true;
    }

    private static int destroyedSkinStageForDamage(int damage) {
        if (damage >= 10) {
            return 4;
        }
        if (damage >= 7) {
            return 3;
        }
        if (damage >= 4) {
            return 2;
        }
        if (damage >= 1) {
            return 1;
        }
        return 0;
    }

    private static NonLiquidComposition toApiLiquidComposition(LiquidHolder.LiquidComposition composition) {
        return switch (composition) {
            default -> throw new MatchException(null, null);
            case LiquidHolder.LiquidComposition.ENTITY -> NonLiquidComposition.ENTITY;
            case LiquidHolder.LiquidComposition.MIXED -> NonLiquidComposition.MIXED;
            case LiquidHolder.LiquidComposition.EMPTY -> NonLiquidComposition.EMPTY;
        };
    }
}

