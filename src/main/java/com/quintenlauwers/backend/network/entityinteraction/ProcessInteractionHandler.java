package com.quintenlauwers.backend.network.entityinteraction;

import com.quintenlauwers.entity.DnaEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ProcessInteractionHandler implements IMessageHandler<ProcessInteractionPackage, IMessage> {
    @Override
    public IMessage onMessage(final ProcessInteractionPackage message, final MessageContext ctx) {
        final IThreadListener mainThread;
        if (ctx.side.equals(Side.SERVER)) {
            mainThread = (WorldServer) ctx.getServerHandler().playerEntity.worldObj;
        } else {
            mainThread = Minecraft.getMinecraft();
        }
        mainThread.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                Entity possiblePlayer;
                Entity animalFrom = null;
                if (Side.SERVER.equals(ctx.side)) {
                    World serverWorld = ctx.getServerHandler().playerEntity.worldObj;
                    possiblePlayer = serverWorld.getPlayerEntityByUUID(message.getPlayerId());
                    for (Entity e : serverWorld.loadedEntityList) {
                        if (e != null && e.getPersistentID().equals(message.getEntityId())) {
                            animalFrom = e;
                        }
                    }
                } else {
                    Minecraft innerMinecraft = Minecraft.getMinecraft();
                    possiblePlayer = innerMinecraft.getIntegratedServer().getEntityFromUuid(message.getPlayerId());
                    animalFrom = innerMinecraft.getIntegratedServer().getEntityFromUuid(message.getEntityId());
                }
                if (possiblePlayer != null && possiblePlayer instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) possiblePlayer;
                    if (animalFrom != null && animalFrom instanceof DnaEntity && animalFrom instanceof EntityAnimal) {
                        ItemStack stack = message.getItemStack();
                        ((EntityAnimal) animalFrom).processInteract(player, message.getHand(), stack);
                    }
                }
            }
        });
        return null;
    }
}
