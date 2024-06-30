package com.lifesafetydevices.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.text.Text;

public class WhiteExitSignItem extends BlockItem {

    public WhiteExitSignItem(Block block, Settings settings) {
        super(block, settings);
    }
    
    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.lifesafetydevices.white_exit_sign"));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
