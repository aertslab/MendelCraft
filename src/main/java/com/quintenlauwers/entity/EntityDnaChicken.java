package com.quintenlauwers.entity;

import com.quintenlauwers.backend.DnaProperties;
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
import java.util.Random;

public class EntityDnaChicken extends EntityChicken implements DnaEntity
{

    private static final ResourceLocation CHICKEN_TEXTURE_GREEN = new ResourceLocation("testmod:textures/entity/dnaChickenGreen.png");
    private static final ResourceLocation CHICKEN_TEXTURE_RED = new ResourceLocation("testmod:textures/entity/dnaChickenRed.png");

    private byte[] dnaData = new byte[TestMod.dnaConfig.getTotalNbOfCodons()];
    private DnaProperties properties;


    public EntityDnaChicken(World worldIn)
    {
        super(worldIn);
        this.setSize(0.4F, 0.7F); // Only sets the hitbox
        this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        this.setPathPriority(PathNodeType.WATER, 0.0F);
        new Random().nextBytes(dnaData);
        properties = new DnaProperties("chicken", dnaData);
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


    @Override
    public void setEntityId(int id){
        super.setEntityId(id);
        this.getInformationFromServer();
        this.resetEntityId();
    }



    public void getInformationFromServer() {
        if (this.getEntityWorld().isRemote) {
            TestMod.networkHelper.sendDnaRequest(this);
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("IsChickenJockey", this.chickenJockey);
        compound.setInteger("EggLayTime", this.timeUntilNextEgg);
        compound.setByteArray("dnaData", this.getDnaData());
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
        if (compound.hasKey("dnaData")) {
            this.properties = new DnaProperties("chicken", compound.getByteArray("dnaData"));
        }
    }

    @Override
    public byte[] getDnaData() {
        return this.properties.getDnaData();
    }

    @Override
    public void setDnaData(byte[] dnaData) {
        if (dnaData != null){
            this.properties = new DnaProperties("chicken", dnaData);
        }
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
        if ("green".equals(this.properties.getStringProperty("color"))) {
            return CHICKEN_TEXTURE_GREEN;
        }
        else {
            return CHICKEN_TEXTURE_RED;
        }
    }

    @Override
    public void finalize() throws Throwable {
        TestMod.storage.removeById(this.getEntityId());
        super.finalize();
    }

}