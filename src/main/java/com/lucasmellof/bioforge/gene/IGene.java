package com.lucasmellof.bioforge.gene;

import com.lucasmellof.bioforge.entity.IEntityWithGene;

import java.util.List;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 16/10/2025
 */
public interface IGene {

    void tick();

    List<Class<? extends IGene>> getIncompatibleGenes();

    default boolean canApplyGene(IEntityWithGene entity, IGene gene) {
    	for (Class<? extends IGene> incompatibleGene : gene.getIncompatibleGenes()) {
			if (entity.bioforge$hasGene(incompatibleGene)) {
				return false;
			}
		}
		return true;
	}

	default boolean removeGene(IEntityWithGene genes) {
		return true;
	}

	default void addGene(IEntityWithGene genes) {}
}
