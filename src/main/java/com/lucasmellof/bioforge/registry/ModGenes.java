package com.lucasmellof.bioforge.registry;

import com.lucasmellof.bioforge.Const;
import com.lucasmellof.bioforge.gene.Gene;
import com.lucasmellof.bioforge.gene.types.AggressiveGene;
import com.lucasmellof.bioforge.gene.types.PassiveGene;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 17/10/2025
 */
public class ModGenes {
    public static final ResourceKey<Registry<Gene>> GENE_REGISTRY_KEY =
            ResourceKey.createRegistryKey(Const.of("genes"));
    public static final Registry<Gene> GENE_REGISTRY = new RegistryBuilder<>(GENE_REGISTRY_KEY).create();

    public static final DeferredRegister<Gene> GENES = DeferredRegister.create(GENE_REGISTRY_KEY, Const.MOD_ID);

    public static final DeferredHolder<Gene, Gene> PASSIVE_GENE = GENES.register("passive_gene", PassiveGene::new);
    public static final DeferredHolder<Gene, Gene> AGGRESSIVE_GENE = GENES.register("aggressive_gene", AggressiveGene::new);
}
