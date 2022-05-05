package ferri.arnus.mendelcraft.gui.buttons;

import ferri.arnus.mendelcraft.MendelCraft;
import ferri.arnus.mendelcraft.capability.DNAUtil;
import ferri.arnus.mendelcraft.gui.tabs.SyringeTab;
import net.minecraft.resources.ResourceLocation;

public class EditButton extends GeneButton{
	
	private static final ResourceLocation TEXTURE = new ResourceLocation(MendelCraft.MODID, "textures/gui/dnabuttonedit.png");

	public EditButton(int pX, int pY, OnPress pOnPress, OnTooltip pOnTooltip, SyringeTab tab, String chromosome,
			int gene, int parent) {
		super(pX, pY, pOnPress, pOnTooltip, tab, chromosome, gene, parent);
	}
	
	@Override
	public ResourceLocation getButtonTexture() {
		if (DNAUtil.getPossibleGenes(chromosome, gene) == null) {
			return super.getButtonTexture();
		}
		return TEXTURE;
	}
}
