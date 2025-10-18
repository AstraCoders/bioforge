package com.lucasmellof.bioforge.gene;

import com.lucasmellof.bioforge.registry.ModGenes;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceLocation;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 18/10/2025
 */
public class GeneCodecs {
    public static final Codec<Gene> GENE_CODEC = GeneType.CODEC.dispatch(Gene::getType, GeneType::codec);
    public static final Codec<Holder<GeneType<?>>> GENE_TYPE_CODEC =
            RegistryFileCodec.create(ModGenes.GENE_REGISTRY_KEY, GeneType.CODEC);

    public static final StreamCodec<RegistryFriendlyByteBuf, GeneType<?>> DIRECT_STREAM_CODEC =
            StreamCodec.composite(ResourceLocation.STREAM_CODEC, GeneType::location, ModGenes.GENE_REGISTRY::get);

    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<GeneType<?>>> STREAM_CODEC =
            ByteBufCodecs.holder(ModGenes.GENE_REGISTRY_KEY, DIRECT_STREAM_CODEC);

    public static final Codec<HolderSet<GeneType<?>>> HOLDER_SET_CODEC =
            HolderSetCodec.create(ModGenes.GENE_REGISTRY_KEY, GENE_TYPE_CODEC, false);

    public static final StreamCodec<RegistryFriendlyByteBuf, HolderSet<GeneType<?>>> HOLDER_SET_STREAM_CODEC =
            ByteBufCodecs.holderSet(ModGenes.GENE_REGISTRY_KEY);
}
