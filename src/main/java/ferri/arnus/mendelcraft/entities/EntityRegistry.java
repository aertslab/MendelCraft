package ferri.arnus.mendelcraft.entities;

import ferri.arnus.mendelcraft.MendelCraft;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityRegistry {
	
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MendelCraft.MODID);
	
	public static void registerEntities() {
		ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	public static final RegistryObject<EntityType<DNAChicken>> DNACHICKEN = ENTITIES.register("dnachicken", ()-> EntityType.Builder.<DNAChicken>of(DNAChicken::new, MobCategory.AMBIENT).build("dnachicken"));

}
