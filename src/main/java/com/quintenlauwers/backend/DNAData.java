package com.quintenlauwers.backend;

import com.quintenlauwers.backend.util.UtilDna;

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
        return UtilDna.byteNucleobaseToString((byte) (chromosomeNumber * 10 + geneNb));
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
}
