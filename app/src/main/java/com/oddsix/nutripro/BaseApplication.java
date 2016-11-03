package com.oddsix.nutripro;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.oddsix.nutripro.models.DayMealModel;
import com.oddsix.nutripro.models.DietModel;
import com.oddsix.nutripro.models.DietNutrientModel;
import com.oddsix.nutripro.models.MealFoodModel;
import com.oddsix.nutripro.models.MealModel;
import com.oddsix.nutripro.models.MealNutrientModel;
import com.oddsix.nutripro.models.RegisterModel;
import com.oddsix.nutripro.utils.Constants;

import java.util.Calendar;
import java.util.Date;

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
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name(Constants.BASE_DB_PATH)
                .initialData(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        //Create a register
                        RegisterModel model = new RegisterModel("nutripro@nutripro.com.br",
                                "123456",
                                "Nutripro", 12, "Masculino", 20, 90);
                        realm.copyToRealmOrUpdate(model);

                        //Create a diet
                        RealmList<DietNutrientModel> nutrients = new RealmList<DietNutrientModel>();
                        nutrients.add(new DietNutrientModel("Valor energético", 2000, 2500, "kcal"));
                        nutrients.add(new DietNutrientModel("Carboidratos", 300, 400, "g"));
                        nutrients.add(new DietNutrientModel("Proteínas", 75, 90, "g"));
                        nutrients.add(new DietNutrientModel("Gorduras Totais", 55, 80,  "g"));
                        nutrients.add(new DietNutrientModel("Gorduras Saturadas", 22, 30, "g"));
                        nutrients.add(new DietNutrientModel("Fibra Alimentar", 25, 40, "g"));
                        nutrients.add(new DietNutrientModel("Sódio", 2300, 2800, "mg"));
                        DietModel dietModel = new DietModel(nutrients, "Dieta Nutripro");
                        realm.copyToRealmOrUpdate(dietModel);

                        //Associate a diet to a register
                        model.setDietModel(dietModel);
                        realm.copyToRealmOrUpdate(model);

                        //Create a DayMeal
                        RealmList<MealNutrientModel> mealNutrients = new RealmList<>();
                        mealNutrients.add(new MealNutrientModel("Valor energético", 10, "kcal"));
                        mealNutrients.add(new MealNutrientModel("Carboidratos", 10, "g"));
                        mealNutrients.add(new MealNutrientModel("Proteínas", 10, "g"));
                        mealNutrients.add(new MealNutrientModel("Gorduras Totais", 5, "g"));
                        mealNutrients.add(new MealNutrientModel("Gorduras Saturadas", 1, "g"));
                        mealNutrients.add(new MealNutrientModel("Fibra Alimentar", 2, "g"));
                        mealNutrients.add(new MealNutrientModel("Sódio", 100, "mg"));

                        RealmList<MealFoodModel> mealFoods = new RealmList<MealFoodModel>();
                        mealFoods.add(new MealFoodModel(mealNutrients, "Arroz"));
                        mealFoods.add(new MealFoodModel(mealNutrients, "Feijão"));
                        mealFoods.add(new MealFoodModel(mealNutrients, "Carne"));

                        RealmList<MealModel> mealModels = new RealmList<MealModel>();
                        mealModels.add(new MealModel(mealFoods, "Almoço", "none"));
                        mealModels.add(new MealModel(mealFoods, "Café da Tarde", "none"));
                        mealModels.add(new MealModel(mealFoods, "Janta", "none"));

                        Calendar cal1 = Calendar.getInstance();
                        cal1.set(2016, 10, 2);
                        DayMealModel mealModel = new DayMealModel(cal1.getTime(), mealModels);
                        realm.copyToRealmOrUpdate(mealModel);
                        Calendar yesterday = Calendar.getInstance();
                        yesterday.set(2016, 10, 1);
                        realm.copyToRealmOrUpdate(new DayMealModel(yesterday.getTime(), mealModels));

                    }
                })
                .deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);
    }
}
