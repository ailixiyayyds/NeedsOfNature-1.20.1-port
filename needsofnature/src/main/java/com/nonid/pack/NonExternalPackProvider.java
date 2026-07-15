/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.logging.LogUtils
 *  net.fabricmc.loader.api.FabricLoader
 *  net.minecraft.text.Text
 *  net.minecraft.resource.ZipResourcePack$ZipBackedFactory
 *  net.minecraft.resource.DirectoryResourcePack$DirectoryBackedFactory
 *  net.minecraft.resource.ResourceType
 *  net.minecraft.resource.ResourcePackProvider
 *  net.minecraft.resource.ResourcePackProfile
 *  net.minecraft.resource.ResourcePackProfile$InsertionPosition
 *  net.minecraft.resource.ResourcePackProfile$PackFactory
 *  net.minecraft.util.path.PathUtil
 *  net.minecraft.resource.ResourcePackSource
 *  net.minecraft.util.path.SymlinkValidationException
 *  net.minecraft.util.path.SymlinkFinder
 *  net.minecraft.resource.ResourcePackInfo
 *  net.minecraft.resource.ResourcePackPosition
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
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import net.minecraft.resource.ZipResourcePack;
import net.minecraft.resource.DirectoryResourcePack;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.util.path.SymlinkValidationException;
import net.minecraft.util.path.SymlinkFinder;
import org.slf4j.Logger;

public final class NonExternalPackProvider
implements ResourcePackProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String EXTERNAL_PACKS_DIR_NAME = "needsofnature";
    private static final ResourcePackSource NON_SOURCE = ResourcePackSource.create(name -> Text.literal((String)"[NoN] ").append(name), (boolean)true);
    private final Path packsDir;
    private final ResourceType type;
    private final SymlinkFinder symlinkFinder;

    public NonExternalPackProvider(Path packsDir, ResourceType type, SymlinkFinder symlinkFinder) {
        this.packsDir = packsDir;
        this.type = type;
        this.symlinkFinder = symlinkFinder;
    }

    public ResourceType getType() {
        return this.type;
    }

    public static Path resolveDefaultPacksDir() {
        return FabricLoader.getInstance().getGameDir().resolve(EXTERNAL_PACKS_DIR_NAME);
    }

    public static List<String> listExternalPackFileNames(Path dir) {
        if (dir == null || !Files.isDirectory(dir, new LinkOption[0])) {
            return List.of();
        }
        ArrayList<String> names = new ArrayList<String>();
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
        List<String> discovered = discoveredNames == null ? List.of() : discoveredNames;
        if (discovered.isEmpty()) {
            return List.of();
        }
        LinkedHashSet<String> discoveredSet = new LinkedHashSet<String>(discovered);
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

    public void register(Consumer<ResourcePackProfile> profileAdder) {
        try {
            Files.createDirectories(this.packsDir);
            ArrayList<PackCandidate> discovered = new ArrayList<PackCandidate>();
            try (Stream<Path> paths = Files.list(this.packsDir);){
                paths.filter(NonPackRootResolver::isSupportedExternalPackPath).forEach(path -> NonPackRootResolver.resolve(path).filter(this::isSymlinkSafe).ifPresent(root -> discovered.add(new PackCandidate((Path)path, NonExternalPackProvider.createPackFactory(root)))));
            }
            List<String> discoveredNames = discovered.stream().map(PackCandidate::fileName).sorted(String.CASE_INSENSITIVE_ORDER).toList();
            List<String> normalizedOrder = NonExternalPackProvider.normalizeConfiguredOrder(NonExternalPackProvider.resolveConfiguredOrder(), discoveredNames);
            Map<String, Integer> indexByName = NonExternalPackProvider.indexByName(normalizedOrder);
            discovered.sort(Comparator.<PackCandidate>comparingInt(candidate -> indexByName.getOrDefault(candidate.fileName(), Integer.MAX_VALUE)).thenComparing(PackCandidate::fileName, String.CASE_INSENSITIVE_ORDER));
            for (int i = discovered.size() - 1; i >= 0; --i) {
                PackCandidate candidate2 = (PackCandidate)discovered.get(i);
                int rank = indexByName.getOrDefault(candidate2.fileName(), Integer.MAX_VALUE);
                String id = this.createPackId(candidate2.path(), rank);
                Text displayName = Text.literal(candidate2.fileName());
                ResourcePackProfile profile = ResourcePackProfile.create(id, displayName, true,
                        candidate2.packFactory(), this.type,
                        ResourcePackProfile.InsertionPosition.TOP, NON_SOURCE);
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
                    this.symlinkFinder.validate(root.sourcePath(), entries);
                }
            } else {
                entries.addAll(this.symlinkFinder.collect(root.rootPath(), true));
            }
        }
        catch (IOException e) {
            LOGGER.warn("Failed to validate NoN pack symlinks in {}, ignoring", (Object)root.sourcePath(), (Object)e);
            return false;
        }
        if (!entries.isEmpty()) {
            LOGGER.warn("Ignoring potential NoN pack entry: {}", (Object)SymlinkValidationException.getMessage((Path)root.sourcePath(), entries));
            return false;
        }
        return true;
    }

    private static ResourcePackProfile.PackFactory createPackFactory(NonPackRootResolver.ResolvedRoot root) {
        if (root.isZip()) {
            return new NonPrefixedZipResourcePack.Factory(root.sourcePath(), root.zipPrefix());
        }
        return name -> new DirectoryResourcePack(name, root.rootPath(), false);
    }

    private String createPackId(Path path, int rank) {
        String fileName = path.getFileName().toString();
        String channel = this.type == ResourceType.CLIENT_RESOURCES ? "client" : "server";
        String safeFileName = NonExternalPackProvider.sanitizeForPackId(fileName);
        String orderPrefix = NonExternalPackProvider.formatOrderPrefix(rank);
        String id = "needsofnature/" + channel + "/" + orderPrefix + "_" + safeFileName;
        return id;
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

    private record PackCandidate(Path path, ResourcePackProfile.PackFactory packFactory) {
        private String fileName() {
            Path fileName = this.path.getFileName();
            return fileName == null ? this.path.toString() : fileName.toString();
        }
    }
}

