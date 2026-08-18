package com.qiushui1012.mod.qca.mixin.rule;

import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import com.qiushui1012.mod.qca.QcaServerRules;

@Mixin(AnvilScreen.class)
public class MixinAnvilScreen_tooExpensive {
    @ModifyConstant(
        //#if MC < 26_01_00
        //$$ method = "renderLabels",
        //#else
        method = "extractLabels",
        //#endif
        constant = @Constant(intValue = 40)
    )
    private int qca$modifyTooExpensiveLevel(int constant) {
        return QcaServerRules.getTooExpensiveLevel();
    }
}
