package com.lifesafetydevices;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class LifeSafetyDevicesClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		
		BlockRenderLayerMap.INSTANCE.putBlock(LifeSafetyDevices.HORN_STROBE, RenderLayer.getCutout());
	}
}