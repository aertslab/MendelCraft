package com.quintenlauwers.item;

import com.quintenlauwers.backend.network.entityinteraction.EntityInteractionPackage;
import com.quintenlauwers.entity.DnaEntity;
import com.quintenlauwers.main.TestMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;

import java.util.Arrays;

public class dnaSyringe extends Item {
    public dnaSyringe() {
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.MISC);
        setUnlocalizedName("dnaSyringe");
        setRegistryName("dnaSyringe");

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

    public byte[] getDnaData(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("dnaData")) {
            return stack.getTagCompound().getByteArray("dnaData");
        }
        return null;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        System.out.println("Client only?");
        if (playerIn.getEntityWorld().isRemote) {
            TestMod.network.sendToServer(new EntityInteractionPackage(target, playerIn, hand));
        }
        if (stack.hasTagCompound()) {
            if (stack.getTagCompound().hasKey("dnaData")) {
                System.out.println("Previous dna data:");
                System.out.println(Arrays.toString(stack.getTagCompound().getByteArray("dnaData")));
            }
        }
        ItemStack equipped = playerIn.getHeldItemMainhand();
        if (equipped == null || !(equipped.getItem() instanceof dnaSyringe)) {
            if (playerIn.getHeldItemOffhand() != null && playerIn.getHeldItemOffhand().getItem() instanceof dnaSyringe) {
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
            if (stack.getItem() instanceof dnaSyringe) {
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
