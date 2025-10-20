package com.lucasmellof.bioforge.entity;

import com.lucasmellof.bioforge.gene.Gene;
import com.lucasmellof.bioforge.gene.GeneType;
import net.minecraft.world.entity.LivingEntity;

import java.util.Set;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 16/10/2025
 */
public interface IEntityWithGene {
	LivingEntity self();

	Set<Gene> bioforge$getGenes();

	default boolean bioforge$addGene(Gene gene) {
		if (this.bioforge$hasGene(gene.getType())) {
			return false;
		}
		if (!gene.canApplyGene(this, gene)) {
			return false;
		}
		gene.addGene(this);
		return bioforge$getGenes().add(gene);
	}

	default void bioforge$removeGene(Gene gene) {
		if (gene == null) return;
		bioforge$getGenes().remove(gene);
		gene.removeGene(this);
	}

	default void bioforge$removeGene(GeneType<?> type) {
		if (type == null) return;
		Gene toRemove = null;
		for (Gene gene : bioforge$getGenes()) {
			if (gene.getType().equals(type)) {
				toRemove = gene;
				break;
			}
		}
		if (toRemove != null) {
			this.bioforge$removeGene(toRemove);
		}
	}

	default boolean bioforge$hasGene(GeneType<?> geneClass) {
		for (Gene gene : bioforge$getGenes()) {
			if (gene.getType().equals(geneClass)) {
				return true;
			}
		}
		return false;
	}
	default boolean bioforge$hasGene(Class<? extends GeneType<?>> geneClass) {
		for (Gene gene : bioforge$getGenes()) {
			if (gene.getType().getClass().equals(geneClass)) {
				return true;
			}
		}
		return false;
	}

	default void bioforge$removeGene(Class<? extends GeneType<?>> geneClass) {
		Gene toRemove = null;
		for (Gene gene : bioforge$getGenes()) {
			if (gene.getType().getClass().equals(geneClass)) {
				toRemove = gene;
				break;
			}
		}
		if (toRemove != null) {
			this.bioforge$removeGene(toRemove);
		}
	}

	default Gene bioforge$getGene(Class<? extends GeneType<?>> geneClass) {
		for (Gene gene : bioforge$getGenes()) {
			if (gene.getType().getClass().equals(geneClass)) {
				return gene;
			}
		}
		return null;
	}
	default Gene bioforge$getGene(GeneType<?> geneClass) {
		for (Gene gene : bioforge$getGenes()) {
			if (gene.getType().equals(geneClass)) {
				return gene;
			}
		}
		return null;
	}

	default void bioforge$clearGenes() {
		bioforge$getGenes().clear();
	}
}
