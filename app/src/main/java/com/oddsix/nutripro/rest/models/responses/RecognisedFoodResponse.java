package com.oddsix.nutripro.rest.models.responses;

import java.io.Serializable;
import java.util.List;

/**
 * Created by filippecl on 17/12/16.
 */

public class RecognisedFoodResponse implements Serializable {
    private String id;
    private String name;
    private int quantity;
    private Area area;

    public Area getArea() {
        return area;
    }

    public RecognisedFoodResponse(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public class Area implements Serializable {
        private String area_id;
        private List<Point> points;

        public List<Point> getPoints() {
            return points;
        }

        public String getArea_id() {
            return area_id;
        }

        public class Point implements Serializable {
            float x;
            float y;

            public float getX() {
                return x;
            }

            public float getY() {
                return y;
            }
        }

    }
}
