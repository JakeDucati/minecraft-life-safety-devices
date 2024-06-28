package com.lifesafetydevices;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.Identifier;

import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lifesafetydevices.block.WhiteExitSign;
import com.lifesafetydevices.block.BG12;


public class LifeSafetyDevices implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("life-safety-devices");

    public static final Block WHITE_EXIT_SIGN = new WhiteExitSign(FabricBlockSettings.of(Material.METAL).strength(4.0f));
    public static final Block BG_12 = new BG12(FabricBlockSettings.of(Material.METAL).strength(4.0f));
    public static final Block TEST = new Block(FabricBlockSettings.of(Material.METAL).strength(4.0f));

    @Override
    public void onInitialize() {
        LOGGER.info("Hello Fabric world!");

        // white exit sign
        Registry.register(Registry.BLOCK, new Identifier("lifesafetydevices", "white_exit_sign"), WHITE_EXIT_SIGN);
        Registry.register(Registry.ITEM, new Identifier("lifesafetydevices", "white_exit_sign"), new BlockItem(WHITE_EXIT_SIGN, new FabricItemSettings()));

        // bg-12 pull station
        Registry.register(Registry.BLOCK, new Identifier("lifesafetydevices", "bg-12"), BG_12);
        Registry.register(Registry.ITEM, new Identifier("lifesafetydevices", "bg-12"), new BlockItem(BG_12, new FabricItemSettings()));

        // test
        Registry.register(Registry.BLOCK, new Identifier("lifesafetydevices", "test"), TEST);
        Registry.register(Registry.ITEM, new Identifier("lifesafetydevices", "test"), new BlockItem(TEST, new FabricItemSettings()));
    }
}
