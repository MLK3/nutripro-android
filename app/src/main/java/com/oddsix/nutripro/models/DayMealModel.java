package com.oddsix.nutripro.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by filippecl on 02/11/16.
 */

public class DayMealModel extends RealmObject {
    private Date date;

    @PrimaryKey
    private String dateString;
    private RealmList<MealModel> meals;

    public DayMealModel(Date date, RealmList<MealModel> meals) {
        this.date = date;
        this.meals = meals;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateString = dateFormat.format(date);
    }

    public DayMealModel() {
    }

    public Date getDate() {
        return date;
    }

    public RealmList<MealModel> getMeals() {
        return meals;
    }
}
