package com.quintenlauwers.backend.util;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Random;

/**
 * Created by quinten on 10/08/16.
 *
 * Some utilities for manipulating DNA data
 */
public class UtilDna {
    /**
     * A function to create one big byte array from all the given byte arrays.
      */
    public static byte[] appendByteArrays (byte[]... array){
        int total_length = 0;
        for (byte[] anArray1 : array) {
            total_length += anArray1.length;
        }
        byte[] concatenated = new byte[total_length];
        int index_concatenated = 0;
        for (byte[] anArray : array) {
            for (byte anAnArray : anArray) {
                concatenated[index_concatenated] = anAnArray;
                index_concatenated++;

            }
        }
        return concatenated;
    }

    public static String getWeightedCodon(Map<String, Double> freqs) {
        double totWeight = 0.0d;
        for (Map.Entry<String, Double> freq : freqs.entrySet()) {
            totWeight += freq.getValue();
        }
        double r = new Random().nextDouble() * totWeight;
        double countWeight = 0.0;
        for (Map.Entry<String, Double> freq : freqs.entrySet()) {
            countWeight += freq.getValue();
            if (countWeight >= r) {
                return freq.getKey();
            }
        }
        return "";
    }


    public static int byteToInt(byte[] bytes) {
        int val = 0;
        for (byte aByte : bytes) {
            val <<= 8;
            val |= aByte & 0xFF;
        }
        return val;
    }

    public static byte[] intToByte(int integer){
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(integer);
        return buffer.array();
    }

    public static String byteNucleobaseToString(byte nucleobase) {
        String text = "";
        int tempNucleo = nucleobase & 0xFF;
        for (int i = 0; i < 3; i++) {
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

    public static byte stringNucleobaseToByte(String nucleobase) {
        int tempInt = 0;
        nucleobase = new StringBuilder(nucleobase).reverse().toString();
        for (int i = 0; i < 3; i++) {
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
