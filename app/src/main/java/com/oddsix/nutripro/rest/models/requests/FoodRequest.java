package com.oddsix.nutripro.rest.models.requests;

public class FoodRequest {
    private String area_id;
    private String food_id;
    private int quantity;

    public FoodRequest(String area_id, String food_id, int quantity) {
        this.area_id = area_id;
        this.food_id = food_id;
        this.quantity = quantity;
    }
}