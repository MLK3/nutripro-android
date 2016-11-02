package com.oddsix.nutripro.models;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Filippe on 22/10/16.
 */

public class DietModel extends RealmObject implements Serializable {
    @PrimaryKey
    private String name;

    private RealmList<DietNutrientModel> diet;

    public DietModel(RealmList<DietNutrientModel> diet, String name) {
        this.diet = diet;
        this.name = name;
    }

    public DietModel() {
    }

    public String getName() {
        return name;
    }

    public RealmList<DietNutrientModel> getDiet() {
        return diet;
    }
}
