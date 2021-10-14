package ferri.arnus.mendelcraft.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import ferri.arnus.mendelcraft.MendelCraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class LaboratoryScreen extends AbstractContainerScreen<LaboratoryContainer>{

	private static final ResourceLocation GUI = new ResourceLocation(MendelCraft.MODID, "textures/gui/labocontainer.png");

	public LaboratoryScreen(LaboratoryContainer pMenu, Inventory pPlayerInventory, Component pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
	}
	
	@Override
	public void render(PoseStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
		this.renderBg(pMatrixStack, pPartialTicks, pMouseX, pMouseY);
		super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
		this.renderTooltip(pMatrixStack, pMouseX, pMouseY);
	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTicks, int pMouseX, int pMouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, GUI);
        this.minecraft.getTextureManager().bindForSetup(GUI);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        LaboratoryScreen.blit(pPoseStack,relX, relY, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
	}

}
