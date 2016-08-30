package com.quintenlauwers.item;

import com.quintenlauwers.lib.RefStrings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {
    private static ModelResourceLocation syringeFullRedTexture = new ModelResourceLocation(RefStrings.MODID + ":" + "dnaSyringeFullRed", "inventory");
    private static ModelResourceLocation syringeFullBlueTexture = new ModelResourceLocation(RefStrings.MODID + ":" + "dnaSyringeFullBlue", "inventory");
    private static ModelResourceLocation syringeFullGreenTexture = new ModelResourceLocation(RefStrings.MODID + ":" + "dnaSyringeFullGreen", "inventory");
    private static ModelResourceLocation syringeFullOrangeTexture = new ModelResourceLocation(RefStrings.MODID + ":" + "dnaSyringeFullOrange", "inventory");
    private static ModelResourceLocation syringeFullPurpleTexture = new ModelResourceLocation(RefStrings.MODID + ":" + "dnaSyringeFullPurple", "inventory");
    private static ModelResourceLocation syringeFullYellowTexture = new ModelResourceLocation(RefStrings.MODID + ":" + "dnaSyringeFullYellow", "inventory");
    private static ModelResourceLocation syringeEmptyTexture = new ModelResourceLocation(RefStrings.MODID + ":" + "dnaSyringeEmpty", "inventory");
    private static ModelResourceLocation inspectorTexture = new ModelResourceLocation(RefStrings.MODID + ":" + "dnaInspector", "inventory");

    public static void mainRegistry() {
        initializeItems();

        regiserItem();
    }

    public static void clientRegistry() {
        preRegister();
    }

    public static void preRegister() {
        ModelBakery.registerItemVariants(dnaSyringe,
                syringeEmptyTexture,
                syringeFullRedTexture,
                syringeFullBlueTexture,
                syringeFullGreenTexture,
                syringeFullOrangeTexture,
                syringeFullPurpleTexture,
                syringeFullYellowTexture);
    }

    public static Item dnaSyringe;
    public static Item inspector;

    public static void initializeItems() {
        dnaSyringe = new dnaSyringe();
        inspector = new dnaInspector();

    }

    public static void regiserItem() {
        GameRegistry.register(dnaSyringe);
        GameRegistry.register(inspector);
    }

    public static void registerRenders() {
        registerComplexRender(dnaSyringe, 0, syringeEmptyTexture);
        registerComplexRender(dnaSyringe, 1, syringeFullRedTexture);
        registerComplexRender(dnaSyringe, 2, syringeFullBlueTexture);
        registerComplexRender(dnaSyringe, 3, syringeFullGreenTexture);
        registerComplexRender(dnaSyringe, 4, syringeFullOrangeTexture);
        registerComplexRender(dnaSyringe, 5, syringeFullPurpleTexture);
        registerComplexRender(dnaSyringe, 6, syringeFullYellowTexture);
        registerComplexRender(inspector, 0, inspectorTexture);

    }

    public static void registerRender(Item item) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0,
                new ModelResourceLocation(RefStrings.MODID + ":" + item.getUnlocalizedName().substring(5),
                        "inventory"));
    }

    public static void registerComplexRender(Item item, int meta, ModelResourceLocation file) {
        System.out.println("registering on position " + item.getUnlocalizedName().substring(5));
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, file);
    }
}
