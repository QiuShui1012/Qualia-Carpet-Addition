package com.qiushui1012.mod.qca.mixin.rule;

import com.qiushui1012.mod.qca.QcaServerRules;
import com.qiushui1012.mod.qca.util.rule.PlantTransformRecord;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.ParticleUtils;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BoneMealItem.class)
public class MixinBoneMealItem_fertilizableSmallFlowers {
    @Inject(method = "addGrowthParticles", at = @At("HEAD"))
    private static void givePermissionToSmallFlowers(LevelAccessor arg, BlockPos arg2, int i, CallbackInfo ci) {
        if (!QcaServerRules.fertilizableSmallFlowers) return;
        BlockState state = arg.getBlockState(arg2);
        for (Block smallPlant : PlantTransformRecord.SMALL_TALL_FLOWERS.keySet()) {
            if (!state.is(smallPlant)) continue;
            ParticleUtils.spawnParticleInBlock(arg, arg2, i, ParticleTypes.HAPPY_VILLAGER);
            return;
        }
    }
}
