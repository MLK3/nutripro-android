package com.oddsix.nutripro;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.oddsix.nutripro.models.DBDayMealModel;
import com.oddsix.nutripro.models.DBDietModel;
import com.oddsix.nutripro.models.DBDietNutrientModel;
import com.oddsix.nutripro.models.DBMealFoodModel;
import com.oddsix.nutripro.models.DBMealModel;
import com.oddsix.nutripro.models.DBMealNutrientModel;
import com.oddsix.nutripro.models.DBRegisterModel;
import com.oddsix.nutripro.utils.Constants;
import com.oddsix.nutripro.utils.helpers.SharedPreferencesHelper;

import java.util.Calendar;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

/**
 * Created by Filippe on 16/10/16.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        SharedPreferencesHelper.initializeInstance(this);
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name(Constants.BASE_DB_PATH)
                .initialData(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        //Create a register
                        DBRegisterModel model = new DBRegisterModel("nutripro@nutripro.com.br",
                                "123456",
                                "Nutripro", 12, "Masculino", 20, 90);
                        realm.copyToRealmOrUpdate(model);

                        //Create a diet
                        RealmList<DBDietNutrientModel> nutrients = new RealmList<DBDietNutrientModel>();
                        nutrients.add(new DBDietNutrientModel("Valor energético", 2000, 2500, "kcal"));
                        nutrients.add(new DBDietNutrientModel("Carboidratos", 300, 400, "g"));
                        nutrients.add(new DBDietNutrientModel("Proteínas", 75, 90, "g"));
                        nutrients.add(new DBDietNutrientModel("Gorduras Totais", 55, 80,  "g"));
                        nutrients.add(new DBDietNutrientModel("Gorduras Saturadas", 22, 30, "g"));
                        nutrients.add(new DBDietNutrientModel("Fibra Alimentar", 25, 40, "g"));
                        nutrients.add(new DBDietNutrientModel("Sódio", 2300, 2800, "mg"));
                        DBDietModel dietModel = new DBDietModel(nutrients, "Dieta Nutripro");
                        realm.copyToRealmOrUpdate(dietModel);

                        //Associate a diet to a register
                        model.setDietModel(dietModel);
                        realm.copyToRealmOrUpdate(model);

                        //Create a DayMeal
                        RealmList<DBMealNutrientModel> mealNutrients = new RealmList<>();
                        mealNutrients.add(new DBMealNutrientModel("Valor energético", 10, "kcal"));
                        mealNutrients.add(new DBMealNutrientModel("Carboidratos", 10, "g"));
                        mealNutrients.add(new DBMealNutrientModel("Proteínas", 10, "g"));
                        mealNutrients.add(new DBMealNutrientModel("Gorduras Totais", 5, "g"));
                        mealNutrients.add(new DBMealNutrientModel("Gorduras Saturadas", 1, "g"));
                        mealNutrients.add(new DBMealNutrientModel("Fibra Alimentar", 2, "g"));
                        mealNutrients.add(new DBMealNutrientModel("Sódio", 100, "mg"));

                        RealmList<DBMealFoodModel> mealFoods = new RealmList<DBMealFoodModel>();
                        mealFoods.add(new DBMealFoodModel(mealNutrients, "Arroz", 80));
                        mealFoods.add(new DBMealFoodModel(mealNutrients, "Feijão", 100));
                        mealFoods.add(new DBMealFoodModel(mealNutrients, "Carne", 120));

                        RealmList<DBMealModel> mealModels = new RealmList<DBMealModel>();
                        mealModels.add(new DBMealModel(mealFoods, "Almoço", "none"));
                        mealModels.add(new DBMealModel(mealFoods, "Café da Tarde", "none"));
                        mealModels.add(new DBMealModel(mealFoods, "Janta", "none"));

                        Calendar cal1 = Calendar.getInstance();
                        cal1.set(2016, 10, 2);
                        DBDayMealModel mealModel = new DBDayMealModel(cal1.getTime(), mealModels);
                        realm.copyToRealmOrUpdate(mealModel);
                        Calendar yesterday = Calendar.getInstance();
                        yesterday.set(2016, 10, 1);
                        realm.copyToRealmOrUpdate(new DBDayMealModel(yesterday.getTime(), mealModels));

                    }
                })
                .deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);
    }
}
