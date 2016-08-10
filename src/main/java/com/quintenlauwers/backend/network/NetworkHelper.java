package com.quintenlauwers.backend.network;

import com.quintenlauwers.backend.network.dnadata.NetworkDnaDataHandler;
import com.quintenlauwers.backend.network.dnadata.NetworkDnaDataPacket;
import com.quintenlauwers.backend.network.dnarequest.NetworkDnaRequestHandler;
import com.quintenlauwers.backend.network.dnarequest.NetworkDnaRequestPacket;
import com.quintenlauwers.entity.DnaEntity;
import com.quintenlauwers.main.TestMod;
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
            TestMod.storage.addEntity(id, (DnaEntity) from);
            TestMod.network.sendToServer(new NetworkDnaRequestPacket(id));
        }
    }

    public void registerMessages() {
        TestMod.network.registerMessage(NetworkDnaDataHandler.class, NetworkDnaDataPacket.class, 0, Side.SERVER);
        TestMod.network.registerMessage(NetworkDnaDataHandler.class, NetworkDnaDataPacket.class, 0, Side.CLIENT);
        TestMod.network.registerMessage(NetworkDnaRequestHandler.class, NetworkDnaRequestPacket.class, 1, Side.SERVER);
    }
}
