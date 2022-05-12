package org.aertslab.mendelcraft.items;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import org.aertslab.mendelcraft.capability.DNAProvider;
import org.aertslab.mendelcraft.entities.DNAChicken;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.RegistryObject;

public class DNAChickenSpawnEgg extends ForgeSpawnEggItem{

	public DNAChickenSpawnEgg(RegistryObject<EntityType<DNAChicken>> dnachicken, int pBackgroundColor, int pHighlightColor) {
		super(dnachicken, pBackgroundColor, pHighlightColor, new Properties().tab(ItemRegistry.DNA));
	}
	
	@Override
	public InteractionResult useOn(UseOnContext pContext) {
		Level level = pContext.getLevel();
		if (!(level instanceof ServerLevel)) {
			return InteractionResult.SUCCESS;
		} else {
			ItemStack itemstack = pContext.getItemInHand();
			BlockPos blockpos = pContext.getClickedPos();
			Direction direction = pContext.getClickedFace();
			BlockState blockstate = level.getBlockState(blockpos);
			if (blockstate.is(Blocks.SPAWNER)) {
				BlockEntity blockentity = level.getBlockEntity(blockpos);
				if (blockentity instanceof SpawnerBlockEntity) {
					BaseSpawner basespawner = ((SpawnerBlockEntity)blockentity).getSpawner();
					EntityType<?> entitytype1 = this.getType(itemstack.getTag());
					basespawner.setEntityId(entitytype1);
					blockentity.setChanged();
					level.sendBlockUpdated(blockpos, blockstate, blockstate, 3);
					itemstack.shrink(1);
					return InteractionResult.CONSUME;
				}
			}
			
			BlockPos blockpos1;
			if (blockstate.getCollisionShape(level, blockpos).isEmpty()) {
				blockpos1 = blockpos;
			} else {
				blockpos1 = blockpos.relative(direction);
			}
			
			EntityType<?> entitytype = this.getType(itemstack.getTag());
			AtomicReference<Entity> spawned2 = new AtomicReference<>();
			itemstack.getCapability(DNAProvider.DNASTORAGE).ifPresent(stack -> {
				CompoundTag tag =  new CompoundTag();
				CompoundTag dna =  new CompoundTag();
				if (!stack.isEmpty()) {
					stack.setEmpty(false);
					dna.put("DNA", stack.serializeNBT());
					tag.put("EntityTag", dna);
					dna.put("DNA", stack.serializeNBT());
					tag.put("EntityTag", dna);
				}
				spawned2.set(entitytype.spawn((ServerLevel)level,tag , null , pContext.getPlayer(), blockpos, MobSpawnType.SPAWN_EGG, true, !Objects.equals(blockpos, blockpos1) && direction == Direction.UP));
			});
			if (spawned2.get() != null) {
				itemstack.shrink(1);
				level.gameEvent(pContext.getPlayer(), GameEvent.ENTITY_PLACE, blockpos);
			}
			
			return InteractionResult.CONSUME;
		}
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
		ItemStack itemstack = pPlayer.getItemInHand(pHand);
		HitResult hitresult = getPlayerPOVHitResult(pLevel, pPlayer, ClipContext.Fluid.SOURCE_ONLY);
		if (hitresult.getType() != HitResult.Type.BLOCK) {
			return InteractionResultHolder.pass(itemstack);
		} else if (!(pLevel instanceof ServerLevel)) {
			return InteractionResultHolder.success(itemstack);
		} else {
			BlockHitResult blockhitresult = (BlockHitResult)hitresult;
			BlockPos blockpos = blockhitresult.getBlockPos();
			if (!(pLevel.getBlockState(blockpos).getBlock() instanceof LiquidBlock)) {
				return InteractionResultHolder.pass(itemstack);
			} else if (pLevel.mayInteract(pPlayer, blockpos) && pPlayer.mayUseItemAt(blockpos, blockhitresult.getDirection(), itemstack)) {
				EntityType<?> entitytype = this.getType(itemstack.getTag());
				AtomicReference<Entity> spawned2 = new AtomicReference<>();
				itemstack.getCapability(DNAProvider.DNASTORAGE).ifPresent(stack -> {
					CompoundTag tag =  new CompoundTag();
					CompoundTag dna =  new CompoundTag();
					if (!stack.isEmpty()) {
						dna.put("DNA", stack.serializeNBT());
						tag.put("EntityTag", dna);
					}
					spawned2.set(entitytype.spawn((ServerLevel)pLevel,tag , null , pPlayer, blockpos, MobSpawnType.SPAWN_EGG, false, false));
				});
				if (spawned2.get() == null) {
					return InteractionResultHolder.pass(itemstack);
				} else {
					if (!pPlayer.getAbilities().instabuild) {
						itemstack.shrink(1);
					}
					pPlayer.awardStat(Stats.ITEM_USED.get(this));
					pLevel.gameEvent(GameEvent.ENTITY_PLACE, pPlayer);
					return InteractionResultHolder.consume(itemstack);
				}
			} else {
				return InteractionResultHolder.fail(itemstack);
			}
		}
	}
}
