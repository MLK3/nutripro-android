package com.oddsix.nutripro;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.oddsix.nutripro.utils.Constants;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

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
                        
                    }
                })
                .deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);
    }
}
