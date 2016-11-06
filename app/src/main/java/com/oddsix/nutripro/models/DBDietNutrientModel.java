package com.oddsix.nutripro.models;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by Filippe on 22/10/16.
 */

public class DBDietNutrientModel extends RealmObject implements Serializable {
    private int max;
    private int min;
    private String name;
    private String unit;

    public DBDietNutrientModel() {
    }

    public DBDietNutrientModel(String name, int min, int max, String unit) {
        this.max = max;
        this.min = min;
        this.name = name;
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public String getName() {
        return name;
    }
}
