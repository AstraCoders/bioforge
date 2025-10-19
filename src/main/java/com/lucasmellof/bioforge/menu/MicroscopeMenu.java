package com.lucasmellof.bioforge.menu;

import com.lucasmellof.bioforge.block.entity.MicroscopeBlockEntity;
import com.lucasmellof.bioforge.registry.ModMenuTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;

/**
 * @author Rok, Pedro Lucas nmm. Created on 19/10/2025
 * @project bioforge
 */
public class MicroscopeMenu extends AbstractContainerMenu {
    private final MicroscopeBlockEntity blockEntity;
    private final ContainerData data;

    public MicroscopeMenu(int containerId, Inventory playerInventory, MicroscopeBlockEntity blockEntity, ContainerData data) {
        super(ModMenuTypes.MICROSCOPE_MENU.get(), containerId);
        this.blockEntity = blockEntity;
        this.data = data;

        addDataSlots(data);

        // Slot de input do microscópio (centro da GUI)
        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 0, 80, 35));

        // Inventário do jogador
        int playerInvY = 84;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, playerInvY + row * 18));
            }
        }

        // Hotbar do jogador
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, playerInvY + 58));
        }
    }

    public MicroscopeMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, null, new SimpleContainerData(2));
    }

    public int getProgress() {
        return data.get(0);
    }

    public int getMaxProgress() {
        return data.get(1);
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            stack = stackInSlot.copy();

            // Se o item está no slot do microscópio (índice 0)
            if (index == 0) {
                if (!this.moveItemStackTo(stackInSlot, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
            }
            // Se o item está no inventário do jogador
            else {
                // Tenta mover para o slot do microscópio
                if (blockEntity != null && blockEntity.getItemHandler().isItemValid(0, stackInSlot)) {
                    if (!this.moveItemStackTo(stackInSlot, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                // Move entre hotbar e inventário
                else if (index < 28) {
                    if (!this.moveItemStackTo(stackInSlot, 28, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(stackInSlot, 1, 28, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stackInSlot.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stackInSlot);
        }

        return stack;
    }

    @Override
    public boolean stillValid(Player player) {
        if (blockEntity == null) return false;
        return player.distanceToSqr(blockEntity.getBlockPos().getCenter()) <= 64.0;
    }
}