package com.oddsix.nutripro.rest.models.requests;

/**
 * Created by filippecl on 20/12/16.
 */

public class NutrientRequest {
    private String name;
    private int quantity;

    public NutrientRequest(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}
