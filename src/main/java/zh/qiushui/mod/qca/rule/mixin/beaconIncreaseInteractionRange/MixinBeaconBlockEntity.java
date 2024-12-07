package zh.qiushui.mod.qca.rule.mixin.beaconIncreaseInteractionRange;

import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zh.qiushui.mod.qca.QcaSettings;
import zh.qiushui.mod.qca.rule.util.beaconIncreaseInteractionRange.IncreaseInteractionRange;
import zh.qiushui.mod.qca.rule.util.beaconIncreaseInteractionRange.InteractionRangeEntityAttributeModifiers;

import java.util.List;
import java.util.Set;

@Mixin(BeaconBlockEntity.class)
public abstract class MixinBeaconBlockEntity implements IncreaseInteractionRange {
    @Shadow int level;
    @Unique private final Set<PlayerEntity> increasedPlayers = Sets.newHashSet();

    @Override
    public Set<PlayerEntity> qca_getIncreasedPlayers() {
        return this.increasedPlayers;
    }
    @Override
    public void qca_addIncreasedPlayer(PlayerEntity player) {
        this.increasedPlayers.add(player);
    }
    @Override
    public void qca_removeIncreasedPlayer(PlayerEntity player) {
        this.increasedPlayers.remove(player);
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
        for (PlayerEntity player : this.increasedPlayers) {
            qca_removeModifierForPlayer(player, this.level);
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
                    qca_removeModifierForPlayer(player, level);
                    beacon.qca_removeIncreasedPlayer(player);
                    inRangeAndNeedIncreasePlayers.remove(player);
                }
            }

            for (PlayerEntity player : inRangeAndNeedIncreasePlayers) {
                qca_removeModifierForPlayer(player, level);
                qca_addModifierForPlayer(player, level);

                beacon.qca_addIncreasedPlayer(player);
            }
        }
    }

    @Unique
    private static void qca_addModifierForPlayer(PlayerEntity player, int level) {
        player.getAttributeInstance(EntityAttributes.BLOCK_INTERACTION_RANGE)
                .addPersistentModifier(InteractionRangeEntityAttributeModifiers.getBeaconBlockRangeModifier(level));
        player.getAttributeInstance(EntityAttributes.ENTITY_INTERACTION_RANGE)
                .addPersistentModifier(InteractionRangeEntityAttributeModifiers.getBeaconEntityRangeModifier(level));
    }
    @Unique
    private static void qca_removeModifierForPlayer(PlayerEntity player, int level) {
        player.getAttributeInstance(EntityAttributes.BLOCK_INTERACTION_RANGE)
                .removeModifier(InteractionRangeEntityAttributeModifiers.getPreviousBeaconBlockRangeModifier(level));
        player.getAttributeInstance(EntityAttributes.ENTITY_INTERACTION_RANGE)
                .removeModifier(InteractionRangeEntityAttributeModifiers.getPreviousBeaconEntityRangeModifier(level));
    }
}
