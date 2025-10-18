package com.lucasmellof.bioforge.gene.types;

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
public class PassiveGene extends Gene {
    public static final MapCodec<PassiveGene> CODEC = RecordCodecBuilder.mapCodec(
            it -> it.group(ResourceLocation.CODEC.fieldOf("id").forGetter(Gene::getId))
                    .apply(it, PassiveGene::new));

    public PassiveGene(ResourceLocation id) {
        super(id);
        setIncompatibleGenes(ModGenes.PASSIVE_GENE);
    }

    @Override
    public GeneType<? extends Gene> getType() {
        return ModGenes.PASSIVE_GENE.value();
    }
}
