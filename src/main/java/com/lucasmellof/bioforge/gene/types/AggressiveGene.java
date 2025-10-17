package com.lucasmellof.bioforge.gene.types;

import com.lucasmellof.bioforge.entity.IEntityWithGene;
import com.lucasmellof.bioforge.gene.IGene;

import java.util.List;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 16/10/2025
 */
public class AggressiveGene implements IGene {
	@Override
	public void tick() {

	}

	@Override
	public List<Class<? extends IGene>> getIncompatibleGenes() {
		return List.of(PassiveGene.class);
	}

	@Override
	public boolean removeGene(IEntityWithGene entity) {
		if (entity.bioforge$getGenes().contains(AggressiveGene.class)) {
			entity.bioforge$addGene(new PassiveGene());
		}

		return true;
	}

	@Override
	public void addGene(IEntityWithGene genes) {
		if (genes.bioforge$getGenes().contains(PassiveGene.class)) {
			genes.bioforge$removeGene(PassiveGene.class);
		}
	}
}
