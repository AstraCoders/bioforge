package com.lucasmellof.bioforge.client.model;

import com.lucasmellof.bioforge.Const;
import com.lucasmellof.bioforge.items.GeckoBlockItem;
import com.lucasmellof.bioforge.items.VialItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 18/10/2025
 */
public class CentrifugeItemModel extends GeoModel<GeckoBlockItem> {
	private static final ResourceLocation MODEL = Const.of("geo/item/centrifuge.geo.json");
	private static final ResourceLocation ANIMATIONS = Const.of("animations/item/vial.animation.json");
	private static final ResourceLocation TEXTURE = Const.of("textures/block/centrifuge.png");

	@Override
	public ResourceLocation getModelResource(GeckoBlockItem animatable) {
		return MODEL;
	}

	@Override
	public ResourceLocation getTextureResource(GeckoBlockItem animatable) {
		return TEXTURE;
	}

	@Override
	public ResourceLocation getAnimationResource(GeckoBlockItem animatable) {
		return ANIMATIONS;
	}
}
