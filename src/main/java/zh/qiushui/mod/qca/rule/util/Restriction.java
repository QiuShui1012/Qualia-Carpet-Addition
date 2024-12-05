package zh.qiushui.mod.qca.rule.util;

import com.google.common.collect.Lists;
import lombok.Setter;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import zh.qiushui.mod.qca.QcaExtension;

import java.util.List;
import java.util.function.Predicate;

public class Restriction {
    protected Predicate<Item> restrictor;

    private Restriction() {
        this.restrictor = null;
    }
    private Restriction(Predicate<Item> restrictor) {
        this.restrictor = restrictor;
    }

    public static Restriction empty() {
        return new Restriction();
    }
    public static Restriction of(Predicate<Item> restrictor) {
        return new Restriction(restrictor);
    }
    public static Restriction ofItem(Item restrictor) {
        return new Restriction(value -> value.equals(restrictor));
    }
    public static Restriction ofTag(TagKey<Item> restrictor) {
        return new Restriction(value -> Registries.ITEM.getEntry(value).isIn(restrictor));
    }

    public boolean restrict(ItemStack value) {
        try {
            return this.restrictor.test(value.getItem());
        } catch (NullPointerException ignored) {
            return true;
        }
    }

    public void setRestrictor(@Nullable String idRaw) {
        if (idRaw != null) {
            if (idRaw.startsWith("#")) {
                this.restrictor = parseTagRestrictor(idRaw);
            } else {
                this.restrictor = parseItemRestrictor(idRaw);
            }
        }
    }

    public static Predicate<Item> parseTagRestrictor(String idRaw) {
        String idRawTrimmed = idRaw.substring(1);
        Identifier restrictorId = Identifier.tryParse(idRawTrimmed);
        return restrictorId != null ? (Item item) -> Registries.ITEM.getEntry(item).isIn(TagKey.of(RegistryKeys.ITEM, restrictorId)) : null;
    }

    public static Predicate<Item> parseItemRestrictor(String idRaw) {
        Item restrictor = Registries.ITEM.get(Identifier.tryParse(idRaw));
        return !restrictor.equals(Items.AIR) ? (Item item) -> item.equals(restrictor) : null;
    }
}
