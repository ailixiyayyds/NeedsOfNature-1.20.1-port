/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.event.Event
 *  net.fabricmc.fabric.api.event.EventFactory
 *  net.minecraft.util.Identifier
 *  net.minecraft.server.network.ServerPlayerEntity
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid.api;

import com.nonid.api.NonChangeSource;
import com.nonid.api.NonDestroyedSkinSnapshot;
import com.nonid.api.NonGender;
import com.nonid.api.NonLiquidTankSnapshot;
import com.nonid.api.NonMessSnapshot;
import com.nonid.api.NonPregnancySnapshot;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.Identifier;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

public final class NonEvents {
    public static final Event<PlayerGenderChanged> PLAYER_GENDER_CHANGED = EventFactory.createArrayBacked(PlayerGenderChanged.class, callbacks -> (player, gender, source) -> {
        for (PlayerGenderChanged callback : callbacks) {
            callback.onPlayerGenderChanged(player, gender, source);
        }
    });
    public static final Event<LiquidTankChanged> LIQUID_TANK_CHANGED = EventFactory.createArrayBacked(LiquidTankChanged.class, callbacks -> (player, snapshot) -> {
        for (LiquidTankChanged callback : callbacks) {
            callback.onLiquidTankChanged(player, snapshot);
        }
    });
    public static final Event<MessChanged> MESS_CHANGED = EventFactory.createArrayBacked(MessChanged.class, callbacks -> (player, snapshot) -> {
        for (MessChanged callback : callbacks) {
            callback.onMessChanged(player, snapshot);
        }
    });
    public static final Event<DestroyedSkinChanged> DESTROYED_SKIN_CHANGED = EventFactory.createArrayBacked(DestroyedSkinChanged.class, callbacks -> (player, snapshot) -> {
        for (DestroyedSkinChanged callback : callbacks) {
            callback.onDestroyedSkinChanged(player, snapshot);
        }
    });
    public static final Event<PregnancyChanged> PREGNANCY_CHANGED = EventFactory.createArrayBacked(PregnancyChanged.class, callbacks -> (player, snapshot) -> {
        for (PregnancyChanged callback : callbacks) {
            callback.onPregnancyChanged(player, snapshot);
        }
    });
    public static final Event<ModifyLiquidGain> MODIFY_LIQUID_GAIN = EventFactory.createArrayBacked(ModifyLiquidGain.class, callbacks -> (player, sourceEntityType, amountMl) -> {
        int result = Math.max(0, amountMl);
        for (ModifyLiquidGain callback : callbacks) {
            result = Math.max(0, callback.modifyLiquidGain(player, sourceEntityType, result));
        }
        return result;
    });
    public static final Event<AllowPregnancy> ALLOW_PREGNANCY = EventFactory.createArrayBacked(AllowPregnancy.class, callbacks -> (player, entityTypeId) -> {
        for (AllowPregnancy callback : callbacks) {
            if (callback.allowPregnancy(player, entityTypeId)) continue;
            return false;
        }
        return true;
    });

    private NonEvents() {
    }

    @FunctionalInterface
    public static interface AllowPregnancy {
        public boolean allowPregnancy(ServerPlayerEntity var1, Identifier var2);
    }

    @FunctionalInterface
    public static interface ModifyLiquidGain {
        public int modifyLiquidGain(ServerPlayerEntity var1, @Nullable Identifier var2, int var3);
    }

    @FunctionalInterface
    public static interface PregnancyChanged {
        public void onPregnancyChanged(ServerPlayerEntity var1, NonPregnancySnapshot var2);
    }

    @FunctionalInterface
    public static interface DestroyedSkinChanged {
        public void onDestroyedSkinChanged(ServerPlayerEntity var1, NonDestroyedSkinSnapshot var2);
    }

    @FunctionalInterface
    public static interface MessChanged {
        public void onMessChanged(ServerPlayerEntity var1, NonMessSnapshot var2);
    }

    @FunctionalInterface
    public static interface LiquidTankChanged {
        public void onLiquidTankChanged(ServerPlayerEntity var1, NonLiquidTankSnapshot var2);
    }

    @FunctionalInterface
    public static interface PlayerGenderChanged {
        public void onPlayerGenderChanged(ServerPlayerEntity var1, NonGender var2, NonChangeSource var3);
    }
}

