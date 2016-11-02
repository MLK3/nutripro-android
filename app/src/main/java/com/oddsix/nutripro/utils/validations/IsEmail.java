package com.oddsix.nutripro.utils.validations;

/**
 * Created by Filippe on 21/10/16.
 */

public class IsEmail {

    private static final String EMAIL_PATTERN = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";

    public static boolean isValid(String text) {
        return text.matches(EMAIL_PATTERN);
    }

}
