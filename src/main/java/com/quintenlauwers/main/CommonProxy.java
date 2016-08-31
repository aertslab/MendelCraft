package com.quintenlauwers.main;

import com.quintenlauwers.interfaces.ModGuiHandler;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 * Created by quinten on 8/08/16.
 */
public class CommonProxy {
    public void registerRenderInfo() {
        NetworkRegistry.INSTANCE.registerGuiHandler(MendelCraft.instance, new ModGuiHandler());
    }

    public void registerItemRenderer(Item item, int meta, String id) {

    }

    public void preRender() {

    }
}
