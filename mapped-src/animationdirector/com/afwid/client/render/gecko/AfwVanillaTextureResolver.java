/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.resource.v1.ResourceLoader
 *  net.minecraft.client.texture.NativeImage
 *  net.minecraft.client.texture.NativeImageBackedTexture
 *  net.minecraft.client.texture.AbstractTexture
 *  net.minecraft.util.Identifier
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.resource.ResourceType
 *  net.minecraft.resource.Resource
 *  net.minecraft.resource.ResourceManager
 *  net.minecraft.resource.ResourceReloader
 *  net.minecraft.resource.SynchronousResourceReloader
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
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.resource.SynchronousResourceReloader;

public final class AfwVanillaTextureResolver {
    private static final Map<Identifier, Identifier> REDIRECT_CACHE = new ConcurrentHashMap<Identifier, Identifier>();
    private static final List<Identifier> UPLOADED_TEXTURES = new ArrayList<Identifier>();
    private static final Identifier RELOAD_LISTENER_ID = Identifier.of((String)"animationframework", (String)"vanilla_textures");

    private AfwVanillaTextureResolver() {
    }

    public static void registerReloadListener() {
        ResourceLoader.get((ResourceType)ResourceType.CLIENT_RESOURCES).registerReloader(RELOAD_LISTENER_ID, (ResourceReloader)((SynchronousResourceReloader)manager -> AfwVanillaTextureResolver.clearCache()));
    }

    public static Identifier resolveTexture(Identifier textureId) {
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
    private static Identifier copyVanillaTexture(Identifier textureId) {
        Identifier class_29602;
        block15: {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null) {
                return textureId;
            }
            ResourceManager resourceManager = client.getResourceManager();
            List resources = resourceManager.getAllResources(textureId);
            if (resources.isEmpty()) {
                return textureId;
            }
            Resource vanillaResource = null;
            for (Resource res : resources) {
                String packId = res.getPackId();
                if (!"vanilla".equals(packId) && !"minecraft".equals(packId) && !"default".equalsIgnoreCase(packId)) continue;
                vanillaResource = res;
                break;
            }
            if (vanillaResource == null) {
                vanillaResource = (Resource)resources.getLast();
            }
            InputStream stream = vanillaResource.getInputStream();
            try {
                NativeImage image = NativeImage.read((InputStream)stream);
                Identifier redirected = Identifier.of((String)"animationframework", (String)("vanilla_textures/" + textureId.getNamespace() + "/" + textureId.getPath()));
                client.getTextureManager().registerTexture(redirected, (AbstractTexture)new NativeImageBackedTexture(() -> "afw_vanilla_texture", image));
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
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            List<Identifier> list = UPLOADED_TEXTURES;
            synchronized (list) {
                for (Identifier id : UPLOADED_TEXTURES) {
                    client.getTextureManager().destroyTexture(id);
                }
                UPLOADED_TEXTURES.clear();
            }
        }
    }
}

