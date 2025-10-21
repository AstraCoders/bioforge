package com.lucasmellof.bioforge.client.renderer;

import com.lucasmellof.bioforge.client.model.CentrifugeItemModel;
import com.lucasmellof.bioforge.client.model.VialItemModel;
import com.lucasmellof.bioforge.data.BloodData;
import com.lucasmellof.bioforge.items.GeckoBlockItem;
import com.lucasmellof.bioforge.items.VialItem;
import com.lucasmellof.bioforge.registry.ModComponentTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import java.util.List;

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
