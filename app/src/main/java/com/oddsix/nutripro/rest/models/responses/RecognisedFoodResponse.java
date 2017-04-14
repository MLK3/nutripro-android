package com.oddsix.nutripro.rest.models.responses;

import com.oddsix.nutripro.models.DBAreaModel;
import com.oddsix.nutripro.models.DBDayMealModel;
import com.oddsix.nutripro.models.DBMealFoodModel;
import com.oddsix.nutripro.models.DBMealNutrientModel;
import com.oddsix.nutripro.models.DBPoint;
import com.oddsix.nutripro.models.NutrientModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by filippecl on 17/12/16.
 */

public class RecognisedFoodResponse implements Serializable {
    private String id;
    private String name;
    private int quantity;
    private int porcao_em_g;
    private Area area = new Area();
    private List<NutrientResponse> nutrients = new ArrayList<>();

    public RecognisedFoodResponse(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public RecognisedFoodResponse(String id, String name, int quantity, List<NutrientModel> nutrients, int portion) {
        this.id = id;
        this.name = name;
        this.porcao_em_g = portion;
        this.quantity = quantity;
        for (NutrientModel nModel: nutrients) {
            this.nutrients.add(new NutrientResponse(nModel.getName(), nModel.getQuantity(), nModel.getUnit()));
        }

    }

    public int getPortion() {
        return porcao_em_g;
    }

    public void setPorcaoEmG(int porcao_em_g) {
        this.porcao_em_g = porcao_em_g;
    }

    public RecognisedFoodResponse() {
    }

    public RecognisedFoodResponse(DBMealFoodModel food) {
        name = food.getFoodName();
        quantity = food.getQuantity();
        area = new Area(food.getArea());
        nutrients = new ArrayList<>();
        for (DBMealNutrientModel nutrientModel : food.getNutrients()) {
            nutrients.add(new NutrientResponse(nutrientModel));
        }
        porcao_em_g = food.getPortionInGrams();
    }

    public List<NutrientResponse> getNutrients() {
        return nutrients;
    }

    public Area getArea() {
        return area;
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
        private List<Point> points = new ArrayList<>();

        public Area() {
        }

        public Area(DBAreaModel areaModel) {
            area_id = areaModel.getAreaId();
            points = new ArrayList<>();
            for (DBPoint point : areaModel.getPoints()) {
                points.add(new Point(point));
            }
        }

        public List<Point> getPoints() {
            return points;
        }

        public String getArea_id() {
            return area_id;
        }

        public class Point implements Serializable {
            float x;
            float y;

            public Point(DBPoint point) {
                x = point.getX();
                y = point.getY();
            }

            public float getX() {
                return x;
            }

            public float getY() {
                return y;
            }
        }

    }
}
