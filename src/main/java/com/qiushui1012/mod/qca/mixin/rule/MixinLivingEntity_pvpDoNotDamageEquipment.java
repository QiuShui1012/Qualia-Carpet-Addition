package com.qiushui1012.mod.qca.mixin.rule;

import com.qiushui1012.mod.qca.QcaServerRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity_pvpDoNotDamageEquipment {
    @Inject(
        method = "doHurtEquipment",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkForPVP(DamageSource source, float damageAmount, EquipmentSlot[] slots, CallbackInfo ci) {
        if (QcaServerRules.pvpDoNotDamageEquipment && source.getDirectEntity() instanceof Player && source.getEntity() instanceof Player) {
            ci.cancel();
        }
    }
}
