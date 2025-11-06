package com.lucasmellof.bioforge.utils;

public class RomanNumeralUtil {
    private static final String[] ROMAN = {
        "", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"
    };

    public static String toRoman(int number) {
        if (number < 1 || number > 10)
            return String.valueOf(number);
        return ROMAN[number];
    }
}