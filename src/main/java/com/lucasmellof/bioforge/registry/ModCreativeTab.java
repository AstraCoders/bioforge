package com.lucasmellof.bioforge.registry;

import com.lucasmellof.bioforge.Bioforge;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * @author Rok, Pedro Lucas nmm. Created on 21/04/2025
 * @project Create Hypertube
 */
public class ModCreativeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Bioforge.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TUBE_TAB =
            CREATIVE_MODE_TABS.register("create_hypertubes", () ->
                    CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup." + Bioforge.MODID))
                            .icon(() -> ModItems.CENTRIFUGE_ITEM.get().getDefaultInstance())
                            .displayItems((parameters, output) -> {
                                output.accept(ModItems.MICROCOPE_ITEM.get());
                                output.accept(ModItems.VIAL_ITEM.get());
                                output.accept(ModItems.SYRINGE_ITEM.get());
                                output.accept(ModItems.VIAL_HOLDER_ITEM.get());
                                output.accept(ModItems.CENTRIFUGE_ITEM.get());
                            })
                            .build()
            );

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}