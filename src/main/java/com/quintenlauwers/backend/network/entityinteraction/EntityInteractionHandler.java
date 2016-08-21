package com.quintenlauwers.backend.network.entityinteraction;

import com.quintenlauwers.entity.DnaEntity;
import com.quintenlauwers.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by quinten on 15/08/16.
 */
public class EntityInteractionHandler implements IMessageHandler<EntityInteractionPackage, IMessage> {
    @Override
    public IMessage onMessage(final EntityInteractionPackage message, final MessageContext ctx) {
        IThreadListener mainThread;
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
                    possiblePlayer = serverWorld.getPlayerEntityByUUID(message.getPlayerUUID());
                    animalFrom = serverWorld.getEntityByID(message.getAnimalId());
                } else {
                    Minecraft innerMinecraft = Minecraft.getMinecraft();
                    possiblePlayer = innerMinecraft.getIntegratedServer().getEntityFromUuid(message.getPlayerUUID());
                    animalFrom = innerMinecraft.getIntegratedServer().getEntityFromUuid(message.getAnimalPersistentId());
                }
                if (possiblePlayer != null && possiblePlayer instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) possiblePlayer;
                    if (animalFrom != null && animalFrom instanceof DnaEntity && animalFrom instanceof EntityLivingBase) {
                        ItemStack stack = player.getHeldItem(message.getHand());
                        ModItems.dnaSyringe.itemInteractionForEntity(stack, player, (EntityLivingBase) animalFrom, message.getHand());
                    }
                }
            }
        });
        return null;
    }
}
