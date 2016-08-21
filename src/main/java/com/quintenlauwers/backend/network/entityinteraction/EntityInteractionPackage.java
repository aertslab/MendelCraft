package com.quintenlauwers.backend.network.entityinteraction;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

/**
 * Created by quinten on 15/08/16.
 */
public class EntityInteractionPackage implements IMessage {

    String animalUUID;
    String uuid;
    int hand;
    int animalId;

    public EntityInteractionPackage() {
    }

    public EntityInteractionPackage(Entity animal, EntityPlayer player, EnumHand hand) {
        this.animalUUID = animal.getPersistentID().toString();
        this.animalId = animal.getEntityId();
        this.uuid = player.getPersistentID().toString();
        if (hand == EnumHand.MAIN_HAND) {
            this.hand = 0;
        } else {
            this.hand = 1;
        }
    }

    public UUID getAnimalPersistentId() {
        return UUID.fromString(this.animalUUID);
    }

    public UUID getPlayerUUID() {
        return UUID.fromString(this.uuid);
    }

    public int getAnimalId() {
        return animalId;
    }

    public EnumHand getHand() {
        if (this.hand == 1) {
            return EnumHand.OFF_HAND;
        }
        return EnumHand.MAIN_HAND;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.animalId = buf.readInt();
        this.animalUUID = ByteBufUtils.readUTF8String(buf);
        this.hand = buf.readInt();
        this.uuid = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.animalId);
        ByteBufUtils.writeUTF8String(buf, this.animalUUID);
        buf.writeInt(this.hand);
        ByteBufUtils.writeUTF8String(buf, this.uuid);
    }
}
