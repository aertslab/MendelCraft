package ferri.arnus.mendelcraft.network;

import ferri.arnus.mendelcraft.MendelCraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

public class MendelCraftChannel {
	private static final String PROTOCOL_VERSION = "1";
	
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(MendelCraft.MODID, "main"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
			);
	
	public static void register() {
		INSTANCE.registerMessage(0, UpdateDNAEntityPacket.class, UpdateDNAEntityPacket::encode, UpdateDNAEntityPacket::decode, UpdateDNAEntityPacket::handle);
	}
}
