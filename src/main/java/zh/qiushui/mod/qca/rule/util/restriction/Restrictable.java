package zh.qiushui.mod.qca.rule.util.restriction;

import net.minecraft.item.ItemStack;

public interface Restrictable {
    default void qca_setRestrictor(String idRaw) {}

    default boolean qca_restrict(ItemStack value) {
        return true;
    }
}
