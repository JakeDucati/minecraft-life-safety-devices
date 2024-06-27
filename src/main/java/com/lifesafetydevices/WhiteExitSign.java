package com.lifesafetydevices;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.util.shape.VoxelShape;
import java.util.Properties;


public class WhiteExitSign extends Block {
    public WhiteExitSign(Settings settings) {
        super(settings);
        //TODO Auto-generated constructor stub
        // setDefaultState(getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.0625f, 0.1875f, 0.8125f, 0.9375f, 0.75f, 1f);
    }
}
