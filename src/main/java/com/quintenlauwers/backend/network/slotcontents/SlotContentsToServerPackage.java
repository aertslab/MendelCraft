package com.quintenlauwers.backend.network.slotcontents;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * Created by quinten on 15/08/16.
 */
public class SlotContentsToServerPackage implements IMessage {

    ItemStack contents;
    int slotId;

    public SlotContentsToServerPackage() {
    }

    public SlotContentsToServerPackage(ItemStack contents, int slotId) {
        this.contents = contents;
        this.slotId = slotId;
    }

    public ItemStack getContents() {
        return this.contents;
    }

    public int getSlotId() {
        return this.slotId;
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        this.slotId = buf.readInt();
        this.contents = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(slotId);
        ByteBufUtils.writeItemStack(buf, this.contents);
    }
}
