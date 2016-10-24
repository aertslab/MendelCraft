package com.quintenlauwers.backend.network.entityinteraction;

import com.quintenlauwers.entity.DnaEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.List;

/**
 * Created by quinten on 30/08/16.
 */
public class EntityChildBirthHandler implements IMessageHandler<EntityChildBirthPackage, IMessage> {

    @Override
    public IMessage onMessage(final EntityChildBirthPackage message, final MessageContext ctx) {
        IThreadListener mainThread;
        try {
            mainThread = Minecraft.getMinecraft();
        } catch (Exception e) {
            mainThread = (WorldServer) ctx.getServerHandler().playerEntity.worldObj;
        }
        mainThread.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                boolean isLocal = false;
                Entity father = null;
                Entity mother = null;
                try {
                    Minecraft innerMinecraft = Minecraft.getMinecraft();
                    if(innerMinecraft.getIntegratedServer() != null) {
                        isLocal = true;
                        father = innerMinecraft.getIntegratedServer().getServer().getEntityFromUuid(message.getFatherId());
                        mother = innerMinecraft.getIntegratedServer().getEntityFromUuid(message.getMotherId());
                    }
                    else {
                        List<Entity> entityList = innerMinecraft.theWorld.loadedEntityList;
                        for (Entity e : entityList) {
                            if (e != null) {
                                if (e.getPersistentID().equals(message.getFatherId())) {
                                    father = e;
                                }
                                if (e.getPersistentID().equals(message.getMotherId())) {
                                    mother = e;
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    World serverWorld = ctx.getServerHandler().playerEntity.worldObj;
                    for (Entity e : serverWorld.loadedEntityList) {
                        if (e != null) {
                            if (e.getPersistentID().equals(message.getFatherId())) {
                                father = e;
                            }
                            if (e.getPersistentID().equals(message.getMotherId())) {
                                mother = e;
                            }
                        }
                    }
                }
                if (father != null && father instanceof DnaEntity && father instanceof EntityAnimal) {
                    ((EntityAnimal) father).resetInLove();
                    ((EntityAnimal) father).setGrowingAge(-3);
                    if (!isLocal)
                        ((EntityAnimal) father).setGrowingAge(0);
                }
                if (mother != null && mother instanceof DnaEntity && mother instanceof EntityAnimal) {
                    ((EntityAnimal) mother).resetInLove();
                    ((EntityAnimal) mother).setGrowingAge(-3);
                    if (!isLocal)
                        ((EntityAnimal) mother).setGrowingAge(0);
                }
            }
        });
        return null;
    }
}


