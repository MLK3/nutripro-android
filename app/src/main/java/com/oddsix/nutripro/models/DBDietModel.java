package com.oddsix.nutripro.models;

import com.oddsix.nutripro.rest.models.responses.DietNutrientResponse;
import com.oddsix.nutripro.rest.models.responses.SuggestedDietResponse;
import com.oddsix.nutripro.utils.helpers.SharedPreferencesHelper;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Filippe on 22/10/16.
 */

public class DBDietModel extends RealmObject implements Serializable {
    @PrimaryKey
    private String email;

    private RealmList<DBDietNutrientModel> diet;

    public DBDietModel(SuggestedDietResponse dietResponse) {
        RealmList<DBDietNutrientModel> dbDietNutrientsModel = new RealmList<DBDietNutrientModel>();
        for (DietNutrientResponse nutrientResponse : dietResponse.getNutrients()) {
            dbDietNutrientsModel.add(new DBDietNutrientModel(nutrientResponse));
        }
        diet = dbDietNutrientsModel;
        email = SharedPreferencesHelper.getInstance().getUserEmail();
    }

    public DBDietModel() {
    }

    public RealmList<DBDietNutrientModel> getDiet() {
        return diet;
    }
}
