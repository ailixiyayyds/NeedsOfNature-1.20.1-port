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

import com.nonid.NonTrinketsIntegration;
import com.nonid.data.NonAccessoryItemRegistry;
import com.nonid.pack.NonGeneratedInMemoryPack;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourcePackProfile;

public final class NonGeneratedAccessoryTagPackProvider
implements ResourcePackProvider {
    public void register(Consumer<ResourcePackProfile> profileAdder) {
        if (!NonTrinketsIntegration.isTrinketsLoaded()) {
            return;
        }
        Map<Identifier, byte[]> resources = NonGeneratedAccessoryTagPackProvider.buildGeneratedResources();
        if (resources.isEmpty()) {
            return;
        }
        ResourcePackProfile profile = NonGeneratedInMemoryPack.createProfile("needsofnature/server/generated_accessory_tags", (Text)Text.literal((String)"NoN Generated Accessory Tags"), (Text)Text.literal((String)"Generated NoN accessory Trinkets tags"), ResourceType.SERVER_DATA, resources);
        if (profile != null) {
            profileAdder.accept(profile);
        }
    }

    private static Map<Identifier, byte[]> buildGeneratedResources() {
        LinkedHashMap<Identifier, byte[]> resources = new LinkedHashMap<Identifier, byte[]>();
        NonGeneratedAccessoryTagPackProvider.addSlotTag(resources, "legs/v");
        NonGeneratedAccessoryTagPackProvider.addSlotTag(resources, "legs/a");
        NonGeneratedAccessoryTagPackProvider.addSlotTag(resources, "legs/d");
        return resources;
    }

    private static void addSlotTag(Map<Identifier, byte[]> resources, String slotId) {
        List<Identifier> itemIds = NonAccessoryItemRegistry.getItemIdsForTrinketsSlot(slotId);
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
        resources.put(Identifier.of((String)"trinkets", (String)path), json.toString().getBytes(StandardCharsets.UTF_8));
    }
}

