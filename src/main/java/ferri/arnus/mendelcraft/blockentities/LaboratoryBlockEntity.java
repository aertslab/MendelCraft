package ferri.arnus.mendelcraft.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class LaboratoryBlockEntity extends BlockEntity{
	private IItemHandler itemstHandler = new ItemStackHandler(3);

	public LaboratoryBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
		super(BlockEntityRegistry.LABORATORY.get(), pWorldPosition, pBlockState);
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return LazyOptional.of(() -> itemstHandler).cast();
		}
		return super.getCapability(cap, side);
	}

}
