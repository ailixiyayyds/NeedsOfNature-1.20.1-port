/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  net.fabricmc.fabric.api.resource.v1.ResourceLoader
 *  net.fabricmc.loader.api.FabricLoader
 *  net.fabricmc.loader.api.ModContainer
 *  net.minecraft.util.Identifier
 *  net.minecraft.resource.ResourceType
 *  net.minecraft.resource.ResourceManager
 *  net.minecraft.resource.ResourceReloader
 *  net.minecraft.resource.SynchronousResourceReloader
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.nonid.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nonid.NeedsOfNature;
import com.nonid.pack.NonExternalPackProvider;
import com.nonid.pack.NonPackRootResolver;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.util.Identifier;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.resource.SynchronousResourceReloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NonPregnancyCues {
    private static final Logger LOGGER = LoggerFactory.getLogger((String)"needsofnature");
    private static volatile Map<Identifier, Integer> CUE_TICK_BY_CLIP = Map.of();
    private static final String BIRTH_EFFECT = "birth";
    private static final String ANIMATION_FILE_SUFFIX = ".animation.json";

    private NonPregnancyCues() {
    }

    public static void registerReloadListener() {
        ResourceLoader.get((ResourceType)ResourceType.SERVER_DATA).registerReloader(Reloader.RELOADER_ID, (ResourceReloader)new Reloader());
    }

    public static Integer getCueTick(Identifier clipId) {
        if (clipId == null) {
            return null;
        }
        return CUE_TICK_BY_CLIP.get(clipId);
    }

    private static void reload() {
        HashMap<Identifier, Integer> resolved = new HashMap<Identifier, Integer>();
        NonPregnancyCues.scanInstalledModAssets(resolved);
        NonPregnancyCues.scanExternalNoNPacks(resolved);
        CUE_TICK_BY_CLIP = Map.copyOf(resolved);
    }

    private static void scanInstalledModAssets(Map<Identifier, Integer> resolved) {
        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            for (Path root : mod.getRootPaths()) {
                Path assetsRoot = root.resolve("assets");
                if (!Files.isDirectory(assetsRoot, new LinkOption[0])) continue;
                NonPregnancyCues.loadAssetsNamespaces(assetsRoot, resolved);
            }
        }
    }

    private static void scanExternalNoNPacks(Map<Identifier, Integer> resolved) {
        Path packsDir = NonExternalPackProvider.resolveDefaultPacksDir();
        if (!Files.isDirectory(packsDir, new LinkOption[0])) {
            return;
        }
        try (Stream<Path> entries = Files.list(packsDir);){
            entries.filter(NonPackRootResolver::isSupportedExternalPackPath).forEach(path -> NonPregnancyCues.scanExternalNoNPack(path, resolved));
        }
        catch (IOException e) {
            NeedsOfNature.logSetupWarning("[NoN] Failed to enumerate external NoN packs in {}", packsDir, e);
        }
    }

    private static void scanExternalNoNPack(Path packPath, Map<Identifier, Integer> resolved) {
        if (packPath == null || !Files.exists(packPath, new LinkOption[0])) {
            return;
        }
        Optional<NonPackRootResolver.ResolvedRoot> root = NonPackRootResolver.resolve(packPath);
        root.ifPresent(resolvedRoot -> {
            if (resolvedRoot.isZip()) {
                NonPregnancyCues.loadExternalZipPack(resolvedRoot, resolved);
            } else {
                NonPregnancyCues.loadExternalFolderPack(resolvedRoot.rootPath(), resolved);
            }
        });
    }

    private static void loadExternalFolderPack(Path packRoot, Map<Identifier, Integer> resolved) {
        Path assetsRoot = packRoot.resolve("assets");
        if (!Files.isDirectory(assetsRoot, new LinkOption[0])) {
            return;
        }
        NonPregnancyCues.loadAssetsNamespaces(assetsRoot, resolved);
    }

    private static void loadExternalZipPack(NonPackRootResolver.ResolvedRoot packRoot, Map<Identifier, Integer> resolved) {
        try (ZipFile zip = new ZipFile(packRoot.sourcePath().toFile());){
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                Identifier clipId;
                String entryPath;
                ZipEntry entry = entries.nextElement();
                if (entry == null || entry.isDirectory() || (entryPath = packRoot.stripZipPrefix(entry.getName())) == null || (clipId = NonPregnancyCues.clipIdFromPackEntryPath(entryPath)) == null) continue;
                try {
                    InputStream in = zip.getInputStream(entry);
                    try {
                        OptionalInt cueTick = NonPregnancyCues.readFirstBirthCueTick(in, String.valueOf(packRoot.sourcePath()) + "!" + entryPath);
                        if (cueTick.isEmpty()) continue;
                        NonPregnancyCues.mergeCueTick(resolved, clipId, cueTick.getAsInt());
                    }
                    finally {
                        if (in == null) continue;
                        in.close();
                    }
                }
                catch (IOException | RuntimeException e) {
                    LOGGER.debug("[NoN] Failed to parse pregnancy cue in {} entry {}", new Object[]{packRoot.sourcePath(), entryPath, e});
                }
            }
        }
        catch (IOException e) {
            NeedsOfNature.logSetupWarning("[NoN] Failed to read external NoN pack {}", packRoot.sourcePath(), e);
        }
    }

    private static void loadAssetsNamespaces(Path assetsRoot, Map<Identifier, Integer> resolved) {
        try (Stream<Path> namespaces = Files.list(assetsRoot);){
            namespaces.filter(x$0 -> Files.isDirectory(x$0, new LinkOption[0])).forEach(namespaceDir -> NonPregnancyCues.loadNamespaceCues(namespaceDir, resolved));
        }
        catch (IOException e) {
            LOGGER.debug("[NoN] Failed to enumerate asset namespaces in {}", (Object)assetsRoot, (Object)e);
        }
    }

    private static void loadNamespaceCues(Path namespaceDir, Map<Identifier, Integer> resolved) {
        String namespace = namespaceDir.getFileName().toString();
        Path afwAnimationsDir = namespaceDir.resolve("geckolib").resolve("animations").resolve("afw");
        if (!Files.isDirectory(afwAnimationsDir, new LinkOption[0])) {
            return;
        }
        try (Stream<Path> files = Files.walk(afwAnimationsDir, new FileVisitOption[0]);){
            files.filter(x$0 -> Files.isRegularFile(x$0, new LinkOption[0])).filter(path -> path.getFileName().toString().endsWith(ANIMATION_FILE_SUFFIX)).forEach(path -> NonPregnancyCues.loadCueFromAnimationFile(namespace, afwAnimationsDir, path, resolved));
        }
        catch (IOException e) {
            LOGGER.debug("[NoN] Failed to scan pregnancy cues under {}", (Object)afwAnimationsDir, (Object)e);
        }
    }

    private static void loadCueFromAnimationFile(String namespace, Path afwAnimationsDir, Path filePath, Map<Identifier, Integer> resolved) {
        String relative = afwAnimationsDir.relativize(filePath).toString().replace('\\', '/');
        Identifier clipId = NonPregnancyCues.clipIdFromAnimationPath(namespace, relative);
        if (clipId == null) {
            return;
        }
        OptionalInt cueTick = NonPregnancyCues.readFirstBirthCueTick(filePath);
        if (cueTick.isEmpty()) {
            return;
        }
        NonPregnancyCues.mergeCueTick(resolved, clipId, cueTick.getAsInt());
    }

    private static void mergeCueTick(Map<Identifier, Integer> resolved, Identifier clipId, int candidate) {
        Integer existing = resolved.get(clipId);
        if (existing == null || candidate < existing) {
            resolved.put(clipId, candidate);
        }
    }

    private static Identifier clipIdFromPackEntryPath(String entryPath) {
        if (entryPath == null || entryPath.isBlank()) {
            return null;
        }
        String normalized = entryPath.replace('\\', '/');
        if (!normalized.startsWith("assets/")) {
            return null;
        }
        int namespaceStart = "assets/".length();
        int namespaceEnd = normalized.indexOf(47, namespaceStart);
        if (namespaceEnd <= namespaceStart) {
            return null;
        }
        String namespace = normalized.substring(namespaceStart, namespaceEnd);
        if (namespace.isBlank()) {
            return null;
        }
        String prefix = "assets/" + namespace + "/geckolib/animations/afw/";
        if (!normalized.startsWith(prefix)) {
            return null;
        }
        String relativePath = normalized.substring(prefix.length());
        return NonPregnancyCues.clipIdFromAnimationPath(namespace, relativePath);
    }

    private static Identifier clipIdFromAnimationPath(String namespace, String relativePath) {
        String fileName;
        if (namespace == null || namespace.isBlank() || relativePath == null || relativePath.isBlank()) {
            return null;
        }
        String normalized = relativePath.replace('\\', '/');
        int slashIndex = normalized.lastIndexOf(47);
        String string = fileName = slashIndex >= 0 ? normalized.substring(slashIndex + 1) : normalized;
        if (!fileName.endsWith(ANIMATION_FILE_SUFFIX)) {
            return null;
        }
        String clipPath = fileName.substring(0, fileName.length() - ANIMATION_FILE_SUFFIX.length());
        int stageMarker = clipPath.lastIndexOf(".p");
        if (stageMarker > 0) {
            int i;
            for (i = stageMarker + 2; i < clipPath.length() && Character.isDigit(clipPath.charAt(i)); ++i) {
            }
            if (i > stageMarker + 2 && i < clipPath.length() && clipPath.charAt(i) == '_') {
                clipPath = clipPath.substring(0, i);
            }
        }
        return Identifier.tryParse((String)(namespace + ":" + clipPath));
    }

    private static OptionalInt readFirstBirthCueTick(Path path) {
        OptionalInt optionalInt;
        block8: {
            InputStream in = Files.newInputStream(path, new OpenOption[0]);
            try {
                optionalInt = NonPregnancyCues.readFirstBirthCueTick(in, path.toString());
                if (in == null) break block8;
            }
            catch (Throwable throwable) {
                try {
                    if (in != null) {
                        try {
                            in.close();
                        }
                        catch (Throwable throwable2) {
                            throwable.addSuppressed(throwable2);
                        }
                    }
                    throw throwable;
                }
                catch (IOException | RuntimeException e) {
                    LOGGER.debug("[NoN] Failed to parse pregnancy cue file {}", (Object)path, (Object)e);
                    return OptionalInt.empty();
                }
            }
            in.close();
        }
        return optionalInt;
    }

    /*
     * Enabled aggressive exception aggregation
     */
    private static OptionalInt readFirstBirthCueTick(InputStream input, String sourceName) {
        try (InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8);){
            JsonObject root = JsonParser.parseReader((Reader)reader).getAsJsonObject();
            JsonObject animations = NonPregnancyCues.asObject(root.get("animations"));
            if (animations == null || animations.isEmpty()) {
                OptionalInt optionalInt = OptionalInt.empty();
                return optionalInt;
            }
            Integer bestTick = null;
            for (Map.Entry animationEntry : animations.entrySet()) {
                JsonObject effects;
                JsonObject animation = NonPregnancyCues.asObject((JsonElement)animationEntry.getValue());
                if (animation == null || (effects = NonPregnancyCues.asObject(animation.get("sound_effects"))) == null || effects.isEmpty()) continue;
                for (Map.Entry effectEntry : effects.entrySet()) {
                    int cueTick;
                    String timeKey = (String)effectEntry.getKey();
                    try {
                        double seconds = Double.parseDouble(timeKey);
                        if (seconds < 0.0) {
                            NeedsOfNature.logSetupWarning("[NoN] Invalid birth cue time '{}' in {}.", timeKey, sourceName);
                            continue;
                        }
                        cueTick = Math.max(0, (int)Math.round(seconds * 20.0));
                    }
                    catch (NumberFormatException ex) {
                        NeedsOfNature.logSetupWarning("[NoN] Malformed birth cue time '{}' in {}.", timeKey, sourceName);
                        continue;
                    }
                    if (!NonPregnancyCues.containsBirthEffect((JsonElement)effectEntry.getValue()) || bestTick != null && cueTick >= bestTick) continue;
                    bestTick = cueTick;
                }
            }
            OptionalInt optionalInt = bestTick == null ? OptionalInt.empty() : OptionalInt.of(bestTick);
            return optionalInt;
        }
        catch (IOException | RuntimeException e) {
            LOGGER.debug("[NoN] Failed to parse pregnancy cue file {}", (Object)sourceName, (Object)e);
            return OptionalInt.empty();
        }
    }

    private static boolean containsBirthEffect(JsonElement element) {
        if (element == null || element.isJsonNull()) {
            return false;
        }
        if (element.isJsonObject()) {
            JsonObject obj = element.getAsJsonObject();
            JsonElement effect = obj.get("effect");
            if (effect != null && effect.isJsonPrimitive() && effect.getAsJsonPrimitive().isString()) {
                return BIRTH_EFFECT.equals(effect.getAsString().trim().toLowerCase(Locale.ROOT));
            }
            return false;
        }
        if (element.isJsonArray()) {
            for (JsonElement child : element.getAsJsonArray()) {
                if (!NonPregnancyCues.containsBirthEffect(child)) continue;
                return true;
            }
        }
        return false;
    }

    private static JsonObject asObject(JsonElement element) {
        if (element == null || !element.isJsonObject()) {
            return null;
        }
        return element.getAsJsonObject();
    }

    public static final class Reloader
    implements SynchronousResourceReloader {
        static final Identifier RELOADER_ID = Identifier.of((String)"needsofnature", (String)"pregnancy_cues");

        public void reload(ResourceManager manager) {
            NonPregnancyCues.reload();
        }
    }
}

