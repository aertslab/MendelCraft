package ferri.arnus.mendelcraft.config;

import java.util.ArrayList;
import java.util.List;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.toml.TomlFormat;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class ModConfig {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static ForgeConfigSpec SPEC;
	public static ConfigValue<Config> CONFIG;
	public static CommentedConfig conf() {
		CommentedConfig f = TomlFormat.newConcurrentConfig();
		f.setComment("Chromosomes.list", "define your chromosomes here");
		ArrayList<String> chromosomes = new ArrayList<String>();
		chromosomes.add("re");
		chromosomes.add("ra");
		f.add("Chromosomes.list", chromosomes);
		f.add("Chromosomes.re.amount", 5);
		ArrayList<String> gen = new ArrayList<String>();
		gen.add("ATA");
		gen.add("TUC");
		f.add("Chromosomes.re.gen.0", gen);
		
		f.add("Chromosomes.ra.amount", 3);
		
		
		f.add("Effect.re.0.type", "body");
		ArrayList<String> match = new ArrayList<String>();
		ArrayList<List<String>> list = new ArrayList<>();
		match.add("ATA");
		match.add("ATA");
		list.add((List<String>) match.clone());
		match.clear();
		match.add("ATA");
		match.add("TUC");
		list.add((List<String>) match.clone());
		match.clear();
		match.add("TUC");
		match.add("ATA");
		list.add((List<String>) match.clone());
		match.clear();
		match.add("TUC");
		match.add("TUC");
		list.add((List<String>) match.clone());
		match.clear();
		ArrayList<Integer> color = new ArrayList<>();
		color.add(0x202020);
		color.add(0xA9A9A9);
		color.add(0xA9A9A9);
		color.add(0xffffff);
		f.add("Effect.re.0.match", list);
		f.add("Effect.re.0.color", color);
		return f;
	}
	
	static {
		CONFIG = BUILDER.comment("t").define("Dna", conf() );
		SPEC = BUILDER.build();
	}

}
