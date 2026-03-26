package com.qiushui1012.mod.qca.mixin.rule;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.qiushui1012.mod.qca.QcaServerRules;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class MixinItemStack_pvpDoNotDamageWeapon {
    @WrapOperation(
        method = "postHurtEnemy",
        at = {
            @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/Item;"
                         + "postHurtEnemy(Lnet/minecraft/world/item/ItemStack;"
                         + "Lnet/minecraft/world/entity/LivingEntity;"
                         + "Lnet/minecraft/world/entity/LivingEntity;)V"
            )
        }
    )
    private void checkForPlayer(
        Item instance,
        ItemStack stack,
        LivingEntity target,
        LivingEntity attacker,
        Operation<Void> original
    ) {
        ItemStack cache = stack.copy();
        original.call(instance, stack, target, attacker);
        if (
            QcaServerRules.pvpDoNotDamageWeapon
            && target instanceof Player && attacker instanceof Player
            && stack.getDamageValue() != cache.getDamageValue()
        ) {
            stack.setDamageValue(cache.getDamageValue());
        }
    }

    //#if MC >= 12105
    @WrapOperation(
        method = "postHurtEnemy",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;"
                     + "hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;"
                     + "Lnet/minecraft/world/entity/EquipmentSlot;)V"
        )
    )
    private void checkForPlayer1(
        ItemStack stack,
        int i,
        LivingEntity attacker,
        EquipmentSlot equipmentSlot,
        Operation<Void> original,
        @Local(ordinal = 0, argsOnly = true) LivingEntity target
    ) {
        ItemStack cache = stack.copy();
        original.call(stack, i, attacker, equipmentSlot);
        if (
            QcaServerRules.pvpDoNotDamageWeapon
            && target instanceof Player && attacker instanceof Player
            && stack.getDamageValue() != cache.getDamageValue()
        ) {
            stack.setDamageValue(cache.getDamageValue());
        }
    }
    //#endif
}
