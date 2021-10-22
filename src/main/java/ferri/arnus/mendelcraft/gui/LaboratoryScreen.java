package ferri.arnus.mendelcraft.gui;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import ferri.arnus.mendelcraft.gui.tabs.AbstractTab;
import ferri.arnus.mendelcraft.gui.tabs.EditDNATab;
import ferri.arnus.mendelcraft.gui.tabs.InventoryTab;
import ferri.arnus.mendelcraft.gui.tabs.SyringeTab;
import ferri.arnus.mendelcraft.items.ItemRegistry;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class LaboratoryScreen extends AbstractContainerScreen<LaboratoryContainer>{

	private static final ResourceLocation CREATIVE_TABS_LOCATION = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
	private int selectedtab = 0;
	private List<AbstractTab> tabs = new ArrayList<>();

	public LaboratoryScreen(LaboratoryContainer pMenu, Inventory pPlayerInventory, Component pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
		tabs.add(new InventoryTab(0, Items.CHEST, this));
		tabs.add(new SyringeTab(1, ItemRegistry.DNASYRINGE.get(),0, this));
		tabs.add(new SyringeTab(2, ItemRegistry.DNASYRINGE.get(),1, this));
		tabs.add(new EditDNATab(3, ItemRegistry.DNACHICKENSPAWNEGG.get(), this));
	}
	
	@Override
	public void render(PoseStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
		this.renderBg(pMatrixStack, pPartialTicks, pMouseX, pMouseY);
		super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
		this.renderTooltip(pMatrixStack, pMouseX, pMouseY);
	}
	
	@Override
	public <T extends GuiEventListener & Widget & NarratableEntry> T addRenderableWidget(T pWidget) {
		return super.addRenderableWidget(pWidget);
	}
	
	@Override
	public void clearWidgets() {
		super.clearWidgets();
	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTicks, int pMouseX, int pMouseY) {
		
		for (AbstractTab laboratorytab : tabs) {
			if (laboratorytab.getId() != selectedtab) {
				RenderSystem.setShader(GameRenderer::getPositionTexShader);
				RenderSystem.setShaderTexture(0, CREATIVE_TABS_LOCATION);
				this.renderTabButton(pPoseStack, laboratorytab);
			}
		}
		
		tabs.get(selectedtab).render(pPoseStack, pPartialTicks, pMouseX, pMouseY);
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, CREATIVE_TABS_LOCATION);
		this.renderTabButton(pPoseStack, tabs.get(selectedtab));
	}
	
	@Override
	protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
		if (tabs.get(selectedtab) instanceof InventoryTab) {
			super.renderLabels(pPoseStack, pMouseX, pMouseY);
		}
	}
	
	@Override
	public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
		double d0 = pMouseX - (double)this.leftPos;
        double d1 = pMouseY - (double)this.topPos;
		for (AbstractTab laboratorytab: tabs) {
			if (this.checkTabClicked(laboratorytab, d0, d1) && this.selectedtab != laboratorytab.getId()) {
				laboratorytab.clear();
				this.selectedtab = laboratorytab.getId();
				laboratorytab.init((this.width - this.getXSize()) / 2, (this.height - this.getYSize()) / 2 + 20);
			}
		}
		return super.mouseReleased(pMouseX, pMouseY, pButton);
	}
	
	protected boolean checkTabClicked(AbstractTab laboratorytab, double pRelativeMouseX, double pRelativeMouseY) {
		int i = laboratorytab.getId();
		int j = 28 * i;
		int k = 0;
		j += i;
		k = k - 32;
		
		return pRelativeMouseX >= (double)j && pRelativeMouseX <= (double)(j + 28) && pRelativeMouseY >= (double)k && pRelativeMouseY <= (double)(k + 32);
	}
	
	protected void renderTabButton(PoseStack pPoseStack, AbstractTab laboratorytab) {
		boolean flag = laboratorytab.getId() == selectedtab;
		int i = laboratorytab.getId();
		int j = i * 28;
		int k = 0;
		int l = this.leftPos + 28 * i;
		int i1 = this.topPos;
		if (flag) {
			k += 32;
		}
		l += i;
		i1 = i1 - 28;
		
		RenderSystem.enableBlend();
		this.blit(pPoseStack, l, i1, j, k, 28, 32);
		this.itemRenderer.blitOffset = 100.0F;
		l = l + 6;
		i1 = i1 + 8 - 1;
		ItemStack itemstack = new ItemStack(laboratorytab.getIcon());
		this.itemRenderer.renderAndDecorateItem(itemstack, l, i1);
		this.itemRenderer.renderGuiItemDecorations(this.font, itemstack, l, i1);
		this.itemRenderer.blitOffset = 0.0F;
	}

}
