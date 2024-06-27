package com.lifesafetydevices;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.Identifier;

import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LifeSafetyDevices implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("life-safety-devices");


    /* Declare and initialize our custom block instance.
       We set our block material to `METAL`.
       
       `strength` sets both the hardness and the resistance of a block to the same value.
       Hardness determines how long the block takes to break, and resistance determines how strong the block is against blast damage (e.g. explosions).
       Stone has a hardness of 1.5f and a resistance of 6.0f, while Obsidian has a hardness of 50.0f and a resistance of 1200.0f.
       
       You can find the stats of all vanilla blocks in the class `Blocks`, where you can also reference other blocks.
    */
    // public static final Block EXAMPLE_BLOCK = new Block(FabricBlockSettings.of(Material.METAL).strength(4.0f)); // fabric api version <= 0.77.0
    // public static final Block EXAMPLE_BLOCK  = new Block(FabricBlockSettings.create().strength(4.0f));

    public static final Block WHITE_EXIT_SIGN = new Block(FabricBlockSettings.of(Material.METAL).strength(4.0f));
    public static final Block TEST = new Block(FabricBlockSettings.of(Material.METAL).strength(4.0f));

    // private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder(new Identifier("tutorial", "test_group"))
	//         .icon(() -> new ItemStack(WHITE_EXIT_SIGN))
	//         .build();


    @Override
    public void onInitialize() {
        LOGGER.info("Hello Fabric world!");

        // white exit sign
        Registry.register(Registry.BLOCK, new Identifier("lifesafetydevices", "white_exit_sign"), WHITE_EXIT_SIGN);
        Registry.register(Registry.ITEM, new Identifier("lifesafetydevices", "white_exit_sign"), new BlockItem(WHITE_EXIT_SIGN, new FabricItemSettings()));


        // test block test texture thing test
        Registry.register(Registry.BLOCK, new Identifier("lifesafetydevices", "test"), TEST);
        Registry.register(Registry.ITEM, new Identifier("lifesafetydevices", "test"), new BlockItem(TEST, new FabricItemSettings()));

        // ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP).register(content -> {
        //     content.add(CUSTOM_ITEM);
        // });
    }
}
