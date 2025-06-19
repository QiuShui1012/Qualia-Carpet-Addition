package zh.qiushui.mod.qca.mixin.rule.crafterLimitation;

import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.FrontAndTop;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CrafterBlock;
import net.minecraft.world.level.block.entity.CrafterBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(CrafterBlock.class)
public abstract class MixinCrafterBlock {
    @Shadow @Final private static EnumProperty<FrontAndTop> ORIENTATION;
    @Unique
    private Section qca$limitation = null;

    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/CrafterBlock;"
                     + "dispenseFrom(Lnet/minecraft/world/level/block/state/BlockState;"
                     + "Lnet/minecraft/server/level/ServerLevel;"
                     + "Lnet/minecraft/core/BlockPos;)V")
    )
    @SuppressWarnings("LoggingSimilarMessage")
    private void qca$updateLimitation(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (!QcaServerRules.canLimit(QcaServerRules.crafterLimitation)) return;
        List<Section> sections = new ArrayList<>();
        if (QcaServerRules.canLimitByItemFrame(QcaServerRules.crafterLimitation)) {
            List<ItemFrame> frames = EntityUtil.getEntitiesIf(
                level, pos.relative(state.getValue(ORIENTATION).top()),
                frame -> !frame.getDirection().equals(state.getValue(ORIENTATION).top()) || frame.getItem().isEmpty(),
                EntityType.ITEM_FRAME, EntityType.GLOW_ITEM_FRAME
            );
            if (!frames.isEmpty()) {
                sections.add(new AnySection(Lists.transform(frames, frame -> new ItemSection(frame.getItem()))));
            }
        }
        if (QcaServerRules.canLimitByCustomName(QcaServerRules.crafterLimitation)) {
            if (level.getBlockEntity(pos) instanceof CrafterBlockEntity entity) {
                Optional.ofNullable(entity.getCustomName())
                    .map(Component::getString)
                    .flatMap(ItemPredicateParser::parseItemPredicate)
                    .ifPresent(sections::add);
            }
        }

        if (sections.isEmpty()) {
            this.qca$limitation = null;
            if (QcaServerRules.qcaDebugLog) {
                Qca.LOGGER.debug("A crafter located at {} reset its restrictor.", pos);
            }
            return;
        }
        if (sections.size() > 1) {
            this.qca$limitation = new AllSection(sections);
        }
        this.qca$limitation = sections.getFirst();
        if (QcaServerRules.qcaDebugLog) {
            Qca.LOGGER.debug("A crafter located at {} updated its limit source {}.", pos, this.qca$limitation);
        }
    }

    @ModifyExpressionValue(
        method = "dispenseFrom",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z", ordinal = 0))
    private boolean qca$limitResult(boolean isEmpty, @Local(ordinal = 0) ItemStack stack) {
        if (!QcaServerRules.canLimit(QcaServerRules.crafterLimitation)) return isEmpty;
        boolean matched = this.qca$limitation.test(stack);
        if (QcaServerRules.qcaDebugLog) {
            Qca.LOGGER.debug(
                "A crafter just limited. Input: {}, Result: {}",
                stack, isEmpty ? "Failed. The instance is empty." : matched ? "Successfully limited." : "Failed."
            );
        }
        return isEmpty || !matched;
    }
}
