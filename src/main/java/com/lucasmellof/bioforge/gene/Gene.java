package com.lucasmellof.bioforge.gene;

import com.lucasmellof.bioforge.entity.IEntityWithGene;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 16/10/2025
 */
@Getter
public abstract class Gene {

    private final ResourceLocation id;
    @Accessors(makeFinal = true)
    private final List<Holder<GeneType<?>>> incompatibleGenes;
    @Accessors(makeFinal = true)
    private final List<EntityType<?>> incompatibleEntities;
    @Accessors(makeFinal = true)
    private final List<EntityType<?>> applicableEntities;

    public Gene(
            ResourceLocation id,
            List<Holder<GeneType<?>>> incompatibleGenes,
            List<EntityType<?>> incompatibleEntities,
            List<EntityType<?>> applicableEntities) {
        this.id = id;
        this.incompatibleGenes = incompatibleGenes;
        this.incompatibleEntities = incompatibleEntities;
        this.applicableEntities = applicableEntities;
    }

    public Gene(ResourceLocation id) {
        this(id, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public void tick() {}

    public boolean canApplyGene(IEntityWithGene entity, Gene gene) {
        for (Holder<GeneType<?>> incompatibleGene : gene.getIncompatibleGenes()) {
            if (entity.bioforge$hasGene(incompatibleGene.value())) {
                return false;
            }
        }

        for (EntityType<?> incompatibleEntity : gene.getIncompatibleEntities()) {
            if (incompatibleEntity.equals(entity.self().getType())) {
                return false;
            }
        }

        for (EntityType<?> applicableEntity : gene.getApplicableEntities()) {
            if (applicableEntity.equals(entity.self().getType())) {
                return true;
            }
        }

        return true;
    }

    public boolean removeGene(IEntityWithGene genes) {
        return true;
    }

    public void addGene(IEntityWithGene genes) {}

    @SafeVarargs
    public final void setIncompatibleGenes(Holder<GeneType<?>>... genes) {
        if (this.incompatibleGenes == null) {
            throw new IllegalStateException("Incompatible genes list is null");
        }
        this.incompatibleGenes.clear();
        Collections.addAll(this.incompatibleGenes, genes);
    }

    public abstract GeneType<? extends Gene> getType();
}
