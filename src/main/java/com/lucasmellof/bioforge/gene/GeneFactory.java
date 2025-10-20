package com.lucasmellof.bioforge.gene;

import net.minecraft.resources.ResourceLocation;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 18/10/2025
 */
@FunctionalInterface
public interface GeneFactory<T extends Gene> {
	T create(ResourceLocation id, GeneType<?> info);
}
