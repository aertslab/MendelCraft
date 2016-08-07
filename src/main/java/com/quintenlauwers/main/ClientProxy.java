package com.quintenlauwers.main;

import com.quintenlauwers.interfaces.ModGuiHandler;
import com.quintenlauwers.item.ObsidianStick;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class ClientProxy extends ServerProxy {
	@Override
	public void registerRenderInfo(){
		ObsidianStick.registerRenders();
		NetworkRegistry.INSTANCE.registerGuiHandler(TestMod.instance, new ModGuiHandler());
	}
}
