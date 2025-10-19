package com.lucasmellof.bioforge.client.screen;

import com.lucasmellof.bioforge.Bioforge;
import com.lucasmellof.bioforge.Const;
import com.lucasmellof.bioforge.menu.MicroscopeMenu;
import com.lucasmellof.bioforge.registry.ModItems;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.BrewingStandScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * @author Rok, Pedro Lucas nmm. Created on 19/10/2025
 * @project bioforge
 */
public class MicroscopeScreen extends AbstractContainerScreen<MicroscopeMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Bioforge.MODID, "textures/gui/microscope_gui.png");
    private static final ResourceLocation TEXTURE_ARROW = Const.of("textures/gui/microscope_progress.png");

    private static final ItemStack DEFAULT_ITEM = new ItemStack(ModItems.MICROCOPE_ITEM);

    public MicroscopeScreen(MicroscopeMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 166;
        this.imageWidth = 176;
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        //super.renderLabels(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;


        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressArrow(guiGraphics, x, y);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isCrafting()) {
            int progress = menu.getProgress();
            int maxProgress = menu.getMaxProgress();
            int arrowHeigh = 57;
            int progressHeight = (int) ((progress / (float) maxProgress) * arrowHeigh);
            guiGraphics.blit(TEXTURE_ARROW, x + 52, y +14, 0, 0, 9, progressHeight, 9, 57);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        renderTooltip(guiGraphics, mouseX, mouseY);
        render3DItem(guiGraphics, partialTick);
    }

    private void render3DItem(GuiGraphics guiGraphics, float partialTick) {
        ItemStack itemToRender = menu.getSlot(0).getItem();

        if (itemToRender.isEmpty()) {
            ItemStack result = menu.getSlot(1).getItem();
            if (result.isEmpty()) {
                itemToRender = DEFAULT_ITEM;
            } else {
                itemToRender = result;
            }
        }

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        int centerX = x + imageWidth / 2 + 4;
        int centerY = y + imageHeight / 2 + 92;

        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();

        poseStack.translate(centerX, centerY+20, -200);

        float scale = 3.0f;
        poseStack.scale(scale, scale, scale);

        poseStack.mulPose(Axis.YP.rotationDegrees(2));
        poseStack.mulPose(Axis.XP.rotationDegrees(20 ));

        BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(itemToRender, null, null, 0);
        guiGraphics.renderItem(itemToRender, -8, -8);

        // Restaura o estado da matriz
        poseStack.popPose();

        // Restaura estado do render
        RenderSystem.applyModelViewMatrix();
    }

    @Override
    public Component getTitle() {
        return Component.empty();
    }
}