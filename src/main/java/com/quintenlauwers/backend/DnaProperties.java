package com.quintenlauwers.backend;

import com.quintenlauwers.backend.util.UtilDna;

/**
 * Created by quinten on 11/08/16.
 */
public class DnaProperties {
    boolean color;
    byte[] dnaData;
    String animal;

    public DnaProperties(String animal, byte[] dnaData) {
        if (dnaData != null) {
            this.animal = animal;
            this.color = UtilDna.byteToBool((byte) (dnaData[0] & (byte) 1));
            this.dnaData = dnaData.clone();
        }
    }

    public boolean getBoolProperty(String property) {
        return false;
    }

    public int getIntProperty(String property) {
        return 0;
    }

    public boolean getColor() {
        return this.color;
    }

    public byte[] getDnaData() {
        return dnaData.clone();
    }
}
