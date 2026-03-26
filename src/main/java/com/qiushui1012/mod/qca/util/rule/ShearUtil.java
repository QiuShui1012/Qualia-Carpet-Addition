package com.qiushui1012.mod.qca.util.rule;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

//#if MC >= 12006 && MC < 12110
//$$ import net.minecraft.world.entity.LivingEntity;
//#endif

public class ShearUtil {
    public static InteractionResult shearTallPlant(UseOnContext ctx, Level level, BlockPos pos, Block plant, DoubleBlockHalf half) {
        Block small = PlantTransformRecord.SMALL_TALL_PLANTS.inverse().get(plant);

        Player player = ctx.getPlayer();
        level.playSound(player, pos, SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);

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
        if (player != null) {
            ctx.getItemInHand().hurtAndBreak(
                1,
                player,
                //#if MC >= 12110
                ctx.getHand()
                //#elseif MC >= 12006
                //$$ LivingEntity.getSlotForHand(ctx.getHand())
                //#else
                //$$ entity -> {}
                //#endif
            );
        }

        return InteractionResult.SUCCESS;
    }

    //#if MC >= 11700
    public static InteractionResult shearBigDripleaf(UseOnContext ctx, Level level, BlockPos pos, Block plant) {
        Block small = PlantTransformRecord.SMALL_TALL_PLANTS.inverse().get(plant);

        Player player = ctx.getPlayer();
        level.playSound(player, pos, SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);

        level.removeBlock(pos, false);

        level.addFreshEntity(new ItemEntity(
            level,
            pos.getX(),
            pos.getY(),
            pos.getZ(),
            ShearUtil.copyWithCount(small.asItem().getDefaultInstance(), 1)
        ));
        if (player != null) {
            ctx.getItemInHand().hurtAndBreak(
                1,
                player,
                //#if MC >= 12110
                ctx.getHand()
                //#elseif MC >= 12006
                //$$ LivingEntity.getSlotForHand(ctx.getHand())
                //#elseif MC >= 11700
                //$$ entity -> {}
                //#endif
            );
        }

        return InteractionResult.SUCCESS;
    }
    //#endif

    private static ItemStack copyWithCount(ItemStack stack, int count) {
        ItemStack copied = stack.copy();
        copied.setCount(count);
        return copied;
    }
}
