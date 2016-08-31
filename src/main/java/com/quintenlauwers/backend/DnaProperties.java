package com.quintenlauwers.backend;

import com.quintenlauwers.backend.util.UtilDna;
import com.quintenlauwers.main.MendelCraft;

import java.util.*;

/**
 * Created by quinten on 11/08/16.
 */
public class DnaProperties {
    byte[] dnaData;
    byte[] dnaData2;
    String animal;
    HashMap<GenePosition, Set<String>> restrictedEntries = null;
    String[] possibleProperties = null;
    public static DnaConfig dnaConfig = MendelCraft.dnaConfig;
    HashMap<String, String> cachedStringProperty = new HashMap<String, String>();

    public DnaProperties(String animal, byte[] dnaData) {
        this(animal, dnaData, null);
    }

    public DnaProperties(String animal, byte[] dnaData, byte[] dnaData2) {
        if (dnaData != null && dnaData.length >= dnaConfig.getTotalNbOfGenes()) {
            this.animal = animal;
            setDna(dnaData);
        } else {
            if (dnaData == null) {
                throw new IllegalArgumentException("dnaData is nonexistent.");
            } else {
                throw new IllegalArgumentException("dnaData only has lenght: " + dnaData.length);
            }
        }
        if (dnaData2 != null && dnaData2.length == dnaData.length) {
            setDna2(dnaData2);
        } else {
            if (dnaData2 == null && dnaConfig.isDiploid()) {
                throw new IllegalArgumentException("The animal should have two dna strings (diploid).");
            }
            if (dnaData2 != null) {
                throw new IllegalArgumentException("dnaData has the wrong length, " + dnaData2.length
                        + " instead of " + dnaData2.length);
            }
        }
    }

    public String[] getPossibleCodons(int[] position) {
        GenePosition pos = new GenePosition(position);
        if (pos == null || !getRestrictedEntries().containsKey(pos)) {
            return null;
        }
        Set<String> possibleSet = getRestrictedEntries().get(pos);
        String[] returnArray = new String[possibleSet.size()];
        possibleSet.toArray(returnArray);
        return returnArray;
    }

    private HashMap<GenePosition, Set<String>> getRestrictedEntries() {
        if (restrictedEntries == null) {
            fillRestrictedEntries();
        }
        return restrictedEntries;
    }

    public boolean isEditablePosition(int[] position) {
        GenePosition pos = new GenePosition(position);
        return (pos != null && getRestrictedEntries().containsKey(pos));
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
            if (dnaConfig.isDiploid() && dnaData2 != null) {
                byte rawCode2 = dnaData2[dnaConfig.getCodonIndex(position)];
                String code2 = UtilDna.byteNucleobaseToString(rawCode2);
                allAlleles += asset.getAlleleOnPosition(position, code2);
            }
        }
        String finalValue = asset.getPropertyValue(allAlleles);
        return finalValue;
    }

    public void filterDna(byte[] data) {
        if (restrictedEntries == null) {
            fillRestrictedEntries();
        }
        for (Map.Entry<GenePosition, Set<String>> entry : restrictedEntries.entrySet()) {
            int index = dnaConfig.getCodonIndex(entry.getKey().getCoordinate());
            if (index < data.length) {
                byte rawCode = data[index];
                String code = UtilDna.byteNucleobaseToString(rawCode);
                if (!entry.getValue().contains(code)) {

                    Set<String> value = entry.getValue();
                    int size = value.size();
                    int item = new Random().nextInt(size); // In real life, the Random object should be rather more shared than this
                    int i = 0;
                    for (String newCode : value) {
                        if (i == item)
                            data[index] = UtilDna.stringNucleobaseToByte(newCode);
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
            }
        }


    }

    public void setDna(byte[] dnaData) {
        this.dnaData = dnaData;
        filterDna(this.dnaData);
    }

    public void setDna2(byte[] dnaData2) {
        this.dnaData2 = dnaData2;
        filterDna(this.dnaData2);
    }

    public String[] getPossibleProperties() {
        if (this.possibleProperties == null) {
            loadPossibleProperties();
        }
        if (this.possibleProperties == null) {
            this.possibleProperties = new String[0];
        }
        return this.possibleProperties.clone();
    }

    public String getAnimal() {
        return this.animal;
    }

    private void loadPossibleProperties() {
        this.possibleProperties = dnaConfig.getPossibleProperties(this.animal);
    }

    public byte[] getDnaData() {
        if (dnaData != null)
            return dnaData.clone();
        return null;
    }

    public byte[] getDnaData2() {
        if (this.dnaData2 != null) {
            return dnaData2.clone();
        }
        return null;
    }
}
