package com.oddsix.nutripro;

import android.app.Application;

import com.oddsix.nutripro.utils.Constants;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Filippe on 16/10/16.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration config = new RealmConfiguration.Builder(this).name(Constants.BASE_DB_PATH)
                .deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);
    }
}
