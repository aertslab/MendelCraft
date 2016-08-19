package com.quintenlauwers.interfaces;

import com.quintenlauwers.item.InventoryItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by quinten on 18/08/16.
 */
public class ContainerDna extends Container {

    protected InventoryItem inventory;

    public ContainerDna(EntityPlayer player, InventoryPlayer inventoryPlayer, InventoryItem te) {
        inventory = te;

        //the Slot constructor takes the IInventory and the slot number in that it binds to
        // and the x-y coordinates it resides on-screen

        addSlotToContainer(new Slot(inventory, 0, 62, 17));


        //commonly used vanilla code that adds the player's inventory
        bindPlayerInventory(inventoryPlayer);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return inventory.isUseableByPlayer(player);
    }


    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
        ItemStack stack = null;
        Slot slotObject = inventorySlots.get(slot);

//null checks and checks if the item can be stacked (maxStackSize > 1)
        if (slotObject != null && slotObject.getHasStack()) {
            ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();

//merges the item into player inventory since its in the tileEntity
            if (slot < inventory.getSizeInventory()) {
                if (!this.mergeItemStack(stackInSlot, inventory.getSizeInventory(), 36 + inventory.getSizeInventory(), true)) {
                    return null;
                }
            }

            //places it into the tileEntity is possible since its in the player inventory
            else if (!this.mergeItemStack(stackInSlot, 0, inventory.getSizeInventory(), false)) {
                return null;
            }

            if (stackInSlot.stackSize == 0) {
                slotObject.putStack(null);
            } else {
                slotObject.onSlotChanged();
            }

            if (stackInSlot.stackSize == stack.stackSize) {
                return null;
            }
            slotObject.onPickupFromSlot(player, stackInSlot);
        }
        return stack;
    }
}
