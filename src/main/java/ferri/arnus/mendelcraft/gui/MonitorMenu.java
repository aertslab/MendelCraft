package ferri.arnus.mendelcraft.gui;

import ferri.arnus.mendelcraft.blockentities.MonitorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;

public class MonitorMenu extends AbstractContainerMenu{
	private MonitorBlockEntity monitor;
	
	public MonitorMenu(final int windowId, Inventory playerInventory, final FriendlyByteBuf data) {
		 this(windowId, playerInventory.player.level, data.readBlockPos(), playerInventory);
	}

	public MonitorMenu(final int windowId, Level world, BlockPos pos, Inventory playerInventory) {
		super(ContainerRegistry.MONITOR.get(), windowId);
		if (world.getBlockEntity(pos) instanceof MonitorBlockEntity monitor) {
			this.monitor = monitor;
		}
		
	}

	@Override
	public boolean stillValid(Player p_38874_) {
		return true;
	}
	
	public MonitorBlockEntity getMonitor() {
		return monitor;
	}

}
