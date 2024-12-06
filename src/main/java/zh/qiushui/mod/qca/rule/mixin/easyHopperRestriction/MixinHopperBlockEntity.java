package zh.qiushui.mod.qca.rule.mixin.easyHopperRestriction;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zh.qiushui.mod.qca.QcaSettings;
import zh.qiushui.mod.qca.rule.util.restriction.Restrictable;
import zh.qiushui.mod.qca.rule.util.restriction.Restriction;

import java.util.Objects;

@Mixin(value = HopperBlockEntity.class, priority = 900)
public abstract class MixinHopperBlockEntity extends LockableContainerBlockEntity implements Restrictable {
    @Unique
    private Restriction restriction = Restriction.empty();

    protected MixinHopperBlockEntity(
            BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState
    ) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    public void qca_setRestrictor(String idRaw) {
        this.restriction.setRestrictor(idRaw);
    }
    @Override
    public boolean qca_restrict(ItemStack stack) {
        return this.restriction.restrict(stack);
    }

    @Inject(method = "serverTick", at = @At("TAIL"))
    private static void qca_updateRestrictionOnTick(
            World world, BlockPos blockPos, BlockState blockState, HopperBlockEntity hopper, CallbackInfo ci
    ) {
        try {
            hopper.qca_setRestrictor(Objects.requireNonNull(hopper.getCustomName()).getString());
        } catch (NullPointerException ignored) {}
    }

    @Redirect(
            method = "canExtract", at = @At(
                    value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;" +
                                               "canTransferTo(Lnet/minecraft/inventory/Inventory;" +
                                               "ILnet/minecraft/item/ItemStack;)" +
                                               "Z"
            )
    )
    private static boolean qca_restrictExtract(Inventory instance, Inventory inventory, int i, ItemStack itemStack) {
        return instance.canTransferTo(inventory, i, itemStack)
               && qca_restrict(inventory, itemStack);
    }
    @Redirect(
            method = "canInsert", at = @At(
                    value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;" +
                                               "isValid(ILnet/minecraft/item/ItemStack;)" +
                                               "Z"
            )
    )
    private static boolean qca_restrictInsert(Inventory instance, int i, ItemStack itemStack) {
        return instance.isValid(i, itemStack)
               && qca_restrict(instance, itemStack);
    }

    @Unique
    private static boolean qca_restrict(Inventory inventory, ItemStack stack) {
        return !QcaSettings.easierHopperRestriction
               || !(inventory instanceof HopperBlockEntity hopper)
               || hopper.qca_restrict(stack);
    }
}
