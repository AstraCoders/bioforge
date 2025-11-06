package com.lucasmellof.bioforge.gene.types;

import com.lucasmellof.bioforge.entity.IEntityWithGene;
import com.lucasmellof.bioforge.gene.GeneAction;
import com.lucasmellof.bioforge.gene.GeneInfo;
import net.minecraft.world.entity.EntityType;

import java.util.List;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 26/10/2025
 */
public class FireResistanceGene implements GeneAction {
    public static final GeneInfo INFO = GeneInfo.builder()
            .incompatibleEntities(List.of(EntityType.getKey(EntityType.SNOW_GOLEM)))
            .build();

    @Override
    public boolean canApply(IEntityWithGene entity) {
        return false;
    }

    @Override
    public boolean apply(IEntityWithGene entity) {
        return false;
    }

    @Override
    public void remove(IEntityWithGene entity) {

    }
}
