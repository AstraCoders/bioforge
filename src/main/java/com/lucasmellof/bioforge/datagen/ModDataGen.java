package com.lucasmellof.bioforge.datagen;

import com.lucasmellof.bioforge.Const;
import net.minecraft.data.DataGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 18/10/2025
 */
@EventBusSubscriber(modid = Const.MOD_ID)
public class ModDataGen {

	@SubscribeEvent
	static void onGatherData(GatherDataEvent event) {
		DataGenerator gen = event.getGenerator();
		gen.addProvider(event.includeClient(), new ModLang(gen.getPackOutput()));
	}
}
