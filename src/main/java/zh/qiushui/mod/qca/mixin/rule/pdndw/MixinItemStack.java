package zh.qiushui.mod.qca.mixin.rule.pdndw;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import zh.qiushui.mod.qca.QcaSettings;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {
    @WrapOperation(
        method = "postHurtEnemy",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item;"
                     + "postHurtEnemy(Lnet/minecraft/world/item/ItemStack;"
                     + "Lnet/minecraft/world/entity/LivingEntity;"
                     + "Lnet/minecraft/world/entity/LivingEntity;)V"
        ))
    private void checkForPlayer(
        Item instance, ItemStack stack, LivingEntity target, LivingEntity attacker, Operation<Void> original
    ) {
        ItemStack cache = stack.copy();
        original.call(instance, stack, target, attacker);
        if (QcaSettings.pvpDoNotDamageWeapon
            && target instanceof Player && attacker instanceof Player
            // Objects.equals is not necessary, but if you use !=, idea will not be happy
            && !stack.getOrDefault(DataComponents.DAMAGE, 0).equals(cache.getOrDefault(DataComponents.DAMAGE, 0))
        ) {
            stack.set(DataComponents.DAMAGE, cache.getOrDefault(DataComponents.DAMAGE, 0));
        }
    }
}
