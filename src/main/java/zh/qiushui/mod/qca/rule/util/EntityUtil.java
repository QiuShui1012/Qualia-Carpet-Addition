package zh.qiushui.mod.qca.rule.util;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.function.Predicate;

public class EntityUtil {
    public static <E extends Entity> List<E> getEntities(Level level, AABB box, EntityType<E> type) {
        return level.getEntities(type, box, player -> true);
    }

    @SafeVarargs
    public static <E extends Entity> List<E> getEntities(Level world, AABB box, EntityType<? extends E>... types) {
        List<E> entities = Lists.newArrayList();
        for (EntityType<? extends E> type : types) {
            entities.addAll(getEntities(world, box, type));
        }
        return entities;
    }

    @SafeVarargs
    public static <E extends Entity> List<E> getEntitiesIf(
        Level level, BlockPos pos, Predicate<E> filter, EntityType<? extends E>... types
    ) {
        return getEntitiesIf(level, new AABB(pos), filter, types);
    }

    @SafeVarargs
    public static <E extends Entity> List<E> getEntitiesIf(Level level, AABB box, Predicate<E> filter, EntityType<? extends E>... types) {
        List<E> entities = getEntities(level, box, types);
        entities.removeIf(filter);
        return entities;
    }
}
