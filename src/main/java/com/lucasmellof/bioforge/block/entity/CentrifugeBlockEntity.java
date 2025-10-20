package com.lucasmellof.bioforge.block.entity;

import com.lucasmellof.bioforge.data.BloodData;
import com.lucasmellof.bioforge.items.VialItem;
import com.lucasmellof.bioforge.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.CrafterBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.items.ItemStackHandler;
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
public class CentrifugeBlockEntity extends SyncableBlockEntity implements GeoBlockEntity {
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.idle");
    private static final RawAnimation ROTATING = RawAnimation.begin().thenPlay("animation.rotating");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final ItemStackHandler inventory;

    public CentrifugeBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.CENTRIFUGE.get(), pos, blockState);

        inventory = new ItemStackHandler(6) {
            @Override
            protected int getStackLimit(int slot, ItemStack stack) {
                return 1;
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return stack.getItem() instanceof VialItem;
            }

            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                sync();
                updateBlockState();
            }
        };
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Inventory", inventory.serializeNBT(registries));
    }

    @Override
    public void syncCompoundTag(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put("Inventory", inventory.serializeNBT(registries));
        tag.put("Mixing", IntTag.valueOf(mixing));
    }

    private int mixing = -1;

    public ItemInteractionResult onInteract(
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
    }

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

    public static void serverTick(Level level, BlockPos pos, BlockState state, CentrifugeBlockEntity be) {
        if (be.mixing <= -1) return;
        be.mixing--;
        be.mixVials();
        be.sync();
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public int findFirstAvailableSlot() {
        for (int i = 0; i < inventory.getSlots(); i++) {
            if (inventory.getStackInSlot(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    public void onRemove(BlockState state, Level level, BlockPos pos) {
        if (!level.isClientSide) {
            for (int i = 0; i < inventory.getSlots(); i++) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), inventory.getStackInSlot(i));
            }
        }
    }

    private void mixVials() {
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
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
        for (int i = 0; i < inventory.getSlots(); i++) {
            if (!inventory.getStackInSlot(i).isEmpty()) {
                count++;
            }
        }
        return count;
    }

    public ItemStack getItem(int slot) {
        if (slot < 0 || slot >= inventory.getSlots()) {
            return ItemStack.EMPTY;
        }
        return inventory.getStackInSlot(slot);
    }
}
