/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Identifier
 *  net.minecraft.resource.AbstractFileResourcePack
 *  net.minecraft.resource.ResourcePack
 *  net.minecraft.resource.ResourcePack$ResultConsumer
 *  net.minecraft.resource.ResourceType
 *  net.minecraft.resource.ResourcePackProfile$Metadata
 *  net.minecraft.resource.ResourcePackProfile$PackFactory
 *  net.minecraft.resource.InputSupplier
 *  net.minecraft.resource.ResourcePackInfo
 */
package com.nonid.pack;

import com.nonid.pack.NonPackRootResolver;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.minecraft.util.Identifier;
import net.minecraft.resource.AbstractFileResourcePack;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.InputSupplier;

final class NonPrefixedZipResourcePack
extends AbstractFileResourcePack {
    private final ZipFile zipFile;
    private final String rootPrefix;

    private NonPrefixedZipResourcePack(String name, Path zipPath, String rootPrefix) throws IOException {
        super(name, false);
        this.zipFile = new ZipFile(zipPath.toFile());
        this.rootPrefix = rootPrefix == null || rootPrefix.isBlank() ? "" : rootPrefix;
    }

    public InputSupplier<InputStream> openRoot(String ... segments) {
        String relative = NonPrefixedZipResourcePack.joinRootSegments(segments);
        if (relative == null) {
            return null;
        }
        ZipEntry entry = this.zipFile.getEntry(this.rootPrefix + relative);
        return entry == null || entry.isDirectory() ? null : InputSupplier.create((ZipFile)this.zipFile, (ZipEntry)entry);
    }

    public InputSupplier<InputStream> open(ResourceType type, Identifier id) {
        if (type == null || id == null) {
            return null;
        }
        String path = this.rootPrefix + type.getDirectory() + "/" + id.getNamespace() + "/" + id.getPath();
        ZipEntry entry = this.zipFile.getEntry(path);
        return entry == null || entry.isDirectory() ? null : InputSupplier.create((ZipFile)this.zipFile, (ZipEntry)entry);
    }

    public void findResources(ResourceType type, String namespace, String prefix, ResourcePack.ResultConsumer consumer) {
        if (type == null || namespace == null || prefix == null || consumer == null) {
            return;
        }
        String scanPrefix = this.rootPrefix + type.getDirectory() + "/" + namespace + "/" + prefix;
        Enumeration<? extends ZipEntry> entries = this.zipFile.entries();
        while (entries.hasMoreElements()) {
            Identifier id;
            String idPath;
            String normalized;
            ZipEntry entry = entries.nextElement();
            if (entry == null || entry.isDirectory() || (normalized = NonPackRootResolver.normalizeZipEntryName(entry.getName())) == null || !normalized.startsWith(scanPrefix) || (idPath = normalized.substring((this.rootPrefix + type.getDirectory() + "/" + namespace + "/").length())).isBlank() || (id = Identifier.tryParse(namespace + ":" + idPath)) == null) continue;
            consumer.accept(id, InputSupplier.create(this.zipFile, entry));
        }
    }

    public Set<String> getNamespaces(ResourceType type) {
        if (type == null) {
            return Set.of();
        }
        String typePrefix = this.rootPrefix + type.getDirectory() + "/";
        LinkedHashSet<String> namespaces = new LinkedHashSet<String>();
        Enumeration<? extends ZipEntry> entries = this.zipFile.entries();
        while (entries.hasMoreElements()) {
            String namespace;
            int namespaceStart;
            int namespaceEnd;
            String normalized;
            ZipEntry entry = entries.nextElement();
            if (entry == null || entry.isDirectory() || (normalized = NonPackRootResolver.normalizeZipEntryName(entry.getName())) == null || !normalized.startsWith(typePrefix) || (namespaceEnd = normalized.indexOf(47, namespaceStart = typePrefix.length())) <= namespaceStart || Identifier.tryParse((namespace = normalized.substring(namespaceStart, namespaceEnd)) + ":probe") == null) continue;
            namespaces.add(namespace);
        }
        return Set.copyOf(namespaces);
    }

    public void close() {
        try {
            this.zipFile.close();
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    private static String joinRootSegments(String ... segments) {
        if (segments == null || segments.length == 0) {
            return "";
        }
        StringBuilder out = new StringBuilder();
        for (String segment : segments) {
            if (segment == null || segment.isBlank()) {
                return null;
            }
            String normalized = segment.replace('\\', '/');
            if (normalized.contains("/") || normalized.equals(".") || normalized.equals("..")) {
                return null;
            }
            if (!out.isEmpty()) {
                out.append('/');
            }
            out.append(normalized);
        }
        return out.toString();
    }

    static final class Factory
    implements ResourcePackProfile.PackFactory {
        private final Path zipPath;
        private final String rootPrefix;

        Factory(Path zipPath, String rootPrefix) {
            this.zipPath = zipPath;
            this.rootPrefix = rootPrefix;
        }

        public ResourcePack open(String name) {
            try {
                return new NonPrefixedZipResourcePack(name, this.zipPath, this.rootPrefix);
            }
            catch (IOException e) {
                throw new RuntimeException("Failed to open prefixed NoN pack " + String.valueOf(this.zipPath), e);
            }
        }

    }
}

