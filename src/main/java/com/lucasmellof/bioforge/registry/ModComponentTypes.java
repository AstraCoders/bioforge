package com.lucasmellof.bioforge.registry;

import com.lucasmellof.bioforge.BioGeneMod;
import com.lucasmellof.bioforge.blood.BloodData;
import com.lucasmellof.bioforge.gene.Gene;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 18/10/2025
 */
public class ModComponentTypes {
    public static final DeferredRegister.DataComponents COMPONENT_TYPES =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, BioGeneMod.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<Gene>>> GENE =
            COMPONENT_TYPES.registerComponentType("genes", it -> it.persistent(Gene.CODEC.listOf())
                    .networkSynchronized(Gene.STREAM_CODEC_LIST));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<BloodData>>> BLOOD_DATA =
            COMPONENT_TYPES.register("blood_data", () ->
                    DataComponentType.<List<BloodData>>builder()
                            .persistent(BloodData.HOLDER_LIST_CODEC)
                            .networkSynchronized(BloodData.HOLDER_LIST_STREAM_CODEC)
                            .build()
            );


    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> BLOOD_MIX_COUNT_DATA =
            COMPONENT_TYPES.register("blood_mix_count", () ->
                                                           DataComponentType.<Integer>builder()
                                                                   .persistent(BloodData.MIX_COUNT_CODEC)
                                                                   .networkSynchronized(BloodData.MIX_COUNT_STREAM_CODEC)
                                                                   .build()
            );


}
