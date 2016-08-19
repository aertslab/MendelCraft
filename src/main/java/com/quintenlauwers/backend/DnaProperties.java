package com.quintenlauwers.backend;

import com.quintenlauwers.backend.util.UtilDna;
import com.quintenlauwers.main.TestMod;
import scala.actors.threadpool.Arrays;

import java.util.*;

/**
 * Created by quinten on 11/08/16.
 */
public class DnaProperties {
    boolean color;
    byte[] dnaData;
    String animal;
    HashMap<GenePosition, Set<String>> restrictedEntries = null;
    String[] possibleProperties = null;
    public static DnaConfig dnaConfig = TestMod.dnaConfig;
    HashMap<String, String> cachedStringProperty = new HashMap<String, String>();

    public DnaProperties(String animal, byte[] dnaData) {
        if (dnaData != null && dnaData.length >= dnaConfig.getTotalNbOfGenes()) {
            this.animal = animal;
            this.color = UtilDna.byteToBool((byte) (dnaData[0] & (byte) 1));
            this.dnaData = dnaData.clone();
            filterDna();
        } else {
            if (dnaData == null) {
                throw new IllegalArgumentException("dnaData is nonexistent.");
            } else {
                throw new IllegalArgumentException("dnaData only has lenght: " + dnaData.length);
            }
        }
    }

    public boolean getBoolProperty(String property) {
        String finalValue = getGenericProperty(property);
        return finalValue != null && "true".equals(finalValue.toLowerCase());
    }

    public int getIntProperty(String property) {
        String finalValue = getGenericProperty(property);
        if (finalValue == null) {
            return 0;
        }
        return Integer.parseInt(finalValue);
    }


    public String getStringProperty(String property) {
        if (!this.cachedStringProperty.containsKey(property)) {
            cachedStringProperty.put(property, getGenericProperty(property));
        }
        return this.cachedStringProperty.get(property);
    }

    public String getGenericProperty(String property) {
        DnaAsset asset = dnaConfig.getDnaAsset(this.animal.toLowerCase(), property);
        int[][] positions = asset.getRelevantPositions();
        String allAlleles = "";
        for (int[] position : positions) {
            byte rawCode = dnaData[dnaConfig.getCodonIndex(position)];
            String code = UtilDna.byteNucleobaseToString(rawCode);
            allAlleles += asset.getAlleleOnPosition(position, code);
        }
        String finalValue = asset.getPropertyValue(allAlleles);
        return finalValue;
    }

    public void filterDna() {
        if (restrictedEntries == null) {
            fillRestrictedEntries();
        }
        for (Map.Entry<GenePosition, Set<String>> entry : restrictedEntries.entrySet()) {
            int index = TestMod.dnaConfig.getCodonIndex(entry.getKey().getCoordinate());
            if (index < dnaData.length) {
                byte rawCode = dnaData[index];
                String code = UtilDna.byteNucleobaseToString(rawCode);
                if (!entry.getValue().contains(code)) {

                    Set<String> value = entry.getValue();
                    int size = value.size();
                    int item = new Random().nextInt(size); // In real life, the Random object should be rather more shared than this
                    int i = 0;
                    for (String newCode : value) {
                        if (i == item)
                            dnaData[index] = UtilDna.stringNucleobaseToByte(newCode);
                        i++;
                    }
                }
            }

        }
    }

    private void fillRestrictedEntries() {
        restrictedEntries = new HashMap<GenePosition, Set<String>>();
        if (this.possibleProperties == null) {
            loadPossibleProperties();
        }
        for (String property : this.possibleProperties) {
            DnaAsset asset = dnaConfig.getDnaAsset(this.animal, property);
            int[][] positions = asset.getRelevantPositions();
            for (int[] position : positions) {
                String[] codons = asset.getPossibleCodonValuesOnPosition(position);
                List<String> codonSet = Arrays.asList(codons);
                GenePosition tempPosition = new GenePosition(position);
                if (restrictedEntries.containsKey(tempPosition)) {
                    restrictedEntries.get(tempPosition).retainAll(codonSet);
                } else {
                    restrictedEntries.put(tempPosition, new HashSet<String>(codonSet));
                }
//                TODO: Hier verder werken.
            }
        }


    }

    private void loadPossibleProperties() {
        this.possibleProperties = dnaConfig.getPossibleProperties(this.animal);
    }

    public boolean getColor() {
        return this.color;
    }

    public byte[] getDnaData() {
        if (dnaData != null)
            return dnaData.clone();
        return null;
    }
}
