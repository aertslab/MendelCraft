package com.quintenlauwers.entity.chicken;

import com.quintenlauwers.backend.DnaProperties;
import com.quintenlauwers.entity.DnaEntity;
import com.quintenlauwers.main.TestMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class EntityDnaChicken extends EntityChicken implements DnaEntity {

    private static final ResourceLocation CHICKEN_TEXTURE_GREEN = new ResourceLocation("testmod:textures/entity/dnaChickenGreen.png");
    private static final ResourceLocation CHICKEN_TEXTURE_RED = new ResourceLocation("testmod:textures/entity/dnaChickenRed.png");
    private static final ResourceLocation CHICKEN_TEXUTRE_MULTICOLOR = new ResourceLocation("testmod:textures/entity/chickenVariations.png");

    private byte[] dnaData = new byte[TestMod.dnaConfig.getTotalNbOfCodons()];
    private byte[] dnaData2;
    private DnaProperties properties;

    private boolean isBig;


    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getEntityBoundingBox();
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        return super.getEntityBoundingBox();
    }

//    @Override
//    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
//        System.out.println("aaah damage!!! " + damageAmount);
//        super.damageEntity(damageSrc, damageAmount);
//    }

    public EntityDnaChicken(World worldIn) {
        super(worldIn);
        this.setSize(0.4F, 0.7F); // Only sets the hitbox
        this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        this.setPathPriority(PathNodeType.WATER, 0.0F);
        Random rand = new Random();
        rand.nextBytes(dnaData);
        if (TestMod.dnaConfig.isDiploid()) {
            this.dnaData2 = new byte[dnaData.length];
            rand.nextBytes(this.dnaData2);
        }
        isBig = rand.nextBoolean();
        properties = new DnaProperties("chicken", this.dnaData, this.dnaData2);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
    }


    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
    }

    @Override
    public EntityDnaChicken createChild(EntityAgeable ageable) {
        return new EntityDnaChicken(this.worldObj);
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    @Override
    public boolean isBreedingItem(@Nullable ItemStack stack) {
        return super.isBreedingItem(stack);
    }


    @Override
    public void setEntityId(int id) {
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
        if (TestMod.dnaConfig.isDiploid()) {
            compound.setByteArray("dnaData2", this.getDnaData2());
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.chickenJockey = compound.getBoolean("IsChickenJockey");

        if (compound.hasKey("EggLayTime")) {
            this.timeUntilNextEgg = compound.getInteger("EggLayTime");
        }
        if (compound.hasKey("dnaData")) {
            if (TestMod.dnaConfig.isDiploid() && compound.hasKey("dnaData2")) {
                this.properties = new DnaProperties("chicken", compound.getByteArray("dnaData"),
                        compound.getByteArray("dnaData2"));
                System.out.println("Diploid loaded");
                return;
            }
            if (!TestMod.dnaConfig.isDiploid()) {
                this.properties = new DnaProperties("chicken", compound.getByteArray("dnaData"));
                System.out.println("Not diploid");
            }
        }
    }

    @Override
    public byte[] getDnaData() {
        return this.properties.getDnaData();
    }

    @Override
    public byte[] getDnaData2() {
        return this.properties.getDnaData2();
    }

    @Override
    public void setDnaData(byte[] dnaData) {
        if (dnaData != null) {
            this.properties = new DnaProperties("chicken", dnaData);
        }
    }

    @Override
    public void setDnaData(byte[] dnaData, byte[] dnaData2) {
        if (dnaData != null) {
            this.properties = new DnaProperties("chicken", dnaData, dnaData2);
        }
    }

    @Override
    public String getAnimalName() {
        return "chicken";
    }

    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);
        float f = MathHelper.sin(this.renderYawOffset * 0.017453292F);
        float f1 = MathHelper.cos(this.renderYawOffset * 0.017453292F);
        float f2 = 0.1F;
        float f3 = 0.0F;
        passenger.setPosition(this.posX + (double) (0.1F * f), this.posY + (double) (this.height * 0.5F) + passenger.getYOffset() + 0.0D, this.posZ - (double) (0.1F * f1));

        if (passenger instanceof EntityLivingBase) {
            ((EntityLivingBase) passenger).renderYawOffset = this.renderYawOffset;
        }
    }

    public int getHeadColor() {
        if ("green".equals(this.properties.getStringProperty("color"))) {
            return 3;
        } else {
            return 4;
        }
    }

    public boolean isBig() {
        if (this.isBig) {
//            AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
//            if (Math.abs(axisalignedbb.maxX - axisalignedbb.minX) < 4.2 * 0.3) {
//                System.out.println(Math.abs(axisalignedbb.maxX - axisalignedbb.minX));
//                this.setEntityBoundingBox(new AxisAlignedBB(axisalignedbb.minX - 4.2 * 0.13, axisalignedbb.minY, axisalignedbb.minZ - 4.2 * 0.13, axisalignedbb.minX + 4.2 * 0.25, axisalignedbb.minY + 0.55 * 4.2, axisalignedbb.minZ + 4.2 * 0.25));
//            }
            setScale(4);
            return true;
        }
        return false;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        System.out.println("Attacked!!");
        System.out.println(net.minecraftforge.common.ForgeHooks.onLivingAttack(this, source, amount));
        System.out.println(source);
        return super.attackEntityFrom(source, amount);
    }

    public ResourceLocation getTexture() {
//        if ("green".equals(this.properties.getStringProperty("color"))) {
//            return CHICKEN_TEXTURE_GREEN;
//        } else {
//            return CHICKEN_TEXTURE_RED;
//        }
        return CHICKEN_TEXUTRE_MULTICOLOR;
    }

    @Override
    public void finalize() throws Throwable {
        TestMod.storage.removeById(this.getEntityId());
        super.finalize();
    }

}