package ferri.arnus.mendelcraft.gui.tabs;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import ferri.arnus.mendelcraft.capability.DNAProvider;
import ferri.arnus.mendelcraft.capability.DNAUtil;
import ferri.arnus.mendelcraft.gui.LaboratoryScreen;
import ferri.arnus.mendelcraft.gui.buttons.EditButton;
import ferri.arnus.mendelcraft.items.ItemRegistry;
import ferri.arnus.mendelcraft.network.LaboratoryPacket;
import ferri.arnus.mendelcraft.network.MendelCraftChannel;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class EditDNATab extends SyringeTab{
	
	private int geneindex = 0;
	
	public EditDNATab(int id, Item icon, LaboratoryScreen screen) {
		super(id, icon, 2, screen);
	}
	
	@Override
	public void addGeneButtons(String chromosome, LaboratoryScreen screen, int relX, int relY) {
		ItemStack item = screen.getMenu().slots.get(getSlot()).getItem();
		getScreen().addRenderableWidget(new Button(relX + 20, relY -10, getScreen().getYSize() - 40 , 20 , new TranslatableComponent("button.mendelcraft.makeegg"), b -> cloneChicken(relX, relY)));
		item.getCapability(DNAProvider.DNASTORAGE).ifPresent(cap -> {
			screen.addRenderableWidget(new Button(relX + 5, relY+65, 10, 20, new TextComponent("<"), b -> this.decreaseGene(b, relX, relY)));
			screen.addRenderableWidget(new Button(relX + screen.getXSize() -15, relY+65, 10, 20, new TextComponent(">"), b -> this.increaseGene(b, chromosome, relX, relY)));
			for (int i = getGenerow()*4; i < Math.min(getGenerow() + 4, DNAUtil.getGeneAmount(chromosome)); i++) {
				AtomicInteger atom = new AtomicInteger(i);
				screen.addRenderableWidget(new EditButton(relX, relY + 50, (b) -> this.ChangeGene(screen, relX, relY, b, cap.getGene(chromosome, atom.get()).get(0), atom.get(), 0), (b,p,x,y) -> screen.renderTooltip(p, new TranslatableComponent("gene").append(" " +atom.get()), x, y), this, chromosome, i%4, 0));
				screen.addRenderableWidget(new EditButton(relX, relY + 80, (b) -> this.ChangeGene(screen, relX, relY, b, cap.getGene(chromosome, atom.get()).get(1), atom.get(), 1), (b,p,x,y) -> screen.renderTooltip(p, new TranslatableComponent("gene").append(" " +atom.get()), x, y), this, chromosome, i%4, 1));
			}
		});
	}
	
	public void cloneChicken(int relX, int relY) {
		NonNullList<Slot> slots = this.getScreen().getMenu().slots;
		if (!slots.get(0).getItem().isEmpty() && !slots.get(1).getItem().isEmpty()) {
			ItemStack stack = new ItemStack(ItemRegistry.DNACHICKENSPAWNEGG.get());
			stack.getCapability(DNAProvider.DNASTORAGE).ifPresent(cap2 -> {
				slots.get(1).getItem().getCapability(DNAProvider.DNASTORAGE).ifPresent(cap1 -> {
					slots.get(0).getItem().getCapability(DNAProvider.DNASTORAGE).ifPresent(cap0 -> {
						Random random = new Random();
						cap2.setChromosomes(cap1.getChromosomes().get(random.nextInt(2)), cap0.getChromosomes().get(random.nextInt(2)));
						cap2.setEmpty(false);
						slots.get(2).set(stack);
						MendelCraftChannel.INSTANCE.sendToServer(new LaboratoryPacket(2, slots.get(2).getItem() ,cap2.serializeNBT() ,getScreen().getMenu().getPos()));
					});
				});
			});
		} else if (!slots.get(0).getItem().isEmpty()) {
			ItemStack stack = new ItemStack(ItemRegistry.DNACHICKENSPAWNEGG.get());
			stack.getCapability(DNAProvider.DNASTORAGE).ifPresent(cap2 -> {
				slots.get(0).getItem().getCapability(DNAProvider.DNASTORAGE).ifPresent(cap0 -> {
					cap2.setChromosomes(cap0.getChromosomes());
					cap2.setEmpty(false);
					slots.get(2).set(stack);
					MendelCraftChannel.INSTANCE.sendToServer(new LaboratoryPacket(2, slots.get(2).getItem() ,cap2.serializeNBT() ,getScreen().getMenu().getPos()));
				});
			});
		} else if (!slots.get(1).getItem().isEmpty()) {
			ItemStack stack = new ItemStack(ItemRegistry.DNACHICKENSPAWNEGG.get());
			stack.getCapability(DNAProvider.DNASTORAGE).ifPresent(cap2 -> {
				slots.get(1).getItem().getCapability(DNAProvider.DNASTORAGE).ifPresent(cap1 -> {
					cap2.setChromosomes(cap1.getChromosomes());
					slots.get(2).set(stack);
					cap2.setEmpty(false);
					MendelCraftChannel.INSTANCE.sendToServer(new LaboratoryPacket(2, slots.get(2).getItem() ,cap2.serializeNBT() ,getScreen().getMenu().getPos()));
				});
			});
		}
		this.init(relX, relY);
	}
	
	
	public void ChangeGene(LaboratoryScreen screen, int relX, int relY, Button b, String gene, int i, int parent) {
		if (gene.isEmpty()) {
			return;
		}
		ItemStack item = screen.getMenu().slots.get(getSlot()).getItem();
		this.setSelectedgene(i);
		this.setParentgene(parent);
		item.getCapability(DNAProvider.DNASTORAGE).ifPresent(cap -> {
			this.geneindex = DNAUtil.getPossibleGenes(getSelectedChromosome(), getSelectedgene()).indexOf(cap.getGene(getSelectedChromosome(), getSelectedgene()).get(getParentgene()));
		});
		this.init(relX, relY);
		screen.addRenderableWidget(new Button(relX + 50, relY + screen.getYSize() - 55, 10, 20, new TextComponent("<"), b2 -> this.decreaseIndex(b2, relX, relY)));
		screen.addRenderableWidget(new Button(relX + screen.getXSize() -65, relY + screen.getYSize() - 55, 10, 20, new TextComponent(">"), b2 -> this.increaseIndex(b2, relX, relY)));
		this.setDraw(gene);
	}
	
	public void increaseIndex(Button b, int relX, int relY) {
		if (this.geneindex < DNAUtil.getPossibleGenes(getSelectedChromosome(), getSelectedgene()).size()-1) {
			this.geneindex +=1;
		}else {
			this.geneindex = 0;
		}
		this.setDraw(DNAUtil.getPossibleGenes(getSelectedChromosome(), getSelectedgene()).get(geneindex));
		getScreen().getMenu().slots.get(2).getItem().getCapability(DNAProvider.DNASTORAGE).ifPresent(c -> {
			List<String> gene = c.getGene(getSelectedChromosome(), getSelectedgene());
			gene.set(this.getParentgene(), getDraw());
			c.setGene(getSelectedChromosome(), getSelectedgene(), gene);
			c.setEmpty(false);
			MendelCraftChannel.INSTANCE.sendToServer(new LaboratoryPacket(2, getScreen().getMenu().slots.get(2).getItem(), c.serializeNBT(), getScreen().getMenu().getPos()));
		});
	}
	
	public void decreaseIndex(Button b, int relX, int relY) {
		if (this.geneindex > 0) {
			this.geneindex -=1;
		}else {
			this.geneindex = DNAUtil.getPossibleGenes(getSelectedChromosome(), getSelectedgene()).size()-1;
		}
		this.setDraw(DNAUtil.getPossibleGenes(getSelectedChromosome(), getSelectedgene()).get(geneindex));
		getScreen().getMenu().slots.get(2).getItem().getCapability(DNAProvider.DNASTORAGE).ifPresent(c -> {
			List<String> gene = c.getGene(getSelectedChromosome(), getSelectedgene());
			gene.set(this.getParentgene(), getDraw());
			c.setGene(getSelectedChromosome(), getSelectedgene(), gene);
			c.setEmpty(false);
			MendelCraftChannel.INSTANCE.sendToServer(new LaboratoryPacket(2, getScreen().getMenu().slots.get(2).getItem(), c.serializeNBT(), getScreen().getMenu().getPos()));
		});
	}
	
	@Override
	public void clear() {
		super.clear();
		this.geneindex = 0;
	}
}
