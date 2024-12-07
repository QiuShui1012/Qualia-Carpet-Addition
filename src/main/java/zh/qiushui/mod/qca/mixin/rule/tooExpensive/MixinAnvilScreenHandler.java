package zh.qiushui.mod.qca.mixin.rule.tooExpensive;

import net.minecraft.screen.AnvilScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zh.qiushui.mod.qca.QcaSettings;

@Mixin(AnvilScreenHandler.class)
public class MixinAnvilScreenHandler {
    @ModifyConstant(method = "updateResult", constant = @Constant(intValue = 40, ordinal = 1))
    private int qca_modifySecondForty(int constant) {
        return QcaSettings.getTooExpensiveLevel() + 1;
    }

    @ModifyConstant(method = "updateResult", constant = @Constant(intValue = 39))
    private int qca_modifyThirtyNine(int constant) {
        return QcaSettings.getTooExpensiveLevel();
    }

    @ModifyConstant(method = "updateResult", constant = @Constant(intValue = 40, ordinal = 2))
    private int qca_modifyThirdForty(int constant) {
        return QcaSettings.getTooExpensiveLevel() + 1;
    }

    @Inject(method = "getNextCost", at = @At("HEAD"), cancellable = true)
    private static void qca_modifyMultiplier(int i, CallbackInfoReturnable<Integer> cir) {
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
