package ferri.arnus.mendelcraft;

import ferri.arnus.mendelcraft.blockentities.BlockEntityRegistry;
import ferri.arnus.mendelcraft.blocks.BlockRegistry;
import ferri.arnus.mendelcraft.capability.DNAProvider;
import ferri.arnus.mendelcraft.entities.EntityRegistry;
import ferri.arnus.mendelcraft.gui.ContainerRegistry;
import ferri.arnus.mendelcraft.items.DNAChickenSpawnEgg;
import ferri.arnus.mendelcraft.items.DNASyringe;
import ferri.arnus.mendelcraft.items.ItemRegistry;
import ferri.arnus.mendelcraft.network.MendelCraftChannel;
import ferri.arnus.mendelcraft.network.UpdateDNAEntityPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.ItemStack;
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
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

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
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ferri.arnus.mendelcraft.config.ModConfig.SPEC); 
		
	}
	
	private void registerAtributes(EntityAttributeCreationEvent event) {
		event.put(EntityRegistry.DNACHICKEN.get(), Chicken.createAttributes().build());
	}
	
	@SubscribeEvent
	static void track(PlayerEvent.StartTracking event) {
		event.getTarget().getCapability(DNAProvider.DNASTORAGE).ifPresent(cap -> {
			if (!event.getTarget().level.isClientSide) {
				MendelCraftChannel.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> event.getTarget()), new UpdateDNAEntityPacket(event.getTarget().getId(), cap.serializeNBT()));
			}
		});
	}
	
	@SubscribeEvent
	static void capItemStack(AttachCapabilitiesEvent<ItemStack> event) {
		if (event.getObject().getItem() instanceof DNASyringe) {
			event.addCapability(new ResourceLocation(MendelCraft.MODID, "dna"),new DNAProvider());
		}
		if (event.getObject().getItem() instanceof DNAChickenSpawnEgg) {
			event.addCapability(new ResourceLocation(MendelCraft.MODID, "dna"),new DNAProvider());
		}
	}
	
	@SubscribeEvent
	static void babySpawn(BabyEntitySpawnEvent event) {
		event.getChild().getCapability(DNAProvider.DNASTORAGE).ifPresent(baby -> {
			event.getParentA().getCapability(DNAProvider.DNASTORAGE).ifPresent(parent1 -> {
				event.getParentB().getCapability(DNAProvider.DNASTORAGE).ifPresent(parent2 -> {
					if (!event.getParentA().level.isClientSide) {
						baby.setChromosomes(parent1.getChromosomes().get(event.getParentA().level.random.nextInt(2)), parent2.getChromosomes().get(event.getParentA().level.random.nextInt(2)));					
					}
				});
			});
		});
	}
}
