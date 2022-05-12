package org.aertslab.mendelcraft;

import org.aertslab.mendelcraft.blockentities.BlockEntityRegistry;
import org.aertslab.mendelcraft.blocks.BlockRegistry;
import org.aertslab.mendelcraft.capability.DNAProvider;
import org.aertslab.mendelcraft.entities.EntityRegistry;
import org.aertslab.mendelcraft.gui.ContainerRegistry;
import org.aertslab.mendelcraft.items.DNAChickenSpawnEgg;
import org.aertslab.mendelcraft.items.DNASyringe;
import org.aertslab.mendelcraft.items.ItemRegistry;
import org.aertslab.mendelcraft.network.MendelCraftChannel;
import org.aertslab.mendelcraft.network.UpdateDNAEntityPacket;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.PacketDistributor;

@EventBusSubscriber
@Mod(MendelCraft.MODID)
public class MendelCraft {

	public static final String MODID = "mendelcraft";

	public MendelCraft() {
		BlockRegistry.registerBlocks();
		ItemRegistry.registerItems();
		BlockEntityRegistry.registerBlockEntities();
		EntityRegistry.registerEntities();
		ContainerRegistry.registerContainers();
		MendelCraftChannel.register();

		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addListener(this::registerAtributes);
		modEventBus.addListener(this::commonSetup);
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, org.aertslab.mendelcraft.config.ModConfig.SPEC);
	}

	private void registerAtributes(EntityAttributeCreationEvent event) {
		event.put(EntityRegistry.DNACHICKEN.get(), Chicken.createAttributes().build());
	}
	
	private void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			SpawnPlacements.register(EntityRegistry.DNACHICKEN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		});
		
	}
	
	
	@SubscribeEvent
	static void track(PlayerEvent.StartTracking event) {
		event.getTarget().getCapability(DNAProvider.DNASTORAGE).ifPresent(cap -> {
			if (!event.getTarget().level.isClientSide) {
				MendelCraftChannel.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> event.getTarget()),
						new UpdateDNAEntityPacket(event.getTarget().getId(), cap.serializeNBT()));
			}
		});
	}

	@SubscribeEvent
	static void capItemStack(AttachCapabilitiesEvent<ItemStack> event) {
		if (event.getObject().getItem() instanceof DNASyringe) {
			event.addCapability(new ResourceLocation(MendelCraft.MODID, "dna"), new DNAProvider());
		}
		if (event.getObject().getItem() instanceof DNAChickenSpawnEgg) {
			event.addCapability(new ResourceLocation(MendelCraft.MODID, "dna"), new DNAProvider());
		}
	}

	@SubscribeEvent
	static void babySpawn(BabyEntitySpawnEvent event) {
		event.getChild().getCapability(DNAProvider.DNASTORAGE).ifPresent(baby -> {
			event.getParentA().getCapability(DNAProvider.DNASTORAGE).ifPresent(parent1 -> {
				event.getParentB().getCapability(DNAProvider.DNASTORAGE).ifPresent(parent2 -> {
					if (!event.getParentA().level.isClientSide) {
						baby.setChromosomes(parent1.getChromosomes().get(event.getParentA().level.random.nextInt(2)),
								parent2.getChromosomes().get(event.getParentA().level.random.nextInt(2)));
					}
				});
			});
		});
	}
}
