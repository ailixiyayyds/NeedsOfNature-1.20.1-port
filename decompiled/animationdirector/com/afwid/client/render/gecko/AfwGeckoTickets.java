/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2960
 *  org.jetbrains.annotations.NotNull
 *  software.bernie.geckolib.constant.dataticket.DataTicket
 */
package com.afwid.client.render.gecko;

import com.afwid.api.AfwGeckoModelEvents;
import com.afwid.client.compat.wildfire.AfwWildfireGenderState;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.class_2960;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.constant.dataticket.DataTicket;

public final class AfwGeckoTickets {
    public static final DataTicket<@NotNull class_2960> AFW_ANIMATION_ID = DataTicket.create((String)"afw_animation_id", class_2960.class);
    public static final DataTicket<@NotNull class_2960> AFW_ANIMATION_RESOURCE_ID = DataTicket.create((String)"afw_animation_resource_id", class_2960.class);
    public static final DataTicket<@NotNull UUID> AFW_INSTANCE_ID = DataTicket.create((String)"afw_instance_id", UUID.class);
    public static final DataTicket<@NotNull UUID> ACTOR_UUID = DataTicket.create((String)"afw_actor_uuid", UUID.class);
    public static final DataTicket<@NotNull class_2960> MODEL_ID = DataTicket.create((String)"afw_model_id", class_2960.class);
    public static final DataTicket<@NotNull class_2960> TEXTURE_ID = DataTicket.create((String)"afw_texture_id", class_2960.class);
    public static final DataTicket<@NotNull List<class_2960>> LAYER_TEXTURES = DataTicket.create((String)"afw_layer_textures", List.class);
    public static final DataTicket<@NotNull List<class_2960>> EMISSIVE_TEXTURES = DataTicket.create((String)"afw_emissive_textures", List.class);
    public static final DataTicket<@NotNull Map<String, class_2960>> BONE_TEXTURES = DataTicket.create((String)"afw_bone_textures", Map.class);
    public static final DataTicket<@NotNull Map<String, AfwGeckoModelEvents.BoneItemProp>> BONE_ITEMS = DataTicket.create((String)"afw_bone_items", Map.class);
    public static final DataTicket<@NotNull Map<String, Boolean>> BONE_VISIBILITY = DataTicket.create((String)"afw_bone_visibility", Map.class);
    public static final DataTicket<@NotNull Map<String, Set<Integer>>> HIDDEN_BONE_CUBE_INDICES = DataTicket.create((String)"afw_hidden_bone_cube_indices", Map.class);
    public static final DataTicket<@NotNull Boolean> TRANSLUCENT_RENDER = DataTicket.create((String)"afw_translucent_render", Boolean.class);
    public static final DataTicket<@NotNull class_2960> LAST_AFW_ANIMATION_RESOURCE_ID = DataTicket.create((String)"afw_last_animation_resource_id", class_2960.class);
    public static final DataTicket<@NotNull UUID> LAST_AFW_INSTANCE_ID = DataTicket.create((String)"afw_last_instance_id", UUID.class);
    public static final DataTicket<@NotNull Double> ANIMATION_SPEED = DataTicket.create((String)"afw_animation_speed", Double.class);
    public static final DataTicket<@NotNull Double> ANIMATION_TIMELINE_SECONDS = DataTicket.create((String)"afw_animation_timeline_seconds", Double.class);
    public static final DataTicket<@NotNull AfwWildfireGenderState> WILDFIRE_GENDER_STATE = DataTicket.create((String)"afw_wildfire_gender_state", AfwWildfireGenderState.class);

    private AfwGeckoTickets() {
    }
}

