package com.quintenlauwers.backend.network.entityinteraction;

import com.quintenlauwers.entity.DnaEntity;
import com.quintenlauwers.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by quinten on 15/08/16.
 */
public class EntityInteractionHandler implements IMessageHandler<EntityInteractionPackage, IMessage> {
    @Override
    public IMessage onMessage(final EntityInteractionPackage message, MessageContext ctx) {
        Minecraft minecraft = Minecraft.getMinecraft();
        IThreadListener mainThread;
        mainThread = minecraft.getIntegratedServer();
        mainThread.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                Minecraft innerMinecraft = Minecraft.getMinecraft();
                Entity possiblePlayer = innerMinecraft.getIntegratedServer().getEntityFromUuid(message.getPlayerUUID());
                if (possiblePlayer != null && possiblePlayer instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) possiblePlayer;
                    Entity animalFrom = innerMinecraft.getIntegratedServer().getEntityFromUuid(message.getAnimalId());
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
