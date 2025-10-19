package com.lucasmellof.bioforge.client.renderer;

import com.lucasmellof.bioforge.client.model.SyringeItemModel;
import com.lucasmellof.bioforge.client.model.VialItemModel;
import com.lucasmellof.bioforge.data.BloodData;
import com.lucasmellof.bioforge.items.SyringeItem;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoItemRenderer;

/**
 * @author Rok, Pedro Lucas nmm. Created on 18/10/2025
 * @project bioforge
 */
@Slf4j
public class VialItemRenderer extends GeoItemRenderer<VialItem> {

    public VialItemRenderer() {
        super(new VialItemModel());
    }

    @Override
    public void renderByItem(
            ItemStack stack,
            ItemDisplayContext displayContext,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay) {

        this.currentItemStack = stack;

        super.renderByItem(stack, displayContext, poseStack, bufferSource, packedLight, packedOverlay);
    }

    @Override
    public long getInstanceId(VialItem animatable) {
        if (this.currentItemStack != null) {
            return this.currentItemStack.hashCode();
        }
        return super.getInstanceId(animatable);
    }

    @Override
    public void actuallyRender(
            PoseStack poseStack,
            VialItem animatable,
            BakedGeoModel model,
            RenderType renderType,
            MultiBufferSource bufferSource,
            VertexConsumer buffer,
            boolean isReRender,
            float partialTick,
            int packedLight,
            int packedOverlay,
            int colour) {

        try {
            super.actuallyRender(
                    poseStack,
                    animatable,
                    model,
                    renderType,
                    bufferSource,
                    buffer,
                    isReRender,
                    partialTick,
                    packedLight,
                    packedOverlay,
                    colour);
        } catch (Exception e) {
            log.error("Erro ao renderizar seringa: ", e);
        }
    }

    @Override
    public void renderRecursively(
            PoseStack poseStack,
            VialItem animatable,
            GeoBone bone,
            RenderType renderType,
            MultiBufferSource bufferSource,
            VertexConsumer buffer,
            boolean isReRender,
            float partialTick,
            int packedLight,
            int packedOverlay,
            int renderColor) {

        ItemStack stack = this.currentItemStack;
        BloodData bloodData = stack != null ? stack.get(ModComponentTypes.BLOOD_DATA) : null;

        String boneName = bone.getName();

        if (bloodData != null) {
            int colorWithBlood = bloodData.color();
            var mix = VialItem.getMixCount(stack);
            if (boneName.equalsIgnoreCase("liquid1") && mix >= 1) {
               super.renderRecursively(
                        poseStack,
                        animatable,
                        bone,
                        renderType,
                        bufferSource,
                        buffer,
                        isReRender,
                        partialTick,
                        packedLight,
                        packedOverlay,
                colorWithBlood);
               return;
            }
            if (boneName.equalsIgnoreCase("liquid2") && mix >= 2) {
                super.renderRecursively(
                        poseStack,
                        animatable,
                        bone,
                        renderType,
                        bufferSource,
                        buffer,
                        isReRender,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        colorWithBlood);
                return;
            }
        }
        if (boneName.equalsIgnoreCase("liquid1") || boneName.equalsIgnoreCase("liquid2")) {
            // Skip rendering this bone if there's no blood data or not enough mixes
            return;
        }
        super.renderRecursively(
                poseStack,
                animatable,
                bone,
                renderType,
                bufferSource,
                buffer,
                isReRender,
                partialTick,
                packedLight,
                packedOverlay,
                renderColor);
    }

    @Override
    public RenderType getRenderType(
            VialItem animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }
}
