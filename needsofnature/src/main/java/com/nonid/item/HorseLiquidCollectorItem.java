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
import java.util.function.Supplier;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
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
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

public class HorseLiquidCollectorItem
extends Item
implements GeoItem {
    private final AnimatableInstanceCache animatableCache = GeckoLibUtil.createInstanceCache(this);
    /*
     * GeoItem.makeRenderer is Fabric-only in GeckoLib 4.8.4.  Connector keeps
     * Forge GeckoLib when native Forge mods depend on it, so linking that method
     * directly crashes class initialization before Connector can start the mod.
     */
    private final Supplier<Object> renderProvider = createRenderProviderSupplier();

    public HorseLiquidCollectorItem(Item.Settings settings) {
        super(settings);
        GeoItem.registerSyncedAnimatable(this);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        BlockHitResult hit = HorseLiquidCollectorItem.raycast((World)world, (PlayerEntity)user, (RaycastContext.FluidHandling)RaycastContext.FluidHandling.ANY);
        if (hit.getType() != HitResult.Type.BLOCK) {
            return TypedActionResult.pass(stack);
        }
        Vec3d hitPos = hit.getPos();
        HorseLiquidCollectorEntity collector = new HorseLiquidCollectorEntity(NeedsOfNature.HORSE_LIQUID_COLLECTOR_ENTITY_TYPE, world);
        collector.refreshPositionAndAngles(hitPos.x, hitPos.y + 0.01, hitPos.z, user.getYaw(), 0.0f);
        collector.setLockedOrientation(user.getYaw(), 0.0f);
        if (!world.isSpaceEmpty((Entity)collector, collector.getBoundingBox())) {
            return TypedActionResult.fail(stack);
        }
        if (world.isClient) {
            return TypedActionResult.success(stack, true);
        }
        world.spawnEntity((Entity)collector);
        world.playSound(null, collector.getX(), collector.getY(), collector.getZ(), SoundEvents.ENTITY_ARMOR_STAND_PLACE, SoundCategory.PLAYERS, 1.0f, 1.0f);
        world.emitGameEvent(user, GameEvent.ENTITY_PLACE, collector.getBlockPos());
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            stack.decrement(1);
        }
        return TypedActionResult.success(stack, false);
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animatableCache;
    }

    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private GeoItemRenderer<HorseLiquidCollectorItem> renderer;

            public GeoItemRenderer<?> getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = new HorseLiquidCollectorItemRenderer();
                }
                return this.renderer;
            }
        });
    }

    @SuppressWarnings("unchecked")
    private Supplier<Object> createRenderProviderSupplier() {
        try {
            Method makeRenderer = GeoItem.class.getMethod("makeRenderer", GeoItem.class);
            return (Supplier<Object>)makeRenderer.invoke(null, this);
        }
        catch (ReflectiveOperationException ignored) {
            // Forge GeckoLib exposes item rendering through initializeClient.
            return () -> null;
        }
    }

    /**
     * Forge/Connector rendering bridge.  Reflection keeps the Fabric build free
     * from a hard Forge dependency while still providing the GeckoLib renderer.
     */
    public void initializeClient(Consumer<Object> consumer) {
        try {
            Class<?> extensions = Class.forName("net.minecraftforge.client.extensions.common.IClientItemExtensions");
            Object provider = Proxy.newProxyInstance(extensions.getClassLoader(), new Class<?>[]{extensions},
                    (proxy, method, args) -> {
                        if (method.getName().equals("getCustomRenderer")) {
                            return new HorseLiquidCollectorItemRenderer();
                        }
                        if (method.getName().equals("toString")) {
                            return "NeedsOfNature Forge item render provider";
                        }
                        Class<?> returnType = method.getReturnType();
                        if (!returnType.isPrimitive()) return null;
                        if (returnType == boolean.class) return false;
                        if (returnType == char.class) return '\0';
                        if (returnType == byte.class) return (byte)0;
                        if (returnType == short.class) return (short)0;
                        if (returnType == int.class) return 0;
                        if (returnType == long.class) return 0L;
                        if (returnType == float.class) return 0F;
                        return 0D;
                    });
            consumer.accept(provider);
        }
        catch (ClassNotFoundException ignored) {
            // Not running on Forge.
        }
    }

    public Supplier<Object> getRenderProvider() {
        return this.renderProvider;
    }
}

