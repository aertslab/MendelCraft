package com.quintenlauwers.backend;

import com.quintenlauwers.backend.util.UtilDna;
import com.quintenlauwers.main.MendelCraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;

import java.util.*;

/**
 * Created by quinten on 11/08/16.
 * Contains the specific properties for one set of DNA.
 * Needs the bytearray(s) containing the actual data, is specific to one entity.
 */
public class DnaProperties {
    private byte[] dnaData;
    private byte[] dnaData2;
    String animal;
    private HashMap<GenePosition, Set<String>> restrictedEntries = null;
    private Set<GenePosition> inEditable = null;
    private String[] possibleProperties = null;
    private static DnaConfig dnaConfig = MendelCraft.dnaConfig;
    private HashMap<String, String> cachedStringProperty = new HashMap<String, String>();

    public DnaProperties(String animal, byte[] dnaData) {
        // expect that this is a haploid animal
        this(animal, dnaData, null);
    }

    public DnaProperties(String animal, byte[] dnaData, byte[] dnaData2) {
        // when instantiating without isRandom - assume it was with non-random DNA
        this(animal, dnaData, dnaData2, false);
    }

    private DnaProperties(String animal, byte[] dnaData, byte[] dnaData2, boolean isRandom) {
        if (dnaData != null && dnaData.length >= dnaConfig.getTotalNbOfGenes()) {
            this.animal = animal;
            setDna(dnaData);
        } else {
            if (dnaData == null) {
                throw new IllegalArgumentException("dnaData is nonexistent.");
            } else {
                throw new IllegalArgumentException("dnaData only has length: " + dnaData.length);
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
                        + " instead of " + dnaData.length);
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
        if (restrictedEntries == null || inEditable == null) {
            fillRestrictedEntries();
        }
        GenePosition pos = new GenePosition(position);
        return (pos != null && getRestrictedEntries().containsKey(pos) && !inEditable.contains(pos));
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
        filterDna(data, false);
    }

    public void filterDna(byte[] data, boolean isRandom) {
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
        inEditable = new HashSet<GenePosition>();

        if (this.possibleProperties == null) {
            loadPossibleProperties();
        }

        for (String property : this.possibleProperties) { // Gets a feature i.e. headColor
            DnaAsset asset = dnaConfig.getDnaAsset(this.animal, property);
            int[][] positions = asset.getRelevantPositions(); //  Returns associated gene position i.e. [0, 0, 1]
            for (int[] position : positions) {

                String[] codons = asset.getPossibleCodonValuesOnPosition(position); // Returns all codons possible for this location

                List<String> codonSet = Arrays.asList(codons);
                GenePosition tempPosition = new GenePosition(position);
                if (restrictedEntries.containsKey(tempPosition)) {
                    restrictedEntries.get(tempPosition).retainAll(codonSet);
                } else {
                    restrictedEntries.put(tempPosition, new HashSet<String>(codonSet));
                }
                if (!asset.isEditable()) {
                    inEditable.add(tempPosition);
                }
            }
        }


    }

    public void setDna(byte[] dnaData, boolean isRandom) {
        this.dnaData = dnaData;
        filterDna(this.dnaData, isRandom);
    }

    public void setDna(byte[] dnaData) {
        setDna(dnaData, false);
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

    public Map<NBTTagCompound, String[]> getDnaNBT() {
        String stringDna[] = new String[dnaConfig.getTotalNbOfCodons()];
        Map<NBTTagCompound, String[]> dnaMap = new HashMap<NBTTagCompound, String[]>();
        NBTTagCompound dna = new NBTTagCompound();
        int codonNum = 0;
        for (int i = 0; i < MendelCraft.dnaConfig.getNbOfChromosomes(); i++) {
            dna.setTag("Chr" + i, new NBTTagCompound());
            for (int j = 0; j < MendelCraft.dnaConfig.getNbOfGenes(i); j++) {
                NBTTagCompound Gene = dna.getCompoundTag("Chr" + i);
                Gene.setTag("Gene_" + j, new NBTTagCompound());
                for (int k = 0; k < MendelCraft.dnaConfig.getNbOfCodons(i, j); k++) {
                    NBTTagCompound Codon = Gene.getCompoundTag("Gene_" + j);
                    String position = Integer.toString(i) + Integer.toString(j) + Integer.toString(k);
                    String property = DnaConfig.getPropertyFromPos(position);
                    String codon = "";
                    if (property != null) {
                    DnaAsset asset = dnaConfig.getDnaAsset(this.animal, property);
                    codon = UtilDna.getWeightedCodon(asset.getFrequencies());
                    } else {
                        String alpha = "ACGT";
                        Random r = new Random();
                        for (int l = 0; l < 3; l++) {
                            codon += alpha.charAt(r.nextInt(alpha.length()));
                        }
                    }
                    Codon.setTag("Codon_" + k, new NBTTagString(codon));
                    stringDna[codonNum] = codon;
                    codonNum += 1;

                }

            }
        }
        dnaMap.put(dna, stringDna);

        return dnaMap;
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
