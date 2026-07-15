/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
 *  net.minecraft.class_2378
 *  net.minecraft.class_2400
 *  net.minecraft.class_2960
 *  net.minecraft.class_7923
 */
package com.nonid.particle;

import com.nonid.NeedsOfNature;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.class_2378;
import net.minecraft.class_2400;
import net.minecraft.class_2960;
import net.minecraft.class_7923;

public final class NonParticles {
    public static final class_2400 LIQUID_PARTICLE = NonParticles.register("liquid_particle");
    public static final class_2400 LIQUID_PARTICLE_FALLING = NonParticles.register("liquid_particle_falling");
    public static final class_2400 LIQUID_PARTICLE_PUDDLE = NonParticles.register("liquid_particle_puddle");
    public static final class_2400 LIQUID_PARTICLE_WATER = NonParticles.register("liquid_particle_water");
    public static final class_2400 SMALLHEART = NonParticles.register("smallheart");
    public static final class_2400 RIPPED_FABRIC = NonParticles.register("ripped_fabric");

    private NonParticles() {
    }

    public static void registerAll() {
    }

    private static class_2400 register(String path) {
        return (class_2400)class_2378.method_10230((class_2378)class_7923.field_41180, (class_2960)NeedsOfNature.id(path), (Object)FabricParticleTypes.simple());
    }
}

