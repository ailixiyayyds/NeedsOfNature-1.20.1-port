/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.world.ClientWorld
 */
package com.nonid.client;

import com.nonid.NeedsOfNature;
import com.nonid.client.NonDebugSpinMode;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;

public final class NonSpinModelLock {
    private static final Set<UUID> LOCKED = new HashSet<UUID>();
    private static boolean initialized = false;
    private static boolean available = false;
    private static Method emfEntityOfMethod;
    private static Method lockMethod;
    private static Method unlockMethod;

    private NonSpinModelLock() {
    }

    public static void clientTick(MinecraftClient client) {
        if (client == null) {
            return;
        }
        ClientWorld world = client.world;
        if (world == null) {
            LOCKED.clear();
            return;
        }
        if (!NonDebugSpinMode.isEnabled()) {
            NonSpinModelLock.releaseAll(world);
            return;
        }
        if (!NonSpinModelLock.ensureApi()) {
            return;
        }
        HashSet<UUID> seen = new HashSet<UUID>();
        for (Entity entity : world.getEntities()) {
            if (!(entity instanceof LivingEntity)) continue;
            LivingEntity living = (LivingEntity)entity;
            UUID uuid2 = living.getUuid();
            seen.add(uuid2);
            if (!LOCKED.add(uuid2)) continue;
            NonSpinModelLock.lockEntity(living);
        }
        LOCKED.removeIf(uuid -> !seen.contains(uuid));
    }

    private static void releaseAll(ClientWorld world) {
        if (LOCKED.isEmpty()) {
            return;
        }
        if (!NonSpinModelLock.ensureApi()) {
            LOCKED.clear();
            return;
        }
        for (UUID uuid : Set.copyOf(LOCKED)) {
            Entity entity = world.getEntity(uuid);
            if (!(entity instanceof LivingEntity)) continue;
            LivingEntity living = (LivingEntity)entity;
            NonSpinModelLock.unlockEntity(living);
        }
        LOCKED.clear();
    }

    private static void lockEntity(LivingEntity entity) {
        NonSpinModelLock.invokeLockMethod(entity, lockMethod);
    }

    private static void unlockEntity(LivingEntity entity) {
        NonSpinModelLock.invokeLockMethod(entity, unlockMethod);
    }

    private static void invokeLockMethod(LivingEntity entity, Method method) {
        if (entity == null || method == null || emfEntityOfMethod == null) {
            return;
        }
        try {
            Object emfEntity = emfEntityOfMethod.invoke(null, entity);
            if (emfEntity != null) {
                method.invoke(null, emfEntity);
            }
        }
        catch (ReflectiveOperationException | RuntimeException e) {
            NeedsOfNature.LOGGER.debug("[NoN] Failed to invoke EMF spin lock integration; disabling it.", (Throwable)e);
            NonSpinModelLock.disableApi();
        }
    }

    private static boolean ensureApi() {
        if (initialized) {
            return available;
        }
        initialized = true;
        try {
            Class<?> apiClass = Class.forName("traben.entity_model_features.EMFAnimationApi");
            Class<?> emfEntityClass = Class.forName("traben.entity_model_features.utils.EMFEntity");
            emfEntityOfMethod = apiClass.getMethod("emfEntityOf", Entity.class);
            lockMethod = apiClass.getMethod("lockEntityToVanillaModel", emfEntityClass);
            unlockMethod = apiClass.getMethod("unlockEntityToVanillaModel", emfEntityClass);
            available = true;
        }
        catch (ClassNotFoundException | NoSuchMethodException e) {
            NonSpinModelLock.disableApi();
        }
        return available;
    }

    private static void disableApi() {
        available = false;
        emfEntityOfMethod = null;
        lockMethod = null;
        unlockMethod = null;
    }
}

