package com.qiushui1012.mod.qca.util.rule;

import net.minecraft.world.entity.Entity;

import java.util.Optional;

public interface EntityCollisionContextExtension {
    Optional<Entity> qca$getEntity();
}
