package com.oddsix.nutripro.rest.models.responses;

import java.util.List;

/**
 * Created by filippecl on 17/12/16.
 */

public class MealDetailResponse {
    private String name;
    private List<FoodResponse> foods;
    private List<NutrientResponse> nutrients;
    private String pictureUrl;

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getName() {
        return name;
    }

    public List<FoodResponse> getFoods() {
        return foods;
    }

    public List<NutrientResponse> getNutrients() {
        return nutrients;
    }
}
