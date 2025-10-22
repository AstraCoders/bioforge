package com.lucasmellof.bioforge.client.renderer;

import com.lucasmellof.bioforge.block.entity.VialHolderBlockEntity;
import com.lucasmellof.bioforge.client.model.VialHolderBlockEntityModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 19/10/2025
 */
public class VialHolderBlockEntityRenderer extends GeoBlockRenderer<VialHolderBlockEntity> {

	public VialHolderBlockEntityRenderer() {
        super(new VialHolderBlockEntityModel());
	}

	@Override
	public void postRender(PoseStack poseStack, VialHolderBlockEntity be, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
		if (!be.hasVial()) return;

		var item = be.getItem();
		if (item.isEmpty()) return;
		poseStack.pushPose();
		poseStack.scale(0.55f, 0.55f, 0.55f);
		poseStack.translate(0, 0.5f, 0);
		Minecraft.getInstance().getItemRenderer().renderStatic(item, ItemDisplayContext.FIXED, packedLight,packedOverlay,poseStack, bufferSource, Minecraft.getInstance().level, 0);
		poseStack.popPose();
	}

}
