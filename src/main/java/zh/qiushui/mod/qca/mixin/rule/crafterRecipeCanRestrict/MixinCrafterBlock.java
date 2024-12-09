package zh.qiushui.mod.qca.mixin.rule.crafterRecipeCanRestrict;

import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.block.CrafterBlock;
import net.minecraft.block.enums.Orientation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zh.qiushui.mod.qca.QcaSettings;
import zh.qiushui.mod.qca.rule.util.EntityUtil;
import zh.qiushui.mod.qca.rule.util.restriction.Restriction;

import java.util.List;
import java.util.Set;

@Mixin(CrafterBlock.class)
public abstract class MixinCrafterBlock {
    @Shadow @Final private static EnumProperty<Orientation> ORIENTATION;
    @Unique private final Restriction restriction = Restriction.empty();

    @Inject(
            method = "scheduledTick", at = @At(
                    value = "INVOKE", target = "Lnet/minecraft/block/CrafterBlock;" +
                                               "craft(Lnet/minecraft/block/BlockState;" +
                                               "Lnet/minecraft/server/world/ServerWorld;" +
                                               "Lnet/minecraft/util/math/BlockPos;)" +
                                               "V"
            )
    )
    private void qca_updateRestriction(
            BlockState blockState, ServerWorld world, BlockPos blockPos, Random random, CallbackInfo ci
    ) {
        List<ItemFrameEntity> itemFrames = EntityUtil.getEntitiesIf(
                world, blockPos.offset(blockState.get(ORIENTATION).getRotation()),
                itemFrame -> !itemFrame.getFacing().equals(blockState.get(ORIENTATION).getRotation()),
                EntityType.ITEM_FRAME, EntityType.GLOW_ITEM_FRAME
        );

        if (!itemFrames.isEmpty()) {
            Set<Item> items = Sets.newHashSet();
            itemFrames.forEach(itemFrame -> {
                Item restrictor = itemFrame.getHeldItemStack().getItem();

                if (!restrictor.equals(Items.AIR)) {
                    items.add(restrictor);
                }
            });
            this.restriction.setRestrictor(items);
        } else {
            this.restriction.resetRestrictor();
        }
    }

    @Redirect(method = "craft", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"))
    private boolean qca_restrictResult(ItemStack instance) {
        return instance.isEmpty()
               || (QcaSettings.crafterRecipeCanRestrict && !this.restriction.restrict(instance));
    }
}
