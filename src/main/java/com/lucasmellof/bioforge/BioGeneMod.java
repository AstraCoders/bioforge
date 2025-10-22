package com.lucasmellof.bioforge;

import com.lucasmellof.bioforge.registry.*;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(BioGeneMod.MODID)
public class BioGeneMod {
	public static final String MODID = "biogene";
	private static final Logger LOGGER = LogUtils.getLogger();

	public BioGeneMod(IEventBus modEventBus, ModContainer modContainer) {
		modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);


		ModMenuTypes.MENUS.register(modEventBus);
		ModGenes.GENES.register(modEventBus);
		ModComponentTypes.COMPONENT_TYPES.register(modEventBus);
		ModBlocks.BLOCKS.register(modEventBus);
		ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
		ModItems.ITEMS.register(modEventBus);
		ModArguments.ARGUMENTS.register(modEventBus);
		ModCreativeTab.CREATIVE_MODE_TABS.register(modEventBus);
	}

}
