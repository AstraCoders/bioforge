package com.lucasmellof.bioforge.gene;

import com.lucasmellof.bioforge.registry.ModGenes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 18/10/2025
 */
public record GeneType<T extends Gene>(ResourceLocation location, GeneFactory<T> factory, MapCodec<T> codec) {

    public T create() {
        return factory.create(location());
    }

    public static final Codec<GeneType<?>> CODEC =
            ResourceLocation.CODEC.xmap(ModGenes.GENE_REGISTRY::get, GeneType::location);

    public Component name() {
        return Component.translatable("gene." + location().getNamespace() + "." + location().getPath());
    }
}
