/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Identifier
 */
package com.afwid.client.render;

import java.util.UUID;
import net.minecraft.util.Identifier;

public interface AfwRenderStateAccess {
    public UUID afw$getEntityUuid();

    public void afw$setEntityUuid(UUID var1);

    public Identifier afw$getEntityTypeId();

    public void afw$setEntityTypeId(Identifier var1);
}

