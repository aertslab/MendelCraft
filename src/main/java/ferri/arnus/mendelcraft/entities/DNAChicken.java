package ferri.arnus.mendelcraft.entities;

import ferri.arnus.mendelcraft.capability.DNAProvider;
import ferri.arnus.mendelcraft.capability.DNAStorage;
import ferri.arnus.mendelcraft.capability.IDNAStorage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class DNAChicken extends Chicken{
	private IDNAStorage storage = DNAStorage.random(this.level);
	
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
	
//	@Override
//	public boolean save(CompoundTag pCompound) {
//		this.getCapability(DNAProvider.DNASTORAGE).ifPresent(s -> pCompound.put("DNA", s.serializeNBT()));
//		return super.save(pCompound);
//	}
	
//	@Override
//	public void load(CompoundTag nbt) {
//		this.getCapability(DNAProvider.DNASTORAGE).ifPresent(s -> s.deserializeNBT(nbt.getCompound("DNA")));
//		super.load(nbt);
//	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap) {
		if (cap == DNAProvider.DNASTORAGE) {
			return LazyOptional.of(() -> storage).cast();
		}
		return super.getCapability(cap);
	}

}
