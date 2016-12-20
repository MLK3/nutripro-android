package com.oddsix.nutripro.models;

import android.graphics.Region;

import com.oddsix.nutripro.rest.models.responses.FoodResponse;

/**
 * Created by filippecl on 19/12/16.
 */

public class AreaModel {
    private Region mRegion;
    private FoodResponse mFood;

    public AreaModel(Region region, FoodResponse food) {
        mRegion = region;
        mFood = food;
    }

    public Region getRegion() {
        return mRegion;
    }

    public FoodResponse getFood() {
        return mFood;
    }
}
