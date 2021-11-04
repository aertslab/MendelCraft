package ferri.arnus.mendelcraft.capability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ferri.arnus.mendelcraft.config.ModConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class DNAUtil {
	
	public static int getBodyColor(Entity entity) {
		int[] color = new int[] {0xffffff};
		entity.getCapability(DNAProvider.DNASTORAGE).ifPresent(cap -> {
			Chromosome("body").forEach((s,i) -> {
				if (cololorMap(s, i).containsKey(cap.getGene(s, i))) {
					color[0] = cololorMap(s, i).get(cap.getGene(s, i));
				}
			});	
		});
		return color[0];
	}
	
	public static int getHeadColor(Entity entity) {
		int[] color = new int[] {0xffffff};
		entity.getCapability(DNAProvider.DNASTORAGE).ifPresent(cap -> {
			Chromosome("head").forEach((s,i) -> {
				if (cololorMap(s, i).containsKey(cap.getGene(s, i))) {
					color[0] = cololorMap(s, i).get(cap.getGene(s, i));
				}
			});	
		});
		return color[0];
	}
	
	public static int getBloodColor(Entity entity) {
		int[] color = new int[] {0xffffff};
		entity.getCapability(DNAProvider.DNASTORAGE).ifPresent(cap -> {
			Chromosome("blood").forEach((s,i) -> {
				if (cololorMap(s, i).containsKey(cap.getGene(s, i))) {
					color[0] = cololorMap(s, i).get(cap.getGene(s, i));
				}
			});	
		});
		return color[0];
	}
	
	public static int getBloodColor(IDNAStorage storage) {
		int[] color = new int[] {0xffffff};
		Chromosome("blood").forEach((s,i) -> {
			if (cololorMap(s, i).containsKey(storage.getGene(s, i))) {
				color[0] = cololorMap(s, i).get(storage.getGene(s, i));
			}
		});
		return color[0];
	}
	
	public static boolean isDinoHead(Entity entity) {
		boolean[] dino = new boolean[] {false};
		entity.getCapability(DNAProvider.DNASTORAGE).ifPresent(cap -> {
			Chromosome("dinohead").forEach((s,i) -> {
				if (dinoList(s, i).contains(cap.getGene(s, i))) {
					dino[0] = true;
				}
			});	
		});
		return dino[0];
	}
	
	public static boolean isDinoWings(Entity entity) {
		boolean[] dino = new boolean[] {false};
		entity.getCapability(DNAProvider.DNASTORAGE).ifPresent(cap -> {
			Chromosome("dinowings").forEach((s,i) -> {
				if (dinoList(s, i).contains(cap.getGene(s, i))) {
					dino[0] = true;
				}
			});	
		});
		return dino[0];
	}
	
	public static boolean isDinoBody(Entity entity) {
		boolean[] dino = new boolean[] {false};
		entity.getCapability(DNAProvider.DNASTORAGE).ifPresent(cap -> {
			Chromosome("dinobody").forEach((s,i) -> {
				if (dinoList(s, i).contains(cap.getGene(s, i))) {
					dino[0] = true;
				}
			});	
		});
		return dino[0];
	}
	
	public static boolean isDinoLegs(Entity entity) {
		boolean[] dino = new boolean[] {false};
		entity.getCapability(DNAProvider.DNASTORAGE).ifPresent(cap -> {
			Chromosome("dinolegs").forEach((s,i) -> {
				if (dinoList(s, i).contains(cap.getGene(s, i))) {
					dino[0] = true;
				}
			});	
		});
		return dino[0];
	}
	
	public static boolean isBig(Entity entity) {
		boolean[] dino = new boolean[] {false};
		entity.getCapability(DNAProvider.DNASTORAGE).ifPresent(cap -> {
			Chromosome("big").forEach((s,i) -> {
				if (dinoList(s, i).contains(cap.getGene(s, i))) {
					dino[0] = true;
				}
			});	
		});
		return dino[0];
	}
	
	public static boolean isStrange(Entity entity) {
		boolean[] dino = new boolean[] {false};
		entity.getCapability(DNAProvider.DNASTORAGE).ifPresent(cap -> {
			Chromosome("strange").forEach((s,i) -> {
				if (dinoList(s, i).contains(cap.getGene(s, i))) {
					dino[0] = true;
				}
			});	
		});
		return dino[0];
	}
	
	public static Map<String,List<String>> randomChromosone(Level level){
		Map<String,List<String>> map = new HashMap<>();
		List<String> list = new ArrayList<>();
		for (String chrom : getChromosomes()) {
			for (int i= 0; i<DNAUtil.getGeneAmount(chrom); i++) {
				list.add(randomGenes(chrom, i, level));
			}
			map.put(chrom, new ArrayList<>(list));
			list.clear();
		}
		return map;
	}

	
	public static List<String> getChromosomes(){
		return ModConfig.CONFIG.get().get("Chromosomes.list");
	}
	
	public static int getGeneAmount(String chromosome) {
		return ModConfig.CONFIG.get().get("Chromosomes." + chromosome + ".amount");
	}
	
	public static List<String> getPossibleGenes(String chromosome, int gen) {
		return ModConfig.CONFIG.get().get("Chromosomes." + chromosome + ".gen." + gen);
	}
	
	private static String randomGenes(String chromosome, int gen, Level level){
		try {
			List<String> list = DNAUtil.getPossibleGenes(chromosome, gen);
			return list.get(level.random.nextInt(list.size()));
		}catch (Exception e){

		}
		return "";
	}
	
	private static Map<List<String>,Integer> cololorMap(String chromosome, int gen){
		List<Integer> color = ModConfig.CONFIG.get().get("Effect." + chromosome + "." + gen +".color");
		List<List<String>> list = ModConfig.CONFIG.get().get("Effect." + chromosome + "." + gen +".match");
		return IntStream.range(0, list.size()).boxed().collect(Collectors.toMap(list::get, color::get));
	}
	
	private static Map<String,Integer> Chromosome(String type){
		Map<String,Integer> map = new HashMap<>();
		for (String chroms : getChromosomes()) {
			for (int i=0; i<DNAUtil.getGeneAmount(chroms);i++) {
				try {
					String types = ModConfig.CONFIG.get().get("Effect." + chroms + "." + i +".type");
					if (types.equals(type)) {
						map.put(chroms, i);
					}
				}catch (Exception e) {
					
				}
			}
		}
		return map;
	}
	
	private static List<List<String>> dinoList(String chromosome, int gen){
		List<List<String>> list = ModConfig.CONFIG.get().get("Effect." + chromosome + "." + gen +".match");
		return list;
	}
}
