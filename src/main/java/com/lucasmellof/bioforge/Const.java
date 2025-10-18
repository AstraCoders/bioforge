package com.lucasmellof.bioforge;

import net.minecraft.resources.ResourceLocation;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 17/10/2025
 */
public class Const {
	public static final String MOD_ID = Bioforge.MODID;
	public static ResourceLocation of(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
