package com.lucasmellof.bioforge.registry;

import com.lucasmellof.bioforge.Const;
import com.lucasmellof.bioforge.block.entity.CentrifugeBlockEntity;
import com.lucasmellof.bioforge.block.entity.VialHolderBlockEntity;
import com.lucasmellof.bioforge.block.entity.MicroscopeBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 18/10/2025
 */
public class ModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
			DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Const.MOD_ID);


	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<VialHolderBlockEntity>> VIAL_HOLDER =
			BLOCK_ENTITIES.register("vial_holder",
					() -> BlockEntityType.Builder.of(VialHolderBlockEntity::new,
							ModBlocks.VIAL_HOLDER_BLOCK.get()
					).build(null)
			);


	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CentrifugeBlockEntity>> CENTRIFUGE =
			BLOCK_ENTITIES.register("centrifuge",
					() -> BlockEntityType.Builder.of(CentrifugeBlockEntity::new,
							ModBlocks.CENTRIFUGE_BLOCK.get()
					).build(null)
			);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MicroscopeBlockEntity>> MICROSCOPE =
			BLOCK_ENTITIES.register("microscope", () ->
					BlockEntityType.Builder.of(MicroscopeBlockEntity::new,
							ModBlocks.MICROSCOPE_BLOCK.get()).build(null));
}
