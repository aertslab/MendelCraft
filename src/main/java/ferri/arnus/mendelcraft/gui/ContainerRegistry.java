package ferri.arnus.mendelcraft.gui;

import ferri.arnus.mendelcraft.MendelCraft;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerRegistry {
	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MendelCraft.MODID);
	
	public static void registerContainers() {
		CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	public static final RegistryObject<MenuType<LaboratoryContainer>> LABORATORY = CONTAINERS.register("laboratory", ()-> IForgeContainerType.create(LaboratoryContainer::new));

}

