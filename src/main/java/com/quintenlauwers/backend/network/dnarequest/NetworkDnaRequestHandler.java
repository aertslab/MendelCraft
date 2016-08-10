package com.quintenlauwers.backend.network.dnarequest;

import com.quintenlauwers.backend.network.dnadata.NetworkDnaDataPacket;
import com.quintenlauwers.entity.DnaEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by quinten on 10/08/16.
 * This class is server side.
 */
public class NetworkDnaRequestHandler implements IMessageHandler<NetworkDnaRequestPacket, NetworkDnaDataPacket> {
    @Override
    public NetworkDnaDataPacket onMessage(NetworkDnaRequestPacket message, MessageContext ctx) {
        int entityId = message.getEntityId();
        if (ctx.side.equals(Side.SERVER)){
            Minecraft minecraft = Minecraft.getMinecraft();
            Entity requested = minecraft.getIntegratedServer().getEntityWorld().getEntityByID(entityId);
            if (!(requested instanceof DnaEntity)) {
                return null;
            }
            DnaEntity animal = (DnaEntity) minecraft.getIntegratedServer().getEntityWorld().getEntityByID(entityId);
            byte[] dnaData = animal.getDnaData();
            return new NetworkDnaDataPacket(entityId, dnaData);
        }
        return null;
    }
}
