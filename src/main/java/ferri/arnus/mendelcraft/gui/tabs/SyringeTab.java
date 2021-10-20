package ferri.arnus.mendelcraft.gui.tabs;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import ferri.arnus.mendelcraft.MendelCraft;
import ferri.arnus.mendelcraft.capability.DNAProvider;
import ferri.arnus.mendelcraft.capability.DNAUtil;
import ferri.arnus.mendelcraft.gui.LaboratoryScreen;
import ferri.arnus.mendelcraft.gui.buttons.ChromosomeButton;
import ferri.arnus.mendelcraft.gui.buttons.GeneButton;
import ferri.arnus.mendelcraft.gui.slots.HideableSlot;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SyringeTab extends AbstractTab{

	private int slot = 0;
	private String selectedChromosome = DNAUtil.getChromosomes().get(0);
	private int selectedgene = -1;
	private int parentgene = 0;
	private static final ResourceLocation GUI = new ResourceLocation(MendelCraft.MODID, "textures/gui/background.png");
	private AbstractContainerScreen<?> screen;
	private String draw = "";
	
	public SyringeTab(int id, Item icon, int slot, AbstractContainerScreen<?> screen) {
		super(id, icon);
		this.slot = slot;
		this.screen = screen;
	}
	
	public String getSelectedChromosome() {
		return selectedChromosome;
	}
	
	public int getSelectedgene() {
		return selectedgene;
	}
	
	public int getParentgene() {
		return parentgene;
	}

	@Override
	public void render(PoseStack pPoseStack, float pPartialTicks, int pMouseX,
			int pMouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, GUI);
		screen.getMinecraft().getTextureManager().bindForSetup(GUI);
        int relX = (screen.width - screen.getXSize()) / 2;
        int relY = (screen.height - screen.getYSize()) / 2;
        ContainerScreen.blit(pPoseStack,relX, relY, 0, 0, screen.getXSize(), screen.getYSize(), screen.getXSize(), screen.getYSize());
        if (!this.draw.isEmpty()) {
        	screen.getMinecraft().font.draw(pPoseStack, new TextComponent(draw), relX + screen.getXSize()/2 - screen.getMinecraft().font.width(draw) / 2, relY + screen.getYSize() - 30, 0);
        }
	}

	@Override
	public void init(int relX, int relY) {
		screen.getMenu().slots.forEach(s -> ((HideableSlot) s).setActive(false));
        ((LaboratoryScreen)screen).clearWidgets();
        this.addChrormosomeButtons(relX, relY);
        this.addGeneButtons(this.selectedChromosome, screen, relX, relY);
	}
	
	public void addChrormosomeButtons(int relX, int relY) {
		ItemStack item = screen.getMenu().slots.get(slot).getItem();
		item.getCapability(DNAProvider.DNASTORAGE).ifPresent(cap -> {
			for (int i = 0; i < DNAUtil.getChromosomes().size(); i++) {
				AtomicReference<String> atom = new AtomicReference<String>(DNAUtil.getChromosomes().get(i));
				((LaboratoryScreen)screen).addRenderableWidget(new ChromosomeButton(relX + 20 + 25*i, relY + 20, (b) -> setChrome(relX, relY, atom), Button.NO_TOOLTIP, this, DNAUtil.getChromosomes().get(i)));
			}
		});
	}

	public void setChrome(int relX, int relY, AtomicReference<String> atom) {
		this.selectedChromosome = atom.get();
		this.selectedgene = -1;
		this.parentgene = 0;
		this.draw = "";
		this.init(relX, relY);
	}
	
	public void addGeneButtons(String chromosome, AbstractContainerScreen<?> screen, int relX, int relY) {
		ItemStack item = screen.getMenu().slots.get(slot).getItem();
		item.getCapability(DNAProvider.DNASTORAGE).ifPresent(cap -> {
			for (int i=0; i<4; i++) {
				AtomicInteger atom = new AtomicInteger(i);
				((LaboratoryScreen)screen).addRenderableWidget(new GeneButton(relX, relY + 50, (b) -> this.drawGenes(b,cap.getGene(chromosome, atom.get()).get(0), atom.get(), 0), Button.NO_TOOLTIP, this, chromosome, i, 0));
				((LaboratoryScreen)screen).addRenderableWidget(new GeneButton(relX, relY + 80, (b) -> this.drawGenes(b,cap.getGene(chromosome, atom.get()).get(1), atom.get(), 1), Button.NO_TOOLTIP, this, chromosome, i, 1));
			}
		});
	}
	
	public void drawGenes(Button b, String gene, int i, int parent) {
		if (gene.isEmpty()) {
			return;
		}
		this.selectedgene = i;
		this.parentgene = parent;
		this.draw  = gene;
	}

}
