package com.quintenlauwers.backend.network.dnadata;

import com.quintenlauwers.backend.util.UtilDna;
import com.quintenlauwers.main.MendelCraft;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.Arrays;

/**
 * Created by quinten on 9/08/16.
 */
public class NetworkDnaDataPacket implements IMessage {

    private byte[] dnaData;
    private byte[] dnaData2;
    private int entityId;

    public NetworkDnaDataPacket() {
    }

    public NetworkDnaDataPacket(int entityId, byte[] dnaData, byte[] dnaData2) {
        this.dnaData = dnaData;
        this.entityId = entityId;
        this.dnaData2 = dnaData2;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        if (buf != null) {
            byte[] totalData = new byte[buf.readableBytes()];
            buf.readBytes(totalData);
            if (totalData.length >= 4) {
                if (MendelCraft.dnaConfig.isDiploid()) {
                    this.entityId = UtilDna.byteToInt(Arrays.copyOf(totalData, 4));
                    int length = UtilDna.byteToInt(Arrays.copyOfRange(totalData, 4, 8));
                    if (8 + length < totalData.length) {
                        this.dnaData = Arrays.copyOfRange(totalData, 8, 8 + length);
                        this.dnaData2 = Arrays.copyOfRange(totalData, 8 + length, 8 + 2 * length);
                    }
                } else {
                    this.entityId = UtilDna.byteToInt(Arrays.copyOf(totalData, 4));
                    this.dnaData = Arrays.copyOfRange(totalData, 4, totalData.length);
                }
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        byte[] totalData;
        if (dnaData2 == null) {
            totalData = UtilDna.appendByteArrays(UtilDna.intToByte(this.getId()), this.getDnaData());
        } else {
            totalData = UtilDna.appendByteArrays(UtilDna.intToByte(this.getId()), UtilDna.intToByte(this.getDnaData().length), this.getDnaData(), this.dnaData2);
        }
        if (buf.capacity() < totalData.length) {
            buf.capacity(totalData.length);
        }
        buf.writeBytes(totalData);
    }

    public byte[] getDnaData() {
        return this.dnaData;
    }

    public byte[] getDnaData2() {
        return this.dnaData2;
    }

    public int getId() {
        return this.entityId;
    }
}
