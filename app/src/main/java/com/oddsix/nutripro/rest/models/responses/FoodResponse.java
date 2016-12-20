package com.oddsix.nutripro.rest.models.responses;

import java.io.Serializable;

/**
 * Created by filippecl on 20/12/16.
 */

public class FoodResponse implements Serializable {
    String name;
    String id;

    public FoodResponse(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
