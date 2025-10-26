package com.lucasmellof.bioforge.client.renderer;

import com.lucasmellof.bioforge.client.model.CentrifugeItemModel;
import com.lucasmellof.bioforge.items.GeckoBlockItem;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

/**
 * @author Rok, Pedro Lucas nmm. Created on 18/10/2025
 * @project bioforge
 */
@Slf4j
public class CentrifugeItemRenderer extends GeoItemRenderer<GeckoBlockItem> {

    public CentrifugeItemRenderer() {
        super(new CentrifugeItemModel());
    }

    @Override
    public RenderType getRenderType(
            GeckoBlockItem animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }
}
