package com.oddsix.nutripro.rest.models.requests;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by filippecl on 21/12/16.
 */

public class CreateMealRequest {
    private String picture_id;
    private String meal_name;
    private List<FoodRequest> foods = new ArrayList<>();

    public CreateMealRequest(String picture_id, String meal_name, List<FoodRequest> foods) {
        this.picture_id = picture_id;
        this.meal_name = meal_name;
        this.foods = foods;
    }
}
