package ferri.arnus.mendelcraft.capability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

public class DNAStorage implements IDNAStorage{
	private Map<String,List<String>> chromosomes1 = new HashMap<String, List<String>>();
	private Map<String,List<String>> chromosomes2 = new HashMap<String, List<String>>();
	private boolean empty = true;
	
	public DNAStorage() {
		
	}
	
	public static DNAStorage random(Level level) {
		DNAStorage storage = new DNAStorage();
		storage.setChromosomes( DNAUtil.randomChromosone(level),  DNAUtil.randomChromosone(level));
		return storage;
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag tag = new CompoundTag();
		tag.putBoolean("empty", empty);
		CompoundTag chromosome1list = new CompoundTag();
		CompoundTag chromosome2list = new CompoundTag();
		this.chromosomes1.forEach((s,l) -> {
			CompoundTag genelist = new CompoundTag();
			for (String gene: l) {
				genelist.putString(l.indexOf(gene) + "", gene);
			}
			chromosome1list.put(s, genelist.copy());
			genelist = new CompoundTag();
		});
		tag.put("chromosome1", chromosome1list);
		this.chromosomes2.forEach((s,l) -> {
			CompoundTag genelist = new CompoundTag();
			for (String gene: l) {
				genelist.putString(l.indexOf(gene) + "", gene);
			}
			chromosome2list.put(s, genelist.copy());
			genelist = new CompoundTag();
		});
		tag.put("chromosome2", chromosome2list);
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		this.empty = nbt.getBoolean("empty");
		 Map<String,List<String>> chromosomes1 = new HashMap<>();
		 CompoundTag tag = nbt.getCompound("chromosome1");
		 for (String chromosome : DNAUtil.getChromosomes()) {
			 List<String> list = new ArrayList<>();
			 CompoundTag gene = tag.getCompound(chromosome);
			 for (int i=0; i<4; i++) {
				 list.add(gene.getString(i+""));
			 }
			 chromosomes1.put(chromosome, list);
		 }
		 this.chromosomes1 = chromosomes1;
		 Map<String,List<String>> chromosomes2 = new HashMap<>();
		 CompoundTag tag2 = nbt.getCompound("chromosome2");
		 for (String chromosome : DNAUtil.getChromosomes()) {
			 List<String> list = new ArrayList<>();
			 CompoundTag gene = tag2.getCompound(chromosome);
			 for (int i=0; i<4; i++) {
				 list.add(gene.getString(i+""));
			 }
			 chromosomes2.put(chromosome, list);
		 }
		 this.chromosomes2 = chromosomes2;
	}

	@Override
	public List<List<String>> getChromosome(String chromosome) {
		List<List<String>> list = new ArrayList<>();
		list.add(this.chromosomes1.get(chromosome));
		list.add(this.chromosomes2.get(chromosome));
		return list;
	}
	
	@Override
	public List<Map<String,List<String>>> getChromosomes(){
		List<Map<String,List<String>>> list  = new ArrayList<Map<String,List<String>>>();
		list.add(chromosomes1);
		list.add(chromosomes2);
		return list;
	}
	
	public void setChromosomes(List<Map<String,List<String>>> chromosomes) {
		this.chromosomes1 = chromosomes.get(0);
		this.chromosomes2 = chromosomes.get(1);
	}

	@Override
	public void setChromosomes(Map<String,List<String>> chromosomes1, Map<String,List<String>> chromosomes2) {
		this.chromosomes1 = chromosomes1;
		this.chromosomes2 = chromosomes2;
		
	}

	@Override
	public List<String> getGene(String chromosome, int place) {
		List<String> list = new ArrayList<>();
		List<List<String>> list2 = getChromosome(chromosome);
		list.add(list2.get(0).get(place));
		list.add(list2.get(1).get(place));
		return list;
	}

	@Override
	public void setGene(List<String> chromosome, int[] place, List<String> sequence) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isEmpty() {
		return this.empty;
	}
	
	@Override
	public void setEmpty(boolean empty) {
		this.empty = empty;
	}
	
}
