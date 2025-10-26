package com.lucasmellof.bioforge.menu;

import com.lucasmellof.bioforge.block.entity.CentrifugeBlockEntity;
import com.lucasmellof.bioforge.registry.ModMenuTypes;
import lombok.Getter;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

/**
 * @author Rok, Pedro Lucas nmm. Created on 19/10/2025
 * @project bioforge
 */
public class CentrifugeMenu extends AbstractContainerMenu {
    @Getter
    private final CentrifugeBlockEntity blockEntity;
    private final ContainerData data;

    public CentrifugeMenu(int containerId, Inventory playerInventory, CentrifugeBlockEntity blockEntity, ContainerData data) {
        super(ModMenuTypes.CENTRIFUGE_MENU.get(), containerId);
        this.blockEntity = blockEntity;
        this.data = data;

        addDataSlots(data);

        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 0, 92, 12));

        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 1, 104, 35));

        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 2, 92, 59));

        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 3, 68, 59));

        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 4, 56, 35));

        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 5, 68, 12));


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

    public CentrifugeMenu(int containerId, Inventory playerInventory) {
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
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();


            if (index < 6) {
                if (!this.moveItemStackTo(slotStack, 6, 42, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                boolean movedItem = false;
                for (int i = 0; i < 6; i++) {
                    Slot centrifugeSlot = this.slots.get(i);

                    if (!centrifugeSlot.hasItem()) {
                        ItemStack singleItem = slotStack.copy();
                        singleItem.setCount(1);
                        centrifugeSlot.set(singleItem);
                        slotStack.shrink(1);
                        movedItem = true;
                        break;
                    } else if (centrifugeSlot.getItem().getCount() < 1 &&
                               ItemStack.isSameItemSameComponents(centrifugeSlot.getItem(), slotStack)) {
                        centrifugeSlot.getItem().grow(1);
                        slotStack.shrink(1);
                        movedItem = true;
                        break;
                    }
                }
                if (!movedItem) {
                    return ItemStack.EMPTY;
                }
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
        }

        return stack;
    }

    @Override
    public boolean stillValid(Player player) {
        if (blockEntity == null) return false;
        return player.distanceToSqr(blockEntity.getBlockPos().getCenter()) <= 64.0;
    }
}
