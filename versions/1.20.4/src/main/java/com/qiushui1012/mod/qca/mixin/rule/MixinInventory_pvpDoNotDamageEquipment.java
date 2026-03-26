package com.qiushui1012.mod.qca.mixin.rule;

import com.qiushui1012.mod.qca.QcaSettings;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public abstract class MixinInventory_pvpDoNotDamageEquipment {
    @Inject(method = "hurtArmor", at = @At("HEAD"), cancellable = true)
    private void checkForPVP(
        DamageSource source,
        float damageAmount,
        //#if MC >= 11700
        int[] slots,
        //#endif
        CallbackInfo ci
    ) {
        if (QcaSettings.pvpDoNotDamageEquipment && source.getDirectEntity() instanceof Player && source.getEntity() instanceof Player) {
            ci.cancel();
        }
    }
}
