/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  net.minecraft.class_124
 *  net.minecraft.class_2561
 *  net.minecraft.class_310
 *  net.minecraft.class_437
 *  net.minecraft.class_442
 *  net.minecraft.class_5250
 *  org.jetbrains.annotations.Nullable
 */
package com.nonid.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nonid.NeedsOfNature;
import com.nonid.NonConfig;
import com.nonid.client.NonPackWarningScreen;
import com.nonid.pack.NonExternalPackProvider;
import com.nonid.pack.NonPackRootResolver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_437;
import net.minecraft.class_442;
import net.minecraft.class_5250;
import org.jetbrains.annotations.Nullable;

public final class NonPackStartupWarnings {
    private static final String WARNING_PATH = "assets/needsofnature/non_warnings.json";
    private static boolean checkedThisSession = false;

    private NonPackStartupWarnings() {
    }

    public static void clientTick(class_310 client) {
        if (client == null || checkedThisSession) {
            return;
        }
        if (client.method_18506() != null) {
            return;
        }
        if (!(client.field_1755 instanceof class_442)) {
            return;
        }
        checkedThisSession = true;
        Warning warning = NonPackStartupWarnings.findHighestPriorityWarning();
        if (warning == null || NonPackStartupWarnings.isAcknowledged(warning.id())) {
            return;
        }
        client.method_1507((class_437)new NonPackWarningScreen(client.field_1755, warning));
    }

    public static void acknowledge(String warningId) {
        if (warningId == null || warningId.isBlank()) {
            return;
        }
        NonConfig config = NeedsOfNature.getConfig();
        if (config != null && config.acknowledgeStartupWarning(warningId)) {
            config.save();
        }
    }

    @Nullable
    private static Warning findHighestPriorityWarning() {
        Path packsDir = NonExternalPackProvider.resolveDefaultPacksDir();
        List<String> discovered = NonExternalPackProvider.listExternalPackFileNames(packsDir);
        if (discovered.isEmpty()) {
            return null;
        }
        NonConfig config = NeedsOfNature.getConfig();
        List<String> configuredOrder = config == null ? List.of() : config.getExternalNoNPackLoadOrder();
        List<String> ordered = NonExternalPackProvider.normalizeConfiguredOrder(configuredOrder, discovered);
        for (String packName : ordered) {
            Warning warning = NonPackStartupWarnings.readWarning(packsDir.resolve(packName), packName);
            if (warning == null) continue;
            return warning;
        }
        return null;
    }

    /*
     * Enabled aggressive exception aggregation
     */
    @Nullable
    private static Warning readWarning(Path packPath, String packName) {
        if (packPath == null || packName == null) {
            return null;
        }
        try {
            Optional<NonPackRootResolver.ResolvedRoot> resolved = NonPackRootResolver.resolve(packPath);
            if (resolved.isEmpty()) {
                return null;
            }
            NonPackRootResolver.ResolvedRoot root = resolved.get();
            if (!root.isZip()) {
                Path warningPath = root.rootPath().resolve(WARNING_PATH.replace("/", root.rootPath().getFileSystem().getSeparator()));
                if (!Files.isRegularFile(warningPath, new LinkOption[0])) {
                    return null;
                }
                try (BufferedReader reader = Files.newBufferedReader(warningPath, StandardCharsets.UTF_8);){
                    Warning warning = NonPackStartupWarnings.parseWarning(reader, packName);
                    return warning;
                }
            }
            try (ZipFile zip = new ZipFile(packPath.toFile());){
                Warning warning;
                ZipEntry entry = zip.getEntry(root.prefixPath(WARNING_PATH));
                if (entry == null || entry.isDirectory()) {
                    Warning warning2 = null;
                    return warning2;
                }
                try (InputStreamReader reader = new InputStreamReader(zip.getInputStream(entry), StandardCharsets.UTF_8);){
                    warning = NonPackStartupWarnings.parseWarning(reader, packName);
                }
                return warning;
            }
        }
        catch (IOException | RuntimeException e) {
            NeedsOfNature.LOGGER.warn("[NoN] Failed to load startup warning from pack {}.", (Object)packName, (Object)e);
            return null;
        }
    }

    @Nullable
    private static Warning parseWarning(Reader reader, String packName) {
        JsonElement root = JsonParser.parseReader((Reader)reader);
        if (!root.isJsonObject()) {
            NonPackStartupWarnings.warnInvalid(packName, "root must be an object");
            return null;
        }
        JsonObject obj = root.getAsJsonObject();
        String id = NonPackStartupWarnings.stringOrNull(obj, "id");
        if (id == null || id.isBlank()) {
            NonPackStartupWarnings.warnInvalid(packName, "missing id");
            return null;
        }
        class_2561 title = NonPackStartupWarnings.readText(obj, "title", "title_key");
        class_2561 body = NonPackStartupWarnings.readText(obj, "text", "text_key");
        if (title == null || body == null) {
            NonPackStartupWarnings.warnInvalid(packName, "missing title/text or title_key/text_key");
            return null;
        }
        return new Warning(packName, id.trim(), title, body);
    }

    @Nullable
    private static class_2561 readText(JsonObject obj, String literalKey, String translationKey) {
        String key = NonPackStartupWarnings.stringOrNull(obj, translationKey);
        if (key != null && !key.isBlank()) {
            return class_2561.method_43471((String)key.trim());
        }
        String literal = NonPackStartupWarnings.stringOrNull(obj, literalKey);
        if (literal == null || literal.isBlank()) {
            return null;
        }
        return NonPackStartupWarnings.parseFormattedLiteral(literal);
    }

    private static class_5250 parseFormattedLiteral(String raw) {
        class_5250 out = class_2561.method_43473();
        ArrayList<class_124> active = new ArrayList<class_124>();
        StringBuilder segment = new StringBuilder();
        for (int i = 0; i < raw.length(); ++i) {
            char c = raw.charAt(i);
            if (c != '&' || i + 1 >= raw.length()) {
                segment.append(c);
                continue;
            }
            class_124 formatting = class_124.method_544((char)raw.charAt(i + 1));
            if (formatting == null) {
                segment.append(c);
                continue;
            }
            NonPackStartupWarnings.appendSegment(out, segment, active);
            ++i;
            if (formatting == class_124.field_1070) {
                active.clear();
                continue;
            }
            if (formatting.method_543()) {
                active.removeIf(class_124::method_543);
                active.add(formatting);
                continue;
            }
            if (active.contains(formatting)) continue;
            active.add(formatting);
        }
        NonPackStartupWarnings.appendSegment(out, segment, active);
        return out;
    }

    private static void appendSegment(class_5250 out, StringBuilder segment, List<class_124> active) {
        if (segment.isEmpty()) {
            return;
        }
        class_5250 text = class_2561.method_43470((String)segment.toString());
        if (!active.isEmpty()) {
            text.method_27695((class_124[])active.toArray(class_124[]::new));
        }
        out.method_10852((class_2561)text);
        segment.setLength(0);
    }

    @Nullable
    private static String stringOrNull(JsonObject obj, String key) {
        if (obj == null || key == null || !obj.has(key)) {
            return null;
        }
        JsonElement element = obj.get(key);
        if (element == null || !element.isJsonPrimitive()) {
            return null;
        }
        try {
            return element.getAsString();
        }
        catch (RuntimeException e) {
            return null;
        }
    }

    private static boolean isAcknowledged(String warningId) {
        NonConfig config = NeedsOfNature.getConfig();
        return config != null && config.isStartupWarningAcknowledged(warningId);
    }

    private static void warnInvalid(String packName, String reason) {
        NeedsOfNature.LOGGER.warn("[NoN] Ignoring startup warning in pack {}: {}.", (Object)packName, (Object)reason);
    }

    public record Warning(String packName, String id, class_2561 title, class_2561 body) {
    }
}

