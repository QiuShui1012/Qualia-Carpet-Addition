package zh.qiushui.mod.qca.rule.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class EntityUtil {
    public static <E extends Entity> Optional<E> getEntityWithFallback(
            World world, BlockPos pos, EntityType<E> type, EntityType<? extends E> fallback
    ) {
        Optional<E> entityOptional = getEntity(world, pos, type);
        if (entityOptional.isEmpty()) {
            return (Optional<E>) getEntity(world, pos, fallback);
        } else {
            return entityOptional;
        }
    }

    public static <E extends Entity> Optional<E> getEntity(World world, BlockPos pos, EntityType<E> type) {
        List<Entity> entities = world.getOtherEntities(null, new Box(pos));
        E entityOptional = null;

        for (Entity entity : entities) {
            if (entity.getType().equals(type)) {
                entityOptional = (E) entity;
            }
        }

        return Optional.ofNullable(entityOptional);
    }
}
