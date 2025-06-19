package zh.qiushui.mod.qca.api.section;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record TagSection(TagKey<Item> tag) implements Section {
    @Override
    public boolean test(ItemStack stack) {
        return stack.is(this.tag);
    }
}
