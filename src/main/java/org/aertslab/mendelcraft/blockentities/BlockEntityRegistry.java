package org.aertslab.mendelcraft.blockentities;

import org.aertslab.mendelcraft.MendelCraft;
import org.aertslab.mendelcraft.blocks.BlockRegistry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegistry {

	public static final DeferredRegister<BlockEntityType<?>> BLOCKENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MendelCraft.MODID);
	
	public static void registerBlockEntities() {
		BLOCKENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	public static final RegistryObject<BlockEntityType<LaboratoryBlockEntity>> LABORATORY = BLOCKENTITIES.register("laboratory", () -> BlockEntityType.Builder.of((p,s) -> new LaboratoryBlockEntity(p, s), BlockRegistry.LABORATORY.get()).build(null));
}
