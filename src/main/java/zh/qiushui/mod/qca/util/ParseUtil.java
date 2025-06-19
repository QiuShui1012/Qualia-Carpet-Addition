package zh.qiushui.mod.qca.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.Optional;

public class ParseUtil {
    public static Optional<Item> parseItem(String idRaw) {
        return Optional.ofNullable(ResourceLocation.tryParse(idRaw))
            .map(BuiltInRegistries.ITEM::get)
            .filter(item -> !item.getDefaultInstance().isEmpty());
    }

    public static Optional<TagKey<Item>> parseItemTag(String idRaw) {
        if (idRaw.contains("#")) {
            idRaw = idRaw.substring(1);
        }

        return Optional.ofNullable(ResourceLocation.tryParse(idRaw))
            .map(id -> TagKey.create(Registries.ITEM, id))
            .filter(tag -> BuiltInRegistries.ITEM.getTagNames().anyMatch(tagKey -> tagKey.equals(tag)));
    }

    public static Optional<RecipeHolder<?>> parseCraftingRecipe(RecipeManager manager, String idRaw) {
        return Optional.ofNullable(ResourceLocation.tryParse(idRaw))
            .flatMap(id -> manager.getRecipes().stream()
                .filter(holder -> holder.id().equals(id))
                .findFirst());
    }
}
