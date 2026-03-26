package com.qiushui1012.mod.qca.mixin.rule;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import com.qiushui1012.mod.qca.QcaSettings;
import com.qiushui1012.mod.qca.util.rule.FertilizableBlock;

@Mixin(FlowerBlock.class)
public abstract class MixinFlowerBlock_boneMealDoubleSmallFlowers extends Block implements FertilizableBlock {
    protected MixinFlowerBlock_boneMealDoubleSmallFlowers(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isValidBonemealTarget(
        LevelReader level,
        BlockPos pos,
        BlockState state
        //#if MC < 12002
        //$$ ,boolean bl
        //#endif
    ) {
        return QcaSettings.fertilizableSmallFlowers;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(
        //#if MC >= 11500
        ServerLevel level,
        //#else
        //$$ Level level,
        //#endif
        RandomSource random,
        BlockPos pos,
        BlockState state
    ) {
        if (QcaSettings.fertilizableSmallFlowers) {
            Block.popResource(level, pos, new ItemStack(this));
        }
    }
}
