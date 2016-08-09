package com.quintenlauwers.main;

import com.quintenlauwers.item.ObsidianStick;
import com.quintenlauwers.lib.RefStrings;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderInfo() {
        ObsidianStick.registerRenders();
        super.registerRenderInfo();
    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(RefStrings.MODID + ":" + id, "inventory"));
    }
}
