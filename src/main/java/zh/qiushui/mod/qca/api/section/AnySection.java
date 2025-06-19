package zh.qiushui.mod.qca.api.section;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public record AnySection(List<Section> sections) implements Section {
    @Override
    public boolean test(ItemStack stack) {
        for (Section section : sections) {
            if (section.test(stack)) return true;
        }
        return false;
    }
}
