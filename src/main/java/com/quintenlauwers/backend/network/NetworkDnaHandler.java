package com.quintenlauwers.backend.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by quinten on 9/08/16.
 */
public class NetworkDnaHandler implements IMessageHandler<NetworkDnaPacket, IMessage>{
    @Override
    public IMessage onMessage(NetworkDnaPacket message, MessageContext ctx) {
        return null;
    }
}
