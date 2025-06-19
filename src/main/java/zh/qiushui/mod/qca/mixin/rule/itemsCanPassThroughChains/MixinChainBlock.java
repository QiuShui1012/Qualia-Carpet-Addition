package zh.qiushui.mod.qca.mixin.rule.itemsCanPassThroughChains;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import zh.qiushui.mod.qca.QcaServerRules;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@Mixin(ChainBlock.class)
public abstract class MixinChainBlock extends Block {
    public MixinChainBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (QcaServerRules.itemsCanPassThroughChains && context instanceof EntityCollisionContext ctx) {
            if (ctx.getEntity() instanceof ItemEntity) {
                return Shapes.empty();
            }
        }

        return super.getCollisionShape(state, level, pos, context);
    }
}
