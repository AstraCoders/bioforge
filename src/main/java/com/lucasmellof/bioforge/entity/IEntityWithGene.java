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

	Set<GeneType<?>> bioforge$getGenes();

	default boolean bioforge$addGene(Gene gene) {
		if (this.bioforge$hasGene(gene.getType())) {
			return false;
		}
		if (!gene.canApplyGene(this, gene)) {
			return false;
		}
		gene.addGene(this);
		return bioforge$getGenes().add(gene.getType());
	}

	default void bioforge$removeGene(GeneType<?> gene) {
		if (gene == null) return;
		bioforge$getGenes().remove(gene);
		gene.create().removeGene(this);
	}
	default boolean bioforge$hasGene(GeneType<?> geneClass) {
		for (GeneType<?> gene : bioforge$getGenes()) {
			if (gene.equals(geneClass)) {
				return true;
			}
		}
		return false;
	}
	default boolean bioforge$hasGene(Class<? extends GeneType<?>> geneClass) {
		for (GeneType<?> gene : bioforge$getGenes()) {
			if (gene.getClass().equals(geneClass)) {
				return true;
			}
		}
		return false;
	}

	default void bioforge$removeGene(Class<? extends GeneType<?>> geneClass) {
		GeneType<?> toRemove = null;
		for (GeneType<?> gene : bioforge$getGenes()) {
			if (gene.getClass().equals(geneClass)) {
				toRemove = gene;
				break;
			}
		}
		if (toRemove != null) {
			this.bioforge$removeGene(toRemove);
		}
	}

	default void bioforge$clearGenes() {
		bioforge$getGenes().clear();
	}
}
