package com.lucasmellof.bioforge.blood;

import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 25/10/2025
 */
public class BloodTypeRegistry {
	private static final Map<EntityType<?>, BloodTypeModifier> REGISTRY = new HashMap<>();

	public static void register(@NotNull EntityType<?> entityType, @NotNull BloodTypeModifier modifier) {
		REGISTRY.put(entityType, modifier);
	}

	@NotNull
	public static BloodTypeModifier getModifier(@NotNull EntityType<?> entityType) {
		BloodTypeModifier modifier = REGISTRY.get(entityType);
		if (modifier == null) {
			return BloodTypeModifier.DEFAULT;
		}
		return modifier;
	}

	public static void clear() {
		REGISTRY.clear();
	}



}
