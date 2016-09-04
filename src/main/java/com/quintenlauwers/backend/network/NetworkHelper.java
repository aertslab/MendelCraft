package com.quintenlauwers.backend.network;

import com.quintenlauwers.backend.network.configsync.ConfigHandler;
import com.quintenlauwers.backend.network.configsync.ConfigPacket;
import com.quintenlauwers.backend.network.dnadata.NetworkDnaDataHandler;
import com.quintenlauwers.backend.network.dnadata.NetworkDnaDataPacket;
import com.quintenlauwers.backend.network.dnarequest.NetworkDnaRequestHandler;
import com.quintenlauwers.backend.network.dnarequest.NetworkDnaRequestPacket;
import com.quintenlauwers.backend.network.entityinteraction.*;
import com.quintenlauwers.backend.network.slotcontents.SlotContentsToServerPackage;
import com.quintenlauwers.backend.network.slotcontents.SlotContentsToSeverHandler;
import com.quintenlauwers.entity.DnaEntity;
import com.quintenlauwers.main.MendelCraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by quinten on 10/08/16.
 */
public class NetworkHelper {

    public void sendDnaRequest(Entity from) {
        if (from == null) {
            return;
        }
        int id = from.getEntityId();
        if (from.getEntityWorld().isRemote && from instanceof DnaEntity) {
            MendelCraft.storage.addEntity(id, (DnaEntity) from);
            MendelCraft.network.sendToServer(new NetworkDnaRequestPacket(id));
        }
    }

    public void registerMessages() {
        MendelCraft.network.registerMessage(NetworkDnaDataHandler.class, NetworkDnaDataPacket.class, 0, Side.SERVER);
        MendelCraft.network.registerMessage(NetworkDnaDataHandler.class, NetworkDnaDataPacket.class, 0, Side.CLIENT);
        MendelCraft.network.registerMessage(NetworkDnaRequestHandler.class, NetworkDnaRequestPacket.class, 1, Side.SERVER);
        MendelCraft.network.registerMessage(EntityInteractionHandler.class, EntityInteractionPackage.class, 2, Side.SERVER);
        MendelCraft.network.registerMessage(SlotContentsToSeverHandler.class, SlotContentsToServerPackage.class, 3, Side.SERVER);
        MendelCraft.network.registerMessage(ProcessInteractionHandler.class, ProcessInteractionPackage.class, 4, Side.SERVER);
        MendelCraft.network.registerMessage(EntityChildBirthHandler.class, EntityChildBirthPackage.class, 5, Side.CLIENT);
        MendelCraft.network.registerMessage(ConfigHandler.class, ConfigPacket.class, 6, Side.CLIENT);
    }
}
