package com.qiushui1012.mod.qca.mixin.rule;

import com.qiushui1012.mod.qca.util.rule.ShearUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import com.qiushui1012.mod.qca.QcaSettings;

@Mixin(ShearsItem.class)
public abstract class MixinShearsItem_tallPlantShearToSmall extends Item {
    public MixinShearsItem_tallPlantShearToSmall(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        if (ctx.getPlayer() != null && ctx.getPlayer().isShiftKeyDown()) return super.useOn(ctx);
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();

        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        switch (QcaSettings.matchTallPlant(block)) {
            case QcaSettings.GRASSES:
            case QcaSettings.FLOWERS:
                return ShearUtil.shearTallPlant(ctx, level, pos, block, state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF));
            //#if MC >= 11700
            case QcaSettings.DRIPLEAF:
                return ShearUtil.shearBigDripleaf(ctx, level, pos, block);
            //#endif
        }
        return super.useOn(ctx);
    }
}
