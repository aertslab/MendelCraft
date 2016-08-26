package com.quintenlauwers.backend;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by quinten on 13/08/16.
 */
public class DnaConfig {

    private JsonObject mainConfig;
    private HashMap<String, JsonObject> animalConfigs = new HashMap<String, JsonObject>();
    private static String CONFIGFOLDER = "/assets/testmod/config/";
    private boolean diploid;
    private int nbOfChromosomes;
    private Random rand = new Random();

    public DnaConfig(String configFile) {
        String fileName = CONFIGFOLDER + configFile;
        InputStream location = getClass().getResourceAsStream(fileName);
        mainConfig = DnaConfig.convertFileToJSON(location);
        loadAnimals();
        loadDiploid();
        loadNbOfChromosomes();
    }

    public static JsonObject convertFileToJSON(InputStream fileName) {

        // Read from File to String
        JsonObject jsonObject = new JsonObject();


        JsonParser parser = new JsonParser();
        JsonReader reader = new JsonReader(new InputStreamReader(fileName));
        JsonElement jsonElement = parser.parse(reader);
        jsonObject = jsonElement.getAsJsonObject();


        return jsonObject;
    }

    private void loadDiploid() {
        if (mainConfig.has("diploid")) {
            this.diploid = mainConfig.get("diploid").getAsBoolean();
        }
    }

    private void loadNbOfChromosomes() {
        if (mainConfig.has("chromosomes")) {
            this.nbOfChromosomes = mainConfig.get("chromosomes").getAsInt();
        }
    }

    public int getNbOfChromosomes() {
        return this.nbOfChromosomes;
    }

    public boolean isDiploid() {
        return this.diploid;
    }

    public int getNbOfCodonsInChromosome(int chromosomeNumber) {
        int total = 0;
        for (int i = 0; i < getNbOfGenes(chromosomeNumber); i++) {
            total += getNbOfCodons(chromosomeNumber, i);
        }
        return total;
    }

    private int getNbOfCodons(int chromosomeNumber, int geneNumber) {
        if (mainConfig.has("codonsPerGene")) {
            return mainConfig.get("codonsPerGene").getAsInt();
        }
        return 0;
        // TODO: Exceptions for number of codons on gene.
    }

    public int getCodonIndex(int chromosomeNumber, int geneNumber, int codonNumber) {
        int total = 0;
        for (int i = 0; i < chromosomeNumber; i++) {
            total += getNbOfCodonsInChromosome(i);
        }
        for (int i = 0; i < geneNumber; i++) {
            total += getNbOfCodons(chromosomeNumber, i);
        }
        return total + codonNumber;
    }

    public int getCodonIndex(int chromosomeNumber, int codonOnChromosome) {
        int total = 0;
        for (int i = 0; i < chromosomeNumber; i++) {
            total += getNbOfCodonsInChromosome(i);
        }
        return total + codonOnChromosome;
    }

    public int getCodonIndex(int[] position) {
        if (position.length < 3) {
            throw new IllegalArgumentException("Position does not contain enough information.");
        }
        return getCodonIndex(position[0], position[1], position[2]);
    }

    public int[] positionFromCodonIndex(int nucleobaseIndex) {
        int chromosomeNb = 0;
        int geneNb = 0;
        int codonNb = 0;
        for (int i = 0; i < getNbOfChromosomes(); i++) {
            int currentChromosome = getNbOfCodonsInChromosome(i);
            if (currentChromosome > nucleobaseIndex) {
                chromosomeNb = i;
                break;
            }
            nucleobaseIndex -= currentChromosome;
        }
        for (int i = 0; i < getNbOfGenes(chromosomeNb); i++) {
            int currentGene = getNbOfCodons(chromosomeNb, i);
            if (currentGene > nucleobaseIndex) {
                geneNb = i;
                break;
            }
            nucleobaseIndex -= currentGene;
        }
        codonNb = nucleobaseIndex;
        int[] position = {chromosomeNb, geneNb, codonNb};
        return position;
    }

    public int getNbOfGenes(int chromosomeNumber) {
        String chromosomeName = "chromosome" + Integer.toString(chromosomeNumber);
        if (mainConfig.has("geneNbExceptions")) {
            JsonObject exception = mainConfig.getAsJsonObject("geneNbExceptions");
            if (exception.getAsJsonObject().has(chromosomeName)) {
                return exception.get(chromosomeName).getAsInt();
            }
        }
        return mainConfig.get("genesPerChromosme").getAsInt();
    }

    public int getTotalNbOfGenes() {
        int total = 0;
        for (int i = 0; i < getNbOfChromosomes(); i++) {
            total += getNbOfGenes(i);
        }
        return total;
    }

    public int getTotalNbOfCodons() {
        int total = 0;
        for (int i = 0; i < getNbOfChromosomes(); i++) {
            total += getNbOfCodonsInChromosome(i);
        }
        return total;
    }

    /**
     * Function used to generate one set of Chromosomes from two sets of chromosomes
     * (generates one bytearray from two bytearrays by randomly choosing one of two chromosomes each time).
     * This function may also introduce random mutations in the resulting dna.
     *
     * @param string1 A bytelist containing dna,
     *                it's length has to be greater or equal to the number of codons in an animal.
     * @param string2 A bytelist containing dna,
     *                it's length has to be greater or equal to the number of codons in an animal.
     * @return A combined byteArray, one of the two input arrays if the other is not valid.
     */
    public byte[] reduceToSingleDnaString(byte[] string1, byte[] string2) {
        int totalCodons = getTotalNbOfCodons();
        if (string1 == null || string1.length < totalCodons) {
            return string2;
        }
        if (string2 == null || string2.length < totalCodons) {
            return string1;
        }
        byte[] combinedDna = new byte[totalCodons];
        int lastIndex = 0;
        int chromosomeLength;
        for (int i = 0; i < nbOfChromosomes; i++) {
            chromosomeLength = Math.min(totalCodons - lastIndex, getNbOfCodonsInChromosome(i));
            if (rand.nextBoolean()) {
                System.arraycopy(string1, lastIndex, combinedDna, lastIndex, chromosomeLength);
            } else {
                System.arraycopy(string2, lastIndex, combinedDna, lastIndex, chromosomeLength);
            }
            lastIndex += chromosomeLength;
        }
        return combinedDna;
    }

    /**
     * Returns the relevant positions for the given property as chromosomeposition, genepositon pairs.
     *
     * @param animal   chicken, ...
     * @param property color, ...
     * @return DnaAsset of property. Null if error.
     */
    public DnaAsset getDnaAsset(String animal, String property) {
        JsonObject animalConfig = this.animalConfigs.get(animal.toLowerCase());
        if (animalConfig == null) {
            System.err.println("Config file is wrong, missing animal: " + animal);
            return null;
        }
        if (!animalConfig.has(property.toLowerCase())) {
            System.err.println("Config file for " + animal + " misses property: " + property.toLowerCase());
            return null;
        }
        JsonObject currentProperty = animalConfig.getAsJsonObject(property.toLowerCase());
        if (currentProperty.has("codonsInvolved") && currentProperty.has("allelesInvolved")) {
            DnaAsset dnaAsset = new DnaAsset(property);
            JsonArray genesInvolved = currentProperty.getAsJsonArray("codonsInvolved");
            for (int i = 0; i < genesInvolved.size(); i++) {
                JsonObject propertyPosition = genesInvolved.get(i).getAsJsonObject();
                if (propertyPosition.has("chromosome") && propertyPosition.has("gene")
                        && propertyPosition.has("codon") && propertyPosition.has("allele")) {
                    int[] position = {propertyPosition.get("chromosome").getAsInt(),
                            propertyPosition.get("gene").getAsInt(),
                            propertyPosition.get("codon").getAsInt()};
                    String allele = propertyPosition.get("allele").getAsString();
                    dnaAsset.addFromPosition(position, allele);
                }
            }
            Set<Map.Entry<String, JsonElement>> entries = currentProperty.getAsJsonObject("allelesInvolved").entrySet();
            for (Map.Entry<String, JsonElement> entry : entries) {
                JsonArray codeStringJsonArray = entry.getValue().getAsJsonArray();
                String[] codeArray = new String[codeStringJsonArray.size()];
                for (int i = 0; i < codeStringJsonArray.size(); i++) {
                    codeArray[i] = codeStringJsonArray.get(i).getAsString();
                }
                dnaAsset.addAlleleInfo(entry.getKey(), codeArray);
            }
            if (currentProperty.has("effects")) {
                entries = currentProperty.getAsJsonObject("effects").entrySet();
                for (Map.Entry<String, JsonElement> entry : entries) {
                    JsonArray alleleCombinationArray = entry.getValue().getAsJsonArray();
                    for (int i = 0; i < alleleCombinationArray.size(); i++) {
                        dnaAsset.addPropertyValue(alleleCombinationArray.get(i).getAsString(), entry.getKey());
                    }
                }
            }
            return dnaAsset;
        }
        return null;
    }

    public String[] getPossibleProperties(String animal) {
        String[] properties = new String[0];
        JsonObject animalConfig = this.animalConfigs.get(animal.toLowerCase());
        if (animalConfig == null) {
            System.err.println("Config file is wrong, missing animal: " + animal);
            return properties;
        }
        Set<Map.Entry<String, JsonElement>> propertieSet = animalConfig.entrySet();
        ArrayList<String> propertyList = new ArrayList<String>();
        for (Map.Entry<String, JsonElement> entry : propertieSet) {
            propertyList.add(entry.getKey());
        }
        properties = new String[propertyList.size()];
        return propertyList.toArray(properties);
    }


    public static boolean isInList(final List<int[]> list, final int[] candidate) {

        for (final int[] item : list) {
            if (Arrays.equals(item, candidate)) {
                return true;
            }
        }
        return false;
    }

    public void loadAnimals() {
        if (mainConfig.has("animals")) {
            JsonArray animals = mainConfig.getAsJsonArray("animals");
            for (int i = 0; i < animals.size(); i++) {
                JsonObject entry = animals.get(i).getAsJsonObject();
                if (entry != null && entry.has("name") && entry.has("geneFile")) {
                    String animalName = entry.get("name").getAsString().toLowerCase();
                    String configFile = entry.get("geneFile").getAsString();
                    String fileName = CONFIGFOLDER + configFile;
                    InputStream location = getClass().getResourceAsStream(fileName);
                    JsonObject animalConfig = DnaConfig.convertFileToJSON(location);
                    if (animalConfig != null) {
                        this.animalConfigs.put(animalName, animalConfig);
                    }
                } else {
                    System.err.println("Entry " + entry + " was invalid (missing name or geneFile)");
                }

            }
        }
    }
}
