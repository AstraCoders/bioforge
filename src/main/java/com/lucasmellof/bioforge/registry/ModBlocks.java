package com.lucasmellof.bioforge.registry;

import com.lucasmellof.bioforge.Const;
import com.lucasmellof.bioforge.block.CentrifugeBlock;
import com.lucasmellof.bioforge.block.VialHolderBlock;
import com.lucasmellof.bioforge.block.MicroscopeBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 18/10/2025
 */
public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, Const.MOD_ID);

    public static final DeferredHolder<Block, VialHolderBlock> VIAL_HOLDER_BLOCK =
            BLOCKS.register("vial_holder", VialHolderBlock::new);

    public static final DeferredHolder<Block, CentrifugeBlock> CENTRIFUGE_BLOCK =
            BLOCKS.register("centrifuge", CentrifugeBlock::new);

    public static final DeferredHolder<Block, MicroscopeBlock> MICROSCOPE_BLOCK =
            BLOCKS.register("microscope", MicroscopeBlock::new);
}
