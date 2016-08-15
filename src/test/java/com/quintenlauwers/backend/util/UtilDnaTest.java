package com.quintenlauwers.backend.util;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by quinten on 15/08/16.
 */
public class UtilDnaTest {

    Random random = new Random();

    @org.junit.Test
    public void byteToInt() throws Exception {
        byte[] nul = {(byte) 0x0};
        assert UtilDna.byteToInt(nul) == 0;
        byte[] one = {(byte) 0, (byte) 0, (byte) 0, (byte) 0x1};
        assert UtilDna.byteToInt(one) == 1;
        byte[] two = {(byte) 0, (byte) 0, (byte) 0, (byte) 0x2};
        assert UtilDna.byteToInt(two) == 2;
        byte[] three = {(byte) 0, (byte) 0, (byte) 0, (byte) 0x3};
        assert UtilDna.byteToInt(three) == 3;
        byte[] hundred = {(byte) 0, (byte) 0, (byte) 0, (byte) 100};
        assert UtilDna.byteToInt(hundred) == 100;
        byte[] twohundred = {(byte) 0, (byte) 0, (byte) 0, (byte) 200};
        assert UtilDna.byteToInt(twohundred) == 200;
    }

    @org.junit.Test
    public void intToByte() throws Exception {
        byte[] nul = {(byte) 0, (byte) 0, (byte) 0, (byte) 0x0};
        assert Arrays.equals(UtilDna.intToByte(0), nul);
        byte[] one = {(byte) 0, (byte) 0, (byte) 0, (byte) 0x1};
        assert Arrays.equals(UtilDna.intToByte(1), one);
        byte[] two = {(byte) 0, (byte) 0, (byte) 0, (byte) 0x2};
        assert Arrays.equals(UtilDna.intToByte(2), two);
        byte[] three = {(byte) 0, (byte) 0, (byte) 0, (byte) 0x3};
        assert Arrays.equals(UtilDna.intToByte(3), three);
        byte[] hundred = {(byte) 0, (byte) 0, (byte) 0, (byte) 100};
        assert Arrays.equals(UtilDna.intToByte(100), hundred);
        byte[] twohundred = {(byte) 0, (byte) 0, (byte) 0, (byte) 200};
        assert Arrays.equals(UtilDna.intToByte(200), twohundred);
    }

    @org.junit.Test
    public void combinedIntTest() throws Exception {
        for (int i = 0; i < 200; i++) {
            int testint = random.nextInt();
            assert UtilDna.byteToInt(UtilDna.intToByte(testint)) == testint;
        }
    }

    @org.junit.Test
    public void combinedBoolTest() throws Exception {
        assert UtilDna.byteToBool(UtilDna.boolToByte(true));
        assertFalse(UtilDna.byteToBool(UtilDna.boolToByte(false)));
    }

    @org.junit.Test
    public void byteNucleobaseToString() throws Exception {
        for (int i = 0; i < 256; i++) {
            UtilDna.byteNucleobaseToString((byte) i);
        }
    }

    @org.junit.Test
    public void combinedNucleobaseTest() throws Exception {
        String[] testlist = {"ATG", "AAT", "CGT", "Atg", "CCg", "aaa"};
        for (String testcase : testlist) {
            assertEquals(UtilDna.byteNucleobaseToString(UtilDna.stringNucleobaseToByte(testcase)), testcase.toUpperCase());
        }
    }

}