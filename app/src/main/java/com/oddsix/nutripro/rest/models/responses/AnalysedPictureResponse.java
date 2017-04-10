package com.oddsix.nutripro.rest.models.responses;

import java.io.Serializable;
import java.util.List;

/**
 * Created by filippecl on 21/12/16.
 */

public class AnalysedPictureResponse implements Serializable {
    private String status;
    private String picture_id;
    private String picture_url;
    private String name;
    private List<AnalysedRecognisedFoodResponse> foods;

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getPicture_id() {
        return picture_id;
    }

    public String getPictureUrl() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public List<AnalysedRecognisedFoodResponse> getFoods() {
        return foods;
    }

}
