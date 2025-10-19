package com.lucasmellof.bioforge.block;

import com.lucasmellof.bioforge.block.entity.VialHolderBlockEntity;
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
public class VialHolderBlock extends HorizontalDirectionalBlock implements EntityBlock {

	public VoxelShape makeShape(){
		VoxelShape shape = Shapes.empty();
		shape = Shapes.join(shape, Shapes.box(0.3125, 0.25, 0.3125, 0.6875, 0.25, 0.6875), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.25, 0, 0.5, 0.75, 0.4375, 0.5), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.375, 0, 0.4375, 0.625, 0.0625, 0.5625), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.3125, -0.008928749999999985, 0.4375, 0.6875, 0.36607125000000007, 0.5625), BooleanOp.OR);
		return shape;
	}

	public static final MapCodec<VialHolderBlock> CODEC = simpleCodec(properties1 -> new VialHolderBlock());
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public VialHolderBlock() {
        super(Properties.ofFullCopy(Blocks.GLASS));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getClockWise());
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
		return new VialHolderBlockEntity(pos, state);
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return EntityBlock.super.getTicker(level, state, blockEntityType);
	}

	@Override
	public @Nullable <T extends BlockEntity> GameEventListener getListener(ServerLevel level, T blockEntity) {
		return EntityBlock.super.getListener(level, blockEntity);
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
		return makeShape();
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (level.getBlockEntity(pos) instanceof VialHolderBlockEntity vialHolder) {
			return vialHolder.onInteract(stack, player, hand, hitResult);
		}
		return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
	}
}
