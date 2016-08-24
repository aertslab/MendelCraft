package com.quintenlauwers.interfaces;

import com.quintenlauwers.interfaces.pages.GuiPage;
import com.quintenlauwers.item.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by quinten on 24/08/16.
 */
class Tab {
    private ItemStack iconItemStack;
    private int column;
    private GuiPage page;

    Tab(int column, GuiPage displayPage) {
        this.column = column;
        this.page = displayPage;
    }

    GuiPage getDisplayPage() {
        return this.page;
    }


    @SideOnly(Side.CLIENT)
    ItemStack getIconItemStack() {
        {
            if (this.iconItemStack == null) {
                this.iconItemStack = new ItemStack(this.getTabIconItem(), 1, 0);
            }

            return this.iconItemStack;
        }
    }

    @SideOnly(Side.CLIENT)
    public Item getTabIconItem() {
        switch (this.column) {
            case 0:
                return Item.getItemFromBlock(Blocks.CHEST);
            case 3:
                return Items.SPAWN_EGG;
            default:
                return ModItems.dnaSyringe;
        }
    }

    int getTabColumn() {
        return this.column;
    }
}
