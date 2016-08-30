package com.quintenlauwers.item;

import com.quintenlauwers.entity.DnaEntity;
import com.quintenlauwers.main.TestMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

/**
 * Created by quinten on 27/08/16.
 */
public class dnaInspector extends Item {
    public dnaInspector() {
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.MISC);
        setUnlocalizedName("dnaInspector");
        setRegistryName("dnaInspector");

    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if (target instanceof DnaEntity) {
            playerIn.openGui(TestMod.instance, 2, playerIn.getEntityWorld(), ((DnaEntity) target).getRegisteredId(), 0, 0);
            return true;
        }
        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }
}
