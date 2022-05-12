package org.aertslab.mendelcraft.config;

import java.util.ArrayList;
import java.util.List;

import com.electronwill.nightconfig.core.CommentedConfig;

public class ConfigBuilder {
	
	public static void chromosomes(CommentedConfig f, String...strings) {
		ArrayList<String> chromosomes = new ArrayList<>();
		for (String s: strings) {
			chromosomes.add(s);
		}
		f.add("Chromosomes.list", chromosomes);
	}
	
	public static void geneAmount(CommentedConfig f, String name, int maxgene) {
		f.add("Chromosomes."+name+".amount", maxgene);
	}
	
	public static void defineGene(CommentedConfig f, String name, int gene, String...possible) {
		ArrayList<String> gen = new ArrayList<String>();
		for (String s: possible) {
			gen.add(s);
		}
		f.add("Chromosomes."+name+".gen." + gene + ".sequence", gen);
	}
	
	public static void defineGeneChance(CommentedConfig f, String name, int gene, float...chance) {
		ArrayList<Float> chances = new ArrayList<Float>();
		for (float c: chance) {
			chances.add(c);
		}
		f.add("Chromosomes."+name+".gen." + gene + ".chance", chances);
	}
	
	@SuppressWarnings("unchecked")
	public static void geneColorBody(CommentedConfig f, String name, int gene) {
		f.add("Effect."+name+"."+gene+".type", "body");
		ArrayList<String> match = new ArrayList<String>();
		ArrayList<List<String>> list = new ArrayList<>();
		match.add("ACT");
		match.add("ACT");
		list.add((List<String>) match.clone());
		match.clear();
		match.add("ACT");
		match.add("GGC");
		list.add((List<String>) match.clone());
		match.clear();
		match.add("GGC");
		match.add("ACT");
		list.add((List<String>) match.clone());
		match.clear();
		match.add("GGC");
		match.add("GGC");
		list.add((List<String>) match.clone());
		match.clear();
		ArrayList<String> color = new ArrayList<>();
		color.add("202020");
		color.add("A9A9A9");
		color.add("A9A9A9");
		color.add("ffffff");
		f.add("Effect."+name+"."+gene+".match", list);
		f.add("Effect."+name+"."+gene+".color", color);
	}
	
	@SuppressWarnings("unchecked")
	public static void geneColorHead(CommentedConfig f, String name, int gene) {
		f.add("Effect."+name+"."+gene+".type", "head");
		ArrayList<String> match = new ArrayList<String>();
		ArrayList<List<String>> list = new ArrayList<>();
		match.add("GCT");
		match.add("GCT");
		list.add((List<String>) match.clone());
		match.clear();
		match.add("GCT");
		match.add("GCA");
		list.add((List<String>) match.clone());
		match.clear();
		match.add("GCA");
		match.add("GCT");
		list.add((List<String>) match.clone());
		match.clear();
		match.add("GCA");
		match.add("GCA");
		list.add((List<String>) match.clone());
		match.clear();
		ArrayList<String> color = new ArrayList<>();
		color.add("202020");
		color.add("ffffff");
		color.add("ffffff");
		color.add("ffffff");
		f.add("Effect."+name+"."+gene+".match", list);
		f.add("Effect."+name+"."+gene+".color", color);
	}
	
	@SuppressWarnings("unchecked")
	public static void geneColorBlood(CommentedConfig f, String name, int gene) {
		f.add("Effect."+name+"."+gene+".type", "blood");
		ArrayList<String> match = new ArrayList<String>();
		ArrayList<List<String>> list = new ArrayList<>();
		match.add("AAA");
		match.add("AAA");
		list.add((List<String>) match.clone());
		match.clear();
		match.add("AAA");
		match.add("TTC");
		list.add((List<String>) match.clone());
		match.clear();
		match.add("TTC");
		match.add("AAA");
		list.add((List<String>) match.clone());
		match.clear();
		match.add("TTC");
		match.add("TTC");
		list.add((List<String>) match.clone());
		match.clear();
		match.add("GGA");
		match.add("GGA");
		list.add((List<String>) match.clone());
		match.clear();
		match.add("GGA");
		match.add("TTC");
		list.add((List<String>) match.clone());
		match.clear();
		match.add("TTC");
		match.add("GGA");
		list.add((List<String>) match.clone());
		match.clear();
		match.add("GGA");
		match.add("AAA");
		list.add((List<String>) match.clone());
		match.clear();
		match.add("AAA");
		match.add("GGA");
		list.add((List<String>) match.clone());
		match.clear();
		ArrayList<String> color = new ArrayList<>();
		color.add("FFF033");
		color.add("36AE11");
		color.add("36AE11");
		color.add("113CAE");
		color.add("FFF033");
		color.add("36AE11");
		color.add("36AE11");
		color.add("FFF033");
		color.add("FFF033");
		f.add("Effect."+name+"."+gene+".match", list);
		f.add("Effect."+name+"."+gene+".color", color);
	}
	
	@SuppressWarnings("unchecked")
	public static void geneDinoHead(CommentedConfig f, String name, int gene) {
		f.add("Effect."+name+"."+gene+".type", "dinohead");
		ArrayList<String> match = new ArrayList<String>();
		ArrayList<List<String>> list = new ArrayList<>();
		match.add("GCG");
		match.add("GCG");
		list.add((List<String>) match.clone());
		f.add("Effect."+name+"."+gene+".match", list);
	}
	
	@SuppressWarnings("unchecked")
	public static void geneDinoBody(CommentedConfig f, String name, int gene) {
		f.add("Effect."+name+"."+gene+".type", "dinobody");
		ArrayList<String> match = new ArrayList<String>();
		ArrayList<List<String>> list = new ArrayList<>();
		match.add("GCG");
		match.add("GCG");
		list.add((List<String>) match.clone());
		f.add("Effect."+name+"."+gene+".match", list);
	}
	
	@SuppressWarnings("unchecked")
	public static void geneDinoWings(CommentedConfig f, String name, int gene) {
		f.add("Effect."+name+"."+gene+".type", "dinowings");
		ArrayList<String> match = new ArrayList<String>();
		ArrayList<List<String>> list = new ArrayList<>();
		match.add("TTA");
		match.add("TTA");
		list.add((List<String>) match.clone());
		f.add("Effect."+name+"."+gene+".match", list);
	}
	
	@SuppressWarnings("unchecked")
	public static void geneDinoLegs(CommentedConfig f, String name, int gene) {
		f.add("Effect."+name+"."+gene+".type", "dinolegs");
		ArrayList<String> match = new ArrayList<String>();
		ArrayList<List<String>> list = new ArrayList<>();
		match.add("GGG");
		match.add("GGG");
		list.add((List<String>) match.clone());
		f.add("Effect."+name+"."+gene+".match", list);
	}
	
	@SuppressWarnings("unchecked")
	public static void geneBig(CommentedConfig f, String name, int gene) {
		f.add("Effect."+name+"."+gene+".type", "big");
		ArrayList<String> match = new ArrayList<String>();
		ArrayList<List<String>> list = new ArrayList<>();
		match.add("CGA");
		match.add("CGA");
		list.add((List<String>) match.clone());
		f.add("Effect."+name+"."+gene+".match", list);
	}
	
	@SuppressWarnings("unchecked")
	public static void geneStrange(CommentedConfig f, String name, int gene) {
		f.add("Effect."+name+"."+gene+".type", "strange");
		ArrayList<String> match = new ArrayList<String>();
		ArrayList<List<String>> list = new ArrayList<>();
		match.add("TTC");
		match.add("TTC");
		list.add((List<String>) match.clone());
		f.add("Effect."+name+"."+gene+".match", list);
	}
	
	public static void geneExtra(CommentedConfig f) {
		ArrayList<String> match = new ArrayList<String>();
		match.add("TAC");
		match.add("ATA");
		match.add("TGA");
		match.add("CTG");
		match.add("GGT");
		f.add("Effect.extra.gene", match);
	}

}
