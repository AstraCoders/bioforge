package com.lucasmellof.bioforge.registry;

import com.lucasmellof.bioforge.Const;
import com.lucasmellof.bioforge.client.renderer.CentrifugeItemRenderer;
import com.lucasmellof.bioforge.client.renderer.MicroscopeItemRenderer;
import com.lucasmellof.bioforge.items.GeckoBlockItem;
import com.lucasmellof.bioforge.items.SyringeItem;
import com.lucasmellof.bioforge.items.VialItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 18/10/2025
 */
public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Const.MOD_ID);
    public static final DeferredHolder<Item, SyringeItem> SYRINGE_ITEM = ITEMS.register("syringe", SyringeItem::new);
    public static final DeferredHolder<Item, VialItem> VIAL_ITEM = ITEMS.register("vial", VialItem::new);
    public static final DeferredHolder<Item, BlockItem> MICROCOPE_ITEM = ITEMS.register("microscope", (prop)
            -> new GeckoBlockItem<>(ModBlocks.MICROSCOPE_BLOCK.get(), new Item.Properties(), MicroscopeItemRenderer::new));
    public static final DeferredHolder<Item, BlockItem> VIAL_HOLDER_ITEM = ITEMS.register("vial_holder", (prop)
            -> new BlockItem(ModBlocks.VIAL_HOLDER_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> CENTRIFUGE_ITEM = ITEMS.register("centrifuge", (prop)
            -> new GeckoBlockItem<>(ModBlocks.CENTRIFUGE_BLOCK.get(), new Item.Properties(), CentrifugeItemRenderer::new));
}
