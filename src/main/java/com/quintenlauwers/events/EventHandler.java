package com.quintenlauwers.events;

import com.quintenlauwers.blocks.ComStone;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by quinten on 8/08/16.
 */
public class EventHandler {

    public EventHandler() {

    }

    @SubscribeEvent
    public void TileEntity(AttachCapabilitiesEvent.TileEntity tile) {
        if (tile != null) {
            if (tile.getTileEntity() != null) {
                if (tile.getTileEntity().getBlockType() != null) {
                    System.out.println(tile.getTileEntity().getBlockType().getClass());
                    if (tile.getTileEntity().getBlockType().getClass() == ComStone.class)
                        System.out.println("Block placed!!");
                }
            }
        }
    }

    @SubscribeEvent
    public void PlaceEvent(BlockEvent.PlaceEvent event) {
        if (event != null) {
            if (event.getPlacedBlock() != null) {
                if (event.getPlacedBlock().getClass() != null) {
                    System.out.println(event.getPlacedBlock().getClass());
                    try {
                        System.out.println(event.getPlacedBlock().getBlock().getClass() == ComStone.class);
                    } catch (NullPointerException e) {
                        System.out.println(e.toString());
                    }
                    System.out.println("Block placed!!");
                }
            }
        }
    }
}
