package com.quintenlauwers.backend;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by quinten on 13/08/16.
 */
public class DnaConfig {

    private JsonObject mainConfig;
    private HashMap<String, JsonObject> animalConfigs = new HashMap<String, JsonObject>();
    private static String CONFIGFOLDER = "/assets/config/";

    public DnaConfig(String configFile) {
        String fileName = CONFIGFOLDER + configFile;
        URL location = getClass().getResource(fileName);
        mainConfig = DnaConfig.convertFileToJSON(location);
        loadAnimals();
        System.out.println(getNbOfGenes(4));
    }

    public static JsonObject convertFileToJSON(URL fileName) {
        String realPath = null;
        try {
            realPath = new File(fileName.toURI()).getAbsolutePath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        // Read from File to String
        JsonObject jsonObject = new JsonObject();

        try {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(new FileReader(realPath));
            jsonObject = jsonElement.getAsJsonObject();
        } catch (FileNotFoundException e) {

        } catch (IOException ioe) {

        }


        return jsonObject;
    }

    public int getNbOfChromosomes() {
        if (mainConfig.has("chromosomes")) {
            return mainConfig.get("chromosomes").getAsInt();
        }
        return 0;
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

    /**
     * Returns the relevant positions for the given property as chromosomeposition, genepositon pairs.
     *
     * @param animal   chicken, ...
     * @param property color, ...
     * @return {{chrom1, gene1}; {chrom2, gene2}, ...}
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
        if (currentProperty.has("genesInvolved")) {
            JsonArray genesInvolved = currentProperty.getAsJsonArray("genesInvolved");
            ArrayList<int[]> tempRelevantPositions = new ArrayList<int[]>();
            for (int i = 0; i < genesInvolved.size(); i++) {
                JsonObject propertyPosition = genesInvolved.get(i).getAsJsonObject();
                if (propertyPosition.has("chromosome") && propertyPosition.has("gene")) {
                    int[] position = {propertyPosition.get("chromosome").getAsInt(), propertyPosition.get("gene").getAsInt()};
                    if (!isInList(tempRelevantPositions, position)) {
                        tempRelevantPositions.add(position);
                    }
                }
            }
//            return tempRelevantPositions.toArray(new int[tempRelevantPositions.size()][2]);
        }
        return null;
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
                    URL location = getClass().getResource(fileName);
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
