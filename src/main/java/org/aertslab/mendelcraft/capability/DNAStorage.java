package org.aertslab.mendelcraft.capability;

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
			for (int i=0; i<DNAUtil.getGeneAmount(s); i++) {
				genelist.putString(i+ "", l.get(i));
			}
			chromosome1list.put(s, genelist.copy());
		});
		tag.put("chromosome1", chromosome1list);
		this.chromosomes2.forEach((s,l) -> {
			CompoundTag genelist = new CompoundTag();
			for (int i=0; i<DNAUtil.getGeneAmount(s); i++) {
				genelist.putString(i+ "", l.get(i));
			}
			chromosome2list.put(s, genelist.copy());
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
			 for (int i=0; i<DNAUtil.getGeneAmount(chromosome); i++) {
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
			 for (int i=0; i<DNAUtil.getGeneAmount(chromosome); i++) {
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
	public void setGene(String chromosome, int place, List<String> sequence) {
		List<String> list1 = this.chromosomes1.get(chromosome);
		list1.set(place, sequence.get(0));
		this.chromosomes1.put(chromosome, list1);
		
		List<String> list2 = this.chromosomes2.get(chromosome);
		list2.set(place, sequence.get(1));
		this.chromosomes2.put(chromosome, list2);
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
