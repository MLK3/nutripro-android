package com.oddsix.nutripro.rest.models.responses;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by filippecl on 02/03/17.
 */

public class NutrientWeekResponse {
    private String name;
    private String unit;
    private List<Quantity> quantities = new ArrayList<>();
    private int min;
    private int max;

    public NutrientWeekResponse(String name, String unit, int min, int max) {
        this.name = name;
        this.unit = unit;
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public List<Quantity> getQuantities() {
        return quantities;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public void addQuantity(Date date, int sum) {
        quantities.add(new Quantity(date, sum));
    }

    public class Quantity {
        private Date date;
        private int sum;

        public Quantity(Date date, int sum) {
            this.date = date;
            this.sum = sum;
        }

        public Date getDate() {
            return date;
        }

        public int getSum() {
            return sum;
        }
    }
}
