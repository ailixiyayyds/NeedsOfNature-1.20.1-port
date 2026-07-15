/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.afwid.data.AfwAnimationDefinitions$Definition
 *  net.minecraft.class_1297
 *  net.minecraft.class_3218
 *  net.minecraft.class_3222
 */
package com.nonid;

import com.afwid.data.AfwAnimationDefinitions;
import com.nonid.NeedsOfNature;
import com.nonid.NonGenderSystem;
import com.nonid.data.NonLiquidRoles;
import java.util.List;
import net.minecraft.class_1297;
import net.minecraft.class_3218;
import net.minecraft.class_3222;

public final class NonInjectorMatchPolicy {
    private NonInjectorMatchPolicy() {
    }

    public static boolean allowsAutomaticNonMatch(class_3218 world, AfwAnimationDefinitions.Definition definition, List<class_1297> actors) {
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

    private static boolean hasMaleOnlyPlayer(List<class_1297> actors) {
        for (class_1297 actor : actors) {
            if (!(actor instanceof class_3222) || !NonGenderSystem.isOnlyMale(actor)) continue;
            return true;
        }
        return false;
    }
}

