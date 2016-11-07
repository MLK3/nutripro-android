package com.oddsix.nutripro.models;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Filippe on 22/10/16.
 */

public class DBDietModel extends RealmObject implements Serializable {
    @PrimaryKey
    private String name;

    private RealmList<DBDietNutrientModel> diet;

    public DBDietModel(RealmList<DBDietNutrientModel> diet, String name) {
        this.diet = diet;
        this.name = name;
    }

    public DBDietModel() {
    }

    public String getName() {
        return name;
    }

    public RealmList<DBDietNutrientModel> getDiet() {
        return diet;
    }
}
