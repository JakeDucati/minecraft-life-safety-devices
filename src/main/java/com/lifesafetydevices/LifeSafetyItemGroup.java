package com.lifesafetydevices;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class LifeSafetyItemGroup extends ItemGroup {

    public static final LifeSafetyItemGroup INSTANCE = new LifeSafetyItemGroup(ItemGroup.GROUPS.length, "life_safety_devices");

    private LifeSafetyItemGroup(int index, String label) {
        super(index, label);
    }

    @Override
    public ItemStack createIcon() {
        // Return an ItemStack that represents the icon for your item group
        // For example, returning an item that you want to represent your mod's icon
        return new ItemStack(LifeSafetyDevices.BG_12);
    }
}
