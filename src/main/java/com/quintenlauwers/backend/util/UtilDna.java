package com.quintenlauwers.backend.util;

import java.nio.ByteBuffer;

/**
 * Created by quinten on 10/08/16.
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

    public static boolean byteToBool(byte value){
        return (value != 0);
    }

    public static byte boolToByte(boolean value){
        return value ? (byte) 1: (byte) 0;
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
