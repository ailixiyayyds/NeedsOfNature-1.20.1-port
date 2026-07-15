/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.resource.v1.ResourceLoader
 *  net.minecraft.class_1011
 *  net.minecraft.class_1043
 *  net.minecraft.class_1044
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_3264
 *  net.minecraft.class_3298
 *  net.minecraft.class_3300
 *  net.minecraft.class_3302
 *  net.minecraft.class_4013
 */
package com.afwid.client.render.gecko;

import com.afwid.AnimationFramework;
import com.afwid.client.config.AfwClientConfig;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_1044;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3264;
import net.minecraft.class_3298;
import net.minecraft.class_3300;
import net.minecraft.class_3302;
import net.minecraft.class_4013;

public final class AfwVanillaTextureResolver {
    private static final Map<class_2960, class_2960> REDIRECT_CACHE = new ConcurrentHashMap<class_2960, class_2960>();
    private static final List<class_2960> UPLOADED_TEXTURES = new ArrayList<class_2960>();
    private static final class_2960 RELOAD_LISTENER_ID = class_2960.method_60655((String)"animationframework", (String)"vanilla_textures");

    private AfwVanillaTextureResolver() {
    }

    public static void registerReloadListener() {
        ResourceLoader.get((class_3264)class_3264.field_14188).registerReloader(RELOAD_LISTENER_ID, (class_3302)((class_4013)manager -> AfwVanillaTextureResolver.clearCache()));
    }

    public static class_2960 resolveTexture(class_2960 textureId) {
        if (textureId == null) {
            return null;
        }
        if (!AfwClientConfig.get().forceVanillaEntityTextures()) {
            return textureId;
        }
        return REDIRECT_CACHE.computeIfAbsent(textureId, AfwVanillaTextureResolver::copyVanillaTexture);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static class_2960 copyVanillaTexture(class_2960 textureId) {
        class_2960 class_29602;
        block15: {
            class_310 client = class_310.method_1551();
            if (client == null) {
                return textureId;
            }
            class_3300 resourceManager = client.method_1478();
            List resources = resourceManager.method_14489(textureId);
            if (resources.isEmpty()) {
                return textureId;
            }
            class_3298 vanillaResource = null;
            for (class_3298 res : resources) {
                String packId = res.method_14480();
                if (!"vanilla".equals(packId) && !"minecraft".equals(packId) && !"default".equalsIgnoreCase(packId)) continue;
                vanillaResource = res;
                break;
            }
            if (vanillaResource == null) {
                vanillaResource = (class_3298)resources.getLast();
            }
            InputStream stream = vanillaResource.method_14482();
            try {
                class_1011 image = class_1011.method_4309((InputStream)stream);
                class_2960 redirected = class_2960.method_60655((String)"animationframework", (String)("vanilla_textures/" + textureId.method_12836() + "/" + textureId.method_12832()));
                client.method_1531().method_4616(redirected, (class_1044)new class_1043(() -> "afw_vanilla_texture", image));
                class_29602 = UPLOADED_TEXTURES;
                synchronized (class_29602) {
                    UPLOADED_TEXTURES.add(redirected);
                }
                class_29602 = redirected;
                if (stream == null) break block15;
            }
            catch (Throwable throwable) {
                try {
                    if (stream != null) {
                        try {
                            stream.close();
                        }
                        catch (Throwable throwable2) {
                            throwable.addSuppressed(throwable2);
                        }
                    }
                    throw throwable;
                }
                catch (IOException | RuntimeException e) {
                    AnimationFramework.LOGGER.warn("[AFW] Failed to pull vanilla texture {}, using active resource stack", (Object)textureId, (Object)e);
                    return textureId;
                }
            }
            stream.close();
        }
        return class_29602;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void clearCache() {
        REDIRECT_CACHE.clear();
        class_310 client = class_310.method_1551();
        if (client != null) {
            List<class_2960> list = UPLOADED_TEXTURES;
            synchronized (list) {
                for (class_2960 id : UPLOADED_TEXTURES) {
                    client.method_1531().method_4615(id);
                }
                UPLOADED_TEXTURES.clear();
            }
        }
    }
}

