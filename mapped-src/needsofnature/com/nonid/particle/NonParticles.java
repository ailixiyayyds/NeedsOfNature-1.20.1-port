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
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;

public final class NonParticles {
    public static final SimpleParticleType LIQUID_PARTICLE = NonParticles.register("liquid_particle");
    public static final SimpleParticleType LIQUID_PARTICLE_FALLING = NonParticles.register("liquid_particle_falling");
    public static final SimpleParticleType LIQUID_PARTICLE_PUDDLE = NonParticles.register("liquid_particle_puddle");
    public static final SimpleParticleType LIQUID_PARTICLE_WATER = NonParticles.register("liquid_particle_water");
    public static final SimpleParticleType SMALLHEART = NonParticles.register("smallheart");
    public static final SimpleParticleType RIPPED_FABRIC = NonParticles.register("ripped_fabric");

    private NonParticles() {
    }

    public static void registerAll() {
    }

    private static SimpleParticleType register(String path) {
        return (SimpleParticleType)Registry.register((Registry)Registries.PARTICLE_TYPE, (Identifier)NeedsOfNature.id(path), (Object)FabricParticleTypes.simple());
    }
}

