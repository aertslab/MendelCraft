package com.quintenlauwers.item;

import com.quintenlauwers.entity.DnaEntity;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ObsStick extends Item {
    public ObsStick() {
        setMaxStackSize(64);
        setCreativeTab(CreativeTabs.MISC);
        setUnlocalizedName("oStick");
        setRegistryName("oStick");

    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
//        if (worldIn.isRemote) {
//            if (itemStackIn.hasTagCompound()) {
//                if (itemStackIn.getTagCompound().hasKey("dnaData")) {
//                    System.out.println(Arrays.toString(itemStackIn.getTagCompound().getByteArray("dnaData")));
//                }
//            } else {
//                System.out.println("No tag compound");
//            }
//        }
//        return new ActionResult(EnumActionResult.PASS, itemStackIn);
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setString("owner", playerIn.getDisplayNameString());
        super.onCreated(stack, worldIn, playerIn);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        ItemStack equipped = playerIn.getHeldItemMainhand();
        if (!(equipped.getItem() instanceof ObsStick)) {
            if (playerIn.getHeldItemOffhand().getItem() instanceof ObsStick) {
                equipped = playerIn.getHeldItemOffhand();
            } else {
                return false;
            }
        }
        if (target instanceof DnaEntity) {
            equipped.setTagCompound(new NBTTagCompound());
            equipped.getTagCompound().setByteArray("dnaData", ((DnaEntity) target).getDnaData());
            return true;
        }
        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }


}
