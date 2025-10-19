package com.lucasmellof.bioforge.gene.types;

import com.lucasmellof.bioforge.gene.Gene;
import com.lucasmellof.bioforge.gene.GeneType;
import com.lucasmellof.bioforge.registry.ModGenes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 16/10/2025
 */
public class BlankGene extends Gene {
    public static final MapCodec<BlankGene> CODEC = RecordCodecBuilder.mapCodec(
            it -> it.group(ResourceLocation.CODEC.fieldOf("id").forGetter(Gene::getId))
                    .apply(it, BlankGene::new));

    public BlankGene(ResourceLocation id) {
        super(id);
    }

    @Override
    public GeneType<? extends Gene> getType() {
        return ModGenes.BLANK_GENE.value();
    }
}
