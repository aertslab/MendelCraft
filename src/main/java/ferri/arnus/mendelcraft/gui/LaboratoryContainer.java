package ferri.arnus.mendelcraft.gui;

import ferri.arnus.mendelcraft.blockentities.LaboratoryBlockEntity;
import ferri.arnus.mendelcraft.gui.slots.HideableSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class LaboratoryContainer extends AbstractContainerMenu{
	
	private BlockPos pos;

	public LaboratoryContainer(final int windowId, Inventory playerInventory, final FriendlyByteBuf data) {
		 this(windowId, playerInventory.player.level, data.readBlockPos(), playerInventory);
	}

	public LaboratoryContainer(final int windowId, Level world, BlockPos pos, Inventory playerInventory) {
		super(ContainerRegistry.LABORATORY.get(), windowId);
		this.pos = pos;
		LaboratoryBlockEntity laboratory = (LaboratoryBlockEntity) world.getBlockEntity(pos);
		laboratory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(cap -> {
			addSlot(new HideableSlot(cap, 0, 26, 31));
			addSlot(new HideableSlot(cap, 1, 75, 31));
			addSlot(new HideableSlot(cap, 2, 133, 31) {
				
				@Override
				public boolean mayPlace(ItemStack p_40231_) {
					return true;
				}
			});
		});
		
		this.bindPlayerInventory(new InvWrapper(playerInventory));
	}
	
	public BlockPos getPos() {
		return pos;
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
	
	@Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
	      Slot slot = this.slots.get(index);
	      if (slot != null && slot.hasItem()) {
	         ItemStack itemstack1 = slot.getItem();
	         itemstack = itemstack1.copy();
	         if (index < 3) {
	            if (!this.moveItemStackTo(itemstack1, 3, this.slots.size(), true)) {
	               return ItemStack.EMPTY;
	            }
	         } else if (!this.moveItemStackTo(itemstack1, 0, 3, false)) {
	            return ItemStack.EMPTY;
	         }

	         if (itemstack1.isEmpty()) {
	            slot.set(ItemStack.EMPTY);
	         } else {
	            slot.setChanged();
	         }
	      }

	      return itemstack;
    }
}
