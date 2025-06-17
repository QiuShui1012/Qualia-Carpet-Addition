package zh.qiushui.mod.qca.rule.util.beaconIncreaseInteractionRange;

import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import zh.qiushui.mod.qca.QcaSettings;
import zh.qiushui.mod.qca.api.task.TaskManager;

import java.util.Objects;

public class BeaconUtil {
    public static final TaskManager<BlockPos> TASKS = new TaskManager<>();

    public static void tick() {
        if (QcaSettings.beaconIncreaseIsEnabled()) {
            TASKS.runQuests();
        }
    }

    public static void addBeaconIncreaseModifiersForPlayer(BlockPos pos, PlayerEntity player, int level) {
        TASKS.provideQuest(pos, () -> addBeaconIncreaseModifiersForPlayer(player, level));
    }

    public static void addBeaconIncreaseModifiersForPlayer(PlayerEntity player, int level) {
        if (player != null && player.isAlive() && player.getEntityWorld() != null) {
            Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE))
                .overwritePersistentModifier(InteractionRangeEntityAttributeModifiers.getBeaconBlockRangeModifier(level));
            Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE))
                .overwritePersistentModifier(InteractionRangeEntityAttributeModifiers.getBeaconEntityRangeModifier(level));
        }
    }

    public static void removeBeaconIncreaseModifiersForPlayer(BlockPos pos, PlayerEntity player) {
        TASKS.provideQuest(pos, () -> removeBeaconIncreaseModifiersForPlayer(player));
    }

    public static void removeBeaconIncreaseModifiersForPlayer(PlayerEntity player) {
        if (player != null && player.isAlive() && player.getEntityWorld() != null) {
            Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE))
                .removeModifier(InteractionRangeEntityAttributeModifiers.BEACON_BLOCK_RANGE_MODIFIER_IDENTIFIER);
            Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE))
                .removeModifier(InteractionRangeEntityAttributeModifiers.BEACON_ENTITY_RANGE_MODIFIER_IDENTIFIER);
        }
    }
}
