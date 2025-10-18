package com.lucasmellof.bioforge.gene;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.List;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 16/10/2025
 */
public abstract class ProductiveGene extends Gene {

    public ProductiveGene(
            ResourceLocation id,
            List<Class<? extends Gene>> incompatibleGenes,
            List<EntityType<?>> incompatibleEntities,
            List<EntityType<?>> applicableEntities) {
        super(id, incompatibleGenes, incompatibleEntities, applicableEntities);
    }

    public ProductiveGene(ResourceLocation id) {
        super(id);
    }

    void produce() {
        // give milk
    }
}
