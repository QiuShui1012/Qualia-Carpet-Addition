package zh.qiushui.mod.qca.mixin.rule.beaconIncreaseInteractionRange;

import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
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
import zh.qiushui.mod.qca.rule.util.EntityUtil;
import zh.qiushui.mod.qca.rule.util.beaconIncreaseInteractionRange.BeaconUtil;
import zh.qiushui.mod.qca.rule.util.beaconIncreaseInteractionRange.IncreaseInteractionRange;

import java.util.List;
import java.util.Set;

@Mixin(BeaconBlockEntity.class)
public abstract class MixinBeaconBlockEntity extends BlockEntity implements IncreaseInteractionRange {
    @Unique
    private final Set<PlayerEntity> increasedPlayers = Sets.newConcurrentHashSet();

    public MixinBeaconBlockEntity(
            BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState
    ) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    public Set<PlayerEntity> qca_getIncreasedPlayers() {
        return this.increasedPlayers;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void registerIdentifier(BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
        BeaconUtil.EVENTS.register(blockPos);
    }

    @Inject(
            method = "tick", at = @At(
                    value = "INVOKE", target = "Lnet/minecraft/block/entity/BeaconBlockEntity;" +
                                               "applyPlayerEffects(Lnet/minecraft/world/World;" +
                                               "Lnet/minecraft/util/math/BlockPos;" +
                                               "ILnet/minecraft/registry/entry/RegistryEntry;" +
                                               "Lnet/minecraft/registry/entry/RegistryEntry;)" +
                                               "V"
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
        BeaconUtil.EVENTS.remove(this.pos);
        for (PlayerEntity player : this.qca_getIncreasedPlayers()) {
            BeaconUtil.removeBeaconIncreaseModifiersForPlayer(this.pos, player);
        }
    }

    @Unique
    private static void qca_increasePlayersInteractionRange(World world, BlockPos pos, BeaconBlockEntity beacon) {
        if (!world.isClient) {
            int level = ((BeaconBlockEntityAccessor) beacon).qca_getLevel();
            double rangeWidth = level * 10 + 10;
            Box range = new Box(pos).expand(rangeWidth).stretch(0, world.getHeight(), 0);

            List<PlayerEntity> inRangePlayers = EntityUtil.getEntities(world, range, EntityType.PLAYER);

            for (PlayerEntity player : beacon.qca_getIncreasedPlayers()) {
                BeaconUtil.removeBeaconIncreaseModifiersForPlayer(beacon.getPos(), player);
            }

            for (PlayerEntity player : inRangePlayers) {
                BeaconUtil.addBeaconIncreaseModifiersForPlayer(beacon.getPos(), player, level);

                beacon.qca_getIncreasedPlayers().add(player);
            }
        }
    }
}
