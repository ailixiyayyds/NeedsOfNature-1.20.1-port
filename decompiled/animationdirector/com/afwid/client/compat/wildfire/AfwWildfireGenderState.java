/*
 * Decompiled with CFR 0.152.
 */
package com.afwid.client.compat.wildfire;

public record AfwWildfireGenderState(float bustSize, float xOffset, float yOffset, float zOffset, float cleavage, boolean uniboob, boolean hasJacketLayer, Side left, Side right) {

    public record Side(float physicsX, float physicsY, float physicsSize, float bounceRotation, UvRect baseUv, UvRect overlayUv) {
    }

    public record UvRect(float u1, float v1, float u2, float v2) {
        public static UvRect pixels(int x1, int y1, int x2, int y2) {
            return new UvRect((float)x1 / 64.0f, (float)y1 / 64.0f, (float)x2 / 64.0f, (float)y2 / 64.0f);
        }
    }
}

