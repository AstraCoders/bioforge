package com.lucasmellof.bioforge.events;

import com.lucasmellof.bioforge.Bioforge;
import com.lucasmellof.bioforge.registry.ModGenes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 17/10/2025
 */
@EventBusSubscriber(modid = Bioforge.MODID)
public class CommonEvents {

    @SubscribeEvent
    static void onRegisterRegistry(NewRegistryEvent event) {
        event.register(ModGenes.GENE_REGISTRY);
    }
}
