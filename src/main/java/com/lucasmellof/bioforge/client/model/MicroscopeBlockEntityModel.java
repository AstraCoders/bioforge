package com.lucasmellof.bioforge.client.model;

import com.lucasmellof.bioforge.Const;
import com.lucasmellof.bioforge.block.entity.MicroscopeBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 18/10/2025
 */
public class MicroscopeBlockEntityModel extends GeoModel<MicroscopeBlockEntity> {
    private static final ResourceLocation MODEL = Const.of("geo/block/microscope.geo.json");
    private static final ResourceLocation ANIMATIONS = Const.of("animations/item/vial.animation.json");
    private static final ResourceLocation TEXTURE = Const.of("textures/block/microscope.png");

    @Override
    public ResourceLocation getModelResource(MicroscopeBlockEntity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(MicroscopeBlockEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(MicroscopeBlockEntity animatable) {
        return ANIMATIONS;
    }
}
