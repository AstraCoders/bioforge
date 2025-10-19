package com.lucasmellof.bioforge.registry;

import com.lucasmellof.bioforge.Const;
import com.lucasmellof.bioforge.gene.GeneType;
import com.lucasmellof.bioforge.gene.types.AggressiveGene;
import com.lucasmellof.bioforge.gene.types.BlankGene;
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
    public static final ResourceKey<Registry<GeneType<?>>> GENE_REGISTRY_KEY =
            ResourceKey.createRegistryKey(Const.of("genes"));

    public static final Registry<GeneType<?>> GENE_REGISTRY = new RegistryBuilder<>(GENE_REGISTRY_KEY).sync(true).create();

    public static final DeferredRegister<GeneType<?>> GENES = DeferredRegister.create(GENE_REGISTRY_KEY, Const.MOD_ID);

    public static final DeferredHolder<GeneType<?>, GeneType<PassiveGene>> PASSIVE_GENE =
            GENES.register("passive_gene", (it) -> new GeneType<>(it, PassiveGene::new, PassiveGene.CODEC));

    public static final DeferredHolder<GeneType<?>, GeneType<AggressiveGene>> AGGRESSIVE_GENE =
            GENES.register("aggressive_gene", (it) -> new GeneType<>(it, AggressiveGene::new, AggressiveGene.CODEC));

    public static final DeferredHolder<GeneType<?>, GeneType<BlankGene>> BLANK_GENE =
            GENES.register("blank_gene", (it) -> new GeneType<>(it, BlankGene::new, BlankGene.CODEC));
}
