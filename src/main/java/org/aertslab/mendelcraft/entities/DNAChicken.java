package org.aertslab.mendelcraft.entities;

import javax.annotation.Nullable;

import org.aertslab.mendelcraft.capability.DNAProvider;
import org.aertslab.mendelcraft.capability.DNAStorage;
import org.aertslab.mendelcraft.capability.DNAUtil;
import org.aertslab.mendelcraft.capability.IDNAStorage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class DNAChicken extends Chicken implements PlayerRideableJumping{
	private IDNAStorage storage = DNAStorage.random(this.level);
	protected boolean isJumping;
	protected float playerJumpPendingScale;

	public DNAChicken(EntityType<? extends Chicken> p_28236_, Level p_28237_) {
		super(EntityRegistry.DNACHICKEN.get(), p_28237_);
		//this.maxUpStep = 1F;
	}
	
	@Override
	public Chicken getBreedOffspring(ServerLevel p_148884_, AgeableMob p_148885_) {
		return EntityRegistry.DNACHICKEN.get().create(p_148884_);
	}
	
	@Override
	public void spawnChildFromBreeding(ServerLevel p_27564_, Animal p_27565_) {
		super.spawnChildFromBreeding(p_27564_, p_27565_);
		this.setAge(0);
		p_27565_.setAge(0);
	}
	
	@Override
	public void setBaby(boolean p_146756_) {
		this.setAge(p_146756_ ? -100 : 0);
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
	public double getX(double p_20166_) {
		if (DNAUtil.isBig(this)) {
			return this.position().x + 1.48D * p_20166_;
		}
		return super.getX(p_20166_);
	}
	
	@Override
	public double getY(double p_20228_) {
		if (DNAUtil.isBig(this)) {
			return this.position().y + 4.5D * p_20228_;
		}		
		return super.getY(p_20228_);
	}
	
	@Override
	public double getZ(double p_20247_) {
		if (DNAUtil.isBig(this)) {
			return this.position().z + 1.48D * p_20247_;
		}		
		return super.getZ(p_20247_);
	}
	
	
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap) {
		if (cap == DNAProvider.DNASTORAGE) {
			return LazyOptional.of(() -> storage).cast();
		}
		return super.getCapability(cap);
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
			if (this.isVehicle() && this.canBeControlledByRider() && DNAUtil.isBig(this)) {
				LivingEntity livingentity = (LivingEntity)this.getControllingPassenger();
				this.setYRot(livingentity.getYRot());
				this.yRotO = this.getYRot();
				this.setXRot(livingentity.getXRot() * 0.5F);
				this.setRot(this.getYRot(), this.getXRot());
				this.yBodyRot = this.getYRot();
				//this.yHeadRot = this.yBodyRot;
				float f = livingentity.xxa * 0.5F;
	            float f1 = livingentity.zza;
				
	            if (this.playerJumpPendingScale > 0.0F && this.onGround && !this.isJumping()) {
	            	double d0 = (double)this.playerJumpPendingScale * (double)this.getBlockJumpFactor();
	            	double d1 = d0 + this.getJumpBoostPower();
	            	Vec3 vec3 = this.getDeltaMovement();
	            	this.setDeltaMovement(vec3.x, d1, vec3.z);
	            	this.setIsJumping(true);
	            	this.hasImpulse = true;
	            	net.minecraftforge.common.ForgeHooks.onLivingJump(this);
	            	if (f1 > 0.0F) {
	            		float f2 = Mth.sin(this.getYRot() * ((float)Math.PI / 180F));
	            		float f3 = Mth.cos(this.getYRot() * ((float)Math.PI / 180F));
	            		this.setDeltaMovement(this.getDeltaMovement().add((double)(-0.4F * f2 * this.playerJumpPendingScale), 0.0D, (double)(0.4F * f3 * this.playerJumpPendingScale)));
	            	}
	            	this.playerJumpPendingScale = 0.0F;
	            }
	            
				if (this.isControlledByLocalInstance()) {
					this.setSpeed((float)this.getAttributeValue(Attributes.MOVEMENT_SPEED));
					super.travel(new Vec3((double)f, p_30633_.y, (double)f1));
				} else if (livingentity instanceof Player) {
					this.setDeltaMovement(Vec3.ZERO);
				}
				
				if (this.onGround) {
					this.playerJumpPendingScale = 0.0F;
					this.setIsJumping(false);
				}
				
				this.calculateEntityAnimation(this, false);
				this.tryCheckInsideBlocks();
			} else {
				this.flyingSpeed = 0.02F;
				super.travel(p_30633_);
			}
		}
	}
	
	private boolean isJumping() {
		return this.isJumping;
	}

	private void setIsJumping(boolean b) {
		this.isJumping = b;
	}

	@Override
	public InteractionResult mobInteract(Player p_27584_, InteractionHand p_27585_) {
		ItemStack itemstack = p_27584_.getItemInHand(p_27585_);
		if (itemstack.isEmpty() && DNAUtil.isBig(this)) {
			this.doPlayerRide(p_27584_);
		}
		return super.mobInteract(p_27584_, p_27585_);
	}
	
	public boolean canBeControlledByRider() {
		return this.getControllingPassenger() instanceof LivingEntity;
	}
	
	@Nullable
	public Entity getControllingPassenger() {
		return this.getFirstPassenger();
	}

	@Override
	public void onPlayerJump(int p_21696_) {
		if (p_21696_ < 0) {
			p_21696_ = 0;
		}
		if (p_21696_ >= 90) {
			this.playerJumpPendingScale = 1.0F;
		} else {
			this.playerJumpPendingScale = 0.4F + 0.4F * (float)p_21696_ / 90.0F;
		}
	}

	@Override
	public void positionRider(Entity p_28269_) {
		if (p_28269_ instanceof Mob mob) {
			this.yBodyRot = mob.yBodyRot;
		}
		super.positionRider(p_28269_);
	}

	@Override
	public boolean canJump() {
		return DNAUtil.isBig(this);
	}

	@Override
	public void handleStartJump(int p_21695_) {
		
	}

	@Override
	public void handleStopJump() {
		
	}
	
}
