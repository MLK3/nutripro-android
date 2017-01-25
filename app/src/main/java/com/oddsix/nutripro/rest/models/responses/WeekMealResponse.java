package com.oddsix.nutripro.rest.models.responses;

import java.util.List;

/**
 * Created by filippecl on 20/12/16.
 */

public class WeekMealResponse {
    private String status;
    private List<Nutrient> nutrients;

    public String getStatus() {
        return status;
    }

    public List<Nutrient> getNutrients() {
        return nutrients;
    }

    public class Nutrient {
        private String name;
        private String unit;
        private List<Quantity> quantities;
        private int min;
        private int max;

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

        public class Quantity {
            private String date;
            private int sum;

            public String getDate() {
                return date;
            }

            public int getSum() {
                return sum;
            }
        }
    }
}
