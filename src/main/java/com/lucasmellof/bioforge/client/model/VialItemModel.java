package com.lucasmellof.bioforge.client.model;

import com.lucasmellof.bioforge.Const;
import com.lucasmellof.bioforge.items.VialItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 18/10/2025
 */
public class VialItemModel extends GeoModel<VialItem> {
	private static final ResourceLocation MODEL = Const.of("geo/item/vial.geo.json");
	private static final ResourceLocation ANIMATIONS = Const.of("animations/item/vial.animation.json");
	private static final ResourceLocation TEXTURE = Const.of("textures/items/blood_vial.png");

	@Override
	public ResourceLocation getModelResource(VialItem animatable) {
		return MODEL;
	}

	@Override
	public ResourceLocation getTextureResource(VialItem animatable) {
		return TEXTURE;
	}

	@Override
	public ResourceLocation getAnimationResource(VialItem animatable) {
		return ANIMATIONS;
	}
}
