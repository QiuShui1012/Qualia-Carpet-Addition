package zh.qiushui.mod.qca.rule.util.beaconIncreaseInteractionRange;

import com.google.common.collect.Sets;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Set;

public interface IncreaseInteractionRange {
    default Set<PlayerEntity> qca_getIncreasedPlayers() {
        return Sets.newHashSet();
    }

    default void qca_addIncreasedPlayer(PlayerEntity player) {}
    default void qca_removeIncreasedPlayer(PlayerEntity player) {}
}
