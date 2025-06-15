package zh.qiushui.mod.qca.rule.util.beaconIncreaseInteractionRange;

import com.google.common.collect.Sets;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Set;

public interface IncreaseInteractionRange {
    default Set<PlayerEntity> qca$getIncreasedPlayers() {
        return Sets.newHashSet();
    }
}
