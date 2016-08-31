package com.quintenlauwers.entity.chicken;

import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelDnaChicken extends ModelChicken {
    public ModelRenderer head;
    public ModelRenderer body;
    public ModelRenderer rightLeg;
    public ModelRenderer leftLeg;
    public ModelRenderer rightWing;
    public ModelRenderer leftWing;
    public ModelRenderer bill;
    public ModelRenderer chin;

    public ModelRenderer[] dinoLowerLips = new ModelRenderer[3];
    public ModelRenderer[] dinoUpperLips = new ModelRenderer[3];
    public ModelRenderer[] dinoLowerNecks = new ModelRenderer[3];
    public ModelRenderer[] dinoUpperNecks = new ModelRenderer[3];
    public ModelRenderer[] dinoHeads = new ModelRenderer[3];

    public ModelRenderer[] dinoBodies = new ModelRenderer[3];
    public ModelRenderer[] dinoUpperTails = new ModelRenderer[3];
    public ModelRenderer[] dinoMidTails = new ModelRenderer[3];
    public ModelRenderer[] dinoLowerTails = new ModelRenderer[3];

    public ModelRenderer[] dinoThighRights = new ModelRenderer[3];
    public ModelRenderer[] dinoThighLefts = new ModelRenderer[3];

    public ModelRenderer[] dinoLeftArms = new ModelRenderer[3];
    public ModelRenderer[] dinoRightArms = new ModelRenderer[3];

    public ModelRenderer[] dinoLeftLegs = new ModelRenderer[3];
    public ModelRenderer[] dinoRightLegs = new ModelRenderer[3];


    public ModelRenderer[][] heads = new ModelRenderer[3][5];
    public ModelRenderer[][] bills = new ModelRenderer[3][5];
    public ModelRenderer[][] chins = new ModelRenderer[3][5];
    public ModelRenderer[][] bodies = new ModelRenderer[3][5];
    public ModelRenderer[][] rightLegs = new ModelRenderer[3][5];
    public ModelRenderer[][] leftLegs = new ModelRenderer[3][5];
    public ModelRenderer[][] rightWings = new ModelRenderer[3][5];
    public ModelRenderer[][] leftWings = new ModelRenderer[3][5];


    public ModelDnaChicken() {
        textureHeight = 192;
        createCustomModelRenderers();
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
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {


        boolean isBig = false;
        if (entityIn instanceof EntityDnaChicken) {
            EntityDnaChicken chicken = (EntityDnaChicken) entityIn;
            isBig = chicken.isBig();
        }

        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);

//        scale = 0.2F;

        if (this.isChild) {
            float f = 2.0F;
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 5.0F * scale, 2.0F * scale);
            for (ModelRenderer headRenderer : getHeadModels(entityIn)) {
                headRenderer.render(scale);
            }
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
            for (ModelRenderer bodyRenderer : getBodyModels(entityIn)) {
                bodyRenderer.render(scale);
            }
            for (ModelRenderer legRenderer : getLegModels(entityIn)) {
                legRenderer.render(scale);
            }
            for (ModelRenderer wingRenderer : getWingModels(entityIn)) {
                wingRenderer.render(scale);
            }
            GlStateManager.popMatrix();
        } else if (isBig) {
            GlStateManager.disableRescaleNormal();
            float f = 0.2F;
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, -17.0F * f, 0.0F * f);

            for (ModelRenderer headRenderer : getHeadModels(entityIn)) {
                headRenderer.render(f);
            }
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, -17.0F * f, 0.0F);
            for (ModelRenderer bodyRenderer : getBodyModels(entityIn)) {
                bodyRenderer.render(f);
            }
            for (ModelRenderer legRenderer : getLegModels(entityIn)) {
                legRenderer.render(f);
            }
            for (ModelRenderer wingRenderer : getWingModels(entityIn)) {
                wingRenderer.render(f);
            }
            GlStateManager.popMatrix();
        } else {
            GlStateManager.enableRescaleNormal();
            for (ModelRenderer headRenderer : getHeadModels(entityIn)) {
                headRenderer.render(scale);
            }
            for (ModelRenderer bodyRenderer : getBodyModels(entityIn)) {
                bodyRenderer.render(scale);
            }
            for (ModelRenderer legRenderer : getLegModels(entityIn)) {
                legRenderer.render(scale);
            }
            for (ModelRenderer wingRenderer : getWingModels(entityIn)) {
                wingRenderer.render(scale);
            }

        }
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {

        int headIndex = 0;
        int billIndex = 0;
        int chinIndex = 0;
        int bodyIndex = 0;
        int rightLegIndex = 0;
        int leftLegIndex = 0;
        int rightWingIndex = 0;
        int leftWingIndex = 0;
        boolean isBig = false;
        boolean isHandicapped = false;
        if (entityIn instanceof EntityDnaChicken) {
            EntityDnaChicken chicken = (EntityDnaChicken) entityIn;
            headIndex = chicken.getHeadColor();
            billIndex = chicken.getBillColor();
            chinIndex = chicken.getChinColor();
            bodyIndex = chicken.getBodyColor();
            rightLegIndex = chicken.getRightLegColor();
            leftLegIndex = chicken.getLeftLegColor();
            rightWingIndex = chicken.getRightWingColor();
            leftWingIndex = chicken.getLeftWingColor();
            isBig = chicken.isBig();
            isHandicapped = chicken.isHandicapped();
        }
        int size = (isBig) ? 1 : 0;
        size = this.isChild ? 2 : size;

        float strange = (isHandicapped) ? 1.751728F : 1;


        this.dinoHeads[size].rotateAngleX = headPitch * 0.017453292F * strange * 0.8F - (float) (-10. / 180 * Math.PI) + (strange - 1);
        this.dinoHeads[size].rotateAngleY = netHeadYaw * 0.017453292F * strange * strange * strange - (strange - 1);
        this.dinoLowerLips[size].rotateAngleX = this.dinoHeads[size].rotateAngleX + (float) (7. / 180 * Math.PI);
        this.dinoLowerLips[size].rotateAngleY = this.dinoHeads[size].rotateAngleY;
        this.dinoUpperLips[size].rotateAngleX = this.dinoHeads[size].rotateAngleX;
        this.dinoUpperLips[size].rotateAngleY = this.dinoHeads[size].rotateAngleY;
        this.dinoLowerNecks[size].rotateAngleX = (float) (-45. / 180 * Math.PI);
        this.dinoUpperNecks[size].rotateAngleX = (float) (29. / 180 * Math.PI);
        this.dinoBodies[size].rotateAngleX = (float) (50. / 180 * Math.PI);
        this.dinoUpperTails[size].rotateAngleX = (float) (-14. / 180 * Math.PI);
        this.dinoMidTails[size].rotateAngleX = (float) (-10. / 180 * Math.PI);
        this.dinoLowerTails[size].rotateAngleX = (float) (8. / 180 * Math.PI);
        this.dinoLeftArms[size].rotateAngleX = (float) (23. / 180 * Math.PI);
        this.dinoRightArms[size].rotateAngleX = (float) (23. / 180 * Math.PI);
        this.heads[size][headIndex].rotateAngleX = headPitch * 0.017453292F * strange + (strange - 1);
        this.heads[size][headIndex].rotateAngleY = netHeadYaw * 0.017453292F * strange * strange * strange - (strange - 1);
        this.bills[size][billIndex].rotateAngleX = this.heads[size][headIndex].rotateAngleX;
        this.bills[size][billIndex].rotateAngleY = this.heads[size][headIndex].rotateAngleY;
        this.chins[size][chinIndex].rotateAngleX = this.heads[size][headIndex].rotateAngleX;
        this.chins[size][chinIndex].rotateAngleY = this.heads[size][headIndex].rotateAngleY;
        this.bodies[size][bodyIndex].rotateAngleX = ((float) Math.PI / 2F);
        this.rightWings[size][rightWingIndex].rotateAngleZ = ageInTicks * strange;
        this.leftWings[size][leftWingIndex].rotateAngleZ = -ageInTicks * strange;

        this.rightLegs[size][rightLegIndex].rotateAngleX = MathHelper.cos(limbSwing * 0.6662F * strange) * 1.4F * limbSwingAmount - (strange * strange - 1);
        this.leftLegs[size][leftLegIndex].rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI * strange) * 1.4F * limbSwingAmount;

        this.dinoThighRights[size].rotateAngleX = this.rightLegs[size][rightLegIndex].rotateAngleX;
        this.dinoThighLefts[size].rotateAngleX = this.leftLegs[size][leftLegIndex].rotateAngleX;

        this.dinoRightLegs[size].rotateAngleX = this.rightLegs[size][rightLegIndex].rotateAngleX;
        this.dinoLeftLegs[size].rotateAngleX = this.leftLegs[size][leftLegIndex].rotateAngleX;

        this.leftWings[size][leftWingIndex].rotateAngleZ += strange - 1;
        this.heads[size][headIndex].rotateAngleZ = -strange + 1;
        this.bills[size][billIndex].rotateAngleZ = this.heads[size][headIndex].rotateAngleZ * (strange - 1);
        this.chins[size][chinIndex].rotateAngleZ = this.heads[size][headIndex].rotateAngleZ * (strange - 1);
        this.rightLegs[size][rightLegIndex].rotateAngleY = (float) Math.PI / 2 * (strange - 1);

    }

    /**
     * Create custom models for different sizes and different colors.
     * Also create the dino models.
     */
    public void createCustomModelRenderers() {
        for (int size = 0; size < 3; size++) {
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

            this.dinoLowerLips[size] = new ModelRenderer(this, 0, 173);
            this.dinoLowerLips[size].addBox(-0.5F, -1.0F, -6.0F, 1, 1, 4, 0.0F);
            this.dinoLowerLips[size].setRotationPoint(0.0F, 8.0F, -6.0F);

            this.dinoUpperLips[size] = new ModelRenderer(this, 0, 167);
            this.dinoUpperLips[size].addBox(-1.0F, -2.0F, -7.0F, 2, 1, 5, 0.0F);
            this.dinoUpperLips[size].setRotationPoint(0.0F, 8.0F, -6.0F);

            this.dinoUpperNecks[size] = new ModelRenderer(this, 0, 178);
            this.dinoUpperNecks[size].addBox(-1.0F, -8.0F, -0.0F, 2, 8, 2, 0.0F);
            this.dinoUpperNecks[size].setRotationPoint(0.0F, 15.0F, -4.0F);

            this.dinoLowerNecks[size] = new ModelRenderer(this, 14, 160);
            this.dinoLowerNecks[size].addBox(-1.5F, -3.5F, -4.0F, 3, 3, 6, 0.0F);
            this.dinoLowerNecks[size].setRotationPoint(0.0F, 15.0F, -4.0F);

            this.dinoHeads[size] = new ModelRenderer(this, 0, 160);
            this.dinoHeads[size].addBox(-1.5F, -2.5F, -4.0F, 3, 3, 4, 0.0F);
            this.dinoHeads[size].setRotationPoint(0.0F, 8.0F, -6.0F);

            this.dinoBodies[size] = new ModelRenderer(this, 14, 169);
            this.dinoBodies[size].addBox(-2.0F, -4.0F, -3.0F, 4, 7, 5, 0.0F);
            this.dinoBodies[size].setRotationPoint(0.0F, 16.0F, 0.0F);

            this.dinoUpperTails[size] = new ModelRenderer(this, 8, 181);
            this.dinoUpperTails[size].addBox(-1.5F, -1.0F, 1.0F, 3, 3, 5, 0.0F);
            this.dinoUpperTails[size].setRotationPoint(0.0F, 16.0F, 0.0F);

            this.dinoMidTails[size] = new ModelRenderer(this, 32, 160);
            this.dinoMidTails[size].addBox(-1.0F, -0.5F, 4.0F, 2, 2, 5, 0.0F);
            this.dinoMidTails[size].setRotationPoint(0.0F, 16.0F, 0.0F);

            this.dinoLowerTails[size] = new ModelRenderer(this, 36, 167);
            this.dinoLowerTails[size].addBox(-0.5F, 2.0F, 8.0F, 1, 1, 4, 0.0F);
            this.dinoLowerTails[size].setRotationPoint(0.0F, 16.0F, 0.0F);

            this.dinoThighRights[size] = new ModelRenderer(this, 34, 172);
            this.dinoThighRights[size].addBox(0.0F, -1.0F, -2.0F, 1, 3, 2);
            this.dinoThighRights[size].setRotationPoint(-2.0F, 19.0F, 1.0F);

            this.dinoThighLefts[size] = new ModelRenderer(this, 34, 172);
            this.dinoThighLefts[size].addBox(0.0F, -1.0F, -2.0F, 1, 3, 2);
            this.dinoThighLefts[size].setRotationPoint(1.0F, 19.0F, 1.0F);

            this.dinoLeftArms[size] = new ModelRenderer(this, 32, 177);
            this.dinoLeftArms[size].addBox(0.0F, 0.0F, -4.0F, 1, 1, 4);
            this.dinoLeftArms[size].setRotationPoint(2.0F, 14.0F, -2.0F);

            this.dinoRightArms[size] = new ModelRenderer(this, 32, 177);
            this.dinoRightArms[size].addBox(-1.0F, 0.0F, -4.0F, 1, 1, 4);
            this.dinoRightArms[size].setRotationPoint(-2.0F, 14.0F, -2.0F);

            this.dinoRightLegs[size] = new ModelRenderer(this, 26, 96);
            this.dinoRightLegs[size].addBox(-1.0F, 0.0F, -3.0F, 3, 5, 3);
            this.dinoRightLegs[size].setRotationPoint(-2.0F, 19.0F, 1.0F);

            this.dinoLeftLegs[size] = new ModelRenderer(this, 26, 96);
            this.dinoLeftLegs[size].addBox(-1.0F, 0.0F, -3.0F, 3, 5, 3);
            this.dinoLeftLegs[size].setRotationPoint(1.0F, 19.0F, 1.0F);
        }
    }

    public ModelRenderer[] getHeadModels(Entity animal) {
        if (!(animal instanceof EntityDnaChicken)) {
            ModelRenderer[] returnArray = {this.head, this.bill, this.chin};
            return returnArray;
        } else {
            EntityDnaChicken chicken = (EntityDnaChicken) animal;
            int headIndex = chicken.getHeadColor();
            int isBigIndex = chicken.isBig() ? 1 : 0;
            isBigIndex = this.isChild ? 2 : isBigIndex;

            if (chicken.hasDinoHead()) {
                ModelRenderer[] returnArray = {this.dinoHeads[isBigIndex],
                        this.dinoUpperNecks[isBigIndex],
                        this.dinoLowerNecks[isBigIndex],
                        this.dinoUpperLips[isBigIndex],
                        this.dinoLowerLips[isBigIndex]};
                return returnArray;
            } else {
                ModelRenderer[] returnArray = {
                        this.heads[isBigIndex][headIndex],
                        this.chins[isBigIndex][headIndex],
                        this.bills[isBigIndex][headIndex]};
                return returnArray;
            }
        }
    }

    /**
     * Get the models that are part of the given animal (right color, dino or not, right size).
     *
     * @param animal
     * @return array containing the models.
     */
    public ModelRenderer[] getBodyModels(Entity animal) {
        if (!(animal instanceof EntityDnaChicken)) {
            ModelRenderer[] returnArray = {this.body};
            return returnArray;
        } else {
            EntityDnaChicken chicken = (EntityDnaChicken) animal;
            int bodyIndex = chicken.getBodyColor();
            int isBigIndex = chicken.isBig() ? 1 : 0;
            isBigIndex = this.isChild ? 2 : isBigIndex;
            if (chicken.hasDinoBody()) {
                ModelRenderer[] returnArray = {this.dinoBodies[isBigIndex],
                        this.dinoUpperTails[isBigIndex],
                        this.dinoMidTails[isBigIndex],
                        this.dinoLowerTails[isBigIndex]};
                return returnArray;
            } else {
                ModelRenderer[] returnArray = {this.bodies[isBigIndex][bodyIndex]};
                return returnArray;
            }
        }
    }

    /**
     * Get the models that are part of the given animal (right color, dino or not, right size).
     *
     * @param animal
     * @return array containing the models.
     */
    public ModelRenderer[] getLegModels(Entity animal) {
        if (!(animal instanceof EntityDnaChicken)) {
            ModelRenderer[] returnArray = {this.leftLeg, this.rightLeg};
            return returnArray;
        } else {
            EntityDnaChicken chicken = (EntityDnaChicken) animal;
            int rightLegIndex = chicken.getRightLegColor();
            int leftLegIndex = chicken.getLeftLegColor();
            int isBigIndex = chicken.isBig() ? 1 : 0;
            isBigIndex = this.isChild ? 2 : isBigIndex;
            if (chicken.hasDinoLeftLeg()) {
                if (chicken.hasDinoRightLeg()) {
                    ModelRenderer[] returnArray = {
                            this.dinoLeftLegs[isBigIndex],
                            this.dinoRightLegs[isBigIndex],
                            this.dinoThighLefts[isBigIndex],
                            this.dinoThighRights[isBigIndex]};
                    return returnArray;
                } else {
                    ModelRenderer[] returnArray = {
                            this.dinoLeftLegs[isBigIndex],
                            this.rightLegs[isBigIndex][rightLegIndex],
                            this.dinoThighLefts[isBigIndex]};
                    return returnArray;
                }
            } else if (chicken.hasDinoRightLeg()) {
                ModelRenderer[] returnArray = {
                        this.dinoRightLegs[isBigIndex],
                        this.leftLegs[isBigIndex][leftLegIndex],
                        this.dinoThighRights[isBigIndex]};
                return returnArray;
            } else {
                ModelRenderer[] returnArray = {
                        this.leftLegs[isBigIndex][leftLegIndex],
                        this.rightLegs[isBigIndex][rightLegIndex]};
                return returnArray;
            }
        }
    }

    /**
     * Get the models that are part of the given animal (right color, dino or not, right size).
     *
     * @param animal
     * @return array containing the models.
     */
    public ModelRenderer[] getWingModels(Entity animal) {
        if (!(animal instanceof EntityDnaChicken)) {
            ModelRenderer[] returnArray = {this.leftWing, this.rightWing};
            return returnArray;
        } else {
            EntityDnaChicken chicken = (EntityDnaChicken) animal;
            int rightWingIndex = chicken.getRightWingColor();
            int leftWingIndex = chicken.getLeftWingColor();
            int isBigIndex = chicken.isBig() ? 1 : 0;
            isBigIndex = this.isChild ? 2 : isBigIndex;
            if (chicken.hasDinoLeftArm()) {
                if (chicken.hasDinoRightArm()) {
                    ModelRenderer[] returnArray = {
                            this.dinoRightArms[isBigIndex],
                            this.dinoLeftArms[isBigIndex]};
                    return returnArray;
                } else {
                    ModelRenderer[] returnArray = {
                            this.dinoLeftArms[isBigIndex],
                            this.rightWings[isBigIndex][rightWingIndex]};
                    return returnArray;
                }
            } else if (chicken.hasDinoRightArm()) {
                ModelRenderer[] returnArray = {
                        this.dinoRightArms[isBigIndex],
                        this.leftWings[isBigIndex][leftWingIndex]};
                return returnArray;
            } else {
                ModelRenderer[] returnArray = {
                        this.leftWings[isBigIndex][leftWingIndex],
                        this.rightWings[isBigIndex][rightWingIndex]};
                return returnArray;
            }
        }
    }
}