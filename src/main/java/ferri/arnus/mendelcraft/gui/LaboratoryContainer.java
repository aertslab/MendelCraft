package ferri.arnus.mendelcraft.gui;

import ferri.arnus.mendelcraft.blockentities.LaboratoryBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class LaboratoryContainer extends AbstractContainerMenu{
	private IItemHandler playerInventory;
	private LaboratoryBlockEntity laboratory;
	
	public LaboratoryContainer(final int windowId, Inventory playerInventory, final FriendlyByteBuf data) {
		 this(windowId, playerInventory.player.level, data.readBlockPos(), playerInventory);
	}

	public LaboratoryContainer(final int windowId, Level world, BlockPos pos, Inventory playerInventory) {
		super(ContainerRegistry.LABORATORY.get(), windowId);
		this.playerInventory = new InvWrapper(playerInventory);
		this.laboratory = (LaboratoryBlockEntity) world.getBlockEntity(pos);
		
		this.laboratory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(cap -> {
			addSlot(new SlotItemHandler(cap, 0, 26, 31));
			addSlot(new SlotItemHandler(cap, 1, 75, 31));
			addSlot(new SlotItemHandler(cap, 2, 133, 31));
		});
		
		bindPlayerInventory();
	}

	private void bindPlayerInventory() {
		for(int i = 0; i < 3; ++i) {
	         for(int j = 0; j < 9; ++j) {
	            this.addSlot(new SlotItemHandler(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
	         }
	      }

	      for(int k = 0; k < 9; ++k) {
	         this.addSlot(new SlotItemHandler(playerInventory, k, 8 + k * 18, 142));
	      }
	}

	@Override
	public boolean stillValid(Player pPlayer) {
		return true;
	}
}
