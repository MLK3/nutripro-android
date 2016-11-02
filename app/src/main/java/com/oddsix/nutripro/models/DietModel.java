package com.oddsix.nutripro.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Filippe on 22/10/16.
 */

public class DietModel extends RealmObject {
    @PrimaryKey
    private String name;

    private RealmList<NutrientModel> diet;

    public DietModel(RealmList<NutrientModel> diet, String name) {
        this.diet = diet;
        this.name = name;
    }

    public DietModel() {
    }

    public String getName() {
        return name;
    }

    public RealmList<NutrientModel> getDiet() {
        return diet;
    }
}
