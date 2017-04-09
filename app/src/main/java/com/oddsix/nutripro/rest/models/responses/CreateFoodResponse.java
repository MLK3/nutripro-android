package com.oddsix.nutripro.rest.models.responses;

import java.util.List;

/**
 * Created by filippecl on 20/12/16.
 */

public class CreateFoodResponse {
    private String status;
    private String id;
    private Integer porcao_em_g;
    private List<NutrientResponse> nutrients;

    public List<NutrientResponse> getNutrients() {
        return nutrients;
    }

    public String getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public Integer getPorcao_em_g() {
        return porcao_em_g;
    }
}
