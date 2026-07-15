/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.loader.api.FabricLoader
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.gui.DrawContext
 *  net.minecraft.client.gui.screen.Screen
 */
package com.nonid.client;

import com.nonid.NeedsOfNature;
import com.nonid.NonConfig;
import com.nonid.client.NonDestroyedSkinClient;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;

public final class NonWildfireGenderSync {
    private static final String MOD_ID = "wildfire_gender";
    private static boolean initialized;
    private static boolean failed;
    private static boolean loggedFailure;
    private static Method getPlayerByIdMethod;
    private static Method updateGenderMethod;
    private static Method updateBustSizeMethod;
    private static Method updateBreastPhysicsMethod;
    private static Method updateBounceMultiplierMethod;
    private static Method updateFloppinessMethod;
    private static Method getBreastsMethod;
    private static Method updateXOffsetMethod;
    private static Method updateYOffsetMethod;
    private static Method updateZOffsetMethod;
    private static Method updateCleavageMethod;
    private static Method updateUniboobMethod;
    private static Method getBustSizeMethod;
    private static Method hasBreastPhysicsMethod;
    private static Method getBounceMultiplierMethod;
    private static Method getFloppinessMethod;
    private static Method getXOffsetMethod;
    private static Method getYOffsetMethod;
    private static Method getZOffsetMethod;
    private static Method getCleavageMethod;
    private static Method isUniboobMethod;
    private static Method getLeftBreastPhysicsMethod;
    private static Method getRightBreastPhysicsMethod;
    private static Method renderPlayerInFrameMethod;
    private static Method saveMethod;
    private static Field bustSizeField;
    private static Field breastPhysicsField;
    private static Field bounceMultiplierField;
    private static Field floppyMultiplierField;
    private static Field xOffsetField;
    private static Field yOffsetField;
    private static Field zOffsetField;
    private static Field cleavageField;
    private static Field uniboobField;
    private static Field physicsBreastSizeField;
    private static Field physicsPreBreastSizeField;
    private static Constructor<?> breastCustomizationScreenConstructor;
    private static Object wildfireMaleGender;
    private static Object wildfireFemaleGender;
    private static Object wildfireOtherGender;

    private NonWildfireGenderSync() {
    }

    public static boolean isAvailable() {
        return FabricLoader.getInstance().isModLoaded(MOD_ID);
    }

    public static boolean shouldLockWildfireGenderControls(NonConfig config) {
        return config != null && config.isSyncFemaleGenderModWithNonGender() && NonWildfireGenderSync.isAvailable();
    }

    public static boolean shouldLockWildfirePersonalizationControls(NonConfig config) {
        return config != null && config.isDestroyedSkinSystemEnabled() && config.isDestroyedSkinFemaleGenderOverridesEnabled() && NonWildfireGenderSync.isAvailable();
    }

    public static void syncFromNonConfig(NonConfig config) {
        if (config == null || !config.isSyncFemaleGenderModWithNonGender() || !NonWildfireGenderSync.isAvailable()) {
            return;
        }
        NonWildfireGenderSync.syncFromSelection(config, config.getPlayerGenderSelection());
    }

    public static void syncFromMask(NonConfig config, int mask) {
        if (config == null || !config.isSyncFemaleGenderModWithNonGender() || !NonWildfireGenderSync.isAvailable()) {
            return;
        }
        NonWildfireGenderSync.syncFromSelection(config, NonConfig.PlayerGenderSelection.fromMask(mask, config.getPlayerGenderSelection()));
    }

    public static void syncDestroyedSkinOverrides(NonConfig config) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) {
            return;
        }
        NonWildfireGenderSync.syncDestroyedSkinOverrides(config, NonDestroyedSkinClient.getStage(client.player.getUuid()));
    }

    public static void syncDestroyedSkinOverrides(NonConfig config, int destroyedStage) {
        if (!(config != null && config.isDestroyedSkinSystemEnabled() && config.isDestroyedSkinFemaleGenderOverridesEnabled() && NonWildfireGenderSync.isAvailable())) {
            return;
        }
        if (!NonWildfireGenderSync.init()) {
            return;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) {
            return;
        }
        NonConfig.FemaleGenderDestroyedProfile profile = destroyedStage >= 2 ? config.getFemaleGenderDestroyedHigh() : config.getFemaleGenderDestroyedLow();
        try {
            Object playerConfig = getPlayerByIdMethod.invoke(null, client.player.getUuid());
            if (playerConfig == null) {
                return;
            }
            NonWildfireGenderSync.applyProfile(playerConfig, profile, true);
        }
        catch (ReflectiveOperationException | RuntimeException e) {
            NonWildfireGenderSync.logFailure(e);
        }
    }

    public static boolean openBreastCustomizationScreen(Screen parent) {
        if (!NonWildfireGenderSync.isAvailable() || !NonWildfireGenderSync.init()) {
            return false;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null || breastCustomizationScreenConstructor == null) {
            return false;
        }
        try {
            Object screen = breastCustomizationScreenConstructor.newInstance(parent, client.player.getUuid());
            if (screen instanceof Screen) {
                Screen minecraftScreen = (Screen)screen;
                client.setScreen(minecraftScreen);
                return true;
            }
        }
        catch (ReflectiveOperationException | RuntimeException e) {
            NonWildfireGenderSync.logFailure(e);
        }
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean renderBreastPreview(Screen parent, DrawContext context, NonConfig.FemaleGenderDestroyedProfile profile, int centerX, int frameY, int mouseX, int mouseY) {
        if (parent == null || context == null || profile == null || !NonWildfireGenderSync.isAvailable() || !NonWildfireGenderSync.init()) {
            return false;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null || breastCustomizationScreenConstructor == null || renderPlayerInFrameMethod == null) {
            return false;
        }
        try {
            Object playerConfig = getPlayerByIdMethod.invoke(null, client.player.getUuid());
            if (playerConfig == null) {
                return false;
            }
            NonConfig.FemaleGenderDestroyedProfile previous = NonWildfireGenderSync.readProfile(playerConfig);
            BreastPhysicsSnapshot physicsSnapshot = NonWildfireGenderSync.snapshotBreastPhysics(playerConfig);
            try {
                NonWildfireGenderSync.applyProfile(playerConfig, profile, false);
                NonWildfireGenderSync.applyBreastPhysicsSize(playerConfig, profile.breastSize());
                Object screen = breastCustomizationScreenConstructor.newInstance(parent, client.player.getUuid());
                renderPlayerInFrameMethod.invoke(screen, context, centerX, frameY, mouseX, mouseY);
            }
            finally {
                NonWildfireGenderSync.applyProfile(playerConfig, previous, false);
                if (physicsSnapshot != null) {
                    physicsSnapshot.restore();
                }
            }
            return true;
        }
        catch (ReflectiveOperationException | RuntimeException e) {
            NonWildfireGenderSync.logFailure(e);
            return false;
        }
    }

    private static boolean applyProfile(Object playerConfig, NonConfig.FemaleGenderDestroyedProfile profile, boolean save) throws ReflectiveOperationException {
        if (!save) {
            NonWildfireGenderSync.applyProfileFields(playerConfig, profile);
            return true;
        }
        boolean changed = false;
        changed |= Boolean.TRUE.equals(updateBustSizeMethod.invoke(playerConfig, Float.valueOf(profile.breastSize())));
        changed |= Boolean.TRUE.equals(updateBreastPhysicsMethod.invoke(playerConfig, profile.breastPhysics()));
        changed |= Boolean.TRUE.equals(updateBounceMultiplierMethod.invoke(playerConfig, Float.valueOf(profile.intensity())));
        changed |= Boolean.TRUE.equals(updateFloppinessMethod.invoke(playerConfig, Float.valueOf(profile.momentum())));
        Object breasts = getBreastsMethod.invoke(playerConfig, new Object[0]);
        if (breasts != null) {
            changed |= Boolean.TRUE.equals(updateXOffsetMethod.invoke(breasts, Float.valueOf(profile.separation())));
            changed |= Boolean.TRUE.equals(updateYOffsetMethod.invoke(breasts, Float.valueOf(profile.height())));
            changed |= Boolean.TRUE.equals(updateZOffsetMethod.invoke(breasts, Float.valueOf(profile.depth())));
            changed |= Boolean.TRUE.equals(updateCleavageMethod.invoke(breasts, Float.valueOf(profile.rotation())));
            changed |= Boolean.TRUE.equals(updateUniboobMethod.invoke(breasts, !profile.dualPhysics()));
        }
        if (save && changed) {
            saveMethod.invoke(playerConfig, new Object[0]);
        }
        return changed;
    }

    private static void applyProfileFields(Object playerConfig, NonConfig.FemaleGenderDestroyedProfile profile) throws ReflectiveOperationException {
        bustSizeField.setFloat(playerConfig, profile.breastSize());
        breastPhysicsField.setBoolean(playerConfig, profile.breastPhysics());
        bounceMultiplierField.setFloat(playerConfig, profile.intensity());
        floppyMultiplierField.setFloat(playerConfig, profile.momentum());
        Object breasts = getBreastsMethod.invoke(playerConfig, new Object[0]);
        if (breasts != null) {
            xOffsetField.setFloat(breasts, profile.separation());
            yOffsetField.setFloat(breasts, profile.height());
            zOffsetField.setFloat(breasts, profile.depth());
            cleavageField.setFloat(breasts, profile.rotation());
            uniboobField.setBoolean(breasts, !profile.dualPhysics());
        }
    }

    private static NonConfig.FemaleGenderDestroyedProfile readProfile(Object playerConfig) throws ReflectiveOperationException {
        Object breasts = getBreastsMethod.invoke(playerConfig, new Object[0]);
        float rotation = 0.0f;
        float height = 0.0f;
        float depth = 0.0f;
        float separation = 0.0f;
        boolean dualPhysics = false;
        if (breasts != null) {
            separation = ((Number)getXOffsetMethod.invoke(breasts, new Object[0])).floatValue();
            height = ((Number)getYOffsetMethod.invoke(breasts, new Object[0])).floatValue();
            depth = ((Number)getZOffsetMethod.invoke(breasts, new Object[0])).floatValue();
            rotation = ((Number)getCleavageMethod.invoke(breasts, new Object[0])).floatValue();
            dualPhysics = !Boolean.TRUE.equals(isUniboobMethod.invoke(breasts, new Object[0]));
        }
        return new NonConfig.FemaleGenderDestroyedProfile(((Number)getBustSizeMethod.invoke(playerConfig, new Object[0])).floatValue(), separation, height, depth, rotation, Boolean.TRUE.equals(hasBreastPhysicsMethod.invoke(playerConfig, new Object[0])), dualPhysics, ((Number)getBounceMultiplierMethod.invoke(playerConfig, new Object[0])).floatValue(), ((Number)getFloppinessMethod.invoke(playerConfig, new Object[0])).floatValue());
    }

    private static BreastPhysicsSnapshot snapshotBreastPhysics(Object playerConfig) throws ReflectiveOperationException {
        Object left = getLeftBreastPhysicsMethod.invoke(playerConfig, new Object[0]);
        Object right = getRightBreastPhysicsMethod.invoke(playerConfig, new Object[0]);
        if (left == null || right == null) {
            return null;
        }
        return new BreastPhysicsSnapshot(left, right, physicsBreastSizeField.getFloat(left), physicsPreBreastSizeField.getFloat(left), physicsBreastSizeField.getFloat(right), physicsPreBreastSizeField.getFloat(right));
    }

    private static void applyBreastPhysicsSize(Object playerConfig, float breastSize) throws ReflectiveOperationException {
        NonWildfireGenderSync.setBreastPhysicsSize(getLeftBreastPhysicsMethod.invoke(playerConfig, new Object[0]), breastSize);
        NonWildfireGenderSync.setBreastPhysicsSize(getRightBreastPhysicsMethod.invoke(playerConfig, new Object[0]), breastSize);
    }

    private static void setBreastPhysicsSize(Object breastPhysics, float breastSize) throws IllegalAccessException {
        if (breastPhysics == null) {
            return;
        }
        physicsBreastSizeField.setFloat(breastPhysics, breastSize);
        physicsPreBreastSizeField.setFloat(breastPhysics, breastSize);
    }

    private static void syncFromSelection(NonConfig config, NonConfig.PlayerGenderSelection selection) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) {
            return;
        }
        if (!NonWildfireGenderSync.init()) {
            return;
        }
        try {
            UUID playerUuid = client.player.getUuid();
            Object playerConfig = getPlayerByIdMethod.invoke(null, playerUuid);
            if (playerConfig == null) {
                return;
            }
            Object targetGender = NonWildfireGenderSync.toWildfireGender(selection);
            Object changed = updateGenderMethod.invoke(playerConfig, targetGender);
            if (Boolean.TRUE.equals(changed)) {
                saveMethod.invoke(playerConfig, new Object[0]);
            }
        }
        catch (ReflectiveOperationException | RuntimeException e) {
            NonWildfireGenderSync.logFailure(e);
        }
    }

    private static Object toWildfireGender(NonConfig.PlayerGenderSelection selection) {
        if (selection == NonConfig.PlayerGenderSelection.MALE) {
            return wildfireMaleGender;
        }
        if (selection == NonConfig.PlayerGenderSelection.BOTH) {
            return wildfireOtherGender;
        }
        return wildfireFemaleGender;
    }

    private static boolean init() {
        if (initialized) {
            return !failed;
        }
        initialized = true;
        if (!NonWildfireGenderSync.isAvailable()) {
            failed = true;
            return false;
        }
        try {
            Class<?> apiClass = Class.forName("com.wildfire.api.WildfireAPI");
            Class<?> playerConfigClass = Class.forName("com.wildfire.main.entitydata.PlayerConfig");
            Class<?> entityConfigClass = Class.forName("com.wildfire.main.entitydata.EntityConfig");
            Class<?> breastsClass = Class.forName("com.wildfire.main.entitydata.Breasts");
            Class<?> genderClass = Class.forName("com.wildfire.main.config.enums.Gender");
            Class<?> breastPhysicsClass = Class.forName("com.wildfire.physics.BreastPhysics");
            Class<?> breastCustomizationScreenClass = Class.forName("com.wildfire.gui.screen.WildfireBreastCustomizationScreen");
            getPlayerByIdMethod = apiClass.getMethod("getPlayerById", UUID.class);
            updateGenderMethod = playerConfigClass.getMethod("updateGender", genderClass);
            updateBustSizeMethod = playerConfigClass.getMethod("updateBustSize", Float.TYPE);
            updateBreastPhysicsMethod = playerConfigClass.getMethod("updateBreastPhysics", Boolean.TYPE);
            updateBounceMultiplierMethod = playerConfigClass.getMethod("updateBounceMultiplier", Float.TYPE);
            updateFloppinessMethod = playerConfigClass.getMethod("updateFloppiness", Float.TYPE);
            getBreastsMethod = playerConfigClass.getMethod("getBreasts", new Class[0]);
            getBustSizeMethod = playerConfigClass.getMethod("getBustSize", new Class[0]);
            hasBreastPhysicsMethod = playerConfigClass.getMethod("hasBreastPhysics", new Class[0]);
            getBounceMultiplierMethod = playerConfigClass.getMethod("getBounceMultiplier", new Class[0]);
            getFloppinessMethod = playerConfigClass.getMethod("getFloppiness", new Class[0]);
            updateXOffsetMethod = breastsClass.getMethod("updateXOffset", Float.TYPE);
            updateYOffsetMethod = breastsClass.getMethod("updateYOffset", Float.TYPE);
            updateZOffsetMethod = breastsClass.getMethod("updateZOffset", Float.TYPE);
            updateCleavageMethod = breastsClass.getMethod("updateCleavage", Float.TYPE);
            updateUniboobMethod = breastsClass.getMethod("updateUniboob", Boolean.TYPE);
            getXOffsetMethod = breastsClass.getMethod("getXOffset", new Class[0]);
            getYOffsetMethod = breastsClass.getMethod("getYOffset", new Class[0]);
            getZOffsetMethod = breastsClass.getMethod("getZOffset", new Class[0]);
            getCleavageMethod = breastsClass.getMethod("getCleavage", new Class[0]);
            isUniboobMethod = breastsClass.getMethod("isUniboob", new Class[0]);
            getLeftBreastPhysicsMethod = entityConfigClass.getMethod("getLeftBreastPhysics", new Class[0]);
            getRightBreastPhysicsMethod = entityConfigClass.getMethod("getRightBreastPhysics", new Class[0]);
            saveMethod = playerConfigClass.getMethod("save", new Class[0]);
            breastCustomizationScreenConstructor = breastCustomizationScreenClass.getConstructor(Screen.class, UUID.class);
            Class<?> baseWildfireScreenClass = Class.forName("com.wildfire.gui.screen.BaseWildfireScreen");
            renderPlayerInFrameMethod = baseWildfireScreenClass.getDeclaredMethod("renderPlayerInFrame", DrawContext.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
            renderPlayerInFrameMethod.setAccessible(true);
            bustSizeField = NonWildfireGenderSync.accessibleField(entityConfigClass, "pBustSize");
            breastPhysicsField = NonWildfireGenderSync.accessibleField(entityConfigClass, "breastPhysics");
            bounceMultiplierField = NonWildfireGenderSync.accessibleField(entityConfigClass, "bounceMultiplier");
            floppyMultiplierField = NonWildfireGenderSync.accessibleField(entityConfigClass, "floppyMultiplier");
            xOffsetField = NonWildfireGenderSync.accessibleField(breastsClass, "xOffset");
            yOffsetField = NonWildfireGenderSync.accessibleField(breastsClass, "yOffset");
            zOffsetField = NonWildfireGenderSync.accessibleField(breastsClass, "zOffset");
            cleavageField = NonWildfireGenderSync.accessibleField(breastsClass, "cleavage");
            uniboobField = NonWildfireGenderSync.accessibleField(breastsClass, "uniboob");
            physicsBreastSizeField = NonWildfireGenderSync.accessibleField(breastPhysicsClass, "breastSize");
            physicsPreBreastSizeField = NonWildfireGenderSync.accessibleField(breastPhysicsClass, "preBreastSize");
            wildfireMaleGender = NonWildfireGenderSync.enumConstant(genderClass, "MALE");
            wildfireFemaleGender = NonWildfireGenderSync.enumConstant(genderClass, "FEMALE");
            wildfireOtherGender = NonWildfireGenderSync.enumConstant(genderClass, "OTHER");
            return true;
        }
        catch (ReflectiveOperationException | RuntimeException e) {
            failed = true;
            NonWildfireGenderSync.logFailure(e);
            return false;
        }
    }

    private static Object enumConstant(Class<?> enumClass, String name) {
        return Enum.valueOf(enumClass.asSubclass(Enum.class), name);
    }

    private static Field accessibleField(Class<?> owner, String name) throws NoSuchFieldException {
        Field field = owner.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    private static void logFailure(Exception e) {
        if (loggedFailure) {
            return;
        }
        loggedFailure = true;
        NeedsOfNature.LOGGER.warn("[NoN] Failed to integrate with Female Gender Mod.", (Throwable)e);
    }

    private static final class BreastPhysicsSnapshot {
        private final Object left;
        private final Object right;
        private final float leftSize;
        private final float leftPreSize;
        private final float rightSize;
        private final float rightPreSize;

        private BreastPhysicsSnapshot(Object left, Object right, float leftSize, float leftPreSize, float rightSize, float rightPreSize) {
            this.left = left;
            this.right = right;
            this.leftSize = leftSize;
            this.leftPreSize = leftPreSize;
            this.rightSize = rightSize;
            this.rightPreSize = rightPreSize;
        }

        private void restore() throws IllegalAccessException {
            physicsBreastSizeField.setFloat(this.left, this.leftSize);
            physicsPreBreastSizeField.setFloat(this.left, this.leftPreSize);
            physicsBreastSizeField.setFloat(this.right, this.rightSize);
            physicsPreBreastSizeField.setFloat(this.right, this.rightPreSize);
        }
    }
}

