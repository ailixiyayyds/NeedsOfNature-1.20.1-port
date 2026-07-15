/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  software.bernie.geckolib.model.GeoModel
 *  software.bernie.geckolib.renderer.GeoItemRenderer
 */
package com.nonid.client;

import com.nonid.client.HorseLiquidCollectorItemGeoModel;
import com.nonid.item.HorseLiquidCollectorItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class HorseLiquidCollectorItemRenderer
extends GeoItemRenderer<HorseLiquidCollectorItem> {
    public HorseLiquidCollectorItemRenderer() {
        super(new HorseLiquidCollectorItemGeoModel());
    }
}

