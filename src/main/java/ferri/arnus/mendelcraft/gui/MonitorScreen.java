package ferri.arnus.mendelcraft.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import ferri.arnus.mendelcraft.MendelCraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MonitorScreen extends AbstractContainerScreen<MonitorMenu>{
	
	private static final ResourceLocation GUI = new ResourceLocation(MendelCraft.MODID, "textures/gui/background.png");

	public MonitorScreen(MonitorMenu p_97741_, Inventory p_97742_, Component p_97743_) {
		super(p_97741_, p_97742_, p_97743_);
	}
	
	@Override
	protected void init() {
		super.init();
		EditBox question = new EditBox(font, width/2, height/2, 300, 20, title);
		addWidget(question);
		question.setFocus(true);
	}

	@Override
	protected void renderBg(PoseStack pMatrixStack, float pPartialTicks, int pMouseX, int pMouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, GUI);
	}
	
	@Override
	public void render(PoseStack p_97795_, int p_97796_, int p_97797_, float p_97798_) {
		this.renderBg(p_97795_, p_97798_, p_97796_, p_97797_);
		super.render(p_97795_, p_97796_, p_97797_, p_97798_);
		this.renderTooltip(p_97795_, p_97796_, p_97797_);
	}
	
}
