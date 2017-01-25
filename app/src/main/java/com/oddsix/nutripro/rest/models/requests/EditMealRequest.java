package com.oddsix.nutripro.rest.models.requests;

import com.oddsix.nutripro.rest.models.responses.EditMealFoodResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by filippecl on 20/12/16.
 */

public class EditMealRequest {
    private String meal_id;
    private String meal_name;
    private List<EditMealFoodResponse> foods;

    public List<EditMealFoodResponse> getFoods() {
        return foods;
    }

    public EditMealRequest(String meal_id, String meal_name) {
        this.meal_id = meal_id;
        this.meal_name = meal_name;
        foods = new ArrayList<>();
    }

}
