package zh.qiushui.mod.qca.api.section;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;

public record TagSection(TagKey<Item> tag) implements Section {
    @Override
    public boolean test(ItemStack stack) {
        return stack.isIn(this.tag);
    }
}
