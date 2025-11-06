package com.lucasmellof.bioforge.gene;

import com.lucasmellof.bioforge.registry.ModGenes;
import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 18/10/2025
 */
public record GeneType<T extends Gene>(
        ResourceLocation location, GeneInfo info, Supplier<GeneAction> action, GeneFactory<T> factory, Codec<T> codec) {

    public T create() {
        return factory.create(location(), this);
    }

    public static final Codec<GeneType<?>> CODEC =
            ResourceLocation.CODEC.xmap(ModGenes.GENE_REGISTRY::get, GeneType::location);

    public MutableComponent name() {
        return Component.translatable(
                "gene." + location().getNamespace() + "." + location().getPath());
    }
}
