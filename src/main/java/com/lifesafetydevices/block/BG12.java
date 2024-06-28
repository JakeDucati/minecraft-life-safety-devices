package com.lifesafetydevices.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class BG12 extends HorizontalFacingBlock {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty ACTIVATED = BooleanProperty.of("activated");

    // Voxel shapes for different orientations
    private static final VoxelShape SHAPE_NORTH = VoxelShapes.union(
        Block.createCuboidShape(0, 0, 0, 16, 3, 16),
        Block.createCuboidShape(1, 3, 1, 15, 4, 15),
        Block.createCuboidShape(2, 4, 2, 14, 5, 14),
        Block.createCuboidShape(3, 5, 3, 13, 16, 13)
    );

    private static final VoxelShape SHAPE_EAST = VoxelShapes.union(
        Block.createCuboidShape(0, 0, 0, 16, 3, 16),
        Block.createCuboidShape(1, 3, 1, 15, 4, 15),
        Block.createCuboidShape(2, 4, 2, 14, 5, 14),
        Block.createCuboidShape(3, 5, 3, 13, 16, 13)
    );

    private static final VoxelShape SHAPE_SOUTH = VoxelShapes.union(
        Block.createCuboidShape(0, 0, 0, 16, 3, 16),
        Block.createCuboidShape(1, 3, 1, 15, 4, 15),
        Block.createCuboidShape(2, 4, 2, 14, 5, 14),
        Block.createCuboidShape(3, 5, 3, 13, 16, 13)
    );

    private static final VoxelShape SHAPE_WEST = VoxelShapes.union(
        Block.createCuboidShape(0, 0, 0, 16, 3, 16),
        Block.createCuboidShape(1, 3, 1, 15, 4, 15),
        Block.createCuboidShape(2, 4, 2, 14, 5, 14),
        Block.createCuboidShape(3, 5, 3, 13, 16, 13)
    );

    public BG12(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(ACTIVATED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, ACTIVATED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case EAST -> SHAPE_EAST;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            default -> SHAPE_NORTH;
        };
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getOutlineShape(state, world, pos, context);
    }
}
