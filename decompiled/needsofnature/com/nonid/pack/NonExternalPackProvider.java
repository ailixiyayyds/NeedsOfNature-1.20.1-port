/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.logging.LogUtils
 *  net.fabricmc.loader.api.FabricLoader
 *  net.minecraft.class_2561
 *  net.minecraft.class_3258$class_8615
 *  net.minecraft.class_3259$class_8619
 *  net.minecraft.class_3264
 *  net.minecraft.class_3285
 *  net.minecraft.class_3288
 *  net.minecraft.class_3288$class_3289
 *  net.minecraft.class_3288$class_7680
 *  net.minecraft.class_4239
 *  net.minecraft.class_5352
 *  net.minecraft.class_8579
 *  net.minecraft.class_8580
 *  net.minecraft.class_9224
 *  net.minecraft.class_9225
 *  org.slf4j.Logger
 */
package com.nonid.pack;

import com.mojang.logging.LogUtils;
import com.nonid.NeedsOfNature;
import com.nonid.NonConfig;
import com.nonid.pack.NonPackRootResolver;
import com.nonid.pack.NonPrefixedZipResourcePack;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_2561;
import net.minecraft.class_3258;
import net.minecraft.class_3259;
import net.minecraft.class_3264;
import net.minecraft.class_3285;
import net.minecraft.class_3288;
import net.minecraft.class_4239;
import net.minecraft.class_5352;
import net.minecraft.class_8579;
import net.minecraft.class_8580;
import net.minecraft.class_9224;
import net.minecraft.class_9225;
import org.slf4j.Logger;

public final class NonExternalPackProvider
implements class_3285 {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String EXTERNAL_PACKS_DIR_NAME = "needsofnature";
    private static final class_5352 NON_SOURCE = class_5352.method_45281(name -> class_2561.method_43470((String)"[NoN] ").method_10852(name), (boolean)true);
    private static final class_9225 ALWAYS_ENABLED_TOP = new class_9225(true, class_3288.class_3289.field_14280, false);
    private final Path packsDir;
    private final class_3264 type;
    private final class_8580 symlinkFinder;

    public NonExternalPackProvider(Path packsDir, class_3264 type, class_8580 symlinkFinder) {
        this.packsDir = packsDir;
        this.type = type;
        this.symlinkFinder = symlinkFinder;
    }

    public class_3264 getType() {
        return this.type;
    }

    public static Path resolveDefaultPacksDir() {
        return FabricLoader.getInstance().getGameDir().resolve(EXTERNAL_PACKS_DIR_NAME);
    }

    public static List<String> listExternalPackFileNames(Path dir) {
        if (dir == null || !Files.isDirectory(dir, new LinkOption[0])) {
            return List.of();
        }
        ArrayList names = new ArrayList();
        try (Stream<Path> paths = Files.list(dir);){
            paths.filter(NonPackRootResolver::isSupportedExternalPackPath).forEach(path -> {
                Path fileName = path.getFileName();
                if (fileName != null) {
                    names.add(fileName.toString());
                }
            });
        }
        catch (IOException e) {
            LOGGER.warn("Failed to list NoN packs in {}", (Object)dir, (Object)e);
        }
        names.sort(String.CASE_INSENSITIVE_ORDER);
        return List.copyOf(names);
    }

    public static List<String> normalizeConfiguredOrder(List<String> configuredOrder, List<String> discoveredNames) {
        List<Object> discovered;
        List<Object> list = discovered = discoveredNames == null ? List.of() : discoveredNames;
        if (discovered.isEmpty()) {
            return List.of();
        }
        LinkedHashSet<Object> discoveredSet = new LinkedHashSet<Object>(discovered);
        LinkedHashSet<String> ordered = new LinkedHashSet<String>();
        if (configuredOrder != null) {
            for (String string : configuredOrder) {
                String name;
                if (string == null || (name = string.trim()).isEmpty() || !discoveredSet.contains(name)) continue;
                ordered.add(name);
            }
        }
        for (String string : discovered) {
            ordered.add(string);
        }
        return List.copyOf(ordered);
    }

    public void method_14453(Consumer<class_3288> profileAdder) {
        try {
            class_4239.method_47525((Path)this.packsDir);
            ArrayList<PackCandidate> discovered = new ArrayList<PackCandidate>();
            try (Stream<Path> paths = Files.list(this.packsDir);){
                paths.filter(NonPackRootResolver::isSupportedExternalPackPath).forEach(path -> NonPackRootResolver.resolve(path).filter(this::isSymlinkSafe).ifPresent(root -> discovered.add(new PackCandidate((Path)path, NonExternalPackProvider.createPackFactory(root)))));
            }
            List<String> discoveredNames = discovered.stream().map(PackCandidate::fileName).sorted(String.CASE_INSENSITIVE_ORDER).toList();
            List<String> normalizedOrder = NonExternalPackProvider.normalizeConfiguredOrder(NonExternalPackProvider.resolveConfiguredOrder(), discoveredNames);
            Map<String, Integer> indexByName = NonExternalPackProvider.indexByName(normalizedOrder);
            discovered.sort(Comparator.comparingInt(candidate -> indexByName.getOrDefault(candidate.fileName(), Integer.MAX_VALUE)).thenComparing(PackCandidate::fileName, String.CASE_INSENSITIVE_ORDER));
            for (int i = discovered.size() - 1; i >= 0; --i) {
                PackCandidate candidate2 = (PackCandidate)discovered.get(i);
                int rank = indexByName.getOrDefault(candidate2.fileName(), Integer.MAX_VALUE);
                class_9224 info = this.createPackInfo(candidate2.path(), rank);
                class_3288 profile = class_3288.method_45275((class_9224)info, (class_3288.class_7680)candidate2.packFactory(), (class_3264)this.type, (class_9225)ALWAYS_ENABLED_TOP);
                if (profile == null) continue;
                profileAdder.accept(profile);
            }
        }
        catch (IOException e) {
            LOGGER.warn("Failed to list NoN packs in {}", (Object)this.packsDir, (Object)e);
        }
    }

    private boolean isSymlinkSafe(NonPackRootResolver.ResolvedRoot root) {
        if (root == null || this.symlinkFinder == null) {
            return true;
        }
        ArrayList entries = new ArrayList();
        try {
            if (root.isZip()) {
                if (Files.isSymbolicLink(root.sourcePath())) {
                    this.symlinkFinder.method_52242(root.sourcePath(), entries);
                }
            } else {
                this.symlinkFinder.method_52619(root.rootPath(), entries);
            }
        }
        catch (IOException e) {
            LOGGER.warn("Failed to validate NoN pack symlinks in {}, ignoring", (Object)root.sourcePath(), (Object)e);
            return false;
        }
        if (!entries.isEmpty()) {
            LOGGER.warn("Ignoring potential NoN pack entry: {}", (Object)class_8579.method_52241((Path)root.sourcePath(), entries));
            return false;
        }
        return true;
    }

    private static class_3288.class_7680 createPackFactory(NonPackRootResolver.ResolvedRoot root) {
        if (root.isZip()) {
            if (root.isNested()) {
                return new NonPrefixedZipResourcePack.Factory(root.sourcePath(), root.zipPrefix());
            }
            return new class_3258.class_8615(root.sourcePath());
        }
        return new class_3259.class_8619(root.rootPath());
    }

    private class_9224 createPackInfo(Path path, int rank) {
        String fileName = path.getFileName().toString();
        String channel = this.type == class_3264.field_14188 ? "client" : "server";
        String safeFileName = NonExternalPackProvider.sanitizeForPackId(fileName);
        String orderPrefix = NonExternalPackProvider.formatOrderPrefix(rank);
        String id = "needsofnature/" + channel + "/" + orderPrefix + "_" + safeFileName;
        return new class_9224(id, (class_2561)class_2561.method_43470((String)fileName), NON_SOURCE, Optional.empty());
    }

    private static List<String> resolveConfiguredOrder() {
        NonConfig config = NeedsOfNature.getConfig();
        if (config != null) {
            return config.getExternalNoNPackLoadOrder();
        }
        return List.of();
    }

    private static Map<String, Integer> indexByName(List<String> names) {
        LinkedHashMap<String, Integer> index = new LinkedHashMap<String, Integer>();
        if (names == null) {
            return index;
        }
        for (int i = 0; i < names.size(); ++i) {
            index.putIfAbsent(names.get(i), i);
        }
        return index;
    }

    private static String formatOrderPrefix(int rank) {
        int clamped = rank;
        if (clamped < 0) {
            clamped = 0;
        }
        if (clamped > 9999) {
            clamped = 9999;
        }
        int encoded = 9999 - clamped;
        return String.format(Locale.ROOT, "%04d", encoded);
    }

    private static String sanitizeForPackId(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return "unknown";
        }
        StringBuilder out = new StringBuilder(fileName.length());
        for (int i = 0; i < fileName.length(); ++i) {
            char c = fileName.charAt(i);
            boolean safe = c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9' || c == '.' || c == '_' || c == '-';
            out.append(safe ? c : (char)'_');
        }
        return out.toString();
    }

    private record PackCandidate(Path path, class_3288.class_7680 packFactory) {
        private String fileName() {
            Path fileName = this.path.getFileName();
            return fileName == null ? this.path.toString() : fileName.toString();
        }
    }
}

