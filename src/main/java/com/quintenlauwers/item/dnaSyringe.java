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

import java.util.List;

public class dnaSyringe extends Item {
    public dnaSyringe() {
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.MISC);
        this.setHasSubtypes(true);
        setUnlocalizedName("dnaSyringe");
        setRegistryName("dnaSyringe");

    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        subItems.add(new ItemStack(itemIn, 1, 0));
    }

    public byte[] getDnaData(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("dnaData")) {
            return stack.getTagCompound().getByteArray("dnaData");
        }
        return null;
    }

    public byte[] getDnaData2(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("dnaData2")) {
            return stack.getTagCompound().getByteArray("dnaData2");
        }
        return null;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if (playerIn.getEntityWorld().isRemote) {
            TestMod.network.sendToServer(new EntityInteractionPackage(target, playerIn, hand));
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
            DnaEntity animal = ((DnaEntity) target);
            writeDnaToTag(equipped.getTagCompound(), animal);
            if (stack.getItem() instanceof dnaSyringe) {
                if (!stack.hasTagCompound()) {
                    stack.setTagCompound(new NBTTagCompound());
                }
                writeDnaToTag(stack.getTagCompound(), animal);
                stack.setItemDamage(1);
            }
            ItemStack inventoryStack = playerIn.inventory.getStackInSlot(playerIn.inventory.currentItem);
            if (inventoryStack != null && inventoryStack.getItem() instanceof dnaSyringe) {
                if (!inventoryStack.hasTagCompound()) {
                    inventoryStack.setTagCompound(new NBTTagCompound());
                }
                writeDnaToTag(inventoryStack.getTagCompound(), animal);
                stack.setItemDamage(1);
            }
            return true;
        }
        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }

    private NBTTagCompound createDnaTag(DnaEntity from) {
        NBTTagCompound tag = new NBTTagCompound();
        writeDnaToTag(tag, from);
        return tag;
    }

    private void writeDnaToTag(NBTTagCompound tag, DnaEntity from) {
        tag.setByteArray("dnaData", from.getDnaData());
        tag.setString("animal", from.getAnimalName());
        if (TestMod.dnaConfig.isDiploid()) {
            tag.setByteArray("dnaData2", from.getDnaData2());
        }
    }


}
