/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_11555
 *  net.minecraft.class_2561
 *  net.minecraft.class_2960
 *  net.minecraft.class_3255
 *  net.minecraft.class_3262
 *  net.minecraft.class_3262$class_7664
 *  net.minecraft.class_3264
 *  net.minecraft.class_3272
 *  net.minecraft.class_3288
 *  net.minecraft.class_3288$class_3289
 *  net.minecraft.class_3288$class_7679
 *  net.minecraft.class_3288$class_7680
 *  net.minecraft.class_5352
 *  net.minecraft.class_6497
 *  net.minecraft.class_7367
 *  net.minecraft.class_7677
 *  net.minecraft.class_9224
 *  net.minecraft.class_9225
 */
package com.nonid.pack;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.minecraft.class_11555;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_3255;
import net.minecraft.class_3262;
import net.minecraft.class_3264;
import net.minecraft.class_3272;
import net.minecraft.class_3288;
import net.minecraft.class_5352;
import net.minecraft.class_6497;
import net.minecraft.class_7367;
import net.minecraft.class_7677;
import net.minecraft.class_9224;
import net.minecraft.class_9225;

final class NonGeneratedInMemoryPack {
    private static final class_5352 NON_SOURCE = class_5352.method_45281(name -> class_2561.method_43470((String)"[NoN] ").method_10852(name), (boolean)true);
    private static final class_9225 ALWAYS_ENABLED_TOP = new class_9225(true, class_3288.class_3289.field_14280, false);

    private NonGeneratedInMemoryPack() {
    }

    static class_3288 createProfile(String id, class_2561 displayName, class_2561 description, class_3264 type, Map<class_2960, byte[]> resources) {
        if (resources == null || resources.isEmpty()) {
            return null;
        }
        class_9224 info = new class_9224(id, displayName, NON_SOURCE, Optional.empty());
        return class_3288.method_45275((class_9224)info, (class_3288.class_7680)new GeneratedPackFactory(type, description, resources), (class_3264)type, (class_9225)ALWAYS_ENABLED_TOP);
    }

    private static final class GeneratedPackFactory
    implements class_3288.class_7680 {
        private final class_3264 type;
        private final class_2561 description;
        private final Map<class_2960, byte[]> resources;

        private GeneratedPackFactory(class_3264 type, class_2561 description, Map<class_2960, byte[]> resources) {
            this.type = type;
            this.description = description;
            this.resources = Map.copyOf(resources);
        }

        public class_3262 method_52424(class_9224 info) {
            return new GeneratedResourcePack(info, this.type, this.description, this.resources);
        }

        public class_3262 method_52425(class_9224 info, class_3288.class_7679 metadata) {
            return this.method_52424(info);
        }
    }

    private static final class GeneratedResourcePack
    extends class_3255 {
        private final class_3264 type;
        private final Map<class_2960, byte[]> resources;
        private final Set<String> namespaces;
        private final class_3272 metadata;
        private final byte[] packMcmeta;

        private GeneratedResourcePack(class_9224 info, class_3264 type, class_2561 description, Map<class_2960, byte[]> resources) {
            super(info);
            this.type = type;
            this.resources = resources;
            this.namespaces = GeneratedResourcePack.namespaces(resources);
            this.metadata = new class_3272(description, new class_6497((Comparable)class_11555.method_72319((int)75)));
            this.packMcmeta = ("{\"pack\":{\"description\":\"" + GeneratedResourcePack.escapeJson(description.getString()) + "\",\"pack_format\":75}}").getBytes(StandardCharsets.UTF_8);
        }

        public class_7367<InputStream> method_14410(String ... segments) {
            if (segments.length == 1 && "pack.mcmeta".equals(segments[0])) {
                return () -> new ByteArrayInputStream(this.packMcmeta);
            }
            return null;
        }

        public class_7367<InputStream> method_14405(class_3264 type, class_2960 id) {
            if (type != this.type) {
                return null;
            }
            byte[] bytes = this.resources.get(id);
            return bytes == null ? null : () -> new ByteArrayInputStream(bytes);
        }

        public void method_14408(class_3264 type, String namespace, String prefix, class_3262.class_7664 consumer) {
            if (type != this.type || consumer == null) {
                return;
            }
            for (Map.Entry<class_2960, byte[]> entry : this.resources.entrySet()) {
                class_2960 id = entry.getKey();
                if (!id.method_12836().equals(namespace) || !id.method_12832().startsWith(prefix)) continue;
                byte[] bytes = entry.getValue();
                consumer.accept((Object)id, () -> new ByteArrayInputStream(bytes));
            }
        }

        public Set<String> method_14406(class_3264 type) {
            return type == this.type ? this.namespaces : Set.of();
        }

        public <T> T method_14407(class_7677<T> serializer) {
            if (serializer == class_3272.method_72356((class_3264)this.type)) {
                return (T)this.metadata;
            }
            if (serializer == class_3272.field_61157) {
                return (T)this.metadata.comp_1580();
            }
            return null;
        }

        public void close() {
        }

        private static Set<String> namespaces(Map<class_2960, byte[]> resources) {
            LinkedHashSet<String> out = new LinkedHashSet<String>();
            for (class_2960 id : resources.keySet()) {
                out.add(id.method_12836());
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

