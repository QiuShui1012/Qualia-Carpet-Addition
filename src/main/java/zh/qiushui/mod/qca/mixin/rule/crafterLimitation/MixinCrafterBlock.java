package zh.qiushui.mod.qca.mixin.rule.crafterLimitation;

import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.CrafterBlock;
import net.minecraft.block.entity.CrafterBlockEntity;
import net.minecraft.block.enums.Orientation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zh.qiushui.mod.qca.QcaExtension;
import zh.qiushui.mod.qca.QcaSettings;
import zh.qiushui.mod.qca.api.parse.ItemPredicateParser;
import zh.qiushui.mod.qca.api.section.AllSection;
import zh.qiushui.mod.qca.api.section.ItemSection;
import zh.qiushui.mod.qca.api.section.AnySection;
import zh.qiushui.mod.qca.api.section.Section;
import zh.qiushui.mod.qca.rule.util.EntityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(CrafterBlock.class)
public abstract class MixinCrafterBlock {
    @Shadow
    @Final
    private static EnumProperty<Orientation> ORIENTATION;
    @Unique
    private Section limitation = null;

    @Inject(
        method = "scheduledTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/CrafterBlock;"
                     + "craft(Lnet/minecraft/block/BlockState;"
                     + "Lnet/minecraft/server/world/ServerWorld;"
                     + "Lnet/minecraft/util/math/BlockPos;)V")
    )
    @SuppressWarnings("LoggingSimilarMessage")
    private void qca$updateLimitation(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (!QcaSettings.canLimit(QcaSettings.crafterLimitation)) return;
        List<Section> sections = new ArrayList<>();
        if (QcaSettings.canLimitByItemFrame(QcaSettings.crafterLimitation)) {
            List<ItemFrameEntity> itemFrames = EntityUtil.getEntitiesIf(
                world, pos.offset(state.get(ORIENTATION).getRotation()),
                itemFrame -> !itemFrame.getFacing().equals(state.get(ORIENTATION).getRotation()) || itemFrame.getHeldItemStack().isEmpty(),
                EntityType.ITEM_FRAME, EntityType.GLOW_ITEM_FRAME
            );
            if (!itemFrames.isEmpty()) {
                sections.add(new AnySection(Lists.transform(itemFrames, itemFrame -> new ItemSection(itemFrame.getHeldItemStack()))));
            }
        }
        if (QcaSettings.canLimitByCustomName(QcaSettings.crafterLimitation)) {
            if (world.getBlockEntity(pos) instanceof CrafterBlockEntity entity) {
                Optional.ofNullable(entity.getCustomName())
                    .map(Text::getString)
                    .flatMap(ItemPredicateParser::parseItemPredicate)
                    .ifPresent(sections::add);
            }
        }

        if (sections.isEmpty()) {
            this.limitation = null;
            if (QcaSettings.qcaDebugLog) {
                QcaExtension.LOGGER.debug("A crafter located at {} reset its restrictor.", pos);
            }
            return;
        }
        if (sections.size() > 1) {
            this.limitation = new AllSection(sections);
        }
        this.limitation = sections.getFirst();
        if (QcaSettings.qcaDebugLog) {
            QcaExtension.LOGGER.debug("A crafter located at {} updated its limit source {}.", pos, this.limitation);
        }
    }

    @ModifyExpressionValue(
        method = "craft",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", ordinal = 0))
    private boolean qca$limitResult(boolean isEmpty, @Local(ordinal = 0) ItemStack stack) {
        if (!QcaSettings.canLimit(QcaSettings.crafterLimitation)) return isEmpty;
        boolean matched = this.limitation.test(stack);
        if (QcaSettings.qcaDebugLog) {
            QcaExtension.LOGGER.debug(
                "A crafter just limited. Input: {}, Result: {}",
                stack, isEmpty ? "Failed. The instance is empty." : matched ? "Successfully limited." : "Failed."
            );
        }
        return isEmpty || !matched;
    }
}
