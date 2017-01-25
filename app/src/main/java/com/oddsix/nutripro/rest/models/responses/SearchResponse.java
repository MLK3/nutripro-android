package com.oddsix.nutripro.rest.models.responses;

import java.io.Serializable;
import java.util.List;

/**
 * Created by filippecl on 20/12/16.
 */

public class SearchResponse implements Serializable{
    private String status;
    private List<FoodResponse> foods;

    public String getStatus() {
        return status;
    }

    public List<FoodResponse> getFoods() {
        return foods;
    }
}
