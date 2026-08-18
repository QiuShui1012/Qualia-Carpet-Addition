package com.qiushui1012.mod.qca.util.rule;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

//#if MC < 1_21_10
//$$ import net.minecraft.world.entity.LivingEntity;
//#endif

public class ShearUtil {
    public static void shearTallPlant(
        Level level,
        BlockPos pos,
        BlockState state,
        Player player,
        ItemStack inHandItem,
        InteractionHand hand
    ) {
        Block small = PlantTransformRecord.SMALL_TALL_PLANTS.inverse().get(state.getBlock());

        level.playSound(player, pos, SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);

        DoubleBlockHalf half = state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF);
        BlockPos upper = null;
        BlockPos lower = null;
        if (half.equals(DoubleBlockHalf.UPPER)) {
            upper = pos;
            lower = pos.below();
        } else if (half.equals(DoubleBlockHalf.LOWER)) {
            upper = pos.above();
            lower = pos;
        }

        if (lower != null) {
            level.removeBlock(lower, false);
        }
        if (upper != null) {
            level.removeBlock(upper, false);
        }

        level.addFreshEntity(new ItemEntity(
            level,
            pos.getX(),
            pos.getY(),
            pos.getZ(),
            ShearUtil.copyWithCount(small.asItem().getDefaultInstance(), 2)
        ));
        inHandItem.hurtAndBreak(
            1,
            player,
            //#if MC < 1_21_10
            //$$ LivingEntity.getSlotForHand(hand)
            //#else
            hand.asEquipmentSlot()
            //#endif
        );
    }

    public static void shearBigDripleaf(
        Level level,
        BlockPos pos,
        BlockState state,
        Player player,
        ItemStack inHandItem,
        InteractionHand hand
    ) {
        Block small = PlantTransformRecord.SMALL_TALL_PLANTS.inverse().get(state.getBlock());

        level.playSound(player, pos, SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);

        level.removeBlock(pos, false);

        level.addFreshEntity(new ItemEntity(
            level,
            pos.getX(),
            pos.getY(),
            pos.getZ(),
            ShearUtil.copyWithCount(small.asItem().getDefaultInstance(), 1)
        ));
        inHandItem.hurtAndBreak(
            1,
            player,
            //#if MC < 1_21_10
            //$$ LivingEntity.getSlotForHand(hand)
            //#else
            hand.asEquipmentSlot()
            //#endif
        );
    }

    private static ItemStack copyWithCount(ItemStack stack, int count) {
        ItemStack copied = stack.copy();
        copied.setCount(count);
        return copied;
    }
}
