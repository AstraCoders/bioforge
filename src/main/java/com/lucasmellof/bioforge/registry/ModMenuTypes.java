package com.lucasmellof.bioforge.registry;

import com.lucasmellof.bioforge.BioGeneMod;
import com.lucasmellof.bioforge.block.CentrifugeBlock;
import com.lucasmellof.bioforge.block.entity.CentrifugeBlockEntity;
import com.lucasmellof.bioforge.block.entity.MicroscopeBlockEntity;
import com.lucasmellof.bioforge.menu.CentrifugeMenu;
import com.lucasmellof.bioforge.menu.MicroscopeMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * @author Rok, Pedro Lucas nmm. Created on 19/10/2025
 * @project bioforge
 */
public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = 
            DeferredRegister.create(Registries.MENU, BioGeneMod.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<MicroscopeMenu>> MICROSCOPE_MENU =
            MENUS.register("microscope_menu", () ->
                    IMenuTypeExtension.create((windowId, inv, data) -> {
                        var blockEntity = inv.player.level().getBlockEntity(data.readBlockPos());
                        if (blockEntity instanceof MicroscopeBlockEntity microscopeBlockEntity) {
                            return new MicroscopeMenu(windowId, inv, microscopeBlockEntity, microscopeBlockEntity.data);
                        }
                        return new MicroscopeMenu(windowId, inv);
                    }));

    public static final DeferredHolder<MenuType<?>, MenuType<CentrifugeMenu>> CENTRIFUGE_MENU =
            MENUS.register("centrifuge_menu", () ->
                    IMenuTypeExtension.create((windowId, inv, data) -> {
                        var blockEntity = inv.player.level().getBlockEntity(data.readBlockPos());
                        if (blockEntity instanceof CentrifugeBlockEntity centrifugeBlockEntity) {
                            return new CentrifugeMenu(windowId, inv, centrifugeBlockEntity, centrifugeBlockEntity.data);
                        }
                        return new CentrifugeMenu(windowId, inv);
                    }));
}