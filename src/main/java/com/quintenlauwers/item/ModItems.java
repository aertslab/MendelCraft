package com.quintenlauwers.item;

import com.quintenlauwers.lib.RefStrings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {

    public static void mainRegistry() {
        initializeItems();
        regiserItem();
    }

    public static Item dnaSyringe;

    public static void initializeItems() {
        dnaSyringe = new dnaSyringe();

    }

    public static void regiserItem() {
        GameRegistry.register(dnaSyringe);
    }

    public static void registerRenders() {
        registerRender(dnaSyringe);
    }

    public static void registerRender(Item item) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(RefStrings.MODID + ":" + item.getUnlocalizedName().substring(5), "inventory"));
    }
}
