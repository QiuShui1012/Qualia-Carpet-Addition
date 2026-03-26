package com.qiushui1012.mod.qca.mixin.rule;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.qiushui1012.mod.qca.QcaSettings;

@Mixin(ItemStack.class)
public abstract class MixinItemStack_pvpDoNotDamageWeapon {
    //#if MC >= 12101
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
            QcaSettings.pvpDoNotDamageWeapon
            && target instanceof Player && attacker instanceof Player
            && stack.getDamageValue() != cache.getDamageValue()
        ) {
            stack.setDamageValue(cache.getDamageValue());
        }
    }
    //#else
    //$$ @WrapOperation(
    //$$     method = "hurtEnemy",
    //$$     at = @At(
    //$$         value = "INVOKE",
    //$$         target = "Lnet/minecraft/world/item/Item;"
    //$$                  + "hurtEnemy(Lnet/minecraft/world/item/ItemStack;"
    //$$                  + "Lnet/minecraft/world/entity/LivingEntity;"
    //$$                  + "Lnet/minecraft/world/entity/LivingEntity;)Z"
    //$$     ))
    //$$ private boolean checkForPlayer(
    //$$     Item instance,
    //$$     ItemStack stack,
    //$$     LivingEntity target,
    //$$     LivingEntity attacker,
    //$$     Operation<Boolean> original
    //$$ ) {
    //$$     ItemStack cache = stack.copy();
    //$$     boolean result = original.call(instance, stack, target, attacker);
    //$$     if (
    //$$         QcaSettings.pvpDoNotDamageWeapon
    //$$         && target instanceof Player && attacker instanceof Player
    //$$         && stack.getDamageValue() != cache.getDamageValue()
    //$$     ) {
    //$$         stack.setDamageValue(cache.getDamageValue());
    //$$     }
    //$$     return result;
    //$$ }
    //#endif

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
            QcaSettings.pvpDoNotDamageWeapon
            && target instanceof Player && attacker instanceof Player
            && stack.getDamageValue() != cache.getDamageValue()
        ) {
            stack.setDamageValue(cache.getDamageValue());
        }
    }
    //#endif
}
