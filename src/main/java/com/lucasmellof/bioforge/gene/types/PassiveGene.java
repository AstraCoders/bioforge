package com.lucasmellof.bioforge.gene.types;

import com.lucasmellof.bioforge.gene.Gene;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 16/10/2025
 */
public class PassiveGene extends Gene {
	public PassiveGene(ResourceLocation id) {
		super(id);
		setIncompatibleGenes(AggressiveGene.class);
	}

}
