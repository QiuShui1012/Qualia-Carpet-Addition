package com.qiushui1012.mod.qca.mixin.rule;

//#if MC <= 11605
//$$ import com.qiushui1012.mod.qca.util.rule.EntityCollisionContextExtension;
//#endif
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
import com.qiushui1012.mod.qca.QcaSettings;

@Mixin(ChainBlock.class)
public abstract class MixinChainBlock_itemsCanPassThroughChains extends Block {
    public MixinChainBlock_itemsCanPassThroughChains(Properties properties) {
        super(properties);
    }

    //#if MC < 12006
    //$$ @SuppressWarnings("deprecation")
    //#endif
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (
            QcaSettings.itemsCanPassThroughChains
            && context instanceof EntityCollisionContext
            //#if MC >= 11800
            && ((EntityCollisionContext) context).getEntity() instanceof ItemEntity
            //#elseif MC >= 11700
            //$$ && ((EntityCollisionContext) context).getEntity().filter(e -> e instanceof ItemEntity).isPresent()
            //#else
            //$$ && ((EntityCollisionContextExtension) context).qca$getEntity().filter(e -> e instanceof ItemEntity).isPresent()
            //#endif
        ) {
            return Shapes.empty();
        }

        return super.getCollisionShape(state, level, pos, context);
    }
}
