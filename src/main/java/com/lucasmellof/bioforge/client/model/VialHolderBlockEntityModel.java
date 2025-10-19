package com.lucasmellof.bioforge.client.model;

import com.lucasmellof.bioforge.Const;
import com.lucasmellof.bioforge.block.entity.VialHolderBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 18/10/2025
 */
public class VialHolderBlockEntityModel extends GeoModel<VialHolderBlockEntity> {
	private static final ResourceLocation MODEL = Const.of("geo/block/vial_holder.geo.json");
	private static final ResourceLocation ANIMATIONS = Const.of("animations/block/vial_holder.animation.json");
	private static final ResourceLocation TEXTURE = Const.of("textures/block/vial_holder.png");

	@Override
	public ResourceLocation getModelResource(VialHolderBlockEntity animatable) {
		return MODEL;
	}

	@Override
	public ResourceLocation getTextureResource(VialHolderBlockEntity animatable) {
		return TEXTURE;
	}

	@Override
	public ResourceLocation getAnimationResource(VialHolderBlockEntity animatable) {
		return ANIMATIONS;
	}
}
