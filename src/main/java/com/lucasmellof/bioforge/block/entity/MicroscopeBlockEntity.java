package com.lucasmellof.bioforge.block.entity;

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
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * @author Rok, Pedro Lucas nmm. Created on 18/10/2025
 * @project bioforge
 */
public class MicroscopeBlockEntity extends SyncableBlockEntity implements MenuProvider, GeoBlockEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;
    private static final int PROCESSING_TIME = 100;

    @Getter
    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (level != null && !level.isClientSide) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return switch (slot) {
                case INPUT_SLOT -> stack.has(ModComponentTypes.BLOOD_DATA);
                case OUTPUT_SLOT -> false;
                default -> false;
            };
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
        super(ModBlockEntities.MICROSCOPE.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.biogene.microscope");
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
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public void syncCompoundTag(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put("inventory", itemHandler.serializeNBT(registries));
        tag.putInt("progress", progress);
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
                processItem(blockEntity, inputStack);
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

    private static void processItem(MicroscopeBlockEntity blockEntity, ItemStack inputStack) {
        if (!hasBloodData(inputStack)) return;

        if (!inputStack.has(ModComponentTypes.BLOOD_DATA)) return;
        ItemStack outputStack = inputStack.copy();
        blockEntity.itemHandler.setStackInSlot(OUTPUT_SLOT, outputStack);
        inputStack.shrink(1);

        // Adiciona informações na lore do item
        /*Component loreComponent = Component.literal("§7═══ Análise do Microscópio ═══")
                .append(Component.literal("\n§cTipo Sanguíneo: §f" + bloodData.bloodType()))
                .append(Component.literal("\n§cDNA: §f" + bloodData.dna().substring(0, Math.min(16, bloodData.dna().length())) + "..."))
                .append(Component.literal("\n§cPureza: §f" + String.format("%.1f%%", bloodData.purity())))
                .append(Component.literal("\n§7═══════════════════════"));

        stack.set(ModDataComponents.LORE, java.util.List.of(loreComponent));*/
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        //        controllers.add(new AnimationController<>(this, state -> {
        //			return NO_VIAL;
        //        }));
    }

    public ItemStack getVial(int slot) {
        return itemHandler.getStackInSlot(slot);
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
