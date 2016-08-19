package com.quintenlauwers.backend;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by quinten on 16/08/16.
 */
public class DnaConfigTest {
    @Test
    public void convertFileToJSON() throws Exception {
        DnaConfig dnaConfig = new DnaConfig("testConfig.json");
        assertEquals(5, dnaConfig.getNbOfChromosomes());
    }

    @Test
    public void loadAnimals() throws Exception {
        DnaConfig dnaConfig = new DnaConfig("testConfig.json");
        dnaConfig.loadAnimals();
    }

    @Test
    public void getDnaAsset() throws Exception {
        DnaConfig dnaConfig = new DnaConfig("testConfig.json");
        dnaConfig.loadAnimals();
        dnaConfig.getDnaAsset("chicken", "color");
    }

    @Test
    public void positionFromNucleobaseIndex() throws Exception {
        DnaConfig dnaConfig = new DnaConfig("testConfig.json");
        int[] arrayZero = {0, 0};
        assertArrayEquals(arrayZero, dnaConfig.positionFromCodonIndex(0));
        int[] arraySix = {1, 0};
        assertArrayEquals(arraySix, dnaConfig.positionFromCodonIndex(6));
        int[] arrayThirtyFive = {4, 11};
        assertArrayEquals(arrayThirtyFive, dnaConfig.positionFromCodonIndex(35));
    }

    @Test
    public void getRelevantPositions() throws Exception {
        DnaConfig dnaConfig = new DnaConfig("testConfig.json");
        dnaConfig.loadAnimals();
        DnaAsset dnaAsset = dnaConfig.getDnaAsset("chicken", "color");
        int[][] positions = dnaAsset.getRelevantPositions();
        assertEquals(1, positions.length);
        int[] compareArray = {3, 2};
        assertTrue(Arrays.equals(compareArray, positions[0]));
    }

    @Test
    public void getPossibleAlleles() throws Exception {
        DnaConfig dnaConfig = new DnaConfig("testConfig.json");
        dnaConfig.loadAnimals();
        DnaAsset dnaAsset = dnaConfig.getDnaAsset("chicken", "color");
        int[] coordinates = {3, 2};
        System.out.println(Arrays.toString(dnaAsset.getPossibleCodonValuesOnPosition(coordinates)));
    }

    @Test
    public void getPossibleGenesOnPosition() throws Exception {
        DnaConfig dnaConfig = new DnaConfig("testConfig.json");
        dnaConfig.loadAnimals();
        DnaAsset dnaAsset = dnaConfig.getDnaAsset("chicken", "color");
        int[] coordinates = {3, 2};
        System.out.println(Arrays.toString(dnaAsset.getPossibleAllelesOnPosition(coordinates)));
    }

}