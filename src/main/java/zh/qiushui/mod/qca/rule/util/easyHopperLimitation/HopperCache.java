package zh.qiushui.mod.qca.rule.util.easyHopperLimitation;

import net.minecraft.text.Text;
import zh.qiushui.mod.qca.api.section.Section;

public interface HopperCache {
    default Text qca$getCache() {
        return Text.empty();
    }

    default void qca$setCache(Text cache) {
    }

    default Section qca$getSection() {
        return null;
    }

    default void qca$setSection(Section section) {
    }
}
