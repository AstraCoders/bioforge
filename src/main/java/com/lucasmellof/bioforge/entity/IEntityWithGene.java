package com.lucasmellof.bioforge.entity;

import com.lucasmellof.bioforge.gene.Gene;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 16/10/2025
 */
public interface IEntityWithGene {
	LivingEntity self();

	List<Gene> bioforge$getGenes();

	default boolean bioforge$addGene(Gene gene) {
		if (this.bioforge$hasGene(gene.getClass())) {
			return false;
		}
		if (!gene.canApplyGene(this, gene)) {
			return false;
		}
		return bioforge$getGenes().add(gene);
	}

	default void bioforge$removeGene(Gene gene) {
		if (!gene.removeGene(this)) return;
		bioforge$getGenes().remove(gene);
	}

	default boolean bioforge$hasGene(Class<? extends Gene> geneClass) {
		for (Gene gene : bioforge$getGenes()) {
			if (gene.getClass().equals(geneClass)) {
				return true;
			}
		}
		return false;
	}

	default void bioforge$removeGene(Class<? extends Gene> geneClass) {
		Gene toRemove = null;
		for (Gene gene : bioforge$getGenes()) {
			if (gene.getClass().equals(geneClass)) {
				toRemove = gene;
				break;
			}
		}
		if (toRemove != null) {
			this.bioforge$removeGene(toRemove);
		}
	}
}
