package com.quintenlauwers.backend.network.entityinteraction;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

/**
 * Created by quinten on 30/08/16.
 */
public class EntityChildBirthPackage implements IMessage {
    private UUID fatherId;
    private UUID motherId;

    public EntityChildBirthPackage() {
    }

    public EntityChildBirthPackage(Entity father, Entity mother) {
        this.fatherId = father.getPersistentID();
        this.motherId = mother.getPersistentID();
    }

    public UUID getFatherId() {
        return this.fatherId;
    }

    public UUID getMotherId() {
        return motherId;
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        this.fatherId = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        this.motherId = UUID.fromString(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.fatherId.toString());
        ByteBufUtils.writeUTF8String(buf, this.motherId.toString());
    }
}
