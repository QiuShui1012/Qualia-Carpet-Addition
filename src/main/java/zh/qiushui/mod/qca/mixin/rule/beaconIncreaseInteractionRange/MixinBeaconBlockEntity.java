package zh.qiushui.mod.qca.mixin.rule.beaconIncreaseInteractionRange;

import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zh.qiushui.mod.qca.Qca;
import zh.qiushui.mod.qca.QcaServerRules;
import zh.qiushui.mod.qca.rule.util.EntityUtil;
import zh.qiushui.mod.qca.rule.util.beaconIncreaseInteractionRange.BeaconUtil;
import zh.qiushui.mod.qca.rule.util.beaconIncreaseInteractionRange.IncreaseInteractionRange;

import java.util.List;
import java.util.Set;

@Mixin(BeaconBlockEntity.class)
public abstract class MixinBeaconBlockEntity extends BlockEntity implements IncreaseInteractionRange {
    @Unique
    private final Set<Player> qca$increasedPlayers = Sets.newConcurrentHashSet();

    public MixinBeaconBlockEntity(
        BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState
    ) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    public Set<Player> qca$getIncreasedPlayers() {
        return this.qca$increasedPlayers;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void registerIdentifier(BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
        BeaconUtil.TASKS.register(blockPos);
        if (QcaServerRules.qcaDebugLog) {
            Qca.LOGGER.debug("Tried to register an identifier to the beacon task manager.");
        }
    }

    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/entity/BeaconBlockEntity;"
                     + "applyEffects(Lnet/minecraft/world/level/Level;"
                     + "Lnet/minecraft/core/BlockPos;"
                     + "ILnet/minecraft/core/Holder;"
                     + "Lnet/minecraft/core/Holder;)V"
    ))
    private static void qca$increasePlayersInteractionRange(
        Level level, BlockPos pos, BlockState state, BeaconBlockEntity beacon, CallbackInfo ci
    ) {
        if (QcaServerRules.beaconIncreaseIsEnabled()) {
            qca$increasePlayersInteractionRange(level, pos, beacon);
        }
    }

    @Inject(method = "setRemoved", at = @At("TAIL"))
    private void qca$removeModifiersOfPlayers(CallbackInfo ci) {
        for (Player player : this.qca$getIncreasedPlayers()) {
            BeaconUtil.removeBeaconIncreaseModifiersForPlayer(this.getBlockPos(), player);
        }
        BeaconUtil.TASKS.remove(this.getBlockPos());
        if (QcaServerRules.qcaDebugLog) {
            Qca.LOGGER.debug("Tried to remove this identifier from the beacon task manager.");
        }
    }

    @Unique
    private static void qca$increasePlayersInteractionRange(Level level, BlockPos pos, BeaconBlockEntity beacon) {
        if (!level.isClientSide) {
            int levels = ((BeaconBlockEntityAccessor) beacon).qca$getLevel();
            double rangeWidth = levels * 10 + 10;
            AABB range = new AABB(pos).inflate(rangeWidth).expandTowards(0, level.getHeight(), 0);

            List<Player> inRangePlayers = EntityUtil.getEntities(level, range, EntityType.PLAYER);

            for (Player player : ((IncreaseInteractionRange) beacon).qca$getIncreasedPlayers()) {
                BeaconUtil.removeBeaconIncreaseModifiersForPlayer(beacon.getBlockPos(), player);
            }
            if (QcaServerRules.qcaDebugLog) {
                Qca.LOGGER.debug("(Tick) Tried to remove the modifier from the increased players.");
            }

            for (Player player : inRangePlayers) {
                BeaconUtil.addBeaconIncreaseModifiersForPlayer(beacon.getBlockPos(), player, levels);

                ((IncreaseInteractionRange) beacon).qca$getIncreasedPlayers().add(player);
            }
            if (QcaServerRules.qcaDebugLog) {
                Qca.LOGGER.debug("(Tick) Tried to add the modifier to the players in range.");
            }
        }
    }
}
