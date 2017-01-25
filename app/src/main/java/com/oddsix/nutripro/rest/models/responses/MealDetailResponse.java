package com.oddsix.nutripro.rest.models.responses;

import java.io.Serializable;
import java.util.List;

/**
 * Created by filippecl on 17/12/16.
 */

public class MealDetailResponse implements Serializable{
    private String name;
    private List<RecognisedFoodResponse> foods;
    private List<NutrientResponse> nutrients;
    private String pictureUrl;
    private String meal_id;

    public String getMeal_id() {
        return meal_id;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getName() {
        return name;
    }

    public List<RecognisedFoodResponse> getFoods() {
        return foods;
    }

    public List<NutrientResponse> getNutrients() {
        return nutrients;
    }
}
