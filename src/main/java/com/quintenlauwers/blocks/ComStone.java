package com.quintenlauwers.blocks;

import com.quintenlauwers.item.dnaSyringe;
import com.quintenlauwers.main.TestMod;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;

public class
ComStone extends BlockBase {

    public ComStone(String name) {
        super(Material.ROCK, name);
    }

    @Override
    public ComStone setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack equipped = playerIn.getHeldItemMainhand();
        if (equipped != null && !(equipped.getItem() instanceof dnaSyringe)) {
            if (playerIn.getHeldItemOffhand() != null && playerIn.getHeldItemOffhand().getItem() instanceof dnaSyringe) {
                equipped = playerIn.getHeldItemOffhand();
            } else {
                return side == EnumFacing.UP;
            }
        }
        if (equipped != null && equipped.hasTagCompound()) {
            NBTTagCompound compound = equipped.getTagCompound();
            if (compound.hasKey("dnaData")) {
                try {
                    System.out.println(Arrays.toString(compound.getByteArray("dnaData")));
                } catch (NullPointerException e) {
                    System.out.println("Random nullpointer");
                    System.err.println(e.toString());
                }
            }


        }
        playerIn.openGui(TestMod.instance, 1, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);

        return side == EnumFacing.UP;
    }
}
