package com.oddsix.nutripro.rest.models.responses;

import com.oddsix.nutripro.models.DBDietNutrientModel;

import java.io.Serializable;

/**
 * Created by filippecl on 17/12/16.
 */

public class DietNutrientResponse implements Serializable {
    private String name;
    private int max;
    private int min;
    private String unit;

    public DietNutrientResponse(String name, int max, int min, String unit) {
        this.name = name;
        this.max = max;
        this.min = min;
        this.unit = unit;
    }



    public DietNutrientResponse(DBDietNutrientModel dbDietNutrientModel) {
        max = dbDietNutrientModel.getMax();
        min = dbDietNutrientModel.getMin();
        name = dbDietNutrientModel.getName();
        unit = dbDietNutrientModel.getUnit();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    public String getName() {
        return name;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }
}
