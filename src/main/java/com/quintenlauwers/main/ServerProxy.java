package com.quintenlauwers.main;

import com.quintenlauwers.backend.network.NetworkDnaHandler;
import com.quintenlauwers.backend.network.NetworkDnaPacket;
import net.minecraftforge.fml.relauncher.Side;

public class ServerProxy extends  CommonProxy{

    private int dnaPacketId = 0;

    @Override
    public void sendDnaPacketData(byte[] data) {
        TestMod.network.registerMessage(NetworkDnaHandler.class, NetworkDnaPacket.class, dnaPacketId, Side.SERVER);
        dnaPacketId = (dnaPacketId++)%255;

    }
}
