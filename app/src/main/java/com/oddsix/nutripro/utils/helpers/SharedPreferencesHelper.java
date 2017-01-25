package com.oddsix.nutripro.utils.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.oddsix.nutripro.utils.Constants;

/**
 * Created by mobile2you on 11/08/16.
 */
public class SharedPreferencesHelper {

    public static final String SHARED_PREFERENCES_NAME = Constants.PACKAGE_NAME + ".SHARED_PREFERENCES";

    private static final String PREF_SESSION_COOKIE = SHARED_PREFERENCES_NAME + ".PREF_SESSION_COOKIE";

    private SharedPreferences mSharedPreferences;

    private SharedPreferencesHelper(Context context) {
        mSharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferencesHelper sInstance;

    public static synchronized void initializeInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SharedPreferencesHelper(context);
        }
    }

    public static synchronized SharedPreferencesHelper getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(SharedPreferencesHelper.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return sInstance;
    }

    public void putSessionCookie(String sessionCookie){
        mSharedPreferences.edit().putString(PREF_SESSION_COOKIE, sessionCookie).apply();
    }

    public String getSessionCookie(){
        return mSharedPreferences.getString(PREF_SESSION_COOKIE, "");
    }

    public boolean isLogged(){
        return !getSessionCookie().isEmpty();
    }

    public void clearSharedPref(){
        mSharedPreferences.edit().clear().apply();
    }
}