package com.lucasmellof.bioforge.registry;

import com.lucasmellof.bioforge.Const;
import com.lucasmellof.bioforge.items.SyringeItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 18/10/2025
 */
public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Const.MOD_ID);
    public static final DeferredHolder<Item, SyringeItem> SYRINGE_ITEM = ITEMS.register("syringe", SyringeItem::new);
}
