package com.quintenlauwers.item;

import com.quintenlauwers.main.TestMod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ObsStick extends Item {
	public ObsStick() {
		setMaxStackSize(64);
		setCreativeTab(CreativeTabs.MISC);
		setUnlocalizedName("oStick");
		
	}
	
	@Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (worldIn.isRemote) {
            playerIn.openGui(TestMod.instance, 0, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
        }
        return new ActionResult(EnumActionResult.PASS, itemStackIn);
    }

}
