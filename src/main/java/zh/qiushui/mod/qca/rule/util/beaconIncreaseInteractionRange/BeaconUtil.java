package zh.qiushui.mod.qca.rule.util.beaconIncreaseInteractionRange;

import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import zh.qiushui.mod.qca.QcaSettings;
import zh.qiushui.mod.qca.api.event.EventManager;

import java.util.Objects;

public class BeaconUtil {
    public static final EventManager<BlockPos> EVENTS = new EventManager<>();

    public static void tick() {
        if (QcaSettings.beaconIncreaseIsEnabled()) {
            EVENTS.runQuests();
        }
    }

    public static void addBeaconIncreaseModifiersForPlayer(BlockPos pos, PlayerEntity player, int level) {
        EVENTS.provideQuest(pos, () -> addBeaconIncreaseModifiersForPlayer(player, level));
    }
    public static void addBeaconIncreaseModifiersForPlayer(PlayerEntity player, int level) {
        if (player != null && player.isAlive() && player.getEntityWorld() != null) {
            Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.BLOCK_INTERACTION_RANGE))
                    .overwritePersistentModifier(InteractionRangeEntityAttributeModifiers.getBeaconBlockRangeModifier(level));
            Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.ENTITY_INTERACTION_RANGE))
                    .overwritePersistentModifier(InteractionRangeEntityAttributeModifiers.getBeaconEntityRangeModifier(level));
        }
    }

    public static void removeBeaconIncreaseModifiersForPlayer(BlockPos pos, PlayerEntity player) {
        EVENTS.provideQuest(pos, () -> removeBeaconIncreaseModifiersForPlayer(player));
    }
    public static void removeBeaconIncreaseModifiersForPlayer(PlayerEntity player) {
        if (player != null && player.isAlive() && player.getEntityWorld() != null) {
            Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.BLOCK_INTERACTION_RANGE))
                    .removeModifier(InteractionRangeEntityAttributeModifiers.BEACON_BLOCK_RANGE_MODIFIER_IDENTIFIER);
            Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.ENTITY_INTERACTION_RANGE))
                    .removeModifier(InteractionRangeEntityAttributeModifiers.BEACON_ENTITY_RANGE_MODIFIER_IDENTIFIER);
        }
    }
}
