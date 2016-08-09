package com.quintenlauwers.backend;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import java.util.concurrent.Callable;

/**
 * Created by quinten on 9/08/16.
 */
public class CapabiltyDnaData {

        @CapabilityInject(IDnaHandler.class)
        public static Capability<IDnaHandler> DNA_IN_ANIMAL = null;

        public static void register()
        {
            CapabilityManager.INSTANCE.register(IDnaHandler.class, new Capability.IStorage<IDnaHandler>()
            {
                @Override
                public NBTBase writeNBT(Capability<IDnaHandler> capability, IDnaHandler instance, EnumFacing side)
                {
                    NBTTagList nbtTagList = new NBTTagList();
                    return nbtTagList;
                }

                @Override
                public void readNBT(Capability<IDnaHandler> capability, IDnaHandler instance, EnumFacing side, NBTBase base) {
                }
            }, new Callable<DnaDataHandler>()
            {
                @Override
                public DnaDataHandler call() throws Exception
                {
                    return new DnaDataHandler();
                }
            });
        }


    }
