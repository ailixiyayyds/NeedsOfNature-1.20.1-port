/*
 * Decompiled with CFR 0.152.
 */
package com.nonid;

import com.nonid.data.NonLiquidRoles;

public enum NonLiquidTankType {
    V("v"),
    A("a");

    private final String slotName;

    private NonLiquidTankType(String slotName) {
        this.slotName = slotName;
    }

    public String trinketsSlotId() {
        return "legs/" + this.slotName;
    }

    public boolean accepts(NonLiquidRoles.InjectorRole role) {
        return this == V && role == NonLiquidRoles.InjectorRole.V || this == A && role == NonLiquidRoles.InjectorRole.A;
    }
}

