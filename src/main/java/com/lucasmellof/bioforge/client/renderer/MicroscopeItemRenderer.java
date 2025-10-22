package com.lucasmellof.bioforge.client.renderer;

import com.lucasmellof.bioforge.client.model.MicroscopeItemModel;
import com.lucasmellof.bioforge.items.GeckoBlockItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoItemRenderer;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 19/10/2025
 */
public class MicroscopeItemRenderer extends GeoItemRenderer<GeckoBlockItem> {

    public MicroscopeItemRenderer() {
        super(new MicroscopeItemModel());
    }


    @Override
    public void renderRecursively(PoseStack poseStack, GeckoBlockItem animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        if (bone.getName().equals("sample")) {
            return;
        }
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }
}
