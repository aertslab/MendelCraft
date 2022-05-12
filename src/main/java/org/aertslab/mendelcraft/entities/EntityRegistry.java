package org.aertslab.mendelcraft.entities;

import java.util.ArrayList;
import java.util.Iterator;

import org.aertslab.mendelcraft.MendelCraft;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = MendelCraft.MODID)
public class EntityRegistry {
	
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MendelCraft.MODID);
	
	public static void registerEntities() {
		ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	public static final RegistryObject<EntityType<DNAChicken>> DNACHICKEN = ENTITIES.register("dnachicken", ()-> EntityType.Builder.<DNAChicken>of(DNAChicken::new, MobCategory.CREATURE).sized(0.4F, 0.7F).clientTrackingRange(10).build("dnachicken"));

	@SubscribeEvent(priority = EventPriority.HIGH)
	static void addChickens(BiomeLoadingEvent event) {
		ArrayList<SpawnerData> list = new ArrayList<>(event.getSpawns().getSpawner(MobCategory.CREATURE));
		list.forEach(e -> {
			if (e.type.equals(EntityType.CHICKEN)) {
				SpawnerData data = new SpawnerData(DNACHICKEN.get(), e.getWeight(), e.minCount, e.maxCount);
				event.getSpawns().addSpawn(MobCategory.CREATURE, data);
			}
		});
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	static void removeChickens(BiomeLoadingEvent event) {
		Iterator<SpawnerData> iterator = event.getSpawns().getSpawner(MobCategory.CREATURE).iterator();
		while (iterator.hasNext()) {
			if (iterator.next().type.equals(EntityType.CHICKEN)) {
				iterator.remove();
			}

		}
	}

}
