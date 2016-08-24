package com.quintenlauwers.backend.network.dnarequest;

import com.quintenlauwers.backend.network.dnadata.NetworkDnaDataPacket;
import com.quintenlauwers.entity.DnaEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
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
            World world = ctx.getServerHandler().playerEntity.worldObj;
            Entity requested = world.getEntityByID(entityId);
            if (!(requested instanceof DnaEntity)) {
                return null;
            }
            DnaEntity animal = (DnaEntity) world.getEntityByID(entityId);
            byte[] dnaData = animal.getDnaData();
            byte[] dnaData2 = animal.getDnaData2();
            return new NetworkDnaDataPacket(entityId, dnaData, dnaData2);
        }
        return null;
    }
}
