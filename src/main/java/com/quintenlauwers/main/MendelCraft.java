package com.quintenlauwers.main;

import com.quintenlauwers.backend.DnaConfig;
import com.quintenlauwers.backend.network.NetworkHelper;
import com.quintenlauwers.backend.util.Storage;
import com.quintenlauwers.blocks.ModBlocks;
import com.quintenlauwers.entity.ModEntities;
import com.quintenlauwers.events.ModEventHandler;
import com.quintenlauwers.item.ModItems;
import com.quintenlauwers.lib.RefStrings;
import com.quintenlauwers.tileentity.TileEntityLab;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

@Mod(modid = RefStrings.MODID, name = RefStrings.NAME, version = RefStrings.VERSION, canBeDeactivated = true)
public class MendelCraft {

    @Instance
    public static MendelCraft instance = new MendelCraft();

    @SidedProxy(clientSide = RefStrings.CLIENTSIDE, serverSide = RefStrings.SERVERSIDE)
    public static CommonProxy proxy;

    static ModEventHandler modEventHandler = new ModEventHandler();
    public static SimpleNetworkWrapper network;
    public static NetworkHelper networkHelper;
    public static Storage storage;
    public static DnaConfig dnaConfig;
    public static Random random = new Random();

    @EventHandler
    public static void preLoad(FMLPreInitializationEvent preEvent) {
        String configLocation = preEvent.getModConfigurationDirectory().toString();
        System.out.println(configLocation);
        File mainConfig = new File(configLocation + "/" + "mainConfig.json");
        if (!mainConfig.exists() || mainConfig.isDirectory()) {
            System.out.println("about to copy");
            copyConfig(configLocation);
        }
        network = NetworkRegistry.INSTANCE.newSimpleChannel(RefStrings.MODID);
        networkHelper = new NetworkHelper();
        networkHelper.registerMessages();
        storage = new Storage();
        dnaConfig = new DnaConfig(configLocation, "mainConfig.json");
        GameRegistry.registerTileEntity(TileEntityLab.class, "122");
        ModItems.mainRegistry();
        ModBlocks.init();
        ModEntities.init();
        MinecraftForge.EVENT_BUS.register(modEventHandler);
        proxy.preRender();
        System.err.println("Preload");
    }

    @EventHandler
    public static void load(FMLInitializationEvent event) {
        System.err.println("Actual load");
        GameRegistry.addRecipe(new ItemStack(ModItems.dnaSyringe), " m ", "mgm", "mgm", 'g', new ItemStack(Blocks.GLASS), 'm', new ItemStack(Items.IRON_INGOT));
        GameRegistry.addRecipe(new ItemStack(ModBlocks.dnaLab), "mmm", "mdm", "mmm", 'm', new ItemStack(Items.IRON_INGOT), 'd', new ItemStack(Items.DIAMOND));
        GameRegistry.addRecipe(new ItemStack(ModItems.inspector), "ddd", "dgd", "ddd", 'g', new ItemStack(Blocks.GLASS), 'd', new ItemStack(Items.DIAMOND));
        proxy.registerRenderInfo();
    }

    @EventHandler
    public static void postLoad(FMLPostInitializationEvent postEvent) {
        System.err.println("Postload");
    }

    static public void copyConfig(String destFolder) {
        String[] configFiles = {"mainConfig.json", "chickenConfig.json"};
        InputStream stream = null;
        OutputStream resStreamOut = null;
        for (String configFile : configFiles) {
            try {
                stream = MendelCraft.class.getResourceAsStream("/assets/mendelcraft/config/" + configFile);
                int readBytes;
                byte[] buffer = new byte[4096];
                resStreamOut = new FileOutputStream(destFolder + "/" + configFile);
                while ((readBytes = stream.read(buffer)) > 0) {
                    resStreamOut.write(buffer, 0, readBytes);
                }
            } catch (Exception ex) {
                System.err.println("Could not copy config file " + configFile + " to .minecraft/config");
                System.err.println(ex);

                try {
                    stream.close();
                    resStreamOut.close();
                } catch (Exception e) {
                    System.err.println("Error in closing streams.");
                    System.err.println(e);
                }

            }
        }
    }
}
