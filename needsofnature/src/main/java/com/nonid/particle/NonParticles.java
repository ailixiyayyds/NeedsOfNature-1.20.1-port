/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
 *  net.minecraft.registry.Registry
 *  net.minecraft.particle.SimpleParticleType
 *  net.minecraft.util.Identifier
 *  net.minecraft.registry.Registries
 */
package com.nonid.particle;

import com.nonid.NeedsOfNature;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.registry.Registry;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;

public final class NonParticles {
    public static final DefaultParticleType LIQUID_PARTICLE = NonParticles.register("liquid_particle");
    public static final DefaultParticleType LIQUID_PARTICLE_FALLING = NonParticles.register("liquid_particle_falling");
    public static final DefaultParticleType LIQUID_PARTICLE_PUDDLE = NonParticles.register("liquid_particle_puddle");
    public static final DefaultParticleType LIQUID_PARTICLE_WATER = NonParticles.register("liquid_particle_water");
    public static final DefaultParticleType SMALLHEART = NonParticles.register("smallheart");
    public static final DefaultParticleType RIPPED_FABRIC = NonParticles.register("ripped_fabric");

    private NonParticles() {
    }

    public static void registerAll() {
    }

    private static DefaultParticleType register(String path) {
        return Registry.register(Registries.PARTICLE_TYPE, NeedsOfNature.id(path), FabricParticleTypes.simple());
    }
}

