package com.lucasmellof.bioforge.events;

import com.lucasmellof.bioforge.Bioforge;
import com.lucasmellof.bioforge.commands.DebugCommand;
import com.lucasmellof.bioforge.datagen.ModLang;
import com.lucasmellof.bioforge.entity.IEntityWithGene;
import com.lucasmellof.bioforge.gene.GeneType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 17/10/2025
 */
@EventBusSubscriber(modid = Bioforge.MODID)
public class ServerEvents {

    @SubscribeEvent
    static void onRegisterCommands(RegisterCommandsEvent event) {
        DebugCommand.INSTANCE.register(event.getDispatcher());
    }

    @SubscribeEvent
    static void onInteract(PlayerInteractEvent.EntityInteractSpecific ev) {
        if (ev.getEntity().level().isClientSide || ev.getHand() == InteractionHand.OFF_HAND) return;
        if (!ev.getEntity().isShiftKeyDown()) {
            return;
        }

        IEntityWithGene genes = (IEntityWithGene) ev.getTarget();
        ev.getEntity().sendSystemMessage(ModLang.ITEM_GENES_LIST.as());
        for (GeneType<?> gene : genes.bioforge$getGenes()) {
            ev.getEntity().sendSystemMessage(gene.name());
        }
    }
}
