package com.quintenlauwers.entity.chicken;

import com.google.common.collect.Sets;
import com.quintenlauwers.backend.DnaProperties;
import com.quintenlauwers.backend.network.entityinteraction.ProcessInteractionPackage;
import com.quintenlauwers.entity.DnaEntity;
import com.quintenlauwers.main.TestMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.Set;

public class EntityDnaChicken extends EntityChicken implements DnaEntity {

    private static final Set<Item> TEMPTATION_ITEMS = Sets.newHashSet(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS);


    private static final ResourceLocation CHICKEN_TEXTURE_GREEN = new ResourceLocation("testmod:textures/entity/dnaChickenGreen.png");
    private static final ResourceLocation CHICKEN_TEXTURE_RED = new ResourceLocation("testmod:textures/entity/dnaChickenRed.png");
    private static final ResourceLocation CHICKEN_TEXUTRE_MULTICOLOR = new ResourceLocation("testmod:textures/entity/chickenVariations.png");

    private byte[] dnaData = new byte[TestMod.dnaConfig.getTotalNbOfCodons()];
    private byte[] dnaData2;
    private DnaProperties properties;
    private int myId = 0;
    int incremental = 0;
//    private boolean inLove;

//    @Nullable
//    @Override
//    public AxisAlignedBB getCollisionBoundingBox() {
//        return this.getEntityBoundingBox();
//    }
//
//    @Nullable
//    @Override
//    public AxisAlignedBB getCollisionBox(Entity entityIn) {
//        return super.getEntityBoundingBox();
//    }

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
        properties = new DnaProperties("chicken", this.dnaData, this.dnaData2);

    }

    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.4D));
        this.tasks.addTask(2, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(3, new EntityAITempt(this, 1.0D, false, TEMPTATION_ITEMS));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
    }


    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate() {
        if (incremental == 0) {
            System.out.println("inlove " + this.isInLove());
            if (!isChild()) {
                if (isBig()) {
                    this.setScale(4);
                } else {
                    setScale(1);
                }
            }
        }
        incremental = (++incremental % 60);
        super.onLivingUpdate();
    }

    @Override
    public EntityDnaChicken createChild(EntityAgeable ageable) {
        if (ageable instanceof EntityDnaChicken) {
            this.resetInLove();
            ((EntityDnaChicken) ageable).resetInLove();
            EntityDnaChicken child = new EntityDnaChicken(this.worldObj);
            child.resetInLove();
            return child;
        }
        return null;
    }

//    @Override
//    public void handleStatusUpdate(byte id) {
//        if (! isInLove() && id == 18) {
//            return;
//        }
//        super.handleStatusUpdate(id);
//    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
//    @Override
//    public boolean isBreedingItem(@Nullable ItemStack stack) {
//        return true;
////        return super.isBreedingItem(stack);
//    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
        if (player.getEntityWorld().isRemote) {
            TestMod.network.sendToServer(new ProcessInteractionPackage(this, player, hand, stack));
        }
        return super.processInteract(player, hand, stack);
    }

//    @Override
//    public boolean canMateWith(EntityAnimal otherAnimal) {
////        return super.canMateWith(otherAnimal);
//        return true;
//    }

    @Override
    public void setEntityId(int id) {
        this.myId = id;
        super.setEntityId(id);
        this.getInformationFromServer();
    }

//    @Override
//    public int getGrowingAge() {
//        return 0;
//    }

    @Override
    public int getRegisteredId() {
        return this.myId;
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

    @Override
    public DnaProperties getProperties() {
        return this.properties;
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

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_CHICKEN_AMBIENT;
    }

    protected SoundEvent getHurtSound() {
        return SoundEvents.ENTITY_CHICKEN_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CHICKEN_DEATH;
    }


    public int getHeadColor() {
        String color = this.properties.getStringProperty("headColor");
        return colorToTextureIndex(color);
    }

    public int getBodyColor() {
        String color = this.properties.getStringProperty("bodyColor");
        return colorToTextureIndex(color);
    }

    public int getRightWingColor() {
        String color = this.properties.getStringProperty("rightWingColor");
        return colorToTextureIndex(color);
    }

    public int getLeftWingColor() {
        String color = this.properties.getStringProperty("leftWingColor");
        return colorToTextureIndex(color);
    }

    public int getRightLegColor() {
        String color = this.properties.getStringProperty("rightLegColor");
        return colorToTextureIndex(color);
    }

    public int getLeftLegColor() {
        String color = this.properties.getStringProperty("leftLegColor");
        return colorToTextureIndex(color);
    }

    public int getBillColor() {
        String color = this.properties.getStringProperty("billColor");
        return colorToTextureIndex(color);
    }

    public int getChinColor() {
        String color = this.properties.getStringProperty("chinColor");
        return colorToTextureIndex(color);
    }

    public boolean isHandicapped() {
        String handicap = this.properties.getStringProperty("handicap");
        return "handicapped".equals(handicap);
    }

    public boolean hasDinoHead() {
        return true;
    }

    public boolean hasDinoBody() {
        return false;
    }

    public boolean hasDinoLeftArm() {
        return true;
    }

    public boolean hasDinoRightArm() {
        return true;
    }

    public boolean hasDinoLeftLeg() {
        return false;
    }

    public boolean hasDinoRightLeg() {
        return false;
    }

    private int colorToTextureIndex(String color) {
        int colorNb;

        if ("white".equals(color))
            colorNb = 0;
        else if ("black".equals(color))
            colorNb = 1;
        else if ("brown".equals(color))
            colorNb = 2;
        else if ("green".equals(color))
            colorNb = 3;
        else if ("red".equals(color))
            colorNb = 4;
        else
            colorNb = 0;

        return colorNb;
    }

    public boolean isBig() {
        String size = this.properties.getStringProperty("size");
        return "big".equals(size);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
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