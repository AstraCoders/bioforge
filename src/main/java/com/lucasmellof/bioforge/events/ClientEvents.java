package com.lucasmellof.bioforge.events;

import com.lucasmellof.bioforge.Const;
import com.lucasmellof.bioforge.client.renderer.CentrifugeBlockEntityRenderer;
import com.lucasmellof.bioforge.client.renderer.MicroscopeBlockEntityRenderer;
import com.lucasmellof.bioforge.client.renderer.VialHolderBlockEntityRenderer;
import com.lucasmellof.bioforge.registry.ModBlockEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 19/10/2025
 */
@EventBusSubscriber(value = Dist.CLIENT, modid = Const.MOD_ID)
public class ClientEvents {

	@SubscribeEvent
	static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(
				ModBlockEntities.VIAL_HOLDER.get(),context -> new VialHolderBlockEntityRenderer());
        event.registerBlockEntityRenderer(
                ModBlockEntities.CENTRIFUGE.get(), context -> new CentrifugeBlockEntityRenderer());
		event.registerBlockEntityRenderer(
				ModBlockEntities.MICROSCOPE.get(), context -> new MicroscopeBlockEntityRenderer());
	}


}
