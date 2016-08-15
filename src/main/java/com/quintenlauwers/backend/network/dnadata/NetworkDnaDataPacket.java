package com.quintenlauwers.backend.network.dnadata;

import com.quintenlauwers.backend.util.UtilDna;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.Arrays;

/**
 * Created by quinten on 9/08/16.
 */
public class NetworkDnaDataPacket implements IMessage {

    private byte[] dnaData;
    private int entityId;

    public NetworkDnaDataPacket() {}

    public NetworkDnaDataPacket(int entityId, byte[] dnaData) {
        this.dnaData = dnaData;
        this.entityId = entityId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        if (buf != null) {
            byte[] totalData = new byte[5];
            // TODO: is to short (make variable).
            buf.readBytes(totalData);
            if (totalData.length >= 4) {
                this.entityId = UtilDna.byteToInt(Arrays.copyOf(totalData, 4));
                this.dnaData = Arrays.copyOfRange(totalData, 4, totalData.length);
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        byte[] totalData = UtilDna.appendByteArrays(UtilDna.intToByte(this.getId()), this.getDnaData());
        if (buf.capacity() < totalData.length) {
            buf.capacity(totalData.length);
        }
        buf.writeBytes(totalData);
    }

    public byte[] getDnaData() {
        return this.dnaData;
    }

    public int getId(){
        return this.entityId;
    }
}
