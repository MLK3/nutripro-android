package com.oddsix.nutripro.rest.models.responses;

import java.util.List;

/**
 * Created by filippecl on 21/12/16.
 */

public class AnalysedPictureResponse {
    private String status;
    private String picture_id;
    private String picture_url;
    private List<AnalysedRecognisedFoodResponse> foods;

    public String getStatus() {
        return status;
    }

    public String getPicture_id() {
        return picture_id;
    }

    public String getPictureUrl() {
        return picture_url;
    }

    public List<AnalysedRecognisedFoodResponse> getFoods() {
        return foods;
    }

}
