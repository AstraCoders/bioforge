package com.lucasmellof.bioforge.events;

import com.lucasmellof.bioforge.Bioforge;
import com.lucasmellof.bioforge.client.screen.MicroscopeScreen;
import com.lucasmellof.bioforge.registry.ModMenuTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

/**
 * @author Rok, Pedro Lucas nmm. Created on 19/10/2025
 * @project bioforge
 */
@EventBusSubscriber(modid = Bioforge.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.MICROSCOPE_MENU.get(), MicroscopeScreen::new);
    }
}
