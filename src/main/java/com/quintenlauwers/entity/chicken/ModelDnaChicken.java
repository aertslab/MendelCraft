package com.quintenlauwers.entity.chicken;

import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;

@SideOnly(Side.CLIENT)
public class ModelDnaChicken extends ModelChicken
{
    public ModelRenderer head;
    public ModelRenderer body;
    public ModelRenderer rightLeg;
    public ModelRenderer leftLeg;
    public ModelRenderer rightWing;
    public ModelRenderer leftWing;
    public ModelRenderer bill;
    public ModelRenderer chin;

    public ModelRenderer[][] heads = new ModelRenderer[2][5];
    public ModelRenderer[][] bills = new ModelRenderer[2][5];
    public ModelRenderer[][] chins = new ModelRenderer[2][5];
    public ModelRenderer[][] bodies = new ModelRenderer[2][5];
    public ModelRenderer[][] rightLegs = new ModelRenderer[2][5];
    public ModelRenderer[][] leftLegs = new ModelRenderer[2][5];
    public ModelRenderer[][] rightWings = new ModelRenderer[2][5];
    public ModelRenderer[][] leftWings = new ModelRenderer[2][5];



    public ModelDnaChicken()
    {
        textureHeight = 160;
        setCustomOffsets();
        int i = 16;
        this.head = new ModelRenderer(this, 0, 64);
        this.head.addBox(-2.0F, -6.0F, -2.0F, 4, 6, 3, 0.0F);
        this.head.setRotationPoint(0.0F, 15.0F, -4.0F);
        this.bill = new ModelRenderer(this, 14, 0);
        this.bill.addBox(-2.0F, -4.0F, -4.0F, 4, 2, 2, 0.0F);
        this.bill.setRotationPoint(0.0F, 15.0F, -4.0F);
        this.chin = new ModelRenderer(this, 14, 4);
        this.chin.addBox(-1.0F, -2.0F, -3.0F, 2, 2, 2, 0.0F);
        this.chin.setRotationPoint(0.0F, 15.0F, -4.0F);
        this.body = new ModelRenderer(this, 0, 9);
        this.body.addBox(-3.0F, -4.0F, -3.0F, 6, 8, 6, 0.0F);
        this.body.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.rightLeg = new ModelRenderer(this, 26, 0);
        this.rightLeg.addBox(-1.0F, 0.0F, -3.0F, 3, 5, 3);
        this.rightLeg.setRotationPoint(-2.0F, 19.0F, 1.0F);
        this.leftLeg = new ModelRenderer(this, 26, 0);
        this.leftLeg.addBox(-1.0F, 0.0F, -3.0F, 3, 5, 3);
        this.leftLeg.setRotationPoint(1.0F, 19.0F, 1.0F);
        this.rightWing = new ModelRenderer(this, 24, 13);
        this.rightWing.addBox(0.0F, 0.0F, -3.0F, 1, 4, 6);
        this.rightWing.setRotationPoint(-4.0F, 13.0F, 0.0F);
        this.leftWing = new ModelRenderer(this, 24, 13);
        this.leftWing.addBox(-1.0F, 0.0F, -3.0F, 1, 4, 6);
        this.leftWing.setRotationPoint(4.0F, 13.0F, 0.0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {


        int headIndex = 0;
        int billIndex = 0;
        int chinIndex = 0;
        int bodyIndex = 0;
        int rightLegIndex = 0;
        int leftLegIndex = 0;
        int rightWingIndex = 0;
        int leftWingIndex = 0;
        boolean isBig = false;
        if (entityIn instanceof EntityDnaChicken) {
            headIndex = ((EntityDnaChicken) entityIn).getHeadColor();
            isBig = ((EntityDnaChicken) entityIn).isBig();
        }

        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);

        if (this.isChild)
        {
            float f = 2.0F;
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 5.0F * scale, 2.0F * scale);
            this.heads[0][headIndex].render(scale);
            this.bills[0][billIndex].render(scale);
            this.chins[0][billIndex].render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
            this.bodies[0][bodyIndex].render(scale);
            this.rightLegs[0][rightLegIndex].render(scale);
            this.leftLegs[0][rightLegIndex].render(scale);
            this.rightWings[0][rightWingIndex].render(scale);
            this.leftWings[0][leftWingIndex].render(scale);
            GlStateManager.popMatrix();
        } else if (isBig) {
            GlStateManager.disableRescaleNormal();
            float f = 0.2F;
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, -16.0F * f, -1.0F * f);
            this.heads[1][headIndex].render(f);
            this.bills[1][billIndex].render(f);
            this.chins[1][chinIndex].render(f);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, -17.0F * f, 0.0F);
            this.bodies[1][bodyIndex].render(f);
            this.rightLegs[1][rightLegIndex].render(f);
            this.leftLegs[1][leftLegIndex].render(f);
            this.rightWings[1][rightWingIndex].render(f);
            this.leftWings[1][leftWingIndex].render(f);
            GlStateManager.popMatrix();
        }
        else
        {
            GlStateManager.enableRescaleNormal();
            this.heads[0][headIndex].render(scale);
            this.bills[0][headIndex].render(scale);
            this.chins[0][chinIndex].render(scale);
            this.bodies[0][bodyIndex].render(scale);
            this.rightLegs[0][rightLegIndex].render(scale);
            this.leftLegs[0][leftLegIndex].render(scale);
            this.rightWings[0][rightWingIndex].render(scale);
            this.leftWings[0][leftWingIndex].render(scale);
        }
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {

        int headIndex = 0;
        int billIndex = 0;
        int chinIndex = 0;
        int bodyIndex = 0;
        int rightLegIndex = 0;
        int leftLegIndex = 0;
        int rightWingIndex = 0;
        int leftWingIndex = 0;
        boolean isBig = false;
        if (entityIn instanceof EntityDnaChicken) {
            headIndex = ((EntityDnaChicken) entityIn).getHeadColor();
            isBig = ((EntityDnaChicken) entityIn).isBig();
        }
        int size = (isBig) ? 1 : 0;

        this.heads[size][headIndex].rotateAngleX = headPitch * 0.017453292F;
        this.heads[size][headIndex].rotateAngleY = netHeadYaw * 0.017453292F;
        this.bills[size][billIndex].rotateAngleX = this.heads[size][headIndex].rotateAngleX;
        this.bills[size][billIndex].rotateAngleY = this.heads[size][headIndex].rotateAngleY;
        this.chins[size][chinIndex].rotateAngleX = this.heads[size][headIndex].rotateAngleX;
        this.chins[size][chinIndex].rotateAngleY = this.heads[size][headIndex].rotateAngleY;
        this.bodies[size][bodyIndex].rotateAngleX = ((float) Math.PI / 2F);
        this.rightLegs[size][rightLegIndex].rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftLegs[size][leftLegIndex].rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.rightWings[size][rightWingIndex].rotateAngleZ = ageInTicks;
        this.leftWings[size][leftWingIndex].rotateAngleZ = -ageInTicks;
    }

    public void setCustomOffsets() {
        for (int size = 0; size < 2; size++) {
            if (heads[size][0] == null) {
                for (int i = 0; i < heads[size].length; i++) {
                    heads[size][i] = new ModelRenderer(this, 0, i * 32);
                    this.heads[size][i].addBox(-2.0F, -6.0F, -2.0F, 4, 6, 3, 0.0F);
                    this.heads[size][i].setRotationPoint(0.0F, 15.0F, -4.0F);
                }
            }
            if (bills[size][0] == null) {
                for (int i = 0; i < bills[size].length; i++) {
                    bills[size][i] = new ModelRenderer(this, 14, i * 32);
                    this.bills[size][i].addBox(-2.0F, -4.0F, -4.0F, 4, 2, 2, 0.0F);
                    this.bills[size][i].setRotationPoint(0.0F, 15.0F, -4.0F);
                }
            }
            if (chins[size][0] == null) {
                for (int i = 0; i < chins[size].length; i++) {
                    chins[size][i] = new ModelRenderer(this, 14, 4 + i * 32);
                    this.chins[size][i].addBox(-1.0F, -2.0F, -3.0F, 2, 2, 2, 0.0F);
                    this.chins[size][i].setRotationPoint(0.0F, 15.0F, -4.0F);
                }
            }
            if (bodies[size][0] == null) {
                for (int i = 0; i < bodies[size].length; i++) {
                    bodies[size][i] = new ModelRenderer(this, 0, 9 + i * 32);
                    this.bodies[size][i].addBox(-3.0F, -4.0F, -3.0F, 6, 8, 6, 0.0F);
                    this.bodies[size][i].setRotationPoint(0.0F, 16.0F, 0.0F);
                }
            }
            if (rightLegs[size][0] == null) {
                for (int i = 0; i < rightLegs[size].length; i++) {
                    rightLegs[size][i] = new ModelRenderer(this, 26, i * 32);
                    this.rightLegs[size][i].addBox(-1.0F, 0.0F, -3.0F, 3, 5, 3);
                    this.rightLegs[size][i].setRotationPoint(-2.0F, 19.0F, 1.0F);
                }
            }
            if (leftLegs[size][0] == null) {
                for (int i = 0; i < leftLegs[size].length; i++) {
                    leftLegs[size][i] = new ModelRenderer(this, 26, i * 32);
                    this.leftLegs[size][i].addBox(-1.0F, 0.0F, -3.0F, 3, 5, 3);
                    this.leftLegs[size][i].setRotationPoint(1.0F, 19.0F, 1.0F);
                }
            }
            if (rightWings[size][0] == null) {
                for (int i = 0; i < rightWings[size].length; i++) {
                    rightWings[size][i] = new ModelRenderer(this, 24, 13 + i * 32);
                    this.rightWings[size][i].addBox(0.0F, 0.0F, -3.0F, 1, 4, 6);
                    this.rightWings[size][i].setRotationPoint(-4.0F, 13.0F, 0.0F);
                }
            }
            if (leftWings[size][0] == null) {
                for (int i = 0; i < leftWings[size].length; i++) {
                    leftWings[size][i] = new ModelRenderer(this, 24, 13 + i * 32);
                    this.leftWings[size][i].addBox(-1.0F, 0.0F, -3.0F, 1, 4, 6);
                    this.leftWings[size][i].setRotationPoint(4.0F, 13.0F, 0.0F);
                }
            }
        }
        System.out.println("Heads 0 " + Arrays.toString(heads[0]));
    }
}