package com.lifesafetydevices.item;

import java.util.List;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.text.Text;

public class BG12Item extends Item {

    public BG12Item(Settings settings) {
        super(settings);
    }
    
    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("tooltip.lifesafetydevices.bg-12"));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
