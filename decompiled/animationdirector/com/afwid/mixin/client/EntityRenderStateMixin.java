/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_10017
 *  net.minecraft.class_2960
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 */
package com.afwid.mixin.client;

import com.afwid.client.render.AfwRenderStateAccess;
import java.util.UUID;
import net.minecraft.class_10017;
import net.minecraft.class_2960;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value={class_10017.class})
public class EntityRenderStateMixin
implements AfwRenderStateAccess {
    @Unique
    private UUID afw$entityUuid;
    @Unique
    private class_2960 afw$entityTypeId;

    @Override
    public UUID afw$getEntityUuid() {
        return this.afw$entityUuid;
    }

    @Override
    public void afw$setEntityUuid(UUID uuid) {
        this.afw$entityUuid = uuid;
    }

    @Override
    public class_2960 afw$getEntityTypeId() {
        return this.afw$entityTypeId;
    }

    @Override
    public void afw$setEntityTypeId(class_2960 id) {
        this.afw$entityTypeId = id;
    }
}

