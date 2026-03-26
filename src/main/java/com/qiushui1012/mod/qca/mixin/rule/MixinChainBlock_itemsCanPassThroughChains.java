package com.qiushui1012.mod.qca.mixin.rule;

import com.qiushui1012.mod.qca.QcaServerRules;
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

@Mixin(ChainBlock.class)
public abstract class MixinChainBlock_itemsCanPassThroughChains extends Block {
    public MixinChainBlock_itemsCanPassThroughChains(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (
            QcaServerRules.itemsCanPassThroughChains
            && context instanceof EntityCollisionContext
            && ((EntityCollisionContext) context).getEntity() instanceof ItemEntity
        ) {
            return Shapes.empty();
        }

        return super.getCollisionShape(state, level, pos, context);
    }
}
