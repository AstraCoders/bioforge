package com.lucasmellof.bioforge.registry;

import com.lucasmellof.bioforge.Const;
import com.lucasmellof.bioforge.gene.*;
import com.lucasmellof.bioforge.gene.types.AggressiveGene;
import com.lucasmellof.bioforge.gene.types.BreedableGene;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 17/10/2025
 */
public class ModGenes {
    public static final ResourceKey<Registry<GeneType<?>>> GENE_REGISTRY_KEY =
            ResourceKey.createRegistryKey(Const.of("genes"));

    public static final Registry<GeneType<?>> GENE_REGISTRY =
            new RegistryBuilder<>(GENE_REGISTRY_KEY).sync(true).create();

    public static final DeferredRegister<GeneType<?>> GENES = DeferredRegister.create(GENE_REGISTRY_KEY, Const.MOD_ID);

    public static final DeferredHolder<GeneType<?>, GeneType<Gene>> AGGRESSIVE_GENE =
            register("aggresive_gene", AggressiveGene.INFO, AggressiveGene::new);

    public static final DeferredHolder<GeneType<?>, GeneType<Gene>> BREEDABLE_GENE =
            register("breedable_gene", BreedableGene.INFO, BreedableGene::new);

    public static DeferredHolder<GeneType<?>, GeneType<Gene>> register(
            String id, GeneInfo info, Supplier<GeneAction> action) {
        return GENES.register(id, it -> new GeneType<>(it, info, action, Gene::new, Gene.CODEC));
    }
}
