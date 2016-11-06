package com.oddsix.nutripro.models;

import java.io.Serializable;

/**
 * Created by filippecl on 06/11/16.
 */

public class NutrientModel implements Serializable {
    private String name;
    private int quantity;
    private String unit;

    public NutrientModel(String name, int quantity, String unit) {
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
