package zh.qiushui.mod.qca.rule.mixin.itemsCanPassThroughChains;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChainBlock;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import zh.qiushui.mod.qca.QcaSettings;

@Mixin(ChainBlock.class)
public abstract class MixinChainBlock extends Block {
    public MixinChainBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getCollisionShape(
            BlockState state, BlockView world, BlockPos pos, ShapeContext shapeContext
    ) {
        if (QcaSettings.itemsCanPassThroughChains && shapeContext instanceof EntityShapeContext ctx) {
            if (ctx.getEntity() instanceof ItemEntity) {
                return VoxelShapes.empty();
            }
        }

        return super.getCollisionShape(state, world, pos, shapeContext);
    }
}
