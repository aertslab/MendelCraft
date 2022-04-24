package ferri.arnus.mendelcraft.entities;

import javax.annotation.Nullable;

import ferri.arnus.mendelcraft.capability.DNAProvider;
import ferri.arnus.mendelcraft.capability.DNAStorage;
import ferri.arnus.mendelcraft.capability.IDNAStorage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class DNAChicken extends Chicken implements Saddleable{
	private IDNAStorage storage = DNAStorage.random(this.level);
	private boolean saddled;
	
	public DNAChicken(EntityType<? extends Chicken> p_28236_, Level p_28237_) {
		super(EntityRegistry.DNACHICKEN.get(), p_28237_);
	}
	
	@Override
	public Chicken getBreedOffspring(ServerLevel p_148884_, AgeableMob p_148885_) {
		return EntityRegistry.DNACHICKEN.get().create(p_148884_);
	}
	
	@Override
	public void spawnChildFromBreeding(ServerLevel p_27564_, Animal p_27565_) {
		super.spawnChildFromBreeding(p_27564_, p_27565_);
		this.setAge(-3);
		p_27565_.setAge(-3);
	}
	
	@Override
	public void setBaby(boolean p_146756_) {
		this.setAge(p_146756_ ? -3 : 0);
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag p_28257_) {
		super.addAdditionalSaveData(p_28257_);
		this.getCapability(DNAProvider.DNASTORAGE).ifPresent(s -> p_28257_.put("DNA", s.serializeNBT()));
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag p_28243_) {
		super.readAdditionalSaveData(p_28243_);
		this.getCapability(DNAProvider.DNASTORAGE).ifPresent(s -> s.deserializeNBT(p_28243_.getCompound("DNA")));
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap) {
		if (cap == DNAProvider.DNASTORAGE) {
			return LazyOptional.of(() -> storage).cast();
		}
		return super.getCapability(cap);
	}
	
	@Override
	public boolean isSaddleable() {
		return true;
	}
	
	@Override
	public void equipSaddle(SoundSource p_21748_) {
		this.saddled = true;
		
	}
	
	@Override
	public boolean isSaddled() {
		return this.saddled;
	}
	
	protected void doPlayerRide(Player p_30634_) {
		if (!this.level.isClientSide) {
			p_30634_.setYRot(this.getYRot());
			p_30634_.setXRot(this.getXRot());
			p_30634_.startRiding(this);
		}
		
	}
	
	@Override
	public void travel(Vec3 p_30633_) {
		if (this.isAlive()) {
			if (this.isVehicle() && this.canBeControlledByRider() && this.isSaddled()) {
				LivingEntity livingentity = (LivingEntity)this.getControllingPassenger();
				this.setYRot(livingentity.getYRot());
				this.yRotO = this.getYRot();
				this.setXRot(livingentity.getXRot() * 0.5F);
				this.setRot(this.getYRot(), this.getXRot());
				this.yBodyRot = this.getYRot();
				this.yHeadRot = this.yBodyRot;
				float f = livingentity.xxa * 0.5F;
	            float f1 = livingentity.zza;
				
				if (this.isControlledByLocalInstance()) {
					this.setSpeed((float)this.getAttributeValue(Attributes.MOVEMENT_SPEED));
					super.travel(new Vec3((double)f, p_30633_.y, (double)f1));
				} else if (livingentity instanceof Player) {
					this.setDeltaMovement(Vec3.ZERO);
				}
				
				this.calculateEntityAnimation(this, false);
				this.tryCheckInsideBlocks();
			} else {
				System.out.println("why");
				this.flyingSpeed = 0.02F;
				super.travel(p_30633_);
			}
		}
	}
	
	@Override
	public InteractionResult mobInteract(Player p_27584_, InteractionHand p_27585_) {
		ItemStack itemstack = p_27584_.getItemInHand(p_27585_);
		if (this.isVehicle()) {
			return super.mobInteract(p_27584_, p_27585_);
		}
		if (itemstack.is(Items.SADDLE)) {
			this.saddled = true;
			return InteractionResult.sidedSuccess(this.level.isClientSide);
		}
		this.doPlayerRide(p_27584_);
		return InteractionResult.sidedSuccess(this.level.isClientSide);
	}
	
	public boolean canBeControlledByRider() {
		return this.getControllingPassenger() instanceof LivingEntity;
	}
	
	@Nullable
	public Entity getControllingPassenger() {
		return this.getFirstPassenger();
	}
	
}
