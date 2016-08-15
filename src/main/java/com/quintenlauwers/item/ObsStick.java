package com.quintenlauwers.item;

import com.quintenlauwers.entity.DnaEntity;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;

import java.util.Arrays;

public class ObsStick extends Item {
    public ObsStick() {
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.MISC);
        setUnlocalizedName("oStick");
        setRegistryName("oStick");

    }

//    @Override
//    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
//            if (itemStackIn.hasTagCompound()) {
//                if (itemStackIn.getTagCompound().hasKey("dnaData")) {
//                    System.out.println(Arrays.toString(itemStackIn.getTagCompound().getByteArray("dnaData")));
//                }
//            } else {
//                System.out.println("No tag compound");
//            }
//        return new ActionResult(EnumActionResult.PASS, itemStackIn);
//    }
//
//    @Override
//    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
//        stack.setTagCompound(new NBTTagCompound());
//        stack.getTagCompound().setString("owner", playerIn.getDisplayNameString());
//        super.onCreated(stack, worldIn, playerIn);
//    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        System.out.println("Client only?");
        if (stack.hasTagCompound()) {
            if (stack.getTagCompound().hasKey("dnaData")) {
                System.out.println("Previous dna data:");
                System.out.println(Arrays.toString(stack.getTagCompound().getByteArray("dnaData")));
            }
        }
        ItemStack equipped = playerIn.getHeldItemMainhand();
        if (!(equipped.getItem() instanceof ObsStick)) {
            if (playerIn.getHeldItemOffhand().getItem() instanceof ObsStick) {
                equipped = playerIn.getHeldItemOffhand();
            } else {
                return false;
            }
        }
        if (target instanceof DnaEntity) {
            if (!equipped.hasTagCompound()) {
                equipped.setTagCompound(new NBTTagCompound());
            }
            equipped.getTagCompound().setByteArray("dnaData", ((DnaEntity) target).getDnaData());
            if (stack.getItem() instanceof ObsStick) {
                if (!stack.hasTagCompound()) {
                    stack.setTagCompound(new NBTTagCompound());
                }
                stack.getTagCompound().setByteArray("dnaData", ((DnaEntity) target).getDnaData());
            }
            return true;
        }
        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }


}
