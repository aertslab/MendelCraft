package org.aertslab.mendelcraft.renderer;

import java.awt.Color;

import org.aertslab.mendelcraft.capability.DNAUtil;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;

public class DNAChickenModel<T extends Entity> extends AgeableListModel<T>{
	public static final String RED_THING = "red_thing";
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart rightLeg;
	private final ModelPart leftLeg;
	private final ModelPart rightWing;
	private final ModelPart leftWing;
	private final ModelPart beak;
	private final ModelPart redThing;
	
	private final ModelPart dino_lip_lower;
	private final ModelPart dino_lip_upper;
	private final ModelPart dino_neck_lower;
	private final ModelPart dino_neck_upper;
	private final ModelPart dino_head;
	private final ModelPart dino_body;
	private final ModelPart dino_tail_lower;
	private final ModelPart dino_tail_mid;
	private final ModelPart dino_tail_upper;
	private final ModelPart dino_thigh_rigth;
	private final ModelPart dino_thigh_left;
	private final ModelPart dino_arm_right;
	private final ModelPart dino_arm_left;
	private final ModelPart dino_leg_right;
	private final ModelPart dino_leg_left;
	private float strange = 1;
	private T chicken;
	private AABB box;
		
	
	public DNAChickenModel(ModelPart p_170490_) {
		this.head = p_170490_.getChild("head");
		this.beak = p_170490_.getChild("beak");
		this.redThing = p_170490_.getChild("red_thing");
		this.body = p_170490_.getChild("body");
		this.rightLeg = p_170490_.getChild("right_leg");
		this.leftLeg = p_170490_.getChild("left_leg");
		this.rightWing = p_170490_.getChild("right_wing");
		this.leftWing = p_170490_.getChild("left_wing");
		
		this.dino_lip_lower = p_170490_.getChild("dino_lip_lower");
		this.dino_lip_upper = p_170490_.getChild("dino_lip_upper");
		this.dino_neck_lower = p_170490_.getChild("dino_neck_lower");
		this.dino_neck_upper = p_170490_.getChild("dino_neck_upper");
		this.dino_head = p_170490_.getChild("dino_head");
		this.dino_body = p_170490_.getChild("dino_body");
		this.dino_tail_lower = p_170490_.getChild("dino_tail_lower");
		this.dino_tail_mid = p_170490_.getChild("dino_tail_mid");
		this.dino_tail_upper = p_170490_.getChild("dino_tail_upper");
		this.dino_thigh_rigth = p_170490_.getChild("dino_thigh_rigth");
		this.dino_thigh_left = p_170490_.getChild("dino_thigh_left");
		this.dino_arm_right = p_170490_.getChild("dino_arm_right");
		this.dino_arm_left = p_170490_.getChild("dino_arm_left");
		this.dino_leg_right = p_170490_.getChild("dino_leg_right");
		this.dino_leg_left = p_170490_.getChild("dino_leg_left");
		
	}
	
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		
		partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 6.0F, 3.0F), PartPose.offset(0.0F, 15.0F, -4.0F));
		partdefinition.addOrReplaceChild("beak", CubeListBuilder.create().texOffs(14, 0).addBox(-2.0F, -4.0F, -4.0F, 4.0F, 2.0F, 2.0F), PartPose.offset(0.0F, 15.0F, -4.0F));
		partdefinition.addOrReplaceChild("red_thing", CubeListBuilder.create().texOffs(14, 4).addBox(-1.0F, -2.0F, -3.0F, 2.0F, 2.0F, 2.0F), PartPose.offset(0.0F, 15.0F, -4.0F));
		partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 9).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 8.0F, 6.0F), PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, ((float)Math.PI / 2F), 0.0F, 0.0F));
		CubeListBuilder cubelistbuilder = CubeListBuilder.create().texOffs(26, 0).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F);
		partdefinition.addOrReplaceChild("right_leg", cubelistbuilder, PartPose.offset(-2.0F, 19.0F, 1.0F));
		partdefinition.addOrReplaceChild("left_leg", cubelistbuilder, PartPose.offset(1.0F, 19.0F, 1.0F));
		partdefinition.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(24, 13).addBox(0.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F), PartPose.offset(-4.0F, 13.0F, 0.0F));
		partdefinition.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(24, 13).addBox(-1.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F), PartPose.offset(4.0F, 13.0F, 0.0F));
		
		partdefinition.addOrReplaceChild("dino_lip_lower", CubeListBuilder.create().texOffs(0, 173).addBox(-0.5F, -1.0F, -6.0F, 1F, 1F, 4), PartPose.offset(0.0F, 8.0F, -6.0F));
		partdefinition.addOrReplaceChild("dino_lip_upper", CubeListBuilder.create().texOffs(0, 167).addBox(-1.0F, -2.0F, -7.0F, 2, 1, 5), PartPose.offset(0.0F, 8.0F, -6.0F));
		partdefinition.addOrReplaceChild("dino_neck_lower", CubeListBuilder.create().texOffs(14, 160).addBox(-1.5F, -3.5F, -4.0F, 3, 3, 6), PartPose.offset(0.0F, 15.0F, -4.0F));
		partdefinition.addOrReplaceChild("dino_neck_upper", CubeListBuilder.create().texOffs(0, 178).addBox(-1.0F, -8.0F, -0.0F, 2, 8, 2), PartPose.offset(0.0F, 15.0F, -4.0F));
		partdefinition.addOrReplaceChild("dino_head", CubeListBuilder.create().texOffs(0, 160).addBox(-1.5F, -2.5F, -4.0F, 3, 3, 4), PartPose.offset(0.0F, 8.0F, -6.0F));
		partdefinition.addOrReplaceChild("dino_body", CubeListBuilder.create().texOffs(14, 169).addBox(-2.0F, -4.0F, -3.0F, 4, 7, 5), PartPose.offset(0.0F, 16.0F, 0.0F));
		partdefinition.addOrReplaceChild("dino_tail_lower", CubeListBuilder.create().texOffs(36, 167).addBox(-0.5F, 2.0F, 8.0F, 1, 1, 4), PartPose.offset(0.0F, 16.0F, 0.0F));
		partdefinition.addOrReplaceChild("dino_tail_mid", CubeListBuilder.create().texOffs(32, 160).addBox(-1.0F, -0.5F, 4.0F, 2, 2, 5), PartPose.offset(0.0F, 16.0F, 0.0F));
		partdefinition.addOrReplaceChild("dino_tail_upper", CubeListBuilder.create().texOffs(8, 181).addBox(-1.5F, -1.0F, 1.0F, 3, 3, 5), PartPose.offset(0.0F, 16.0F, 0.0F));
		partdefinition.addOrReplaceChild("dino_thigh_rigth", CubeListBuilder.create().texOffs(34, 172).addBox(0.0F, -1.0F, -2.0F, 1, 3, 2), PartPose.offset(-2.0F, 19.0F, 1.0F));
		partdefinition.addOrReplaceChild("dino_thigh_left", CubeListBuilder.create().texOffs(34, 172).addBox(0.0F, -1.0F, -2.0F, 1, 3, 2), PartPose.offset(1.0F, 19.0F, 1.0F));
		partdefinition.addOrReplaceChild("dino_arm_right", CubeListBuilder.create().texOffs(32, 177).addBox(-1.0F, 0.0F, -4.0F, 1, 1, 4), PartPose.offset(-2.0F, 14.0F, -2.0F));
		partdefinition.addOrReplaceChild("dino_arm_left", CubeListBuilder.create().texOffs(32, 177).addBox(-1.0F, 0.0F, -4.0F, 1, 1, 4), PartPose.offset(2.0F, 14.0F, -2.0F));
		partdefinition.addOrReplaceChild("dino_leg_right", CubeListBuilder.create().texOffs(26, 96).addBox(-1.0F, 0.0F, -3.0F, 3, 5, 3), PartPose.offset(-2.0F, 19.0F, 1.0F));
		partdefinition.addOrReplaceChild("dino_leg_left", CubeListBuilder.create().texOffs(26, 96).addBox(-1.0F, 0.0F, -3.0F, 3, 5, 3), PartPose.offset(1.0F, 19.0F, 1.0F));

		return LayerDefinition.create(meshdefinition, 64, 192);
	}
	
	@Override
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of(this.head, this.beak, this.redThing);
	}
	
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(this.body, this.rightWing, this.leftWing);
	}
	
	protected Iterable<ModelPart> legParts() {
		return ImmutableList.of(this.rightLeg, this.leftLeg);
	}
	
	protected Iterable<ModelPart> dinoParts() {
		return ImmutableList.of(this.dino_lip_lower,this.dino_lip_upper,this.dino_neck_lower ,this.dino_neck_upper,
		this.dino_head ,this.dino_body ,this.dino_tail_lower,this.dino_tail_mid ,this.dino_tail_upper ,this.dino_thigh_rigth,
		this.dino_thigh_left ,this.dino_arm_right ,this.dino_arm_left ,this.dino_leg_right ,this.dino_leg_left);
	}
	
	@Override
	public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		this.head.xRot = pHeadPitch * 0.017453292F * strange  * 0.8F - (float) (-10. / 180 * Math.PI) + (strange - 1);
		this.head.yRot = pNetHeadYaw * 0.017453292F * strange * strange * strange - (strange - 1);
		this.beak.xRot = this.head.xRot;
		this.beak.yRot = this.head.yRot;
		this.redThing.xRot = this.head.xRot;
		this.redThing.yRot = this.head.yRot;
		this.rightLeg.xRot = Mth.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount;
		this.leftLeg.xRot = Mth.cos(pLimbSwing * 0.6662F + (float)Math.PI) * 1.4F * pLimbSwingAmount;
		this.rightWing.zRot = pAgeInTicks;
		this.leftWing.zRot = -pAgeInTicks;
		
		this.dino_head.xRot = pHeadPitch * 0.017453292F * strange  * 0.8F - (float) (-10. / 180 * Math.PI) + (strange - 1);
		this.dino_head.yRot = pNetHeadYaw * 0.017453292F * strange * strange * strange - (strange - 1);
		this.dino_lip_lower.xRot = this.dino_head.xRot + (float) (7. / 180 * Math.PI);
		this.dino_lip_lower.yRot = this.dino_head.yRot;
		this.dino_lip_upper.xRot = this.dino_head.xRot;
		this.dino_lip_upper.yRot = this.dino_head.yRot;
		this.dino_neck_lower.xRot = (float) (-45. / 180 * Math.PI);
		this.dino_neck_upper.xRot = (float) (29. / 180 * Math.PI);
		this.dino_body.xRot = (float) (50. / 180 * Math.PI);
		this.dino_tail_upper.xRot = (float) (-14. / 180 * Math.PI);
		this.dino_tail_mid.xRot = (float) (-10. / 180 * Math.PI);
		this.dino_tail_lower.xRot = (float) (8. / 180 * Math.PI);
		this.dino_arm_left.xRot = (float) (23. / 180 * Math.PI);
		this.dino_arm_right.xRot = (float) (23. / 180 * Math.PI);
		this.dino_thigh_rigth.xRot = this.rightLeg.xRot;
		this.dino_thigh_left.xRot = this.leftLeg.xRot;
		this.dino_leg_right.xRot = this.rightLeg.xRot;
		this.dino_leg_left.xRot = this.leftLeg.xRot;

	}
	
	@Override
	public void prepareMobModel(T p_102614_, float p_102615_, float p_102616_, float p_102617_) {
		super.prepareMobModel(p_102614_, p_102615_, p_102616_, p_102617_);
		this.chicken = p_102614_;
		this.strange = DNAUtil.isStrange(p_102614_)? 1.751728F : 1;
		this.box = new AABB(chicken.position().x - 0.74, chicken.position().y, chicken.position().z - 0.74D, chicken.position().x + 0.74D, chicken.position().y + 2.59, chicken.position().z + 0.74D);
	}
	
	@Override
	public void renderToBuffer(PoseStack pMatrixStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		pMatrixStack.pushPose();
		if (DNAUtil.isBig(chicken)) {
			chicken.setBoundingBox(box);
			pMatrixStack.scale(3.7F, 3.7F, 3.7F);
			pMatrixStack.translate(0, -1.1D, 0);
		}
		if (DNAUtil.isDinoHead(chicken)) {
			this.dino_head.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay);
			this.dino_lip_lower.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay);
			this.dino_lip_upper.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay);
			this.dino_neck_upper.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay);
 			this.dino_tail_lower.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay);
			this.dino_tail_mid.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay);
		} else {
			Color color = new Color(DNAUtil.getHeadColor(chicken));
			this.head.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, color.getRed()/255F, color.getGreen()/255F, color.getBlue()/255F, color.getAlpha()/255F);
			this.beak.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay);
			this.redThing.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay);
		}
		if (DNAUtil.isDinoBody(chicken)) {
			this.dino_body.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay);
			this.dino_tail_upper.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay);
			this.dino_thigh_left.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay);
			this.dino_thigh_rigth.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay);
		} else {
			Color color = new Color(DNAUtil.getBodyColor(chicken));
			this.body.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, color.getRed()/255F, color.getGreen()/255F, color.getBlue()/255F, color.getAlpha()/255F);
		}
		if (DNAUtil.isDinoLegs(chicken)) {
			this.dino_leg_left.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay);
			this.dino_leg_right.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay);
			this.dino_thigh_left.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay);
			this.dino_thigh_rigth.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay);
		} else {
			//Color color = new Color(DNAUtil.getBodyColor(chicken));
			this.legParts().forEach(part -> part.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay));
		}
		if (DNAUtil.isDinoWings(chicken)) {
			this.dino_arm_left.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay);
			this.dino_arm_right.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay);
		} else {
			Color color = new Color(DNAUtil.getBodyColor(chicken));
			this.leftWing.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, color.getRed()/255F, color.getGreen()/255F, color.getBlue()/255F, color.getAlpha()/255F);
			this.rightWing.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, color.getRed()/255F, color.getGreen()/255F, color.getBlue()/255F, color.getAlpha()/255F);
		}
		pMatrixStack.popPose();
	}
}
