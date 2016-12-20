package com.oddsix.nutripro.rest.models.responses;

import java.io.Serializable;
import java.util.List;

/**
 * Created by filippecl on 20/12/16.
 */

public class FoodFromMealResponse {
    private String id;
    private String name;
    private int quantity;
    private List<NutrientResponse> nutrients;

    public List<NutrientResponse> getNutrients() {
        return nutrients;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }



}
