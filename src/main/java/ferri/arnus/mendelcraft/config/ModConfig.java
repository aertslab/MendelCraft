package ferri.arnus.mendelcraft.config;

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

		ConfigBuilder.chromosomes(f, "Chrom1", "Chrom2", "Chrom3", "Chrom4");
		
		ConfigBuilder.geneAmount(f, "Chrom1", 5);
		ConfigBuilder.defineGene(f, "Chrom1", 0, "ACT", "GGC");
		ConfigBuilder.defineGene(f, "Chrom1", 1, "GCA", "GCT");
		ConfigBuilder.geneColorBody(f, "Chrom1", 0);
		ConfigBuilder.geneColorHead(f, "Chrom1", 1);
		
		ConfigBuilder.geneAmount(f, "Chrom2", 4);
		ConfigBuilder.defineGene(f, "Chrom2", 3, "AAA", "GGA", "TTC");
		ConfigBuilder.geneColorBlood(f, "Chrom2", 3);
		
		ConfigBuilder.geneAmount(f, "Chrom3", 7);
		ConfigBuilder.defineGene(f, "Chrom3", 0, "CAA", "GCG");
		ConfigBuilder.defineGene(f, "Chrom3", 1, "CAA", "GCG");
		ConfigBuilder.defineGene(f, "Chrom3", 2, "ATG", "TTA");
		ConfigBuilder.defineGene(f, "Chrom3", 3, "GGC", "GGG");
		ConfigBuilder.geneDinoHead(f, "Chrom3", 0);
		ConfigBuilder.geneDinoBody(f, "Chrom3", 1);
		ConfigBuilder.geneDinoWings(f, "Chrom3", 2);
		ConfigBuilder.geneDinoLegs(f, "Chrom3", 3);	
		
		ConfigBuilder.geneAmount(f, "Chrom4", 8);
		
		return f;
	}
	
	static {
		CONFIG = BUILDER.comment("DNA config").define("Dna", conf() );
		SPEC = BUILDER.build();
	}

}
