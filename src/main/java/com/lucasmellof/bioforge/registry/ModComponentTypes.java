package com.lucasmellof.bioforge.registry;

import com.lucasmellof.bioforge.Bioforge;
import com.lucasmellof.bioforge.gene.Gene;
import com.mojang.serialization.Codec;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 18/10/2025
 */
public class ModComponentTypes {
    public static final DeferredRegister.DataComponents COMPONENT_TYPES =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Bioforge.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<HolderSet<Gene>>> GENE =
            COMPONENT_TYPES.registerComponentType("gene", it -> it.networkSynchronized());
}
