package com.quintenlauwers.main;

import com.quintenlauwers.interfaces.ModGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class ServerProxy extends  CommonProxy{
    @Override
    public void registerRenderInfo() {
        NetworkRegistry.INSTANCE.registerGuiHandler(MendelCraft.instance, new ModGuiHandler());
//        ModItems.registerRenders();
    }
}
