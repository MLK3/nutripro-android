package com.oddsix.nutripro.rest.models.responses;

/**
 * Created by filippecl on 17/12/16.
 */

public class NutrientResponse {
    private String name;
    private int quantity;
    private String unit;

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
