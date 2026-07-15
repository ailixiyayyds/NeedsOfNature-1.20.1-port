/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  com.google.gson.JsonPrimitive
 *  net.fabricmc.fabric.api.resource.v1.ResourceLoader
 *  net.fabricmc.loader.api.FabricLoader
 *  net.fabricmc.loader.api.ModContainer
 *  net.minecraft.util.Identifier
 *  net.minecraft.resource.ResourceType
 *  net.minecraft.resource.ResourceManager
 *  net.minecraft.resource.ResourceReloader
 *  net.minecraft.resource.SynchronousResourceReloader
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.nonid.NeedsOfNature;
import com.nonid.NonConfig;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
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
import org.jetbrains.annotations.Nullable;

public final class NonReactiveImpactCues {
    private static final String EXTERNAL_PACKS_DIR_NAME = "needsofnature";
    private static final String REACTIVE_IMPACT_EFFECT = "reactiveimpact";
    private static final String ANIMATION_SUFFIX = ".animation.json";
    private static final String AFW_ANIMATION_PATH_PREFIX = "geckolib/animations/afw/";
    private static volatile Map<Identifier, CueProfile> PROFILE_BY_CLIP = Map.of();

    private NonReactiveImpactCues() {
    }

    public static void registerReloadListener() {
        ResourceLoader.get((ResourceType)ResourceType.SERVER_DATA).registerReloader(Reloader.RELOADER_ID, (ResourceReloader)new Reloader());
    }

    @Nullable
    public static CueProfile getProfile(@Nullable Identifier clipId) {
        if (clipId == null) {
            return null;
        }
        return PROFILE_BY_CLIP.get(clipId);
    }

    private static void reload() {
        LinkedHashMap<String, ParsedFile> filesByResourceKey = new LinkedHashMap<String, ParsedFile>();
        NonReactiveImpactCues.scanInstalledMods(filesByResourceKey);
        NonReactiveImpactCues.scanExternalPacks(filesByResourceKey);
        LinkedHashMap<Identifier, MutableProfile> merged = new LinkedHashMap<Identifier, MutableProfile>();
        for (ParsedFile file : filesByResourceKey.values()) {
            if (file == null || file.clipId == null || file.cueTicks.isEmpty()) continue;
            MutableProfile profile = merged.computeIfAbsent(file.clipId, ignored -> new MutableProfile());
            profile.cueTicks.addAll(file.cueTicks);
            if (file.cycleTicks == null || file.cycleTicks <= 0 || profile.cycleTicks != null && file.cycleTicks <= profile.cycleTicks) continue;
            profile.cycleTicks = file.cycleTicks;
        }
        LinkedHashMap<Identifier, CueProfile> finalized = new LinkedHashMap<Identifier, CueProfile>();
        for (Map.Entry entry : merged.entrySet()) {
            ArrayList<Integer> ticks = new ArrayList<Integer>(((MutableProfile)entry.getValue()).cueTicks);
            if (ticks.isEmpty()) continue;
            Integer cycleTicks = ((MutableProfile)entry.getValue()).cycleTicks;
            finalized.put((Identifier)entry.getKey(), new CueProfile(List.copyOf(ticks), cycleTicks));
        }
        PROFILE_BY_CLIP = Map.copyOf(finalized);
    }

    private static void scanInstalledMods(Map<String, ParsedFile> filesByResourceKey) {
        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            for (Path root : mod.getRootPaths()) {
                Path assetsRoot = root.resolve("assets");
                if (!Files.isDirectory(assetsRoot, new LinkOption[0])) continue;
                NonReactiveImpactCues.loadAssetsNamespaces(assetsRoot, filesByResourceKey);
            }
        }
    }

    private static void scanExternalPacks(Map<String, ParsedFile> filesByResourceKey) {
        Path packsDir = FabricLoader.getInstance().getGameDir().resolve(EXTERNAL_PACKS_DIR_NAME);
        if (!Files.isDirectory(packsDir, new LinkOption[0])) {
            return;
        }
        List<String> discovered = NonExternalPackProvider.listExternalPackFileNames(packsDir);
        NonConfig config = NeedsOfNature.getConfig();
        List<Object> configuredOrder = config == null ? List.of() : config.getExternalNoNPackLoadOrder();
        List<String> normalized = NonExternalPackProvider.normalizeConfiguredOrder(configuredOrder, discovered);
        if (normalized.isEmpty()) {
            return;
        }
        ArrayList<String> lowToHigh = new ArrayList<String>(normalized);
        Collections.reverse(lowToHigh);
        for (String fileName : lowToHigh) {
            if (fileName == null || fileName.isBlank()) continue;
            NonReactiveImpactCues.scanExternalPackPath(packsDir.resolve(fileName), filesByResourceKey);
        }
    }

    private static void scanExternalPackPath(Path path, Map<String, ParsedFile> filesByResourceKey) {
        if (path == null || !Files.exists(path, new LinkOption[0])) {
            return;
        }
        Optional<NonPackRootResolver.ResolvedRoot> resolved = NonPackRootResolver.resolve(path);
        if (resolved.isEmpty()) {
            return;
        }
        NonPackRootResolver.ResolvedRoot root = resolved.get();
        if (!root.isZip()) {
            Path assetsRoot = root.rootPath().resolve("assets");
            if (Files.isDirectory(assetsRoot, new LinkOption[0])) {
                NonReactiveImpactCues.loadAssetsNamespaces(assetsRoot, filesByResourceKey);
            }
            return;
        }
        try (ZipFile zip = new ZipFile(path.toFile());){
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ParsedPath parsedPath;
                String normalized;
                String entryPath;
                ZipEntry entry = entries.nextElement();
                if (entry == null || entry.isDirectory() || (entryPath = root.stripZipPrefix(entry.getName())) == null || entryPath.isBlank() || !(normalized = entryPath.replace('\\', '/')).startsWith("assets/") || (parsedPath = NonReactiveImpactCues.parseZipAssetPath(normalized)) == null) continue;
                try {
                    InputStream in = zip.getInputStream(entry);
                    try {
                        ParsedFile parsed = NonReactiveImpactCues.parseAnimationFile(in, parsedPath.namespace, parsedPath.namespaceRelativePath);
                        if (parsed == null) continue;
                        filesByResourceKey.put(parsedPath.resourceKey(), parsed);
                    }
                    finally {
                        if (in == null) continue;
                        in.close();
                    }
                }
                catch (IOException | RuntimeException e) {
                    NeedsOfNature.LOGGER.debug("[NoN] Failed to parse reactiveimpact cues in {} entry {}", new Object[]{path, normalized, e});
                }
            }
        }
        catch (IOException e) {
            NeedsOfNature.logSetupWarning("[NoN] Failed to scan external NoN pack for reactiveimpact cues {}", path, e);
        }
    }

    private static void loadAssetsNamespaces(Path assetsRoot, Map<String, ParsedFile> filesByResourceKey) {
        try (Stream<Path> namespaces = Files.list(assetsRoot);){
            namespaces.filter(x$0 -> Files.isDirectory(x$0, new LinkOption[0])).forEach(namespaceDir -> NonReactiveImpactCues.loadNamespaceFiles(namespaceDir, filesByResourceKey));
        }
        catch (IOException e) {
            NeedsOfNature.LOGGER.debug("[NoN] Failed to enumerate namespaces under {}", (Object)assetsRoot, (Object)e);
        }
    }

    private static void loadNamespaceFiles(Path namespaceDir, Map<String, ParsedFile> filesByResourceKey) {
        String namespace = namespaceDir.getFileName().toString();
        Path afwRoot = namespaceDir.resolve("geckolib").resolve("animations").resolve("afw");
        if (!Files.isDirectory(afwRoot, new LinkOption[0])) {
            return;
        }
        try (Stream<Path> files = Files.walk(afwRoot, new FileVisitOption[0]);){
            files.filter(x$0 -> Files.isRegularFile(x$0, new LinkOption[0])).filter(path -> {
                String name = path.getFileName().toString();
                return name.endsWith(ANIMATION_SUFFIX);
            }).forEach(path -> {
                String rel = namespaceDir.relativize((Path)path).toString().replace('\\', '/');
                try (InputStream in = Files.newInputStream(path, new OpenOption[0]);){
                    ParsedFile parsed = NonReactiveImpactCues.parseAnimationFile(in, namespace, rel);
                    if (parsed != null) {
                        filesByResourceKey.put(namespace + ":" + rel, parsed);
                    }
                }
                catch (IOException | RuntimeException e) {
                    NeedsOfNature.LOGGER.debug("[NoN] Failed to parse reactiveimpact cue file {}", path, (Object)e);
                }
            });
        }
        catch (IOException e) {
            NeedsOfNature.LOGGER.debug("[NoN] Failed to walk AFW animation assets under {}", (Object)afwRoot, (Object)e);
        }
    }

    /*
     * Enabled aggressive exception aggregation
     */
    @Nullable
    private static ParsedFile parseAnimationFile(InputStream input, String namespace, String namespaceRelativePath) {
        if (input == null || namespace == null || namespace.isBlank() || namespaceRelativePath == null || namespaceRelativePath.isBlank()) {
            return null;
        }
        String normalizedPath = namespaceRelativePath.replace('\\', '/');
        if (!normalizedPath.startsWith(AFW_ANIMATION_PATH_PREFIX) || !normalizedPath.endsWith(ANIMATION_SUFFIX)) {
            return null;
        }
        Identifier clipId = NonReactiveImpactCues.clipIdFromAnimationPath(namespace, normalizedPath.substring(AFW_ANIMATION_PATH_PREFIX.length()));
        if (clipId == null) {
            return null;
        }
        try (InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8);){
            Object object;
            JsonObject root = JsonParser.parseReader((Reader)reader).getAsJsonObject();
            JsonObject animations = NonReactiveImpactCues.asObject(root.get("animations"));
            if (animations == null || animations.isEmpty()) {
                ParsedFile parsedFile = null;
                return parsedFile;
            }
            TreeSet<Integer> cueTicks = new TreeSet<Integer>();
            Integer cycleTicks = null;
            for (Map.Entry animationEntry : animations.entrySet()) {
                JsonObject soundEffects;
                JsonObject animation = NonReactiveImpactCues.asObject((JsonElement)animationEntry.getValue());
                if (animation == null) continue;
                Integer parsedCycle = NonReactiveImpactCues.parseCycleTicks(animation);
                if (parsedCycle != null && parsedCycle > 0 && (cycleTicks == null || parsedCycle > cycleTicks)) {
                    cycleTicks = parsedCycle;
                }
                if ((soundEffects = NonReactiveImpactCues.asObject(animation.get("sound_effects"))) == null || soundEffects.isEmpty()) continue;
                for (Map.Entry cueEntry : soundEffects.entrySet()) {
                    int cueTick = NonReactiveImpactCues.parseCueTick((String)cueEntry.getKey());
                    if (cueTick < 0 || !NonReactiveImpactCues.containsReactiveImpact((JsonElement)cueEntry.getValue())) continue;
                    cueTicks.add(cueTick);
                }
            }
            if (cueTicks.isEmpty()) {
                object = null;
                return object;
            }
            object = new ParsedFile(clipId, List.copyOf(cueTicks), cycleTicks);
            return object;
        }
        catch (IOException | RuntimeException e) {
            NeedsOfNature.LOGGER.debug("[NoN] Failed to parse reactiveimpact cues in {}:{}", new Object[]{namespace, namespaceRelativePath, e});
            return null;
        }
    }

    @Nullable
    private static Identifier clipIdFromAnimationPath(String namespace, String relativeAfwPath) {
        String fileName;
        if (namespace == null || namespace.isBlank() || relativeAfwPath == null || relativeAfwPath.isBlank()) {
            return null;
        }
        String normalized = relativeAfwPath.replace('\\', '/');
        int slash = normalized.lastIndexOf(47);
        String string = fileName = slash >= 0 ? normalized.substring(slash + 1) : normalized;
        if (!fileName.endsWith(ANIMATION_SUFFIX)) {
            return null;
        }
        String clipPath = fileName.substring(0, fileName.length() - ANIMATION_SUFFIX.length());
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

    private static int parseCueTick(String timeKey) {
        if (timeKey == null || timeKey.isBlank()) {
            return -1;
        }
        try {
            double seconds = Double.parseDouble(timeKey.trim());
            if (!Double.isFinite(seconds) || seconds < 0.0) {
                return -1;
            }
            return Math.max(0, (int)Math.round(seconds * 20.0));
        }
        catch (NumberFormatException e) {
            return -1;
        }
    }

    @Nullable
    private static Integer parseCycleTicks(JsonObject animationObj) {
        if (animationObj == null) {
            return null;
        }
        Double lengthSeconds = NonReactiveImpactCues.parseFiniteDouble(animationObj.get("animation_length"));
        if (lengthSeconds == null || lengthSeconds <= 0.0) {
            return null;
        }
        return Math.max(1, (int)Math.round(lengthSeconds * 20.0));
    }

    @Nullable
    private static Double parseFiniteDouble(@Nullable JsonElement element) {
        if (element == null || !element.isJsonPrimitive()) {
            return null;
        }
        JsonPrimitive primitive = element.getAsJsonPrimitive();
        if (primitive.isNumber()) {
            double value = primitive.getAsDouble();
            return Double.isFinite(value) ? Double.valueOf(value) : null;
        }
        return null;
    }

    private static boolean containsReactiveImpact(@Nullable JsonElement element) {
        if (element == null || element.isJsonNull()) {
            return false;
        }
        if (element.isJsonPrimitive()) {
            if (!element.getAsJsonPrimitive().isString()) {
                return false;
            }
            return REACTIVE_IMPACT_EFFECT.equalsIgnoreCase(element.getAsString().trim());
        }
        if (element.isJsonArray()) {
            for (JsonElement child : element.getAsJsonArray()) {
                if (!NonReactiveImpactCues.containsReactiveImpact(child)) continue;
                return true;
            }
            return false;
        }
        if (!element.isJsonObject()) {
            return false;
        }
        JsonObject obj = element.getAsJsonObject();
        String effect = NonReactiveImpactCues.readString(obj, "effect");
        if (effect == null) {
            effect = NonReactiveImpactCues.readString(obj, "sound");
        }
        return effect != null && REACTIVE_IMPACT_EFFECT.equalsIgnoreCase(effect.trim());
    }

    @Nullable
    private static String readString(JsonObject obj, String key) {
        if (obj == null || key == null || key.isBlank()) {
            return null;
        }
        JsonElement value = obj.get(key);
        if (value == null || !value.isJsonPrimitive() || !value.getAsJsonPrimitive().isString()) {
            return null;
        }
        return value.getAsString();
    }

    @Nullable
    private static JsonObject asObject(@Nullable JsonElement element) {
        if (element == null || !element.isJsonObject()) {
            return null;
        }
        return element.getAsJsonObject();
    }

    @Nullable
    private static ParsedPath parseZipAssetPath(String entryPath) {
        int nsStart = "assets/".length();
        int nsEnd = entryPath.indexOf(47, nsStart);
        if (nsEnd <= nsStart) {
            return null;
        }
        String namespace = entryPath.substring(nsStart, nsEnd);
        if (namespace.isBlank()) {
            return null;
        }
        String nsRelative = entryPath.substring(nsEnd + 1);
        if (nsRelative.isBlank()) {
            return null;
        }
        return new ParsedPath(namespace, nsRelative);
    }

    public static final class Reloader
    implements SynchronousResourceReloader {
        static final Identifier RELOADER_ID = Identifier.of((String)"needsofnature", (String)"reactiveimpact_cues");

        public void reload(ResourceManager manager) {
            NonReactiveImpactCues.reload();
        }
    }

    public record CueProfile(List<Integer> cueTicks, @Nullable Integer cycleTicks) {
    }

    private record ParsedFile(Identifier clipId, List<Integer> cueTicks, @Nullable Integer cycleTicks) {
    }

    private static final class MutableProfile {
        private final Set<Integer> cueTicks = new TreeSet<Integer>();
        @Nullable
        private Integer cycleTicks;

        private MutableProfile() {
        }
    }

    private record ParsedPath(String namespace, String namespaceRelativePath) {
        private String resourceKey() {
            return this.namespace + ":" + this.namespaceRelativePath;
        }
    }
}

