/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.World
 *  net.minecraft.block.Blocks
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.block.BlockState
 */
package com.nonid;

import com.nonid.NonConfig;
import net.minecraft.world.World;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.BlockState;

public final class NonSkinRepairBlocks {
    private NonSkinRepairBlocks() {
    }

    public static boolean isNearRepairBlock(World world, BlockPos center, NonConfig config) {
        if (world == null || center == null) {
            return false;
        }
        for (BlockPos pos : BlockPos.iterateOutwards((BlockPos)center, (int)1, (int)1, (int)1)) {
            if (!NonSkinRepairBlocks.isRepairBlock(world.getBlockState(pos), config)) continue;
            return true;
        }
        return false;
    }

    public static boolean isRepairBlock(BlockState state, NonConfig config) {
        if (state == null) {
            return false;
        }
        if (state.isOf(Blocks.LOOM)) {
            return true;
        }
        return config != null && config.isCraftingTableSkinRepairAllowed() && state.isOf(Blocks.CRAFTING_TABLE);
    }
}

