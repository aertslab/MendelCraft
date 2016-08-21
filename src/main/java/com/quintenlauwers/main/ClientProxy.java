package com.quintenlauwers.main;

import com.quintenlauwers.entity.ModEntities;
import com.quintenlauwers.item.ModItems;
import com.quintenlauwers.lib.RefStrings;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderInfo() {
        ModItems.registerRenders();
        super.registerRenderInfo();
    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(RefStrings.MODID + ":" + id, "inventory"));
    }

    @Override
    public void preRender() {
        ModEntities.registerRenderers();
        super.preRender();
    }
}
