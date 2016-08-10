package com.quintenlauwers.backend.network.dnarequest;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * Created by quinten on 10/08/16.
 */
public class NetworkDnaRequestPacket implements IMessage {

    public int getEntityId() {
        return entityId;
    }

    int entityId;

    public NetworkDnaRequestPacket() {}

    public NetworkDnaRequestPacket(int entityId) {
        this.entityId = entityId;

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        if (buf != null) {
            this.entityId = buf.readInt();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityId);
    }
}
