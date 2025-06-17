package zh.qiushui.mod.qca.mixin.rule.pvpDoNotDamageWeapon;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import zh.qiushui.mod.qca.QcaSettings;

import java.util.Objects;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {
    @WrapOperation(
        method = "postDamageEntity",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/Item;"
                     + "postDamageEntity(Lnet/minecraft/item/ItemStack;"
                     + "Lnet/minecraft/entity/LivingEntity;"
                     + "Lnet/minecraft/entity/LivingEntity;)V"
        ))
    private void checkForPlayer(
        Item instance, ItemStack stack, LivingEntity victim, LivingEntity attacker,
        Operation<Void> original
    ) {
        ItemStack cache = stack.copy();
        original.call(instance, stack, victim, attacker);
        if (QcaSettings.pvpDoNotDamageWeapon
            && victim instanceof PlayerEntity && attacker instanceof PlayerEntity
            // Objects.equals is not necessary, but if you use !=, idea will not be happy
            && !Objects.equals(stack.getOrDefault(DataComponentTypes.DAMAGE, 0), cache.getOrDefault(DataComponentTypes.DAMAGE, 0))
        ) {
            stack.set(DataComponentTypes.DAMAGE, cache.getOrDefault(DataComponentTypes.DAMAGE, 0));
        }
    }
}