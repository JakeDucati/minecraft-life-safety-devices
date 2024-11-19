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

import com.lifesafetydevices.ModSounds;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import java.util.HashMap;
import java.util.Map;

public class FireBell extends HorizontalFacingBlock implements Waterloggable {

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty ACTIVATED = BooleanProperty.of("activated");
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    // Voxel shapes for different orientations
    private static final VoxelShape SHAPE_NORTH = Block.createCuboidShape(3, 2, 13, 13, 14, 16);
    private static final VoxelShape SHAPE_EAST = Block.createCuboidShape(0, 2, 3, 3, 14, 13);
    private static final VoxelShape SHAPE_SOUTH = Block.createCuboidShape(3, 2, 0, 13, 14, 3);
    private static final VoxelShape SHAPE_WEST = Block.createCuboidShape(13, 2, 3, 16, 14, 13);

    private static final int LOOP_INTERVAL_TICKS = 40; // 2 seconds at 20 ticks per second

    // Map to track which blocks are active and when they last played the sound
    private static final Map<BlockPos, Long> activeBellMap = new HashMap<>();

    public FireBell(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(ACTIVATED, false)
                .with(WATERLOGGED, false));

        // Register the server tick event listener
        ServerTickEvents.END_WORLD_TICK.register(this::onServerTick);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, ACTIVATED, WATERLOGGED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(FACING, ctx.getPlayerFacing().getOpposite())
                .with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).isOf(Fluids.WATER));
    }

    @SuppressWarnings("deprecation")
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

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

    @SuppressWarnings("deprecation")
    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean notify) {
        super.neighborUpdate(state, world, pos, neighborBlock, neighborPos, notify);

        if (world.isClient) {
            return; // Only handle sound on the server side
        }

        boolean powered = world.isReceivingRedstonePower(pos);

        if (powered && !state.get(ACTIVATED)) {
            // If powered and not activated, activate the bell and start the sound loop
            world.setBlockState(pos, state.with(ACTIVATED, true));
            activeBellMap.put(pos, world.getTime()); // Store the current server time when the bell is activated
        } else if (!powered && state.get(ACTIVATED)) {
            // If not powered and previously activated, deactivate and stop sound
            world.setBlockState(pos, state.with(ACTIVATED, false));
            activeBellMap.remove(pos); // Remove the bell from active map when it's deactivated
        }
    }

    // This method will be called every server tick
    private void onServerTick(World world) {
        if (world.isClient()) {
            return;
        }

        long currentTime = world.getTime(); // Get the current server time

        // Iterate over all active bells and check if it's time to play the sound
        for (Map.Entry<BlockPos, Long> entry : activeBellMap.entrySet()) {
            BlockPos pos = entry.getKey();
            long lastPlayedTime = entry.getValue();

            // If the current time exceeds the last played time by the LOOP_INTERVAL_TICKS (2 seconds)
            if (currentTime - lastPlayedTime >= LOOP_INTERVAL_TICKS) {
                BlockState state = world.getBlockState(pos);
                if (state.get(ACTIVATED)) {
                    world.playSound(null, pos, ModSounds.FIRE_BELL_RING, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    activeBellMap.put(pos, currentTime);
                }
            }
        }
    }
}
