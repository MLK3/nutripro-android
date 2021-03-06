package com.oddsix.nutripro.models;

import android.graphics.Region;

import com.oddsix.nutripro.rest.models.responses.RecognisedFoodResponse;

/**
 * Created by filippecl on 19/12/16.
 */

public class AreaModel {
    private Region mRegion;
    private RecognisedFoodResponse mFood;
    private int mArrayIndex;

    public AreaModel(Region region, RecognisedFoodResponse food, int index) {
        mRegion = region;
        mFood = food;
        mArrayIndex = index;
    }

    public int getArrayIndex() {
        return mArrayIndex;
    }

    public Region getRegion() {
        return mRegion;
    }

    public RecognisedFoodResponse getFood() {
        return mFood;
    }
}
