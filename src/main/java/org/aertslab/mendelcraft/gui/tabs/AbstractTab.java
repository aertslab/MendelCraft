package org.aertslab.mendelcraft.gui.tabs;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.world.item.Item;

public abstract class AbstractTab {
	private int id;
	private Item icon;

	public AbstractTab(int id, Item icon) {
		this.id = id;
		this.icon = icon;
	}
	
	public int getId() {
		return id;
	}
	
	public Item getIcon() {
		return icon;
	}
	
	public abstract void render(PoseStack pPoseStack, float pPartialTicks, int pMouseX, int pMouseY );
	
	public abstract void init(int relX, int relY);
	
	public void clear() {}
}
