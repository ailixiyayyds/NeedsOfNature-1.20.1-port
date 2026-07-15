/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  com.google.gson.JsonPrimitive
 *  net.fabricmc.fabric.api.resource.v1.ResourceLoader
 *  net.minecraft.util.Formatting
 *  net.minecraft.text.Text
 *  net.minecraft.util.Identifier
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.resource.ResourceType
 *  net.minecraft.resource.Resource
 *  net.minecraft.resource.ResourceManager
 *  net.minecraft.resource.ResourceReloader
 *  net.minecraft.resource.SynchronousResourceReloader
 *  org.jetbrains.annotations.Nullable
 */
package com.afwid.client.diagnostics;

import com.afwid.AfwDebugChatCategory;
import com.afwid.AnimationFramework;
import com.afwid.client.config.AfwClientConfig;
import com.afwid.client.render.gecko.AfwGeckoResourceResolver;
import com.afwid.data.AfwAnimationDefinitions;
import com.afwid.network.AnimationStageInfo;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.util.Formatting;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.resource.SynchronousResourceReloader;
import org.jetbrains.annotations.Nullable;

public final class AfwAnimationAssetDiagnostics {
    private static final Identifier RELOADER_ID = Identifier.of((String)"animationframework", (String)"afw_animation_asset_diagnostics");
    private static final double LENGTH_WARNING_TOLERANCE_SECONDS = 0.05;
    private static volatile Map<Identifier, AnimationAssetLength> LENGTHS_BY_RESOURCE = Map.of();
    private static final Set<String> WARNED_MISMATCHES = new LinkedHashSet<String>();
    private static long reloadRevision = 0L;
    private static String lastDefinitionValidationKey = "";

    private AfwAnimationAssetDiagnostics() {
    }

    public static void registerReloadListener() {
        ResourceLoader.get((ResourceType)ResourceType.CLIENT_RESOURCES).registerReloader(RELOADER_ID, (ResourceReloader)new Reloader());
    }

    public static void validateLoadedDefinitionsOnce() {
        List<AfwAnimationDefinitions.Definition> definitions = AfwAnimationDefinitions.getLoadedDefinitionsSnapshot();
        if (definitions.isEmpty() || LENGTHS_BY_RESOURCE.isEmpty()) {
            return;
        }
        String validationKey = AfwAnimationAssetDiagnostics.buildDefinitionValidationKey(definitions);
        if (validationKey.equals(lastDefinitionValidationKey)) {
            return;
        }
        lastDefinitionValidationKey = validationKey;
        for (AfwAnimationDefinitions.Definition definition : definitions) {
            AfwAnimationAssetDiagnostics.validateDefinition(definition);
        }
    }

    public static void validateStartPayload(Identifier animationId, List<String> actorKeys, List<AnimationStageInfo> stages) {
        if (animationId == null || stages == null || stages.isEmpty() || LENGTHS_BY_RESOURCE.isEmpty()) {
            return;
        }
        List<Object> safeActorKeys = actorKeys == null ? List.of() : actorKeys;
        for (AnimationStageInfo stage : stages) {
            if (stage == null) continue;
            if (safeActorKeys.isEmpty()) {
                AfwAnimationAssetDiagnostics.validateStageResource(animationId, stage, null, List.of());
                continue;
            }
            for (String string : safeActorKeys) {
                AfwAnimationAssetDiagnostics.validateStageResource(animationId, stage, string, List.of());
            }
        }
    }

    private static void validateDefinition(AfwAnimationDefinitions.Definition definition) {
        if (definition == null || definition.stages().isEmpty()) {
            return;
        }
        List<String> actorKeys = AfwAnimationDefinitions.resolveActorKeys(definition);
        List<AfwAnimationDefinitions.ActorConstraint> actors = definition.actors();
        for (AnimationStageInfo stage : definition.stages()) {
            if (stage == null) continue;
            int count = Math.max(actorKeys.size(), actors.size());
            if (count == 0) {
                AfwAnimationAssetDiagnostics.validateStageResource(definition.id(), stage, null, List.of());
                continue;
            }
            for (int i = 0; i < count; ++i) {
                String actorKey = i < actorKeys.size() ? actorKeys.get(i) : null;
                AfwAnimationDefinitions.ActorConstraint actor = i < actors.size() ? actors.get(i) : null;
                List<Identifier> entityTypes = actor == null ? List.of() : AfwAnimationAssetDiagnostics.sortedEntityTypes(actor.entityTypes());
                AfwAnimationAssetDiagnostics.validateStageResource(definition.id(), stage, actorKey, entityTypes);
            }
        }
    }

    private static void validateStageResource(Identifier definitionId, AnimationStageInfo stage, @Nullable String actorKey, List<Identifier> entityTypes) {
        Identifier actorResource;
        if (definitionId == null || stage == null || stage.lengthTicks() <= 0L) {
            return;
        }
        Identifier playbackId = stage.effectiveAnimationId();
        if (playbackId == null) {
            return;
        }
        Identifier class_29602 = actorResource = actorKey == null || actorKey.isBlank() ? null : AfwGeckoResourceResolver.toAnimationResource(playbackId, actorKey);
        if (AfwAnimationAssetDiagnostics.validateIfPresent(definitionId, stage, actorKey, actorResource)) {
            return;
        }
        if (entityTypes != null && !entityTypes.isEmpty()) {
            boolean checkedAnyType = false;
            for (Identifier entityType : entityTypes) {
                if (entityType == null) continue;
                Identifier typeResource = AfwGeckoResourceResolver.toAnimationResource(playbackId, entityType.getPath());
                checkedAnyType |= AfwAnimationAssetDiagnostics.validateIfPresent(definitionId, stage, actorKey, typeResource);
                String namespacedKey = entityType.getNamespace() + "_" + entityType.getPath();
                if (namespacedKey.equals(entityType.getPath())) continue;
                Identifier namespacedResource = AfwGeckoResourceResolver.toAnimationResource(playbackId, namespacedKey);
                checkedAnyType |= AfwAnimationAssetDiagnostics.validateIfPresent(definitionId, stage, actorKey, namespacedResource);
            }
            if (checkedAnyType) {
                return;
            }
        }
        Identifier baseResource = AfwGeckoResourceResolver.toAnimationResource(playbackId);
        AfwAnimationAssetDiagnostics.validateIfPresent(definitionId, stage, actorKey, baseResource);
    }

    private static boolean validateIfPresent(Identifier definitionId, AnimationStageInfo stage, @Nullable String actorKey, @Nullable Identifier animationResource) {
        if (animationResource == null) {
            return false;
        }
        AnimationAssetLength asset = LENGTHS_BY_RESOURCE.get(animationResource);
        if (asset == null || asset.lengthSeconds() <= 0.0) {
            return false;
        }
        double stageSeconds = AfwAnimationAssetDiagnostics.stageCycleSeconds(stage);
        double delta = Math.abs(stageSeconds - asset.lengthSeconds());
        if (delta > 0.05) {
            AfwAnimationAssetDiagnostics.warnMismatch(definitionId, stage, actorKey, animationResource, asset, stageSeconds);
        }
        return true;
    }

    private static void warnMismatch(Identifier definitionId, AnimationStageInfo stage, @Nullable String actorKey, Identifier animationResource, AnimationAssetLength asset, double stageSeconds) {
        String key = String.valueOf(definitionId) + "|" + String.valueOf(stage.animationId()) + "|" + actorKey + "|" + String.valueOf(animationResource) + "|" + asset.animationKey();
        if (!WARNED_MISMATCHES.add(key)) {
            return;
        }
        String actorText = actorKey == null || actorKey.isBlank() ? "base" : "actor '" + actorKey + "'";
        String playbackText = stage.animationId().equals((Object)stage.effectiveAnimationId()) ? "" : " (plays " + stage.effectiveAnimationId().getPath() + ")";
        String message = String.format(Locale.ROOT, "%s stage %s%s declares cycle_seconds=%ss, but %s animation %s key '%s' has animation_length=%ss. cycle_seconds should match the authored GeckoLib clip length; use stage speed to slow/accelerate playback.", definitionId, stage.animationId().getPath(), playbackText, AfwAnimationAssetDiagnostics.formatSeconds(stageSeconds), actorText, animationResource, asset.animationKey(), AfwAnimationAssetDiagnostics.formatSeconds(asset.lengthSeconds()));
        AnimationFramework.LOGGER.warn("[AFW] Setup warning: {}", (Object)message);
        AfwAnimationAssetDiagnostics.sendSetupChatWarning(message);
    }

    private static void sendSetupChatWarning(String message) {
        if (message == null || message.isBlank()) {
            return;
        }
        if (!AfwClientConfig.get().allowsDebugChat(AfwDebugChatCategory.SETUP)) {
            return;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) {
            return;
        }
        client.player.sendMessage((Text)Text.literal((String)("[AFW] Setup warning: " + message)).formatted(Formatting.YELLOW), false);
    }

    private static List<Identifier> sortedEntityTypes(Set<Identifier> entityTypes) {
        if (entityTypes == null || entityTypes.isEmpty()) {
            return List.of();
        }
        ArrayList<Identifier> out = new ArrayList<Identifier>(entityTypes);
        out.sort(Comparator.comparing(Identifier::toString));
        return List.copyOf(out);
    }

    private static String buildDefinitionValidationKey(List<AfwAnimationDefinitions.Definition> definitions) {
        StringBuilder sb = new StringBuilder();
        sb.append(reloadRevision);
        for (AfwAnimationDefinitions.Definition definition : definitions) {
            if (definition == null) continue;
            sb.append('|').append(definition.id());
            for (AnimationStageInfo stage : definition.stages()) {
                if (stage == null) continue;
                sb.append(':').append(stage.animationId()).append("->").append(stage.effectiveAnimationId()).append('@').append(stage.lengthTicks()).append('~').append(stage.cycleTicks()).append('/').append(stage.cycleMidpointOffsetSeconds());
            }
        }
        return sb.toString();
    }

    private static void reload(ResourceManager manager) {
        Map resources = manager.findResources("geckolib/animations", id -> id.getPath().endsWith(".animation.json"));
        ArrayList entries = new ArrayList(resources.entrySet());
        entries.sort(Comparator.comparing(e -> ((Identifier)e.getKey()).toString()));
        LinkedHashMap<Identifier, AnimationAssetLength> loaded = new LinkedHashMap<Identifier, AnimationAssetLength>();
        for (Map.Entry entry : entries) {
            Identifier fileId = (Identifier)entry.getKey();
            Identifier animationResource = AfwAnimationAssetDiagnostics.animationIdFromPath(fileId);
            if (animationResource == null) continue;
            try (InputStreamReader reader = new InputStreamReader(((Resource)entry.getValue()).getInputStream(), StandardCharsets.UTF_8);){
                double lengthSeconds;
                JsonObject root = JsonParser.parseReader((Reader)reader).getAsJsonObject();
                JsonObject animationsObj = root.has("animations") && root.get("animations").isJsonObject() ? root.getAsJsonObject("animations") : null;
                if (animationsObj == null || animationsObj.isEmpty()) continue;
                Map.Entry firstAnimation = (Map.Entry)animationsObj.entrySet().iterator().next();
                String animationKey = (String)firstAnimation.getKey();
                JsonElement animationEl = (JsonElement)firstAnimation.getValue();
                if (animationKey == null || animationKey.isBlank() || animationEl == null || !animationEl.isJsonObject() || (lengthSeconds = AfwAnimationAssetDiagnostics.readAnimationLength(animationEl.getAsJsonObject())) <= 0.0) continue;
                loaded.put(animationResource, new AnimationAssetLength(animationKey, lengthSeconds));
            }
            catch (IOException | RuntimeException e2) {
                AnimationFramework.LOGGER.debug("[AFW] Failed to read animation length from {}", (Object)fileId, (Object)e2);
            }
        }
        LENGTHS_BY_RESOURCE = Map.copyOf(loaded);
        WARNED_MISMATCHES.clear();
        lastDefinitionValidationKey = "";
        ++reloadRevision;
    }

    private static double stageCycleSeconds(AnimationStageInfo stage) {
        if (stage == null) {
            return 0.0;
        }
        double exactTicks = stage.cycleTicks();
        if (Double.isFinite(exactTicks) && exactTicks > 0.0) {
            return exactTicks / 20.0;
        }
        return stage.lengthTicks() > 0L ? (double)stage.lengthTicks() / 20.0 : 0.0;
    }

    private static double readAnimationLength(JsonObject animObj) {
        Double animationLength = AfwAnimationAssetDiagnostics.readFiniteDouble(animObj.get("animation_length"));
        if (animationLength != null) {
            return animationLength;
        }
        Double length = AfwAnimationAssetDiagnostics.readFiniteDouble(animObj.get("length"));
        if (length != null) {
            return length;
        }
        return -1.0;
    }

    private static Double readFiniteDouble(JsonElement element) {
        if (element == null || !element.isJsonPrimitive()) {
            return null;
        }
        JsonPrimitive primitive = element.getAsJsonPrimitive();
        if (primitive.isNumber()) {
            double value = primitive.getAsDouble();
            return Double.isFinite(value) ? Double.valueOf(value) : null;
        }
        if (!primitive.isString()) {
            return null;
        }
        String raw = primitive.getAsString();
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            double value = Double.parseDouble(raw.trim());
            return Double.isFinite(value) ? Double.valueOf(value) : null;
        }
        catch (NumberFormatException ignored) {
            return null;
        }
    }

    private static Identifier animationIdFromPath(Identifier fileId) {
        String path = fileId.getPath();
        String prefix = "geckolib/animations/";
        String suffix = ".animation.json";
        if (!path.startsWith(prefix) || !path.endsWith(suffix)) {
            return null;
        }
        String animPath = path.substring(prefix.length(), path.length() - suffix.length());
        return Identifier.of((String)fileId.getNamespace(), (String)animPath);
    }

    private static String formatSeconds(double seconds) {
        return String.format(Locale.ROOT, "%.3f", seconds);
    }

    public static final class Reloader
    implements SynchronousResourceReloader {
        public void reload(ResourceManager manager) {
            AfwAnimationAssetDiagnostics.reload(manager);
        }
    }

    private record AnimationAssetLength(String animationKey, double lengthSeconds) {
    }
}

