package com.lucasmellof.bioforge.gene;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 20/10/2025
 */
@Getter
@Builder
public class GeneInfo {
    public static Codec<GeneInfo> CODEC = RecordCodecBuilder.create(it -> it.group(
                    ResourceLocation.CODEC
                            .listOf()
                            .fieldOf("incompatible_genes")
                            .orElse(List.of())
                            .forGetter(GeneInfo::getIncompatibleGenes),
                    ResourceLocation.CODEC
                            .listOf()
                            .fieldOf("incompatible_entities")
                            .orElse(List.of())
                            .forGetter(GeneInfo::getIncompatibleEntities),
                    ResourceLocation.CODEC
                            .listOf()
                            .fieldOf("applicable_entities")
                            .orElse(List.of())
                            .forGetter(GeneInfo::getApplicableEntities),
                    Codec.INT.fieldOf("level").orElse(1).forGetter(GeneInfo::getLevel))
            .apply(it, GeneInfo::new));

    @Builder.Default
    @Accessors(makeFinal = true)
    private final List<ResourceLocation> incompatibleGenes = List.of();

    @Builder.Default
    @Accessors(makeFinal = true)
    private final List<ResourceLocation> incompatibleEntities = List.of();

    @Builder.Default
    @Accessors(makeFinal = true)
    private final List<ResourceLocation> applicableEntities = List.of();

    @Builder.Default
    private int level = 1;
}
