package ferri.arnus.mendelcraft.network;

import java.util.function.Supplier;

import ferri.arnus.mendelcraft.blockentities.LaboratoryBlockEntity;
import ferri.arnus.mendelcraft.capability.DNAProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fmllegacy.network.NetworkEvent.Context;
import net.minecraftforge.items.CapabilityItemHandler;

public class LaboratoryPacket {

	private int slot;
	private CompoundTag storage;
	private BlockPos pos;

	public LaboratoryPacket(int slot, CompoundTag storage, BlockPos pos) {
		this.slot =slot;
		this.storage = storage;
		this.pos = pos;
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(slot);
		buffer.writeNbt(storage);
		buffer.writeBlockPos(pos);
	}
	
	public static LaboratoryPacket decode(FriendlyByteBuf buffer) {
		int slot = buffer.readInt();
		CompoundTag storage = buffer.readNbt();
		BlockPos pos = buffer.readBlockPos();
		return new LaboratoryPacket(slot, storage, pos);
	}
	
	static void handle(final LaboratoryPacket message, Supplier<Context> ctx) {
		ctx.get().enqueueWork(() -> {
			BlockEntity be = ctx.get().getSender().level.getBlockEntity(message.pos);
			if (be instanceof LaboratoryBlockEntity lab) {
				lab.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
					h.getStackInSlot(message.slot).getCapability(DNAProvider.DNASTORAGE).ifPresent(cap -> {
						cap.deserializeNBT(message.storage);
					});
				});
			}
		});
	}
}
