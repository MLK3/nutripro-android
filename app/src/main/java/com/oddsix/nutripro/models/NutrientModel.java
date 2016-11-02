package com.oddsix.nutripro.models;

import io.realm.RealmObject;

/**
 * Created by Filippe on 22/10/16.
 */

public class NutrientModel extends RealmObject {
    private int max;
    private int min;
    private String name;
    private String unit;

    public NutrientModel() {
    }

    public NutrientModel(String name, int min, int max, String unit) {
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
