package com.quintenlauwers.backend.network.entityinteraction;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by quinten on 15/08/16.
 */
@SideOnly(Side.CLIENT)
public class EntityInteractionPackage implements IMessage {

    public EntityInteractionPackage() {

    }

    public EntityInteractionPackage(int itemId, byte[] dnaData) {

    }

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }
}
