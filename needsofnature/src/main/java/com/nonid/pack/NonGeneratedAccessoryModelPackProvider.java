/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.text.Text
 *  net.minecraft.util.Identifier
 *  net.minecraft.resource.ResourceType
 *  net.minecraft.resource.ResourcePackProvider
 *  net.minecraft.resource.ResourcePackProfile
 */
package com.nonid.pack;

import com.nonid.data.NonAccessoryItemRegistry;
import com.nonid.pack.NonGeneratedInMemoryPack;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourcePackProfile;

public final class NonGeneratedAccessoryModelPackProvider
implements ResourcePackProvider {
    public void register(Consumer<ResourcePackProfile> profileAdder) {
        Map<Identifier, byte[]> resources = NonGeneratedAccessoryModelPackProvider.buildGeneratedResources();
        if (resources.isEmpty()) {
            return;
        }
        ResourcePackProfile profile = NonGeneratedInMemoryPack.createProfile("needsofnature/client/generated_accessory_models", (Text)Text.literal((String)"NoN Generated Accessory Models"), (Text)Text.literal((String)"Generated NoN accessory item models"), ResourceType.CLIENT_RESOURCES, resources);
        if (profile != null) {
            profileAdder.accept(profile);
        }
    }

    private static Map<Identifier, byte[]> buildGeneratedResources() {
        LinkedHashMap<Identifier, byte[]> resources = new LinkedHashMap<Identifier, byte[]>();
        for (Map.Entry<Identifier, NonAccessoryItemRegistry.ItemClientModel> entry : NonAccessoryItemRegistry.getClientModelsByItem().entrySet()) {
            Identifier itemId = entry.getKey();
            NonAccessoryItemRegistry.ItemClientModel model = entry.getValue();
            if (model.itemModel() != null) {
                NonGeneratedAccessoryModelPackProvider.addItemDefinition(resources, itemId, model.itemModel());
            }
            if (model.itemTexture() == null) continue;
            NonGeneratedAccessoryModelPackProvider.addGeneratedItemModel(resources, itemId, model.itemTexture());
        }
        return resources;
    }

    private static void addItemDefinition(Map<Identifier, byte[]> resources, Identifier itemId, Identifier modelId) {
        String json = "{\n  \"model\": {\n    \"type\": \"minecraft:model\",\n    \"model\": \"" + String.valueOf(modelId) + "\"\n  }\n}\n";
        resources.put(Identifier.of((String)itemId.getNamespace(), (String)("items/" + itemId.getPath() + ".json")), json.getBytes(StandardCharsets.UTF_8));
    }

    private static void addGeneratedItemModel(Map<Identifier, byte[]> resources, Identifier itemId, Identifier textureId) {
        String json = "{\n  \"parent\": \"minecraft:item/generated\",\n  \"textures\": {\n    \"layer0\": \"" + String.valueOf(textureId) + "\"\n  }\n}\n";
        resources.put(Identifier.of((String)itemId.getNamespace(), (String)("models/item/" + itemId.getPath() + ".json")), json.getBytes(StandardCharsets.UTF_8));
    }
}

