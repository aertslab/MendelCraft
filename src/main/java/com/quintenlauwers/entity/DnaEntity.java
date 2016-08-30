package com.quintenlauwers.entity;

import com.quintenlauwers.backend.DnaProperties;

/**
 * Created by quinten on 10/08/16.
 */
public interface DnaEntity {
    byte[] getDnaData();

    byte[] getDnaData2();

    void setDnaData(byte[] dnaData);

    void setDnaData(byte[] dnaData, byte[] dnaData2);

    String getAnimalName();

    DnaProperties getProperties();

    /**
     * Id that can be used with World.getEntityById(id).
     *
     * @return id
     */
    int getRegisteredId();

}
