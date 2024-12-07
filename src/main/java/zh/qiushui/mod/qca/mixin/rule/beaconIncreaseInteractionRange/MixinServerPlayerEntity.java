package zh.qiushui.mod.qca.mixin.rule.beaconIncreaseInteractionRange;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zh.qiushui.mod.qca.rule.util.beaconIncreaseInteractionRange.PlayerUtil;

import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity {
    @Inject(method = "worldChanged", at = @At("TAIL"))
    private void qca_removeBeaconIncreaseModifiers(ServerWorld serverWorld, CallbackInfo ci) {
        AtomicBoolean contains = new AtomicBoolean(false);
        PlayerUtil.INCREASED_PLAYERS.values().forEach(set -> contains.set(set.contains((ServerPlayerEntity) (Object) this)));
        if (contains.get()) {
            PlayerUtil.removeBeaconIncreaseModifiersForPlayer((ServerPlayerEntity) (Object) this);
        }
    }
}
