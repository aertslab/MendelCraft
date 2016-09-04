package com.quintenlauwers.backend.network.configsync;

import com.quintenlauwers.main.MendelCraft;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by quinten on 2/09/16.
 */
public class ConfigHandler implements IMessageHandler<ConfigPacket, IMessage> {


    @Override
    public IMessage onMessage(final ConfigPacket message, MessageContext ctx) {
        IThreadListener mainThread;
        try {
            mainThread = Minecraft.getMinecraft();
        } catch (Exception e) {
            mainThread = (WorldServer) ctx.getServerHandler().playerEntity.worldObj;
        }
        if (message == null || message.getPacketName() == null || message.getConfigAsBytes() == null) {
            return null;
        }
        mainThread.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                if ("mainconfig".equals(message.getPacketName())) {
                    MendelCraft.dnaConfig.reloadMainConfig(message.getConfigAsBytes());
                } else {
                    MendelCraft.dnaConfig.reloadAnimal(message.getPacketName(), message.getConfigAsBytes());
                }
            }
        });
        return null;
    }
}
