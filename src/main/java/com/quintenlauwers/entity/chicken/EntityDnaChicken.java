package com.quintenlauwers.entity.chicken;

import com.google.common.collect.Sets;
import com.quintenlauwers.backend.DnaProperties;
import com.quintenlauwers.backend.network.entityinteraction.EntityChildBirthPackage;
import com.quintenlauwers.backend.network.entityinteraction.ProcessInteractionPackage;
import com.quintenlauwers.entity.DnaEntity;
import com.quintenlauwers.lib.RefStrings;
import com.quintenlauwers.main.MendelCraft;
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

    private static final ResourceLocation CHICKEN_TEXUTRE_MULTICOLOR = new ResourceLocation(RefStrings.MODID + ":textures/entity/chickenVariations.png");

    private byte[] dnaData = new byte[MendelCraft.dnaConfig.getTotalNbOfCodons()];
    private byte[] dnaData2;
    private DnaProperties properties;
    private int myId = 0;
    int incremental = 0;


    public EntityDnaChicken(World worldIn) {
        super(worldIn);
        this.setSize(0.4F, 0.7F); // Only sets the hitbox
        this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        this.setPathPriority(PathNodeType.WATER, 0.0F);
        Random rand = new Random();
        rand.nextBytes(dnaData);
        if (MendelCraft.dnaConfig.isDiploid()) {
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
            if (!this.getEntityWorld().isRemote) {
                MendelCraft.network.sendToAll(new EntityChildBirthPackage(this, ageable));
            }
            EntityDnaChicken child = new EntityDnaChicken(this.worldObj);
            EntityDnaChicken other = (EntityDnaChicken) ageable;
            if (MendelCraft.dnaConfig.isDiploid()) {
                byte[] dnaFinal1 = MendelCraft.dnaConfig.reduceToSingleDnaString(this.getDnaData(), this.getDnaData2());
                byte[] dnaFinal2 = MendelCraft.dnaConfig.reduceToSingleDnaString(other.getDnaData(), other.getDnaData2());
                if (dnaFinal1 == null || dnaFinal2 == null) {
                    return null;
                }
                child.setDnaData(dnaFinal1, dnaFinal2);
            } else {
                byte[] dnaFinal1 = MendelCraft.dnaConfig.reduceToSingleDnaString(this.getDnaData(), other.getDnaData());
                if (dnaFinal1 == null) {
                    return null;
                }
                child.setDnaData(dnaFinal1);
            }
            child.resetInLove();
            return child;
        }
        return null;
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
        if (player.getEntityWorld().isRemote) {
            MendelCraft.network.sendToServer(new ProcessInteractionPackage(this, player, hand, stack));

        }
        return super.processInteract(player, hand, stack);
    }

    @Override
    public void setEntityId(int id) {
        this.myId = id;
        super.setEntityId(id);
        this.getInformationFromServer();
    }

    /**
     * Solves strange client-side bug.
     *
     * @return
     */
    @Override
    public int getGrowingAge() {
        if (super.getGrowingAge() > 0) {
            return 0;
        }
        return super.getGrowingAge();
    }

    @Override
    public int getRegisteredId() {
        return this.myId;
    }

    public void getInformationFromServer() {
        if (this.getEntityWorld().isRemote) {
            MendelCraft.networkHelper.sendDnaRequest(this);
        }
    }

    /**
     * Protected helper method to write entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("IsChickenJockey", this.chickenJockey);
        compound.setInteger("EggLayTime", this.timeUntilNextEgg);
        compound.setByteArray("dnaData", this.getDnaData());
        if (MendelCraft.dnaConfig.isDiploid()) {
            compound.setByteArray("dnaData2", this.getDnaData2());
        }
    }

    /**
     * Protected helper method to read entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.chickenJockey = compound.getBoolean("IsChickenJockey");

        if (compound.hasKey("EggLayTime")) {
            this.timeUntilNextEgg = compound.getInteger("EggLayTime");
        }
        if (compound.hasKey("dnaData")) {
            if (MendelCraft.dnaConfig.isDiploid() && compound.hasKey("dnaData2")) {
                this.properties = new DnaProperties("chicken", compound.getByteArray("dnaData"),
                        compound.getByteArray("dnaData2"));
                return;
            }
            if (!MendelCraft.dnaConfig.isDiploid()) {
                this.properties = new DnaProperties("chicken", compound.getByteArray("dnaData"));
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
            this.dnaData = dnaData;
            this.dnaData2 = dnaData2;

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


    /**
     * Get the headcolor from the dna data.
     * @return An int representing the texture # in CHICKEN_TEXTURE_MULTICOLOR.
     */
    public int getHeadColor() {
        String color = this.properties.getStringProperty("headColor");
        return colorToTextureIndex(color);
    }

    /**
     * Get the bodycolor from the dna data.
     * @return An int representing the texture # in CHICKEN_TEXTURE_MULTICOLOR.
     */
    public int getBodyColor() {
        String color = this.properties.getStringProperty("bodyColor");
        return colorToTextureIndex(color);
    }

    /**
     * Get the wingcolor from the dna data.
     * @return An int representing the texture # in CHICKEN_TEXTURE_MULTICOLOR.
     */
    public int getRightWingColor() {
        String color = this.properties.getStringProperty("rightWingColor");
        return colorToTextureIndex(color);
    }

    /**
     * Get the wingcolor from the dna data.
     * @return An int representing the texture # in CHICKEN_TEXTURE_MULTICOLOR.
     */
    public int getLeftWingColor() {
        String color = this.properties.getStringProperty("leftWingColor");
        return colorToTextureIndex(color);
    }

    /**
     * Get the legcolor from the dna data.
     * @return An int representing the texture # in CHICKEN_TEXTURE_MULTICOLOR.
     */
    public int getRightLegColor() {
        String color = this.properties.getStringProperty("rightLegColor");
        return colorToTextureIndex(color);
    }

    /**
     * Get the legcolor from the dna data.
     * @return An int representing the texture # in CHICKEN_TEXTURE_MULTICOLOR.
     */
    public int getLeftLegColor() {
        String color = this.properties.getStringProperty("leftLegColor");
        return colorToTextureIndex(color);
    }

    /**
     * Get the billcolor from the dna data.
     * @return An int representing the texture # in CHICKEN_TEXTURE_MULTICOLOR.
     */
    public int getBillColor() {
        String color = this.properties.getStringProperty("billColor");
        return colorToTextureIndex(color);
    }

    /**
     * Get the chin from the dna data.
     * @return An int representing the texture # in CHICKEN_TEXTURE_MULTICOLOR.
     */
    public int getChinColor() {
        String color = this.properties.getStringProperty("chinColor");
        return colorToTextureIndex(color);
    }

    /**
     * Get if this chicken is handicapped.
     */
    public boolean isHandicapped() {
        String handicap = this.properties.getStringProperty("handicap");
        return "handicapped".equals(handicap);
    }

    public boolean hasDinoHead() {
        return "dino".equals(this.properties.getStringProperty("headDino"));
    }

    public boolean hasDinoBody() {
        return "dino".equals(this.properties.getStringProperty("bodyDino"));
    }

    public boolean hasDinoLeftArm() {
        return "dino".equals(this.properties.getStringProperty("leftWingDino"));
    }

    public boolean hasDinoRightArm() {
        return "dino".equals(this.properties.getStringProperty("rightWingDino"));
    }

    public boolean hasDinoLeftLeg() {
        return "dino".equals(this.properties.getStringProperty("leftLegDino"));
    }

    public boolean hasDinoRightLeg() {
        return "dino".equals(this.properties.getStringProperty("rightLegDino"));
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

    /**
     * Is this a big chicken?
     */
    public boolean isBig() {
        String size = this.properties.getStringProperty("size");
        return "big".equals(size);
    }

    public ResourceLocation getTexture() {
        return CHICKEN_TEXUTRE_MULTICOLOR;
    }

    /**
     * Called when the java Object is removed.
     * Remove this entityid from the storage (for network communication).
     * @throws Throwable
     */
    @Override
    public void finalize() throws Throwable {
        MendelCraft.storage.removeById(this.getEntityId());
        super.finalize();
    }

}