package com.lifesafetydevices;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.Identifier;

import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// all blocks & items
import com.lifesafetydevices.block.*;
import com.lifesafetydevices.item.*;


public class LifeSafetyDevices implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("life-safety-devices");

    // item group
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(
        new Identifier("lifesafetydevices", "general"),
        () -> new ItemStack(com.lifesafetydevices.LifeSafetyDevices.BG_12_ITEM)
    );

    // blocks
    public static final Block WHITE_EXIT_SIGN = new WhiteExitSign(FabricBlockSettings.of(Material.METAL).strength(4.0f));
    public static final Block BG_12 = new BG12(FabricBlockSettings.of(Material.METAL).strength(4.0f));
    public static final Block TEST = new Block(FabricBlockSettings.of(Material.METAL).strength(4.0f));

    // items
    public static final Item BG_12_ITEM = new BG12Item(BG_12, new FabricItemSettings().group(ITEM_GROUP));
    public static final Item WHITE_EXIT_SIGN_ITEM = new WhiteExitSignItem(WHITE_EXIT_SIGN, new FabricItemSettings().group(ITEM_GROUP));
    

    @Override
    public void onInitialize() {
        LOGGER.info("Hello Fabric world!");
        
        // sounds
        ModSounds.registerSounds();

        // white exit sign
        Registry.register(Registry.BLOCK, new Identifier("lifesafetydevices", "white_exit_sign"), WHITE_EXIT_SIGN);
        Registry.register(Registry.ITEM, new Identifier("lifesafetydevices", "white_exit_sign"), WHITE_EXIT_SIGN_ITEM);

        // bg-12 pull station
        Registry.register(Registry.BLOCK, new Identifier("lifesafetydevices", "bg-12"), BG_12);
        Registry.register(Registry.ITEM, new Identifier("lifesafetydevices", "bg-12"), BG_12_ITEM);

        // test
        Registry.register(Registry.BLOCK, new Identifier("lifesafetydevices", "test"), TEST);
        Registry.register(Registry.ITEM, new Identifier("lifesafetydevices", "test"), new BlockItem(TEST, new FabricItemSettings().group(ITEM_GROUP)));
    }
}
