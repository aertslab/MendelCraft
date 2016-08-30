package com.quintenlauwers.backend.network.entityinteraction;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

public class ProcessInteractionPackage implements IMessage {
    UUID entityId;
    UUID playerId;
    int hand;
    ItemStack itemStack;

    public ProcessInteractionPackage() {
    }

    public ProcessInteractionPackage(Entity from, EntityPlayer player, EnumHand hand, ItemStack stack) {
        this.entityId = from.getPersistentID();
        this.playerId = player.getPersistentID();
        if (hand == EnumHand.MAIN_HAND) {
            this.hand = 0;
        } else {
            this.hand = 1;
        }
        this.itemStack = stack;
    }

    public UUID getEntityId() {
        return this.entityId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public EnumHand getHand() {
        if (this.hand == 1) {
            return EnumHand.OFF_HAND;
        }
        return EnumHand.MAIN_HAND;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityId = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        this.playerId = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        this.hand = buf.readInt();
        this.itemStack = ByteBufUtils.readItemStack(buf);

    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.entityId.toString());
        ByteBufUtils.writeUTF8String(buf, this.playerId.toString());
        buf.writeInt(hand);
        ByteBufUtils.writeItemStack(buf, itemStack);
    }
}
