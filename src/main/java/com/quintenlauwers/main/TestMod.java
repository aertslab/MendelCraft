package com.quintenlauwers.main;

import com.quintenlauwers.backend.DnaConfig;
import com.quintenlauwers.backend.network.NetworkHelper;
import com.quintenlauwers.backend.util.Storage;
import com.quintenlauwers.blocks.ModBlocks;
import com.quintenlauwers.entity.ModEntities;
import com.quintenlauwers.item.ModItems;
import com.quintenlauwers.lib.RefStrings;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Random;

@Mod(modid = RefStrings.MODID, name = RefStrings.NAME, version = RefStrings.VERSION)
public class TestMod {

    @Instance
    public static TestMod instance = new TestMod();

    @SidedProxy(clientSide = RefStrings.CLIENTSIDE, serverSide = RefStrings.SERVERSIDE)
    public static CommonProxy proxy;

    static com.quintenlauwers.events.EventHandler eventHandler = new com.quintenlauwers.events.EventHandler();
    public static SimpleNetworkWrapper network;
    public static NetworkHelper networkHelper;
    public static Storage storage;
    public static DnaConfig dnaConfig;
    public static Random random = new Random();

    @EventHandler
    public static void preLoad(FMLPreInitializationEvent preEvent) {
        network = NetworkRegistry.INSTANCE.newSimpleChannel(RefStrings.MODID);
        networkHelper = new NetworkHelper();
        networkHelper.registerMessages();
        storage = new Storage();
        dnaConfig = new DnaConfig("mainConfig.json");
        ModItems.mainRegistry();
        ModBlocks.init();
        ModEntities.init();
        MinecraftForge.EVENT_BUS.register(eventHandler);
        proxy.preRender();
        System.err.println("Preload");
    }

    @EventHandler
    public static void load(FMLInitializationEvent event) {
        System.err.println("Actual load");
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.dnaSyringe), new ItemStack(Blocks.OBSIDIAN));
        proxy.registerRenderInfo();
    }

    @EventHandler
    public static void postLoad(FMLPostInitializationEvent postEvent) {
        System.err.println("Postload");
    }
}
