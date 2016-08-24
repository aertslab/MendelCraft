package com.quintenlauwers.backend.inventory;

import com.quintenlauwers.item.dnaSyringe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by quinten on 18/08/16.
 */
public class ContainerDna extends Container {

    private static final int INV_START = InventoryItem.SIZE + 1, INV_END = INV_START + 26, HOTBAR_START = INV_END + 1,
            HOTBAR_END = HOTBAR_START + 8;

    protected IInventory dnaInventory;
    private EntityPlayer player;

    private RestrictedSlot inputSlot1;
    private RestrictedSlot inputSlot2;
    private TakeOnlySlot outputSlot;

    int inputSlot1Nb;
    int inputSlot2Nb;
    int outputSlotNb;

    public ContainerDna(EntityPlayer player, InventoryPlayer inventoryPlayer, IInventory te) {

        this.dnaInventory = te;
        this.player = player;

        addSlotToContainer(this.inputSlot1 = new RestrictedSlot(dnaInventory, 0, 26, 31));
        addSlotToContainer(this.inputSlot2 = new RestrictedSlot(dnaInventory, 1, 75, 31));
        addSlotToContainer(this.outputSlot = new TakeOnlySlot(dnaInventory, 2, 133, 31));

        //commonly used vanilla code that adds the player's inventory
        bindPlayerInventory(inventoryPlayer);
        //the Slot constructor takes the IInventory and the slot number in that it binds to
        // and the x-y coordinates it resides on-screen

    }

    public RestrictedSlot getInputSlot(int slot) {
        switch (slot) {
            case 0:
                return this.inputSlot1;
            case 1:
                return this.inputSlot2;
            default:
                return null;
        }
    }

    public TakeOnlySlot getOutputSlot() {
        return outputSlot;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return dnaInventory.isUseableByPlayer(player);
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

//    @Override
//    public List<ItemStack> getInventory() {
//        return dnaInventory.toList();
//    }

    @Override
    public Slot getSlot(int slotId) {
        return super.getSlot(slotId);
    }

    @Override
    public void putStackInSlot(int slotID, ItemStack stack) {
        this.getSlot(slotID).putStack(stack);
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
            if (slot < INV_START) {
                if (!this.mergeItemStack(stackInSlot, INV_START, HOTBAR_END + 1, true)) {
                    return null;
                }
                slotObject.onSlotChange(stackInSlot, stack);
            }
            // Item is in inventory / hotbar, try to place in custom slots.
            else {
                if (stackInSlot.getItem() instanceof dnaSyringe) {
                    if (!this.mergeItemStack(stackInSlot, 0, 2, false)) {
                        return null;
                    }
                }
                // Item is in inventory, move to hotbar
                else if (slot >= INV_START && slot < HOTBAR_START) {
                    if (!this.mergeItemStack(stackInSlot, HOTBAR_START, HOTBAR_END + 1, false)) {
                        return null;
                    }
                }
                // Item is in hotbar, move to inventory
                else if (slot >= HOTBAR_START && slot < HOTBAR_END + 1) {
                    if (!this.mergeItemStack(stackInSlot, INV_START, INV_END + 1, false)) {
                        return null;
                    }
                }
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
