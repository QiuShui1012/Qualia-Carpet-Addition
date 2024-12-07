package zh.qiushui.mod.qca.rule.util.beaconIncreaseInteractionRange;

import com.google.common.collect.Maps;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.Map;
import java.util.Set;

public class PlayerUtil {
    public static final Map<BlockPos, Set<PlayerEntity>> INCREASED_PLAYERS = Maps.newHashMap();

    public static void addBeaconIncreaseModifiersForPlayer(PlayerEntity player, int level) {
        if (player != null && player.isAlive() && player.getEntityWorld() != null) {
            player.getAttributeInstance(EntityAttributes.BLOCK_INTERACTION_RANGE)
                    .addPersistentModifier(InteractionRangeEntityAttributeModifiers.getBeaconBlockRangeModifier(level));
            player.getAttributeInstance(EntityAttributes.ENTITY_INTERACTION_RANGE)
                    .addPersistentModifier(InteractionRangeEntityAttributeModifiers.getBeaconEntityRangeModifier(level));
        }
    }
    public static void removeBeaconIncreaseModifiersForPlayer(PlayerEntity player) {
        if (player != null && player.isAlive() && player.getEntityWorld() != null) {
            player.getAttributeInstance(EntityAttributes.BLOCK_INTERACTION_RANGE)
                    .removeModifier(InteractionRangeEntityAttributeModifiers.BEACON_BLOCK_RANGE_MODIFIER_IDENTIFIER);
            player.getAttributeInstance(EntityAttributes.ENTITY_INTERACTION_RANGE)
                    .removeModifier(InteractionRangeEntityAttributeModifiers.BEACON_ENTITY_RANGE_MODIFIER_IDENTIFIER);
        }
    }
}
