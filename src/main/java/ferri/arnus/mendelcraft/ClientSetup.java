package ferri.arnus.mendelcraft;

import ferri.arnus.mendelcraft.capability.DNAProvider;
import ferri.arnus.mendelcraft.entities.EntityRegistry;
import ferri.arnus.mendelcraft.gui.MonitorScreen;
import ferri.arnus.mendelcraft.gui.ContainerRegistry;
import ferri.arnus.mendelcraft.gui.LaboratoryScreen;
import ferri.arnus.mendelcraft.items.ItemRegistry;
import ferri.arnus.mendelcraft.items.SyringeColor;
import ferri.arnus.mendelcraft.renderer.DNAChickenModel;
import ferri.arnus.mendelcraft.renderer.DNAChickenRenderer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(bus = Bus.MOD, modid = MendelCraft.MODID, value = Dist.CLIENT)
public class ClientSetup {
	
	public static final ModelLayerLocation DNACHICKENMODEL = new ModelLayerLocation(new ResourceLocation(MendelCraft.MODID, "dnachicken"), "main");
	
	@SubscribeEvent
    static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(DNACHICKENMODEL, DNAChickenModel::createBodyLayer);
    }
	
	@SubscribeEvent
	public static void registerItemColor(ColorHandlerEvent.Item event) {
		event.getItemColors().register(new SyringeColor(), ItemRegistry.DNASYRINGE.get());
	}
	
	@SubscribeEvent
	static void registerRenderer(final FMLClientSetupEvent event) {
		EntityRenderers.register(EntityRegistry.DNACHICKEN.get(), (c) -> new DNAChickenRenderer(c));
		
		MenuScreens.register(ContainerRegistry.LABORATORY.get(), LaboratoryScreen::new);
		MenuScreens.register(ContainerRegistry.MONITOR.get(), MonitorScreen::new);
		
		ItemProperties.register(ItemRegistry.DNASYRINGE.get(), new ResourceLocation(MendelCraft.MODID,"colored"), (s,l,e,i) -> {
			boolean[] empty = new boolean[] {true};
			s.getCapability(DNAProvider.DNASTORAGE).ifPresent(cap -> {
				empty[0] = cap.isEmpty();
			});
			return empty[0]? 0 : 1F;
         });
	}
}
