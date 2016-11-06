package com.oddsix.nutripro.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by filippecl on 06/11/16.
 */

public class MealModel implements Serializable {
    private List<MealModel> foods;
    private String name;
    private String imagePath;

    public MealModel(List<MealModel> foods, String name, String imagePath) {
        this.foods = foods;
        this.name = name;
        this.imagePath = imagePath;
    }

    public List<MealModel> getFoods() {
        return foods;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }
}
