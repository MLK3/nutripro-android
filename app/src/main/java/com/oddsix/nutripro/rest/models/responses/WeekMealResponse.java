package com.oddsix.nutripro.rest.models.responses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by filippecl on 20/12/16.
 */

public class WeekMealResponse {
    private String status;
    private List<NutrientWeekResponse> mNutrientResponses = new ArrayList<>();

    public String getStatus() {
        return status;
    }

    public List<NutrientWeekResponse> getNutrientResponses() {
        return mNutrientResponses;
    }
}
