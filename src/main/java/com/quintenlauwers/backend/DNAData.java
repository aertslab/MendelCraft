package com.quintenlauwers.backend;

import java.util.HashMap;

/**
 * Created by quinten on 6/08/16.
 */
public class DNAData {
    int nbGenes;
    private HashMap<String, String> chromosomeNames = new HashMap<String, String>();


    public DNAData(String animal) {
        this.nbGenes = 5;
    }

    public boolean isActiveGene(int chromosomeNumber, int geneNb) {
        return true;
    }

    public String getNucleobase(int chromosomeNumber, int geneNb){
        return byteNucleobaseToString((byte) (chromosomeNumber * 10 + geneNb));
    }

    public int getNbOfGenes(int chromosomeNumber) {
        return this.nbGenes;
    }

    public int getNbOfChromosomes() {
        return 5;
    }

    public String getChromosmeDescription(int id) {
        if (chromosomeNames.containsKey(Integer.toString(id))) {
            return chromosomeNames.get(Integer.toString(id));
        }
        return "Gene Nb. " + Integer.toString(id);
    }

    public void setChromosomeDescription(int id, String description){
        chromosomeNames.put(Integer.toString(id), description);
    }

    public String byteNucleobaseToString(byte nucleobase) {
        String text = "";
        int tempNucleo = nucleobase & 0xFF;
        for (int i = 0; i < 4; i++) {
            switch (tempNucleo % 4) {
                case 0:
                    text += "A";
                    break;
                case 1:
                    text += "C";
                    break;
                case 2:
                    text += "G";
                    break;
                case 3:
                    text += "T";
                    break;
            }
            tempNucleo = tempNucleo >> 2;
        }
        return text;
    }

    public byte stringNucleobaseToByte(String nucleobase) {
        int tempInt = 0;
        nucleobase = new StringBuilder(nucleobase).reverse().toString();
        for (int i = 0; i < 4; i++) {
            if (nucleobase.length() == i)
                    return (byte) tempInt;
            String character = Character.toString(nucleobase.charAt(i));
            tempInt = tempInt << 2;
            if (character.equals("C") || character.equals("c"))
                tempInt += 1;
            if (character.equals("G") || character.equals("g"))
                tempInt += 2;
            if (character.equals("T") || character.equals("t"))
                tempInt += 3;
        }
        return (byte) tempInt;
    }
}
