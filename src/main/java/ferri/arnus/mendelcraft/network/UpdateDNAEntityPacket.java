package ferri.arnus.mendelcraft.network;

import java.util.function.Supplier;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class UpdateDNAEntityPacket{
	private int id;
	private CompoundTag tag;
	
	public UpdateDNAEntityPacket(int id, CompoundTag tag) {
		this.id = id;
		this.tag = tag;
	}
	
	public int getId() {
		return id;
	}
	
	public CompoundTag getTag() {
		return tag;
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeNbt(tag);
	}
	
	public static UpdateDNAEntityPacket decode(FriendlyByteBuf buffer) {
		int id = buffer.readInt();
		CompoundTag tag = buffer.readNbt();
		return new UpdateDNAEntityPacket(id, tag);
	}
	
	static void handle(final UpdateDNAEntityPacket message, Supplier<Context> ctx) {
		ctx.get().enqueueWork(() ->
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> UpdateDNAEntityHandler.handlePacket(message, ctx))
				);
		ctx.get().setPacketHandled(true);
	}
}
