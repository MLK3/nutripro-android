package com.oddsix.nutripro.models;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by filippecl on 02/11/16.
 */

public class DBDayMealModel extends RealmObject {
    private Date date;

    @PrimaryKey
    private String dateString;
    private RealmList<DBMealModel> meals;

    public DBDayMealModel(Date date, RealmList<DBMealModel> meals) {
        this.date = date;
        this.meals = meals;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateString = dateFormat.format(date);
    }

    public DBDayMealModel() {
    }

    public Date getDate() {
        return date;
    }

    public RealmList<DBMealModel> getMeals() {
        return meals;
    }
}
