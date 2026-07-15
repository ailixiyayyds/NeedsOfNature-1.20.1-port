/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_310
 *  net.minecraft.class_638
 */
package com.nonid.client;

import com.nonid.NeedsOfNature;
import com.nonid.client.NonDebugSpinMode;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_310;
import net.minecraft.class_638;

public final class NonSpinModelLock {
    private static final Set<UUID> LOCKED = new HashSet<UUID>();
    private static boolean initialized = false;
    private static boolean available = false;
    private static Method emfEntityOfMethod;
    private static Method lockMethod;
    private static Method unlockMethod;

    private NonSpinModelLock() {
    }

    public static void clientTick(class_310 client) {
        if (client == null) {
            return;
        }
        class_638 world = client.field_1687;
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
        for (class_1297 entity : world.method_18112()) {
            if (!(entity instanceof class_1309)) continue;
            class_1309 living = (class_1309)entity;
            UUID uuid2 = living.method_5667();
            seen.add(uuid2);
            if (!LOCKED.add(uuid2)) continue;
            NonSpinModelLock.lockEntity(living);
        }
        LOCKED.removeIf(uuid -> !seen.contains(uuid));
    }

    private static void releaseAll(class_638 world) {
        if (LOCKED.isEmpty()) {
            return;
        }
        if (!NonSpinModelLock.ensureApi()) {
            LOCKED.clear();
            return;
        }
        for (UUID uuid : Set.copyOf(LOCKED)) {
            class_1297 entity = world.method_66347(uuid);
            if (!(entity instanceof class_1309)) continue;
            class_1309 living = (class_1309)entity;
            NonSpinModelLock.unlockEntity(living);
        }
        LOCKED.clear();
    }

    private static void lockEntity(class_1309 entity) {
        NonSpinModelLock.invokeLockMethod(entity, lockMethod);
    }

    private static void unlockEntity(class_1309 entity) {
        NonSpinModelLock.invokeLockMethod(entity, unlockMethod);
    }

    private static void invokeLockMethod(class_1309 entity, Method method) {
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
            emfEntityOfMethod = apiClass.getMethod("emfEntityOf", class_1297.class);
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

