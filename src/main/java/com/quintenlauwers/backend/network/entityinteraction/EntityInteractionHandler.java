package com.quintenlauwers.backend.network.entityinteraction;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by quinten on 15/08/16.
 */
@SideOnly(Side.SERVER)
public class EntityInteractionHandler implements IMessageHandler<EntityInteractionPackage, IMessage> {
    @Override
    public IMessage onMessage(EntityInteractionPackage message, MessageContext ctx) {
        Minecraft minecraft = Minecraft.getMinecraft();
        return null;
    }
}
