package com.oddsix.nutripro.models;

import com.oddsix.nutripro.utils.helpers.SharedPreferencesHelper;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by filippecl on 21/02/17.
 */

public class DBAllMealsByDayModel extends RealmObject {

    @PrimaryKey
    private String email;

    private RealmList<DBDayMealModel> allDays;

    public DBAllMealsByDayModel(RealmList<DBDayMealModel> allDays) {
        this.allDays = allDays;
        this.email = SharedPreferencesHelper.getInstance().getUserEmail();
    }

    public DBAllMealsByDayModel() {
    }

    public String getEmail() {
        return email;
    }

    public RealmList<DBDayMealModel> getAllDays() {
        return allDays;
    }
}
