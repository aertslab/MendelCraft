package org.aertslab.mendelcraft.network;

import java.util.function.Supplier;

import org.aertslab.mendelcraft.capability.DNAProvider;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent.Context;

public class UpdateDNAEntityHandler {

	public static void handlePacket(UpdateDNAEntityPacket message, Supplier<Context> ctx) {
		Entity entity = Minecraft.getInstance().level.getEntity(message.getId());
		entity.getCapability(DNAProvider.DNASTORAGE).ifPresent(cap -> {
			cap.deserializeNBT(message.getTag());
		});
	}

}
