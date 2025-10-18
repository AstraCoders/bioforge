package com.lucasmellof.bioforge.gene.types;

import com.lucasmellof.bioforge.entity.IEntityWithGene;
import com.lucasmellof.bioforge.gene.Gene;
import com.lucasmellof.bioforge.registry.ModGenes;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 16/10/2025
 */
public class AggressiveGene extends Gene {

	public AggressiveGene(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean removeGene(IEntityWithGene entity) {
		if (entity.bioforge$hasGene(AggressiveGene.class)) {
			entity.bioforge$addGene(ModGenes.PASSIVE_GENE.get());
		}

		return true;
	}

	@Override
	public void addGene(IEntityWithGene genes) {
		if (genes.bioforge$hasGene(PassiveGene.class)) {
			genes.bioforge$removeGene(PassiveGene.class);
		}
	}

}
