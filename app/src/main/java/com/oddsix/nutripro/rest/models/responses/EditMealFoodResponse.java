package com.oddsix.nutripro.rest.models.responses;

/**
 * Created by filippecl on 20/12/16.
 */

public class EditMealFoodResponse {
    private String area_id;
    private String food_id;
    private int quantity;

    public EditMealFoodResponse(String area_id, String food_id, int quantity) {
        this.area_id = area_id;
        this.food_id = food_id;
        this.quantity = quantity;
    }
}
