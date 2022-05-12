package org.aertslab.mendelcraft.gui.buttons;

import org.aertslab.mendelcraft.MendelCraft;
import org.aertslab.mendelcraft.capability.DNAUtil;
import org.aertslab.mendelcraft.gui.tabs.SyringeTab;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

public class ChromosomeButton extends Button{

	private static final ResourceLocation BUTTON = new ResourceLocation(MendelCraft.MODID, "textures/gui/chromosomebuttons.png");
	private SyringeTab tab;
	private String chromosome;

	public ChromosomeButton(int pX, int pY, OnPress pOnPress, OnTooltip pOnTooltip, SyringeTab tab, String chromosome) {
		super(pX, pY, 20, 20, new TextComponent(""), pOnPress, pOnTooltip);
		this.tab = tab;
		this.chromosome = chromosome;
	}
	
	@Override
	public void renderButton(PoseStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, BUTTON);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
		int i = this.getYImage(this.isHoveredOrFocused());
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		if (this.chromosome.equals(this.tab.getSelectedChromosome())) {
			ContainerScreen.blit(pMatrixStack, this.x, this.y, DNAUtil.getChromosomes().indexOf(chromosome)*20, 0, this.width, this.height, 600, 60);
		}else {
			ContainerScreen.blit(pMatrixStack, this.x, this.y, DNAUtil.getChromosomes().indexOf(chromosome)*20, 20*i, this.width, this.height, 600, 60);
		}
		if (this.isHoveredOrFocused()) {
			this.renderToolTip(pMatrixStack, pMouseX, pMouseY);
	    }
	}


}
