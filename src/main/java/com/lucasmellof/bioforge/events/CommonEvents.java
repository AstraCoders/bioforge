package com.lucasmellof.bioforge.events;

import com.google.gson.Gson;
import com.lucasmellof.bioforge.BioGeneMod;
import com.lucasmellof.bioforge.Const;
import com.lucasmellof.bioforge.blood.BloodTypeReloadListener;
import com.lucasmellof.bioforge.network.C2SStartCentrifugePacket;
import com.lucasmellof.bioforge.network.S2CStopCentrifugePacket;
import com.lucasmellof.bioforge.registry.ModGenes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 17/10/2025
 */
@EventBusSubscriber(modid = BioGeneMod.MODID)
public class CommonEvents {

    @SubscribeEvent
    static void onRegisterRegistry(NewRegistryEvent event) {
        event.register(ModGenes.GENE_REGISTRY);
    }

    @SubscribeEvent
    static void onRegisterPayload(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(ModList.get().getModContainerById(Const.MOD_ID).get().getModInfo().getVersion().toString());
        registrar.playToServer(C2SStartCentrifugePacket.TYPE, C2SStartCentrifugePacket.STREAM_CODEC, C2SStartCentrifugePacket::handle);
        registrar.playToClient(S2CStopCentrifugePacket.TYPE, S2CStopCentrifugePacket.STREAM_CODEC, S2CStopCentrifugePacket::handle);
    }

	@SubscribeEvent
	static void onRegisterListener(AddReloadListenerEvent event) {
        event.addListener(new BloodTypeReloadListener(new Gson()));
	}
}
