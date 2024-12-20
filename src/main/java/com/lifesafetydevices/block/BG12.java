package com.lifesafetydevices.block;

import com.lifesafetydevices.ModSounds;
import com.lifesafetydevices.item.KeyItem;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.state.property.Properties;

public class BG12 extends HorizontalFacingBlock implements Waterloggable {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty ACTIVATED = BooleanProperty.of("activated");
    public static final BooleanProperty PULSE = BooleanProperty.of("pulse");
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    private long lastResetTime = 0;

    // Voxel shapes for different orientations
    private static final VoxelShape SHAPE_NORTH = Block.createCuboidShape(3, 2, 13, 13, 14, 16);
    private static final VoxelShape SHAPE_EAST = Block.createCuboidShape(0, 2, 3, 3, 14, 13);
    private static final VoxelShape SHAPE_SOUTH = Block.createCuboidShape(3, 2, 0, 13, 14, 3);
    private static final VoxelShape SHAPE_WEST = Block.createCuboidShape(13, 2, 3, 16, 14, 13);

    public BG12(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(ACTIVATED, false)
                // .with(PULSE, false) maybe use this (when active, pulse instead of constant
                // signal)
                // waterlog
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

    // right click to activate
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult hit) {
        boolean isActivated = state.get(ACTIVATED);
        ItemStack heldItem = player.getStackInHand(hand);
        long currentTime = world.getTime(); // Current game time

        if (world.isClient) {
            return ActionResult.PASS;
        }

        // Case 1: Player is holding the key item
        if (heldItem.getItem() instanceof KeyItem) {
            if (isActivated) {
                // Reset the alarm and record the reset time
                world.setBlockState(pos, state.with(ACTIVATED, false));
                world.playSound(null, pos, ModSounds.BG12_RESET, SoundCategory.BLOCKS, 1.0f, 1.0f);
                lastResetTime = currentTime;
                return ActionResult.SUCCESS;
            }
            // Do nothing if the alarm is not activated
            return ActionResult.PASS;
        }

        // Case 2: Prevent reactivation if it's too soon after a reset
        if (currentTime - lastResetTime < 20) { // 20 ticks = 1 second
            return ActionResult.PASS; // Ignore activations within 1 second of reset
        }

        // Case 3: Player is not holding the key and alarm is not activated
        if (!isActivated) {
            // Activate the alarm
            world.setBlockState(pos, state.with(ACTIVATED, true));
            world.playSound(null, pos, ModSounds.BG12_ACTIVATION, SoundCategory.BLOCKS, 1.0f, 1.0f);
            return ActionResult.SUCCESS;
        }

        // Case 4: Alarm is already activated, and the player is not using the key
        return ActionResult.PASS;
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
