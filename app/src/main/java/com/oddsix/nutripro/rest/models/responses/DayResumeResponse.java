package com.oddsix.nutripro.rest.models.responses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by filippecl on 17/12/16.
 */

public class DayResumeResponse {
    private String status;

    private List<NutrientResponse> nutrients = new ArrayList<>();
    private List<MealResponse> meals = new ArrayList<>();

    public class MealResponse implements Serializable {
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    public String getStatus() {
        return status;
    }

    public List<NutrientResponse> getNutrients() {
        return nutrients;
    }

    public List<MealResponse> getMeals() {
        return meals;
    }
}
