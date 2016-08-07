package com.quintenlauwers.main;

import com.quintenlauwers.blocks.CompressedStone;
import com.quintenlauwers.item.ObsidianStick;
import com.quintenlauwers.lib.RefStrings;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatCrafting;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = RefStrings.MODID, name = RefStrings.NAME, version = RefStrings.VERSION)
public class TestMod {

    @Instance
    public static TestMod instance = new TestMod();

    @SidedProxy(clientSide = RefStrings.CLIENTSIDE, serverSide = RefStrings.SERVERSIDE)
    public static ServerProxy proxy;

    static ItemStack dirtStack = new ItemStack(Blocks.DIRT);
    static ItemStack obsidianStack = new ItemStack(Blocks.OBSIDIAN);

    @EventHandler
    public static void preLoad(FMLPreInitializationEvent preEvent) {
        ObsidianStick.mainRegistry();
//        CompressedStone.mainRegistry();
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
