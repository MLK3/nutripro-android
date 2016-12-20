package com.oddsix.nutripro.rest.models.responses;

import java.io.Serializable;

/**
 * Created by filippecl on 20/12/16.
 */

public class FoodResponse implements Serializable {
    String name;
    String id;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
