package zh.qiushui.mod.qca.mixin.rule.breakDripleafKeepStem;

import net.minecraft.block.BigDripleafStemBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zh.qiushui.mod.qca.QcaSettings;

import static net.minecraft.block.HorizontalFacingBlock.FACING;

@Mixin(BigDripleafStemBlock.class)
public abstract class MixinBigDripleafStemBlock {
    @Inject(method = "scheduledTick", at = @At("HEAD"), cancellable = true)
    private void qca_checkForUpDripleaf(
            BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci
    ) {
        BlockState upState = world.getBlockState(pos.up());
        if (QcaSettings.breakDripleafKeepStem && !upState.getBlock().equals(Blocks.BIG_DRIPLEAF)) {
            world.setBlockState(pos, Blocks.BIG_DRIPLEAF.getDefaultState().with(FACING, state.get(FACING)));
            ci.cancel();
        }
    }
}
