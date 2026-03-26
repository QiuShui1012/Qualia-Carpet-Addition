package com.qiushui1012.mod.qca.mixin.rule;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.qiushui1012.mod.qca.QcaServerRules;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Mixin(AnvilMenu.class)
public class MixinAnvilMenu_tooExpensive {
    @Shadow
    @Final
    private DataSlot cost;

    @Definition(id = "cost", field = "Lnet/minecraft/world/inventory/AnvilMenu;cost:Lnet/minecraft/world/inventory/DataSlot;")
    @Definition(id = "get", method = "Lnet/minecraft/world/inventory/DataSlot;get()I")
    @Expression("this.cost.get() >= 40")
    @ModifyExpressionValue(
        //#if MC >= 12108
        method = "createResultInternal",
        //#else
        //$$ method = "createResult",
        //#endif
        at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private boolean qca$modifyTooExpensiveExpression(boolean original) {
        return QcaServerRules.tooExpensiveLevel >= 0 && this.cost.get() >= QcaServerRules.tooExpensiveLevel;
    }

    @ModifyConstant(method = "createResultInternal", constant = @Constant(intValue = 39))
    private int qca$modifyThirtyNine(int constant) {
        return QcaServerRules.tooExpensiveLevel - 1;
    }

    @WrapOperation(
        method = "createResultInternal",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AnvilMenu;calculateIncreasedRepairCost(I)I")
    )
    private static int qca$modifyMultiplier(int i, Operation<Integer> original) {
        return Math.clamp(
            BigDecimal.valueOf(i)
                .multiply(BigDecimal.valueOf(QcaServerRules.repairCostMultiplier))
                .add(BigDecimal.ONE)
                .round(new MathContext(0, RoundingMode.CEILING))
                .intValue(),
            0,
            2147483647
        );
    }
}
