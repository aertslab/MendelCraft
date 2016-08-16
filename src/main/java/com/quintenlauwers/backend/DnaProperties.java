package com.quintenlauwers.backend;

import com.quintenlauwers.backend.util.UtilDna;
import com.quintenlauwers.main.TestMod;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by quinten on 11/08/16.
 */
public class DnaProperties {
    boolean color;
    byte[] dnaData;
    String animal;
    HashMap<DnaAsset.GenePosition, Set<String>> restrictedEntries = null;
    String[] possibleProperties = null;
    public static DnaConfig dnaConfig = TestMod.dnaConfig;

    public DnaProperties(String animal, byte[] dnaData) {
        if (dnaData != null && dnaData.length >= dnaConfig.getTotalNbOfGenes()) {
            this.animal = animal;
            this.color = UtilDna.byteToBool((byte) (dnaData[0] & (byte) 1));
            this.dnaData = dnaData.clone();
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
        return 0;
    }

    public String getGenericProperty(String property) {
        DnaAsset asset = dnaConfig.getDnaAsset(this.animal.toLowerCase(), property);
        int[][] positions = asset.getRelevantPositions();
        String allAlleles = "";
        for (int[] position : positions) {
            byte rawCode = dnaData[dnaConfig.getNucleobaseIndex(position)];
            String code = UtilDna.byteNucleobaseToString(rawCode);
            System.out.println("Now checking: " + code);
            allAlleles += asset.getAlleleOnPosition(position, code);
        }
        String finalValue = asset.getPropertyValue(allAlleles);
        System.out.println("Final value is: " + finalValue);
        return finalValue;
    }

    public void filterDna() {
        if (restrictedEntries == null) {
            fillRestrictedEntries();
        }

    }

    private void fillRestrictedEntries() {
        restrictedEntries = new HashMap<DnaAsset.GenePosition, Set<String>>();
        if (this.possibleProperties == null) {
            loadPossibleProperties();
        }
        for (String property : this.possibleProperties) {
            DnaAsset asset = dnaConfig.getDnaAsset(this.animal, property);
            int[][] positions = asset.getRelevantPositions();
            for (int[] position : positions) {
                String[] codons = asset.getPossibleGenesOnPosition(position);
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
