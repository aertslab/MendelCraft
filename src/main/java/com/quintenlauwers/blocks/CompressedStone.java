package com.quintenlauwers.blocks;

import com.quintenlauwers.lib.RefStrings;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CompressedStone {
	public static void mainRegistry() {
		initializeBlock();
		regiserBlock();
	}
	
	public static Block ComStone;
	
	public static void initializeBlock(){
		ComStone = new ComStone(Material.GROUND);
		ComStone.setUnlocalizedName("ComStone");
		ComStone.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}
	
	public static void regiserBlock(){
		GameRegistry.registerBlock(ComStone, ComStone.getUnlocalizedName());
	}
	
	public static void registerRenders(){
		registerRender(ComStone);
	}
	
	public static void registerRender(Block block){
		Item item = Item.getItemFromBlock(block);
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(RefStrings.MODID + ":" + item.getUnlocalizedName(), "inventory"));
	}
}
