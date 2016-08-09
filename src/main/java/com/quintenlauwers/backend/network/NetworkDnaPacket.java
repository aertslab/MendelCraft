package com.quintenlauwers.backend.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * Created by quinten on 9/08/16.
 */
public class NetworkDnaPacket implements IMessage {

    byte[] dnaData;

    public NetworkDnaPacket() {}

    public NetworkDnaPacket(byte[] dnaData) {
        this.dnaData = dnaData;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        buf.getBytes(0, this.dnaData);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if (buf.capacity() < this.dnaData.length) {
            buf.capacity(this.dnaData.length);
        }
        buf.writeBytes(this.dnaData);
    }
}
