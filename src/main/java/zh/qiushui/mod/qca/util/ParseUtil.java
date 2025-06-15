package zh.qiushui.mod.qca.util;

import net.minecraft.item.Item;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ParseUtil {
    public static Optional<Item> parseItem(String idRaw) {
        return Optional.ofNullable(Identifier.tryParse(idRaw))
            .map(Registries.ITEM::get)
            .filter(item -> !item.getDefaultStack().isEmpty());
    }

    public static Optional<TagKey<Item>> parseItemTag(String idRaw) {
        if (idRaw.contains("#")) {
            idRaw = idRaw.substring(1);
        }

        return Optional.ofNullable(Identifier.tryParse(idRaw))
            .map(id -> TagKey.of(RegistryKeys.ITEM, id))
            .filter(tag -> Registries.ITEM.streamTagKeys().anyMatch(tagKey -> tagKey.equals(tag)));
    }

    public static Optional<RecipeEntry<?>> parseCraftingRecipe(ServerRecipeManager manager, String idRaw) {
        return Optional.ofNullable(Identifier.tryParse(idRaw))
            .flatMap(identifier -> manager.values().stream()
                .filter(entry -> entry.id().getValue().equals(identifier))
                .findFirst());
    }
}
