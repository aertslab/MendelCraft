package com.quintenlauwers.blocks;

import com.quintenlauwers.main.MendelCraft;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * Created by quinten on 8/08/16.
 */
public class BlockBase extends Block implements ItemModelProvider {

    protected String name;

    public BlockBase(Material materialIn, String name) {
        super(materialIn);
        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    @Override
    public void registerItemModel(Item itemBlock) {
        MendelCraft.proxy.registerItemRenderer(itemBlock, 0, name);

    }

    @Override
    public BlockBase setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }


}