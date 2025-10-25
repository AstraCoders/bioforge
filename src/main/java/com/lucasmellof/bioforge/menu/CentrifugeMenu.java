package com.lucasmellof.bioforge.menu;

import com.lucasmellof.bioforge.block.entity.CentrifugeBlockEntity;
import com.lucasmellof.bioforge.block.entity.MicroscopeBlockEntity;
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

/**
 * @author Rok, Pedro Lucas nmm. Created on 19/10/2025
 * @project bioforge
 */
public class CentrifugeMenu extends AbstractContainerMenu {
    @Getter private final CentrifugeBlockEntity blockEntity;
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
    public ItemStack quickMoveStack(Player player, int index) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);

		if (slot != null && slot.hasItem()) {
			ItemStack stackInSlot = slot.getItem();
			stack = stackInSlot.copy();

			int containerSlots = blockEntity != null ? blockEntity.getItemHandler().getSlots() : 6;
			int totalSlots = this.slots.size();
			int playerInvStart = containerSlots;
			int hotbarStart = totalSlots - 9;

			if (index < containerSlots) {
				// From container to player inventory/hotbar
				if (!this.moveItemStackTo(stackInSlot, playerInvStart, totalSlots, true)) {
					return ItemStack.EMPTY;
				}
			} else {
				// From player inventory/hotbar to container slots
				boolean movedToContainer = false;
				if (blockEntity != null) {
					// Try inserting into each handler slot explicitly so items go into the first valid slot
					for (int i = 0; i < containerSlots; i++) {
						// If stack is empty stop
						if (stackInSlot.isEmpty()) break;
						// Try to insert into the handler slot using the underlying ItemStackHandler
						ItemStack before = stackInSlot.copy();
						stackInSlot = blockEntity.getItemHandler().insertItem(i, stackInSlot, false);
						// If insert changed the stack, we moved something
						if (stackInSlot.getCount() != before.getCount()) {
							movedToContainer = true;
						}
					}
					// Update the slot reference after manipulating underlying handler
					if (movedToContainer) {
						// sync slot contents
						// Attempt to reflect changes in the menu's slot instances
						// If the original slot is now empty, set it, otherwise mark changed
						if (stackInSlot.isEmpty()) {
							slot.set(ItemStack.EMPTY);
						} else {
							slot.setChanged();
						}
					} else {
						// Fallback to standard move between player inv/hotbar if nothing fit into container
						if (!this.moveItemStackTo(stackInSlot, 0, containerSlots, false)) {
							if (index >= playerInvStart && index < hotbarStart) {
								if (!this.moveItemStackTo(stackInSlot, hotbarStart, totalSlots, false)) {
									return ItemStack.EMPTY;
								}
							} else if (index >= hotbarStart && index < totalSlots) {
								if (!this.moveItemStackTo(stackInSlot, playerInvStart, hotbarStart, false)) {
									return ItemStack.EMPTY;
								}
							} else {
								return ItemStack.EMPTY;
							}
						}
					}
				} else {
					// No block entity (client-side menu), fall back to default behavior
					if (!this.moveItemStackTo(stackInSlot, 0, containerSlots, false)) {
						if (index >= playerInvStart && index < hotbarStart) {
							if (!this.moveItemStackTo(stackInSlot, hotbarStart, totalSlots, false)) {
								return ItemStack.EMPTY;
							}
						} else if (index >= hotbarStart && index < totalSlots) {
							if (!this.moveItemStackTo(stackInSlot, playerInvStart, hotbarStart, false)) {
								return ItemStack.EMPTY;
							}
						} else {
							return ItemStack.EMPTY;
						}
					}
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
