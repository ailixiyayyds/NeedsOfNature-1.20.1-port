/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  net.fabricmc.loader.api.FabricLoader
 */
package com.afwid.client.config;

import com.afwid.AfwDebugChatCategory;
import com.afwid.AfwDebugChatMode;
import com.afwid.AnimationFramework;
import com.afwid.api.AfwDamageBehavior;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import net.fabricmc.loader.api.FabricLoader;

public final class AfwClientConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("animationframework.json");
    private static volatile AfwClientConfig INSTANCE = AfwClientConfig.loadFromDisk();
    private boolean forceVanillaEntityTextures = false;
    private AfwDebugChatMode debugChatMode = AfwDebugChatMode.SETUP_ERRORS;
    private AfwDamageBehavior debugDamageBehavior = AfwDamageBehavior.STOP_ON_DAMAGE;
    private boolean debugIgnoreAttackers = false;
    private boolean anchorAtLastSelected = false;
    private int blockSearchRadius = 3;
    private boolean autoSwitchThirdPersonOnAnimationStart = true;

    private AfwClientConfig() {
    }

    public static AfwClientConfig get() {
        return INSTANCE;
    }

    public static void reload() {
        INSTANCE = AfwClientConfig.loadFromDisk();
    }

    public boolean forceVanillaEntityTextures() {
        return this.forceVanillaEntityTextures;
    }

    public void setForceVanillaEntityTextures(boolean forceVanillaEntityTextures) {
        this.forceVanillaEntityTextures = forceVanillaEntityTextures;
    }

    public AfwDebugChatMode debugChatMode() {
        return this.debugChatMode == null ? AfwDebugChatMode.SETUP_ERRORS : this.debugChatMode;
    }

    public void setDebugChatMode(AfwDebugChatMode mode) {
        this.debugChatMode = mode == null ? AfwDebugChatMode.SETUP_ERRORS : mode;
    }

    public boolean allowsDebugChat(AfwDebugChatCategory category) {
        return this.debugChatMode().allows(category);
    }

    public AfwDamageBehavior debugDamageBehavior() {
        return this.debugDamageBehavior == null ? AfwDamageBehavior.STOP_ON_DAMAGE : this.debugDamageBehavior;
    }

    public void setDebugDamageBehavior(AfwDamageBehavior debugDamageBehavior) {
        this.debugDamageBehavior = debugDamageBehavior == null ? AfwDamageBehavior.STOP_ON_DAMAGE : debugDamageBehavior;
    }

    public boolean debugIgnoreAttackers() {
        return this.debugIgnoreAttackers;
    }

    public void setDebugIgnoreAttackers(boolean debugIgnoreAttackers) {
        this.debugIgnoreAttackers = debugIgnoreAttackers;
    }

    public boolean anchorAtLastSelected() {
        return this.anchorAtLastSelected;
    }

    public void setAnchorAtLastSelected(boolean anchorAtLastSelected) {
        this.anchorAtLastSelected = anchorAtLastSelected;
    }

    public int blockSearchRadius() {
        return Math.max(1, this.blockSearchRadius);
    }

    public void setBlockSearchRadius(int blockSearchRadius) {
        this.blockSearchRadius = Math.max(1, blockSearchRadius);
    }

    public boolean autoSwitchThirdPersonOnAnimationStart() {
        return this.autoSwitchThirdPersonOnAnimationStart;
    }

    public void setAutoSwitchThirdPersonOnAnimationStart(boolean autoSwitchThirdPersonOnAnimationStart) {
        this.autoSwitchThirdPersonOnAnimationStart = autoSwitchThirdPersonOnAnimationStart;
    }

    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent(), new FileAttribute[0]);
            try (BufferedWriter writer = Files.newBufferedWriter(CONFIG_PATH, new OpenOption[0]);){
                GSON.toJson((Object)this, (Appendable)writer);
            }
        }
        catch (IOException e) {
            AnimationFramework.LOGGER.warn("[AFW] Failed to save client config {}", (Object)CONFIG_PATH, (Object)e);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static AfwClientConfig loadFromDisk() {
        if (Files.exists(CONFIG_PATH, new LinkOption[0])) {
            try (BufferedReader reader = Files.newBufferedReader(CONFIG_PATH);){
                AfwClientConfig loaded = (AfwClientConfig)GSON.fromJson((Reader)reader, AfwClientConfig.class);
                if (loaded != null) {
                    if (loaded.debugDamageBehavior == null) {
                        loaded.debugDamageBehavior = AfwDamageBehavior.STOP_ON_DAMAGE;
                    }
                    if (loaded.debugChatMode == null) {
                        loaded.debugChatMode = AfwDebugChatMode.SETUP_ERRORS;
                    }
                    if (loaded.blockSearchRadius < 1) {
                        loaded.blockSearchRadius = 3;
                    }
                    AfwClientConfig afwClientConfig = loaded;
                    return afwClientConfig;
                }
            }
            catch (IOException | RuntimeException e) {
                AnimationFramework.LOGGER.warn("[AFW] Failed to read client config {}, using defaults", (Object)CONFIG_PATH, (Object)e);
            }
        }
        AfwClientConfig fresh = new AfwClientConfig();
        fresh.save();
        return fresh;
    }
}

