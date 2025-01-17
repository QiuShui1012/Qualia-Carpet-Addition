package zh.qiushui.mod.qca.mixin.rule.beaconIncreaseInteractionRange;

import net.minecraft.block.entity.BeaconBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BeaconBlockEntity.class)
public interface BeaconBlockEntityAccessor {
    @Accessor("level")
    int qca_getLevel();
}
