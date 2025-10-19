package com.lucasmellof.bioforge.registry;

import com.lucasmellof.bioforge.Bioforge;
import com.lucasmellof.bioforge.commands.arguments.GeneArguments;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 18/10/2025
 */
public class ModArguments {


	public static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARGUMENTS =
			DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, Bioforge.MODID);

	static {
		var gene = SingletonArgumentInfo.contextFree(GeneArguments::gene);
		ARGUMENTS.register("gene", () -> gene);

		ArgumentTypeInfos.registerByClass(GeneArguments.class, gene);
	}
}
