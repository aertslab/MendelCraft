package org.aertslab.mendelcraft.items;

import org.aertslab.mendelcraft.capability.DNAProvider;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class DNASyringe extends Item{

	public DNASyringe() {
		super(new Properties().stacksTo(1).tab(ItemRegistry.DNA));
	}
	
	@Override
	public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
		if (pInteractionTarget instanceof Chicken) {
			pStack.getCapability(DNAProvider.DNASTORAGE).ifPresent(stack -> {
				pInteractionTarget.getCapability(DNAProvider.DNASTORAGE).ifPresent(chicken -> {
					if (pPlayer.isCreative()) {
						ItemStack newStack = pStack.copy();
						newStack.getCapability(DNAProvider.DNASTORAGE).ifPresent(s-> {
							s.setChromosomes(chicken.getChromosomes());
							s.setEmpty(false);
						});
						pPlayer.addItem(newStack);
					}else {
						stack.setChromosomes(chicken.getChromosomes());
						stack.setEmpty(false);
					}
				});
			});
			return InteractionResult.SUCCESS;
		}
		return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand);
	}
	
	@Override
	public CompoundTag getShareTag(ItemStack stack) {
		CompoundTag tag = super.getShareTag(stack)==null? stack.getOrCreateTag() : super.getShareTag(stack);
		stack.getCapability(DNAProvider.DNASTORAGE).ifPresent(cap -> {
			tag.put("DNA", cap.serializeNBT());
		});
		return tag;
	}
	
	@Override
	public void readShareTag(ItemStack stack, CompoundTag nbt) {
		stack.getCapability(DNAProvider.DNASTORAGE).ifPresent(cap -> {
			cap.deserializeNBT(nbt.getCompound("DNA"));
		});
		super.readShareTag(stack, nbt);
	}
}
