package zh.qiushui.mod.qca.mixin.rule;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zh.qiushui.mod.qca.QcaSettings;
import zh.qiushui.mod.qca.util.rule.PlantTransformRecord;

@Mixin(ShearsItem.class)
public abstract class MixinShearsItem_tallPlantShearToSmall {
    @Inject(method = "useOn", at = @At(value = "TAIL"), cancellable = true)
    private void qca$checkForTallPlant(UseOnContext ctx, CallbackInfoReturnable<InteractionResult> cir) {
        if (ctx.getPlayer() != null && ctx.getPlayer().isShiftKeyDown()) return;
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();

        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        switch (QcaSettings.matchTallPlant(block)) {
            case 1, 3 -> cir.setReturnValue(
                qca$shearTallPlant(ctx, level, pos, block, state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF))
            );
            case 2 -> cir.setReturnValue(
                qca$shearBigDripleaf(ctx, level, pos, block)
            );
        }
    }

    @Unique
    private static InteractionResult qca$shearTallPlant(UseOnContext ctx, Level level, BlockPos pos, Block plant, DoubleBlockHalf half) {
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
            small.asItem().getDefaultInstance().copyWithCount(2)
        ));
        if (player != null) {
            ctx.getItemInHand().hurtAndBreak(
                1,
                player,
                //#if MC < 12110
                LivingEntity.getSlotForHand(ctx.getHand())
                //#else
                //$$ ctx.getHand()
                //#endif
            );
        }

        return InteractionResult.SUCCESS;
    }

    @Unique
    private static InteractionResult qca$shearBigDripleaf(UseOnContext ctx, Level level, BlockPos pos, Block plant) {
        Block small = PlantTransformRecord.SMALL_TALL_PLANTS.inverse().get(plant);

        Player player = ctx.getPlayer();
        level.playSound(player, pos, SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);

        level.removeBlock(pos, false);

        level.addFreshEntity(new ItemEntity(
            level, pos.getX(), pos.getY(), pos.getZ(),
            small.asItem().getDefaultInstance().copyWithCount(1)
        ));
        if (player != null) {
            ctx.getItemInHand().hurtAndBreak(
                1,
                player,
                //#if MC < 12110
                LivingEntity.getSlotForHand(ctx.getHand())
                //#else
                //$$ ctx.getHand()
                //#endif
            );
        }

        return InteractionResult.SUCCESS;
    }
}
