package com.qiushui1012.mod.qca.mixin.rule;

import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import com.qiushui1012.mod.qca.QcaServerRules;

@Mixin(AnvilScreen.class)
public class MixinAnvilScreen_tooExpensive {
    @ModifyConstant(
        method = "renderLabels",
        constant = @Constant(intValue = 40)
    )
    private int qca$modifyTooExpensiveLevel(int constant) {
        return QcaServerRules.getTooExpensiveLevel();
    }
}
