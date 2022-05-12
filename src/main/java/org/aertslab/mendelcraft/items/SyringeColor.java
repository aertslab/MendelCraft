package org.aertslab.mendelcraft.items;

import org.aertslab.mendelcraft.capability.DNAProvider;
import org.aertslab.mendelcraft.capability.DNAUtil;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;

public class SyringeColor implements ItemColor{

	@Override
	public int getColor(ItemStack stack, int tintindex) {
		int[] color = new int[] {0xffffff};
		stack.getCapability(DNAProvider.DNASTORAGE).ifPresent(cap -> {
			if (tintindex == 1) {
				color[0] = DNAUtil.getBloodColor(cap);
			}
		});
		return color[0];
	}

}
