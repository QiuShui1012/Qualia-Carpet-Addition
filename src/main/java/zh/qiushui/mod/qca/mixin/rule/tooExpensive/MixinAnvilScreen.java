package zh.qiushui.mod.qca.mixin.rule.tooExpensive;

import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import zh.qiushui.mod.qca.QcaSettings;

@Mixin(AnvilScreen.class)
public class MixinAnvilScreen {
    @ModifyConstant(method = "renderLabels", constant = @Constant(intValue = 40))
    private int qca$modifyTooExpensiveLevel(int constant) {
        return QcaSettings.getTooExpensiveLevel();
    }
}
