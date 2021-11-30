package ferri.arnus.mendelcraft.gui;

import ferri.arnus.mendelcraft.MendelCraft;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ContainerRegistry {
	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MendelCraft.MODID);
	
	public static void registerContainers() {
		CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	public static final RegistryObject<MenuType<LaboratoryContainer>> LABORATORY = CONTAINERS.register("laboratory", ()-> IForgeMenuType.create(LaboratoryContainer::new));

}

