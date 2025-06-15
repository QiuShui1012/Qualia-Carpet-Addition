package zh.qiushui.mod.qca.mixin.rule.boneMealDoubleSmallFlowers;

import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import zh.qiushui.mod.qca.QcaSettings;
import zh.qiushui.mod.qca.rule.util.boneMealDoubleSmallFlowers.DoubleWithFertilization;

@Mixin(FlowerBlock.class)
public abstract class MixinFlowerBlock extends PlantBlock implements DoubleWithFertilization {
    protected MixinFlowerBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return QcaSettings.boneMealDoubleSmallFlowers;
    }

    @Override
    public void qca$doubled(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (QcaSettings.boneMealDoubleSmallFlowers) {
            dropStack(world, pos, new ItemStack(this));
        }
    }
}
