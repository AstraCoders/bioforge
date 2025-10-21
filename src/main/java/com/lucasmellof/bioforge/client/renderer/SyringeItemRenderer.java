package com.lucasmellof.bioforge.client.renderer;

import com.lucasmellof.bioforge.client.model.SyringeItemModel;
import com.lucasmellof.bioforge.data.BloodData;
import com.lucasmellof.bioforge.items.SyringeItem;
import com.lucasmellof.bioforge.registry.ModComponentTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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

import java.util.List;

/**
 * @author Rok, Pedro Lucas nmm. Created on 18/10/2025
 * @project bioforge
 */
public class SyringeItemRenderer extends GeoItemRenderer<SyringeItem> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SyringeItemRenderer.class);

	public SyringeItemRenderer() {
		super(new SyringeItemModel());
	}

	@Override
	public void renderByItem(ItemStack stack, ItemDisplayContext displayContext,
							 PoseStack poseStack, MultiBufferSource bufferSource,
							 int packedLight, int packedOverlay) {
		this.currentItemStack = stack;

		super.renderByItem(stack, displayContext, poseStack, bufferSource,
				packedLight, packedOverlay);
	}

	@Override
	public long getInstanceId(SyringeItem animatable) {
		if (this.currentItemStack != null) {
			return this.currentItemStack.hashCode();
		}
		return super.getInstanceId(animatable);
	}

	@Override
	public void actuallyRender(PoseStack poseStack, SyringeItem animatable,
							   BakedGeoModel model, RenderType renderType,
							   MultiBufferSource bufferSource, VertexConsumer buffer,
							   boolean isReRender, float partialTick,
							   int packedLight, int packedOverlay, int colour) {

		try {
			super.actuallyRender(poseStack, animatable, model, renderType, bufferSource,
					buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
		} catch (Exception e) {
			LOGGER.error("Erro ao renderizar seringa: ", e);
		}
	}

	@Override
	public void renderRecursively(PoseStack poseStack, SyringeItem animatable,
								  GeoBone bone, RenderType renderType,
								  MultiBufferSource bufferSource, VertexConsumer buffer,
								  boolean isReRender, float partialTick,
								  int packedLight, int packedOverlay,
								  int renderColor) {

		ItemStack stack = this.currentItemStack;
		List<BloodData> bloodDatas = stack != null ? stack.get(ModComponentTypes.BLOOD_DATA) : null;

		String boneName = bone.getName();

		if (bloodDatas != null && !bloodDatas.isEmpty()	) {
			var bloodData = bloodDatas.getFirst();
			if (boneName.equalsIgnoreCase("liquid1") ||
				boneName.equalsIgnoreCase("liquid2")) {

				int colorWithBlood = bloodData.color();

				super.renderRecursively(poseStack, animatable, bone, renderType,
						bufferSource, buffer, isReRender, partialTick,
						packedLight, packedOverlay, colorWithBlood);
				return;
			}
		}

		super.renderRecursively(poseStack, animatable, bone, renderType,
				bufferSource, buffer, isReRender, partialTick,
				packedLight, packedOverlay, renderColor);
	}

	@Override
	public RenderType getRenderType(SyringeItem animatable, ResourceLocation texture,
									MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(texture);
	}
}
