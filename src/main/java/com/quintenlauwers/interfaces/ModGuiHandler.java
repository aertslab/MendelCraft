package com.quintenlauwers.interfaces;

import com.quintenlauwers.backend.inventory.ContainerDna;
import com.quintenlauwers.backend.inventory.InventoryItem;
import com.quintenlauwers.item.dnaSyringe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ModGuiHandler implements IGuiHandler {

	public static final int TUTORIAL_GUI = 0;
    public static final int DNA_GUI = 1;

    public static Container SERVERCONTAINER;

    @Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == DNA_GUI && player.getHeldItem(EnumHand.MAIN_HAND) != null) {
            // Use the player's held item to create the inventory
            SERVERCONTAINER = new ContainerDna(player,
                    player.inventory,
                    new InventoryItem(
                            (player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof dnaSyringe ?
                                    player.getHeldItem(EnumHand.MAIN_HAND)
                                    : player.getHeldItem(EnumHand.OFF_HAND))));
            return SERVERCONTAINER;
        }
        return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
	    if (ID == TUTORIAL_GUI)
	        return new GuiTutorial();
        if (ID == DNA_GUI && player.getHeldItem(EnumHand.MAIN_HAND) != null) {
            // We have to cast the new container as our custom class
            // and pass in currently held item for the inventory
            return new GuiDnaMain(new ContainerDna(player,
                    player.inventory,
                    new InventoryItem(
                            (player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof dnaSyringe ?
                                    player.getHeldItem(EnumHand.MAIN_HAND)
                                    : player.getHeldItem(EnumHand.OFF_HAND)))));
        }
        return null;
	}
}
