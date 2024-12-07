package zh.qiushui.mod.qca.mixin.rule.tooExpensive;

import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import zh.qiushui.mod.qca.QcaSettings;

@Mixin(AnvilScreen.class)
public class MixinAnvilScreen {
    @ModifyConstant(method = "drawForeground", constant = @Constant(intValue = 40))
    private int qca_modifyTooExpensiveLevel(int constant) {
        return QcaSettings.getTooExpensiveLevel();
    }
}
