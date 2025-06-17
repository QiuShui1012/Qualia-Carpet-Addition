package zh.qiushui.mod.qca.mixin.rule.easyHopperLimitation;

import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zh.qiushui.mod.qca.QcaExtension;
import zh.qiushui.mod.qca.QcaSettings;
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
public abstract class MixinHopperBlockEntity extends LockableContainerBlockEntity implements HopperCache {
    @Unique
    private Text customNameCache = null;
    @Unique
    private Section section = null;

    protected MixinHopperBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    public Text qca$getCache() {
        return this.customNameCache;
    }

    @Override
    public void qca$setCache(Text cache) {
        this.customNameCache = cache;
    }

    @Override
    public Section qca$getSection() {
        return this.section;
    }

    @Override
    public void qca$setSection(Section section) {
        this.section = section;
    }

    @Inject(method = "serverTick", at = @At("TAIL"))
    private static void qca$updateRestrictionOnTick(
        World world, BlockPos pos, BlockState state, HopperBlockEntity hopper, CallbackInfo ci
    ) {
        if (!QcaSettings.canLimit(QcaSettings.easyHopperLimitation)) return;
        List<Section> sections = new ArrayList<>();
        if (QcaSettings.canLimitByItemFrame(QcaSettings.easyHopperLimitation)) {
            Direction facing = UnsafeUtil.<HopperAccessor>cast(hopper).qca$getFacing();
            List<ItemFrameEntity> itemFrames = EntityUtil.getEntitiesIf(
                world, pos.offset(facing),
                itemFrame -> !itemFrame.getFacing().equals(facing) || itemFrame.getHeldItemStack().isEmpty(),
                EntityType.ITEM_FRAME, EntityType.GLOW_ITEM_FRAME
            );
            if (!itemFrames.isEmpty()) {
                sections.add(new AnySection(Lists.transform(itemFrames, itemFrame -> new ItemSection(itemFrame.getHeldItemStack()))));
                if (QcaSettings.qcaDebugLog) {
                    QcaExtension.LOGGER.debug("(Tick) Tried to set the restrictor from the item frame.");
                }
            }
        }
        if (QcaSettings.canLimitByCustomName(QcaSettings.easyHopperLimitation)) {
            Optional.ofNullable(hopper.getCustomName())
                .ifPresent(name -> {
                    if (!Objects.equals(hopper.qca$getCache(), name)) {
                        hopper.qca$setCache(name);
                        sections.add(ItemPredicateParser.parseItemPredicate(name.getString()).orElse(null));
                        if (QcaSettings.qcaDebugLog) {
                            QcaExtension.LOGGER.debug("(Tick) Tried to set the restrictor from the custom name.");
                        }
                    }
                });
        }
        if (sections.size() > 1) {
            hopper.qca$setSection(new AllSection(sections));
        } else if (sections.size() == 1) {
            hopper.qca$setSection(sections.getFirst());
        }
    }

    @WrapOperation(
        method = "canExtract",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/inventory/Inventory;"
                     + "canTransferTo(Lnet/minecraft/inventory/Inventory;"
                     + "ILnet/minecraft/item/ItemStack;)Z"
        ))
    private static boolean qca$restrictExtract(
        Inventory instance, Inventory inventory, int i, ItemStack stack, Operation<Boolean> original
    ) {
        return original.call(instance, inventory, i, stack) && qca$restrict(inventory, stack);
    }

    @WrapOperation(
        method = "canInsert",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/inventory/Inventory;"
                     + "isValid(ILnet/minecraft/item/ItemStack;)Z"
        ))
    private static boolean qca$restrictInsert(Inventory instance, int i, ItemStack stack, Operation<Boolean> original) {
        return original.call(instance, i, stack) && qca$restrict(instance, stack);
    }

    @Unique
    private static boolean qca$restrict(Inventory inventory, ItemStack stack) {
        return !QcaSettings.canLimit(QcaSettings.easyHopperLimitation)
               || !(inventory instanceof HopperBlockEntity hopper)
               || hopper.qca$getSection().test(stack);
    }
}
