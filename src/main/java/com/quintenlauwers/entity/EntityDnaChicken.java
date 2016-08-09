package com.quintenlauwers.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityDnaChicken extends EntityChicken
{
    public EntityDnaChicken(World worldIn)
    {
        super(worldIn);
        this.setSize(0.4F, 0.7F); // Only sets the hitbox
        this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        this.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    @Override
    protected void initEntityAI()
    {
        super.initEntityAI();
    }



    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
    }

    @Override
    public EntityDnaChicken createChild(EntityAgeable ageable)
    {
        return new EntityDnaChicken(this.worldObj);
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    @Override
    public boolean isBreedingItem(@Nullable ItemStack stack)
    {
        return super.isBreedingItem(stack);
    }


    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.chickenJockey = compound.getBoolean("IsChickenJockey");

        if (compound.hasKey("EggLayTime"))
        {
            this.timeUntilNextEgg = compound.getInteger("EggLayTime");
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setBoolean("IsChickenJockey", this.chickenJockey);
        compound.setInteger("EggLayTime", this.timeUntilNextEgg);
    }

    public void updatePassenger(Entity passenger)
    {
        super.updatePassenger(passenger);
        float f = MathHelper.sin(this.renderYawOffset * 0.017453292F);
        float f1 = MathHelper.cos(this.renderYawOffset * 0.017453292F);
        float f2 = 0.1F;
        float f3 = 0.0F;
        passenger.setPosition(this.posX + (double)(0.1F * f), this.posY + (double)(this.height * 0.5F) + passenger.getYOffset() + 0.0D, this.posZ - (double)(0.1F * f1));

        if (passenger instanceof EntityLivingBase)
        {
            ((EntityLivingBase)passenger).renderYawOffset = this.renderYawOffset;
        }
    }
}