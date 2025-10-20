package com.lucasmellof.bioforge.client.model;

import com.lucasmellof.bioforge.Const;
import com.lucasmellof.bioforge.block.entity.CentrifugeBlockEntity;
import com.lucasmellof.bioforge.block.entity.VialHolderBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 18/10/2025
 */
public class CentrifugeBlockEntityModel extends GeoModel<CentrifugeBlockEntity> {
	private static final ResourceLocation MODEL = Const.of("geo/block/centrifuge.geo.json");
	private static final ResourceLocation ANIMATIONS = Const.of("animations/block/centrifuge.animation.json");
	private static final ResourceLocation TEXTURE = Const.of("textures/block/centrifuge.png");

	@Override
	public ResourceLocation getModelResource(CentrifugeBlockEntity animatable) {
		return MODEL;
	}

	@Override
	public ResourceLocation getTextureResource(CentrifugeBlockEntity animatable) {
		return TEXTURE;
	}

	@Override
	public ResourceLocation getAnimationResource(CentrifugeBlockEntity animatable) {
		return ANIMATIONS;
	}
}
