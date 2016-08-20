package com.quintenlauwers.backend.network.slotcontents;

import com.quintenlauwers.interfaces.ModGuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by quinten on 15/08/16.
 */
public class SlotContentsToSeverHandler implements IMessageHandler<SlotContentsToServerPackage, IMessage> {
    @Override
    public IMessage onMessage(final SlotContentsToServerPackage message, MessageContext ctx) {
        if (message == null) {
            return null;
        }
        Minecraft minecraft = Minecraft.getMinecraft();
        IThreadListener mainThread;
        mainThread = minecraft.getIntegratedServer();
        mainThread.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                Minecraft innerMinecraft = Minecraft.getMinecraft();
                Container container = ModGuiHandler.SERVERCONTAINER;
                if (container != null) {
                    Slot slot = container.getSlot(message.getSlotId());
                    if (slot != null) {
                        slot.putStack(message.getContents());
                    }
                }
            }
        });
        return null;
    }
}
