package zh.qiushui.mod.qca.rule.mixin.tallPlantShearToSmall;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ShearsItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zh.qiushui.mod.qca.QcaSettings;
import zh.qiushui.mod.qca.rule.util.PlantTransformUtil;

@Mixin(ShearsItem.class)
public abstract class MixinShearsItem {
    @Inject(method = "useOnBlock", at = @At(value = "TAIL"), cancellable = true)
    private void qca_checkForTallPlant(
            ItemUsageContext ctx, CallbackInfoReturnable<ActionResult> cir
    ) {
        World world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();

        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        switch (QcaSettings.matchTallPlant(block)) {
            case 1, 3 -> cir.setReturnValue(
                    qca_shearTallPlant(ctx, world, pos, block, state.get(Properties.DOUBLE_BLOCK_HALF))
            );
            case 2 -> cir.setReturnValue(
                    qca_shearBigDripleaf(ctx, world, pos, block)
            );
        }
    }

    @Unique
    private static ActionResult qca_shearTallPlant(
            ItemUsageContext ctx, World world, BlockPos pos,
            Block plant, DoubleBlockHalf half
    ) {
        Block small = PlantTransformUtil.SMALL_TALL_PLANTS.inverse().get(plant).getFirst();

        PlayerEntity player = ctx.getPlayer();
        world.playSound(player, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1.0F, 1.0F);

        BlockPos upper = null;
        BlockPos lower = null;
        if (half.equals(DoubleBlockHalf.UPPER)) {
            upper = pos;
            lower = pos.down();
        } else if (half.equals(DoubleBlockHalf.LOWER)) {
            upper = pos.up();
            lower = pos;
        }

        if (lower != null) {
            world.removeBlock(lower, false);
        }
        if (upper != null) {
            world.removeBlock(upper, false);
        }

        world.spawnEntity(new ItemEntity(
                world, pos.getX(), pos.getY(), pos.getZ(),
                small.asItem().getDefaultStack().copyWithCount(2)
        ));
        if (player != null) {
            ctx.getStack().damage(1, player, LivingEntity.getSlotForHand(ctx.getHand()));
        }

        return ActionResult.SUCCESS;
    }

    @Unique
    private static ActionResult qca_shearBigDripleaf(ItemUsageContext ctx, World world, BlockPos pos, Block plant) {
        Block small = PlantTransformUtil.SMALL_TALL_PLANTS.inverse().get(plant).getFirst();

        PlayerEntity player = ctx.getPlayer();
        world.playSound(player, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1.0F, 1.0F);

        world.removeBlock(pos, false);

        world.spawnEntity(new ItemEntity(
                world, pos.getX(), pos.getY(), pos.getZ(),
                small.asItem().getDefaultStack().copyWithCount(1)
        ));
        if (player != null) {
            ctx.getStack().damage(1, player, LivingEntity.getSlotForHand(ctx.getHand()));
        }

        return ActionResult.SUCCESS;
    }
}
