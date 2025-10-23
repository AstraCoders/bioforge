package com.lucasmellof.bioforge.block;

import com.lucasmellof.bioforge.block.entity.CentrifugeBlockEntity;
import com.lucasmellof.bioforge.block.entity.MicroscopeBlockEntity;
import com.lucasmellof.bioforge.registry.ModBlockEntities;
import com.lucasmellof.bioforge.registry.ModItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CrafterBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 18/10/2025
 */
public class CentrifugeBlock extends HorizontalDirectionalBlock implements EntityBlock {

	public static final VoxelShape FACING_NORTH_SOUTH = Block.box(2d, 0d, 0d, 14d, 9.5d, 16d);
	public static final VoxelShape FACING_EAST_WEST = Block.box(0d, 0d, 2D, 16d, 9.5d, 14d);

	public static final MapCodec<CentrifugeBlock> CODEC = simpleCodec(properties1 -> new CentrifugeBlock());
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public CentrifugeBlock() {
        super(Properties.ofFullCopy(Blocks.IRON_BLOCK));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CentrifugeBlockEntity(pos, state);
	}

	@javax.annotation.Nullable
	protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
			BlockEntityType<A> serverType, BlockEntityType<E> clientType, BlockEntityTicker<? super E> ticker
	) {
		return clientType == serverType ? (BlockEntityTicker<A>)ticker : null;
	}

	@Override
	protected BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}


	@Override
	public BlockState mirror(BlockState p_323894_, Mirror p_324242_) {
		return p_323894_.rotate(p_324242_.getRotation(p_323894_.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}


	@Override
	protected @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		if (state.getValue(FACING) == Direction.NORTH || state.getValue(FACING) == Direction.SOUTH) {
			return FACING_NORTH_SOUTH;
		}
		return FACING_EAST_WEST;
	}


	@Override
	protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (!level.isClientSide) {
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity instanceof CentrifugeBlockEntity microscopeBlockEntity) {
				player.openMenu(microscopeBlockEntity, pos);
			}
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}


	/*@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (level.getBlockEntity(pos) instanceof CentrifugeBlockEntity vialHolder) {
			return vialHolder.onInteract(stack, player, hand, hitResult);
		}
		return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
	}*/

	@Override
	protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
		if (level.getBlockEntity(pos) instanceof CentrifugeBlockEntity vialHolder) {
			vialHolder.onRemove(state, level, pos);
		}
		super.onRemove(state, level, pos, newState, movedByPiston);
	}

	@Override
	public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
		return ModItems.CENTRIFUGE_ITEM.get().getDefaultInstance();
	}


	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		if (level.isClientSide) {
			return null;
		}
		return type == ModBlockEntities.CENTRIFUGE.get()
				? (lvl, pos, st, blockEntity) -> CentrifugeBlockEntity.tick(lvl, pos, st, (CentrifugeBlockEntity) blockEntity)
				: null;
	}
}
