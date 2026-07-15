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

import com.nonid.NonTrinketsIntegration;
import com.nonid.data.NonAccessoryItemRegistry;
import com.nonid.pack.NonGeneratedInMemoryPack;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_3264;
import net.minecraft.class_3285;
import net.minecraft.class_3288;

public final class NonGeneratedAccessoryTagPackProvider
implements class_3285 {
    public void method_14453(Consumer<class_3288> profileAdder) {
        if (!NonTrinketsIntegration.isTrinketsLoaded()) {
            return;
        }
        Map<class_2960, byte[]> resources = NonGeneratedAccessoryTagPackProvider.buildGeneratedResources();
        if (resources.isEmpty()) {
            return;
        }
        class_3288 profile = NonGeneratedInMemoryPack.createProfile("needsofnature/server/generated_accessory_tags", (class_2561)class_2561.method_43470((String)"NoN Generated Accessory Tags"), (class_2561)class_2561.method_43470((String)"Generated NoN accessory Trinkets tags"), class_3264.field_14190, resources);
        if (profile != null) {
            profileAdder.accept(profile);
        }
    }

    private static Map<class_2960, byte[]> buildGeneratedResources() {
        LinkedHashMap<class_2960, byte[]> resources = new LinkedHashMap<class_2960, byte[]>();
        NonGeneratedAccessoryTagPackProvider.addSlotTag(resources, "legs/v");
        NonGeneratedAccessoryTagPackProvider.addSlotTag(resources, "legs/a");
        NonGeneratedAccessoryTagPackProvider.addSlotTag(resources, "legs/d");
        return resources;
    }

    private static void addSlotTag(Map<class_2960, byte[]> resources, String slotId) {
        List<class_2960> itemIds = NonAccessoryItemRegistry.getItemIdsForTrinketsSlot(slotId);
        if (itemIds.isEmpty()) {
            return;
        }
        String path = "tags/item/" + slotId + ".json";
        StringBuilder json = new StringBuilder();
        json.append("{\n  \"replace\": false,\n  \"values\": [\n");
        for (int i = 0; i < itemIds.size(); ++i) {
            json.append("    \"").append(itemIds.get(i)).append("\"");
            if (i + 1 < itemIds.size()) {
                json.append(',');
            }
            json.append('\n');
        }
        json.append("  ]\n}\n");
        resources.put(class_2960.method_60655((String)"trinkets", (String)path), json.toString().getBytes(StandardCharsets.UTF_8));
    }
}

