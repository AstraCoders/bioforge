package com.lucasmellof.bioforge.gene.types;

import com.lucasmellof.bioforge.entity.IEntityWithGene;
import com.lucasmellof.bioforge.gene.Gene;
import com.lucasmellof.bioforge.gene.GeneType;
import com.lucasmellof.bioforge.registry.ModGenes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 16/10/2025
 */
public class AggressiveGene extends Gene {

	public static final MapCodec<AggressiveGene> CODEC = RecordCodecBuilder.mapCodec(
			it -> it.group(ResourceLocation.CODEC.fieldOf("id").forGetter(Gene::getId))
						  .apply(it, AggressiveGene::new));

	public AggressiveGene(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean removeGene(IEntityWithGene entity) {
		if (entity.bioforge$hasGene(ModGenes.AGGRESSIVE_GENE.get())) {
			entity.bioforge$addGene(ModGenes.PASSIVE_GENE.get().create());
		}

		return true;
	}

	@Override
	public void addGene(IEntityWithGene genes) {
		if (genes.bioforge$hasGene(ModGenes.PASSIVE_GENE.get())) {
			genes.bioforge$removeGene(ModGenes.PASSIVE_GENE.get());
		}
	}

	@Override
	public GeneType<? extends Gene> getType() {
		return ModGenes.AGGRESSIVE_GENE.value();
	}
}
