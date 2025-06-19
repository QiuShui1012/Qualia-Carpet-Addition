package zh.qiushui.mod.qca.mixin.rule.breakDripleafKeepStem;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.BigDripleafStemBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zh.qiushui.mod.qca.QcaServerRules;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

@Mixin(BigDripleafStemBlock.class)
public abstract class MixinBigDripleafStemBlock {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void qca$checkForUpDripleaf(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        BlockState upState = level.getBlockState(pos.above());
        if (QcaServerRules.breakDripleafKeepStem && !upState.getBlock().equals(Blocks.BIG_DRIPLEAF)) {
            level.setBlockAndUpdate(pos, Blocks.BIG_DRIPLEAF.defaultBlockState().setValue(FACING, state.getValue(FACING)));
            ci.cancel();
        }
    }
}
