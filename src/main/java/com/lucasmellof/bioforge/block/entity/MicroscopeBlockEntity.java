package com.lucasmellof.bioforge.block.entity;

import com.lucasmellof.bioforge.data.BloodData;
import com.lucasmellof.bioforge.menu.MicroscopeMenu;
import com.lucasmellof.bioforge.registry.ModBlockEntities;
import com.lucasmellof.bioforge.registry.ModComponentTypes;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

/**
 * @author Rok, Pedro Lucas nmm. Created on 18/10/2025
 * @project bioforge
 */
public class MicroscopeBlockEntity extends BlockEntity implements MenuProvider {
    private static final int INPUT_SLOT = 0;
    private static final int PROCESSING_TIME = 100; // 5 segundos

    @Getter
    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (level != null && !level.isClientSide) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            // Aqui você pode verificar se o item tem BloodData
            return stack.has(ModComponentTypes.BLOOD_DATA);
        }
    };

    public final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> progress;
                case 1 -> maxProgress;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> progress = value;
                case 1 -> maxProgress = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    private int progress = 0;
    private int maxProgress = PROCESSING_TIME;

    public MicroscopeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MICROSCOPE_BE.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.yourmod.microscope");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new MicroscopeMenu(containerId, playerInventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", itemHandler.serializeNBT(registries));
        tag.putInt("progress", progress);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        itemHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        progress = tag.getInt("progress");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void dropContents() {
        NonNullList<ItemStack> items = NonNullList.create();
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            items.add(itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(level, worldPosition, items);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, MicroscopeBlockEntity blockEntity) {
        if (level.isClientSide) return;

        ItemStack inputStack = blockEntity.itemHandler.getStackInSlot(INPUT_SLOT);

        if (!inputStack.isEmpty() && hasBloodData(inputStack)) {
            blockEntity.progress++;

            if (blockEntity.progress >= blockEntity.maxProgress) {
                processItem(inputStack);
                blockEntity.progress = 0;
            }
        } else {
            blockEntity.progress = 0;
        }

        setChanged(level, pos, state);
    }

    private static boolean hasBloodData(ItemStack stack) {
        return stack.has(ModComponentTypes.BLOOD_DATA);
    }

    private static void processItem(ItemStack stack) {
        if (!hasBloodData(stack)) return;

        BloodData bloodData = stack.get(ModComponentTypes.BLOOD_DATA);
        if (bloodData == null) return;

        // Adiciona informações na lore do item
        /*Component loreComponent = Component.literal("§7═══ Análise do Microscópio ═══")
                .append(Component.literal("\n§cTipo Sanguíneo: §f" + bloodData.bloodType()))
                .append(Component.literal("\n§cDNA: §f" + bloodData.dna().substring(0, Math.min(16, bloodData.dna().length())) + "..."))
                .append(Component.literal("\n§cPureza: §f" + String.format("%.1f%%", bloodData.purity())))
                .append(Component.literal("\n§7═══════════════════════"));

        // Adiciona à lore existente ou cria nova
        stack.set(ModDataComponents.LORE, java.util.List.of(loreComponent));*/
    }
}