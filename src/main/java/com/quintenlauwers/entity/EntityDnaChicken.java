package com.quintenlauwers.entity;

import com.quintenlauwers.main.TestMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.Random;

public class EntityDnaChicken extends EntityChicken
{

    private static final ResourceLocation CHICKEN_TEXTURE_GREEN = new ResourceLocation("testmod:textures/entity/dnaChickenGreen.png");
    private static final ResourceLocation CHICKEN_TEXTURE_RED = new ResourceLocation("testmod:textures/entity/dnaChickenRed.png");

    private boolean color;
    private boolean isColorSet = false;

    public EntityDnaChicken(World worldIn)
    {
        super(worldIn);
        this.setSize(0.4F, 0.7F); // Only sets the hitbox
        this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        this.setPathPriority(PathNodeType.WATER, 0.0F);
        if (!isColorSet) {
            this.color = new Random().nextBoolean();
        }
        System.out.println("Chicken made!");
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
        this.color = compound.getBoolean("color");
        this.isColorSet = true;
        System.out.println("Getting color??");

        if (compound.hasKey("EggLayTime"))
        {
            this.timeUntilNextEgg = compound.getInteger("EggLayTime");
        }
    }

    private void getDnaDataToClient() {
        ByteBuffer b = ByteBuffer.allocate(4);
        b.putInt(this.getEntityId());
        byte[] idBytes = b.array();
        byte[] color = { isColorSet ? (byte) 1: (byte) 0 };
        // TODO: Append byte[]'s and send them
        TestMod.proxy.sendDnaPacketData(idBytes);
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
        compound.setBoolean("color", this.color);
        System.out.println("Setting color!!");
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

    public ResourceLocation getTexture(){
        if (!isColorSet)
            System.out.println("This might be the problem");
        if (this.color) {
            return CHICKEN_TEXTURE_GREEN;
        }
        else {
            return CHICKEN_TEXTURE_RED;
        }
    }
}