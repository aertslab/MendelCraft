package com.quintenlauwers.backend.network.entityinteraction;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

/**
 * Created by quinten on 15/08/16.
 */
@SideOnly(Side.CLIENT)
public class EntityInteractionPackage implements IMessage {

    String animalId;
    String uuid;
    int hand;

    public EntityInteractionPackage() {
    }

    public EntityInteractionPackage(Entity animal, EntityPlayer player, EnumHand hand) {
        this.animalId = animal.getPersistentID().toString();
        this.uuid = player.getPersistentID().toString();
        if (hand == EnumHand.MAIN_HAND) {
            this.hand = 0;
        } else {
            this.hand = 1;
        }
    }

    public UUID getAnimalId() {
        return UUID.fromString(this.animalId);
    }

    public UUID getPlayerUUID() {
        return UUID.fromString(this.uuid);
    }

    public EnumHand getHand() {
        if (this.hand == 1) {
            return EnumHand.OFF_HAND;
        }
        return EnumHand.MAIN_HAND;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.animalId = ByteBufUtils.readUTF8String(buf);
        this.hand = buf.readInt();
        this.uuid = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.animalId);
        buf.writeInt(this.hand);
        ByteBufUtils.writeUTF8String(buf, this.uuid);
    }
}
