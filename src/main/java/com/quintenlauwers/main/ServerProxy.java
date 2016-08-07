package com.quintenlauwers.main;

import com.quintenlauwers.interfaces.ModGuiHandler;

import net.minecraftforge.fml.common.network.NetworkRegistry;

public class ServerProxy {
	public void registerRenderInfo() {
		NetworkRegistry.INSTANCE.registerGuiHandler(TestMod.instance, new ModGuiHandler());
	}
}
