/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid.pack;

import com.nonid.NeedsOfNature;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.jetbrains.annotations.Nullable;

public final class NonPackRootResolver {
    private static final Set<String> WARNED_NESTED_PACKS = ConcurrentHashMap.newKeySet();
    private static final Set<String> WARNED_INVALID_PACKS = ConcurrentHashMap.newKeySet();

    private NonPackRootResolver() {
    }

    public static boolean isSupportedExternalPackPath(Path path) {
        if (path == null) {
            return false;
        }
        if (Files.isDirectory(path, new LinkOption[0])) {
            return true;
        }
        Path fileName = path.getFileName();
        if (fileName == null) {
            return false;
        }
        return fileName.toString().toLowerCase(Locale.ROOT).endsWith(".zip");
    }

    public static Optional<ResolvedRoot> resolve(Path path) {
        return NonPackRootResolver.resolve(path, true);
    }

    public static Optional<ResolvedRoot> resolve(Path path, boolean warn) {
        String lowerName;
        if (path == null || !Files.exists(path, new LinkOption[0])) {
            return Optional.empty();
        }
        if (Files.isDirectory(path, new LinkOption[0])) {
            return NonPackRootResolver.resolveDirectory(path, warn);
        }
        Path fileName = path.getFileName();
        String string = lowerName = fileName == null ? "" : fileName.toString().toLowerCase(Locale.ROOT);
        if (!lowerName.endsWith(".zip")) {
            return Optional.empty();
        }
        return NonPackRootResolver.resolveZip(path, warn);
    }

    private static Optional<ResolvedRoot> resolveDirectory(Path path, boolean warn) {
        if (NonPackRootResolver.isPackRootDirectory(path)) {
            return Optional.of(ResolvedRoot.directory(path, path, "", null));
        }
        LinkedHashSet candidates = new LinkedHashSet();
        try (Stream<Path> entries = Files.list(path);){
            entries.filter(x$0 -> Files.isDirectory(x$0, new LinkOption[0])).filter(NonPackRootResolver::isPackRootDirectory).forEach(candidates::add);
        }
        catch (IOException e) {
            if (warn) {
                NonPackRootResolver.warnInvalid(path, "failed to inspect directory pack root");
            }
            return Optional.empty();
        }
        if (candidates.size() == 1) {
            Path nestedRoot = (Path)candidates.iterator().next();
            String folder = NonPackRootResolver.fileName(nestedRoot);
            if (warn) {
                NonPackRootResolver.warnNested(path, folder);
            }
            return Optional.of(ResolvedRoot.directory(path, nestedRoot, "", folder));
        }
        if (warn) {
            if (candidates.size() > 1) {
                NonPackRootResolver.warnInvalid(path, "contains multiple one-level nested pack roots");
            } else {
                NonPackRootResolver.warnInvalid(path, "does not contain assets/, data/, or pack.mcmeta at the root or inside one wrapper folder");
            }
        }
        return Optional.empty();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static Optional<ResolvedRoot> resolveZip(Path path, boolean warn) {
        try (ZipFile zip = new ZipFile(path.toFile());){
            Object normalized;
            boolean rootMatches = false;
            LinkedHashSet<String> candidates = new LinkedHashSet<String>();
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                normalized = NonPackRootResolver.normalizeZipEntryName(entries.nextElement().getName());
                if (normalized == null || ((String)normalized).isBlank()) continue;
                if (NonPackRootResolver.isPackRootEntry((String)normalized)) {
                    rootMatches = true;
                    break;
                }
                int slash = ((String)normalized).indexOf(47);
                if (slash <= 0 || slash >= ((String)normalized).length() - 1) continue;
                String top = ((String)normalized).substring(0, slash);
                String nested = ((String)normalized).substring(slash + 1);
                if (NonPackRootResolver.isIgnoredTopLevelFolder(top) || !NonPackRootResolver.isPackRootEntry(nested)) continue;
                candidates.add(top);
            }
            if (rootMatches) {
                return Optional.of(ResolvedRoot.zip(path, "", null));
            }
            if (candidates.size() == 1) {
                String folder = (String)candidates.iterator().next();
                if (warn) {
                    NonPackRootResolver.warnNested(path, folder);
                }
                Optional<ResolvedRoot> optional = Optional.of(ResolvedRoot.zip(path, folder + "/", folder));
                return optional;
            }
            if (!warn) return Optional.empty();
            if (candidates.size() > 1) {
                NonPackRootResolver.warnInvalid(path, "contains multiple one-level nested pack roots");
                return Optional.empty();
            }
            NonPackRootResolver.warnInvalid(path, "does not contain assets/, data/, or pack.mcmeta at the root or inside one wrapper folder");
            return Optional.empty();
        }
        catch (IOException e) {
            if (!warn) return Optional.empty();
            NonPackRootResolver.warnInvalid(path, "failed to inspect zip pack root");
        }
        return Optional.empty();
    }

    private static boolean isPackRootDirectory(Path path) {
        return Files.isRegularFile(path.resolve("pack.mcmeta"), new LinkOption[0]) || Files.isDirectory(path.resolve("assets"), new LinkOption[0]) || Files.isDirectory(path.resolve("data"), new LinkOption[0]);
    }

    private static boolean isPackRootEntry(String entryPath) {
        if (entryPath == null || entryPath.isBlank()) {
            return false;
        }
        return "pack.mcmeta".equals(entryPath) || entryPath.startsWith("assets/") || entryPath.startsWith("data/");
    }

    private static boolean isIgnoredTopLevelFolder(String folder) {
        return "__MACOSX".equals(folder) || folder.startsWith(".");
    }

    @Nullable
    public static String normalizeZipEntryName(@Nullable String raw) {
        if (raw == null) {
            return null;
        }
        String normalized = raw.replace('\\', '/');
        while (normalized.startsWith("/")) {
            normalized = normalized.substring(1);
        }
        if (normalized.contains("../") || normalized.equals("..") || normalized.startsWith("../")) {
            return null;
        }
        return normalized;
    }

    private static void warnNested(Path path, String folder) {
        String key = NonPackRootResolver.normalizedKey(path) + "|" + folder;
        if (!WARNED_NESTED_PACKS.add(key)) {
            return;
        }
        NeedsOfNature.logSetupWarning("[NoN] External pack '{}' has its pack contents inside the folder '{}'. NoN will load it, but released packs should place assets/, data/, and pack.mcmeta directly at the pack root.", NonPackRootResolver.fileName(path), folder);
    }

    private static void warnInvalid(Path path, String reason) {
        String key = NonPackRootResolver.normalizedKey(path) + "|" + reason;
        if (!WARNED_INVALID_PACKS.add(key)) {
            return;
        }
        NeedsOfNature.logSetupWarning("[NoN] Ignoring external NoN pack '{}': {}.", NonPackRootResolver.fileName(path), reason);
    }

    private static String normalizedKey(Path path) {
        if (path == null) {
            return "<null>";
        }
        return path.toAbsolutePath().normalize().toString();
    }

    private static String fileName(Path path) {
        if (path == null || path.getFileName() == null) {
            return String.valueOf(path);
        }
        return path.getFileName().toString();
    }

    public record ResolvedRoot(Path sourcePath, Path rootPath, String zipPrefix, @Nullable String wrapperFolder) {
        static ResolvedRoot directory(Path sourcePath, Path rootPath, String zipPrefix, @Nullable String wrapperFolder) {
            return new ResolvedRoot(sourcePath, rootPath, zipPrefix, wrapperFolder);
        }

        static ResolvedRoot zip(Path sourcePath, String zipPrefix, @Nullable String wrapperFolder) {
            return new ResolvedRoot(sourcePath, sourcePath, zipPrefix == null ? "" : zipPrefix, wrapperFolder);
        }

        public boolean isZip() {
            return Files.isRegularFile(this.sourcePath, new LinkOption[0]);
        }

        public boolean isNested() {
            return this.wrapperFolder != null && !this.wrapperFolder.isBlank();
        }

        public String prefixPath(String relativePath) {
            String relative;
            String string = relative = relativePath == null ? "" : relativePath.replace('\\', '/');
            if (this.zipPrefix == null || this.zipPrefix.isBlank()) {
                return relative;
            }
            return this.zipPrefix + relative;
        }

        @Nullable
        public String stripZipPrefix(String entryPath) {
            String normalized = NonPackRootResolver.normalizeZipEntryName(entryPath);
            if (normalized == null) {
                return null;
            }
            if (this.zipPrefix == null || this.zipPrefix.isBlank()) {
                return normalized;
            }
            if (!normalized.startsWith(this.zipPrefix)) {
                return null;
            }
            return normalized.substring(this.zipPrefix.length());
        }
    }
}

