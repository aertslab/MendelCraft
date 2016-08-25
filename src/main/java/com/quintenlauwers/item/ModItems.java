package com.quintenlauwers.item;

import com.quintenlauwers.lib.RefStrings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {
    private static ModelResourceLocation syringeFullTexture = new ModelResourceLocation(RefStrings.MODID + ":" + "dnaSyringeFull", "inventory");
    private static ModelResourceLocation syringeEmptyTexture = new ModelResourceLocation(RefStrings.MODID + ":" + "dnaSyringeEmpty", "inventory");

    public static void mainRegistry() {
        initializeItems();
        preRegister();
        regiserItem();
    }

    public static void preRegister() {
        ModelBakery.registerItemVariants(dnaSyringe, syringeEmptyTexture, syringeFullTexture);
    }

    public static Item dnaSyringe;

    public static void initializeItems() {
        dnaSyringe = new dnaSyringe();

    }

    public static void regiserItem() {
        GameRegistry.register(dnaSyringe);
    }

    public static void registerRenders() {
        registerComplexRender(dnaSyringe, 0, syringeEmptyTexture);
        registerComplexRender(dnaSyringe, 1, syringeFullTexture);
    }

    public static void registerRender(Item item) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(RefStrings.MODID + ":" + item.getUnlocalizedName().substring(5), "inventory"));
    }

    public static void registerComplexRender(Item item, int meta, ModelResourceLocation file) {
        System.out.println("registering on position " + item.getUnlocalizedName().substring(5));
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, file);
    }
}
