package zh.qiushui.mod.qca.rule.util;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

public class EntityUtil {
    public static <E extends Entity> List<E> getEntities(World world, BlockPos pos, EntityType<E> type) {
        return getEntities(world, new Box(pos), type);
    }

    public static <E extends Entity> List<E> getEntities(World world, Box box, EntityType<E> type) {
        return world.getEntitiesByType(type, box, player -> true);
    }

    @SafeVarargs
    public static <E extends Entity> List<E> getEntities(
        World world, BlockPos pos, EntityType<? extends E>... types
    ) {
        return getEntities(world, new Box(pos), types);
    }

    @SafeVarargs
    public static <E extends Entity> List<E> getEntities(
        World world, Box box, EntityType<? extends E>... types
    ) {
        List<E> entities = Lists.newArrayList();
        for (EntityType<? extends E> type : types) {
            entities.addAll(getEntities(world, box, type));
        }
        return entities;
    }

    public static <E extends Entity> List<E> getEntitiesIf(
        World world, BlockPos pos, EntityType<E> type, Predicate<E> filter
    ) {
        return getEntitiesIf(world, new Box(pos), type, filter);
    }

    public static <E extends Entity> List<E> getEntitiesIf(
        World world, Box box, EntityType<E> type, Predicate<E> filter
    ) {
        List<E> entities = getEntities(world, box, type);
        entities.removeIf(filter);
        return entities;
    }

    @SafeVarargs
    public static <E extends Entity> List<E> getEntitiesIf(
        World world, BlockPos pos, Predicate<E> filter, EntityType<? extends E>... types
    ) {
        return getEntitiesIf(world, new Box(pos), filter, types);
    }

    @SafeVarargs
    public static <E extends Entity> List<E> getEntitiesIf(
        World world, Box box, Predicate<E> filter, EntityType<? extends E>... types
    ) {
        List<E> entities = getEntities(world, box, types);
        entities.removeIf(filter);
        return entities;
    }
}
