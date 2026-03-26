package com.qiushui1012.mod.qca.mixin.rule;

import com.qiushui1012.mod.qca.util.rule.EntityCollisionContextExtension;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(EntityCollisionContext.class)
public abstract class MixinEntityCollisionContext_itemsCanPassThroughChains implements EntityCollisionContextExtension {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Unique
    private Optional<Entity> qca$entity = Optional.empty();

    @SuppressWarnings("OptionalOfNullableMisuse")
    @Inject(method = "<init>(Lnet/minecraft/world/entity/Entity;)V", at = @At("RETURN"))
    private void storeEntity(Entity entity, CallbackInfo ci) {
        this.qca$entity = Optional.ofNullable(entity);
    }

    @Override
    public Optional<Entity> qca$getEntity() {
        return this.qca$entity;
    }
}
