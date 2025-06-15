package zh.qiushui.mod.qca.api.section;

import net.minecraft.item.ItemStack;

public record ItemSection(ItemStack stack) implements Section {
    @Override
    public boolean test(ItemStack stack) {
        return ItemStack.areItemsAndComponentsEqual(stack, this.stack);
    }
}
