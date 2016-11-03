package com.oddsix.nutripro.models;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by filippecl on 02/11/16.
 */

public class MealModel extends RealmObject{
    private RealmList<MealFoodModel> foods;
    private String name;
    private String imagePath;

    public MealModel() {
    }

    public MealModel(RealmList<MealFoodModel> foods, String name, String imagePath) {
        this.foods = foods;
        this.name = name;
        this.imagePath = imagePath;
    }

    public RealmList<MealFoodModel> getFoods() {
        return foods;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }
}
