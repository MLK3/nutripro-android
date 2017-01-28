package com.oddsix.nutripro.rest.models.responses;

import com.oddsix.nutripro.models.DBDietModel;
import com.oddsix.nutripro.models.DBDietNutrientModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by filippecl on 17/12/16.
 */

public class SuggestedDietResponse implements Serializable {
    private String status;
    private List<DietNutrientResponse> nutrients;

    public String getStatus() {
        return status;
    }

    public SuggestedDietResponse (DBDietModel dbDietModel) {
        for (DBDietNutrientModel nutrient:dbDietModel.getDiet()) {
            nutrients.add(new DietNutrientResponse(nutrient));
        }
    }

    public List<DietNutrientResponse> getNutrients() {
        return nutrients;
    }

}
