package com.oddsix.nutripro.rest.models.responses;

import com.oddsix.nutripro.models.DBMealFoodModel;
import com.oddsix.nutripro.models.DBMealModel;

import java.io.Serializable;
import java.util.ArrayList;
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

    public MealDetailResponse(DBMealModel mealModel) {
        this.name = mealModel.getName();
        this.pictureUrl = mealModel.getImagePath();
        foods = new ArrayList<>();
        for (DBMealFoodModel food: mealModel.getFoods()) {
            foods.add(new RecognisedFoodResponse(food));
        }
    }

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
