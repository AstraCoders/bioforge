package com.lucasmellof.bioforge.client.screen;

import com.lucasmellof.bioforge.BioGeneMod;
import com.lucasmellof.bioforge.Const;
import com.lucasmellof.bioforge.menu.CentrifugeMenu;
import com.lucasmellof.bioforge.menu.MicroscopeMenu;
import com.lucasmellof.bioforge.network.C2SStartCentrifugePacket;
import com.lucasmellof.bioforge.registry.ModItems;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

/**
 * @author Rok, Pedro Lucas nmm. Created on 19/10/2025
 * @project bioforge
 */
public class CentrifugeScreen extends AbstractContainerScreen<CentrifugeMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(BioGeneMod.MODID, "textures/gui/centrifuge_gui.png");
    private static final ResourceLocation TEXTURE_ARROW_1 = Const.of("textures/gui/centrifuge_progress_1.png");
    private static final ResourceLocation TEXTURE_ARROW_2 = Const.of("textures/gui/centrifuge_progress_2.png");


    private Button startButton;

    public CentrifugeScreen(CentrifugeMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 166;
        this.imageWidth = 176;
    }

    @Override
    protected void init() {
        super.init();

        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;

        // Botão "Iniciar"
        this.startButton = this.addRenderableWidget(
                Button.builder(Component.literal("Start"), button -> {
                            if (!menu.isCrafting()) {
                                Minecraft.getInstance().player.connection.send(
                                        new C2SStartCentrifugePacket(menu.getBlockEntity().getBlockPos()));
                            }
                        })
                        .bounds(relX + 119, relY + 57, 50, 20) // posição e tamanho
                        .build()
        );
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

            int arrowWidth = 22;
            int arrowHeight = 21;

            float normalized = progress / (float) maxProgress;
            int width1 = (int) (Math.min(normalized * 2f, 1f) * arrowWidth);
            if (width1 > 0) {
                guiGraphics.blit(TEXTURE_ARROW_1, x + 77, y + 33, 0, 0, width1, arrowHeight, arrowWidth, arrowHeight);
            }

            if (normalized > 0.45f) {
                float secondPart = (normalized - 0.45f) * 2f;
                int width2 = (int) (secondPart * arrowWidth);
                if (width2 > 0) {
                    int drawX = x + 77 + (arrowWidth - width2);
                    int u = arrowWidth - width2;
                    guiGraphics.blit(TEXTURE_ARROW_2, drawX, y + 33, u, 0, width2, arrowHeight, arrowWidth, arrowHeight);
                }
            }
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public Component getTitle() {
        return Component.empty();
    }
}
