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
        System.out.println(integer);
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
}
