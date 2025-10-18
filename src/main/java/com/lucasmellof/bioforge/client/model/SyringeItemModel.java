package com.lucasmellof.bioforge.client.model;

import com.lucasmellof.bioforge.Const;
import com.lucasmellof.bioforge.items.SyringeItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 18/10/2025
 */
public class SyringeItemModel extends GeoModel<SyringeItem> {
	private static final ResourceLocation MODEL = Const.of("geo/item/syringe.geo.json");
	private static final ResourceLocation ANIMATIONS = Const.of("animations/item/syringe.animation.json");
	private static final ResourceLocation TEXTURE = Const.of("textures/items/syringe.png");

	@Override
	public ResourceLocation getModelResource(SyringeItem animatable) {
		return MODEL;
	}

	@Override
	public ResourceLocation getTextureResource(SyringeItem animatable) {
		return TEXTURE;
	}

	@Override
	public ResourceLocation getAnimationResource(SyringeItem animatable) {
		return ANIMATIONS;
	}
}
