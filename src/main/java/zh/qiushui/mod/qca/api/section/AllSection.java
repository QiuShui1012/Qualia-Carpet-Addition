package zh.qiushui.mod.qca.api.section;

import net.minecraft.item.ItemStack;

import java.util.List;

public record AllSection(List<Section> sections) implements Section {
    @Override
    public boolean test(ItemStack stack) {
        for (Section section : sections) {
            if (!section.test(stack)) return false;
        }
        return true;
    }
}
