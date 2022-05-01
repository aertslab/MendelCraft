package ferri.arnus.mendelcraft.gui.buttons;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import ferri.arnus.mendelcraft.MendelCraft;
import ferri.arnus.mendelcraft.gui.tabs.SyringeTab;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

public class GeneButton extends Button{

	private ResourceLocation button = new ResourceLocation(MendelCraft.MODID, "textures/gui/dnabutton.png");
	private SyringeTab tab;
	public String chromosome;
	public int gene = 0;
	private int parent = 0;

	public GeneButton(int pX, int pY, OnPress pOnPress, OnTooltip pOnTooltip, SyringeTab tab, String chromosome, int gene, int parent) {
		super(pX + 34*(gene%4) + 20, pY, 34, 20, new TextComponent(""), pOnPress, pOnTooltip);
		this.tab = tab;
		this.chromosome = chromosome;
		this.gene = gene;
		this.parent = parent;
	}
	
	public ResourceLocation getButtonTexture() {
		return button;
	}
	
	@Override
	public void render(PoseStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
		this.visible = true;
		if (!this.chromosome.equals(this.tab.getSelectedChromosome())){
			this.visible = false;
		}
		super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
	}
	
	@Override
	public void renderButton(PoseStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, getButtonTexture());
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
		int i = this.getYImage(this.isHoveredOrFocused());
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		if (tab.getSelectedgene() == this.gene && tab.getParentgene() == this.parent) {
			ContainerScreen.blit(pMatrixStack, this.x, this.y, 0, 0, this.width / 2, 20, 34, 60);
			ContainerScreen.blit(pMatrixStack, this.x + this.width / 2, this.y, 34 - this.width / 2, 0, this.width / 2, this.height, 34, 60);
		}else {
			ContainerScreen.blit(pMatrixStack, this.x, this.y, 0, 20*i, this.width / 2, 20, 34, 60);
			ContainerScreen.blit(pMatrixStack, this.x + this.width / 2, this.y, 34 - this.width / 2, 20*i, this.width / 2, this.height, 34, 60);
		}
		if (this.isHoveredOrFocused()) {
			this.renderToolTip(pMatrixStack, pMouseX, pMouseY);
	    }
	}


}
