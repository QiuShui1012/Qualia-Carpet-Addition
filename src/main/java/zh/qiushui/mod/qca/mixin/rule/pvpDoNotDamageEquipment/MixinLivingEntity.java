package zh.qiushui.mod.qca.mixin.rule.pvpDoNotDamageEquipment;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import zh.qiushui.mod.qca.QcaSettings;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
    @WrapOperation(
        method = "damageEquipment",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;"
                     + "damage(ILnet/minecraft/entity/LivingEntity;"
                     + "Lnet/minecraft/entity/EquipmentSlot;)V"
        ))
    private void checkForPlayerInEquipment(
        ItemStack instance, int i, LivingEntity entity, EquipmentSlot equipmentSlot, Operation<Void> original,
        @Local(argsOnly = true) DamageSource source
    ) {
        if (!QcaSettings.pvpDoNotDamageEquipment
            || !(entity instanceof PlayerEntity && source.getAttacker() instanceof PlayerEntity)
        ) {
            instance.damage(i, entity, equipmentSlot);
        }
    }
}