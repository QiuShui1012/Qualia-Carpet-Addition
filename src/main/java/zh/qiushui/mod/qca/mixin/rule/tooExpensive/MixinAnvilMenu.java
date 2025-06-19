package zh.qiushui.mod.qca.mixin.rule.tooExpensive;

import net.minecraft.world.inventory.AnvilMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zh.qiushui.mod.qca.QcaSettings;

@Mixin(AnvilMenu.class)
public class MixinAnvilMenu {
    @ModifyConstant(method = "createResult", constant = @Constant(intValue = 40, ordinal = 1))
    private int qca$modifySecondForty(int constant) {
        return QcaSettings.getTooExpensiveLevel() + 1;
    }

    @ModifyConstant(method = "createResult", constant = @Constant(intValue = 39))
    private int qca$modifyThirtyNine(int constant) {
        return QcaSettings.getTooExpensiveLevel();
    }

    @ModifyConstant(method = "createResult", constant = @Constant(intValue = 40, ordinal = 2))
    private int qca$modifyThirdForty(int constant) {
        return QcaSettings.getTooExpensiveLevel() + 1;
    }

    @Inject(method = "calculateIncreasedRepairCost", at = @At("HEAD"), cancellable = true)
    private static void qca$modifyMultiplier(int i, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(
            Math.max(
                0, (int) Math.ceil(Math.min(
                    (long) i * QcaSettings.repairCostMultiplier + 1L,
                    2147483647L
                ))
            )
        );
    }
}
