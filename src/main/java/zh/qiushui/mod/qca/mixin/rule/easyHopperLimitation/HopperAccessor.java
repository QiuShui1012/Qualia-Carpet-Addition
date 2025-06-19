package zh.qiushui.mod.qca.mixin.rule.easyHopperLimitation;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HopperBlockEntity.class)
public interface HopperAccessor {
    @Accessor("facing")
    Direction qca$getFacing();
}
