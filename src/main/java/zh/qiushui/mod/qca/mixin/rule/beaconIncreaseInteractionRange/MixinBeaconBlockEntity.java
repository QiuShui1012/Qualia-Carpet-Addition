package zh.qiushui.mod.qca.mixin.rule.beaconIncreaseInteractionRange;

import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zh.qiushui.mod.qca.QcaSettings;
import zh.qiushui.mod.qca.rule.util.beaconIncreaseInteractionRange.IncreaseInteractionRange;
import zh.qiushui.mod.qca.rule.util.beaconIncreaseInteractionRange.PlayerUtil;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Mixin(BeaconBlockEntity.class)
public abstract class MixinBeaconBlockEntity extends BlockEntity implements IncreaseInteractionRange {
    public MixinBeaconBlockEntity(
            BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState
    ) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    public Set<PlayerEntity> qca_getIncreasedPlayers() {
        return Objects.requireNonNullElseGet(
                PlayerUtil.INCREASED_PLAYERS.get(this.pos),
                () -> {
                    PlayerUtil.INCREASED_PLAYERS.put(this.pos, Sets.newHashSet());
                    return PlayerUtil.INCREASED_PLAYERS.get(this.pos);
                }
        );
    }
    @Override
    public void qca_addIncreasedPlayer(PlayerEntity player) {
        PlayerUtil.INCREASED_PLAYERS.get(this.pos).add(player);
    }
    @Override
    public void qca_removeIncreasedPlayer(PlayerEntity player) {
        PlayerUtil.INCREASED_PLAYERS.get(this.pos).remove(player);
    }

    @Inject(
            method = "tick", at = @At(
                    value = "INVOKE", target = "Lnet/minecraft/block/entity/BeaconBlockEntity;" +
                                               "applyPlayerEffects(Lnet/minecraft/world/World;" +
                                               "Lnet/minecraft/util/math/BlockPos;" +
                                               "ILnet/minecraft/registry/entry/RegistryEntry;" +
                                               "Lnet/minecraft/registry/entry/RegistryEntry;)" +
                                               "V",
                    shift = At.Shift.AFTER
            )
    )
    private static void qca_increasePlayersInteractionRange(
            World world, BlockPos pos, BlockState state, BeaconBlockEntity beacon, CallbackInfo ci
    ) {
        if (QcaSettings.beaconIncreaseIsEnabled()) {
            qca_increasePlayersInteractionRange(world, pos, beacon);
        }
    }
    @Inject(method = "markRemoved", at = @At("TAIL"))
    private void qca_removeModifiersOfPlayers(CallbackInfo ci) {
        for (PlayerEntity player : this.qca_getIncreasedPlayers()) {
            PlayerUtil.removeBeaconIncreaseModifiersForPlayer(player);
        }
    }

    @Unique
    private static void qca_increasePlayersInteractionRange(World world, BlockPos pos, BeaconBlockEntity beacon) {
        if (!world.isClient) {
            int level = ((BeaconBlockEntityAccessor) beacon).qca_getLevel();
            double rangeWidth = level * 10 + 10;
            Box range = new Box(pos).expand(rangeWidth).stretch(0, world.getHeight(), 0);

            List<PlayerEntity> inRangeAndNeedIncreasePlayers = world.getEntitiesByClass(PlayerEntity.class, range, player -> true);

            for (PlayerEntity player : beacon.qca_getIncreasedPlayers()) {
                if (!inRangeAndNeedIncreasePlayers.contains(player)) {
                    PlayerUtil.removeBeaconIncreaseModifiersForPlayer(player);
                    beacon.qca_removeIncreasedPlayer(player);
                    inRangeAndNeedIncreasePlayers.remove(player);
                }
            }

            for (PlayerEntity player : inRangeAndNeedIncreasePlayers) {
                PlayerUtil.removeBeaconIncreaseModifiersForPlayer(player);
                PlayerUtil.addBeaconIncreaseModifiersForPlayer(player, level);

                beacon.qca_addIncreasedPlayer(player);
            }
        }
    }
}
