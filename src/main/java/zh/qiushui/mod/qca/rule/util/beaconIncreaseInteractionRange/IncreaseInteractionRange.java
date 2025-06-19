package zh.qiushui.mod.qca.rule.util.beaconIncreaseInteractionRange;

import com.google.common.collect.Sets;
import net.minecraft.world.entity.player.Player;

import java.util.Set;

public interface IncreaseInteractionRange {
    default Set<Player> qca$getIncreasedPlayers() {
        return Sets.newHashSet();
    }
}
