/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2561
 *  net.minecraft.class_2960
 *  net.minecraft.class_3264
 *  net.minecraft.class_3285
 *  net.minecraft.class_3288
 */
package com.nonid.pack;

import com.nonid.data.NonAccessoryItemRegistry;
import com.nonid.pack.NonGeneratedInMemoryPack;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_3264;
import net.minecraft.class_3285;
import net.minecraft.class_3288;

public final class NonGeneratedAccessoryModelPackProvider
implements class_3285 {
    public void method_14453(Consumer<class_3288> profileAdder) {
        Map<class_2960, byte[]> resources = NonGeneratedAccessoryModelPackProvider.buildGeneratedResources();
        if (resources.isEmpty()) {
            return;
        }
        class_3288 profile = NonGeneratedInMemoryPack.createProfile("needsofnature/client/generated_accessory_models", (class_2561)class_2561.method_43470((String)"NoN Generated Accessory Models"), (class_2561)class_2561.method_43470((String)"Generated NoN accessory item models"), class_3264.field_14188, resources);
        if (profile != null) {
            profileAdder.accept(profile);
        }
    }

    private static Map<class_2960, byte[]> buildGeneratedResources() {
        LinkedHashMap<class_2960, byte[]> resources = new LinkedHashMap<class_2960, byte[]>();
        for (Map.Entry<class_2960, NonAccessoryItemRegistry.ItemClientModel> entry : NonAccessoryItemRegistry.getClientModelsByItem().entrySet()) {
            class_2960 itemId = entry.getKey();
            NonAccessoryItemRegistry.ItemClientModel model = entry.getValue();
            if (model.itemModel() != null) {
                NonGeneratedAccessoryModelPackProvider.addItemDefinition(resources, itemId, model.itemModel());
            }
            if (model.itemTexture() == null) continue;
            NonGeneratedAccessoryModelPackProvider.addGeneratedItemModel(resources, itemId, model.itemTexture());
        }
        return resources;
    }

    private static void addItemDefinition(Map<class_2960, byte[]> resources, class_2960 itemId, class_2960 modelId) {
        String json = "{\n  \"model\": {\n    \"type\": \"minecraft:model\",\n    \"model\": \"" + String.valueOf(modelId) + "\"\n  }\n}\n";
        resources.put(class_2960.method_60655((String)itemId.method_12836(), (String)("items/" + itemId.method_12832() + ".json")), json.getBytes(StandardCharsets.UTF_8));
    }

    private static void addGeneratedItemModel(Map<class_2960, byte[]> resources, class_2960 itemId, class_2960 textureId) {
        String json = "{\n  \"parent\": \"minecraft:item/generated\",\n  \"textures\": {\n    \"layer0\": \"" + String.valueOf(textureId) + "\"\n  }\n}\n";
        resources.put(class_2960.method_60655((String)itemId.method_12836(), (String)("models/item/" + itemId.method_12832() + ".json")), json.getBytes(StandardCharsets.UTF_8));
    }
}

