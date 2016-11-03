package com.oddsix.nutripro.models;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by filippecl on 02/11/16.
 */

public class MealFoodModel extends RealmObject {
    private RealmList<MealNutrientModel> nutrients;
    private String foodName;

    public MealFoodModel() {
    }

    public MealFoodModel(RealmList<MealNutrientModel> nutrients, String foodName) {
        this.nutrients = nutrients;
        this.foodName = foodName;
    }

    public RealmList<MealNutrientModel> getNutrients() {
        return nutrients;
    }

    public String getFoodName() {
        return foodName;
    }
}
