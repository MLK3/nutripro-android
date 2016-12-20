package com.oddsix.nutripro.rest.models.requests;

import com.oddsix.nutripro.rest.models.requests.NutrientRequest;

import java.util.List;

/**
 * Created by filippecl on 20/12/16.
 */

public class RegisterFoodRequest {

    private String food_name;
    private List<NutrientRequest> nutrients;

    public RegisterFoodRequest(String food_name, List<NutrientRequest> nutrients) {
        this.food_name = food_name;
        this.nutrients = nutrients;
    }
}
