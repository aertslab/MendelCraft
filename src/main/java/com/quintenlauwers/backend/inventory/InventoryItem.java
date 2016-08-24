package com.quintenlauwers.backend.inventory;

import com.quintenlauwers.item.dnaSyringe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by quinten on 18/08/16.
 */
public class InventoryItem implements IInventory {

    private final ItemStack InItemstack;

    public static final int SIZE = 3;

    private ItemStack[] contains = new ItemStack[SIZE];

    public InventoryItem(ItemStack stack) {
        this.InItemstack = stack;

        if (!InItemstack.hasTagCompound()) {
            InItemstack.setTagCompound(new NBTTagCompound());
        }

        readFromNBT(InItemstack.getTagCompound());
    }

    @Override
    public int getSizeInventory() {
        return (contains.length);
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if (index >= 36) {
            index = index % 36;
        }
        return contains[index];
    }

    @Nullable
    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = getStackInSlot(index);
        if (stack != null && count > 0) {
            removeStackFromSlot(index);
        }
        return stack;
    }

    @Nullable
    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack stack = getStackInSlot(index);
        setInventorySlotContents(index, null);
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
        if (index >= 36) {
            index = index % 36;
        }
        if (index >= SIZE) {
            System.err.println("Trying to put in inventory: " + index + " : " + stack);
            return;
        }
        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }
        this.contains[index] = stack;
        markDirty();

    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void markDirty() {
        for (int i = 0; i < SIZE; i++) {
            ItemStack stack = getStackInSlot(i);
            if (stack != null && stack.stackSize == 0) {
                contains[i] = null;
            }
        }
        writeToNBT(InItemstack.getTagCompound());
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int indstackex, ItemStack stack) {
        return (stack != null && stack.getItem() instanceof dnaSyringe);
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < SIZE; i++) {
            removeStackFromSlot(i);
        }
    }

    public void readFromNBT(NBTTagCompound compound) {
        // Gets the custom taglist we wrote to this compound, if any
        NBTTagList items = compound.getTagList("ItemInventory", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < items.tagCount(); ++i) {
            // 1.7.2+ change to items.getCompoundTagAt(i)
            NBTTagCompound item = items.getCompoundTagAt(i);
            int slot = item.getInteger("Slot");

            // Just double-checking that the saved slot index is within our inventory array bounds
            if (slot >= 0 && slot < getSizeInventory()) {
                contains[slot] = ItemStack.loadItemStackFromNBT(item);
            }
        }
    }

    /**
     * A custom method to write our inventory to an ItemStack's NBT compound
     */
    public void writeToNBT(NBTTagCompound tagcompound) {
        // Create a new NBT Tag List to store itemstacks as NBT Tags
        NBTTagList items = new NBTTagList();

        for (int i = 0; i < getSizeInventory(); ++i) {
            // Only write stacks that contain items
            if (getStackInSlot(i) != null) {
                // Make a new NBT Tag Compound to write the itemstack and slot index to
                NBTTagCompound item = new NBTTagCompound();
                item.setInteger("Slot", i);
                // Writes the itemstack in slot(i) to the Tag Compound we just made
                getStackInSlot(i).writeToNBT(item);

                // add the tag compound to our tag list
                items.appendTag(item);
            }
        }
        // Add the TagList to the ItemStack's Tag Compound with the name "ItemInventory"
        tagcompound.setTag("ItemInventory", items);
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }

    public List<ItemStack> toList() {
        return Arrays.asList(contains);
    }
}
