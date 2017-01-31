package com.oddsix.nutripro.rest.models.responses;

import java.util.List;

/**
 * Created by filippecl on 31/01/17.
 */

public class LoginResponse {
    private String status;
    private List<DietNutrientResponse> nutrients;
    private RegisterResponse profile;

    public String getStatus() {
        return status;
    }

    public List<DietNutrientResponse> getNutrients() {
        return nutrients;
    }

    public RegisterResponse getProfile() {
        return profile;
    }
}
