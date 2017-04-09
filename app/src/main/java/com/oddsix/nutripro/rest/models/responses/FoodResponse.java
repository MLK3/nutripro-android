package com.oddsix.nutripro.rest.models.responses;

import android.content.Intent;

import java.io.Serializable;
import java.util.List;

/**
 * Created by filippecl on 20/12/16.
 */

public class FoodResponse implements Serializable {
    private String name;
    private String id;
    private Integer quantity;
    private List<NutrientResponse> nutrients;
    private Integer porcao_em_g;

    public Integer getPorcao_em_g() {
        return porcao_em_g;
    }

    public List<NutrientResponse> getNutrients() {
        return nutrients;
    }

    public FoodResponse(String name, String id, int quantity) {
        this.name = name;
        this.id = id;
        this.quantity = quantity;
    }

    public FoodResponse(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
