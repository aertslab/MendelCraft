package ferri.arnus.mendelcraft.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class DNAProvider implements ICapabilitySerializable<CompoundTag>{
	private DNAStorage storage = new DNAStorage();
	
	public static final Capability<IDNAStorage> DNASTORAGE = CapabilityManager.get(new CapabilityToken<>(){});
	
	public DNAProvider() {
		// TODO Auto-generated constructor stub
	}
	
	public DNAProvider(Level level) {
			storage = DNAStorage.random(level);
	}
	
	@SubscribeEvent
	public static void register(RegisterCapabilitiesEvent event){
		event.register(IDNAStorage.class);
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == DNASTORAGE) {
			return LazyOptional.of(()-> storage).cast();
		}
		return LazyOptional.empty();
	}
	
	@Override
	public CompoundTag serializeNBT() {
		CompoundTag t = new CompoundTag();
		t.put("DNA", storage.serializeNBT());
		return t;
	}
	
	@Override
	public void deserializeNBT(CompoundTag nbt) {
		this.storage.deserializeNBT(nbt.getCompound("DNA"));
		
	}
	
}
