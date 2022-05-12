package org.aertslab.mendelcraft.blockentities;

import org.aertslab.mendelcraft.capability.DNAProvider;
import org.aertslab.mendelcraft.capability.IDNAStorage;
import org.aertslab.mendelcraft.items.ItemRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class LaboratoryBlockEntity extends BlockEntity{
	private ItemStackHandler itemstHandler = new ItemStackHandler(3) {
		protected void onContentsChanged(int slot) {
			setChanged();
		}
		
		public boolean isItemValid(int slot, ItemStack stack) {
			if (slot < 2) {
				LazyOptional<IDNAStorage> cap = stack.getCapability(DNAProvider.DNASTORAGE);
				if (cap.isPresent()) {
					return stack.is(ItemRegistry.DNASYRINGE.get()) && !cap.resolve().get().isEmpty();
				}
			}
			return stack.is(ItemRegistry.DNACHICKENSPAWNEGG.get());
		}
		
	};
	private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemstHandler);

	public LaboratoryBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
		super(BlockEntityRegistry.LABORATORY.get(), pWorldPosition, pBlockState);
	}
	
	@Override
    public void setRemoved() {
        super.setRemoved();
        handler.invalidate();
    }
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return handler.cast();
		}
		return super.getCapability(cap, side);
	}
	
	@Override
	public void invalidateCaps() {
		handler.invalidate();
		super.invalidateCaps();
	}
	
	@Override
	protected void saveAdditional(CompoundTag pTag) {
		pTag.put("dnaitem", itemstHandler.serializeNBT());
		super.saveAdditional(pTag);
	}
	
	@Override
	public void load(CompoundTag pTag) {
		itemstHandler.deserializeNBT(pTag.getCompound("dnaitem"));
		super.load(pTag);
	}
}
