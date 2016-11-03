package com.oddsix.nutripro.models;

import io.realm.RealmObject;

/**
 * Created by filippecl on 02/11/16.
 */

public class MealNutrientModel extends RealmObject {
    private String name;
    private int quantity;
    private String unit;

    public MealNutrientModel() {
    }

    public MealNutrientModel(String name, int quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }
}
