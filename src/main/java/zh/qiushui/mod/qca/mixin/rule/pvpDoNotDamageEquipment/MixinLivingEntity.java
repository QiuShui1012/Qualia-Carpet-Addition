package zh.qiushui.mod.qca.mixin.rule.pvpDoNotDamageEquipment;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import zh.qiushui.mod.qca.QcaSettings;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
    @Redirect(
        method = "damageEquipment",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;"
                     + "damage(ILnet/minecraft/entity/LivingEntity;"
                     + "Lnet/minecraft/entity/EquipmentSlot;)V"
        ))
    private void checkForPlayerInEquipment(
        ItemStack instance, int i, LivingEntity thiS, EquipmentSlot equipmentSlot, DamageSource source
    ) {
        if (
            !QcaSettings.pvpDoNotDamageEquipment || !(thiS instanceof PlayerEntity && source.getAttacker() instanceof PlayerEntity)
        ) {
            instance.damage(i, thiS, equipmentSlot);
        }
    }
}
