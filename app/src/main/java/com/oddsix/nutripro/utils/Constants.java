package com.oddsix.nutripro.utils;

/**
 * Created by Filippe on 16/10/16.
 */

public class Constants {
    public static final String PACKAGE_NAME = "com.oddsix.nutripro";
    public static final String SHARED_PREF_NAME = PACKAGE_NAME;

    public static final String BASE_DB_PATH = PACKAGE_NAME + ".DB";

    //MOCK URL
    public static final String MOCK_URL = "https://private-1efe0b-nutripro.apiary-mock.com/";

    //PICTURE TO UPLOAD MAX SIZE
    public static final int PIC_UPLOAD_MAX_SIZE = 500;

    //DATE FORMATS
    public static final String STANDARD_DATE_FORMAT = "dd/MM/yyyy";
    public static final String REQUEST_DATE_FORMAT = "ddMMyyyy";

    //REQUEST CODES
    public final static int REQ_REGISTER = 0;
    public final static int REQ_EDIT_REGISTER = 1;
    public final static int REQ_SEARCH = 2;

    //EXTRA
    public final static String EXTRA_REGISTER_MODEL = "EXTRA_REGISTER_MODEL";
    public final static String EXTRA_BOOL_EDIT_REGISTER = "EXTRA_BOOL_EDIT_REGISTER";
    public final static String EXTRA_FOOD_MODEL = "EXTRA_FOOD_MODEL";
    public final static String EXTRA_MEAL_MODEL = "EXTRA_MEAL_MODEL";
    public final static String EXTRA_DIET = "EXTRA_DIET";

    //PREF
    public final static String PREF_MAIL = PACKAGE_NAME + ".PREF_MAIL";
    public final static String PREF_IS_LOGGED = PACKAGE_NAME + ".PREF_IS_LOGGED";
}
