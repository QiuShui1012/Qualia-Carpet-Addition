package zh.qiushui.mod.qca.mixin.rule.easyHopperLimitation;

import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zh.qiushui.mod.qca.Qca;
import zh.qiushui.mod.qca.QcaServerRules;
import zh.qiushui.mod.qca.api.parse.ItemPredicateParser;
import zh.qiushui.mod.qca.api.section.AllSection;
import zh.qiushui.mod.qca.api.section.AnySection;
import zh.qiushui.mod.qca.api.section.ItemSection;
import zh.qiushui.mod.qca.api.section.Section;
import zh.qiushui.mod.qca.rule.util.EntityUtil;
import zh.qiushui.mod.qca.rule.util.easyHopperLimitation.HopperCache;
import zh.qiushui.mod.qca.util.UnsafeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Mixin(value = HopperBlockEntity.class, priority = 900)
public abstract class MixinHopperBlockEntity extends RandomizableContainerBlockEntity implements HopperCache {
    @Unique
    private Component qca$customNameCache = null;
    @Unique
    private Section qca$limitation = null;

    protected MixinHopperBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    public Component qca$getCache() {
        return this.qca$customNameCache;
    }

    @Override
    public void qca$setCache(Component cache) {
        this.qca$customNameCache = cache;
    }

    @Override
    public Section qca$getLimitation() {
        return this.qca$limitation;
    }

    @Override
    public void qca$setLimitation(Section limitation) {
        this.qca$limitation = limitation;
    }

    @Inject(method = "pushItemsTick", at = @At("TAIL"))
    private static void qca$updateLimitationOnTick(
        Level level, BlockPos pos, BlockState state, HopperBlockEntity hopper, CallbackInfo ci
    ) {
        if (!QcaServerRules.canLimit(QcaServerRules.easyHopperLimitation)) return;
        List<Section> sections = new ArrayList<>();
        if (QcaServerRules.canLimitByItemFrame(QcaServerRules.easyHopperLimitation)) {
            List<ItemFrame> frames = EntityUtil.getEntitiesIf(
                level, pos.above(),
                frame -> !frame.getDirection().equals(Direction.UP) || frame.getItem().isEmpty(),
                EntityType.ITEM_FRAME, EntityType.GLOW_ITEM_FRAME
            );
            if (!frames.isEmpty()) {
                sections.add(new AnySection(Lists.transform(frames, frame -> new ItemSection(frame.getItem()))));
                Qca.debugLog("(Tick) Tried to set the restrictor from the item frame.");
            }
        }
        if (QcaServerRules.canLimitByCustomName(QcaServerRules.easyHopperLimitation)) {
            Optional.ofNullable(hopper.getCustomName())
                .ifPresent(name -> {
                    if (!Objects.equals(UnsafeUtil.<HopperCache>cast(hopper).qca$getCache(), name)) {
                        UnsafeUtil.<HopperCache>cast(hopper).qca$setCache(name);
                        sections.add(ItemPredicateParser.parseItemPredicate(name.getString()).orElse(null));
                        Qca.debugLog("(Tick) Tried to set the restrictor from the custom name.");
                    }
                });
        }
        if (sections.size() > 1) {
            UnsafeUtil.<HopperCache>cast(hopper).qca$setLimitation(new AllSection(sections));
        } else if (sections.size() == 1) {
            UnsafeUtil.<HopperCache>cast(hopper).qca$setLimitation(sections.getFirst());
        }
    }

    @WrapOperation(
        method = "canTakeItemFromContainer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/Container;canTakeItem(Lnet/minecraft/world/Container;ILnet/minecraft/world/item/ItemStack;)Z"
        ))
    private static boolean qca$limitInsert(Container instance, Container target, int slot, ItemStack stack, Operation<Boolean> original) {
        return original.call(instance, target, slot, stack) && qca$limit(instance, stack);
    }

    @WrapOperation(
        method = "canPlaceItemInContainer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/Container;canPlaceItem(ILnet/minecraft/world/item/ItemStack;)Z"
        ))
    private static boolean qca$limitPlace(
        Container instance, int slot, ItemStack stack, Operation<Boolean> original
    ) {
        return original.call(instance, slot, stack) && qca$limit(instance, stack);
    }

    @Unique
    private static boolean qca$limit(Container inventory, ItemStack stack) {
        return !QcaServerRules.canLimit(QcaServerRules.easyHopperLimitation)
               || !(inventory instanceof HopperBlockEntity hopper)
               || UnsafeUtil.<HopperCache>cast(hopper).qca$getLimitation().test(stack);
    }
}
