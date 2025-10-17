package com.lucasmellof.bioforge.gene.types;

import com.lucasmellof.bioforge.gene.IGene;

import java.util.List;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 16/10/2025
 */
public class PassiveGene implements IGene {
	@Override
	public void tick() {

	}

	@Override
	public List<Class<? extends IGene>> getIncompatibleGenes() {
		return List.of(AggressiveGene.class);
	}
}
