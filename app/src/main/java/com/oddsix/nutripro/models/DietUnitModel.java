package com.oddsix.nutripro.models;

import io.realm.RealmObject;

/**
 * Created by Filippe on 22/10/16.
 */

public class DietUnitModel extends RealmObject {
    private int max;
    private int min;
    private int name;

    public DietUnitModel() {
    }

    public DietUnitModel(int name, int min, int max) {
        this.max = max;
        this.min = min;
        this.name = name;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public int getName() {
        return name;
    }
}
