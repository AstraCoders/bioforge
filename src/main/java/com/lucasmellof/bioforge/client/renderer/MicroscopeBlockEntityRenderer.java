package com.lucasmellof.bioforge.client.renderer;

import com.lucasmellof.bioforge.block.entity.MicroscopeBlockEntity;
import com.lucasmellof.bioforge.client.model.MicroscopeBlockEntityModel;
import com.lucasmellof.bioforge.blood.BloodData;
import com.lucasmellof.bioforge.registry.ModComponentTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

import java.util.List;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 19/10/2025
 */
public class MicroscopeBlockEntityRenderer extends GeoBlockRenderer<MicroscopeBlockEntity> {

	public MicroscopeBlockEntityRenderer() {
        super(new MicroscopeBlockEntityModel());
	}

	@Override
	public void postRender(PoseStack poseStack, MicroscopeBlockEntity be, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {

	}

	@Override
	public void renderRecursively(PoseStack poseStack, MicroscopeBlockEntity be, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {

		if (bone.getName().equals("sample")) {
			ItemStack slot = be.getVial(1);
			if (slot.isEmpty()) {
				slot = be.getVial(0);
			}
			if (slot.isEmpty()) return;
			List<BloodData> bloodDatas = slot.get(ModComponentTypes.BLOOD_DATA);
			if (bloodDatas == null || bloodDatas.isEmpty()) return;
 			super.renderRecursively(poseStack, be, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, bloodDatas.get(0).color());
			return;
		}


		super.renderRecursively(poseStack, be, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
	}

}
