package com.oddsix.nutripro.models;

import com.oddsix.nutripro.rest.models.responses.RecognisedFoodResponse;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by filippecl on 21/02/17.
 */

public class DBAreaModel extends RealmObject{
    private String areaId;
    private RealmList<DBPoint> points = new RealmList<>();

    public DBAreaModel(String areaId, List<RecognisedFoodResponse.Area.Point> points) {
        this.areaId = areaId;
        for (RecognisedFoodResponse.Area.Point point: points) {
            this.points.add(new DBPoint(point.getX(), point.getY()));
        }
    }

    public DBAreaModel() {
    }

    public String getAreaId() {
        return areaId;
    }

    public RealmList<DBPoint> getPoints() {
        return points;
    }
}
