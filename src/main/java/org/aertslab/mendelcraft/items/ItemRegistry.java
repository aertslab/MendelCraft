package org.aertslab.mendelcraft.items;

import org.aertslab.mendelcraft.MendelCraft;
import org.aertslab.mendelcraft.entities.EntityRegistry;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MendelCraft.MODID);

	public static void registerItems() {
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	public static RegistryObject<Item> DNACHICKENSPAWNEGG = ITEMS.register("dnachickenspawnegg", () -> new DNAChickenSpawnEgg(EntityRegistry.DNACHICKEN, 0, 0x0000FF));
	public static RegistryObject<Item> DNASYRINGE = ITEMS.register("dnasyringe", () -> new DNASyringe());
	
	public static CreativeModeTab DNA = new CreativeModeTab("mendelcraft") {
		
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(DNACHICKENSPAWNEGG.get());
		}
	};
}
