package ferri.arnus.mendelcraft.gui.slots;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class HideableSlot extends SlotItemHandler{

	private boolean active = true;
	
	public HideableSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}
	
	public HideableSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, boolean active) {
		super(itemHandler, index, xPosition, yPosition);
		this.active = active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	@Override
	public boolean isActive() {
		return this.active;
	}
	
}
