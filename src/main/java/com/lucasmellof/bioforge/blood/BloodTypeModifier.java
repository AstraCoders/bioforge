package com.lucasmellof.bioforge.blood;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 25/10/2025
 */
@Getter
public class BloodTypeModifier {
	private List<ResourceLocation> entities;
	private int color;

	public BloodTypeModifier(List<ResourceLocation> entities, int color) {
		this.entities = entities;
		// minecraft uses ARGB format for colors, so we need to convert from RGB to ARGB
		this.color = (0xFF << 24) | (color & 0x00FFFFFF);
	}

	public static BloodTypeModifier DEFAULT = new BloodTypeModifier(List.of(), 0xFF0000);

    public static final Codec<BloodTypeModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    ResourceLocation.CODEC.listOf().fieldOf("entities").forGetter(BloodTypeModifier::getEntities),
                    Codec.INT.fieldOf("color").forGetter(BloodTypeModifier::getColor))
            .apply(instance, BloodTypeModifier::new));

    public static BloodTypeModifier of(int color, ResourceLocation... entities) {
        return new BloodTypeModifier(List.of(entities), color);
    }

	public static BloodTypeModifier of(int color, EntityType<?>... entities) {
		var list = new ArrayList<ResourceLocation>();
		for (EntityType<?> entity : entities) {
			list.add(EntityType.getKey(entity));
		}

		return new BloodTypeModifier(list, color);
	}

}
