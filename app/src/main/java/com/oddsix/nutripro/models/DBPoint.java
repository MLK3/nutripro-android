package com.oddsix.nutripro.models;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by filippecl on 21/02/17.
 */

public class DBPoint extends RealmObject {
    private float x;
    private float y;

    public DBPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public DBPoint() {
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
