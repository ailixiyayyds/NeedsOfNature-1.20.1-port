/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resource.PackVersion
 *  net.minecraft.text.Text
 *  net.minecraft.util.Identifier
 *  net.minecraft.resource.AbstractFileResourcePack
 *  net.minecraft.resource.ResourcePack
 *  net.minecraft.resource.ResourcePack$ResultConsumer
 *  net.minecraft.resource.ResourceType
 *  net.minecraft.resource.metadata.PackResourceMetadata
 *  net.minecraft.resource.ResourcePackProfile
 *  net.minecraft.resource.ResourcePackProfile$InsertionPosition
 *  net.minecraft.resource.ResourcePackProfile$Metadata
 *  net.minecraft.resource.ResourcePackProfile$PackFactory
 *  net.minecraft.resource.ResourcePackSource
 *  net.minecraft.util.dynamic.Range
 *  net.minecraft.resource.InputSupplier
 *  net.minecraft.resource.metadata.ResourceMetadataSerializer
 *  net.minecraft.resource.ResourcePackInfo
 *  net.minecraft.resource.ResourcePackPosition
 */
package com.nonid.pack;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.minecraft.resource.PackVersion;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.resource.AbstractFileResourcePack;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.metadata.PackResourceMetadata;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.util.dynamic.Range;
import net.minecraft.resource.InputSupplier;
import net.minecraft.resource.metadata.ResourceMetadataSerializer;
import net.minecraft.resource.ResourcePackInfo;
import net.minecraft.resource.ResourcePackPosition;

final class NonGeneratedInMemoryPack {
    private static final ResourcePackSource NON_SOURCE = ResourcePackSource.create(name -> Text.literal((String)"[NoN] ").append(name), (boolean)true);
    private static final ResourcePackPosition ALWAYS_ENABLED_TOP = new ResourcePackPosition(true, ResourcePackProfile.InsertionPosition.TOP, false);

    private NonGeneratedInMemoryPack() {
    }

    static ResourcePackProfile createProfile(String id, Text displayName, Text description, ResourceType type, Map<Identifier, byte[]> resources) {
        if (resources == null || resources.isEmpty()) {
            return null;
        }
        ResourcePackInfo info = new ResourcePackInfo(id, displayName, NON_SOURCE, Optional.empty());
        return ResourcePackProfile.create((ResourcePackInfo)info, (ResourcePackProfile.PackFactory)new GeneratedPackFactory(type, description, resources), (ResourceType)type, (ResourcePackPosition)ALWAYS_ENABLED_TOP);
    }

    private static final class GeneratedPackFactory
    implements ResourcePackProfile.PackFactory {
        private final ResourceType type;
        private final Text description;
        private final Map<Identifier, byte[]> resources;

        private GeneratedPackFactory(ResourceType type, Text description, Map<Identifier, byte[]> resources) {
            this.type = type;
            this.description = description;
            this.resources = Map.copyOf(resources);
        }

        public ResourcePack open(ResourcePackInfo info) {
            return new GeneratedResourcePack(info, this.type, this.description, this.resources);
        }

        public ResourcePack openWithOverlays(ResourcePackInfo info, ResourcePackProfile.Metadata metadata) {
            return this.open(info);
        }
    }

    private static final class GeneratedResourcePack
    extends AbstractFileResourcePack {
        private final ResourceType type;
        private final Map<Identifier, byte[]> resources;
        private final Set<String> namespaces;
        private final PackResourceMetadata metadata;
        private final byte[] packMcmeta;

        private GeneratedResourcePack(ResourcePackInfo info, ResourceType type, Text description, Map<Identifier, byte[]> resources) {
            super(info);
            this.type = type;
            this.resources = resources;
            this.namespaces = GeneratedResourcePack.namespaces(resources);
            this.metadata = new PackResourceMetadata(description, new Range((Comparable)PackVersion.of((int)75)));
            this.packMcmeta = ("{\"pack\":{\"description\":\"" + GeneratedResourcePack.escapeJson(description.getString()) + "\",\"pack_format\":75}}").getBytes(StandardCharsets.UTF_8);
        }

        public InputSupplier<InputStream> openRoot(String ... segments) {
            if (segments.length == 1 && "pack.mcmeta".equals(segments[0])) {
                return () -> new ByteArrayInputStream(this.packMcmeta);
            }
            return null;
        }

        public InputSupplier<InputStream> open(ResourceType type, Identifier id) {
            if (type != this.type) {
                return null;
            }
            byte[] bytes = this.resources.get(id);
            return bytes == null ? null : () -> new ByteArrayInputStream(bytes);
        }

        public void findResources(ResourceType type, String namespace, String prefix, ResourcePack.ResultConsumer consumer) {
            if (type != this.type || consumer == null) {
                return;
            }
            for (Map.Entry<Identifier, byte[]> entry : this.resources.entrySet()) {
                Identifier id = entry.getKey();
                if (!id.getNamespace().equals(namespace) || !id.getPath().startsWith(prefix)) continue;
                byte[] bytes = entry.getValue();
                consumer.accept((Object)id, () -> new ByteArrayInputStream(bytes));
            }
        }

        public Set<String> getNamespaces(ResourceType type) {
            return type == this.type ? this.namespaces : Set.of();
        }

        public <T> T parseMetadata(ResourceMetadataSerializer<T> serializer) {
            if (serializer == PackResourceMetadata.getSerializerFor((ResourceType)this.type)) {
                return (T)this.metadata;
            }
            if (serializer == PackResourceMetadata.DESCRIPTION_SERIALIZER) {
                return (T)this.metadata.comp_1580();
            }
            return null;
        }

        public void close() {
        }

        private static Set<String> namespaces(Map<Identifier, byte[]> resources) {
            LinkedHashSet<String> out = new LinkedHashSet<String>();
            for (Identifier id : resources.keySet()) {
                out.add(id.getNamespace());
            }
            return out.isEmpty() ? Set.of() : Set.copyOf(out);
        }

        private static String escapeJson(String value) {
            if (value == null || value.isEmpty()) {
                return "";
            }
            return value.replace("\\", "\\\\").replace("\"", "\\\"");
        }
    }
}

