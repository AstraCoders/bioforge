package com.lucasmellof.bioforge.entity;

import com.lucasmellof.bioforge.gene.IGene;

import java.util.List;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 16/10/2025
 */
public interface IEntityWithGene {
	List<? extends IGene> bioforge$getGenes();

	boolean bioforge$addGene(IGene gene);

	void bioforge$removeGene(IGene gene);

	void bioforge$removeGene(Class<? extends IGene> geneClass);

	boolean bioforge$hasGene(Class<? extends IGene> geneClass);
}
