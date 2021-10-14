package ferri.arnus.mendelcraft.capability;

import java.util.List;
import java.util.Map;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IDNAStorage extends INBTSerializable<CompoundTag>{
	
	List<List<String>> getChromosome(String chromosome);
	
	void setChromosomes(Map<String,List<String>> chromosome1, Map<String,List<String>> chromosome2);
	
	void setChromosomes(List<Map<String,List<String>>> chromosomes);
	
	List<Map<String,List<String>>> getChromosomes();
	
	List<String> getGene(String chromosome, int place);
	
	void setGene(List<String> chromosome, int[] place, List<String> sequence);
	
	boolean isEmpty();
	
	void setEmpty(boolean empty);

}
