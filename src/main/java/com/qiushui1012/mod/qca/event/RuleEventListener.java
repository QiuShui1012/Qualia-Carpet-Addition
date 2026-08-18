package com.qiushui1012.mod.qca.event;

import com.qiushui1012.mod.qca.QcaExtension;
import com.qiushui1012.mod.qca.QcaServerRules;
import com.qiushui1012.mod.qca.util.rule.PlantTransformRecord;
import com.qiushui1012.mod.qca.util.rule.ShearUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BigDripleafStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.BonemealEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

//#if MC <= 12101
//$$ import net.neoforged.neoforge.event.entity.living.ArmorHurtEvent;
//$$ import net.minecraft.world.damagesource.DamageSource;
//$$ import net.minecraft.world.entity.player.Player;
//#endif

@EventBusSubscriber(modid = QcaExtension.MOD_ID)
public class RuleEventListener {
    @SubscribeEvent
    public static void tallPlantShearToSmall(PlayerInteractEvent.RightClickBlock event) {
        if (QcaServerRules.tallPlantShearToSmall.equals("disable")) return;

        ItemStack stack = event.getItemStack();
        if (!stack.is(Items.SHEARS)) return;

        Level level = event.getLevel();
        if (level.isClientSide()) return;
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);

        switch (QcaServerRules.matchTallPlant(state.getBlock())) {
            case QcaServerRules.GRASSES, QcaServerRules.FLOWERS -> ShearUtil.shearTallPlant(
                level,
                pos,
                state,
                event.getEntity(),
                stack,
                event.getHand()
            );
            case QcaServerRules.DRIPLEAF -> ShearUtil.shearBigDripleaf(
                level,
                pos,
                state,
                event.getEntity(),
                stack,
                event.getHand()
            );
        }
        event.setCancellationResult(InteractionResult.CONSUME);
    }

    @SubscribeEvent
    public static void fertilizableSmallFlowers(BonemealEvent event) {
        if (!QcaServerRules.fertilizableSmallFlowers) return;

        Level level = event.getLevel();
        if (level.isClientSide()) return;
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);

        for (Block smallPlant : PlantTransformRecord.SMALL_TALL_FLOWERS.keySet()) {
            if (!state.is(smallPlant)) continue;

            if (event.getPlayer() == null || !event.getPlayer().hasInfiniteMaterials()) event.getStack().shrink(1);
            Block.popResource(level, pos, new ItemStack(smallPlant));
            level.levelEvent(1505, event.getPos(), 15);

            event.setCanceled(true);
            event.setSuccessful(true);
            return;
        }
    }

    @SubscribeEvent
    public static void breakDripleafKeepStem(BlockEvent.NeighborNotifyEvent event) {
        if (!QcaServerRules.breakDripleafKeepStem) return;

        LevelAccessor level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof BigDripleafStemBlock)) return;
        BlockState up = level.getBlockState(pos.above());
        if (up.getBlock().equals(Blocks.BIG_DRIPLEAF)) return;
        level.setBlock(
            pos,
            Blocks.BIG_DRIPLEAF.defaultBlockState().setValue(FACING, state.getValue(FACING)),
            Block.UPDATE_ALL
        );
        event.setCanceled(true);
    }

    //#if MC <= 12101
    //$$ @SubscribeEvent
    //$$ public static void pvpDoNotDamageEquipment(ArmorHurtEvent event) {
    //$$     if (!QcaServerRules.pvpDoNotDamageEquipment) return;
    //$$     DamageSource source = event.getDamageSource();
    //$$     event.setCanceled(source.getDirectEntity() instanceof Player && source.getEntity() instanceof Player);
    //$$ }
    //#endif
}
