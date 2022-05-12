package org.aertslab.mendelcraft.config;

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
		ConfigBuilder.geneExtra(f);
		
		ConfigBuilder.geneAmount(f, "Chrom1", 5);
		ConfigBuilder.defineGene(f, "Chrom1", 0, "ACT", "GGC");
		ConfigBuilder.defineGeneChance(f, "Chrom1", 0, 0.5F, 0.5F);
		ConfigBuilder.defineGene(f, "Chrom1", 1, "GCA", "GCT");
		ConfigBuilder.defineGeneChance(f, "Chrom1", 1, 0.5F, 0.50F);
		ConfigBuilder.geneColorBody(f, "Chrom1", 0);
		ConfigBuilder.geneColorHead(f, "Chrom1", 1);
		
		ConfigBuilder.geneAmount(f, "Chrom2", 4);
		ConfigBuilder.defineGene(f, "Chrom2", 3, "AAA", "GGA", "TTC");
		ConfigBuilder.defineGeneChance(f, "Chrom2", 3, 0.333F, 0.333F, 0.334F);
		ConfigBuilder.geneColorBlood(f, "Chrom2", 3);
		
		ConfigBuilder.geneAmount(f, "Chrom3", 7);
		ConfigBuilder.defineGene(f, "Chrom3", 0, "CAA", "GCG");
		ConfigBuilder.defineGeneChance(f, "Chrom3", 0, 0.5F, 0.50F);
		ConfigBuilder.defineGene(f, "Chrom3", 1, "CAA", "GCG");
		ConfigBuilder.defineGeneChance(f, "Chrom3", 1, 0.5F, 0.50F);
		ConfigBuilder.defineGene(f, "Chrom3", 2, "ATG", "TTA");
		ConfigBuilder.defineGeneChance(f, "Chrom3", 2, 0.5F, 0.50F);
		ConfigBuilder.defineGene(f, "Chrom3", 3, "GGC", "GGG");
		ConfigBuilder.defineGeneChance(f, "Chrom3", 3, 0.5F, 0.50F);
		ConfigBuilder.geneDinoHead(f, "Chrom3", 0);
		ConfigBuilder.geneDinoBody(f, "Chrom3", 1);
		ConfigBuilder.geneDinoWings(f, "Chrom3", 2);
		ConfigBuilder.geneDinoLegs(f, "Chrom3", 3);	
		
		ConfigBuilder.geneAmount(f, "Chrom4", 6);
		ConfigBuilder.defineGene(f, "Chrom4", 0, "AAA", "CGA");
		ConfigBuilder.defineGeneChance(f, "Chrom4", 0, 0.5F, 0.50F);
		ConfigBuilder.defineGene(f, "Chrom4", 5, "ATA", "TTC");
		ConfigBuilder.defineGeneChance(f, "Chrom4", 5, 0.5F, 0.50F);
		ConfigBuilder.geneBig(f, "Chrom4", 0);
		ConfigBuilder.geneStrange(f, "Chrom4", 5);
				
		return f;
	}
	
	static {
		CONFIG = BUILDER.comment("DNA config").define("Dna", conf() );
		SPEC = BUILDER.build();
	}

}
