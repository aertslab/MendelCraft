package com.quintenlauwers.item;

import com.quintenlauwers.lib.RefStrings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ObsidianStick {
	
	public static void mainRegistry() {
		initializeItem();
		regiserItem();
	}
	
	public static Item oStick;
	
	public static void initializeItem(){
		oStick = new ObsStick();
		
	}
	
	public static void regiserItem(){
		GameRegistry.registerItem(oStick, oStick.getUnlocalizedName().substring(5));
	}
	
	public static void registerRenders(){
		registerRender(oStick);
	}
	
	public static void registerRender(Item item){
		Minecraft a = Minecraft.getMinecraft();
		RenderItem b = a.getRenderItem();
		ItemModelMesher c = b.getItemModelMesher();
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(RefStrings.MODID + ":" + item.getUnlocalizedName().substring(5), "inventory"));
	}
}
