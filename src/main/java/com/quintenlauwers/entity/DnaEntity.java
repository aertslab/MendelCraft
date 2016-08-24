package com.quintenlauwers.entity;

/**
 * Created by quinten on 10/08/16.
 */
public interface DnaEntity {
    byte[] getDnaData();

    byte[] getDnaData2();

    void setDnaData(byte[] dnaData);

    void setDnaData(byte[] dnaData, byte[] dnaData2);

    String getAnimalName();
}
