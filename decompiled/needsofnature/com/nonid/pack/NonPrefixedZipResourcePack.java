/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2960
 *  net.minecraft.class_3255
 *  net.minecraft.class_3262
 *  net.minecraft.class_3262$class_7664
 *  net.minecraft.class_3264
 *  net.minecraft.class_3288$class_7679
 *  net.minecraft.class_3288$class_7680
 *  net.minecraft.class_7367
 *  net.minecraft.class_9224
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
import net.minecraft.class_2960;
import net.minecraft.class_3255;
import net.minecraft.class_3262;
import net.minecraft.class_3264;
import net.minecraft.class_3288;
import net.minecraft.class_7367;
import net.minecraft.class_9224;

final class NonPrefixedZipResourcePack
extends class_3255 {
    private final ZipFile zipFile;
    private final String rootPrefix;

    private NonPrefixedZipResourcePack(class_9224 info, Path zipPath, String rootPrefix) throws IOException {
        super(info);
        this.zipFile = new ZipFile(zipPath.toFile());
        this.rootPrefix = rootPrefix == null || rootPrefix.isBlank() ? "" : rootPrefix;
    }

    public class_7367<InputStream> method_14410(String ... segments) {
        String relative = NonPrefixedZipResourcePack.joinRootSegments(segments);
        if (relative == null) {
            return null;
        }
        ZipEntry entry = this.zipFile.getEntry(this.rootPrefix + relative);
        return entry == null || entry.isDirectory() ? null : class_7367.create((ZipFile)this.zipFile, (ZipEntry)entry);
    }

    public class_7367<InputStream> method_14405(class_3264 type, class_2960 id) {
        if (type == null || id == null) {
            return null;
        }
        String path = this.rootPrefix + type.method_14413() + "/" + id.method_12836() + "/" + id.method_12832();
        ZipEntry entry = this.zipFile.getEntry(path);
        return entry == null || entry.isDirectory() ? null : class_7367.create((ZipFile)this.zipFile, (ZipEntry)entry);
    }

    public void method_14408(class_3264 type, String namespace, String prefix, class_3262.class_7664 consumer) {
        if (type == null || namespace == null || prefix == null || consumer == null) {
            return;
        }
        String scanPrefix = this.rootPrefix + type.method_14413() + "/" + namespace + "/" + prefix;
        Enumeration<? extends ZipEntry> entries = this.zipFile.entries();
        while (entries.hasMoreElements()) {
            class_2960 id;
            String idPath;
            String normalized;
            ZipEntry entry = entries.nextElement();
            if (entry == null || entry.isDirectory() || (normalized = NonPackRootResolver.normalizeZipEntryName(entry.getName())) == null || !normalized.startsWith(scanPrefix) || (idPath = normalized.substring((this.rootPrefix + type.method_14413() + "/" + namespace + "/").length())).isBlank() || (id = class_2960.method_43902((String)namespace, (String)idPath)) == null) continue;
            consumer.accept((Object)id, (Object)class_7367.create((ZipFile)this.zipFile, (ZipEntry)entry));
        }
    }

    public Set<String> method_14406(class_3264 type) {
        if (type == null) {
            return Set.of();
        }
        String typePrefix = this.rootPrefix + type.method_14413() + "/";
        LinkedHashSet<String> namespaces = new LinkedHashSet<String>();
        Enumeration<? extends ZipEntry> entries = this.zipFile.entries();
        while (entries.hasMoreElements()) {
            String namespace;
            int namespaceStart;
            int namespaceEnd;
            String normalized;
            ZipEntry entry = entries.nextElement();
            if (entry == null || entry.isDirectory() || (normalized = NonPackRootResolver.normalizeZipEntryName(entry.getName())) == null || !normalized.startsWith(typePrefix) || (namespaceEnd = normalized.indexOf(47, namespaceStart = typePrefix.length())) <= namespaceStart || !class_2960.method_20209((String)(namespace = normalized.substring(namespaceStart, namespaceEnd)))) continue;
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
    implements class_3288.class_7680 {
        private final Path zipPath;
        private final String rootPrefix;

        Factory(Path zipPath, String rootPrefix) {
            this.zipPath = zipPath;
            this.rootPrefix = rootPrefix;
        }

        public class_3262 method_52424(class_9224 info) {
            try {
                return new NonPrefixedZipResourcePack(info, this.zipPath, this.rootPrefix);
            }
            catch (IOException e) {
                throw new RuntimeException("Failed to open prefixed NoN pack " + String.valueOf(this.zipPath), e);
            }
        }

        public class_3262 method_52425(class_9224 info, class_3288.class_7679 metadata) {
            return this.method_52424(info);
        }
    }
}

