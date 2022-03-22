package ferri.arnus.mendelcraft.network;

import java.util.function.Supplier;

import ferri.arnus.mendelcraft.blockentities.LaboratoryBlockEntity;
import ferri.arnus.mendelcraft.capability.DNAProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.network.NetworkEvent.Context;

public class LaboratoryPacket {

	private int slot;
	private ItemStack storage;
	private CompoundTag tag;
	private BlockPos pos;

	public LaboratoryPacket(int slot, ItemStack storage, CompoundTag tag, BlockPos pos) {
		this.slot =slot;
		this.storage = storage;
		this.tag = tag;
		this.pos = pos;
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(slot);
		buffer.writeItemStack(storage, true);
		buffer.writeNbt(tag);
		buffer.writeBlockPos(pos);
	}
	
	public static LaboratoryPacket decode(FriendlyByteBuf buffer) {
		int slot = buffer.readInt();
		ItemStack storage = buffer.readItem();
		CompoundTag tag = buffer.readNbt();
		BlockPos pos = buffer.readBlockPos();
		return new LaboratoryPacket(slot, storage, tag, pos);
	}
	
	static void handle(final LaboratoryPacket message, Supplier<Context> ctx) {
		ctx.get().enqueueWork(() -> {
			BlockEntity be = ctx.get().getSender().level.getBlockEntity(message.pos);
			if (be instanceof LaboratoryBlockEntity lab) {
				lab.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
					h.extractItem(message.slot, h.getStackInSlot(message.slot).getCount(), false);
					message.storage.getCapability(DNAProvider.DNASTORAGE).ifPresent(cap -> {
						cap.deserializeNBT(message.tag);
					});
					h.insertItem(message.slot, message.storage, false);
				});
			}
		});
	}
}
