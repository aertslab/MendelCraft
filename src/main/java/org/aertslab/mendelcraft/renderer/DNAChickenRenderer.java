package org.aertslab.mendelcraft.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;

import org.aertslab.mendelcraft.ClientSetup;
import org.aertslab.mendelcraft.MendelCraft;
import org.aertslab.mendelcraft.entities.DNAChicken;

import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class DNAChickenRenderer extends MobRenderer<DNAChicken, DNAChickenModel<DNAChicken>>{
	private static final ResourceLocation CHICKEN_LOCATION = new ResourceLocation(MendelCraft.MODID,"textures/entity/chickenvariations.png" );

	public DNAChickenRenderer(Context context) {
		super(context, new DNAChickenModel<>(context.bakeLayer(ClientSetup.DNACHICKENMODEL)), 0.3f);
	}

	@Override
	public ResourceLocation getTextureLocation(DNAChicken p_114482_) {
		return CHICKEN_LOCATION;
	}
	
	protected float getBob(DNAChicken p_114000_, float p_114001_) {
		float f = Mth.lerp(p_114001_, p_114000_.oFlap, p_114000_.flap);
		float f1 = Mth.lerp(p_114001_, p_114000_.oFlapSpeed, p_114000_.flapSpeed);
		return (Mth.sin(f) + 1.0F) * f1;
	}
}
