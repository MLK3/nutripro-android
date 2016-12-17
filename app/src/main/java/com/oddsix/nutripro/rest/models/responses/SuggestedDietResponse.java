package com.oddsix.nutripro.rest.models.responses;

import java.util.List;

/**
 * Created by filippecl on 17/12/16.
 */

public class SuggestedDietResponse {
    private String status;
    private List<DietNutrientResponse> nutrients;

    public String getStatus() {
        return status;
    }

    public List<DietNutrientResponse> getNutrients() {
        return nutrients;
    }
}
