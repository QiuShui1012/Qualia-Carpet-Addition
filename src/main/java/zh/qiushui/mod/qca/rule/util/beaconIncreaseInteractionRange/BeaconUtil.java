package zh.qiushui.mod.qca.rule.util.beaconIncreaseInteractionRange;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import zh.qiushui.mod.qca.QcaExtension;
import zh.qiushui.mod.qca.QcaSettings;
import zh.qiushui.mod.qca.api.task.TaskManager;

import java.util.Objects;

public class BeaconUtil {
    public static final TaskManager<BlockPos> TASKS = new TaskManager<>();
    public static final ResourceLocation BEACON_BLOCK_RANGE_ID = QcaExtension.id("beacon_effected_block_range");
    public static final ResourceLocation BEACON_ENTITY_RANGE_ID = QcaExtension.id("beacon_effected_entity_range");

    public static void tick() {
        if (QcaSettings.beaconIncreaseIsEnabled()) {
            TASKS.runQuests();
        }
    }

    public static void addBeaconIncreaseModifiersForPlayer(BlockPos pos, Player player, int level) {
        TASKS.provideQuest(pos, () -> addBeaconIncreaseModifiersForPlayer(player, level));
    }

    public static void addBeaconIncreaseModifiersForPlayer(Player player, int level) {
        if (player != null && player.isAlive()) {
            Objects.requireNonNull(player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE))
                .addOrReplacePermanentModifier(getBeaconBlockRangeModifier(level));
            Objects.requireNonNull(player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE))
                .addOrReplacePermanentModifier(getBeaconEntityRangeModifier(level));
        }
    }

    public static void removeBeaconIncreaseModifiersForPlayer(BlockPos pos, Player player) {
        TASKS.provideQuest(pos, () -> removeBeaconIncreaseModifiersForPlayer(player));
    }

    public static void removeBeaconIncreaseModifiersForPlayer(Player player) {
        if (player != null && player.isAlive()) {
            Objects.requireNonNull(player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE))
                .removeModifier(BEACON_BLOCK_RANGE_ID);
            Objects.requireNonNull(player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE))
                .removeModifier(BEACON_ENTITY_RANGE_ID);
        }
    }

    public static AttributeModifier getBeaconBlockRangeModifier(int beaconLevel) {
        return new AttributeModifier(
            BEACON_BLOCK_RANGE_ID,
            QcaSettings.getBeaconIncreaseInteractionRangeValue(beaconLevel),
            QcaSettings.beaconIncreaseMode()
        );
    }

    public static AttributeModifier getBeaconEntityRangeModifier(int beaconLevel) {
        return new AttributeModifier(
            BEACON_ENTITY_RANGE_ID,
            QcaSettings.getBeaconIncreaseInteractionRangeValue(beaconLevel),
            QcaSettings.beaconIncreaseMode()
        );
    }
}
