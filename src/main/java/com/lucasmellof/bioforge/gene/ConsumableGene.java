package com.lucasmellof.bioforge.gene;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.List;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 16/10/2025
 */
public abstract class ConsumableGene extends Gene {

    public ConsumableGene(
            ResourceLocation id,
            List<Holder<GeneType<?>>> incompatibleGenes,
            List<EntityType<?>> incompatibleEntities,
            List<EntityType<?>> applicableEntities) {
        super(id, incompatibleGenes, incompatibleEntities, applicableEntities);
    }

    public ConsumableGene(ResourceLocation id) {
        super(id);
    }

    void consume() {
        // eat grass
    }
}
