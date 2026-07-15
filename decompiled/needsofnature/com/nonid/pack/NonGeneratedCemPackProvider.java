/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.loader.api.FabricLoader
 *  net.fabricmc.loader.api.ModContainer
 *  net.minecraft.class_2561
 *  net.minecraft.class_2960
 *  net.minecraft.class_3264
 *  net.minecraft.class_3285
 *  net.minecraft.class_3288
 */
package com.nonid.pack;

import com.nonid.NeedsOfNature;
import com.nonid.pack.NonExternalPackProvider;
import com.nonid.pack.NonGeneratedInMemoryPack;
import com.nonid.pack.NonPackRootResolver;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_3264;
import net.minecraft.class_3285;
import net.minecraft.class_3288;

public final class NonGeneratedCemPackProvider
implements class_3285 {
    private static final String CEM_ROOT = "assets/minecraft/optifine/cem/";
    private static final byte[] VANILLA_FALLBACK_JEM_BYTES = "{\n  \"textureSize\": [64, 64],\n  \"models\": []\n}\n".getBytes(StandardCharsets.UTF_8);
    private final Path packsDir;

    public NonGeneratedCemPackProvider(Path packsDir) {
        this.packsDir = packsDir;
    }

    public void method_14453(Consumer<class_3288> profileAdder) {
        Map<class_2960, byte[]> generatedResources = this.buildGeneratedResources();
        if (generatedResources.isEmpty()) {
            return;
        }
        class_3288 profile = NonGeneratedInMemoryPack.createProfile("needsofnature/client/generated_cem", (class_2561)class_2561.method_43470((String)"NoN Generated CEM Rules"), (class_2561)class_2561.method_43470((String)"Generated NoN CEM gender and age rules"), class_3264.field_14188, generatedResources);
        if (profile != null) {
            profileAdder.accept(profile);
        }
    }

    private Map<class_2960, byte[]> buildGeneratedResources() {
        List<ScanSource> orderedSources = NonGeneratedCemPackProvider.listOrderedScanSources(this.packsDir);
        if (orderedSources.isEmpty()) {
            return Map.of();
        }
        LinkedHashMap<String, Resolution> resolved = new LinkedHashMap<String, Resolution>();
        for (ScanSource source : orderedSources) {
            PackScan scan = NonGeneratedCemPackProvider.scanSource(source);
            if (scan.isEmpty()) continue;
            LinkedHashSet<String> bases = new LinkedHashSet<String>();
            bases.addAll(scan.explicitProperties);
            bases.addAll(scan.variants.keySet());
            for (String base : bases) {
                if (resolved.containsKey(base)) continue;
                if (scan.explicitProperties.contains(base)) {
                    resolved.put(base, Resolution.explicit());
                    continue;
                }
                CemVariants variants = scan.variants.get(base);
                if (variants == null || !variants.needsGeneratedProperties()) continue;
                resolved.put(base, Resolution.generated(variants));
            }
        }
        if (resolved.isEmpty()) {
            return Map.of();
        }
        LinkedHashMap<class_2960, byte[]> generated = new LinkedHashMap<class_2960, byte[]>();
        for (Map.Entry entry : resolved.entrySet()) {
            String base = (String)entry.getKey();
            Resolution resolution = (Resolution)entry.getValue();
            if (resolution.kind == ResolutionKind.EXPLICIT) continue;
            generated.put(NonGeneratedCemPackProvider.cemIdentifier(base + ".properties"), NonGeneratedCemPackProvider.generatedPropertiesBytes(resolution.variants));
            generated.put(NonGeneratedCemPackProvider.cemIdentifier(base + ".jem"), VANILLA_FALLBACK_JEM_BYTES);
            NonGeneratedCemPackProvider.putGeneratedVariant(generated, base, 3, resolution.variants.adultPrimaryBytes());
            NonGeneratedCemPackProvider.putGeneratedVariant(generated, base, 2, resolution.variants.adultTwo);
            NonGeneratedCemPackProvider.putGeneratedVariant(generated, base, 5, resolution.variants.babyPrimaryBytes());
            NonGeneratedCemPackProvider.putGeneratedVariant(generated, base, 4, resolution.variants.babyTwo);
        }
        return generated;
    }

    private static void putGeneratedVariant(Map<class_2960, byte[]> generated, String base, int suffix, byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return;
        }
        generated.put(NonGeneratedCemPackProvider.cemIdentifier(base + suffix + ".jem"), bytes);
    }

    private static List<ScanSource> listOrderedScanSources(Path externalDir) {
        ArrayList<ScanSource> ordered = new ArrayList<ScanSource>();
        ordered.addAll(NonGeneratedCemPackProvider.listOrderedExternalPackSources(externalDir));
        NonGeneratedCemPackProvider.resolveBuiltinCemRoot().ifPresent(cemRoot -> ordered.add(ScanSource.cemRoot("builtin", cemRoot)));
        return List.copyOf(ordered);
    }

    private static List<ScanSource> listOrderedExternalPackSources(Path dir) {
        if (dir == null || !Files.isDirectory(dir, new LinkOption[0])) {
            return List.of();
        }
        ArrayList discoveredPaths = new ArrayList();
        try (Stream<Path> stream = Files.list(dir);){
            stream.filter(NonPackRootResolver::isSupportedExternalPackPath).forEach(discoveredPaths::add);
        }
        catch (IOException e) {
            NeedsOfNature.logSetupWarning("[NoN] Failed to scan external NoN packs for generated CEM rules.", e);
            return List.of();
        }
        if (discoveredPaths.isEmpty()) {
            return List.of();
        }
        LinkedHashMap<String, Path> byName = new LinkedHashMap<String, Path>();
        for (Path path : discoveredPaths) {
            Path fileName = path.getFileName();
            if (fileName == null) continue;
            byName.put(fileName.toString(), path);
        }
        ArrayList<String> discoveredNames = new ArrayList<String>(byName.keySet());
        discoveredNames.sort(String.CASE_INSENSITIVE_ORDER);
        List<String> normalizedOrder = NonExternalPackProvider.normalizeConfiguredOrder(NeedsOfNature.getConfig().getExternalNoNPackLoadOrder(), discoveredNames);
        ArrayList ordered = new ArrayList();
        for (String name : normalizedOrder) {
            Path path = (Path)byName.get(name);
            if (path == null) continue;
            NonPackRootResolver.resolve(path).ifPresent(root -> ordered.add(ScanSource.packRoot(name, root)));
        }
        return List.copyOf(ordered);
    }

    private static Optional<Path> resolveBuiltinCemRoot() {
        Optional container = FabricLoader.getInstance().getModContainer("needsofnature");
        if (container.isEmpty()) {
            return Optional.empty();
        }
        return ((ModContainer)container.get()).findPath(CEM_ROOT.substring(0, CEM_ROOT.length() - 1));
    }

    private static PackScan scanSource(ScanSource source) {
        if (source == null) {
            return PackScan.EMPTY;
        }
        try {
            if (source.kind == ScanSourceKind.CEM_ROOT) {
                return NonGeneratedCemPackProvider.scanCemRoot(source.path);
            }
            if (source.packRoot == null) {
                return PackScan.EMPTY;
            }
            if (source.packRoot.isZip()) {
                return NonGeneratedCemPackProvider.scanZipPack(source.packRoot);
            }
            return NonGeneratedCemPackProvider.scanDirectoryPack(source.packRoot.rootPath());
        }
        catch (IOException e) {
            NeedsOfNature.LOGGER.debug("[NoN] Failed to scan {} for generated CEM rules.", (Object)source.debugName, (Object)e);
            return PackScan.EMPTY;
        }
    }

    private static PackScan scanDirectoryPack(Path packPath) throws IOException {
        Path cemRoot = packPath.resolve(CEM_ROOT);
        if (!Files.isDirectory(cemRoot, new LinkOption[0])) {
            return PackScan.EMPTY;
        }
        return NonGeneratedCemPackProvider.scanCemRoot(cemRoot);
    }

    private static PackScan scanCemRoot(Path cemRoot) throws IOException {
        LinkedHashMap<String, CemVariants> variants = new LinkedHashMap<String, CemVariants>();
        LinkedHashSet<String> explicitProperties = new LinkedHashSet<String>();
        try (Stream<Path> paths = Files.walk(cemRoot, new FileVisitOption[0]);){
            for (Path path : paths.filter(x$0 -> Files.isRegularFile(x$0, new LinkOption[0])).toList()) {
                String relative = cemRoot.relativize(path).toString().replace('\\', '/');
                byte[] bytes = relative.endsWith(".jem") ? Files.readAllBytes(path) : null;
                NonGeneratedCemPackProvider.processCemPath(relative, bytes, explicitProperties, variants);
            }
        }
        return new PackScan(explicitProperties, variants);
    }

    private static PackScan scanZipPack(NonPackRootResolver.ResolvedRoot packRoot) throws IOException {
        LinkedHashSet<String> explicitProperties = new LinkedHashSet<String>();
        LinkedHashMap<String, CemVariants> variants = new LinkedHashMap<String, CemVariants>();
        try (ZipFile zipFile = new ZipFile(packRoot.sourcePath().toFile());){
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                String name;
                ZipEntry entry = entries.nextElement();
                if (entry.isDirectory() || (name = packRoot.stripZipPrefix(entry.getName())) == null || !name.startsWith(CEM_ROOT)) continue;
                String relative = name.substring(CEM_ROOT.length());
                byte[] bytes = relative.endsWith(".jem") ? zipFile.getInputStream(entry).readAllBytes() : null;
                NonGeneratedCemPackProvider.processCemPath(relative, bytes, explicitProperties, variants);
            }
        }
        return new PackScan(explicitProperties, variants);
    }

    private static void processCemPath(String relativePath, byte[] bytes, Set<String> explicitProperties, Map<String, CemVariants> variants) {
        CemVariantKind kind;
        String base;
        if (relativePath == null || relativePath.isBlank()) {
            return;
        }
        String normalized = relativePath.replace('\\', '/');
        if (normalized.endsWith(".properties")) {
            String base2 = normalized.substring(0, normalized.length() - ".properties".length());
            if (!base2.isBlank()) {
                explicitProperties.add(base2);
            }
            return;
        }
        if (!normalized.endsWith(".jem")) {
            return;
        }
        String stem = normalized.substring(0, normalized.length() - ".jem".length());
        boolean baby = false;
        if (stem.endsWith("_baby") && stem.length() > "_baby".length()) {
            stem = stem.substring(0, stem.length() - "_baby".length());
            baby = true;
        }
        if (stem.endsWith("1") && stem.length() > 1) {
            base = stem.substring(0, stem.length() - 1);
            kind = CemVariantKind.ONE;
        } else if (stem.endsWith("2") && stem.length() > 1) {
            base = stem.substring(0, stem.length() - 1);
            kind = CemVariantKind.TWO;
        } else {
            base = stem;
            kind = CemVariantKind.BASE;
        }
        if (base.isBlank()) {
            return;
        }
        CemVariants entry = variants.computeIfAbsent(base, key -> new CemVariants());
        entry.mark(kind, baby, bytes);
    }

    private static class_2960 cemIdentifier(String relativePath) {
        return class_2960.method_60655((String)"minecraft", (String)("optifine/cem/" + relativePath));
    }

    private static byte[] generatedPropertiesBytes(CemVariants variants) {
        StringBuilder content = new StringBuilder();
        int rule = 1;
        if (variants.hasAdultPrimary()) {
            rule = NonGeneratedCemPackProvider.appendPrimaryGenderRules(content, rule, 3, false);
        }
        if (variants.hasAdultTwo) {
            rule = NonGeneratedCemPackProvider.appendAgeGenderRule(content, rule, 2, false, 2, true);
        }
        if (variants.hasBabyPrimary()) {
            rule = NonGeneratedCemPackProvider.appendPrimaryGenderRules(content, rule, 5, true);
        }
        if (variants.hasBabyTwo) {
            NonGeneratedCemPackProvider.appendAgeGenderRule(content, rule, 4, true, 2, true);
        }
        return content.toString().getBytes(StandardCharsets.UTF_8);
    }

    private static int appendPrimaryGenderRules(StringBuilder content, int rule, int modelSuffix, boolean baby) {
        rule = NonGeneratedCemPackProvider.appendAgeGenderRule(content, rule, modelSuffix, baby, 1, true);
        return NonGeneratedCemPackProvider.appendAgeGenderRule(content, rule, modelSuffix, baby, 3, true);
    }

    private static int appendAgeGenderRule(StringBuilder content, int rule, int modelSuffix, boolean baby, int gender, boolean enabled) {
        if (!enabled) {
            return rule;
        }
        NonGeneratedCemPackProvider.appendRuleHeader(content, rule, modelSuffix);
        content.append("nbt.").append(rule).append('.').append("NeedsOfNatureGender").append('=').append(gender).append('\n');
        content.append("baby.").append(rule).append('=').append(baby).append('\n');
        return rule + 1;
    }

    private static void appendRuleHeader(StringBuilder content, int rule, int modelSuffix) {
        if (!content.isEmpty()) {
            content.append('\n');
        }
        content.append("models.").append(rule).append('=').append(modelSuffix).append('\n');
    }

    private static final class ScanSource {
        private final ScanSourceKind kind;
        private final String debugName;
        private final Path path;
        private final NonPackRootResolver.ResolvedRoot packRoot;

        private ScanSource(ScanSourceKind kind, String debugName, Path path, NonPackRootResolver.ResolvedRoot packRoot) {
            this.kind = kind;
            this.debugName = debugName;
            this.path = path;
            this.packRoot = packRoot;
        }

        private static ScanSource packRoot(String debugName, NonPackRootResolver.ResolvedRoot packRoot) {
            return new ScanSource(ScanSourceKind.PACK_ROOT, debugName, null, packRoot);
        }

        private static ScanSource cemRoot(String debugName, Path path) {
            return new ScanSource(ScanSourceKind.CEM_ROOT, debugName, path, null);
        }
    }

    private record PackScan(Set<String> explicitProperties, Map<String, CemVariants> variants) {
        private static final PackScan EMPTY = new PackScan(Set.of(), Map.of());

        private boolean isEmpty() {
            return this.explicitProperties.isEmpty() && this.variants.isEmpty();
        }
    }

    private static final class Resolution {
        private final ResolutionKind kind;
        private final CemVariants variants;

        private Resolution(ResolutionKind kind, CemVariants variants) {
            this.kind = kind;
            this.variants = variants;
        }

        private static Resolution explicit() {
            return new Resolution(ResolutionKind.EXPLICIT, null);
        }

        private static Resolution generated(CemVariants variants) {
            return new Resolution(ResolutionKind.GENERATED, variants);
        }
    }

    private static final class CemVariants {
        private boolean hasAdultBase;
        private boolean hasAdultOne;
        private boolean hasAdultTwo;
        private boolean hasBabyBase;
        private boolean hasBabyOne;
        private boolean hasBabyTwo;
        private byte[] adultBase;
        private byte[] adultOne;
        private byte[] adultTwo;
        private byte[] babyBase;
        private byte[] babyOne;
        private byte[] babyTwo;

        private CemVariants() {
        }

        private void mark(CemVariantKind kind, boolean baby, byte[] bytes) {
            if (baby) {
                switch (kind.ordinal()) {
                    case 0: {
                        this.hasBabyBase = true;
                        this.babyBase = bytes;
                        break;
                    }
                    case 1: {
                        this.hasBabyOne = true;
                        this.babyOne = bytes;
                        break;
                    }
                    case 2: {
                        this.hasBabyTwo = true;
                        this.babyTwo = bytes;
                    }
                }
                return;
            }
            switch (kind.ordinal()) {
                case 0: {
                    this.hasAdultBase = true;
                    this.adultBase = bytes;
                    break;
                }
                case 1: {
                    this.hasAdultOne = true;
                    this.adultOne = bytes;
                    break;
                }
                case 2: {
                    this.hasAdultTwo = true;
                    this.adultTwo = bytes;
                }
            }
        }

        private boolean hasAdultPrimary() {
            return this.hasAdultOne || this.hasAdultBase;
        }

        private boolean hasBabyPrimary() {
            return this.hasBabyOne || this.hasBabyBase;
        }

        private byte[] adultPrimaryBytes() {
            return this.adultOne != null ? this.adultOne : this.adultBase;
        }

        private byte[] babyPrimaryBytes() {
            return this.babyOne != null ? this.babyOne : this.babyBase;
        }

        private boolean needsGeneratedProperties() {
            return this.hasAdultModelRules() || this.hasBabyModelRules();
        }

        private boolean hasAdultModelRules() {
            return this.hasAdultBase || this.hasAdultOne || this.hasAdultTwo;
        }

        private boolean hasBabyModelRules() {
            return this.hasBabyBase || this.hasBabyOne || this.hasBabyTwo;
        }
    }

    private static enum ResolutionKind {
        EXPLICIT,
        GENERATED;

    }

    private static enum ScanSourceKind {
        PACK_ROOT,
        CEM_ROOT;

    }

    private static enum CemVariantKind {
        BASE,
        ONE,
        TWO;

    }
}

