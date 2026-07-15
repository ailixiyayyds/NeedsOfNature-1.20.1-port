/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.entity.state.EntityRenderState
 *  net.minecraft.util.Identifier
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 */
package com.afwid.mixin.client;

import com.afwid.client.render.AfwRenderStateAccess;
import java.util.UUID;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value={EntityRenderState.class})
public class EntityRenderStateMixin
implements AfwRenderStateAccess {
    @Unique
    private UUID afw$entityUuid;
    @Unique
    private Identifier afw$entityTypeId;

    @Override
    public UUID afw$getEntityUuid() {
        return this.afw$entityUuid;
    }

    @Override
    public void afw$setEntityUuid(UUID uuid) {
        this.afw$entityUuid = uuid;
    }

    @Override
    public Identifier afw$getEntityTypeId() {
        return this.afw$entityTypeId;
    }

    @Override
    public void afw$setEntityTypeId(Identifier id) {
        this.afw$entityTypeId = id;
    }
}

