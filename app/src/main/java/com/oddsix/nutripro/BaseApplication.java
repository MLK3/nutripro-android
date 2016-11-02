package com.oddsix.nutripro;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.oddsix.nutripro.models.DietModel;
import com.oddsix.nutripro.models.NutrientModel;
import com.oddsix.nutripro.models.RegisterModel;
import com.oddsix.nutripro.utils.Constants;

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
                        RegisterModel model = new RegisterModel("nutripro@nutripro.com.br",
                                "123456",
                                "Nutripro", 12, "Masculino", 20, 90);
                        realm.copyToRealmOrUpdate(model);

                        RealmList<NutrientModel> nutrients = new RealmList<NutrientModel>();
                        nutrients.add(new NutrientModel("Valor energético", 2000, 2500, "kcal"));
                        nutrients.add(new NutrientModel("Carboidratos", 300, 400, "g"));
                        nutrients.add(new NutrientModel("Proteínas", 75, 90, "g"));
                        nutrients.add(new NutrientModel("Gorduras Totais", 55, 80,  "g"));
                        nutrients.add(new NutrientModel("Gorduras Saturadas", 22, 30, "g"));
                        nutrients.add(new NutrientModel("Fibra Alimentar", 25, 40, "g"));
                        nutrients.add(new NutrientModel("Sódio", 2300, 2800, "mg"));
                        DietModel dietModel = new DietModel(nutrients, "Dieta Nutripro");
                        model.setDietModel(dietModel);
                        realm.copyToRealmOrUpdate(model);
                    }
                })
                .deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);
    }
}
