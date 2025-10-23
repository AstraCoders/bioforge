package com.lucasmellof.bioforge.block.entity;

import com.lucasmellof.bioforge.data.BloodData;
import com.lucasmellof.bioforge.items.VialItem;
import com.lucasmellof.bioforge.menu.CentrifugeMenu;
import com.lucasmellof.bioforge.registry.ModBlockEntities;
import com.lucasmellof.bioforge.registry.ModComponentTypes;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.CrafterBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 18/10/2025
 */
public class CentrifugeBlockEntity extends SyncableBlockEntity implements MenuProvider, GeoBlockEntity {
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.idle");
    private static final RawAnimation ROTATING = RawAnimation.begin().thenPlay("animation.rotating");
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

    public CentrifugeBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.CENTRIFUGE.get(), pos, blockState);
    }


    @Override
    public Component getDisplayName() {
        return Component.translatable("container.bioforge.centrifuge");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new CentrifugeMenu(i, inventory, this, this.data);
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
        tag.put("mixing", IntTag.valueOf(mixing));
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }



    private int mixing = -1;

    /*public ItemInteractionResult onInteract(
            ItemStack stack, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (hand == InteractionHand.OFF_HAND) return ItemInteractionResult.FAIL;
        ItemStack handItem = player.getItemInHand(hand);

        // todo: remove me
        if (handItem.getItem() == Items.STICK && mixing == -1) {
            for(int i = 0; i < inventory.getSlots(); i++) {
                var z = inventory.getStackInSlot(i);
                if (!z.isEmpty()) {
					if (!player.addItem(z)) {
						player.drop(z, false);
					}
                }
            }
            return ItemInteractionResult.SUCCESS;
        }


        var availableSlot = findFirstAvailableSlot();
        if (availableSlot != -1 && handItem.getItem() instanceof VialItem) {
            ItemStack toInsert = handItem.split(1);
            inventory.insertItem(availableSlot, toInsert, false);
            return ItemInteractionResult.SUCCESS;
        }


        mixing = 40;

        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }*/

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<CentrifugeBlockEntity> dialogueController =
                new AnimationController<>(this, "controller", 0, this::controller);
        dialogueController.triggerableAnim("idle", IDLE);
        dialogueController.triggerableAnim("rotating", ROTATING);

        controllers.add(dialogueController);
    }

    private <E extends GeoAnimatable> PlayState controller(AnimationState<E> state) {
        // Define qual animação deve rodar
        if (mixing > 0) {
            state.setAndContinue(ROTATING);
        } else {
            state.setAndContinue(IDLE);
        }

        return PlayState.CONTINUE;
    }

    public void dropContents() {
        NonNullList<ItemStack> items = NonNullList.create();
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            items.add(itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(level, worldPosition, items);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CentrifugeBlockEntity blockEntity) {
        if (level.isClientSide) return;

        ItemStack inputStack = blockEntity.itemHandler.getStackInSlot(INPUT_SLOT);

        if (!inputStack.isEmpty() && hasBloodData(inputStack)) {
            blockEntity.progress++;

            if (blockEntity.progress >= blockEntity.maxProgress) {
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


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public void onRemove(BlockState state, Level level, BlockPos pos) {
        if (!level.isClientSide) {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), itemHandler.getStackInSlot(i));
            }
        }
    }

    private void mixVials() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            // just in case
            if (!(stack.getItem() instanceof VialItem)) continue;

            List<BloodData> data = VialItem.getBloodData(stack);
            if (data == null) continue;

            // we don't mix if there's only one type of data
            if (data.size() == 1) continue;

            var blood = data.getFirst();
            for (BloodData datum : data) {
                blood = blood.mix(datum);
            }

            VialItem.setBloodData(stack, List.of(blood));
        }
    }

    @Override
    public void onDataPacket(
            Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        super.onDataPacket(net, pkt, lookupProvider);
        CompoundTag tag = pkt.getTag();
        mixing = tag.getInt("Mixing");
    }

    public int getItemCount() {
        int count = 0;
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                count++;
            }
        }
        return count;
    }

    public ItemStack getItem(int slot) {
        if (slot < 0 || slot >= itemHandler.getSlots()) {
            return ItemStack.EMPTY;
        }
        return itemHandler.getStackInSlot(slot);
    }
}
