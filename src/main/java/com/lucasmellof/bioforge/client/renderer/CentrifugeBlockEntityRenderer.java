package com.lucasmellof.bioforge.client.renderer;

import com.lucasmellof.bioforge.block.entity.CentrifugeBlockEntity;
import com.lucasmellof.bioforge.block.entity.VialHolderBlockEntity;
import com.lucasmellof.bioforge.client.model.CentrifugeBlockEntityModel;
import com.lucasmellof.bioforge.client.model.VialHolderBlockEntityModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 19/10/2025
 */
public class CentrifugeBlockEntityRenderer extends GeoBlockRenderer<CentrifugeBlockEntity> {

	public CentrifugeBlockEntityRenderer() {
        super(new CentrifugeBlockEntityModel());
	}

	@Override
	public void postRender(PoseStack poseStack, CentrifugeBlockEntity be, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
//		if (!be.hasVial()) return;
//
//		var item = be.getItem();
//		if (item.isEmpty()) return;
//		poseStack.pushPose();
//		poseStack.scale(0.55f, 0.55f, 0.55f);
//		poseStack.translate(0, -0.03f, 0);
//		Minecraft.getInstance().getItemRenderer().renderStatic(item, ItemDisplayContext.FIXED, packedLight,packedOverlay,poseStack, bufferSource, Minecraft.getInstance().level, 0);
//		poseStack.popPose();
	}

	@Override
	public void renderRecursively(PoseStack poseStack, CentrifugeBlockEntity be, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
		if (!bone.getName().contains("vial")) {
			super.renderRecursively(poseStack, be, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
		}
		if (bone.getName().equals("vial1") && be.getItem(0).isEmpty()) {
			return;
		}

		if (bone.getName().equals("vial2") && be.getItem(1).isEmpty()) {
			return;
		}
		if (bone.getName().equals("vial3") && be.getItem(2).isEmpty()) {
			return;
		}
		if (bone.getName().equals("vial4") && be.getItem(3).isEmpty()) {
			return;
		}
		if (bone.getName().equals("vial5") && be.getItem(4).isEmpty()) {
			return;
		}
		if (bone.getName().equals("vial6") && be.getItem(5).isEmpty()) {
			return;
		}


		super.renderRecursively(poseStack, be, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
	}
}
