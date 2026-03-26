package com.qiushui1012.mod.qca.mixin.rule;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.qiushui1012.mod.qca.util.rule.EntityCollisionContextExtension;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

@Mixin(EntityCollisionContext.class)
public abstract class MixinEntityCollisionContext_itemsCanPassThroughChains implements EntityCollisionContextExtension {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Unique
    private Optional<Entity> qca$entity = Optional.empty();

    @SuppressWarnings("OptionalOfNullableMisuse")
    @WrapMethod(method = "<init>(Lnet/minecraft/world/entity/Entity;)V")
    private void storeEntity(Entity entity, Operation<Void> original) {
        original.call(entity);
        this.qca$entity = Optional.ofNullable(entity);
    }

    @Override
    public Optional<Entity> qca$getEntity() {
        return this.qca$entity;
    }
}
