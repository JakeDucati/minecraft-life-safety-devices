package com.lifesafetydevices.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class FireBell extends HorizontalFacingBlock implements Waterloggable {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty ACTIVATED = BooleanProperty.of("activated");
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    // Voxel shapes for different orientations
    private static final VoxelShape SHAPE_NORTH = Block.createCuboidShape(3, 2, 13, 13, 14, 16);
    private static final VoxelShape SHAPE_EAST = Block.createCuboidShape(0, 2, 3, 3, 14, 13);
    private static final VoxelShape SHAPE_SOUTH = Block.createCuboidShape(3, 2, 0, 13, 14, 3);
    private static final VoxelShape SHAPE_WEST = Block.createCuboidShape(13, 2, 3, 16, 14, 13);

    private static final long LOOP_INTERVAL = 20; // The interval for looping in ticks (20 ticks = 1 second)
    private long lastLoopTime = 0; // Tracks the last time the sound was played

    public FireBell(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(ACTIVATED, false)
                .with(Properties.HORIZONTAL_FACING, Direction.NORTH)
                .with(WATERLOGGED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, ACTIVATED, WATERLOGGED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(FACING, ctx.getPlayerFacing().getOpposite())
                // waterlog
                .with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite())
                .with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).isOf(Fluids.WATER));
    }

    // waterlog
    @SuppressWarnings("deprecation")
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    // waterlog
    @SuppressWarnings("deprecation")
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
            WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
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

    // redstone
    @SuppressWarnings("deprecation")
    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos,
            boolean notify) {
        super.neighborUpdate(state, world, pos, neighborBlock, neighborPos, notify);

        if (world.isClient) {
            return; // Do not play sound on the client side
        }

        // Get the current redstone power at the position
        int power = world.getReceivedRedstonePower(pos);

        // If powered and not activated, play sound and set activated
        if (power > 0 && !state.get(ACTIVATED)) {
            world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_HARP, SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.setBlockState(pos, state.with(ACTIVATED, true)); // Set block to activated
        }
        // If power is off and was previously activated, reset the state
        else if (power == 0 && state.get(ACTIVATED)) {
            world.setBlockState(pos, state.with(ACTIVATED, false)); // Reset block to unpowered
        }

        // If the block is activated, loop the sound based on interval
        if (state.get(ACTIVATED)) {
            long currentTime = world.getTime();

            // Play sound at intervals
            if (currentTime % LOOP_INTERVAL == 0 && currentTime > lastLoopTime) {
                world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_HARP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                lastLoopTime = currentTime; // Update the last played time
            }
        }
    }
}
