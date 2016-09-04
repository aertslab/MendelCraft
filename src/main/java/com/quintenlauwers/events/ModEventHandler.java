package com.quintenlauwers.events;

import com.quintenlauwers.backend.network.configsync.ConfigPacket;
import com.quintenlauwers.entity.chicken.EntityDnaChicken;
import com.quintenlauwers.main.MendelCraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * Created by quinten on 8/08/16.
 */
public class ModEventHandler {

    public ModEventHandler() {

    }

    @SubscribeEvent
    public void EntityJoinWorldEvent(EntityJoinWorldEvent event) {
        if (event != null && !event.getWorld().isRemote) {
            if (event.getEntity() instanceof EntityChicken
                    && !(event.getEntity() instanceof EntityDnaChicken)) {
                Entity chicken = event.getEntity();
                EntityDnaChicken replacement = new EntityDnaChicken(event.getWorld());
                replacement.setPosition(chicken.posX, chicken.posY, chicken.posZ);
                chicken.setDead();
                event.getWorld().removeEntity(chicken);
                event.getWorld().spawnEntityInWorld(replacement);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            byte[] mainconfigBytes = MendelCraft.dnaConfig.getMainconfigAsBytes();
            EntityPlayerMP playerMP = (EntityPlayerMP) event.player;
            MendelCraft.network.sendTo(new ConfigPacket("mainconfig", mainconfigBytes), playerMP);
            String[] animals = MendelCraft.dnaConfig.getAnimals();
            for (String animal : animals) {
                byte[] animalconfigBytes = MendelCraft.dnaConfig.getAnimalConfigAsBytes(animal);
                MendelCraft.network.sendTo(new ConfigPacket(animal, animalconfigBytes), playerMP);
            }
        }

//        MendelCraft.network.sendTo(new IMessage() {
//            @Override
//            public void fromBytes(ByteBuf buf) {
//
//            }
//
//            @Override
//            public void toBytes(ByteBuf buf) {
//
//            }
//        }, (EntityPlayerMP) event.player);
    }
}
