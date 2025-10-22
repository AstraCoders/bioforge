package com.lucasmellof.bioforge.block.entity;

import com.lucasmellof.bioforge.items.SyringeItem;
import com.lucasmellof.bioforge.items.VialItem;
import com.lucasmellof.bioforge.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.items.ItemStackHandler;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 18/10/2025
 */
public class VialHolderBlockEntity extends SyncableBlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final ItemStackHandler inventory;

    public VialHolderBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.VIAL_HOLDER.get(), pos, blockState);

        inventory = new ItemStackHandler(1) {
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
    }

    public ItemInteractionResult onInteract(
            ItemStack stack, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (hand == InteractionHand.OFF_HAND) return ItemInteractionResult.FAIL;
        ItemStack handItem = player.getItemInHand(hand);
        boolean hasVial = hasVial();
        if (hasVial && handItem.isEmpty()) {
            var vial = getItem();
            if (!player.addItem(vial)) {
                player.drop(vial, false);
            }
            return ItemInteractionResult.SUCCESS;
        }
        if (!hasVial && handItem.getItem() instanceof VialItem) {
            ItemStack toInsert = handItem.split(1);
            inventory.insertItem(0, toInsert, false);
            return ItemInteractionResult.SUCCESS;
        }
        if (hasVial && handItem.getItem() instanceof VialItem) {
            var vial = getItem();
            if (!player.addItem(vial)) {
                player.drop(vial, false);
            }
            ItemStack toInsert = handItem.split(1);
            inventory.insertItem(0, toInsert, false);
            return ItemInteractionResult.SUCCESS;
        }

        if (handItem.isEmpty() || !hasVial) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (!(handItem.getItem() instanceof SyringeItem)) {
            return ItemInteractionResult.FAIL;
        }

        var vial = getItem();
        if (VialItem.isFull(vial)) return ItemInteractionResult.FAIL;
        var s = handItem.split(1);
        var blood = SyringeItem.getBloodData(s);
        if (blood != null && VialItem.addBlood(player, vial, blood)) {
            SyringeItem.setBloodData(s, null);
            if (player.addItem(s)) {
                player.drop(s, false);
            }
            sync();
        }

        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        //        controllers.add(new AnimationController<>(this, state -> {
        //			return NO_VIAL;
        //        }));
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public boolean hasVial() {
        return !inventory.getStackInSlot(0).isEmpty();
    }

    public ItemStack getItem() {
        return inventory.getStackInSlot(0);
    }

    public void onRemove(BlockState state, Level level, BlockPos pos) {
        if (!level.isClientSide) {
            for (int i = 0; i < inventory.getSlots(); i++) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), inventory.getStackInSlot(i));
            }
        }
    }
}
