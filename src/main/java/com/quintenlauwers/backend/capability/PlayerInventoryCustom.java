package com.quintenlauwers.backend.capability;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

/**
 * Created by quinten on 23/08/16.
 */
public class PlayerInventoryCustom implements IItemHandler {

    public static final int SIZE = 3;

    private ItemStack[] contains = new ItemStack[SIZE];

    @Override
    public int getSlots() {
        return SIZE;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return contains[slot];
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (slot >= SIZE) {
            System.err.println("Trying to put in inventory: " + slot + " : " + stack);
            return stack;
        }
        ItemStack returnStack = null;
        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            returnStack = stack.copy();
            returnStack.stackSize -= getInventoryStackLimit();
        }
        if (!simulate) {
            contains[slot] = stack;
        }
        return returnStack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack returnStack = contains[slot].copy();
        if (returnStack == null || returnStack.stackSize == 0 || returnStack.getItem() == null) {
            return returnStack;
        }
        int maxStack = returnStack.getItem().getItemStackLimit(new ItemStack(returnStack.getItem()));
        if (amount > maxStack && returnStack.stackSize >= maxStack) {
            returnStack.stackSize = maxStack;
            if (!simulate) {
                contains[slot].stackSize -= maxStack;
                if (contains[slot].stackSize == 0) {
                    contains[slot] = null;
                }
            }
            return returnStack;
        } else if (returnStack.stackSize < amount) {
            if (!simulate) {
                contains[slot] = null;
            }
            return returnStack;
        } else {
            returnStack.stackSize = amount;
            if (!simulate) {
                contains[slot].stackSize -= amount;
            }
            return returnStack;
        }
    }


    public int getInventoryStackLimit() {
        return 1;
    }

}
