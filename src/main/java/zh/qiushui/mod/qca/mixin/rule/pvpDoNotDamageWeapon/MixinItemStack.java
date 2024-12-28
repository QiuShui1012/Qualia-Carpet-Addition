package zh.qiushui.mod.qca.mixin.rule.pvpDoNotDamageWeapon;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import zh.qiushui.mod.qca.QcaSettings;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {
    @Redirect(
            method = "postDamageEntity", at = @At(
                    value = "INVOKE", target = "Lnet/minecraft/item/Item;" +
                                               "postDamageEntity(Lnet/minecraft/item/ItemStack;" +
                                               "Lnet/minecraft/entity/LivingEntity;" +
                                               "Lnet/minecraft/entity/LivingEntity;)" +
                                               "V"
            )
    )
    private void checkForPlayer(
            Item instance, ItemStack itemStack, LivingEntity livingEntity, LivingEntity livingEntity2
    ) {
        if (
                !QcaSettings.pvpDoNotDamageWeapon ||
                !(livingEntity instanceof PlayerEntity && livingEntity2 instanceof PlayerEntity)
        ) {
            instance.postDamageEntity(itemStack, livingEntity, livingEntity2);
        }
    }
}
