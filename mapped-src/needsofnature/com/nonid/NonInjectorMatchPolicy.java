/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.afwid.data.AfwAnimationDefinitions$Definition
 *  net.minecraft.entity.Entity
 *  net.minecraft.server.world.ServerWorld
 *  net.minecraft.server.network.ServerPlayerEntity
 */
package com.nonid;

import com.afwid.data.AfwAnimationDefinitions;
import com.nonid.NeedsOfNature;
import com.nonid.NonGenderSystem;
import com.nonid.data.NonLiquidRoles;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;

public final class NonInjectorMatchPolicy {
    private NonInjectorMatchPolicy() {
    }

    public static boolean allowsAutomaticNonMatch(ServerWorld world, AfwAnimationDefinitions.Definition definition, List<Entity> actors) {
        if (world == null || definition == null || actors == null || actors.isEmpty()) {
            return false;
        }
        if (!NonInjectorMatchPolicy.hasMaleOnlyPlayer(actors)) {
            return true;
        }
        NonLiquidRoles.LiquidRoles roles = NonLiquidRoles.getRoles(definition.id());
        if (roles == null || roles.injectorRoles().isEmpty()) {
            return true;
        }
        boolean hasV = roles.injectorRoles().containsValue((Object)NonLiquidRoles.InjectorRole.V);
        boolean hasA = roles.injectorRoles().containsValue((Object)NonLiquidRoles.InjectorRole.A);
        if (!hasV) {
            return true;
        }
        if (hasA) {
            return false;
        }
        return NeedsOfNature.getConfig().convertMaleOnlyVInjectionsToA();
    }

    private static boolean hasMaleOnlyPlayer(List<Entity> actors) {
        for (Entity actor : actors) {
            if (!(actor instanceof ServerPlayerEntity) || !NonGenderSystem.isOnlyMale(actor)) continue;
            return true;
        }
        return false;
    }
}

