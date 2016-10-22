package com.oddsix.nutripro.models;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Filippe on 22/10/16.
 */

public class DietModel extends RealmObject {
    @PrimaryKey
    private String name;

    private RealmList<DietUnitModel> diet;
}
