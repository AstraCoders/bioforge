package com.lucasmellof.bioforge.block;

import com.lucasmellof.bioforge.block.entity.CentrifugeBlockEntity;
import com.lucasmellof.bioforge.registry.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
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
import org.jetbrains.annotations.Nullable;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 18/10/2025
 */
public class CentrifugeBlock extends HorizontalDirectionalBlock implements EntityBlock {

	public static VoxelShape SHAPE = makeShape();
	public static VoxelShape makeShape(){
		VoxelShape shape = Shapes.empty();
		shape = Shapes.join(shape, Shapes.box(0.12343749999999998, 0, 0.21875, 0.8765625, 0.03125, 0.96875), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.125, 0, 0.1875, 0.875, 0.5, 0.3125), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.125, 0.000175, 0.006716249999999979, 0.875, 0.28454999999999997, 0.18796625), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.125, 0.26354, 0.033191250000000005, 0.875, 0.54479, 0.18944125), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.22499999999999998, 0.30104, 0.023816249999999983, 0.775, 0.50729, 0.04256624999999997), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.21875, 0.29479, 0.017566250000000005, 0.78125, 0.51354, 0.048816250000000005), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.125, 0.03125, 0.875, 0.875, 0.5, 1), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.25, 0.03125, 0.84375, 0.75, 0.46875, 0.875), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.25, 0.03125, 0.3125, 0.75, 0.46875, 0.34375), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.09375, 0.03125, 0.21875, 0.21875, 0.500625, 0.96875), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.78125, 0.03125, 0.21875, 0.90625, 0.500625, 0.96875), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.75, 0.03125, 0.3125, 0.78125, 0.46875, 0.875), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.21875, 0.03125, 0.3125, 0.25, 0.46875, 0.875), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.8125, 0.5, 0.21875, 0.875, 0.59375, 0.96875), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.1875, 0.5, 0.21875, 0.8125, 0.59375, 0.28125), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.1875, 0.5, 0.90625, 0.8125, 0.59375, 0.96875), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.1875, 0.515625, 0.28125, 0.8125, 0.578125, 0.90625), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.125, 0.5, 0.21875, 0.1875, 0.59375, 0.96875), BooleanOp.OR);
		return shape;
	}

	public static final MapCodec<CentrifugeBlock> CODEC = simpleCodec(properties1 -> new CentrifugeBlock());
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public CentrifugeBlock() {
        super(Properties.ofFullCopy(Blocks.GLASS));
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
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return level.isClientSide ? null : createTickerHelper(blockEntityType, ModBlockEntities.CENTRIFUGE.get(), CentrifugeBlockEntity::serverTick);
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
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (level.getBlockEntity(pos) instanceof CentrifugeBlockEntity vialHolder) {
			return vialHolder.onInteract(stack, player, hand, hitResult);
		}
		return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
	}

	@Override
	protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
		if (level.getBlockEntity(pos) instanceof CentrifugeBlockEntity vialHolder) {
			vialHolder.onRemove(state, level, pos);
		}
		super.onRemove(state, level, pos, newState, movedByPiston);
	}
}
