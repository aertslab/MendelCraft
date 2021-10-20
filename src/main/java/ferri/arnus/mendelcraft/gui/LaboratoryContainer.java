package ferri.arnus.mendelcraft.gui;

import ferri.arnus.mendelcraft.blockentities.LaboratoryBlockEntity;
import ferri.arnus.mendelcraft.gui.slots.HideableSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class LaboratoryContainer extends AbstractContainerMenu{
	
	public LaboratoryContainer(final int windowId, Inventory playerInventory, final FriendlyByteBuf data) {
		 this(windowId, playerInventory.player.level, data.readBlockPos(), playerInventory);
	}

	public LaboratoryContainer(final int windowId, Level world, BlockPos pos, Inventory playerInventory) {
		super(ContainerRegistry.LABORATORY.get(), windowId);
		LaboratoryBlockEntity laboratory = (LaboratoryBlockEntity) world.getBlockEntity(pos);
		laboratory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(cap -> {
			addSlot(new HideableSlot(cap, 0, 26, 31));
			addSlot(new HideableSlot(cap, 1, 75, 31));
			addSlot(new HideableSlot(cap, 2, 133, 31));
		});
		
		this.bindPlayerInventory(new InvWrapper(playerInventory));
	}

	private void bindPlayerInventory(IItemHandler inventory) {
		for(int i = 0; i < 3; ++i) {
	         for(int j = 0; j < 9; ++j) {
	            this.addSlot(new HideableSlot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
	         }
	      }

	      for(int k = 0; k < 9; ++k) {
	         this.addSlot(new HideableSlot(inventory, k, 8 + k * 18, 142));
	      }
	}

	@Override
	public boolean stillValid(Player pPlayer) {
		return true;
	}
}
