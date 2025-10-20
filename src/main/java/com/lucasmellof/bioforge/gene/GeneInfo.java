package com.lucasmellof.bioforge.gene;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;

import java.util.List;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 20/10/2025
 */
@Getter
@Builder
public class GeneInfo {
    @Builder.Default
    @Accessors(makeFinal = true)
    private final List<Holder<GeneType<?>>> incompatibleGenes = List.of();

    @Builder.Default
    @Accessors(makeFinal = true)
    private final List<EntityType<?>> incompatibleEntities = List.of();

    @Builder.Default
    @Accessors(makeFinal = true)
    private final List<EntityType<?>> applicableEntities = List.of();

    @Builder.Default
    private int level = 1;
}
