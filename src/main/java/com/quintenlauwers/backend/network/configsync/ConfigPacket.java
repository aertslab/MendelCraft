package com.quintenlauwers.backend.network.configsync;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * Created by quinten on 2/09/16.
 */
public class ConfigPacket implements IMessage {
    private String packetName;
    private byte[] configAsBytes;

    public ConfigPacket() {

    }

    public ConfigPacket(String packetName, byte[] configAsBytes) {
        this.packetName = packetName;
        if (configAsBytes != null)
            this.configAsBytes = configAsBytes.clone();
    }

    public String getPacketName() {
        return this.packetName;
    }

    public byte[] getConfigAsBytes() {
        return this.configAsBytes.clone();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.packetName = ByteBufUtils.readUTF8String(buf);
        int length = buf.readInt();
        this.configAsBytes = new byte[length];
        buf.readBytes(this.configAsBytes);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if (buf == null || configAsBytes == null)
            return;
        ByteBufUtils.writeUTF8String(buf, packetName);
        buf.writeInt(configAsBytes.length);
        if ((buf.capacity() - buf.writerIndex()) < configAsBytes.length) {
            buf.capacity(buf.writerIndex() + configAsBytes.length);
        }
        buf.writeBytes(configAsBytes);

    }
}
