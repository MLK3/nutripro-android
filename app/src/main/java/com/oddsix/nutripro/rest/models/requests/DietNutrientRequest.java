package com.oddsix.nutripro.rest.models.requests;

/**
 * Created by filippecl on 21/12/16.
 */

public class DietNutrientRequest {
    private String name;
    private int max;
    private int min;

    public String getName() {
        return name;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public DietNutrientRequest(String name, int max, int min) {
        this.name = name;
        this.max = max;
        this.min = min;
    }
}
