package com.lucasmellof.bioforge.gene;

import com.lucasmellof.bioforge.entity.IEntityWithGene;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 20/10/2025
 */
public interface GeneAction {
	boolean canApply(IEntityWithGene entity);

	boolean apply(IEntityWithGene entity);
	void remove(IEntityWithGene entity);

}
