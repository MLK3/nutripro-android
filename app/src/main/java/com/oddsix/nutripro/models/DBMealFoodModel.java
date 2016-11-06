package com.oddsix.nutripro.models;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by filippecl on 02/11/16.
 */

public class DBMealFoodModel extends RealmObject {
    private RealmList<DBMealNutrientModel> nutrients;
    private String foodName;

    public DBMealFoodModel() {
    }

    public DBMealFoodModel(RealmList<DBMealNutrientModel> nutrients, String foodName) {
        this.nutrients = nutrients;
        this.foodName = foodName;
    }

    public RealmList<DBMealNutrientModel> getNutrients() {
        return nutrients;
    }

    public String getFoodName() {
        return foodName;
    }
}
