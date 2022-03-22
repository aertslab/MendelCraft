package ferri.arnus.mendelcraft.blocks;

import ferri.arnus.mendelcraft.MendelCraft;
import ferri.arnus.mendelcraft.items.ItemRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistry {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MendelCraft.MODID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MendelCraft.MODID);
	
	public static void registerBlocks() {
		BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	public static final RegistryObject<Block> LABORATORY = BLOCKS.register("laboratory", ()-> new Laboratory());
	public static final RegistryObject<Item> LABORATORY_ITEM = ITEMS.register("laboratory", () -> new BlockItem(LABORATORY.get(), new Properties().tab(ItemRegistry.DNA)));
	
	public static final RegistryObject<Monitor> MONITOR = BLOCKS.register("monitor", ()-> new Monitor());
	public static final RegistryObject<Item> MONITOR_ITEM = ITEMS.register("monitor", () -> new BlockItem(MONITOR.get(), new Properties().tab(ItemRegistry.DNA)));
}
