package zh.qiushui.mod.qca.mixin.rule.easyHopperLimitation;

import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HopperBlockEntity.class)
public interface HopperAccessor {
    @Accessor("facing")
    Direction qca$getFacing();
}
