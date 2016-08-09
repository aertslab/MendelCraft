package com.quintenlauwers.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class
ComStone extends BlockBase {
	
	public ComStone(String name) {
		super(Material.ROCK, name);
	}

	@Override
	public ComStone setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);
		return  this;
	}



}
