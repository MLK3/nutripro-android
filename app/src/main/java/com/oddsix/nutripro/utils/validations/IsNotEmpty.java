package com.oddsix.nutripro.utils.validations;

/**
 * Created by Filippe on 21/10/16.
 */

public class IsNotEmpty {
    public static boolean isValid(String text) {
        return !text.isEmpty();
    }
}
