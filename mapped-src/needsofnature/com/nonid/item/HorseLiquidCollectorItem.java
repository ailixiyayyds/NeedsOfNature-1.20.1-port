/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Hand
 *  net.minecraft.util.ActionResult
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.item.Item
 *  net.minecraft.item.Item$Settings
 *  net.minecraft.item.ItemStack
 *  net.minecraft.world.World
 *  net.minecraft.util.hit.HitResult$Type
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.sound.SoundEvents
 *  net.minecraft.sound.SoundCategory
 *  net.minecraft.stat.Stats
 *  net.minecraft.world.RaycastContext$FluidHandling
 *  net.minecraft.util.hit.BlockHitResult
 *  net.minecraft.world.event.GameEvent
 *  net.minecraft.registry.entry.RegistryEntry
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
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.world.RaycastContext;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.event.GameEvent;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

public class HorseLiquidCollectorItem
extends Item
implements GeoItem {
    private AnimatableInstanceCache animatableCache;
    private boolean syncedAnimatableRegistered;

    public HorseLiquidCollectorItem(Item.Settings settings) {
        super(settings);
    }

    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        BlockHitResult hit = HorseLiquidCollectorItem.raycast((World)world, (PlayerEntity)user, (RaycastContext.FluidHandling)RaycastContext.FluidHandling.ANY);
        if (hit.getType() != HitResult.Type.BLOCK) {
            return ActionResult.PASS;
        }
        Vec3d hitPos = hit.getPos();
        HorseLiquidCollectorEntity collector = new HorseLiquidCollectorEntity(NeedsOfNature.HORSE_LIQUID_COLLECTOR_ENTITY_TYPE, world);
        collector.refreshPositionAndAngles(hitPos.x, hitPos.y + 0.01, hitPos.z, user.getYaw(), 0.0f);
        collector.setLockedOrientation(user.getYaw(), 0.0f);
        if (!world.isSpaceEmpty((Entity)collector, collector.getBoundingBox())) {
            return ActionResult.FAIL;
        }
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }
        world.spawnEntity((Entity)collector);
        world.playSound(null, collector.getX(), collector.getY(), collector.getZ(), SoundEvents.ENTITY_ARMOR_STAND_PLACE, SoundCategory.PLAYERS, 1.0f, 1.0f);
        world.emitGameEvent((Entity)user, (RegistryEntry)GameEvent.ENTITY_PLACE, collector.getBlockPos());
        user.incrementStat(Stats.USED.getOrCreateStat((Object)this));
        if (!user.isInCreativeMode()) {
            stack.decrement(1);
        }
        return ActionResult.SUCCESS;
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

