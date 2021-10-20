package ferri.arnus.mendelcraft.gui.tabs;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import ferri.arnus.mendelcraft.MendelCraft;
import ferri.arnus.mendelcraft.gui.LaboratoryScreen;
import ferri.arnus.mendelcraft.gui.slots.HideableSlot;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class InventoryTab extends AbstractTab{
	
	private static final ResourceLocation GUI = new ResourceLocation(MendelCraft.MODID, "textures/gui/labocontainer.png");
	private AbstractContainerScreen<?> screen;

	public InventoryTab(int id, Item icon, AbstractContainerScreen<?> screen) {
		super(id, icon);
		this.screen = screen;
	}

	@Override
	public void render(PoseStack pPoseStack, float pPartialTicks, int pMouseX, int pMouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, GUI);
		screen.getMinecraft().getTextureManager().bindForSetup(GUI);
        int relX = (screen.width - screen.getXSize()) / 2;
        int relY = (screen.height - screen.getYSize()) / 2;
        ContainerScreen.blit(pPoseStack,relX, relY, 0, 0, screen.getXSize(), screen.getYSize(), screen.getXSize(), screen.getYSize());
	}

	@Override
	public void init(int relX, int relY) {
		screen.getMenu().slots.forEach(s -> ((HideableSlot) s).setActive(true));
        ((LaboratoryScreen)screen).clearWidgets();
	}
}
