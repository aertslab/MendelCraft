package com.quintenlauwers.backend.network.dnadata;

import com.quintenlauwers.entity.DnaEntity;
import com.quintenlauwers.main.TestMod;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by quinten on 9/08/16.
 * ONLY client side !!!
 */
public class NetworkDnaDataHandler implements IMessageHandler<NetworkDnaDataPacket, IMessage>{
    @Override
    public IMessage onMessage(final NetworkDnaDataPacket message, MessageContext ctx) {
        IThreadListener mainThread;
        mainThread = Minecraft.getMinecraft().getIntegratedServer();
        mainThread.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                DnaEntity animal = TestMod.storage.getById(message.getId());
                if (animal != null) {
                    animal.setDnaData(message.getDnaData());
                }
            }
        });
        return null;
    }
}
