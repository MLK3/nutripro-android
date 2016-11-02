package com.oddsix.nutripro.utils.validations;

/**
 * Created by Filippe on 21/10/16.
 */

public class HasMinLength {
    public static boolean isValid(String text, int minLength) {
        return text.length() >= minLength;
    }
}
