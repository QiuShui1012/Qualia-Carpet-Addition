package com.qiushui1012.mod.qca.util.rule;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;

public interface FertilizableBlock extends BonemealableBlock {
    @Override
    default boolean isValidBonemealTarget(
        LevelReader levelReader,
        BlockPos blockPos,
        BlockState blockState
        //#if MC < 12002
        //$$ ,boolean bl
        //#endif
    ) {
        throw new AssertionError("Not implemented");
    }

    @Override
    default boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        throw new AssertionError("Not implemented");
    }

    @Override
    default void performBonemeal(
        //#if MC >= 11500
        ServerLevel serverLevel,
        //#else
        //$$ Level level,
        //#endif
        RandomSource randomSource,
        BlockPos blockPos,
        BlockState blockState
    ) {
        throw new AssertionError("Not implemented");
    }
}
