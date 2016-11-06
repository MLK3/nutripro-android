package com.oddsix.nutripro.models;

import io.realm.RealmObject;

/**
 * Created by filippecl on 02/11/16.
 */

public class DBMealNutrientModel extends RealmObject {
    private String name;
    private int quantity;
    private String unit;

    public DBMealNutrientModel() {
    }

    public DBMealNutrientModel(String name, int quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
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
