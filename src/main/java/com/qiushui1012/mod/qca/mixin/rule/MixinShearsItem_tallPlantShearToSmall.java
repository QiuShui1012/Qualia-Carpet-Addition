package com.qiushui1012.mod.qca.mixin.rule;

import com.qiushui1012.mod.qca.util.rule.ShearUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.qiushui1012.mod.qca.QcaSettings;

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
            case QcaSettings.GRASSES:
            case QcaSettings.FLOWERS:
                cir.setReturnValue(ShearUtil.shearTallPlant(ctx, level, pos, block, state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF)));
                break;
            case QcaSettings.DRIPLEAF:
                cir.setReturnValue(ShearUtil.shearBigDripleaf(ctx, level, pos, block));
                break;
        }
    }
}
