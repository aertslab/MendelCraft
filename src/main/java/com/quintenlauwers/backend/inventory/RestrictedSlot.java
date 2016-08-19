package com.quintenlauwers.backend.inventory;

import com.quintenlauwers.item.dnaSyringe;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

/**
 * Created by quinten on 19/08/16.
 */
public class RestrictedSlot extends Slot {
    public RestrictedSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(@Nullable ItemStack stack) {
        return stack.getItem() instanceof dnaSyringe;
    }
}
