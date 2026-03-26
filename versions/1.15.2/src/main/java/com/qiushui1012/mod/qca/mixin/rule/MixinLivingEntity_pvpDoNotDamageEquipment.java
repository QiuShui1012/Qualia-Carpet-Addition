package com.qiushui1012.mod.qca.mixin.rule;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.qiushui1012.mod.qca.QcaSettings;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity_pvpDoNotDamageEquipment {
    @WrapOperation(
        method = "getDamageAfterArmorAbsorb",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurtArmor(F)V")
    )
    private void checkForPVP(LivingEntity instance, float f, Operation<Void> original, @Local(argsOnly = true) DamageSource source) {
        if (
            QcaSettings.pvpDoNotDamageEquipment
            && source.getDirectEntity() instanceof Player
            && source.getEntity() instanceof Player
        ) {
            return;
        }
        original.call(instance, f);
    }
}
