package zh.qiushui.mod.qca.rule.util.easyHopperLimitation;

import net.minecraft.network.chat.Component;
import zh.qiushui.mod.qca.api.section.Section;

public interface HopperCache {
    default Component qca$getCache() {
        return Component.empty();
    }

    default void qca$setCache(Component cache) {
    }

    default Section qca$getLimitation() {
        return null;
    }

    default void qca$setLimitation(Section section) {
    }
}
