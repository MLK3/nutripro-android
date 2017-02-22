package com.oddsix.nutripro.models;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by filippecl on 02/11/16.
 */

public class DBMealModel extends RealmObject{
    private RealmList<DBMealFoodModel> foods = new RealmList<>();
    private String name;
    private String imagePath;

    public DBMealModel() {
    }

    public DBMealModel(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
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
}
