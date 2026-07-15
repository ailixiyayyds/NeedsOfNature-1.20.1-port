/*
 * Decompiled with CFR 0.152.
 */
package com.nonid.api;

public record NonMessSnapshot(int v, int a, int m) {
    public int total() {
        return Math.max(0, this.v) + Math.max(0, this.a) + Math.max(0, this.m);
    }
}

