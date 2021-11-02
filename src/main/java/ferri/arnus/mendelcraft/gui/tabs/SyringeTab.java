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
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SyringeTab extends AbstractTab {

	private int slot = 0;
	private String selectedChromosome = DNAUtil.getChromosomes().get(0);
	private int selectedgene = -1;
	private int parentgene = 0;
	private int chromosomerow = 0;
	private int generow = 0;
	private static final ResourceLocation GUI = new ResourceLocation(MendelCraft.MODID, "textures/gui/background.png");
	private LaboratoryScreen screen;
	private String draw = "";

	public SyringeTab(int id, Item icon, int slot, LaboratoryScreen screen) {
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

	public int getSlot() {
		return slot;
	}

	public int getChromosomerow() {
		return chromosomerow;
	}

	public int getGenerow() {
		return generow;
	}

	public void setSelectedgene(int selectedgene) {
		this.selectedgene = selectedgene;
	}

	public void setDraw(String draw) {
		this.draw = draw;
	}
	
	public LaboratoryScreen getScreen() {
		return screen;
	}

	public String getDraw() {
		return draw;
	}

	public void setParentgene(int parentgene) {
		this.parentgene = parentgene;
	}

	@Override
	public void render(PoseStack pPoseStack, float pPartialTicks, int pMouseX, int pMouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, GUI);
		getScreen().getMinecraft().getTextureManager().bindForSetup(GUI);
		int relX = (getScreen().width - getScreen().getXSize()) / 2;
		int relY = (getScreen().height - getScreen().getYSize()) / 2;
		ContainerScreen.blit(pPoseStack, relX, relY, 0, 0, getScreen().getXSize(), getScreen().getYSize(),
				getScreen().getXSize(), getScreen().getYSize());
		if (!this.getDraw().isEmpty()) {
			getScreen().getMinecraft().font.draw(pPoseStack, new TextComponent(getDraw()),
					relX + getScreen().getXSize() / 2 - getScreen().getMinecraft().font.width(getDraw()) / 2,
					relY + getScreen().getYSize() - 30, 0);
		}
	}

	@Override
	public void init(int relX, int relY) {
		getScreen().getMenu().slots.forEach(s -> ((HideableSlot) s).setActive(false));
		getScreen().clearWidgets();
		this.addChrormosomeButtons(relX, relY);
		this.addGeneButtons(this.selectedChromosome, getScreen(), relX, relY);
	}

	public void addChrormosomeButtons(int relX, int relY) {
		ItemStack item = getScreen().getMenu().slots.get(getSlot()).getItem();
		item.getCapability(DNAProvider.DNASTORAGE).ifPresent(cap -> {
			getScreen().addRenderableWidget(new Button(relX + 5, relY + 20, 10, 20,
					new TextComponent("<"), b -> this.decreaseChromosome(b, relX, relY)));
			getScreen().addRenderableWidget(new Button(relX + getScreen().getXSize() - 15,
					relY + 20, 10, 20, new TextComponent(">"), b -> this.increaseChromosome(b, relX, relY)));
			for (int i = chromosomerow * 4; i < Math.min(chromosomerow + 4, DNAUtil.getChromosomes().size()); i++) {
				AtomicReference<String> atom = new AtomicReference<String>(DNAUtil.getChromosomes().get(i));
				getScreen().addRenderableWidget(
						new ChromosomeButton(relX + 20 + 25 * (i % 4), relY + 20, (b) -> setChrome(relX, relY, atom),
								(b, p, x, y) -> getScreen().renderTooltip(p,
										new TranslatableComponent("chromosome.").append(atom.get()), x, y),
								this, DNAUtil.getChromosomes().get(i)));
			}
		});
	}

	public void increaseChromosome(Button b, int relX, int relY) {
		if ((this.chromosomerow + 1) * 4 < DNAUtil.getChromosomes().size()) {
			this.chromosomerow += 1;
		}
		this.init(relX, relY);
	}

	public void decreaseChromosome(Button b, int relX, int relY) {
		if (this.chromosomerow > 0) {
			this.chromosomerow -= 1;
		}
		this.init(relX, relY);
	}

	public void setChrome(int relX, int relY, AtomicReference<String> atom) {
		this.selectedChromosome = atom.get();
		this.setSelectedgene(-1);
		this.setParentgene(0);
		this.setDraw("");
		this.generow = 0;
		this.init(relX, relY);
	}

	public void addGeneButtons(String chromosome, LaboratoryScreen screen, int relX, int relY) {
		ItemStack item = screen.getMenu().slots.get(getSlot()).getItem();
		item.getCapability(DNAProvider.DNASTORAGE).ifPresent(cap -> {
			screen.addRenderableWidget(new Button(relX + 5, relY + 65, 10, 20,
					new TextComponent("<"), b -> this.decreaseGene(b, relX, relY)));
			screen.addRenderableWidget(new Button(relX + screen.getXSize() - 15, relY + 65, 10, 20,
					new TextComponent(">"), b -> this.increaseGene(b, chromosome, relX, relY)));
			for (int i = generow * 4; i < Math.min(generow + 4, DNAUtil.getGeneAmount(chromosome)); i++) {
				AtomicInteger atom = new AtomicInteger(i);
				screen.addRenderableWidget(new GeneButton(relX, relY + 50,
						(b) -> this.drawGenes(b, cap.getGene(chromosome, atom.get()).get(0), atom.get(), 0),
						(b, p, x, y) -> screen.renderTooltip(p,
								new TranslatableComponent("gene").append(" " + atom.get()), x, y),
						this, chromosome, i, 0));
				screen.addRenderableWidget(new GeneButton(relX, relY + 80,
						(b) -> this.drawGenes(b, cap.getGene(chromosome, atom.get()).get(1), atom.get(), 1),
						(b, p, x, y) -> screen.renderTooltip(p,
								new TranslatableComponent("gene").append(" " + atom.get()), x, y),
						this, chromosome, i, 1));
			}
		});
	}

	public void increaseGene(Button b, String chromosome, int relX, int relY) {
		if ((this.generow + 1) * 4 < DNAUtil.getGeneAmount(chromosome)) {
			this.generow += 1;
			this.selectedgene = -1;
		}
		this.init(relX, relY);
	}

	public void decreaseGene(Button b, int relX, int relY) {
		if (this.generow > 0) {
			this.generow -= 1;
			this.selectedgene = -1;
		}
		this.init(relX, relY);
	}

	public void drawGenes(Button b, String gene, int i, int parent) {
		if (gene.isEmpty()) {
			return;
		}
		this.setSelectedgene(i);
		this.setParentgene(parent);
		this.setDraw(gene);
	}
	
	@Override
	public void clear() {
		this.selectedChromosome = DNAUtil.getChromosomes().get(0);
		this.selectedgene = -1;
		this.parentgene = 0;
		this.chromosomerow = 0;
		this.generow = 0;
		this.draw = "";
	}

}
