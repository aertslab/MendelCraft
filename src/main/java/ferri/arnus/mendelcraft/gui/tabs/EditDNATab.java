package ferri.arnus.mendelcraft.gui.tabs;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import ferri.arnus.mendelcraft.capability.DNAProvider;
import ferri.arnus.mendelcraft.capability.DNAUtil;
import ferri.arnus.mendelcraft.gui.LaboratoryContainer;
import ferri.arnus.mendelcraft.gui.LaboratoryScreen;
import ferri.arnus.mendelcraft.gui.buttons.EditButton;
import ferri.arnus.mendelcraft.network.LaboratoryPacket;
import ferri.arnus.mendelcraft.network.MendelCraftChannel;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class EditDNATab extends SyringeTab{
	
	private int geneindex = 0;

	public EditDNATab(int id, Item icon, AbstractContainerScreen<?> screen) {
		super(id, icon, 2, screen);
	}
	
	@Override
	public void addGeneButtons(String chromosome, AbstractContainerScreen<?> screen, int relX, int relY) {
		ItemStack item = screen.getMenu().slots.get(getSlot()).getItem();
		item.getCapability(DNAProvider.DNASTORAGE).ifPresent(cap -> {
			((LaboratoryScreen)screen).addRenderableWidget(new Button(relX + 5, relY+65, 10, 20, new TextComponent("<"), b -> this.decreaseGene(b, relX, relY)));
			((LaboratoryScreen)screen).addRenderableWidget(new Button(relX + screen.getXSize() -15, relY+65, 10, 20, new TextComponent(">"), b -> this.increaseGene(b, chromosome, relX, relY)));
			for (int i = getGenerow()*4; i < Math.min(getGenerow() + 4, DNAUtil.getGeneAmount(chromosome)); i++) {
				AtomicInteger atom = new AtomicInteger(i);
				((LaboratoryScreen)screen).addRenderableWidget(new EditButton(relX, relY + 50, (b) -> this.ChangeGene(screen, relX, relY, b, cap.getGene(chromosome, atom.get()).get(0), atom.get(), 0), (b,p,x,y) -> screen.renderTooltip(p, new TranslatableComponent("gene").append(" " +atom.get()), x, y), this, chromosome, i%4, 0));
				((LaboratoryScreen)screen).addRenderableWidget(new EditButton(relX, relY + 80, (b) -> this.ChangeGene(screen, relX, relY, b, cap.getGene(chromosome, atom.get()).get(1), atom.get(), 1), (b,p,x,y) -> screen.renderTooltip(p, new TranslatableComponent("gene").append(" " +atom.get()), x, y), this, chromosome, i%4, 1));
			}
		});
	}
	
	public void ChangeGene(AbstractContainerScreen<?> screen, int relX, int relY, Button b, String gene, int i, int parent) {
		if (gene.isEmpty()) {
			return;
		}
		ItemStack item = screen.getMenu().slots.get(getSlot()).getItem();
		this.setSelectedgene(i);
		this.setParentgene(parent);
		item.getCapability(DNAProvider.DNASTORAGE).ifPresent(cap -> {
			this.geneindex = DNAUtil.getPossibleGenes(getSelectedChromosome(), getSelectedgene()).indexOf(cap.getGene(getSelectedChromosome(), getSelectedgene()).get(getParentgene()));
		});
		((LaboratoryScreen)screen).addRenderableWidget(new Button(relX + 50, relY + screen.getYSize() - 30, 10, 20, new TextComponent("<"), b2 -> this.decreaseIndex(b2, relX, relY)));
		((LaboratoryScreen)screen).addRenderableWidget(new Button(relX + screen.getXSize() -60, relY + screen.getYSize() - 30, 10, 20, new TextComponent(">"), b2 -> this.increaseIndex(b2, relX, relY)));
		this.setDraw(gene);
	}
	
	public void increaseIndex(Button b, int relX, int relY) {
		if (this.geneindex < DNAUtil.getPossibleGenes(getSelectedChromosome(), getSelectedgene()).size()-1) {
			this.geneindex +=1;
		}else {
			this.geneindex = 0;
		}
		this.setDraw(DNAUtil.getPossibleGenes(getSelectedChromosome(), getSelectedgene()).get(geneindex));
		getScreen().getMenu().slots.get(getSlot()).getItem().getCapability(DNAProvider.DNASTORAGE).ifPresent(c -> {
			List<String> gene = c.getGene(getSelectedChromosome(), getSelectedgene());
			gene.set(this.getParentgene(), getDraw());
			c.setGene(getSelectedChromosome(), getSelectedgene(), gene);
			MendelCraftChannel.INSTANCE.sendToServer(new LaboratoryPacket(getSlot(), c.serializeNBT(), ((LaboratoryContainer)getScreen().getMenu()).getPos()));
		});
	}
	
	public void decreaseIndex(Button b, int relX, int relY) {
		if (this.geneindex > 0) {
			this.geneindex -=1;
		}else {
			this.geneindex = DNAUtil.getPossibleGenes(getSelectedChromosome(), getSelectedgene()).size()-1;
		}
		this.setDraw(DNAUtil.getPossibleGenes(getSelectedChromosome(), getSelectedgene()).get(geneindex));
		getScreen().getMenu().slots.get(getSlot()).getItem().getCapability(DNAProvider.DNASTORAGE).ifPresent(c -> {
			List<String> gene = c.getGene(getSelectedChromosome(), getSelectedgene());
			gene.set(this.getParentgene(), getDraw());
			c.setGene(getSelectedChromosome(), getSelectedgene(), gene);
			MendelCraftChannel.INSTANCE.sendToServer(new LaboratoryPacket(getSlot(), c.serializeNBT(), ((LaboratoryContainer)getScreen().getMenu()).getPos()));
		});
	}
	
	@Override
	public void clear() {
		super.clear();
		this.geneindex = 0;
	}

}
