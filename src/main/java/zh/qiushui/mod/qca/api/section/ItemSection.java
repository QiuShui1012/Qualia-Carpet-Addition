package zh.qiushui.mod.qca.api.section;

import net.minecraft.world.item.ItemStack;

public record ItemSection(ItemStack stack) implements Section {
    @Override
    public boolean test(ItemStack stack) {
        return ItemStack.isSameItemSameComponents(stack, this.stack);
    }
}
