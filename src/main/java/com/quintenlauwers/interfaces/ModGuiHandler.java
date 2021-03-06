package com.quintenlauwers.interfaces;

import com.quintenlauwers.backend.inventory.ContainerDna;
import com.quintenlauwers.entity.DnaEntity;
import com.quintenlauwers.tileentity.TileEntityLab;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ModGuiHandler implements IGuiHandler {

	public static final int TUTORIAL_GUI = 0;
    public static final int DNA_GUI = 1;
    public static final int INSPECTOR_GUI = 2;

    public static Container SERVERCONTAINER;

    @Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == DNA_GUI) {
            // Use the lab block to get the inventory of the lab.
            TileEntity tile_entity = world.getTileEntity(new BlockPos(x, y, z));
            if (tile_entity instanceof TileEntityLab) {
                SERVERCONTAINER = new ContainerDna(player,
                        player.inventory,
                        (TileEntityLab) tile_entity);
                return SERVERCONTAINER;
            }
        }
        return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == DNA_GUI) {
            // The lab block contains the inventory.
            TileEntity tile_entity = world.getTileEntity(new BlockPos(x, y, z));
            if (tile_entity instanceof TileEntityLab) {
                return new GuiDnaMain(new ContainerDna(player,
                        player.inventory,
                        (TileEntityLab) tile_entity));
            }
        }
        if (ID == INSPECTOR_GUI) {
            Entity entity = world.getEntityByID(x);
            if (entity != null && entity instanceof DnaEntity) {
                return new GuiInspector(((DnaEntity) entity).getProperties());
            }
        }
        return null;
	}
}
