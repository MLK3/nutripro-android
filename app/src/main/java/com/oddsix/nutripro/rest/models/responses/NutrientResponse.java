package com.oddsix.nutripro.rest.models.responses;

import com.oddsix.nutripro.models.DBMealNutrientModel;

import java.io.Serializable;

/**
 * Created by filippecl on 17/12/16.
 */

public class NutrientResponse implements Serializable{
    private String name;
    private int quantity;
    private String unity;

    public NutrientResponse(String name, int quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unity = unit;
    }

    public NutrientResponse(DBMealNutrientModel nutrientModel){
        name = nutrientModel.getName();
        quantity = nutrientModel.getQuantity();
        unity = nutrientModel.getUnit();
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
