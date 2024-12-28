package zh.qiushui.mod.qca.rule.util.boneMealDoubleSmallFlowers;

import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public interface DoubleWithFertilization extends Fertilizable {
    @Override
    default boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    default boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    default void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        qca_doubled(world, random, pos, state);
    }

    default void qca_doubled(ServerWorld world, Random random, BlockPos pos, BlockState state) {}
}
