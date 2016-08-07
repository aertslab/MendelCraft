package com.quintenlauwers.backend;

/**
 * Created by quinten on 6/08/16.
 */
public class DNAData {
    int nbGenes;


    public DNAData() {
        this.nbGenes = 5;
    }

    public boolean isActiveGene(int geneNb) {
        return true;
    }

    public int getNbOfGenes(int chromosomeNumber) {
        return this.nbGenes;
    }

    public int getNbOfChromosomes() {
        return 80;
    }

    public String getChromosmeDescription(int id) {
        return "lalala";
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
