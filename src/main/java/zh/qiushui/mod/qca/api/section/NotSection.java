package zh.qiushui.mod.qca.api.section;

import net.minecraft.item.ItemStack;

public record NotSection(Section child) implements Section {
    @Override
    public boolean test(ItemStack stack) {
        return !child.test(stack);
    }
}
