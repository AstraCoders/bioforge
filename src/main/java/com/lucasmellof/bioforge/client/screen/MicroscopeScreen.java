package com.lucasmellof.bioforge.client.screen;

import com.lucasmellof.bioforge.Bioforge;
import com.lucasmellof.bioforge.menu.MicroscopeMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * @author Rok, Pedro Lucas nmm. Created on 19/10/2025
 * @project bioforge
 */
public class MicroscopeScreen extends AbstractContainerScreen<MicroscopeMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Bioforge.MODID, "textures/gui/microscope_gui.png");

    public MicroscopeScreen(MicroscopeMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 166;
        this.imageWidth = 176;
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
            int arrowWidth = 24;
            int progressWidth = (int) ((progress / (float) maxProgress) * arrowWidth);
            guiGraphics.blit(TEXTURE, x + 76, y + 55, 176, 0, progressWidth, 16);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}