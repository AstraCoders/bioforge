package com.lucasmellof.bioforge.registry;

import com.lucasmellof.bioforge.Bioforge;
import com.lucasmellof.bioforge.data.BloodData;
import com.lucasmellof.bioforge.gene.GeneCodecs;
import com.lucasmellof.bioforge.gene.GeneType;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 18/10/2025
 */
public class ModComponentTypes {
    public static final DeferredRegister.DataComponents COMPONENT_TYPES =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Bioforge.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<HolderSet<GeneType<?>>>> GENE =
            COMPONENT_TYPES.registerComponentType("gene", it -> it.persistent(GeneCodecs.HOLDER_SET_CODEC)
                    .networkSynchronized(GeneCodecs.HOLDER_SET_STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BloodData>> BLOOD_DATA =
            COMPONENT_TYPES.register("blood_data", () ->
                    DataComponentType.<BloodData>builder()
                            .persistent(BloodData.CODEC)
                            .networkSynchronized(BloodData.STREAM_CODEC)
                            .build()
            );

}
