package com.quintenlauwers.main;

import com.quintenlauwers.blocks.ModBlocks;
import com.quintenlauwers.entity.ModEntities;
import com.quintenlauwers.item.ObsidianStick;
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

@Mod(modid = RefStrings.MODID, name = RefStrings.NAME, version = RefStrings.VERSION)
public class TestMod {

    @Instance
    public static TestMod instance = new TestMod();

    @SidedProxy(clientSide = RefStrings.CLIENTSIDE, serverSide = RefStrings.SERVERSIDE)
    public static CommonProxy proxy;

    static ItemStack dirtStack = new ItemStack(Blocks.DIRT);
    static ItemStack obsidianStack = new ItemStack(Blocks.OBSIDIAN);

    static com.quintenlauwers.events.EventHandler eventHandler = new com.quintenlauwers.events.EventHandler();
    static SimpleNetworkWrapper network;

    @EventHandler
    public static void preLoad(FMLPreInitializationEvent preEvent) {
        network = NetworkRegistry.INSTANCE.newSimpleChannel(RefStrings.MODID);
        ObsidianStick.mainRegistry();
        ModBlocks.init();
        ModEntities.init();
        MinecraftForge.EVENT_BUS.register(eventHandler);
        System.err.println("Preload");
    }

    @EventHandler
    public static void load(FMLInitializationEvent event) {
        System.err.println("Actual load");
		GameRegistry.addShapelessRecipe(new ItemStack(ObsidianStick.oStick), new ItemStack(Blocks.OBSIDIAN));
        proxy.registerRenderInfo();
    }

    @EventHandler
    public static void postLoad(FMLPostInitializationEvent postEvent) {
        System.err.println("Postload");
    }
}
