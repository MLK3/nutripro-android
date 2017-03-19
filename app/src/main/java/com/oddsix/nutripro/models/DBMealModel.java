package com.oddsix.nutripro.models;

import com.oddsix.nutripro.utils.Constants;
import com.oddsix.nutripro.utils.DateHelper;
import com.oddsix.nutripro.utils.helpers.SharedPreferencesHelper;

import java.text.ParseException;
import java.util.Calendar;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by filippecl on 02/11/16.
 */

public class DBMealModel extends RealmObject {
    @PrimaryKey
    private String primaryKey;

    private RealmList<DBMealFoodModel> foods = new RealmList<>();
    private String name;
    private String imagePath;

    public DBMealModel() {
    }

    public DBMealModel(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
        try {
            this.primaryKey = SharedPreferencesHelper.getInstance().getUserEmail() + DateHelper.parseDate(Constants.DB_DATE_FORMAT, Calendar.getInstance().getTime());
        } catch (ParseException e) {
            primaryKey = "";
            e.printStackTrace();
        }
    }

    public DBMealModel(String name, String imagePath, String primaryKey) {
        this.name = name;
        this.imagePath = imagePath;
        this.primaryKey = primaryKey;
    }


    public RealmList<DBMealFoodModel> getFoods() {
        return foods;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }
}
