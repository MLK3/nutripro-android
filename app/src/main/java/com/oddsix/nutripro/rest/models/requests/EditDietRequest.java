package com.oddsix.nutripro.rest.models.requests;

import com.oddsix.nutripro.rest.models.responses.DietNutrientResponse;
import com.oddsix.nutripro.rest.models.responses.WeekMealResponse;

import java.util.List;

/**
 * Created by filippecl on 21/12/16.
 */

public class EditDietRequest {
    private List<DietNutrientRequest> nutrients;

    public EditDietRequest(List<DietNutrientRequest> nutrients) {
        this.nutrients = nutrients;
    }
}
