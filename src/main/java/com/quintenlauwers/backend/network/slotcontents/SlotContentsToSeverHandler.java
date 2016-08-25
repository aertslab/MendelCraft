package com.quintenlauwers.backend.network.slotcontents;

import com.quintenlauwers.interfaces.ModGuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by quinten on 15/08/16.
 */
public class SlotContentsToSeverHandler implements IMessageHandler<SlotContentsToServerPackage, IMessage> {
    @Override
    public IMessage onMessage(final SlotContentsToServerPackage message, final MessageContext ctx) {
        if (message == null) {
            return null;
        }
        final IThreadListener mainThread;
        if (ctx.side.equals(Side.SERVER)) {
            mainThread = (WorldServer) ctx.getServerHandler().playerEntity.worldObj;
        } else {
            mainThread = Minecraft.getMinecraft();
        }
        mainThread.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                Container container;
//                Container container = ctx.getServerHandler().playerEntity.openContainer;
//                Container container = ModGuiHandler.SERVERCONTAINER;
                if (ctx.side.equals(Side.SERVER)) {
                    container = ctx.getServerHandler().playerEntity.openContainer;
                } else {
                    container = ModGuiHandler.SERVERCONTAINER;
                }
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
