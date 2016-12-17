package com.oddsix.nutripro.rest.models.responses;

/**
 * Created by filippecl on 17/12/16.
 */

public class NutrientResponse {
    private String name;
    private int max;
    private int min;
    private String unit;

    public String getUnit() {
        return unit;
    }

    public String getName() {
        return name;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }
}
