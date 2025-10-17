package com.lucasmellof.bioforge.events;

import com.lucasmellof.bioforge.Bioforge;
import com.lucasmellof.bioforge.commands.DebugCommand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 17/10/2025
 */
@EventBusSubscriber(modid = Bioforge.MODID)
public class ServerEvents {

	@SubscribeEvent
	static void onRegisterCommands(RegisterCommandsEvent event) {
		new DebugCommand().register(event.getDispatcher());
	}
}
