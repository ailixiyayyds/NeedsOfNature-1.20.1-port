/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1268
 *  net.minecraft.class_1269
 *  net.minecraft.class_1297
 *  net.minecraft.class_1657
 *  net.minecraft.class_1792
 *  net.minecraft.class_1792$class_1793
 *  net.minecraft.class_1799
 *  net.minecraft.class_1937
 *  net.minecraft.class_239$class_240
 *  net.minecraft.class_243
 *  net.minecraft.class_3417
 *  net.minecraft.class_3419
 *  net.minecraft.class_3468
 *  net.minecraft.class_3959$class_242
 *  net.minecraft.class_3965
 *  net.minecraft.class_5712
 *  net.minecraft.class_6880
 *  org.jetbrains.annotations.NotNull
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animatable.GeoItem
 *  software.bernie.geckolib.animatable.SingletonGeoAnimatable
 *  software.bernie.geckolib.animatable.client.GeoRenderProvider
 *  software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
 *  software.bernie.geckolib.renderer.GeoItemRenderer
 *  software.bernie.geckolib.util.GeckoLibUtil
 */
package com.nonid.item;

import com.nonid.NeedsOfNature;
import com.nonid.client.HorseLiquidCollectorItemRenderer;
import com.nonid.entity.HorseLiquidCollectorEntity;
import java.util.function.Consumer;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1937;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.minecraft.class_3468;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_5712;
import net.minecraft.class_6880;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

public class HorseLiquidCollectorItem
extends class_1792
implements GeoItem {
    private AnimatableInstanceCache animatableCache;
    private boolean syncedAnimatableRegistered;

    public HorseLiquidCollectorItem(class_1792.class_1793 settings) {
        super(settings);
    }

    public class_1269 method_7836(class_1937 world, class_1657 user, class_1268 hand) {
        class_1799 stack = user.method_5998(hand);
        class_3965 hit = HorseLiquidCollectorItem.method_7872((class_1937)world, (class_1657)user, (class_3959.class_242)class_3959.class_242.field_1347);
        if (hit.method_17783() != class_239.class_240.field_1332) {
            return class_1269.field_5811;
        }
        class_243 hitPos = hit.method_17784();
        HorseLiquidCollectorEntity collector = new HorseLiquidCollectorEntity(NeedsOfNature.HORSE_LIQUID_COLLECTOR_ENTITY_TYPE, world);
        collector.method_5808(hitPos.field_1352, hitPos.field_1351 + 0.01, hitPos.field_1350, user.method_36454(), 0.0f);
        collector.setLockedOrientation(user.method_36454(), 0.0f);
        if (!world.method_8587((class_1297)collector, collector.method_5829())) {
            return class_1269.field_5814;
        }
        if (world.method_8608()) {
            return class_1269.field_5812;
        }
        world.method_8649((class_1297)collector);
        world.method_43128(null, collector.method_23317(), collector.method_23318(), collector.method_23321(), class_3417.field_14969, class_3419.field_15248, 1.0f, 1.0f);
        world.method_33596((class_1297)user, (class_6880)class_5712.field_28738, collector.method_24515());
        user.method_7259(class_3468.field_15372.method_14956((Object)this));
        if (!user.method_56992()) {
            stack.method_7934(1);
        }
        return class_1269.field_5812;
    }

    public void registerControllers(// Could not load outer class - annotation placement on inner may be incorrect
     @NotNull AnimatableManager.ControllerRegistrar controllers) {
    }

    @NotNull
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        if (!this.syncedAnimatableRegistered) {
            GeoItem.registerSyncedAnimatable((SingletonGeoAnimatable)this);
            this.syncedAnimatableRegistered = true;
        }
        if (this.animatableCache == null) {
            this.animatableCache = GeckoLibUtil.createInstanceCache((GeoAnimatable)this);
        }
        return this.animatableCache;
    }

    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider(this){
            private GeoItemRenderer<@NotNull HorseLiquidCollectorItem> renderer;

            public GeoItemRenderer<?> getGeoItemRenderer() {
                if (this.renderer == null) {
                    this.renderer = new HorseLiquidCollectorItemRenderer();
                }
                return this.renderer;
            }
        });
    }
}

