package ferri.arnus.mendelcraft.items;

import ferri.arnus.mendelcraft.capability.DNAProvider;
import ferri.arnus.mendelcraft.entities.DNAChicken;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.fmllegacy.RegistryObject;

public class DNAChickenSpawnEgg extends SpawnEggItem{
	private RegistryObject<EntityType<DNAChicken>> type;

	public DNAChickenSpawnEgg(RegistryObject<EntityType<DNAChicken>> dnachicken, int pBackgroundColor, int pHighlightColor) {
		super(null, pBackgroundColor, pHighlightColor, new Properties());
		this.type = dnachicken;
	}
	
	@Override
	public EntityType<?> getType(CompoundTag pNbt) {
		if (pNbt != null && pNbt.contains("EntityTag", 10)) {
			CompoundTag compoundtag = pNbt.getCompound("EntityTag");
			if (compoundtag.contains("id", 8)) {
				return EntityType.byString(compoundtag.getString("id")).orElse(this.type.get());
			}
		}
		
		return this.type.get();
	}
	
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
				Entity spawned = entitytype.spawn((ServerLevel)pLevel, itemstack, pPlayer, blockpos, MobSpawnType.SPAWN_EGG, false, false);
				if (spawned == null) {
					return InteractionResultHolder.pass(itemstack);
				} else {
					if (!pPlayer.getAbilities().instabuild) {
						itemstack.shrink(1);
					}
					spawned.getCapability(DNAProvider.DNASTORAGE).ifPresent(entity -> {
						itemstack.getCapability(DNAProvider.DNASTORAGE).ifPresent(stack -> {
							entity.setChromosomes(stack.getChromosomes());
						});
					});
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
